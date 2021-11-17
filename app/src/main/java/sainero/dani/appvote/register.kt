package sainero.dani.appvote

import android.content.ContentValues.TAG
import android.content.Intent
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import sainero.dani.appvote.databinding.ActivityLoginBinding
import sainero.dani.appvote.databinding.ActivityRegisterBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.storage.*
import com.google.firebase.storage.ktx.storage

class register : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var  binding: ActivityRegisterBinding

    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    private lateinit var img: StorageReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()
        storage = Firebase.storage

        val user = auth.currentUser
        var storageRef = storage.reference

        img = storage.getReferenceFromUrl("gs://appvote-bdc78.appspot.com/imgUser/sainorum.png")





        binding.registerImg.setOnClickListener{
            var intent : Intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.setType("image/")
            startActivityForResult(Intent.createChooser(intent,"Seleccione la Aplicación"),10)

        }
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

    private fun setInformationUser() {
        db.collection("users").document(auth.currentUser?.uid.toString())
            .set(
                hashMapOf(
                    "name" to binding.registerName.text.toString(),
                    "email" to binding.registerMail.text.toString(),
                    "password" to binding.registerPassword.text.toString(),
                    "imgPath" to img.toString()
                )
            )
    }
    protected override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val path: Uri? = data?.data
            var storageRef = storage.reference
            val riversRef = storageRef.child("imgUser/${path?.lastPathSegment}")
            img = storage.getReferenceFromUrl(storageRef.child("imgUser/${path?.lastPathSegment}").toString())

            val uploadTask = riversRef.putFile(path!!)
            uploadTask.addOnFailureListener {
                Toast.makeText(this,"No ha funcionado",Toast.LENGTH_LONG).show()
            }.addOnSuccessListener { taskSnapshot ->
                Toast.makeText(this,"Ha funcionado",Toast.LENGTH_LONG).show()
                asignarImg(img, binding.registerImg)
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

    private fun createUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser

                    Toast.makeText(this,"El usuario ha sido creado",Toast.LENGTH_LONG).show()
                    setInformationUser()
                    reload()
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(this,"El usuario no ha podido ser creado",Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun reload() {
        this.startActivity(Intent(this, MainActivity::class.java))
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            reload();
        }
    }

}

