package com.csci310_group29.trojancheckincheckout.viewmodels

import androidx.lifecycle.ViewModel
import com.csci310_group29.trojancheckincheckout.data.models.User
import com.csci310_group29.trojancheckincheckout.data.remote.AuthFirebaseDataSource

class LoginViewModel: ViewModel() {
    private var authRepo = AuthFirebaseDataSource()
    private var User = User()


    public fun login(email: String, password: String) {
        ///User = authRepo.getUserWithCredentials(email = email, password = password)


    }
}