package com.csci310_group29.trojancheckincheckout.espresso.views.ui

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.runner.AndroidJUnit4
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.ui.views.*
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ManagerHomeActivityEspressoTest {

    /**
     * Testing ManagerHomeActivity
     */
    @get:Rule
    val activityRule = ActivityScenarioRule(ManagerHomeActivity::class.java)

    companion object {
        init {
        }

        val managerEmail = "espressoManager@usc.edu"
        val managerPassword = "espressoManager"

        @BeforeClass @JvmStatic fun setup() {
            val activityScenario = ActivityScenario.launch(LoginActivity::class.java)
            Espresso.onView(ViewMatchers.withId(R.id.emailInput))
                .perform(ViewActions.typeText(managerEmail), ViewActions.closeSoftKeyboard())
            Espresso.onView(ViewMatchers.withId(R.id.passwordInput))
                .perform(ViewActions.typeText(managerPassword), ViewActions.closeSoftKeyboard())
            Espresso.onView(ViewMatchers.withId(R.id.loginButton)).perform(ViewActions.click())
            Thread.sleep(5000)
        }
    }

    @Before
    fun setUp() {
        Intents.init()
    }

    @Test
    fun checkFirstNameDisplay() {
        Espresso.onView(ViewMatchers.withId(R.id.NameManager)).check(matches(withText("espressoManager")))
    }

    @Test
    fun managerBuildingListStart() {
        Espresso.onView(ViewMatchers.withId(R.id.checkBuilding)).perform(ViewActions.click())

        // Should start StudentHomeActivity
        Intents.intended(IntentMatchers.hasComponent(BuildingInfoActivity::class.java.name))
    }

    @Test
    fun managerUpdateCapacityStart() {
        Espresso.onView(ViewMatchers.withId(R.id.button5)).perform(ViewActions.click())

        // Should start StudentHomeActivity
        Intents.intended(IntentMatchers.hasComponent(ManagerUpdateCapacityActivity::class.java.name))
    }

    @Test
    fun managerSearchStart() {
        Espresso.onView(ViewMatchers.withId(R.id.button7)).perform(ViewActions.click())

        // Should start StudentHomeActivity
        Intents.intended(IntentMatchers.hasComponent(VisitQueryActivity::class.java.name))
    }

    @Test
    fun managerProfileStart() {
        Espresso.onView(ViewMatchers.withId(R.id.ViewProfileButton)).perform(ViewActions.click())

        // Should start StudentHomeActivity
        Intents.intended(IntentMatchers.hasComponent(ManagerProfileActivity::class.java.name))
    }

    @Test
    fun managerLogoutStart() {
        Espresso.onView(ViewMatchers.withId(R.id.LogoutButton)).perform(ViewActions.click())

        // Should start StudentHomeActivity
        Intents.intended(IntentMatchers.hasComponent(AppHomeActivity::class.java.name))
    }

    @After
    fun tearDown() {
        Intents.release()
    }
}