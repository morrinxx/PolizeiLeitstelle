package at.htlleonding.policemobileclient

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import at.htlleonding.policemobileclient.databinding.FragmentAuthenicationBinding
import at.htlleonding.policemobileclient.messaging.MyFirebaseMessagingService
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.fragment_authenication.*
import java.util.concurrent.Executors

class AuthenticationFragment : Fragment() {
    companion object{
        val TAG = this::class.java.toString()
        private const val RC_SIGN_IN = 120
    }
    private val executor = Executors.newSingleThreadExecutor()
    private lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentAuthenicationBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_authenication, container, false
        )
        MainActivity.SEND_LOCATION = false

        MyFirebaseMessagingService.sharedPref = activity?.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            MyFirebaseMessagingService.token = it.token
        }

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        MainActivity.mAuth = FirebaseAuth.getInstance()

        binding.btAuthenticationLogin.setOnClickListener{
            if(MainActivity.district != -1){
                signIn()
            }
            else{
                requireActivity().runOnUiThread {
                    Toast.makeText(activity, "Fahrzeugname und Bezirk auswählen!", Toast.LENGTH_LONG).show()
                }
            }
        }

        return binding.root
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if(task.isSuccessful){
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(TAG, "Google sign in failed", e)
                    // ...
                }
            }else{
                Log.d(TAG, exception.toString())
            }

        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        MainActivity.mAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = MainActivity.mAuth.currentUser
                    Log.d(TAG, user.toString())
                    findNavController().navigate(R.id.action_authenicationFragment_to_mainFragment)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }
}
