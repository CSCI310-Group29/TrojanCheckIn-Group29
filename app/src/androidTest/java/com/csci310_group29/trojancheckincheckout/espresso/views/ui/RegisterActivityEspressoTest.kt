package com.csci310_group29.trojancheckincheckout.espresso.views.ui

import android.view.View
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.runner.AndroidJUnit4
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.ui.views.LoginActivity
import com.csci310_group29.trojancheckincheckout.ui.views.RegisterActivity
import org.hamcrest.CoreMatchers.anything
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterActivityEspressoTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(RegisterActivity::class.java)


    //vars for tests
    private val validFirst = "First"
    private val validLast = "Last"
    private val validEmail = "email@usc.edu"
    private val invalidEmail = "hi@gmail.com"
    private val validPassword = "password"
    private val validId = "1234567890"

    private val successToastString = "Registered"
    private val unsuccessString = "Unable to register: "

    private var decorView: View? = null

    @Before
    fun setUp() {
        Intents.init()
        activityRule.scenario.onActivity { activity: RegisterActivity ->
            decorView = activity.window.decorView
        }
        //note: when you run these tests change the datasource module to use fake data sources
    }


    @Test
    fun validStudentRegister() {
        onView(withId(R.id.NameInput)).perform(typeText(validFirst))
        onView(withId(R.id.LastNameInput)).perform(typeText(validLast))
        onView(withId(R.id.EmailInput)).perform(typeText(validEmail))
        onView(withId(R.id.PasswordInput)).perform(typeText(validPassword), closeSoftKeyboard())
        onView(withId(R.id.idInput)).perform(typeText(validId), closeSoftKeyboard())

        onView(withId(R.id.major_spinner)).perform(click());
        onData(anything()).atPosition(4).perform(click());
        onView(withId(R.id.registerButton)).perform(click())


        onView(ViewMatchers.withText(successToastString))
            .inRoot(RootMatchers.withDecorView(Matchers.not(decorView)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }

    @Test
    fun noPassword() {
        onView(withId(R.id.NameInput)).perform(typeText(validFirst))
        onView(withId(R.id.LastNameInput)).perform(typeText(validLast))
        onView(withId(R.id.EmailInput)).perform(typeText(validEmail))
        onView(withId(R.id.idInput)).perform(typeText(validId), closeSoftKeyboard())

        onView(withId(R.id.major_spinner)).perform(click());
        onData(anything()).atPosition(4).perform(click());
        onView(withId(R.id.registerButton)).perform(click())


        onView(ViewMatchers.withText(unsuccessString + "Must enter password"))
            .inRoot(RootMatchers.withDecorView(Matchers.not(decorView)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }

    @Test
    fun invalidEmailRegisterStudent() {
        onView(withId(R.id.NameInput)).perform(typeText(validFirst))
        onView(withId(R.id.LastNameInput)).perform(typeText(validLast))
        onView(withId(R.id.EmailInput)).perform(typeText(invalidEmail))
        onView(withId(R.id.PasswordInput)).perform(typeText(validPassword), closeSoftKeyboard())
        onView(withId(R.id.idInput)).perform(typeText(validId), closeSoftKeyboard())

        onView(withId(R.id.major_spinner)).perform(click());
        onData(anything()).atPosition(4).perform(click());
        onView(withId(R.id.registerButton)).perform(click())


        onView(ViewMatchers.withText(unsuccessString + "Must register with a usc email"))
            .inRoot(RootMatchers.withDecorView(Matchers.not(decorView)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun invalidEmailRegisterManager() {
        onView(withId(R.id.NameInput)).perform(typeText(validFirst))
        onView(withId(R.id.LastNameInput)).perform(typeText(validLast))
        onView(withId(R.id.EmailInput)).perform(typeText(invalidEmail))
        onView(withId(R.id.PasswordInput)).perform(typeText(validPassword), closeSoftKeyboard())


        onView(withId(R.id.accountTypeSpinner)).perform(click());
        onData(anything()).atPosition(1).perform(click());
        onView(withId(R.id.registerButton)).perform(click())


        onView(ViewMatchers.withText(unsuccessString + "Must register with a usc email"))
            .inRoot(RootMatchers.withDecorView(Matchers.not(decorView)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun noStudentMajor() {
        onView(withId(R.id.NameInput)).perform(typeText(validFirst))
        onView(withId(R.id.LastNameInput)).perform(typeText(validLast))
        onView(withId(R.id.EmailInput)).perform(typeText(validEmail))
        onView(withId(R.id.PasswordInput)).perform(typeText(validPassword), closeSoftKeyboard())
        onView(withId(R.id.idInput)).perform(typeText(validId), closeSoftKeyboard())


        onView(withId(R.id.registerButton)).perform(click())


        onView(ViewMatchers.withText(unsuccessString + "Must choose a major if you are a student"))
            .inRoot(RootMatchers.withDecorView(Matchers.not(decorView)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun noStudentId() {
        onView(withId(R.id.NameInput)).perform(typeText(validFirst))
        onView(withId(R.id.LastNameInput)).perform(typeText(validLast))
        onView(withId(R.id.EmailInput)).perform(typeText(validEmail))
        onView(withId(R.id.PasswordInput)).perform(typeText(validPassword), closeSoftKeyboard())


        onView(withId(R.id.major_spinner)).perform(click());
        onData(anything()).atPosition(4).perform(click());
        onView(withId(R.id.registerButton)).perform(click())


        onView(ViewMatchers.withText(unsuccessString + "Must enter student id that is 10 digits if you are a student"))
            .inRoot(RootMatchers.withDecorView(Matchers.not(decorView)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun noFirstName() {

        onView(withId(R.id.LastNameInput)).perform(typeText(validLast))
        onView(withId(R.id.EmailInput)).perform(typeText(validEmail))
        onView(withId(R.id.PasswordInput)).perform(typeText(validPassword), closeSoftKeyboard())
        onView(withId(R.id.idInput)).perform(typeText(validId), closeSoftKeyboard())

        onView(withId(R.id.major_spinner)).perform(click());
        onData(anything()).atPosition(4).perform(click());
        onView(withId(R.id.registerButton)).perform(click())


        onView(ViewMatchers.withText(unsuccessString + "Must enter first name"))
            .inRoot(RootMatchers.withDecorView(Matchers.not(decorView)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))


    }

    @Test
    fun noLastName()  {
        onView(withId(R.id.NameInput)).perform(typeText(validFirst))
        onView(withId(R.id.EmailInput)).perform(typeText(validEmail))
        onView(withId(R.id.PasswordInput)).perform(typeText(validPassword), closeSoftKeyboard())
        onView(withId(R.id.idInput)).perform(typeText(validId), closeSoftKeyboard())

        onView(withId(R.id.major_spinner)).perform(click());
        onData(anything()).atPosition(4).perform(click());
        onView(withId(R.id.registerButton)).perform(click())


        onView(ViewMatchers.withText(unsuccessString + "Must enter last name"))
            .inRoot(RootMatchers.withDecorView(Matchers.not(decorView)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }


    @Test
    fun validManagerRegister() {
        onView(withId(R.id.NameInput)).perform(typeText(validFirst))
        onView(withId(R.id.LastNameInput)).perform(typeText(validLast))
        onView(withId(R.id.EmailInput)).perform(typeText(validEmail))
        onView(withId(R.id.PasswordInput)).perform(typeText(validPassword), closeSoftKeyboard())
        onView(withId(R.id.idInput)).perform(typeText(validId), closeSoftKeyboard())

        onView(withId(R.id.major_spinner)).perform(click());
        onData(anything()).atPosition(4).perform(click());
        onView(withId(R.id.registerButton)).perform(click())


        onView(ViewMatchers.withText(successToastString))
            .inRoot(RootMatchers.withDecorView(Matchers.not(decorView)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @After
    fun tearDown() {
        Intents.release()
    }
}