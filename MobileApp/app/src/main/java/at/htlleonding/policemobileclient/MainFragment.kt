package at.htlleonding.policemobileclient

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import at.htlleonding.policemobileclient.MainActivity.Companion.mqttAndroidClient
import at.htlleonding.policemobileclient.databinding.FragmentMainBinding
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttException
import java.util.*

class MainFragment : Fragment() {
    companion object{
        private val LOG_TAG = this.javaClass.simpleName
    }

    private var connectionStatus: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentMainBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main, container, false
        )
        //connectMqttClient(requireContext())
        return binding.root
    }

    private fun connectMqttClient(applicationContext: Context){
        mqttAndroidClient = MqttAndroidClient ( applicationContext,"broker.hivemq.com","testLMAO"/*"PoliceApp" + Date().toString()*/)
        try {
            val token = mqttAndroidClient.connect()
            token.actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken)                        {
                    Log.i("Connection", "success ")
                    connectionStatus = true
                    // Give your callback on connection established here
                }
                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    connectionStatus = false
                    Log.i("Connection", "failure")
                    // Give your callback on connection failure here
                    exception.printStackTrace()
                }
            }
        } catch (e: MqttException) {
            // Give your callback on connection failure here
            e.printStackTrace()
        }

    }

}
