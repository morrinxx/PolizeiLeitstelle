package at.htlleonding.policemobileclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.navigation.NavController
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_authenication.*
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    companion object{
        lateinit var bP: BiometricPrompt
        lateinit var promptInfo: BiometricPrompt.PromptInfo
    }

    private lateinit var navController: NavController
    private val executor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bP = BiometricPrompt(this, executor, object: BiometricPrompt.AuthenticationCallback(){
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                runOnUiThread {
                    Toast.makeText(this@MainActivity, getString(R.string.main_successfulAuthentication), Toast.LENGTH_LONG).show()
                }

            }


            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                if(errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON){
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, getString(R.string.main_authenticationError), Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onAuthenticationFailed() {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, getString(R.string.main_authenticationFailed), Toast.LENGTH_LONG).show()
                }
            }
        })
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.authenticate))
            .setNegativeButtonText(getString(R.string.cancel))
            .build()
    }
}
