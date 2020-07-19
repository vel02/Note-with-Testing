package kiz.learnwithvel.notes.ui.note;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Flowable;
import io.reactivex.internal.operators.single.SingleToFlowable;
import kiz.learnwithvel.notes.model.Note;
import kiz.learnwithvel.notes.repository.NoteRepository;
import kiz.learnwithvel.notes.ui.Resource;
import kiz.learnwithvel.notes.util.InstantExecutorExtension;
import kiz.learnwithvel.notes.util.LiveDataTestUtil;
import kiz.learnwithvel.notes.util.TestUtil;

import static kiz.learnwithvel.notes.repository.NoteRepository.INSERT_FAILURE;
import static kiz.learnwithvel.notes.repository.NoteRepository.INSERT_SUCCESS;
import static kiz.learnwithvel.notes.repository.NoteRepository.NOTE_TITLE_NULL;
import static kiz.learnwithvel.notes.repository.NoteRepository.UPDATE_FAILURE;
import static kiz.learnwithvel.notes.repository.NoteRepository.UPDATE_SUCCESS;
import static kiz.learnwithvel.notes.ui.note.NoteViewModel.NO_CONTENT_ERROR;
import static kiz.learnwithvel.notes.ui.note.NoteViewModel.ViewState;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(InstantExecutorExtension.class)
public class NoteViewModelTest {

    //system under test
    @InjectMocks
    private NoteViewModel viewModel;

    @Mock
    private NoteRepository repository;

    @BeforeEach
    public void initEach() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void insertNote_returnRow() throws Exception {
        // Arrange
        final Note note = new Note(TestUtil.TEST_NOTE_1);
        final Flowable<Resource<Integer>> returnedData = SingleToFlowable.just(Resource.success(1, INSERT_SUCCESS));
        when(repository.insertNote(any(Note.class))).thenReturn(returnedData);

        // Act
        LiveDataTestUtil<Resource<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        viewModel.setNote(note);
        viewModel.isNewNote(true);
        final Resource<Integer> returnedValue = liveDataTestUtil.getValue(viewModel.saveNote());

        // Assert
        verify(repository).insertNote(any(Note.class));
        verifyNoMoreInteractions(repository);
        assertEquals(Resource.success(1, INSERT_SUCCESS), returnedValue);
    }

    @Test
    void insertNote_returnError() throws Exception {
        // Arrange
        final Flowable<Resource<Integer>> returnedData = SingleToFlowable.just(Resource.error(-1, INSERT_FAILURE));
        when(repository.insertNote(any(Note.class))).thenReturn(returnedData);

        // Act
        LiveDataTestUtil<Resource<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        viewModel.setNote(new Note(TestUtil.TEST_NOTE_1));
        viewModel.isNewNote(true);
        final Resource<Integer> returnedValue = liveDataTestUtil.getValue(viewModel.saveNote());

        // Assert
        verify(repository).insertNote(any(Note.class));
        verifyNoMoreInteractions(repository);
        assertEquals(Resource.error(-1, INSERT_FAILURE), returnedValue);
    }

    @Test
    void insertNote_emptyContent() {
        Exception exception = assertThrows(Exception.class, () -> {
            final Note note = new Note(TestUtil.TEST_NOTE_1);
            note.setContent("");
            viewModel.setNote(note);
            viewModel.isNewNote(true);
            viewModel.saveNote();
        });
        assertEquals(NO_CONTENT_ERROR, exception.getMessage());
    }

    @Test
    void insertNote_nullTitle() {
        Exception exception = assertThrows(Exception.class, () -> {
            final Note note = new Note(TestUtil.TEST_NOTE_1);
            note.setTitle(null);
            viewModel.setNote(note);
            viewModel.isNewNote(true);
            viewModel.saveNote();
        });
        assertEquals(NOTE_TITLE_NULL, exception.getMessage());
    }

    @Test
    void updateNote_returnRow() throws Exception {
        // Arrange
        final Note note = new Note(TestUtil.TEST_NOTE_1);
        final Flowable<Resource<Integer>> returnedData = SingleToFlowable.just(Resource.success(1, UPDATE_SUCCESS));
        when(repository.updateNote(any(Note.class))).thenReturn(returnedData);

        // Act
        final LiveDataTestUtil<Resource<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        viewModel.setNote(note);
        viewModel.isNewNote(false);
        final Resource<Integer> returnedValue = liveDataTestUtil.getValue(viewModel.saveNote());

        // Assert
        verify(repository).updateNote(any(Note.class));
        verifyNoMoreInteractions(repository);
        assertEquals(Resource.success(1, UPDATE_SUCCESS), returnedValue);
    }

    @Test
    void updateNote_returnError() throws Exception {
        // Arrange
        final Note note = new Note(TestUtil.TEST_NOTE_1);
        final Flowable<Resource<Integer>> returnedData = SingleToFlowable.just(Resource.error(-1, UPDATE_FAILURE));
        when(repository.updateNote(any(Note.class))).thenReturn(returnedData);

        // Act
        final LiveDataTestUtil<Resource<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        viewModel.setNote(note);
        viewModel.isNewNote(false);
        final Resource<Integer> returnedValue = liveDataTestUtil.getValue(viewModel.saveNote());

        // Assert
        assertEquals(Resource.error(-1, UPDATE_FAILURE), returnedValue);
    }

