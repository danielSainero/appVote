package sainero.dani.appvote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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
    private lateinit var allPolls: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinApollBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()

        allPolls = mutableListOf()

        db.collection("poll").get().addOnSuccessListener {
            for (document in it)
                allPolls.add(document.id)
        }


        if (intent.getStringExtra("accion").equals("ver")) {
            binding.btnJoinAPoll.text = "Ver resultado de la encuesta"
            binding.btnJoinRandomPoll.text = "Ver una encuesta aleatoria"
            binding.btnJoinAPoll.setOnClickListener{

                if (binding.joinId.text.toString().equals("") || binding.joinId.text.isEmpty()){
                    binding.joinId.setError("Debes de rellenar el campo obligatoriamente")
                    return@setOnClickListener
                }

                for (i in allPolls) {
                    if (binding.joinId.text.toString().equals(i)) {
                        showViewPollResult(binding.joinId.text.toString().trim())
                        return@setOnClickListener
                    }
                }
                binding.joinId.setError("La encuesta no existe")
            }

            binding.btnJoinRandomPoll.setOnClickListener{
                var randomPoll = allPolls.random()
                showViewPollResult(randomPoll)
            }
        } else {
            binding.btnJoinAPoll.setOnClickListener{

                if (binding.joinId.text.toString().equals("") || binding.joinId.text.isEmpty()){
                    binding.joinId.setError("Debes de rellenar el campo obligatoriamente")
                    return@setOnClickListener
                }

                for (i in allPolls) {
                    if (binding.joinId.text.toString().equals(i)) {
                        showViewPoll(binding.joinId.text.toString().trim())
                        return@setOnClickListener
                    }
                }
                binding.joinId.setError("La encuesta no existe")
            }

            binding.btnJoinRandomPoll.setOnClickListener{
                var randomPoll = allPolls.random()
                showViewPoll(randomPoll)
            }
        }
    }

    private fun showViewPoll(idPoll: String) {
        var intent: Intent = Intent(this,Poll::class.java)
        intent.putExtra("pollId", idPoll)
        this.startActivity(intent)
    }

    private fun showViewPollResult(idResultPoll: String) {
        var intent: Intent = Intent(this,PollResult::class.java)
        intent.putExtra("pollId", idResultPoll.trim())
        this.startActivity(intent)
    }
}