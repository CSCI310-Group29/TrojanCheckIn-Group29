package com.csci310_group29.trojancheckincheckout.espresso.views.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.runner.AndroidJUnit4
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.ui.views.LoginActivity
import com.csci310_group29.trojancheckincheckout.ui.views.ManagerHomeActivity
import com.csci310_group29.trojancheckincheckout.ui.views.StudentHomeActivity
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
//    val intentsRule = IntentsTestRule(LoginActivity::class.java)
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

//    private var mIdlingResource: IdlingResource? = null
//    private lateinit var mIdlingResource: IdlingResource
    // Local vars for testing
    private val validEmailStudent = "espressoStudent@usc.edu"
    private val validPasswordStudent = "espressoStudent"
    private val validEmailManager = "espressoManager@usc.edu"
    private val validPasswordManager = "espressoManager"
    private val invalidEmail = "bruin@gmail.com"
    private val invalidPassword = "1234"


//    @Before
//    fun registerIdlingResource() {
//        val activityScenario: ActivityScenario<*> = ActivityScenario.launch(LoginActivity::class.java)
//        activityScenario.onActivity(ActivityScenario.ActivityAction<LoginActivity> { activity ->
//            mIdlingResource = activity.getIdlingResourceInTest()
//        })
//
////        ActivityScenario.ActivityAction<LoginActivity> { activity ->
////            mIdlingResource = activity.getIdlingResourceInTest()
////            // To prove that the test fails, omit this call:
////            IdlingRegistry.getInstance().register(mIdlingResource)
//    }
    @Before
    fun setUp() {
        Intents.init()
    }

    @Test
    // Tests that valid student login succeeds
    fun validLoginStudent() {
//        val idlingResource : CountingIdlingResource? = intentsRule.activity.getIdlingResourceInTest()
//        Espresso.registerIdlingResources(idlingResource)

//        val componentIdlingResource: CountingIdlingResource =
//            activityScenario. .getIdlingResourceInTest()


//        Intents.init()
        onView(withId(R.id.emailInput)).perform(typeText(validEmailStudent))
        onView(withId(R.id.passwordInput)).perform(typeText(validPasswordStudent))
        onView(withId(R.id.loginButton)).perform(click())

        // Should start StudentHomeActivity
        intended(hasComponent(StudentHomeActivity::class.java.name))
//        Intents.release()
    }

    @Test
    // Tests that valid manager login succeeds
    fun validLoginManager() {
//        Intents.init()
        onView(withId(R.id.emailInput)).perform(typeText(validEmailManager))
        onView(withId(R.id.passwordInput)).perform(typeText(validPasswordManager))
        onView(withId(R.id.loginButton)).perform(click())

        // Should start ManagerHomeActivity
        intended(hasComponent(ManagerHomeActivity::class.java.name))
//        Intents.release()
    }

//    @Test
//    // Tests incorrect credentials will stay on LoginActivity
//    fun invalidLoginReturnToLogin() {
//        onView(withId(R.id.emailInput)).perform(typeText(invalidEmail))
//        onView(withId(R.id.passwordInput)).perform(typeText(invalidPassword))
//        onView(withId(R.id.loginButton)).perform(click())
//
//        // Should remain on LoginActivity
//
//    }

    @Test
    // Tests incorrect credentials will show error Toast
    fun invalidLoginToast() {
        onView(withId(R.id.emailInput)).perform(typeText(invalidEmail))
        onView(withId(R.id.passwordInput)).perform(typeText(invalidPassword))
        onView(withId(R.id.loginButton)).perform(click())

        // Should show toast "Unable to login: email or password incorrect"
        val toastString = "email or password incorrect"
        onView(withText("email or password incorrect"))
            .inRoot(ToastMatcher()).check(matches(isDisplayed()))
    }

//    protected fun afterActivityLaunched() {
//        Intents.init()
//        super.afterActivityLaunched()
//    }
//
//    protected fun afterActivityFinished() {
//        super.afterActivityFinished()
//        Intents.release()
//    }

//    @After
//    fun unregisterIdlingResource() {
//        if (mIdlingResource != null) {
//            IdlingRegistry.getInstance().unregister(mIdlingResource)
//        }
//    }

    @After
    fun tearDown() {
        Intents.release()
    }
}