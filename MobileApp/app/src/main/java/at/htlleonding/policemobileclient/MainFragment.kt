package at.htlleonding.policemobileclient

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import at.htlleonding.policemobileclient.databinding.FragmentMainBinding
import at.htlleonding.policemobileclient.messaging.MyFirebaseMessagingService
import at.htlleonding.policemobileclient.messaging.sendStatus
import at.htlleonding.policemobileclient.messaging.subscribe
import com.google.firebase.messaging.ktx.messaging
import com.google.gson.Gson
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage

data class MessageDto(val id: Int, val description: String)

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentMainBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main, container, false
        )
        subscribe()
        binding.btMainStatus1.setOnClickListener { sendStatus( 1) }
        binding.btMainStatus2.setOnClickListener { sendStatus( 2) }
        binding.btMainStatus3.setOnClickListener { sendStatus( 3) }
        binding.btMainStatus4.setOnClickListener { sendStatus( 4) }
        binding.btMainStatus5.setOnClickListener { sendStatus( 5) }
        binding.btMainStatus6.setOnClickListener { sendStatus( 6) }
        binding.btMainStatus7.setOnClickListener { sendStatus( 7) }
        binding.btMainStatus8.setOnClickListener { sendStatus( 8) }

        return binding.root
    }

    fun newMissionArrived(description: String){
        MainActivity.missionDescription = description
        findNavController().navigate(R.id.action_mainFragment_to_missionFragment)
    }

    private fun receiveMessages() {
        MainActivity.mqttAndroidClient.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable) {
                Log.e("Connection", "Lost MQTT Connection")
            }
            override fun messageArrived(topic: String, message: MqttMessage) {
                try {
                    val data = String(message.payload, charset("UTF-8"))
                    Log.d("Subscribe", "Message arrived: topic: $topic    payload: $data")
                    val jsonObject = Gson().fromJson(data, MessageDto::class.java)
                    MainActivity.missionDescription = jsonObject.description
                    findNavController().navigate(R.id.action_mainFragment_to_missionFragment)

                } catch (e: Exception) {
                    Log.e("Connection", "Exception on MQTT receiver: $e")
                }
            }
            override fun deliveryComplete(token: IMqttDeliveryToken) {
                // Acknowledgement on delivery complete
            }
        })
    }
}
