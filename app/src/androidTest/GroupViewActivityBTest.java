package com.example.udacity.test;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class GroupViewActivityBTest {

    @Rule
    public ActivityTestRule<GroupViewActivity> groupViewActivityTestRule =
            new ActivityTestRule<GroupViewActivity>(GroupViewActivity.class);

    @Before
    public void init() {

    }

    /**
     * Test that removing a group should make it disappear from list of groups
     */
    @Test
    public void removeGroup_groupViewActivity() {
        String groupName = "aaa";

        // add a group
        onView(withId(R.id.gvMakeGroupButton)).perform(click());
        onView(withId(R.id.grpNameTextview)).perform(typeText(groupName));
        closeSoftKeyboard();
        onView(withId(R.id.mgDoneButton)).perform(click());

        // remove the group
        //onView(withId(R.id.leaveViewbutton)).perform(click());
        //onData(allOf(is(instanceOf(GroupModel.class)), hasEntry(equalTo("name"),
         //       is(groupName)))).onChildView(withId(R.id.leaveViewbutton)).perform(click());

        // check
        //onView(withText(groupName)).check(doesNotExist());
    }


    @Test
    /*
     * makes one group and checks if it shows up on list of groups view
     */
    public void addOneGroup_groupViewActivity() {
        // create group
        String groupName = "cs310";
        onView(withId(R.id.gvMakeGroupButton)).perform(click());
        onView(withId(R.id.grpNameTextview)).perform(typeText(groupName));
        closeSoftKeyboard();
        onView(withId(R.id.mgDoneButton)).perform(click());

        // check
        onView(withId(R.id.groupViewName)).check(matches(withText(groupName)));
    }


    @Test
    /*
     * makes multiple group and checks if they all show up on list of groups view
     */
    public void addMultipleGroups_groupViewActivity() {
        String groupName1 = "Juniors";
        String groupName2 = "Seniors";

        onView(withId(R.id.gvMakeGroupButton)).perform(click());
        onView(withId(R.id.grpNameTextview)).perform(typeText(groupName1));
        closeSoftKeyboard();
        onView(withId(R.id.mgDoneButton)).perform(click());

        onView(withId(R.id.gvMakeGroupButton)).perform(click());
        onView(withId(R.id.grpNameTextview)).perform(typeText(groupName2));
        closeSoftKeyboard();
        onView(withId(R.id.mgDoneButton)).perform(click());

        // check if both group names exist
        onData(allOf(is(instanceOf(GroupModel.class)), hasEntry(equalTo("name"), is(groupName1))));
        onData(allOf(is(instanceOf(GroupModel.class)), hasEntry(equalTo("name"), is(groupName2))));
    }

    @Test
    /*
     * makes multiple group and checks if they all show up on list of groups view
     */
    public void selectFriend_makeGroupActivity() {
        String groupName = "Friends";
        String friendName = "Molly He";

        onView(withId(R.id.gvMakeGroupButton)).perform(click());
        onView(withId(R.id.grpNameTextview)).perform(typeText(groupName));
        closeSoftKeyboard();
        onView(withId(R.id.mgDoneButton)).perform(click());

        // check if friend's name exist
        onData(allOf(is(instanceOf(FriendModel.class)), hasEntry(equalTo("name"), is(friendName))));
    }
}















