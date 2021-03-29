package com.csci310_group29.trojancheckincheckout.espresso.views.ui

import android.provider.MediaStore
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.ui.views.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StudentHomeEspressoTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @get:Rule
    var mRuntimePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.CAMERA)

    private var decorView: View? = null

    @Before
    fun setUp() {
        Intents.init()
        onView(withId(R.id.emailInput)).perform(
            ViewActions.typeText("student@usc.edu"),
            ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.passwordInput)).perform(
            ViewActions.typeText("student"),
            ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.loginButton)).perform(click())

        activityRule.scenario.onActivity { activity: LoginActivity ->
            decorView = activity.window.decorView
        }
        //note: when you run these tests change the datasource module to use fake data sources
    }

    @Test
    fun successfulLogout() {

        onView(withId(R.id.button4)).perform(click())
        Thread.sleep(2000)

        Intents.intended(IntentMatchers.hasComponent(AppHomeActivity::class.java.name))
    }

    @Test
    fun goToProfile() {
        onView(withId(R.id.button3)).perform(click())
        Thread.sleep(2000)

        Intents.intended(IntentMatchers.hasComponent(StudentProfileActivity::class.java.name))
    }

    @Test
    fun scanOpenCamera() {
        onView(withId(R.id.button2)).perform(click())
        Thread.sleep(2000)

        Intents.intended(IntentMatchers.hasAction(MediaStore.ACTION_IMAGE_CAPTURE))
    }



    @After
    fun tearDown() {
        Intents.release()
    }


}