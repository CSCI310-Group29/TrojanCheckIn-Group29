
package com.csci310_group29.trojancheckincheckout.ui.viewmodels
import android.util.Log
import androidx.lifecycle.ViewModel
import com.csci310_group29.trojancheckincheckout.domain.entities.UserEntity
import com.csci310_group29.trojancheckincheckout.domain.usecases.AuthUseCases
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable
import javax.inject.Inject

@RunWith(MockitoJUnitRunner::class)
class RegisterViewModel @Inject constructor(private val authDomain: AuthUseCases): ViewModel() {


    @Mock
    private lateinit var mockRegister: register


    @Before

    @Test
    public fun register(email:String, password:String, user: UserEntity): Completable {

    `when`(mockRegister.register()).thenReturn(Completable.complete())

    }


    //@Test
  //  private fun getEmailDomain(email: String): String {



  //  }


}
