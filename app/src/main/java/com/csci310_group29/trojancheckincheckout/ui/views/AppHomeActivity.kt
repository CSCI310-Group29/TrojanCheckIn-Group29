
package com.csci310_group29.trojancheckincheckout.ui.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.csci310_group29.trojancheckincheckout.R

class AppHomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_home)
    }

    fun onLogin(view: View) {
        var intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

    }

    fun onSignUp(view: View) {
        var intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}
