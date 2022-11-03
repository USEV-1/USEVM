package com.example.usevm

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.ActionBar
import com.example.usevm.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
class SignupActivity : AppCompatActivity() {

    //view binding
    private lateinit var binding: ActivitySignupBinding

    //action bar
    private lateinit var actionBar: ActionBar

    //ProgessDialog
    private lateinit var progressDialog: ProgressDialog

    //
    private lateinit var firebaseAuth:FirebaseAuth
    private var email= ""
    private var password = ""

    //FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //configure action bar
        actionBar = supportActionBar!!
        actionBar.title = "Sign Up"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        //configure progess dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("please wait")
        progressDialog.setMessage("Creating account In...")
        progressDialog.setCanceledOnTouchOutside(false)

        // init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        //handle click, begin signup
        binding.SignupBtn.setOnClickListener{
            //validate data
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
        else if (password.length <6){
            // if password is less than 6
            binding.passwordEt.error="Password must atleast 6 characters long"
        }
        else {
            //data is vaildated then signup
            firebaseSignup()
        }
    }

    private fun firebaseSignup() {
             // show progess
            progressDialog.show()

            //create account
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    //signup success
                    progressDialog.dismiss()
                    val firebaseUser = firebaseAuth.currentUser
                    val email = firebaseUser!!.email
                    Toast.makeText(this, "Account created with email $email", Toast.LENGTH_SHORT).show()

                    //open profile
                    startActivity(Intent(this,ProfileActivity::class.java ))
                    finish()
                }
                .addOnFailureListener{e->
                    progressDialog.dismiss()
                    Toast.makeText(this, "Signup failed due to ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

    override fun onSupportNavigateUp(): Boolean {
            onBackPressed()//go back to previous activity, when back button of action bar clicked
            return super.onSupportNavigateUp()
    }
}


