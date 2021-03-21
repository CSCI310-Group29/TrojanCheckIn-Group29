
package com.csci310_group29.trojancheckincheckout.ui.viewmodels

import com.csci310_group29.trojancheckincheckout.domain.models.Building
import com.csci310_group29.trojancheckincheckout.domain.models.User


class Session {
    companion object {
        var uid = ""
        var user: User? = null
        var checkedInBuilding: Building? = null

    }

}
