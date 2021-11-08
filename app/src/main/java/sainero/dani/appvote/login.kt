package sainero.dani.appvote

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import sainero.dani.appvote.databinding.ActivityLoginBinding
import kotlin.math.log

class login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var  binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.loginTextRegister.setOnClickListener{
            this.startActivity(
               Intent(this,register::class.java)
           )
        }

        binding.signInBtn.setOnClickListener{
            //if (binding.signInEmail.text.toString().isNotEmpty() || binding.signInPassword.text.toString().isNotEmpty()){}
            signIn(
                binding.signInEmail.text.toString(),
                binding.signInPassword.text.toString()
            )
        }
        binding.loginNewPassword.setOnClickListener{
            this.startActivity(
                Intent(this,NewPassword::class.java)
            )
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("AccesoFirebase", "signInWithEmail:success")
                    val user = auth.currentUser
                    reload()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("AccesoFirebase", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val user = auth.currentUser
        if(user != null){
            user?.let {
                val name = user.displayName
                val email = user.email
                val photoUrl = user.photoUrl
                val emailVerified = user.isEmailVerified
                val uid = user.uid
            }
            /*
            val emailAddress = "user@example.com"

            auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "Email sent.")
                        }
                    }


            user!!.sendEmailVerification()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Email sent.")
                    }
                }*/
            reload();
        }
    }

    private fun reload() {
        this.startActivity(Intent(this, MainActivity::class.java))
    }
}