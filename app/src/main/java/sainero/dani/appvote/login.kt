package sainero.dani.appvote

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import sainero.dani.appvote.databinding.ActivityLoginBinding
import kotlin.math.log

class login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var  binding: ActivityLoginBinding
    private lateinit var db: FirebaseFirestore

    private val RC_SIGN_IN = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()

        binding.loginTextRegister.setOnClickListener{
            this.startActivity(Intent(this,register::class.java))
        }

        binding.signInPassword.setOnKeyListener(View.OnKeyListener {
            v, keyCode, event -> if (keyCode == KeyEvent.KEYCODE_ENTER) {
                signIn(binding.signInEmail.text.toString(),binding.signInPassword.text.toString())
                return@OnKeyListener true
            } else {
                return@OnKeyListener false
            }
        })

        binding.signInBtn.setOnClickListener{
            if (binding.signInEmail.text.isEmpty() || binding.signInEmail.text.toString().trim().equals("")){
                binding.signInEmail.setError("El email no puede estar vacio")
                return@setOnClickListener
            }

            if (binding.signInPassword.text.isEmpty() || binding.signInPassword.text.toString().trim().equals("")) {
                binding.signInPassword.setError("La contraseña no puede estar vacia")
                return@setOnClickListener
            }
            signIn(
                binding.signInEmail.text.toString(),
                binding.signInPassword.text.toString()
            )
        }
        binding.loginNewPassword.setOnClickListener{
            this.startActivity(Intent(this,NewPassword::class.java))
        }
        binding.signInGoogle.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(this, gso)
            googleSignInClient.signOut()
            signInGoogle(googleSignInClient)
        }
    }

    private fun signInGoogle(googleSignInClient :GoogleSignInClient) {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {

                val account = task.getResult(ApiException::class.java)

                if (account != null)
                    firebaseAuthWithGoogle(account.idToken)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {

        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    allUsers()

                    reload()

                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("Inicio de sesión", "Se ha iniciado la sesión")
                    val user = auth.currentUser
                    reload()
                } else {
                    Log.w("Inicio de sesión", "No se ha podido iniciar la sesión", task.exception)
                    Toast.makeText(baseContext, "El usuario o contraseña no son válidos.", Toast.LENGTH_LONG).show()
                }
            }
    }

    public override fun onStart() {
        super.onStart()
        val user = auth.currentUser
        if(user != null){
            reload();
        }
    }
    private fun setInformationUser() {
        db.collection("users").document(auth.currentUser?.uid.toString())
            .set(
                hashMapOf(
                    //"password" to binding.registerPassword.text.toString(),
                    "name" to "",
                    "email" to auth.currentUser?.email.toString(),
                    "imgPath" to "gs://appvote-bdc78.appspot.com/imgUser/sainorum.png",
                    "ofertas" to "true"
                )
            )
    }
    private fun reload() {
        this.startActivity(Intent(this, MainActivity::class.java))
    }


    private fun allUsers() {
        var tmp: Boolean = false

        db.collection("users").get().addOnSuccessListener {

            for (document in it) {

                if(document.id.equals(auth.currentUser?.uid))
                    tmp = true
            }
            if(!tmp)
                setInformationUser()

        }
    }
}