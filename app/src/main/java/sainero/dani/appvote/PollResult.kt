package sainero.dani.appvote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import sainero.dani.appvote.databinding.ActivityPollBinding
import sainero.dani.appvote.databinding.ActivityPollResultBinding

data class DataSetpollResult(val option: String, val numSelected: Int)

class PollResult : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var  binding: ActivityPollResultBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var dataPollResult: MutableList<DataSetpollResult>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPollResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()
        dataPollResult = mutableListOf()
        var opciones : MutableList<String> = mutableListOf()
        var pregunta: String
        var respuestas: MutableList<Int> = mutableListOf()
        var id = intent.getStringExtra("pollId").toString()

        binding.textResultPollId.text = id

        db.collection("poll").document(id)
            .get().addOnSuccessListener{
                opciones = it.get("Opciones") as MutableList<String>
                pregunta = it.get("Question") as String
                respuestas = it.get("Respuestas") as MutableList<Int>

                binding.resultQuestion.text = pregunta
                createDataSet(opciones,respuestas)
                crearRecyleView(dataPollResult)
            }

        binding.pollResultMain.setOnClickListener{
            this.startActivity(Intent(this,MainActivity::class.java))
        }
    }

    fun createDataSet(opciones:MutableList<String>, respuestas:MutableList<Int>){
        for (i in 0..opciones.size-1)
            dataPollResult.add(DataSetpollResult(opciones.get(i),respuestas.get(i)))
    }

    fun crearRecyleView(results: MutableList<DataSetpollResult>) {
        val rv = binding.pollResultView
        rv.adapter = AdaptadorPollResult(results)
        rv.layoutManager = LinearLayoutManager(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.startActivity(Intent(this,MainActivity::class.java))
    }
}