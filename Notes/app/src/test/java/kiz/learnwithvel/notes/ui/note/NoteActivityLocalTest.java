package kiz.learnwithvel.notes.ui.note;


import android.content.Intent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import kiz.learnwithvel.notes.model.Note;
import kiz.learnwithvel.notes.util.InstantExecutorExtension;
import kiz.learnwithvel.notes.util.TestUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(InstantExecutorExtension.class)
public class NoteActivityLocalTest {

    public static final String KEY_INTENT_NOTE = "intent_note";

    @Mock
    private Intent intent;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void getIncomingIntent_withOldNote() {
        // Arrange
        final Note returnNote = new Note(TestUtil.TEST_NOTE_1);
        when(intent.getParcelableExtra(KEY_INTENT_NOTE)).thenReturn(returnNote);

        // Act
        final Note returnedValue = intent.getParcelableExtra(KEY_INTENT_NOTE);

        // Assert
        assertEquals(returnNote, returnedValue);
    }

    @Test
    void getIncomingIntent_hasExtra_returnFalse() {
        // Arrange
        final boolean returnData = false;
        when(intent.hasExtra(KEY_INTENT_NOTE)).thenReturn(returnData);

        // Act
        final boolean returnedValue = intent.hasExtra(KEY_INTENT_NOTE);
        verify(intent).hasExtra(anyString());
        verifyNoMoreInteractions(intent);

        // Assert
        assertEquals(returnData, returnedValue);
    }

    @Test
    void getIncomingIntent_hasExtra_returnTrue() {
        // Arrange
        final boolean returnData = true;
        when(intent.hasExtra(KEY_INTENT_NOTE)).thenReturn(returnData);

        // Act
        final boolean returnedValue = intent.hasExtra(KEY_INTENT_NOTE);
        verify(intent).hasExtra(KEY_INTENT_NOTE);
        verifyNoMoreInteractions(intent);

        // Assert
        assertEquals(returnData, returnedValue);
    }


}
