package com.csci310_group29.trojancheckincheckout.espresso.views.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.runner.AndroidJUnit4
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.ui.views.LoginActivity
import com.csci310_group29.trojancheckincheckout.ui.views.StudentHomeActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityEspressoTest {

    /**
     * Testing LoginActivity
     */
    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)


    @Test
    // Tests that valid student login succeeds
    fun validLoginStudent () {
        val validEmailStudent = "ttrojan@usc.edu"
        val validPasswordStudent = "ttrojan"

        onView(withId(R.id.emailInput)).perform(typeText(validEmailStudent))
        onView(withId(R.id.passwordInput)).perform(typeText(validPasswordStudent))
        onView(withId(R.id.loginButton)).perform(click())

        intended(hasComponent(StudentHomeActivity::class.java.getName()))
    }
}