package com.csci310_group29.trojancheckincheckout.espresso.views.ui

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.isInternal
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.ui.views.LoginActivity
import com.csci310_group29.trojancheckincheckout.ui.views.StudentProfileActivity
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher
import org.junit.*
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class StudentProfileEspressoTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(StudentProfileActivity::class.java)

    @get:Rule
    var mRuntimePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.READ_EXTERNAL_STORAGE)

    private var decorView: View? = null

    companion object {
        init {
        }

        val studentEmail = "student@usc.edu"
        val studentPassword = "student"

        @BeforeClass
        @JvmStatic fun setup() {
            val activityScenario = ActivityScenario.launch(LoginActivity::class.java)
            Espresso.onView(ViewMatchers.withId(R.id.emailInput))
                .perform(ViewActions.typeText(studentEmail), ViewActions.closeSoftKeyboard())
            Espresso.onView(ViewMatchers.withId(R.id.passwordInput))
                .perform(ViewActions.typeText(studentPassword), ViewActions.closeSoftKeyboard())
            Espresso.onView(ViewMatchers.withId(R.id.loginButton)).perform(ViewActions.click())
            Thread.sleep(2000)
            Espresso.onView(ViewMatchers.withId(R.id.button3)).perform(ViewActions.click())
        }
    }



    @Before
    fun setUp() {
        Intents.init()


        /*activityRule.scenario.onActivity { activity: LoginActivity ->
            decorView = activity.window.decorView
        }*/

    }

    @Test
    fun updateProfilePic() {
        val path = "/storage/emulated/0/Download/tennis_ball.jpg"
        val resultData = Intent()
        resultData.data = Uri.fromFile(File(path))
        val result: Instrumentation.ActivityResult =
            Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)

        intending(not(isInternal())).respondWith(result);

        Espresso.onView(ViewMatchers.withId(R.id.SProfilePic)).perform(ViewActions.click())
        Thread.sleep(10000)
    }


    @After
    fun tearDown() {
        Intents.release()
    }
}