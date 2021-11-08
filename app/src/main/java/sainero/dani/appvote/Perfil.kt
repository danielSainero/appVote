package sainero.dani.appvote

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import sainero.dani.appvote.databinding.ActivityLoginBinding
import sainero.dani.appvote.databinding.ActivityNewPasswordBinding
import sainero.dani.appvote.databinding.ActivityPerfilBinding

class Perfil : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var  binding: ActivityPerfilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        val user = auth.currentUser
        binding.btnPerfilModificarContra.setOnClickListener {
            this.startActivity(Intent(this, NewPassword::class.java))
        }
        binding.btnPerfilActualizarDatos.setOnClickListener{
            val profileUpdates = userProfileChangeRequest {
                displayName = binding.perfilNombre.text.toString()
                photoUri = Uri.parse("https://example.com/jane-q-user/profile.jpg")
            }

            user!!.updateEmail(binding.prefilCorreo.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(baseContext, "Se ha actualizado el correo.", Toast.LENGTH_SHORT).show()
                    }
                }

            user!!.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(baseContext, "Se ha actualizado el usuario.", Toast.LENGTH_SHORT).show()

                    }
                }
        }
    }
}