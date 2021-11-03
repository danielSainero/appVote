package sainero.dani.appvote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
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
                if(!binding.registerPassword.text.toString().equals(binding.registerRepeatPassword.text.toString())) {
                    binding.registerRepeatPassword.setError("La contraseña no coincide.")
                    return@setOnClickListener
                }
            }

            auth.createUserWithEmailAndPassword(binding.registerMail.text.toString(),binding.registerPassword.text.toString()).addOnCompleteListener(this){ task ->
                if (task.isSuccessful) {
                    Toast.makeText(this,"El usuario ha sido creado",Toast.LENGTH_LONG).show()
                    reload()
                } else {
                    Toast.makeText(this,"El usuario no ha podido ser creado",Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    private fun reload() {
        this.startActivity(Intent(this, MainActivity::class.java))
    }

}