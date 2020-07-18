package kiz.learnwithvel.notes.model;

import org.junit.jupiter.api.Test;

import kiz.learnwithvel.notes.util.TestUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class NoteTest {

    @Test
    void compareNote_withNull_returnFalse() {
        //Arrange
        final Note note1 = new Note(TestUtil.TEST_NOTE_1);
        final Note note2 = null;

        //Assert
        assertNotEquals(note1, note2);
    }

    @Test
    void compareNote_differentClass_returnFalse() {
        // Arrange
        final Note note = new Note(TestUtil.TEST_NOTE_1);

        // Assert
        assertNotEquals(note, 10);
    }

    @Test
    void compareNote_identicalProperties_returnTrue() {
        // Arrange
        final Note note1 = new Note(TestUtil.TEST_NOTE_1);
        note1.setId(1);
        final Note note2 = new Note(TestUtil.TEST_NOTE_1);
        note2.setId(1);

        // Assert
        assertEquals(note1, note2);
    }

    @Test
    void compareNote_differentId_returnFalse() {
        // Arrange
        final Note note1 = new Note(TestUtil.TEST_NOTE_1);
        note1.setId(1);
        final Note note2 = new Note(TestUtil.TEST_NOTE_1);
        note2.setId(2);

        // Assert
        assertNotEquals(note1, note2);
    }

    @Test
    void compareNote_differentTitle_returnFalse() {
        // Arrange
        final Note note1 = new Note("Title 1", "", "07-2020");
        note1.setId(1);
        final Note note2 = new Note("Title 2", "", "07-2020");
        note2.setId(1);

        // Assert
        assertNotEquals(note1, note2);
    }

    @Test
    void compareNote_differentTimestamp_returnTrue() {
        final Note note1 = new Note("Title 1", "", "07-2020");
        note1.setId(1);
        final Note note2 = new Note("Title 1", "", "05-2020");
        note2.setId(1);

        // Assert
        assertEquals(note1, note2);
    }
}
