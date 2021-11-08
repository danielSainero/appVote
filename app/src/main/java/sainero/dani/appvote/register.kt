package sainero.dani.appvote

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import sainero.dani.appvote.databinding.ActivityLoginBinding
import sainero.dani.appvote.databinding.ActivityRegisterBinding


class register : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var  binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.registerPolicies.setOnClickListener{
            this.startActivity(Intent(this, privacyPolicies::class.java))
        }

        binding.btnRegister.setOnClickListener{

            if(TextUtils.isEmpty(binding.registerName.text.toString())){
                binding.registerName.setError("El nombre es obligatorio")
                return@setOnClickListener
            }

            if(TextUtils.isEmpty(binding.registerMail.text.toString())){
                binding.registerMail.setError("El mail es obligatorio")
                return@setOnClickListener
            }

            if(TextUtils.isEmpty(binding.registerPassword.text.toString())){
                binding.registerPassword.setError("La contraseña es obligatorio")
                return@setOnClickListener
            }

            if(TextUtils.isEmpty(binding.registerRepeatPassword.text.toString())){
                binding.registerRepeatPassword.setError("Repetir la contraseña es obligatorio")
                return@setOnClickListener

            } else if(!binding.registerPassword.text.toString().equals(binding.registerRepeatPassword.text.toString())) {
                binding.registerRepeatPassword.setError("La contraseña no coincide.")
                return@setOnClickListener
            }

            createUser(
                binding.registerMail.text.toString().trim(),
                binding.registerPassword.text.toString().trim()
            )
            }
        }


    private fun createUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                   // updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                }
            }
        /*

        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){ task: Task<AuthResult> ->
            if (task.isSuccessful) {
                Toast.makeText(this,"El usuario ha sido creado",Toast.LENGTH_LONG).show()
                val user = auth.currentUser
                //updateUI(user)
                reload()
            } else {
                Toast.makeText(this,"El usuario no ha podido ser creado",Toast.LENGTH_LONG).show()
                //updateUI(null)
            }
        }*/
    }

    private fun reload() {
        this.startActivity(Intent(this, MainActivity::class.java))
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            reload();
        }
    }

}

