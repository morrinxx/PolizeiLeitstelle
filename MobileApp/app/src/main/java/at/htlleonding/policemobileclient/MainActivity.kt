package at.htlleonding.policemobileclient

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import at.htlleonding.policemobileclient.MQTT.connectMqttClient
import at.htlleonding.policemobileclient.MQTT.disconnectMqttClient
import at.htlleonding.policemobileclient.MQTT.publishLocation
import at.htlleonding.policemobileclient.messaging.sendLocation
import at.htlleonding.policemobileclient.messaging.unsubscribe
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import org.eclipse.paho.android.service.MqttAndroidClient
import java.beans.PropertyChangeSupport
import java.util.*
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

class MainActivity : AppCompatActivity() {

    companion object{
        private val LOG_TAG = this::class.java.toString()
        const val PREFERENCE_FILENAME = "PoliceMobileClientPreferences"
        const val DISTRICT_KEY = "DISTRICT_KEY"
        const val NAME_KEY = "NAME_KEY"
        const val UUID_KEY = "UUID_KEY"
        const val MISSION_SUB_KEY = "MISSION_SUB_KEY"
        var district = -1
        var name = ""
        var missionDescription = "Kein Einsatz"
        const val polizeiLeitstelle = "f2mHBdkwTQg0rDysY3sJ-T:APA91bGOjE1BBuyV981Fw_2ayltJX19mgHpQsyujpcn0XcruYkDaXq-7V13az_tnNiJ6FzbfckJyZzjCkmWe1xz6q2_QpKRVTU-pGNdNpg3Rx9I8j12THwl3Dd7eoYe93wyjmpetXKR8"
        lateinit var mqttAndroidClient: MqttAndroidClient
        lateinit var location: Location
        var SEND_LOCATION = false
        lateinit var mAuth: FirebaseAuth
    }

    private val PERMISSION_ID = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadPreferences()
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()
        val sendLocationTimer = Timer()
        val sendLocationTask = object: TimerTask() {
            override fun run(){
                if(SEND_LOCATION){
                    getLastLocation()
                    sendLocation()
                }
            }
        }
        sendLocationTimer.schedule(sendLocationTask, 10000, 3000)
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val currentLocation: Location? = task.result
                    requestNewLocationData()
//                    if (currentLocation == null) {
//                        requestNewLocationData()
//                    } else {
//                        Log.d("Location", "${currentLocation.latitude} ${currentLocation.longitude}")
//                        location = currentLocation
//                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }
    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            Log.d("Location", "new location: ${mLastLocation.longitude.toString()} ${mLastLocation.latitude.toString()}")
            location = mLastLocation
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId){
            R.id.menu_item_quit -> {
                finish()
                true
            }
            R.id.menu_item_settings ->{
                Log.d("MainActivity", "Setting menu called")
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    private fun loadPreferences() {
        val preferences = this.applicationContext
            .getSharedPreferences(PREFERENCE_FILENAME, Context.MODE_PRIVATE)
        district = preferences.getInt(DISTRICT_KEY, -1)
        name = preferences.getString(NAME_KEY, "Auto1")!!
        Log.d(LOG_TAG, "loaded preferences: $district    $name")
    }


    override fun onDestroy() {
        super.onDestroy()
        unsubscribe()
        if(mqttAndroidClient.isConnected) disconnectMqttClient()
    }
}
