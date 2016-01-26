package com.moiseenko.gitapp;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Viktar_Maiseyenka on 22.01.2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginEspressoTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    @Test
    public void login() {
//        onView(withText("Hello World!")).check(matches(isDisplayed()));
        onView(withId(R.id.username)).perform(typeText("google"), closeSoftKeyboard()).check(matches(withText("google")));
        onView(withId(R.id.authSwitch)).perform(click());
        onView(withId(R.id.bLogin)).perform(click()).perform();
//        onData(getI)
        onView(withId(R.id.recycler_view)).perform(swipeUp());
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));

    }

//    @Test
//    public void loginWithPassword() {
////        onView(withText("Hello World!")).check(matches(isDisplayed()));
//        onView(withId(R.id.username)).perform(typeText("vmoiseenko"), closeSoftKeyboard()).check(matches(withText("vmoiseenko")));
//        onView(withId(R.id.password)).perform(typeText("vmoiseenko"), closeSoftKeyboard()).check(matches(withText("vmoiseenko")));
//        onView(withId(R.id.bLogin)).perform(click()).perform();
//    }

}