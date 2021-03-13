package com.csci310_group29.trojancheckincheckout.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.data.fake.AuthFakeDataSource
import com.csci310_group29.trojancheckincheckout.data.models.User
import com.csci310_group29.trojancheckincheckout.data.repo.AuthRepoImpl
import com.csci310_group29.trojancheckincheckout.viewmodels.StudentHomeViewModel

class StudentHomeActivity : AppCompatActivity() {

    private val viewModel: StudentHomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_home)

    }

    private fun observeViewModel() {
        TODO("add observers for views in ui")
    }


}