package sainero.dani.appvote

import android.content.ClipData
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.ClipboardManager
import android.util.Log
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
import androidx.annotation.NonNull

import com.google.android.gms.tasks.OnFailureListener

import com.google.android.gms.tasks.OnSuccessListener





class Poll : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var  binding: ActivityPollBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private var pollOptions: MutableList<PollOption> = mutableListOf()
    private lateinit var pollType: String
    private lateinit var id: String
    private lateinit var new: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPollBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()

        var opciones: MutableList<String> = mutableListOf()
        var pregunta: String = ""
        var respuestas : MutableList<Int> = mutableListOf()
        var usuarios: MutableList<String> = mutableListOf()

        id = intent.getStringExtra("pollId").toString()
        pollType = intent.getStringExtra("pollType").toString()
        new = intent.getStringExtra("new").toString()

        binding.textPollId.text = "${id}"
        db.collection("poll").document(id)
            .get().addOnSuccessListener{
                opciones = it.get("Opciones") as MutableList<String>
                pregunta = it.get("Question") as String
                respuestas = it.get("Respuestas") as MutableList<Int>
                usuarios = it.get("Usuarios") as MutableList<String>
                for (i in opciones)
                    pollOptions.add(PollOption(i,false))
                crearRecyleView(pollOptions)
                binding.questionPoll.text = pregunta.toString()
            }
        binding.btnCopy.setOnClickListener{
            val clipBoard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Id de la encuesta",binding.textPollId.text)
            clipBoard.setPrimaryClip(clip)
            Toast.makeText(this,"El elemento ha sido copiado",Toast.LENGTH_SHORT).show()
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
            usuarios.add(auth.currentUser?.uid.toString())

            documento.set(
                hashMapOf(
                    "Opciones" to opciones,
                    "Question" to pregunta,
                    "Respuestas" to respuestas,
                    "Usuarios" to usuarios
                )
            )


            var intent: Intent = Intent(this,PollResult::class.java)
            intent.putExtra("pollId", id)
            this.startActivity(intent)
        }
    }

    fun crearRecyleView(options: MutableList<PollOption>) {
        val rv = binding.optionsPoll
        //Toast.makeText(this,pollType.toString(),Toast.LENGTH_SHORT).show()

        if (pollType.equals("0"))
            rv.adapter = AdaptadorPoll(options)
        else
            rv.adapter = AdapterPollSingleChoice(options)
        rv.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()
    }
    override fun onBackPressed() {
        super.onBackPressed()

        if(new.equals("new")) {
            db.collection("poll").document(id)
                .delete()
                .addOnSuccessListener { Log.d("BackPressed", "Se ha eliminado el documento") }
                .addOnFailureListener { e -> Log.w("BackPressed", "No se ha podido eliminar el documento", e) }

            this.startActivity(Intent(this,NewPoll::class.java))
        } else {
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

    }
}