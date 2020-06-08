package at.htlleonding.policemobileclient

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import at.htlleonding.policemobileclient.MQTT.publishStatus
import at.htlleonding.policemobileclient.databinding.FragmentMainBinding
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
        receiveMessages()
        binding.btMainStatus1.setOnClickListener { publishStatus(requireContext(), 1) }
        binding.btMainStatus2.setOnClickListener { publishStatus(requireContext(), 2) }
        binding.btMainStatus3.setOnClickListener { publishStatus(requireContext(), 3) }
        binding.btMainStatus4.setOnClickListener { publishStatus(requireContext(), 4) }
        binding.btMainStatus5.setOnClickListener { publishStatus(requireContext(), 5) }
        binding.btMainStatus6.setOnClickListener { publishStatus(requireContext(), 6) }
        binding.btMainStatus7.setOnClickListener { publishStatus(requireContext(), 7) }
        binding.btMainStatus8.setOnClickListener { publishStatus(requireContext(), 8) }

        return binding.root
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
