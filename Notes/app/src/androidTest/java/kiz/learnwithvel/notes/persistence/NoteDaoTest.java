package kiz.learnwithvel.notes.persistence;

import android.database.sqlite.SQLiteConstraintException;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import kiz.learnwithvel.notes.model.Note;
import kiz.learnwithvel.notes.util.LiveDataTestUtil;
import kiz.learnwithvel.notes.util.TestUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class NoteDaoTest extends NoteDatabaseTest {

    public static final String TEST_TITLE = "This is a test title";
    public static final String TEST_CONTENT = "This is some test content";
    public static final String TEST_TIMESTAMP = "08-2020";


    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();


    @Test
    public void insertReadDelete() throws Exception {

        final Note note = new Note(TestUtil.TEST_NOTE_1);

        //insert
        getNoteDao().insertNote(note).blockingGet();

        //read
        LiveDataTestUtil<List<Note>> listLiveDataTestUtil = new LiveDataTestUtil<>();
        List<Note> insertedNotes = listLiveDataTestUtil.getValue(getNoteDao().getNotes());

        assertNotNull(insertedNotes);
        assertEquals(note.getTitle(), insertedNotes.get(0).getTitle());
        assertEquals(note.getContent(), insertedNotes.get(0).getContent());
        assertEquals(note.getTimestamp(), insertedNotes.get(0).getTimestamp());

        note.setId(insertedNotes.get(0).getId());
        assertEquals(note, insertedNotes.get(0));

        //delete
        getNoteDao().deleteNote(note).blockingGet();

        insertedNotes = listLiveDataTestUtil.getValue(getNoteDao().getNotes());
        assertEquals(0, insertedNotes.size());

    }

    @Test
    public void insertReadUpdateReadDelete() throws Exception {
        final Note note = new Note(TestUtil.TEST_NOTE_1);

        //insert
        getNoteDao().insertNote(note).blockingGet();

        //read
        LiveDataTestUtil<List<Note>> liveDataTestUtil = new LiveDataTestUtil<>();
        List<Note> insertedNotes = liveDataTestUtil.getValue(getNoteDao().getNotes());

        assertNotNull(insertedNotes);
        assertEquals(note.getTitle(), insertedNotes.get(0).getTitle());
        assertEquals(note.getContent(), insertedNotes.get(0).getContent());
        assertEquals(note.getTimestamp(), insertedNotes.get(0).getTimestamp());

        note.setId(insertedNotes.get(0).getId());
        assertEquals(note, insertedNotes.get(0));

        //update
        note.setTitle(TEST_TITLE);
        note.setContent(TEST_CONTENT);
        note.setTimestamp(TEST_TIMESTAMP);
        getNoteDao().updateNote(note).blockingGet();

        //read
        insertedNotes = liveDataTestUtil.getValue(getNoteDao().getNotes());
        assertNotNull(insertedNotes);
        assertEquals(note, insertedNotes.get(0));

        //delete
        getNoteDao().deleteNote(note).blockingGet();

        insertedNotes = liveDataTestUtil.getValue(getNoteDao().getNotes());
        assertEquals(0, insertedNotes.size());
    }

    @Test(expected = SQLiteConstraintException.class)
    public void insertNote_nullTitle_throwException() throws Exception {

        final Note note = new Note(TestUtil.TEST_NOTE_1);
        note.setTitle(null);
        getNoteDao().insertNote(note).blockingGet();

    }

    @Test(expected = SQLiteConstraintException.class)
    public void insertUpdate_nullTitle_throwException() throws Exception {
        final Note note = new Note(TestUtil.TEST_NOTE_1);
        //insert
        getNoteDao().insertNote(note).blockingGet();

        //read
        LiveDataTestUtil<List<Note>> liveDataTestUtil = new LiveDataTestUtil<>();
        List<Note> insertedNote = liveDataTestUtil.getValue(getNoteDao().getNotes());
        assertNotNull(insertedNote);
        note.setId(insertedNote.get(0).getId());
        assertEquals(note, insertedNote.get(0));

        //update
        note.setTitle(null);
        getNoteDao().updateNote(note).blockingGet();

    }
}
