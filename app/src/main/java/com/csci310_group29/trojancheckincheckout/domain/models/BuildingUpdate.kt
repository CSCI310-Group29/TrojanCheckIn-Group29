package com.csci310_group29.trojancheckincheckout.domain.models

import com.opencsv.bean.CsvBindByName

class BuildingUpdate {

    @CsvBindByName
    var buildingName: String = ""

    @CsvBindByName
    var newCapacity: Double = 0.0

    constructor() {}
    constructor(buildingName: String, newCapacity: Double) {
        this.buildingName = buildingName
        this.newCapacity = newCapacity
    }

    fun getName(): String {
        return this.buildingName
    }

    fun getCap(): Double {
        return this.newCapacity
    }

    override fun toString(): String {
        return "New Building [name=" + buildingName + ", newcap=" + newCapacity + "]"
    }
}