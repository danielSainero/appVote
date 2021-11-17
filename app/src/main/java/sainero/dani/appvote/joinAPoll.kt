package sainero.dani.appvote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import sainero.dani.appvote.databinding.ActivityJoinApollBinding
import sainero.dani.appvote.databinding.ActivityMainBinding

class joinAPoll : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var  binding: ActivityJoinApollBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinApollBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()
        if (intent.getStringExtra("accion").equals("ver")) {
            binding.btnJoinAPoll.text = "Ver resultado de la encuesta"
            binding.btnJoinAPoll.setOnClickListener{
                var intent: Intent = Intent(this,PollResult::class.java)
                intent.putExtra("pollId", binding.joinId.text.toString().trim())
                this.startActivity(intent)
            }
        } else {
            binding.btnJoinAPoll.setOnClickListener{
                var intent: Intent = Intent(this,Poll::class.java)
                intent.putExtra("pollId", binding.joinId.text.toString().trim())
                this.startActivity(intent)
            }
        }
    }
}