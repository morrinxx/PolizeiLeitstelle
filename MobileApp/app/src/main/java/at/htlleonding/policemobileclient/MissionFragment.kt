package at.htlleonding.policemobileclient

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import at.htlleonding.policemobileclient.MQTT.publishLocation
import at.htlleonding.policemobileclient.databinding.FragmentMissionBinding
import kotlinx.android.synthetic.main.fragment_mission.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class MissionFragment : Fragment() {
    private lateinit var progressTimer: Timer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentMissionBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_mission, container, false
        )
        progressTimer = Timer()
        val progressTask = object: TimerTask() {
            override fun run(){
                if(pb_mission.progress == pb_mission.max){
                    this.cancel()
                    pb_mission.findNavController().navigate(R.id.action_missionFragment_to_mainFragment)
                }
                pb_mission.progress += 10
            }
        }
        progressTimer.schedule(progressTask, 0, 10)

        return binding.root
    }
}
