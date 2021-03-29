package com.csci310_group29.trojancheckincheckout.ui.viewmodels

import android.util.Log
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.usecases.UserUseCases
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import javax.inject.Inject

@RunWith(MockitoJUnitRunner::class)
class SplashScreenViewModel @Inject constructor(private val userDomain: UserUseCases) {


    @Mock
    private lateinit var mockisLoggedIn: isLoggedIn


    @Before

    @Test
    fun isLoggedIn(): Single<User> {

      `when`(mockRegister.isLoggedIn()).thenReturn(Completable.complete())

    }
}
