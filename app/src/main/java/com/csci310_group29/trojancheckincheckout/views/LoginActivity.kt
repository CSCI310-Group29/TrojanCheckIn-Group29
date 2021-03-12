package com.csci310_group29.trojancheckincheckout.views

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.csci310_group29.trojancheckincheckout.R
import kotlinx.android.synthetic.main.activity_login.*
import com.csci310_group29.trojancheckincheckout.viewmodels.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private var loginViewModel = LoginViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton.setOnClickListener() {
            val email = editTextTextEmailAddress2.getText().toString()
            val password = editTextTextPassword.getText().toString()

            if(email.length > 0 && password.length > 0) {
                loginViewModel.login(email, password)
            } else if(email.length == 0 ) {
                val toast = Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT)
                toast.show()
            } else if(password.length == 0) {
                val toast = Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }
}