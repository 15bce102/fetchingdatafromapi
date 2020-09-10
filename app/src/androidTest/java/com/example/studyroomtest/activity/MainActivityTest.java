package com.example.studyroomtest.activity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.rule.ActivityTestRule;

import static org.junit.Assert.*;

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    private MainActivity mainActivity = null;

    @Before
    public void setUp() {
        mainActivity = mActivityTestRule.getActivity();
    }

    @Test
    public void checkNetwork(){
        Boolean b = mainActivity.isNetworkAvailable();
        assertEquals(b,true);
    }

    @After
    public void tearDown() {

        mainActivity = null;
    }
}