package at.htlleonding.policemobileclient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import at.htlleonding.policemobileclient.MQTT.publishStatus
import at.htlleonding.policemobileclient.databinding.FragmentMissionBinding
import java.util.*

class MissionFragment : Fragment() {
    private lateinit var progressTimer: Timer
    private var accepted = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentMissionBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_mission, container, false
        )
        binding.tvMissionDescription.text = MainActivity.missionDescription
        binding.btMissionAccept.setOnClickListener {
            accepted = true
            publishStatus(requireContext(), 3)
            Toast.makeText(activity, getString(R.string.mission_acceptedMission), Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_missionFragment_to_mainFragment)
        }
        progressTimer = Timer()
        val progressTask = object: TimerTask() {
            override fun run(){
                if(binding.pbMission.progress == binding.pbMission.max){
                    if(!accepted) {
                        findNavController().navigate(R.id.action_missionFragment_to_mainFragment)
                    }
                    this.cancel()
                }
                binding.pbMission.progress += 10
            }
        }
        progressTimer.schedule(progressTask, 0, 10)

        return binding.root
    }
}
