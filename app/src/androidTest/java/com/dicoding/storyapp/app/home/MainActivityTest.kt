package com.dicoding.storyapp.app.home


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.dicoding.storyapp.R
import com.dicoding.storyapp.app.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import androidx.test.espresso.action.ViewActions
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest{

    @get:Rule
    val activity = ActivityScenarioRule(MainActivity::class.java)


    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)

    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun clickButtonAdd(){
        onView(withId(R.id.floatingActionButton)).perform(click())
        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val allowButton = uiDevice.findObject(UiSelector().text("WHILE USING THE APP")) // Replace with the text on the Allow button in your language/locale
        if (allowButton.exists() && allowButton.isEnabled) {
            allowButton.click()
        }
        //Open Camera
        onView(withId(R.id.camera_btn)).perform(click())

        //Write Desc
        onView(withId(R.id.ed_deskripsi)).perform(typeText("huauauau"))
        onView(isRoot()).perform(ViewActions.pressBack())
        onView(withId(R.id.btnPostStory)).perform(click())

    }







}