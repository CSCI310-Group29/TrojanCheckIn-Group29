package com.csci310_group29.trojancheckincheckout.espresso.views.ui

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.runner.AndroidJUnit4
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.ui.views.AppHomeActivity
import com.csci310_group29.trojancheckincheckout.ui.views.LoginActivity
import com.csci310_group29.trojancheckincheckout.ui.views.RegisterActivity
import com.csci310_group29.trojancheckincheckout.ui.views.StudentHomeActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppHomeActivityEspressoTest {

    /**
     * Testing AppHomeActivity
     */
    @get:Rule
    val activityRule = ActivityScenarioRule(AppHomeActivity::class.java)

    @Before
    fun setUp() {
        Intents.init()
    }

    @Test
    // Tests that valid student login succeeds
    fun loginStart() {
        Espresso.onView(ViewMatchers.withId(R.id.login)).perform(ViewActions.click())

        // Should start LoginActivity
        Intents.intended(IntentMatchers.hasComponent(LoginActivity::class.java.name))
    }

    @Test
    // Tests that valid student login succeeds
    fun registerStart() {
        Espresso.onView(ViewMatchers.withId(R.id.signup)).perform(ViewActions.click())

        // Should start RegisterActivity
        Intents.intended(IntentMatchers.hasComponent(RegisterActivity::class.java.name))
    }

    @After
    fun tearDown() {
        Intents.release()
    }

}