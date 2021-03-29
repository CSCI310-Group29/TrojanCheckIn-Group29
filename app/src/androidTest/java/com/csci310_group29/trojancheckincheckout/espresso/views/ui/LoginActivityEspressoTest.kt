package com.csci310_group29.trojancheckincheckout.espresso.views.ui

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Root
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.runner.AndroidJUnit4
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.ui.views.LoginActivity
import com.csci310_group29.trojancheckincheckout.ui.views.ManagerHomeActivity
import com.csci310_group29.trojancheckincheckout.ui.views.StudentHomeActivity
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
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

    // Local vars for testing
    private val validEmailStudent = "espressoStudent@usc.edu"
    private val validPasswordStudent = "espressoStudent"
    private val validEmailManager = "espressoManager@usc.edu"
    private val validPasswordManager = "espressoManager"
    private val invalidEmail = "bruin@gmail.com"
    private val invalidPassword = "1234"

    private var decorView: View? = null

    @Before
    fun setUp() {
        Intents.init()
        activityRule.scenario.onActivity { activity: LoginActivity ->
            decorView = activity.window.decorView
        }
    }


    @Test
    // Tests that valid student login succeeds
    fun validLoginStudent() {
        onView(withId(R.id.emailInput)).perform(typeText(validEmailStudent), closeSoftKeyboard())
        onView(withId(R.id.passwordInput)).perform(typeText(validPasswordStudent), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())
        Thread.sleep(5000)

        // Should start StudentHomeActivity
        intended(hasComponent(StudentHomeActivity::class.java.name))
    }

    @Test
    // Tests that valid manager login succeeds
    fun validLoginManager() {
        onView(withId(R.id.emailInput)).perform(typeText(validEmailManager), closeSoftKeyboard())
        onView(withId(R.id.passwordInput)).perform(typeText(validPasswordManager), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())
        Thread.sleep(5000)

        // Should start ManagerHomeActivity
        intended(hasComponent(ManagerHomeActivity::class.java.name))
    }

    @Test
    // Tests incorrect credentials will show error Toast
    fun invalidLoginToast() {
        onView(withId(R.id.emailInput)).perform(typeText(invalidEmail), closeSoftKeyboard())
        onView(withId(R.id.passwordInput)).perform(typeText(invalidPassword), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

        // Should show toast "Unable to login: email or password incorrect"
        val toastString = "email or password incorrect"
        onView(withText(toastString))
            .inRoot(RootMatchers.withDecorView(not(decorView)))
            .check(matches(isDisplayed()))
    }


    @After
    fun tearDown() {
        Intents.release()
    }
}