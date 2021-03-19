

package com.csci310_group29.trojancheckincheckout.views
/*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.data.models.User
import com.csci310_group29.trojancheckincheckout.viewmodels.LoginViewModel
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_login.*

private const val TAG = "LoginActivity"

class LoginActivity : AppCompatActivity() {
    private val loginViewModel = LoginViewModel()
    private var user = User()
    private var pb: ProgressBar? = null

    public fun getActivity(): LoginActivity {
        return this;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        pb = findViewById(R.id.indeterminateBar)
        loadingEnd()


    }

    public fun onLogin(view: View) {
        try {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        } catch (e: java.lang.Exception) {
        }

        val email = emailInput.text.toString()
        val password = passwordInput.text.toString()
        var dis: Disposable? = null
        try {

            //Log.i(TAG,"User returned is: " + user.firstName + " " + user.lastName + " " + user.major + " " + user.isStudent)
            val observable = loginViewModel.login(email,password)
            observable.subscribe(object: SingleObserver<User> {

                override fun onSuccess(t: User) {
                    loadingEnd()
                    loginNextActivity(t)
                    dis!!.dispose()
                }

                override fun onSubscribe(d: Disposable) {
                    dis = d
                    loadingStart()
                }

                override fun onError(e: Throwable) {
                    loadingEnd()
                    dis!!.dispose()
                }
            })



        } catch(e: Exception) {
            //Log.i(TAG, "exception returned to onLogin in LoginActivity " + e.localizedMessage)
            val toast = Toast.makeText(this, "Unable to Login: " + e.localizedMessage, Toast.LENGTH_SHORT)
            toast.show()
        }
    }

    fun loadingStart() {
        pb!!.visibility = ProgressBar.VISIBLE
    }

    fun loadingEnd() {
        pb!!.visibility = ProgressBar.INVISIBLE
    }


    fun loginNextActivity(user: User) {
        if(user != null && user!!.isStudent!!) {
            startActivity(Intent(this,StudentHomeActivity::class.java))
            finish()
        } else if ( user != null) {
            startActivity(Intent(this, ManagerHomeActivity::class.java))
            finish()
        } else {
            throw Exception("User is null");
        }
    }

}
*/
