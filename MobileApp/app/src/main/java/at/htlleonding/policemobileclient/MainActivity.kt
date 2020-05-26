package at.htlleonding.policemobileclient

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.navigation.NavController
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.fragment_authenication.*
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.MqttAsyncClient.generateClientId
import org.eclipse.paho.client.mqttv3.MqttClient.generateClientId
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    companion object{
        private val LOG_TAG = this::javaClass.toString()
        const val PREFERENCE_FILENAME = "PoliceMobileClientPreferences"
        const val DISTRICT_KEY = "DISTRICT_KEY"
        const val NAME_KEY = "NAME_KEY"
        var district = -1
        var name = ""
        lateinit var mqttAndroidClient: MqttAndroidClient
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectMqttClient(applicationContext)
        loadPreferences()

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

    private fun connectMqttClient(applicationContext: Context){
        mqttAndroidClient = MqttAndroidClient ( applicationContext,"tcp://broker.hivemq.com:1883","testLMAOasdfs"/*"PoliceApp" + Date().toString()*/)
        val mqttOptions = MqttConnectOptions()
        mqttOptions.isAutomaticReconnect = true
        mqttOptions.isCleanSession = false
        try {
            mqttAndroidClient.connect(mqttOptions, null, object: IMqttActionListener{
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d("Connection", "Success")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d("Connection", "Failure, \n" + exception?.message + " \n\n\n" + exception?.toString())
                }
            })
        }catch (ex: MqttException){
            ex.printStackTrace()
        }
        /*try {
            val token = mqttAndroidClient.connect()
            token.actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken)                        {
                    Log.i("Connection", "success ")
                    //connectionStatus = true
                    // Give your callback on connection established here
                }
                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    //connectionStatus = false
                    Log.i("Connection", "failure")
                    // Give your callback on connection failure here
                    exception.printStackTrace()
                }
            }
        } catch (e: MqttException) {
            // Give your callback on connection failure here
            e.printStackTrace()
        }*/

    }
    private fun loadPreferences() {
        val preferences = this.applicationContext
            .getSharedPreferences(PREFERENCE_FILENAME, Context.MODE_PRIVATE)
        district = preferences.getInt(DISTRICT_KEY, -1)
        name = preferences.getString(NAME_KEY, "Auto1")!!
        Log.d(LOG_TAG, "loaded preferences: $district    $name")
    }
}
