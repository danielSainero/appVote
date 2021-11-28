package sainero.dani.appvote

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import sainero.dani.appvote.databinding.ActivityLoginBinding
import sainero.dani.appvote.databinding.ActivityNewPasswordBinding

class NewPassword : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var  binding: ActivityNewPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        val user = auth.currentUser
        binding.btnValidarNewPassword.setOnClickListener{

            if (binding.newPasswordEmail.text.toString().isEmpty()) {
                binding.newPasswordEmail.setError("El valor no puede ser nulo")
                return@setOnClickListener
            }

          val emailAddress = binding.newPasswordEmail.text.toString()

          auth.sendPasswordResetEmail(emailAddress)
                  .addOnCompleteListener { task ->
                      if (task.isSuccessful)
                          Toast.makeText(baseContext, "La contraseña ha sido cambiada", Toast.LENGTH_SHORT).show()

                  }
        }
    }
}