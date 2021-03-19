package com.csci310_group29.trojancheckincheckout.views

//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.csci310_group29.trojancheckincheckout.R
//import com.csci310_group29.trojancheckincheckout.domain.models.User
//import kotlinx.android.synthetic.main.activity_login.*
//import com.csci310_group29.trojancheckincheckout.viewmodels.LoginViewModel
//
//private const val TAG = "LoginActivity"
//
//class LoginActivity : AppCompatActivity() {
//    private val loginViewModel = LoginViewModel()
//    private var user = User()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)
//
//    }
//
//    public fun onLogin(view: View) {
//        val email = emailInput.text.toString()
//        val password = passwordInput.text.toString()
//
//        try {
//            user = loginViewModel.login(email, password)
//
//            //Log.i(TAG,"User returned is: " + user.firstName + " " + user.lastName + " " + user.major + " " + user.isStudent)
//
//            if(user.isStudent!!) {
//                startActivity(Intent(this,StudentHomeActivity::class.java))
//                finish()
//            } else {
//                val toast = Toast.makeText(this, "is Manager", Toast.LENGTH_SHORT);
//                toast.show()
//            }
//        } catch(e: Exception) {
//            //Log.i(TAG, "exception returned to onLogin in LoginActivity " + e.localizedMessage)
//            val toast = Toast.makeText(this, "Unable to Login: " + e.localizedMessage, Toast.LENGTH_SHORT)
//            toast.show()
//        }
//    }
//}