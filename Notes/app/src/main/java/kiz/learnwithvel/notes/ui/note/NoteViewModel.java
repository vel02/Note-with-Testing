package kiz.learnwithvel.notes.ui.note;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.reactivestreams.Subscription;

import java.util.Objects;

import javax.inject.Inject;

import kiz.learnwithvel.notes.model.Note;
import kiz.learnwithvel.notes.repository.NoteRepository;
import kiz.learnwithvel.notes.ui.Resource;
import kiz.learnwithvel.notes.util.DateUtil;

import static kiz.learnwithvel.notes.repository.NoteRepository.NOTE_TITLE_NULL;

public class NoteViewModel extends ViewModel {


    public static final String NO_CONTENT_ERROR = "Can't save note with no content";
    private static final String TAG = "NoteViewModel";
    private final NoteRepository noteRepository;

    private MutableLiveData<Note> note = new MutableLiveData<>();
    private MutableLiveData<ViewState> viewState = new MutableLiveData<>();

    private boolean isNewNote;
    private Subscription insertSubscription, updateSubscription;


    @Inject
    public NoteViewModel(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    private LiveData<Resource<Integer>> insertNote() throws Exception {
        return LiveDataReactiveStreams.fromPublisher(
                noteRepository.insertNote(note.getValue())
                        .doOnSubscribe(subscription -> insertSubscription = subscription));
    }

    private LiveData<Resource<Integer>> updateNote() throws Exception {
        return LiveDataReactiveStreams.fromPublisher(
                noteRepository.updateNote(note.getValue())
                        .doOnSubscribe(subscription -> updateSubscription = subscription));
    }

    public LiveData<Resource<Integer>> saveNote() throws Exception {
        if (!shouldAllowSave()) {
            throw new Exception(NO_CONTENT_ERROR);
        }

        cancelPendingTransaction();

        return new InsertUpdateResource<Integer>() {

            @Override
            protected void completed() {
                insertSubscription = null;
                updateSubscription = null;
            }

            @Override
            protected void setNoteId(int id) {
                isNewNote = false;
                Note currentNote = new Note(Objects.requireNonNull(note.getValue()));
                currentNote.setId(id);
                note.setValue(currentNote);
            }

            @Override
            protected String definedAction() {
                if (isNewNote) {
                    return ACTION_INSERT;
                } else return ACTION_UPDATE;
            }

            @Override
            protected LiveData<Resource<Integer>> getAction() throws Exception {
                if (isNewNote) {
                    return insertNote();
                } else return updateNote();
            }
        }.getAsLiveData();
    }

    private void cancelPendingTransaction() {
        if (insertSubscription != null) {
            cancelInsertTransaction();
        }
        if (updateSubscription != null) {
            cancelUpdateTransaction();
        }
    }

    private void cancelInsertTransaction() {
        insertSubscription.cancel();
        insertSubscription = null;
    }

    private void cancelUpdateTransaction() {
        insertSubscription.cancel();
        insertSubscription = null;
    }

    private boolean shouldAllowSave() throws Exception {
        try {
            return removeWhiteSpace(Objects.requireNonNull(note.getValue()).getContent()).length() > 0;
        } catch (Exception e) {
            throw new Exception(NO_CONTENT_ERROR);
        }
    }

    public void updateNote(String title, String content) throws Exception {
        if (title == null || title.equals("")) {
            throw new Exception(NOTE_TITLE_NULL);
        }

        String temp = removeWhiteSpace(content);

        if (temp.length() > 0) {
            Note currentNote = new Note(Objects.requireNonNull(note.getValue()));
            currentNote.setTitle(title);
            currentNote.setContent(content);
            currentNote.setTimestamp(DateUtil.getCurrentTimeStamp());
            this.note.setValue(currentNote);
        }

    }

    private String removeWhiteSpace(String content) {
        content = content.replace("\n", "");
        content = content.replace(" ", "");
        return content;
    }

    public void setViewState(ViewState viewState) {
        this.viewState.setValue(viewState);
    }

    public LiveData<ViewState> observeViewState() {
        return viewState;
    }

    public LiveData<Note> observeNote() {
        return note;
    }

    public void setNote(final Note note) throws Exception {
        if (note.getTitle() == null || note.getTitle().equals("")) {
            throw new Exception(NOTE_TITLE_NULL);
        }
        this.note.setValue(note);
    }

    public void isNewNote(boolean isNewNote) {
        this.isNewNote = isNewNote;
    }

    public boolean shouldNavigateBack() {
        return (this.viewState.getValue() == ViewState.VIEW);
    }

    public enum ViewState {EDIT, VIEW}
}
