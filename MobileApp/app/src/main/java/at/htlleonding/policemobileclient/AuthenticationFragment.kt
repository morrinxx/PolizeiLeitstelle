package at.htlleonding.policemobileclient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import at.htlleonding.policemobileclient.databinding.FragmentAuthenicationBinding
import kotlinx.android.synthetic.main.fragment_authenication.*
import java.util.concurrent.Executors


class AuthenticationFragment : Fragment() {
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
        return binding.root
    }
}
