package sainero.dani.appvote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import sainero.dani.appvote.databinding.ActivityNewPollBinding
import sainero.dani.appvote.databinding.ActivityPollBinding


class Poll : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var  binding: ActivityPollBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private var pollOptions: MutableList<PollOption> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPollBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()

        var opciones: MutableList<String> = mutableListOf()
        var pregunta: String = ""
        var respuestas : MutableList<Int> = mutableListOf()

        var id = intent.getStringExtra("pollId").toString()

        db.collection("poll").document(id)
            .get().addOnSuccessListener{
                opciones = it.get("Opciones") as MutableList<String>
                pregunta = it.get("Question") as String
                respuestas = it.get("Respuestas") as MutableList<Int>
                for (i in opciones)
                    pollOptions.add(PollOption(i,false))
                crearRecyleView(pollOptions)
                binding.questionPoll.text = pregunta.toString()
            }

        //agregar a la BD
        binding.button.setOnClickListener{

            var position = 0
            for( i in pollOptions) {
                if(i.isChecked)
                    respuestas.set(position,respuestas.get(position) +1)

                position++
            }

          val documento = db.collection("poll").document(id)

            documento.set(
                hashMapOf(
                    "Opciones" to opciones,
                    "Question" to pregunta,
                    "Respuestas" to respuestas
                )
            )

            var intent: Intent = Intent(this,PollResult::class.java)
            intent.putExtra("pollId", id)
            this.startActivity(intent)
        }
    }




    fun crearRecyleView(options: MutableList<PollOption>) {
        val rv = binding.optionsPoll
        rv.adapter = AdaptadorPoll(options)
        rv.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()
    }
}