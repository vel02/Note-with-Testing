package kiz.learnwithvel.notes.ui.note;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import kiz.learnwithvel.notes.R;
import kiz.learnwithvel.notes.model.Note;
import kiz.learnwithvel.notes.util.DateUtil;
import kiz.learnwithvel.notes.util.TestUtil;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

/**
 * source:
 * https://developer.android.com/guide/components/activities/testing#retrieve-activity-results
 * https://stackoverflow.com/questions/57588441/how-to-use-activitytestrule-when-launching-activity-with-bundle
 * https://www.vogella.com/tutorials/AndroidTestingEspresso/article.html
 */

@RunWith(JUnit4.class)
public class NoteActivityTest {

    private static final String TAG = "NoteActivityTest";
    private static final Note NOTE = new Note("Title", "", "07-2020");

    @Rule
    public ActivityTestRule<NoteActivity> activityTestRule = new ActivityTestRule<>(NoteActivity.class);

    @Test
    public void getIncomingIntent_returnOldNote() {
        //Arrange
        final Note returnedData = new Note(TestUtil.TEST_NOTE_1);
        final Intent intent = new Intent(activityTestRule.getActivity(), NoteActivity.class);
        intent.putExtra("intent_note", returnedData);

        //Act
        final ActivityScenario<NoteActivity> scenario = ActivityScenario.launch(intent);

        //Assert
        scenario.onActivity(activity -> {
            final Note returnedValue = activity.getIncomingIntent();//activity.getIntent().getParcelableExtra("intent_note");
            assertNotNull(returnedValue);
            assertEquals(returnedData, returnedValue);
        });
    }

    @Test
    public void getIncomingIntent_returnNewNote() throws Exception {
        // Arrange
        final Note returnedData = new Note("Title", "", DateUtil.getCurrentTimeStamp());
        final Intent intent = new Intent(activityTestRule.getActivity(), NoteActivity.class);

        // Act
        final ActivityScenario<NoteActivity> scenario = ActivityScenario.launch(intent);
        // Assert
        scenario.onActivity(activity -> {
            final Note returnedValue = activity.getIncomingIntent();//activity.getIntent().hasExtra("intent_note");
            assertNotNull(returnedValue);
            assertEquals(returnedData, returnedValue);
        });
    }

    @Test
    public void setNoteProperties_oldNote() {
        // Arrange
        final Note returnedData = new Note(TestUtil.TEST_NOTE_1);
        final Intent intent = new Intent(activityTestRule.getActivity(), NoteActivity.class);
        intent.putExtra("intent_note", returnedData);

        // Act
        ActivityScenario.launch(intent);

        // Assert
        onView(withId(R.id.note_edit_title)).check(matches(withText(returnedData.getTitle())));
        onView(withId(R.id.note_text_title)).check(matches(withText(returnedData.getTitle())));
        onView(withId(R.id.note_text)).check(matches(withText(returnedData.getContent())));
    }

    @Test
    public void setNoteProperties_newNote() {
        // Arrange
        final Note returnedData = new Note(NOTE);
        final Intent intent = new Intent(activityTestRule.getActivity(), NoteActivity.class);
        intent.putExtra("intent_note", returnedData);

        // Act
        ActivityScenario.launch(intent);

        // Assert
        onView(withId(R.id.note_edit_title)).check(matches(withText(returnedData.getTitle())));
        onView(withId(R.id.note_text_title)).check(matches(withText(returnedData.getTitle())));
        onView(withId(R.id.note_text)).check(matches(withText(returnedData.getContent())));
    }


    @Test
    public void setNoteProperties_returnError() throws Exception {

        Exception exception = assertThrows(Exception.class, () -> {
            activityTestRule.getActivity().setNoteProperties(null);
        });
        assertEquals("Error displaying note properties", exception.getMessage());
    }

}