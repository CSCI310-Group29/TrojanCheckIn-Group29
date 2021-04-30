package com.csci310_group29.trojancheckincheckout.domain.models

enum class Operation { ADD, UPDATE, REMOVE }

/*
Class for adding stuff from CSV

Operation is a enum with UPDATE, REMOVE, and ADD.
For UPDATE and ADD, capacity must not be null. For
REMOVE, capacity can be null
 */
data class BuildingOperation(
    val operation: Operation,
    val buildingName: String,
    val capacity: Int? = null
)
