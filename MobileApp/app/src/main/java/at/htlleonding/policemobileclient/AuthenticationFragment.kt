package at.htlleonding.policemobileclient

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import at.htlleonding.policemobileclient.databinding.FragmentAuthenicationBinding
import at.htlleonding.policemobileclient.messaging.RetrofitInstance
import at.htlleonding.policemobileclient.model.NotificationData
import at.htlleonding.policemobileclient.model.PushNotification
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import com.google.firebase.messaging.ktx.remoteMessage
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_authenication.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors


class AuthenticationFragment : Fragment() {
    companion object{
        val TAG = this::class.java.toString()
    }
    private val executor = Executors.newSingleThreadExecutor()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentAuthenicationBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_authenication, container, false
        )
        val bP = BiometricPrompt(this, executor, object: BiometricPrompt.AuthenticationCallback(){
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                activity!!.runOnUiThread {
                    Toast.makeText(activity, getString(R.string.main_successfulAuthentication), Toast.LENGTH_LONG).show()
                }
                bt_authentication_login.findNavController().navigate(R.id.action_authenicationFragment_to_mainFragment)
            }


            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                if(errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON){
                    activity!!.runOnUiThread {
                        Toast.makeText(activity, getString(R.string.main_authenticationError), Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onAuthenticationFailed() {
                activity!!.runOnUiThread {
                    Toast.makeText(activity, getString(R.string.main_authenticationFailed), Toast.LENGTH_LONG).show()
                }
            }
        })
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.authenticate))
            .setNegativeButtonText(getString(R.string.cancel))
            .build()

        binding.btAuthenticationLogin.setOnClickListener { bP.authenticate(promptInfo) }
        binding.btSubscribe.setOnClickListener{
            Firebase.messaging.subscribeToTopic("topic_weather")
                .addOnCompleteListener{ task ->
                    var msg = "successfully Subscribed!"
                    if (!task.isSuccessful) {
                        Log.e("FCM", task.exception?.message.toString())
                        msg = "Subscribing failed!"
                    }
                    Log.d("FCM", msg)
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }
        }
        binding.btLogToken.setOnClickListener {
            // Get token
            // [START log_reg_token]
            Firebase.messaging.token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result
                MyFirebaseMessagingService.token = token
                // Log and toast
                Log.d("FCM", "Token: $token")
                Toast.makeText(context, "Token: $token", Toast.LENGTH_SHORT).show()
            })

        }
        binding.btSend.setOnClickListener{
            PushNotification(
                NotificationData("TestTitle", "TestMessage"), "topic_weather"
            ).also{
                sendNotification(it)
            }
        }

        return binding.root
    }
    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful){
                Log.d("FCM", "Response: ${
                    Gson().toJson(response)}")
            }
            else{
                Log.e("FCM", response.errorBody().toString())
            }
        } catch(e: Exception){
            Log.e("FCM", e.toString())
        }
    }
}
