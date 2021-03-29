package com.csci310_group29.trojancheckincheckout.espresso.views.ui

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.domain.models.Building
import com.csci310_group29.trojancheckincheckout.ui.viewmodels.Session
import com.csci310_group29.trojancheckincheckout.ui.views.LoginActivity
import com.csci310_group29.trojancheckincheckout.ui.views.StudentHomeActivity
import org.hamcrest.Matchers
import org.junit.*
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class CheckInOutEspressoTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @get:Rule
    var mRuntimePermissionRule = GrantPermissionRule.grant(android.Manifest.permission.CAMERA)

    companion object {
        init {
        }
        val studentEmail = "student@usc.edu"
        val studentPassword = "student"
        @BeforeClass @JvmStatic fun setup() {
            /*val activityScenario = ActivityScenario.launch(LoginActivity::class.java)
            Espresso.onView(ViewMatchers.withId(R.id.emailInput))
                .perform(ViewActions.typeText(studentEmail), ViewActions.closeSoftKeyboard())
            Espresso.onView(ViewMatchers.withId(R.id.passwordInput))
                .perform(ViewActions.typeText(studentPassword), ViewActions.closeSoftKeyboard())
            Espresso.onView(ViewMatchers.withId(R.id.loginButton)).perform(ViewActions.click())
            Thread.sleep(5000)*/
        }
    }

    private var decorView: View? = null

    @Before
    fun setUp() {
        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)
        Espresso.onView(ViewMatchers.withId(R.id.emailInput))
            .perform(ViewActions.typeText(studentEmail), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.passwordInput))
            .perform(ViewActions.typeText(studentPassword), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.loginButton)).perform(ViewActions.click())
        Thread.sleep(2000)

        Intents.init()


//        activityRule.scenario.onActivity { activity: StudentHomeActivity ->
//            decorView = activity.window.decorView
//        }

    }

    @Test
    fun checkIn() {
        //note: for this test case you must have the user not checked into any building in the db
        val bitmap = BitmapFactory.decodeResource(InstrumentationRegistry.getInstrumentation().targetContext.resources, R.drawable.qrcode)
        val resultData = Intent()
        resultData.putExtra("data", bitmap)
        val result: Instrumentation.ActivityResult =
            Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)

        intending(toPackage("com.android.camera2")).respondWith(result);

        onView(withId(R.id.button2)).perform(click())

        Thread.sleep(2000)


        onView(ViewMatchers.withText("Successfully checked into GFS"))
            .inRoot(RootMatchers.withDecorView(Matchers.not(decorView)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun checkInInvalidQr() {
        //note: for this test case you must have the user not checked into any building in the db
        val bitmap = BitmapFactory.decodeResource(InstrumentationRegistry.getInstrumentation().targetContext.resources, R.drawable.invalidqr)
        val resultData = Intent()
        resultData.putExtra("data", bitmap)
        val result: Instrumentation.ActivityResult =
            Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)

        intending(toPackage("com.android.camera2")).respondWith(result);

        onView(withId(R.id.button2)).perform(click())

        Thread.sleep(2000)


        onView(ViewMatchers.withText("Could not check into the building"))
            .inRoot(RootMatchers.withDecorView(Matchers.not(decorView)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun checkOutInvalidQr() {
        //note: for this test case you must have the user checked into a building in the db
        Session.checkedInBuilding = Building("Ax8j8movNxKM6eb3HIRf","", "", 0,0,"")

        val bitmap = BitmapFactory.decodeResource(InstrumentationRegistry.getInstrumentation().targetContext.resources, R.drawable.invalidqr)
        val resultData = Intent()
        resultData.putExtra("data", bitmap)
        val result: Instrumentation.ActivityResult =
            Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)

        intending(toPackage("com.android.camera2")).respondWith(result);

        onView(withId(R.id.button2)).perform(click())

        Thread.sleep(2000)


        onView(ViewMatchers.withText("Check out before you can check into another building"))
            .inRoot(RootMatchers.withDecorView(Matchers.not(decorView)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun checkOutScan() {
        //note: for this test case you must have the user not checked into a building in the db
        Session.checkedInBuilding = Building("Ax8j8movNxKM6eb3HIRf","", "", 0,0,"")

        val bitmap = BitmapFactory.decodeResource(InstrumentationRegistry.getInstrumentation().targetContext.resources, R.drawable.qrcode)
        val resultData = Intent()
        resultData.putExtra("data", bitmap)
        val result: Instrumentation.ActivityResult =
            Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)

        intending(toPackage("com.android.camera2")).respondWith(result);

        onView(withId(R.id.button2)).perform(click())

        Thread.sleep(1000)

        onView(ViewMatchers.withText("Successfully checked out of GFS"))
            .inRoot(RootMatchers.withDecorView(Matchers.not(decorView)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun checkOutManual() {
        //note: for this test case you must have the user checked into a building in the db
        Session.checkedInBuilding = Building("Ax8j8movNxKM6eb3HIRf","", "", 0,0,"")

        onView(withId(R.id.button)).perform(click())

        Thread.sleep(1000)

        onView(ViewMatchers.withText("Checked out of GFS"))
            .inRoot(RootMatchers.withDecorView(Matchers.not(decorView)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }

    @Test
    fun checkOutNotCheckedIn() {
        //note: for this test case you must have the user not checked into any building in the db
        onView(withId(R.id.button)).perform(click())

        Thread.sleep(1000)

        onView(ViewMatchers.withText("Must check into building before checking out"))
            .inRoot(RootMatchers.withDecorView(Matchers.not(decorView)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }


    fun getCurrentActivity(): Activity? {
        var currentActivity: Activity? = null
        getInstrumentation().runOnMainSync { run { currentActivity = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(
            Stage.RESUMED).elementAtOrNull(0) } }
        return currentActivity
    }

    @After
    fun tearDown() {
        Intents.release()
    }
}