    @Test
    void updateNote_emptyContent() {
        Exception exception = assertThrows(Exception.class, () -> {
            final Note note = new Note(TestUtil.TEST_NOTE_1);
            note.setContent("");
            viewModel.setNote(note);
            viewModel.isNewNote(false);
            viewModel.saveNote();
        });
        assertEquals(NO_CONTENT_ERROR, exception.getMessage());
    }

    @Test
    void updateNote_nullTitle() {
        Exception exception = assertThrows(Exception.class, () -> {
            final Note note = new Note(TestUtil.TEST_NOTE_1);
            note.setTitle(null);
            viewModel.setNote(note);
            viewModel.isNewNote(false);
            viewModel.saveNote();
        });
        assertEquals(NOTE_TITLE_NULL, exception.getMessage());
    }

    @Test
    void getNote_returnNote() throws Exception {
        // Arrange
        final Note note = new Note(TestUtil.TEST_NOTE_1);
        final LiveDataTestUtil<Note> liveDataTestUtil = new LiveDataTestUtil<>();

        // Act
        viewModel.setNote(note);
        final Note returnedValue = liveDataTestUtil.getValue(viewModel.observeNote());

        // Assert
        note.setId(returnedValue.getId());
        assertEquals(note, returnedValue);
    }

    @Test
    void getNote_withEmpty() throws Exception {
        // Arrange
        final LiveDataTestUtil<Note> liveDataTestUtil = new LiveDataTestUtil<>();

        // Act
        final Note returnedValue = liveDataTestUtil.getValue(viewModel.observeNote());

        // Assert
        assertNull(returnedValue);
    }

    @Test
    void getNote_withoutObserver() throws Exception {
        // Arrange
        final Note note = new Note(TestUtil.TEST_NOTE_1);
        // Act
        viewModel.setNote(note);
        // Assert
        verify(repository, never()).insertNote(any(Note.class));
    }

    @Test
    void updateNoteObserver_returnError() {

        Exception exception = assertThrows(Exception.class, () -> {
            viewModel.updateNote(null, "Content");
        });

        assertEquals(NOTE_TITLE_NULL, exception.getMessage());

    }

    @Test
    void setViewState_returnEditMode() throws Exception {
        //Arrange
        final ViewState returnedData = ViewState.EDIT;
        viewModel.setViewState(returnedData);

        //Act
        final LiveDataTestUtil<ViewState> liveDataTestUtil = new LiveDataTestUtil<>();
        final ViewState returnedValue = liveDataTestUtil.getValue(viewModel.observeViewState());

        //Assert
        assertNotNull(returnedValue);
        assertEquals(returnedData, returnedValue);
    }

    @Test
    void setViewState_returnViewMode() throws Exception {
        // Arrange
        final ViewState returnedData = ViewState.VIEW;
        viewModel.setViewState(returnedData);

        // Act
        final LiveDataTestUtil<ViewState> liveDataTestUtil = new LiveDataTestUtil<>();
        final ViewState returnedValue = liveDataTestUtil.getValue(viewModel.observeViewState());

        // Assert
        assertNotNull(returnedValue);
        assertEquals(returnedData, returnedValue);
    }

    @Test
    void updateNoteObserver_returnNote() throws Exception {
        // Arrange
        final Note initialNote = new Note(TestUtil.TEST_NOTE_1);
        viewModel.setNote(initialNote);

        // Act
        viewModel.updateNote("Title", "Content");
        final LiveDataTestUtil<Note> liveDataTestUtil = new LiveDataTestUtil<>();
        final Note returnedValue = liveDataTestUtil.getValue(viewModel.observeNote());

        // Assert
        assertNotNull(returnedValue);
        assertEquals("Title", returnedValue.getTitle());
        assertEquals("Content", returnedValue.getContent());
    }

    @Test
    void shouldAllowSave_returnError() {

        Exception exception = assertThrows(Exception.class, () -> {

            // Arrange
            final Note saveNote = new Note(TestUtil.TEST_NOTE_1);

            // Act
            saveNote.setContent("");
            viewModel.setNote(saveNote);
            viewModel.isNewNote(true);
            viewModel.saveNote();
        });

        assertEquals(NO_CONTENT_ERROR, exception.getMessage());
    }

    @Test
    void shouldAllowSave_withNull_returnError() {

        Exception exception = assertThrows(Exception.class, () -> {

            // Arrange
            final Note saveNote = new Note(TestUtil.TEST_NOTE_1);

            // Act
            saveNote.setContent(null);
            viewModel.setNote(saveNote);
            viewModel.isNewNote(true);
            viewModel.saveNote();
        });

        assertEquals(NO_CONTENT_ERROR, exception.getMessage());
    }
}
