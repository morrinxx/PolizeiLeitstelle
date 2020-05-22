package at.htlleonding.policemobileclient

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.databinding.DataBindingUtil
import at.htlleonding.policemobileclient.databinding.FragmentAuthenicationBinding
import kotlinx.android.synthetic.main.fragment_authenication.*
import java.util.concurrent.Executors


class AuthenicationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentAuthenicationBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_authenication, container, false
        )
        binding.btAuthenticationLogin.setOnClickListener { MainActivity.bP.authenticate(MainActivity.promptInfo) }
        Log.d("AuthenticationFragment", "TEST")
//        bt_authentication_login.setOnClickListener {
//            MainActivity.bP.authenticate(MainActivity.promptInfo)
//        }
        return binding.root
    }
}
