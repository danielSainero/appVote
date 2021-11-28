package sainero.dani.appvote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.get
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import sainero.dani.appvote.databinding.ActivityNewPollBinding
import android.R
import android.bluetooth.BluetoothA2dp
import android.graphics.Color
import android.graphics.Color.*
import android.util.Log
import android.view.ViewParent
import com.bumptech.glide.request.RequestOptions.option
import java.util.*


class NewPoll : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var  binding: ActivityNewPollBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private  lateinit var  options : MutableList<String>
    private  lateinit var id: String
    private lateinit var respuestas: MutableList<Int>
    private lateinit var usuarios: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPollBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()

        respuestas = mutableListOf()
        usuarios = mutableListOf()
        var valueOfPollType: Int = 0;

        val lista = listOf("Multi selección","Única selección")
        val  adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, R.layout.simple_spinner_item,lista)
        binding.pollType.adapter = adapter

        binding.pollType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
               when(position) {
                   0 -> valueOfPollType = 0
                   1 -> valueOfPollType = 1
                   else -> {
                       valueOfPollType = 0
                   }
               }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        options = mutableListOf("Opciones:")
        var contentAdapter: ArrayAdapter<String>
        contentAdapter = ArrayAdapter(this, R.layout.simple_list_item_1,options)
        binding.optionList.adapter = contentAdapter

        binding.newPollAddOption.setOnClickListener{
            addPollOption()
        }

        binding.optionList.setOnItemClickListener (AdapterView.OnItemClickListener{ parent, view, position, values ->

            if (position != 0) {
                options.removeAt(position)
                binding.optionList.adapter = ArrayAdapter(this, R.layout.simple_list_item_1,options)
            }
        })

         binding.newPollOption.setOnKeyListener(View.OnKeyListener {
              v, keyCode, event -> if (keyCode == KeyEvent.KEYCODE_ENTER) {
                  addPollOption()
                  return@OnKeyListener true
              } else {
                  return@OnKeyListener false
              }
          })

        binding.btnNewPoll.setOnClickListener{
            if (binding.Question.text.isEmpty() || binding.Question.text.toString().trim().equals("")) {
                binding.Question.setError("El valor no puede estar vacio")
                return@setOnClickListener
            }

            if(binding.Question.text.length > 40) {
                binding.Question.setError("La pregunta no puede teenr más de 40 caracteres")
                return@setOnClickListener
            }

            if(options.size < 2) {
                binding.newPollOption.setError("Debes añadir alguna opción")
                return@setOnClickListener
            }
            options.remove("Opciones:")
            for (i in options) respuestas.add(0)

            setInformationPoll(options)
            var intent: Intent = Intent(this,Poll::class.java)
            intent.putExtra("pollId", id)
            intent.putExtra("pollType", valueOfPollType.toString())
            intent.putExtra("new","new")
            this.startActivity(intent)

        }
    }

    private fun setInformationPoll(opciones: MutableList<String>) {
        val documento = db.collection("poll").document()
        id = documento.id

        documento.set(
            hashMapOf(
                "Question" to binding.Question.text.toString(),
                "Opciones" to opciones,
                "Respuestas" to respuestas,
                "Usuarios" to usuarios
                )
        )

    }

    private fun addPollOption() {

        if(binding.newPollOption.text.length > 40) {
            binding.newPollOption.setError("La opción no puede tener más de 40 caracteres")
        } else {
            if (!(binding.newPollOption.text.isEmpty() || binding.newPollOption.text.toString().trim().equals(""))) {
                val contentAdapter :ArrayAdapter<String>
                contentAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,options)
                options.add(binding.newPollOption.text.toString().trim())
                binding.optionList.adapter = contentAdapter
                binding.newPollOption.setText("")
            }
        }

    }


    override fun onStart() {
        super.onStart()
     }
}