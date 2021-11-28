package sainero.dani.appvote

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
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
    private lateinit var img: String
    private  lateinit var newImg: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()
        storage = Firebase.storage
        val user = auth.currentUser



        db.collection("users").document(auth.currentUser?.uid.toString())
            .get().addOnSuccessListener{
                binding.perfilNombre.setText(it.get("name") as String?)
                binding.prefilCorreo.setText(it.get("email") as String?)
                img = storage.getReferenceFromUrl(it.get("imgPath").toString()).toString()
                asignarImg(storage.getReferenceFromUrl(it.get("imgPath").toString()),binding.perfilImg)
            }

        binding.perfilImg.setOnClickListener{
            var intent : Intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.setType("image/")
            startActivityForResult(Intent.createChooser(intent,"Seleccione la Aplicación"),10)
        }
        binding.btnPerfilModificarContra.setOnClickListener {
            this.startActivity(Intent(this, NewPassword::class.java))
        }

        binding.btnPerfilActualizarDatos.setOnClickListener{
            user!!.sendEmailVerification()
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        setInformationUser()
                        user!!.updateEmail(binding.prefilCorreo.text.toString().trim())
                            .addOnCompleteListener{ task ->
                                if (task.isSuccessful) Toast.makeText(baseContext, "Los datos se han actualizado correctamente", Toast.LENGTH_SHORT).show()
                            }
                    } else Toast.makeText(baseContext, "No se puede validar la cuenta de usuario", Toast.LENGTH_SHORT).show()
                }
        }
    }
    private fun setInformationUser() {
        db.collection("users").document(auth.currentUser?.uid.toString())
            .set(
                hashMapOf(
                    "name" to binding.perfilNombre.text.toString(),
                    "email" to binding.prefilCorreo.text.toString(),
                    "imgPath" to img,
                    "ofertas" to binding.cbOfertas.isChecked.toString()
                )
            )
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

    protected override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val path: Uri? = data?.data
            var storageRef = storage.reference
            val riversRef = storageRef.child("imgUser/${path?.lastPathSegment}")
            newImg = storage.getReferenceFromUrl(storageRef.child("imgUser/${path?.lastPathSegment}").toString())

            val uploadTask = riversRef.putFile(path!!)
            uploadTask.addOnFailureListener {
                Toast.makeText(this,"No ha funcionado",Toast.LENGTH_LONG).show()
            }.addOnSuccessListener { taskSnapshot ->
                Toast.makeText(this,"Ha funcionado",Toast.LENGTH_LONG).show()
                img = newImg.toString()
                asignarImg(newImg, binding.perfilImg)
            }
        }
    }
}