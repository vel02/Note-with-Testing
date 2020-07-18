package kiz.learnwithvel.notes.ui.notelist;

import androidx.lifecycle.MutableLiveData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import kiz.learnwithvel.notes.model.Note;
import kiz.learnwithvel.notes.repository.NoteRepository;
import kiz.learnwithvel.notes.ui.Resource;
import kiz.learnwithvel.notes.util.InstantExecutorExtension;
import kiz.learnwithvel.notes.util.LiveDataTestUtil;
import kiz.learnwithvel.notes.util.TestUtil;

import static kiz.learnwithvel.notes.repository.NoteRepository.DELETE_FAILURE;
import static kiz.learnwithvel.notes.repository.NoteRepository.DELETE_SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(InstantExecutorExtension.class)
public class NoteListViewModelTest {

    //system under test
    private NoteListViewModel viewModel;

    @Mock
    private NoteRepository repository;

    @BeforeEach
    public void initEach() {
        MockitoAnnotations.openMocks(this);
        viewModel = new NoteListViewModel(repository);
    }

    @Test
    void retrieveNote_returnNotes() throws Exception {
        // Arrange
        final List<Note> noteList = TestUtil.TEST_NOTES_LIST;
        final MutableLiveData<List<Note>> returnedData = new MutableLiveData<>();
        returnedData.setValue(noteList);
        when(repository.getNotes()).thenReturn(returnedData);

        // Act
        final LiveDataTestUtil<List<Note>> liveDataTestUtil = new LiveDataTestUtil<>();
        viewModel.getNotes();
        final List<Note> returnedValue = liveDataTestUtil.getValue(viewModel.observeNotes());

        // Assert
        verify(repository).getNotes();
        verifyNoMoreInteractions(repository);
        assertEquals(noteList, returnedValue);
    }

    @Test
    void retrieveNote_returnEmpty() throws Exception {
        // Arrange
        final List<Note> noteList = new ArrayList<>();
        final MutableLiveData<List<Note>> returnedData = new MutableLiveData<>();
        returnedData.setValue(noteList);
        when(repository.getNotes()).thenReturn(returnedData);

        // Act
        final LiveDataTestUtil<List<Note>> liveDataTestUtil = new LiveDataTestUtil<>();
        viewModel.getNotes();
        final List<Note> returnedValues = liveDataTestUtil.getValue(viewModel.observeNotes());

        // Assert
        verify(repository).getNotes();
        verifyNoMoreInteractions(repository);
        assertEquals(noteList, returnedValues);
    }

    @Test
    void deleteNote_returnRow() throws Exception {
        // Arrange
        final MutableLiveData<Resource<Integer>> returnedData = new MutableLiveData<>();
        returnedData.setValue(Resource.success(1, DELETE_SUCCESS));
        when(repository.deleteNote(any(Note.class))).thenReturn(returnedData);

        // Act
        final Note note = new Note(TestUtil.TEST_NOTE_1);
        final LiveDataTestUtil<Resource<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        Resource<Integer> returnedValue = liveDataTestUtil.getValue(viewModel.deleteNote(note));

        // Assert
        verify(repository).deleteNote(any(Note.class));
        verifyNoMoreInteractions(repository);
        assertEquals(Resource.success(1, DELETE_SUCCESS), returnedValue);
    }

    @Test
    void deleteNote_returnError() throws Exception {
        // Arrange
        final MutableLiveData<Resource<Integer>> returnedData = new MutableLiveData<>();
        returnedData.setValue(Resource.error(-1, DELETE_FAILURE));
        when(repository.deleteNote(any(Note.class))).thenReturn(returnedData);

        // Act
        final LiveDataTestUtil<Resource<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        final Note note = new Note(TestUtil.TEST_NOTE_1);
        Resource<Integer> returnedValue = liveDataTestUtil.getValue(viewModel.deleteNote(note));

        // Assert
        verify(repository).deleteNote(any(Note.class));
        verifyNoMoreInteractions(repository);
        assertEquals(Resource.error(-1, DELETE_FAILURE), returnedValue);
    }


}
