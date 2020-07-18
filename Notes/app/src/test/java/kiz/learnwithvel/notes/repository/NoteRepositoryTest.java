package kiz.learnwithvel.notes.repository;

import androidx.lifecycle.MutableLiveData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import kiz.learnwithvel.notes.model.Note;
import kiz.learnwithvel.notes.persistence.NoteDao;
import kiz.learnwithvel.notes.ui.Resource;
import kiz.learnwithvel.notes.util.InstantExecutorExtension;
import kiz.learnwithvel.notes.util.LiveDataTestUtil;
import kiz.learnwithvel.notes.util.TestUtil;

import static kiz.learnwithvel.notes.repository.NoteRepository.DELETE_FAILURE;
import static kiz.learnwithvel.notes.repository.NoteRepository.DELETE_SUCCESS;
import static kiz.learnwithvel.notes.repository.NoteRepository.INSERT_FAILURE;
import static kiz.learnwithvel.notes.repository.NoteRepository.INSERT_SUCCESS;
import static kiz.learnwithvel.notes.repository.NoteRepository.INVALID_NOTE_ID;
import static kiz.learnwithvel.notes.repository.NoteRepository.NOTE_TITLE_NULL;
import static kiz.learnwithvel.notes.repository.NoteRepository.UPDATE_FAILURE;
import static kiz.learnwithvel.notes.repository.NoteRepository.UPDATE_SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(InstantExecutorExtension.class)
public class NoteRepositoryTest {

    private final Note NOTE1 = new Note(TestUtil.TEST_NOTE_1);

    //system under test
    private NoteRepository repository;


    private NoteDao noteDao;

    @BeforeEach
    public void initEach() {
        noteDao = Mockito.mock(NoteDao.class);
        repository = new NoteRepository(noteDao);
    }

    @Test
    void insertNote_returnRow() throws Exception {
        // Arrange
        final Long insertedRow = 1L;
        final Single<Long> returnedData = Single.just(insertedRow);
        when(noteDao.insertNote(any(Note.class))).thenReturn(returnedData);

        // Act
        final Resource<Integer> returnedValue = repository.insertNote(NOTE1).blockingFirst();

        // Assert
        verify(noteDao).insertNote(any(Note.class));
        verifyNoMoreInteractions(noteDao);
        System.out.println("Returned value: " + returnedValue.data);
        assertEquals(Resource.success(1, INSERT_SUCCESS), returnedValue);

    }


    @Test
    void insertNote_returnError() throws Exception {
        // Arrange
        final Long insertedRow = -1L;
        final Single<Long> returnedData = Single.just(insertedRow);
        when(noteDao.insertNote(any(Note.class))).thenReturn(returnedData);

        // Act
        final Resource<Integer> returnedValue = repository.insertNote(NOTE1).blockingFirst();

        // Assert
        assertEquals(Resource.error(-1, INSERT_FAILURE), returnedValue);
    }

    @Test
    void insertNote_nullTitle_throwException() {
        Exception exception = assertThrows(Exception.class, () -> {
            final Note note = new Note(NOTE1);
            note.setTitle(null);
            repository.insertNote(note);
        });
        assertEquals(NOTE_TITLE_NULL, exception.getMessage());
    }

    @Test
    void updateNote_returnRow() throws Exception {
        // Arrange
        final int updatedRow = 1;
        Single<Integer> returnedData = Single.just(updatedRow);
        when(noteDao.updateNote(any(Note.class))).thenReturn(returnedData);

        // Act
        final Resource<Integer> returnedValue = repository.updateNote(NOTE1).blockingFirst();

        // Assert
        verify(noteDao).updateNote(any(Note.class));
        verifyNoMoreInteractions(noteDao);
        assertEquals(Resource.success(1, UPDATE_SUCCESS), returnedValue);
    }

    @Test
    void updateNote_returnError() throws Exception {
        // Arrange
        final int returnedError = -1;
        final Single<Integer> returnedData = Single.just(returnedError);
        when(noteDao.updateNote(any(Note.class))).thenReturn(returnedData);

        // Act
        final Resource<Integer> returnedValue = repository.updateNote(NOTE1).blockingFirst();

        // Assert
        assertEquals(Resource.error(-1, UPDATE_FAILURE), returnedValue);
    }

    @Test
    void updateNote_nullTitle() {
        Exception exception = assertThrows(Exception.class, () -> {
            final Note note = new Note(NOTE1);
            note.setTitle(null);
            repository.updateNote(note);
        });
        assertEquals(NOTE_TITLE_NULL, exception.getMessage());
    }

    @Test
    void deleteNote_returnRow() throws Exception {
        // Arrange
        final int deletedRow = 1;
        final Single<Integer> returnedData = Single.just(deletedRow);
        when(noteDao.deleteNote(any(Note.class))).thenReturn(returnedData);

        // Act
        LiveDataTestUtil<Resource<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        final Resource<Integer> returnedValue = liveDataTestUtil.getValue(repository.deleteNote(NOTE1));

        // Assert
        verify(noteDao).deleteNote(any(Note.class));
        verifyNoMoreInteractions(noteDao);
        assertEquals(Resource.success(1, DELETE_SUCCESS), returnedValue);
    }

    @Test
    void deletedNote_returnError() throws Exception {
        // Arrange
        final int deletedRow = -1;
        Single<Integer> returnedData = Single.just(deletedRow);
        when(noteDao.deleteNote(any(Note.class))).thenReturn(returnedData);

        // Act
        LiveDataTestUtil<Resource<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        final Resource<Integer> returnedValue = liveDataTestUtil.getValue(repository.deleteNote(NOTE1));

        // Assert
        assertEquals(Resource.error(-1, DELETE_FAILURE), returnedValue);
    }

    @Test
    void deletedNote_nullTitle() {
        Exception exception = assertThrows(Exception.class, () -> {
            final Note note = new Note(NOTE1);
            note.setId(-1);
            repository.deleteNote(note);
        });
        assertEquals(INVALID_NOTE_ID, exception.getMessage());
    }

    @Test
    void getNotes_returnedList() throws Exception {
        // Arrange
        List<Note> noteList = TestUtil.TEST_NOTES_LIST;
        MutableLiveData<List<Note>> returnedData = new MutableLiveData<>();
        returnedData.setValue(noteList);
        when(noteDao.getNotes()).thenReturn(returnedData);

        // Act
        LiveDataTestUtil<List<Note>> liveDataTestUtil = new LiveDataTestUtil<>();
        List<Note> returnedValues = liveDataTestUtil.getValue(repository.getNotes());

        // Assert
        verify(noteDao).getNotes();
        verifyNoMoreInteractions(noteDao);
        assertEquals(noteList, returnedValues);
    }

    @Test
    void getNotes_returnedEmpty() throws Exception {
        // Arrange
        final List<Note> noteList = new ArrayList<>();
        final MutableLiveData<List<Note>> returnedData = new MutableLiveData<>();
        returnedData.setValue(noteList);
        when(noteDao.getNotes()).thenReturn(returnedData);

        // Act
        final LiveDataTestUtil<List<Note>> liveDataTestUtil = new LiveDataTestUtil<>();
        final List<Note> returnedValues = liveDataTestUtil.getValue(repository.getNotes());

        // Assert
        assertEquals(noteList, returnedValues);
    }
}
