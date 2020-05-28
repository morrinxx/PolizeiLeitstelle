package at.htlleonding.policemobileclient

import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import at.htlleonding.policemobileclient.MQTT.disconnectMqttClient
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.MqttException

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var locationManager : LocationManager
    companion object{
        private val LOG_TAG = this::class.java.toString()
        const val PREFERENCE_FILENAME = "PoliceMobileClientPreferences"
        const val DISTRICT_KEY = "DISTRICT_KEY"
        const val NAME_KEY = "NAME_KEY"
        const val UUID_KEY = "UUID_KEY"
        var district = -1
        var name = ""
        lateinit var mqttAndroidClient: MqttAndroidClient
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadPreferences()

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

//        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, )
//        Log.d("Location", location.longitude.toString())

        setContentView(R.layout.activity_main)
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

    fun loadPreferences() {
        val preferences = this.applicationContext
            .getSharedPreferences(PREFERENCE_FILENAME, Context.MODE_PRIVATE)
        district = preferences.getInt(DISTRICT_KEY, -1)
        name = preferences.getString(NAME_KEY, "Auto1")!!
        Log.d(LOG_TAG, "loaded preferences: $district    $name")
    }

    override fun onDestroy() {
        if(mqttAndroidClient.isConnected) disconnectMqttClient()
        super.onDestroy()
    }
}
