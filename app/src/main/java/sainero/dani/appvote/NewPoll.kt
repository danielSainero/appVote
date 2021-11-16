package sainero.dani.appvote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import sainero.dani.appvote.databinding.ActivityMainBinding
import sainero.dani.appvote.databinding.ActivityNewPollBinding
import kotlin.math.log

class NewPoll : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var  binding: ActivityNewPollBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPollBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()

        var options = mutableListOf("Opciones:")
        var contentAdapter: ArrayAdapter<String>
        contentAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,options)
        binding.optionList.adapter = contentAdapter

        binding.newPollAddOption.setOnClickListener{
            if (binding.newPollOption.text.isEmpty() || binding.newPollOption.text.toString().trim().equals("")) return@setOnClickListener

            options.add(binding.newPollOption.text.toString().trim())
            contentAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,options)
            binding.optionList.adapter = contentAdapter
            binding.newPollOption.setText("")

        }

        binding.btnNewPoll.setOnClickListener{
            if (binding.Question.text.isEmpty() || binding.Question.text.toString().trim().equals("")) {
                binding.Question.setError("El valor no puede estar vacio")
                return@setOnClickListener
            }

            if(options.size < 2) {
                binding.newPollOption.setError("Debes añadir alguna opción")
                return@setOnClickListener
            }
            options.remove("Opciones:")
            setInformationPoll(options)
        }
    }
    private fun setInformationPoll(opciones: MutableList<String>) {
        db.collection("poll").document(randomID())
            .set(
                hashMapOf(
                    "Question" to binding.Question.text.toString(),
                    "Opciones" to opciones
                    )
            )
    }

    private fun randomID(): String = List(16) {
        (('a'..'z') + ('A'..'Z') + ('0'..'9')).random()
    }.joinToString("")


}