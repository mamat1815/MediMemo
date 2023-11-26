package com.medimemo.ui.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.medimemo.R
import com.medimemo.databinding.ActivitySignUpBinding
import com.medimemo.ui.signin.SignInActivity

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.apply {
            btnSignUp.setOnClickListener {
                val email = edtRgEmail.text.toString()
                val password = edtRgPassword.text.toString()
                val confirmPassword = edtRgConfirmPassword.text.toString()
                val name = edtRgName.text.toString()

                /*TODO
                *
                */

                if (email.isEmpty()) {
                    edtRgEmail.error = "Email Harus Di Isi"
                    edtRgEmail.requestFocus()
                    return@setOnClickListener
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    edtRgEmail.error = "Email Tidak Valid"
                    edtRgEmail.requestFocus()
                    return@setOnClickListener
                }

                if (password.isEmpty()) {
                    edtRgPassword.error = "Password Tidak Boleh Kosong"
                    edtRgPassword.requestFocus()
                    return@setOnClickListener
                }

                if (password.length < 8) {
                    edtRgPassword.error = "Password Tidak Boleh Kurang Dari 8"
                    edtRgPassword.requestFocus()
                    return@setOnClickListener
                }

                if (password != confirmPassword) {
                    edtRgPassword.error = "Password Tidak Sama"
                    edtRgPassword.requestFocus()
                    return@setOnClickListener
                }

                registerFirebase(email, password, name)
            }
        }

    }

    private fun registerFirebase(email: String, password: String, name: String) {
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){
                if (it.isSuccessful) {
                    val user: FirebaseUser? = auth.currentUser
                    val profileUpdate = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()

                    user?.updateProfile(profileUpdate)

                    Toast.makeText(this, "Registrasi Berhasil", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}