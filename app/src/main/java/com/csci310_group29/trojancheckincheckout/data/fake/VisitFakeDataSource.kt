package com.csci310_group29.trojancheckincheckout.data.fake

import com.csci310_group29.trojancheckincheckout.data.models.User
import com.csci310_group29.trojancheckincheckout.data.models.Visit
import com.csci310_group29.trojancheckincheckout.data.repo.VisitRepository
import io.reactivex.Single
import java.time.LocalDateTime

class VisitFakeDataSource: VisitRepository {
    override fun attemptCheckIn(userId: String, buildingName: String): Single<Visit> {
        return Single.create { emitter ->
            val user = User(
                    id = "1234",
                    isStudent = true,
                    firstName = "Tommy",
                    lastName = "Trojan",
                    major = "Computer Science",
                    studentId = "1234"
            )
            emitter.onSuccess(Visit(
                    user = user,
                    buildingName = "Sample Building",
                    checkIn = LocalDateTime.now(),
                    checkOut = null
            ))
        }
    }

    override fun checkOut(userId: String): Single<Visit> {
        return Single.create { emitter ->
            val user = User(
                    id = "1234",
                    isStudent = true,
                    firstName = "Tommy",
                    lastName = "Trojan",
                    major = "Computer Science",
                    studentId = "1234"
            )
            val checkOut = LocalDateTime.now()
            val checkIn = checkOut.minusHours(2)
            emitter.onSuccess(Visit(
                    user = user,
                    buildingName = "Sample Building",
                    checkIn = checkIn,
                    checkOut = checkOut,
            ))
        }
    }

    override fun isCheckedIn(userId: String): Single<Visit> {
        return Single.create { emitter ->
            val user = User(
                    id = "1234",
                    isStudent = true,
                    firstName = "Tommy",
                    lastName = "Trojan",
                    major = "Computer Science",
                    studentId = "1234"
            )
            val checkIn = LocalDateTime.now()
            emitter.onSuccess(Visit(
                    user = user,
                    buildingName = "Sample Building",
                    checkIn = checkIn,
                    checkOut = null
            ))
        }
    }

    override fun queryVisits(user: User, visit: Visit): Single<List<Visit>> {
        return Single.create { emitter ->
            val user1 = User(
                    id = "1234",
                    isStudent = true,
                    firstName = "Tommy",
                    lastName = "Trojan",
                    major = "Computer Science",
                    studentId = "1234"
            )
            val user2 = User(
                    id = "5678",
                    isStudent = true,
                    firstName = "Trojan",
                    lastName = "Tommy",
                    major = "Computer Science",
                    studentId = "5678"
            )

            val visit1 = Visit(
                    user = user1,
                    buildingName = "Sample Building",
                    checkIn = LocalDateTime.now().minusHours(2),
                    checkOut = LocalDateTime.now()
            )

            val visit2 = Visit(
                    user = user2,
                    checkIn = LocalDateTime.now().minusHours(5),
                    checkOut = LocalDateTime.now().minusHours(2)
            )
            val visits = listOf(visit1, visit2)
            emitter.onSuccess(visits)
        }

    }

}