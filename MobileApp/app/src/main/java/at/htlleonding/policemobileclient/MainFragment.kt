package at.htlleonding.policemobileclient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import at.htlleonding.policemobileclient.MQTT.connectMqttClient
import at.htlleonding.policemobileclient.MQTT.disconnectMqttClient
import at.htlleonding.policemobileclient.MQTT.publishStatus
import at.htlleonding.policemobileclient.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    companion object{
        private val LOG_TAG = this::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentMainBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main, container, false
        )
        connectMqttClient(applicationContext = requireContext())

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



    override fun onDestroy() {
        disconnectMqttClient()
        super.onDestroy()
    }
}
