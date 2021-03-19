package com.csci310_group29.trojancheckincheckout.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.csci310_group29.trojancheckincheckout.R

class ManagerHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_home)
    }

    fun onViewProfile(view: View) {
        startActivity(Intent(this,ManagerProfileActivity::class.java))
    }

    fun onLogout(view: View)  {

    }


}