package com.example.androiduitesting;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.espresso.intent.Intents;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class ShowActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> scenario =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {;
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    private void addCity(String name) {
        onView(withId(R.id.button_add)).perform(click());
        onView(withId(R.id.editText_name)).perform(typeText(name));
        onView(withId(R.id.button_confirm)).perform(click());
    }

    @Test
    public void testActivitySwitchesOnClick() {
        addCity("Edmonton");

        // Tap first list item
        onData(is(instanceOf(String.class)))
                .inAdapterView(withId(R.id.city_list))
                .atPosition(0).perform(click());

        // Verify new Activity started
        intended(hasComponent(ShowActivity.class.getName()));
    }

    @Test
    public void testCityNameIsConsistent() {
        addCity("Edmonton");

        onData(is(instanceOf(String.class)))
                .inAdapterView(withId(R.id.city_list))
                .atPosition(0).perform(click());

        onView(withId(R.id.text_city))
                .check(matches(withText("Edmonton")))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testBackButton() {
        addCity("Edmonton");

        onData(is(instanceOf(String.class)))
                .inAdapterView(withId(R.id.city_list))
                .atPosition(0).perform(click());

        // Option A: click in-UI back
        onView(withId(R.id.button_back)).perform(click());
        // Option B (if using system Back): pressBack();

        // We’re back on MainActivity (list is visible)
        onView(withId(R.id.city_list)).check(matches(isDisplayed()));
    }
}
