package com.csci310_group29.trojancheckincheckout.ui.views

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.ui.viewmodels.RegisterViewModel
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    @Inject
    lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val spinner = findViewById<View>(R.id.major_spinner) as Spinner
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.majors_array, android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        val spinner1 = findViewById<View>(R.id.accountTypeSpinner) as Spinner
        val adapter1 = ArrayAdapter.createFromResource(
            this,
            R.array.account_type, android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner1.adapter = adapter1
        Log.i(TAG, "oncreate registeractivity")
    }

    fun onRegister(view: View) {
        try {
            val imm =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        } catch (e: Exception) {
        }
        val firstNameText = findViewById<TextInputEditText>(R.id.NameInput)
        val emailText = findViewById<EditText>(R.id.EmailInput)
        val passwordText = findViewById<EditText>(R.id.PasswordInput)
        val lastNameText = findViewById<EditText>(R.id.LastNameInput)
        val SIDText = findViewById<EditText>(R.id.idInput)
        val first = firstNameText.text.toString()
        val last = lastNameText.text.toString()
        val email = emailText.text.toString()
        val pass = passwordText.text.toString()
        val id = SIDText.text.toString()
        val mSpinner = findViewById<Spinner>(R.id.major_spinner)
        val major = mSpinner.selectedItem.toString()
        val tSpinner = findViewById<Spinner>(R.id.accountTypeSpinner)
        val type = tSpinner.selectedItem.toString()
        val isStudent = if (type == "Student") true else false

        val newUser =
            UserEntity(
                null,
                isStudent,
                first,
                last,
                major,
                null,
                id,
                null
            )

        try {
            registerViewModel!!.register(email, pass, newUser)

            firstNameText.text!!.clear()
            lastNameText.text!!.clear()
            emailText.text!!.clear()
            passwordText.text!!.clear()
            SIDText.text!!.clear()

            val toast = Toast.makeText(this, "Registered", Toast.LENGTH_SHORT)
            toast.show()
        } catch (e: Exception) {
            //Log.i(TAG, "error returned from register function in registerViewmodel " + e.getMessage());
            val toast =
                Toast.makeText(this, "Cannot Register: " + e.message, Toast.LENGTH_SHORT)
            toast.show()
        }
    }

    companion object {
        private const val TAG = "RegisterActivity"
    }
}