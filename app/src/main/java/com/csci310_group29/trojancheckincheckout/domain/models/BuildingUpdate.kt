package com.csci310_group29.trojancheckincheckout.domain.models

import com.opencsv.bean.CsvBindByName

class BuildingUpdate {

    @CsvBindByName
    var buildingName: String = ""

    @CsvBindByName
    var newCapacity: Int = 0

    constructor() {}
    constructor(buildingName: String, newCapacity: Int) {
        this.buildingName = buildingName
        this.newCapacity = newCapacity
    }

    fun getName(): String {
        return this.buildingName
    }

    fun getCap(): Int {
        return this.newCapacity
    }

    override fun toString(): String {
        return "New Building [name=" + buildingName + ", newcap=" + newCapacity + "]"
    }
}