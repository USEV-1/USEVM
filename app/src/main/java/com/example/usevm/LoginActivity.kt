package com.example.usevm


import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.ActionBar
import com.example.usevm.R
import com.example.usevm.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    //view binding
    private lateinit var binding: ActivityLoginBinding

    //action bar
    private lateinit var actionBar: ActionBar

    //ProgessDialog
    private lateinit var progressDialog:ProgressDialog

    //FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //configure actionbar
        actionBar = supportActionBar!!
        actionBar.title= "login"


        //configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("please wait")
        progressDialog.setMessage("Logging In...")
        progressDialog.setCanceledOnTouchOutside(false)

        //init firebaseauth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //handle click,begin register
        binding.notAccountTv.setOnClickListener{
            startActivity(Intent(this, SignupActivity::class.java ))
        }


        // handle click, begin login
        binding.loginBtn.setOnClickListener{
        validateData()
        }


    }

    private fun validateData() {
        //get data
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()

        //validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid email
            binding.emailEt.setError("Invalid email format")
        }
        else if (TextUtils.isEmpty(password)){
            //no password entered
            binding.passwordEt.error = "please enter password"
        }
        else{
            //data is vaildated then login
            firebaselogin()
        }
    }

    private fun firebaselogin() {
        //show progess
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                //login sucess
                progressDialog.dismiss()
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this, "loggedIn  as $email", Toast.LENGTH_SHORT).show()

                //open profile
                startActivity(Intent(this, ProfileActivity::class.java))
                finish()
        }
            .addOnFailureListener{e->
                //login failed
                progressDialog.dismiss()
                Toast.makeText(this, "login failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser !=null){
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }
    }


}
