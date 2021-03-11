package com.csci310_group29.trojancheckincheckout.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.csci310_group29.trojancheckincheckout.data.models.User
import com.csci310_group29.trojancheckincheckout.data.remote.AuthRemoteDataSource

class LoginViewModel: ViewModel() {
    private var authRepo = AuthRemoteDataSource()
    private var User = User()


    public fun login(email: String, password: String) {
        ///User = authRepo.getUserWithCredentials(email = email, password = password)


    }
}