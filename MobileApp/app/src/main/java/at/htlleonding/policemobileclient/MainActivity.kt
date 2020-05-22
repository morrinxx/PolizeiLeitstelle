package at.htlleonding.policemobileclient

import android.content.Context
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

    private lateinit var navController: NavController
    companion object{
        fun showToast(context: Context?, text:String){
            Toast.makeText(context, text, Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
