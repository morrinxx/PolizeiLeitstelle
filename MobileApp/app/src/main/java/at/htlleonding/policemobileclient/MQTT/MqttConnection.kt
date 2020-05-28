package at.htlleonding.policemobileclient.MQTT

import android.content.Context
import android.util.Log
import at.htlleonding.policemobileclient.MainActivity
import at.htlleonding.policemobileclient.R
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import java.lang.Exception
import java.util.*

const val MQTT_BROKER = "tcp://broker.hivemq.com:1883"

fun connectMqttClient(applicationContext: Context){
    MainActivity.mqttAndroidClient = MqttAndroidClient ( applicationContext, MQTT_BROKER, getMqttClientId(applicationContext))
    val mqttOptions = MqttConnectOptions()
    mqttOptions.isAutomaticReconnect = true
    mqttOptions.isCleanSession = false
    try {
        MainActivity.mqttAndroidClient.connect(mqttOptions, null, object: IMqttActionListener{
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
}

fun disconnectMqttClient() {
    try {
        val disconToken = MainActivity.mqttAndroidClient.disconnect(0)
        disconToken.actionCallback = object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken) {
                //connectionStatus = false
                Log.d("Connection", "Disconnected")
            }
            override fun onFailure(
                asyncActionToken: IMqttToken,
                exception: Throwable
            ) {
                Log.d("Connection", "Disconnection Failed . . .")
            }
        }
    } catch (ex: MqttException) {
        Log.e("Connection", ex.toString())
    }
}

fun getMqttClientId(applicationContext: Context): String? {
    val preferences = applicationContext.getSharedPreferences(MainActivity.PREFERENCE_FILENAME, Context.MODE_PRIVATE)
    var uuid = preferences.getString(MainActivity.UUID_KEY, null)
    if(uuid == null){
        uuid = UUID.randomUUID().toString()
        val editor = preferences.edit()
        editor.putString(MainActivity.UUID_KEY, uuid)
        editor.apply()
    }
    return uuid
}

fun publishStatus(context: Context, statusId: Int){
    val data = """
        {"statusId": "$statusId", "description": ""}
    """.trimIndent()
    try {
        val encodedPayload = data.toByteArray(charset("UTF-8"))
        val message = MqttMessage(encodedPayload)
        message.qos = 0
        message.isRetained = false
        val topic = getTopicStatus(context)
        MainActivity.mqttAndroidClient.publish(topic, message)
        Log.d("Publish - Sent", "\nTopic: $topic\n $message")
    }
    catch (ex:MqttException){
        Log.e("Publish MQTT Exception", ex.toString())
    }
    catch (ex:Exception){
        Log.e("Publish Exception", ex.toString())
    }
}

fun getTopicStatus(context: Context): String {
    val res = context.resources.getStringArray(R.array.districtNames)
    val district = res[MainActivity.district]            //Get the District name with the district ID from MainActivity
    return "Leitstelle/$district/${MainActivity.name}/Status"

}
