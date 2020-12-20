package at.htlleonding.policemobileclient.messaging

import android.content.Context
import android.util.Log
import at.htlleonding.policemobileclient.MainActivity
import at.htlleonding.policemobileclient.R
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
        message = "$id;${MainActivity.name};${MainActivity.missionDescription}"
    )
    val pushNotification = PushNotification(
        data = data,
        to = MainActivity.polizeiLeitstelle
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
    val message = "${MainActivity.location.latitude};${MainActivity.location.longitude};${MainActivity.name}"
    val data = NotificationData(
        title = "Location",
        message = message
    )
    val pushNotification = PushNotification(
        data = data,
        to = MainActivity.polizeiLeitstelle
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

fun sendInitial(context: Context) = CoroutineScope(Dispatchers.IO).launch {
    val res = context.resources.getStringArray(R.array.districtNames)
    val district = res[MainActivity.district]
    val message = "${res[MainActivity.district]};${MainActivity.name};${MyFirebaseMessagingService.token}"
    Log.d(TAG, message)
    val data = NotificationData(
        title = "Fahrzeug aktiv",
        message = message
    )
    Log.d(TAG, data.toString())
    val pushNotification = PushNotification(
        data = data,
        to = MainActivity.polizeiLeitstelle
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