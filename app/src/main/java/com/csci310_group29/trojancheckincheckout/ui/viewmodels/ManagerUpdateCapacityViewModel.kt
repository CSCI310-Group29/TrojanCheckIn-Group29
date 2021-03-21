package com.csci310_group29.trojancheckincheckout.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import com.csci310_group29.trojancheckincheckout.domain.models.Building
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.usecases.BuildingUseCases
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class ManagerUpdateCapacityViewModel @Inject constructor(private val buildingDomain: BuildingUseCases) {

    private val TAG = "ManagerUpdateCapacityViewModel"
    private val colRef = Firebase.firestore.collection("buildings")

    var currUser: MutableLiveData<User> = MutableLiveData<User>(Session.user)
    var currBuilding: MutableLiveData<Building> = MutableLiveData<Building>(currUser.)

    fun updateWithUI() {

    }

    fun updateWithCSV() {

    }

}