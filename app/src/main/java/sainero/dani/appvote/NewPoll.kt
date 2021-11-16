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
        var options = mutableListOf("Opciones:")
        var contentAdapter: ArrayAdapter<String>

//  ArrayAdapter<String>(this, binding.optionList,options)

        binding.newPollAddOption.setOnClickListener{
            if (binding.newPollOption.text.isEmpty() || binding.newPollOption.text.toString().trim().equals("")) return@setOnClickListener

            options.add(binding.newPollOption.text.toString().trim())
            contentAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,options)
            binding.optionList.adapter = contentAdapter
            binding.newPollOption.setText("")

        }
    }


}