package at.htlleonding.policemobileclient.messaging

import android.util.Log
import at.htlleonding.policemobileclient.MainActivity
import at.htlleonding.policemobileclient.model.NotificationData
import at.htlleonding.policemobileclient.model.PushNotification
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val STATUS_TOPIC = "/topics/status"
const val MISSION_TOPIC = "/topics/einsatz"
const val LOCATION_TOPIC = "/topics/location"
const val TAG = "FCM"

fun subscribe(){
    Firebase.messaging.subscribeToTopic(MISSION_TOPIC)
        .addOnCompleteListener{ task ->
            var msg = "successfully Subscribed!"
            if (!task.isSuccessful) {
                Log.e(TAG, task.exception?.message.toString())
                msg = "Subscribing failed!"
            }
            Log.d(TAG, msg)
        }

    Firebase.messaging.subscribeToTopic(STATUS_TOPIC)
        .addOnCompleteListener{ task ->
            var msg = "successfully Subscribed!"
            if (!task.isSuccessful) {
                Log.e(TAG, task.exception?.message.toString())
                msg = "Subscribing failed!"
            }
            Log.d(TAG, msg)
        }

    /*
    Firebase.messaging.subscribeToTopic(LOCATION_TOPIC)
        .addOnCompleteListener{ task ->
            var msg = "successfully Subscribed!"
            if (!task.isSuccessful) {
                Log.e(TAG, task.exception?.message.toString())
                msg = "Subscribing failed!"
            }
            Log.d(TAG, msg)
        }*/
}

fun sendStatus(id: Int) = CoroutineScope(Dispatchers.IO).launch {
    val data = NotificationData(
        title = "Status",
        message = id.toString()
    )
    val pushNotification = PushNotification(
        data = data,
        to = STATUS_TOPIC
    )
    try {
        val response = RetrofitInstance.api.postNotification(pushNotification)
        if(response.isSuccessful){
            Log.d(
                TAG, "Response: ${
                Gson().toJson(response)}")
        }
        else{
            Log.e(TAG, response.errorBody().toString())
        }
    } catch(e: Exception){
        Log.e(TAG, e.toString())
    }
}
fun sendLocation() = CoroutineScope(Dispatchers.IO).launch {
    val message = """
        {"x-coordinate": "${MainActivity.location.latitude}", "y-coordinate": "${MainActivity.location.longitude}"}
    """.trimIndent()
    val data = NotificationData(
        title = "Location",
        message = message
    )
    val pushNotification = PushNotification(
        data = data,
        to = LOCATION_TOPIC
    )
    try {
        val response = RetrofitInstance.api.postNotification(pushNotification)
        if(response.isSuccessful){
            Log.d(
                TAG, "Response: ${
                    response.code()}")
        }
        else{
            Log.e(TAG, response.errorBody().toString())
        }
    } catch(e: Exception){
        Log.e(TAG, e.toString())
    }
}

fun unsubscribe() {
    Log.d(TAG,"Unsubscribing ...")
    Firebase.messaging.unsubscribeFromTopic(STATUS_TOPIC).isSuccessful
    Firebase.messaging.unsubscribeFromTopic(LOCATION_TOPIC).isSuccessful
    Firebase.messaging.unsubscribeFromTopic(MISSION_TOPIC).isSuccessful
}