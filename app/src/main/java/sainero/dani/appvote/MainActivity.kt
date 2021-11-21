package sainero.dani.appvote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import sainero.dani.appvote.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var  binding: ActivityMainBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()
        storage = Firebase.storage

        db.collection("users").document(auth.currentUser?.uid.toString())
            .get().addOnSuccessListener{
                asignarImg(storage.getReferenceFromUrl(it.get("imgPath").toString()),binding.imgPerfil)
            }

        binding.mainCrearVoto.setOnClickListener{
            this.startActivity(Intent(this,NewPoll::class.java))
        }
        binding.mainSalir.setOnClickListener{
            val intent = Intent(applicationContext, login::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            auth.signOut()
            startActivity(intent)
        }

        binding.mainUnirseVoto.setOnClickListener{
            this.startActivity(Intent(this,joinAPoll::class.java))
        }

        binding.mainResultadoEncuesta.setOnClickListener{
            var intent: Intent = Intent(this,joinAPoll::class.java)
            intent.putExtra("accion","ver")
            this.startActivity(intent)
        }

        binding.imgPerfil.setOnClickListener{
            this.startActivity(Intent(this,Perfil::class.java))
        }


    }
    private fun asignarImg(url: StorageReference, idImagen: ImageView) {
        url.downloadUrl.addOnSuccessListener {
            Glide.with(this)
                .load(it)
                .fitCenter()
                .centerCrop()
                .into(idImagen)
        }
    }
}