package sainero.dani.appvote

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import sainero.dani.appvote.databinding.ActivityLoginBinding
import sainero.dani.appvote.databinding.ActivityNewPasswordBinding
import sainero.dani.appvote.databinding.ActivityPerfilBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class Perfil : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var  binding: ActivityPerfilBinding
    private  lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()
        storage = Firebase.storage

        val user = auth.currentUser
        val storageRef = storage.reference


        db.collection("users").document(auth.currentUser?.uid.toString())
            .get().addOnSuccessListener{
                binding.perfilNombre.setText(it.get("name") as String?)
                binding.prefilCorreo.setText(it.get("email") as String?)
                asignarImg(storage.getReferenceFromUrl(it.get("imgPath").toString()),binding.perfilImg)

            }

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
    private fun asignarImg(url: StorageReference, idImagen: ImageView) {
        url.downloadUrl.addOnSuccessListener{
            Glide.with(this)
                .load(it)
                .fitCenter()
                .centerCrop()
                .into(idImagen)
        }

    }
}