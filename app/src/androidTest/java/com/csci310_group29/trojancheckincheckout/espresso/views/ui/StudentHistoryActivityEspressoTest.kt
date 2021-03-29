package com.csci310_group29.trojancheckincheckout.espresso.views.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.runner.AndroidJUnit4
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.ui.views.BuildingInfoActivity
import com.csci310_group29.trojancheckincheckout.ui.views.LoginActivity
import com.csci310_group29.trojancheckincheckout.ui.views.StudentHistoryActivity
import com.csci310_group29.trojancheckincheckout.ui.views.StudentProfileActivity
import it.xabaras.android.espresso.recyclerviewchildactions.RecyclerViewChildActions
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StudentHistoryActivityEspressoTest {

    /**
     * Testing StudentHistoryActivity
     */
    @get:Rule
    val activityRule = ActivityScenarioRule(StudentHistoryActivity::class.java)

    private val LIST_ITEM_UNDER_TEST = 1
    private val BUILDING_UNDER_TEST = "GFS"
    // FORMAT: "Day[3] Month[3] Date Hour:Minute:Second Timezone Year"
    private val CHECKIN_UNDER_TEST = "Mon Mar 29 03:38:03 EDT 2021"
    private val CHECKOUT_UNDER_TEST = "Mon Mar 29 03:53:54 EDT 2021"

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
            Thread.sleep(5000)

        }
    }

    @Before
    fun setUp() {
        Intents.init()
    }

    @Test
    fun displayCheck() {
        Espresso.onView(ViewMatchers.withId(R.id.visitHistory))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun itemCheck() {
        Thread.sleep(3000)

        // Match Building
        Espresso.onView(ViewMatchers.withId(R.id.visitHistory))
            .check(
                ViewAssertions.matches(
                    RecyclerViewChildActions.childOfViewAtPositionWithMatcher(
                        R.id.historyBuilding,
                        LIST_ITEM_UNDER_TEST,
                        ViewMatchers.withText(BUILDING_UNDER_TEST)
                    )
                )
            )

        // Match Check In
        Espresso.onView(ViewMatchers.withId(R.id.visitHistory))
            .check(
                ViewAssertions.matches(
                    RecyclerViewChildActions.childOfViewAtPositionWithMatcher(
                        R.id.historyCheckIn,
                        LIST_ITEM_UNDER_TEST,
                        ViewMatchers.withText(CHECKIN_UNDER_TEST)
                    )
                )
            )

        // Match Check Out
        Espresso.onView(ViewMatchers.withId(R.id.visitHistory))
            .check(
                ViewAssertions.matches(
                    RecyclerViewChildActions.childOfViewAtPositionWithMatcher(
                        R.id.historyCheckOut,
                        LIST_ITEM_UNDER_TEST,
                        ViewMatchers.withText(CHECKOUT_UNDER_TEST)
                    )
                )
            )
    }


    @After
    fun tearDown() {
        Intents.release()
    }

}