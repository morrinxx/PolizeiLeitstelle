package at.htlleonding.policemobileclient

import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import at.htlleonding.policemobileclient.MQTT.connectMqttClient
import at.htlleonding.policemobileclient.MQTT.disconnectMqttClient
import at.htlleonding.policemobileclient.MQTT.publishLocation
import at.htlleonding.policemobileclient.MQTT.publishStatus
import at.htlleonding.policemobileclient.databinding.FragmentMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.fragment_main.*

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
}
