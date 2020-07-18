package kiz.learnwithvel.notes.ui.notelist;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import kiz.learnwithvel.notes.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.doubleClick;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;


/**
 * sources:
 * https://stackoverflow.com/questions/44822660/espresso-recyclerviewactions-actiononitem-error
 * https://stackoverflow.com/questions/37736616/espresso-how-to-find-a-specific-item-in-a-recycler-view-order-is-random
 * https://developer.android.com/studio/debug/dev-options.html
 * https://stackoverflow.com/questions/42272683/get-recyclerview-total-item-count-in-espresso
 * https://stackoverflow.com/questions/57356838/how-to-change-the-app-screen-orientation-using-espresso
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class NoteListActivityTest {

    public static final String TYPE_TEXT_TITLE = "I love Espresso";
    public static final String TYPE_UPDATE_TEXT_TITLE = "Need to consider";
    public static final String TYPE_TEXT_CONTENT = "Testing with Android Junit 4!";
    public static final String TYPE_UPDATE_TEXT_CONTENT = "Learn to unit testing with Robolectric.";

    @Rule
    public ActivityTestRule<NoteListActivity> activityTestRule
            = new ActivityTestRule<NoteListActivity>(NoteListActivity.class);


    @Test
    public void addEditDeleteNote() {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.note_edit_title)).perform(clearText());
        onView(withId(R.id.note_edit_title)).perform(typeText("ariel"));
        ViewInteraction interaction = onView(withText("ariel")).check(ViewAssertions.matches(isDisplayed()));
        interaction.withFailureHandler((error, viewMatcher) -> {
            error.getMessage();
        });
        onView(withId(R.id.note_text)).perform(typeText("pogi"));
        onView(withId(R.id.toolbar_check)).perform(click());
        onView(withId(R.id.toolbar_back_arrow)).perform(click());

        onView(withId(R.id.recyclerview)).perform(actionOnItemAtPosition(0, ViewActions.swipeLeft()));
        onView(withId(R.id.recyclerview)).perform(actionOnItem(hasDescendant(withText("ariel")), click()).atPosition(0));
        onView(withId(R.id.note_text)).perform(clearText());
        onView(withId(R.id.note_text)).perform(typeText("sobra palang pogi hahaha"));
        onView(isRoot()).perform(pressBack());
    }

    @Test
    public void addEditUpdateDelete() {

        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.note_edit_title)).perform(clearText());
        onView(withId(R.id.note_edit_title)).perform(typeText(TYPE_TEXT_TITLE));
        onView(withId(R.id.note_text)).perform(typeText(TYPE_TEXT_CONTENT));
        onView(withId(R.id.toolbar_check)).perform(click());
        onView(withId(R.id.note_text)).perform(doubleClick());
        onView(withId(R.id.note_text)).perform(clearText());
        onView(withId(R.id.note_text)).perform(typeText(TYPE_UPDATE_TEXT_CONTENT));
        onView(withId(R.id.toolbar_check)).perform(click());
        onView(withId(R.id.toolbar_back_arrow)).perform(click());

        onView(withId(R.id.recyclerview)).perform(actionOnItem(hasDescendant(withText(TYPE_TEXT_TITLE)), click()));

        onView(withId(R.id.toolbar_check)).perform(click());
        onView(withId(R.id.note_text_title)).perform(click());
        onView(withId(R.id.note_edit_title)).perform(clearText());
        onView(withId(R.id.note_edit_title)).perform(typeText(TYPE_UPDATE_TEXT_TITLE));
        onView(withId(R.id.toolbar_check)).perform(click());
        onView(withId(R.id.toolbar_back_arrow)).perform(click());

        onView(withId(R.id.recyclerview)).perform(actionOnItem(hasDescendant(withText(TYPE_UPDATE_TEXT_TITLE)), swipeRight()));

    }

    @Test
    public void addRotateEditDelete() throws Exception {
        //worked but slow responds...

        UiDevice device = UiDevice.getInstance(getInstrumentation());

        //add
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.note_edit_title)).perform(clearText());
        onView(withId(R.id.note_edit_title)).perform(typeText(TYPE_TEXT_TITLE));
        onView(withId(R.id.note_text)).perform(typeText(TYPE_TEXT_CONTENT));
        onView(withId(R.id.toolbar_check)).perform(click());

        //edit
        onView(withId(R.id.note_text)).perform(doubleClick());
        device.setOrientationRight();
        onView(withId(R.id.note_edit_title)).perform(clearText());
        onView(withId(R.id.note_edit_title)).perform(typeText(TYPE_UPDATE_TEXT_TITLE), closeSoftKeyboard());
        onView(withId(R.id.note_text)).perform(clearText());
        onView(withId(R.id.note_text)).perform(typeText(TYPE_UPDATE_TEXT_CONTENT));

        //update
        device.setOrientationNatural();
        onView(withId(R.id.toolbar_check)).perform(click());
        onView(withId(R.id.toolbar_back_arrow)).perform(click());

        //delete
        onView(withId(R.id.recyclerview)).perform(actionOnItem(hasDescendant(withText(TYPE_UPDATE_TEXT_TITLE)), swipeRight()));
    }
}