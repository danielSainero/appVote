package sainero.dani.appvote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RestrictTo
import com.google.android.gms.common.Scopes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import sainero.dani.appvote.databinding.ActivityJoinApollBinding
import sainero.dani.appvote.databinding.ActivityMainBinding
import java.util.*
import kotlin.concurrent.schedule
import kotlin.concurrent.thread
import kotlin.coroutines.startCoroutine

class joinAPoll : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var  binding: ActivityJoinApollBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var allPolls: MutableList<DataPolls>
    private lateinit var usuarios: MutableList<String>
    private lateinit var availablePolls: MutableList<DataPolls>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinApollBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()

        availablePolls = mutableListOf()
        allPolls = mutableListOf()
        usuarios = mutableListOf();

        initialiceArrays()

        if (intent.getStringExtra("accion").equals("ver")) {
            binding.btnJoinAPoll.text = getString(R.string.joinResultPoll)
            binding.btnJoinRandomPoll.text = getString(R.string.joinResultRandomPoll)
            binding.btnJoinAPoll.setOnClickListener{

                if (binding.joinId.text.toString().equals("") || binding.joinId.text.isEmpty()){
                    binding.joinId.setError("Debes de rellenar el campo obligatoriamente")
                    return@setOnClickListener
                }

                for (i in allPolls) {
                    if (binding.joinId.text.toString().equals(i.id)) {
                        showViewPollResult(binding.joinId.text.toString().trim())
                        return@setOnClickListener
                    }
                }
                binding.joinId.setError("La encuesta no existe")
            }

            binding.btnJoinRandomPoll.setOnClickListener{

                var randomPoll = allPolls.random()
                showViewPollResult(randomPoll.id)
            }

        } else {
            binding.btnJoinAPoll.setOnClickListener{

                if (binding.joinId.text.toString().equals("") || binding.joinId.text.isEmpty()){
                    binding.joinId.setError("Debes de rellenar el campo obligatoriamente")
                    return@setOnClickListener
                }

                if(availablePolls.any{it.id.equals(binding.joinId.text.toString())}) {
                    showViewPoll(binding.joinId.text.toString().trim())
                    return@setOnClickListener
                }

                binding.joinId.setError("La encuesta no existe o ya has participado en ella")
            }

            binding.btnJoinRandomPoll.setOnClickListener{
                if(availablePolls.size == 0) {
                    Toast.makeText(this,"No hay más encuestas disponibles",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                var randomPoll: DataPolls = availablePolls.random()
                showViewPoll(randomPoll.id)
            }
        }
    }


    private fun initialiceArrays() {
        db.collection("poll").get().addOnSuccessListener {
            for (document in it) {
                allPolls.add(
                    DataPolls(
                        document.get("Opciones") as MutableList<String>,
                        document.get("Question") as String,
                        document.get("Respuestas") as MutableList<String>,
                        document.get("Usuarios") as MutableList<String>,
                        document.id
                    )
                )
            }
            getAvailablePolls(allPolls)
        }
    }

    private fun getAvailablePolls(allPolls: MutableList<DataPolls>) {

        for(i in allPolls){
            if(!i.usuarios.any{it.equals(auth.currentUser?.uid.toString())})
                availablePolls.add(i)
        }
    }

    private fun showViewPoll(idPoll: String) {
        var intent: Intent = Intent(this,Poll::class.java)
        intent.putExtra("pollId", idPoll)
        intent.putExtra("new","notNew")
        this.startActivity(intent)
    }

    private fun showViewPollResult(idResultPoll: String) {
        var intent: Intent = Intent(this,PollResult::class.java)
        intent.putExtra("pollId", idResultPoll.trim())
        this.startActivity(intent)
    }
}


