package sainero.dani.appvote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import sainero.dani.appvote.databinding.ActivityLoginBinding
import sainero.dani.appvote.databinding.ActivityRegisterBinding

class register : AppCompatActivity() {

    //private lateinit var auth: FirebaseAuth
    private lateinit var  binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.registerPolicies.setOnClickListener{
            this.startActivity(Intent(this, privacyPolicies::class.java))
        }
        binding.btnRegister.setOnClickListener{}
    }
}