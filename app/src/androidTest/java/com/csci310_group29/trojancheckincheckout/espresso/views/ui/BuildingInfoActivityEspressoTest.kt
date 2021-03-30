package com.csci310_group29.trojancheckincheckout.espresso.views.ui

import android.content.ClipData.Item
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.runner.AndroidJUnit4
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.domain.models.Building
import com.csci310_group29.trojancheckincheckout.ui.util.EspressoIdlingResource
import com.csci310_group29.trojancheckincheckout.ui.viewmodels.BuildingListAdapter
import com.csci310_group29.trojancheckincheckout.ui.views.*
import it.xabaras.android.espresso.recyclerviewchildactions.RecyclerViewChildActions.Companion.actionOnChild
import it.xabaras.android.espresso.recyclerviewchildactions.RecyclerViewChildActions.Companion.childOfViewAtPositionWithMatcher
import org.hamcrest.CoreMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.junit.*
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class BuildingInfoActivityEspressoTest {

    /**
     * Testing BuildingInfoActivity
     */
    @get:Rule
    val activityRule = ActivityScenarioRule(BuildingInfoActivity::class.java)

    /*
        Building list order and capacity current as of 09:21 EDT Mar 29 2021
     */
    private val LIST_ITEM_UNDER_TEST = 1
    private val BUILDING_UNDER_TEST = "SAL"
    private val CAPACITY_UNDER_TEST = "42"

    companion object {
        init {
        }

        val managerEmail = "espressoManager@usc.edu"
        val managerPassword = "espressoManager"

        @BeforeClass
        @JvmStatic fun setup() {
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
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun displayCheck() {
        onView(withId(R.id.buildingInfo)).check(matches(isDisplayed()))
    }

    @Test
    fun itemCheck() {
        // Match name
        onView(withId(R.id.buildingInfo))
            .check(matches(childOfViewAtPositionWithMatcher(R.id.buildingName,
                LIST_ITEM_UNDER_TEST,
                withText(BUILDING_UNDER_TEST))))
        // Match capacity
        onView(withId(R.id.buildingInfo))
            .check(matches(childOfViewAtPositionWithMatcher(R.id.numOfCapacity,
                LIST_ITEM_UNDER_TEST,
                withText(CAPACITY_UNDER_TEST))))
    }

    @Test
    fun openQrCode_checkBuildingName() {
        onView(withId(R.id.buildingInfo))
            .perform(RecyclerViewActions
                .actionOnItemAtPosition<BuildingListAdapter.ViewHolder>(
                    1,
                    clickItemWithId(R.id.qr_code)))
//        onView(ViewMatchers.withId(R.id.buildingInfo))
//            .perform(actionOnItemAtPosition<BuildingListAdapter.ViewHolder>(
//                LIST_ITEM_UNDER_TEST,
//                actionOnChild(click(), R.id.qr_code)))

        Intents.intended(IntentMatchers.hasComponent(QrCodeActivity::class.java.name))

        // Building name under QR code matches
        onView(withId(R.id.bName)).check(matches(withText(BUILDING_UNDER_TEST)))
    }

//    @Test
//    fun updateBuildings() {
//        val newCap = "42"
//
//        // Match initial capacity
//        onView(withId(R.id.buildingInfo))
//            .check(matches(childOfViewAtPositionWithMatcher(R.id.numOfCapacity,
//                LIST_ITEM_UNDER_TEST,
//                withText(CAPACITY_UNDER_TEST))))
//
//        // Launch UpdateCapacity and update
//        val activityScenario2 = ActivityScenario.launch(ManagerUpdateCapacityActivity::class.java)
//        onView(withId(R.id.BuildingInput)).perform(click())
//        Espresso.onData(CoreMatchers.anything()).atPosition(LIST_ITEM_UNDER_TEST).perform(click())
//        onView(withId(R.id.NewCapacityInput)).perform(typeText(newCap))
//        onView(withId(R.id.UpdateCapacityUI)).perform(click())
//        val activityScenario3 = ActivityScenario.launch(BuildingInfoActivity::class.java)
//
//        // Match initial capacity
//        onView(withId(R.id.buildingInfo))
//            .check(matches(childOfViewAtPositionWithMatcher(R.id.numOfCapacity,
//                LIST_ITEM_UNDER_TEST,
//                withText(newCap))))
//
//        // Launch UpdateCapacity and reset capacity
//        val activityScenario4 = ActivityScenario.launch(ManagerUpdateCapacityActivity::class.java)
//        onView(withId(R.id.BuildingInput)).perform(click())
//        Espresso.onData(CoreMatchers.anything()).atPosition(LIST_ITEM_UNDER_TEST).perform(click())
//        onView(withId(R.id.NewCapacityInput)).perform(typeText(CAPACITY_UNDER_TEST))
//        onView(withId(R.id.UpdateCapacityUI)).perform(click())
//    }

    @After
    fun tearDown() {
        Intents.release()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }


    fun clickItemWithId(id: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View>? {
                return null
            }

            override fun getDescription(): String {
                return "Click on a child view with specified id."
            }

            override fun perform(uiController: UiController, view: View) {
                val v = view.findViewById<View>(id) as View
                v.performClick()
            }
        }
    }
}
