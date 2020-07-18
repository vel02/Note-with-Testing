package kiz.learnwithvel.notes.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import kiz.learnwithvel.notes.model.Note;
import kiz.learnwithvel.notes.persistence.NoteDao;
import kiz.learnwithvel.notes.ui.Resource;

@Singleton
public class NoteRepository {

    public static final String NOTE_TITLE_NULL = "Note title cannot be null";
    public static final String INVALID_NOTE_ID = "Invalid id. Can't delete note";
    public static final String DELETE_SUCCESS = "Delete success";
    public static final String DELETE_FAILURE = "Delete failure";
    public static final String UPDATE_SUCCESS = "Update success";
    public static final String UPDATE_FAILURE = "Update failure";
    public static final String INSERT_SUCCESS = "Insert success";
    public static final String INSERT_FAILURE = "Insert failure";
    private static final String TAG = "NoteRepository";

    private final NoteDao noteDao;
    private int timeDelay = 0;
    private TimeUnit timeUnit = TimeUnit.SECONDS;

    @Inject
    public NoteRepository(NoteDao noteDao) {
        this.noteDao = noteDao;
    }

    public Flowable<Resource<Integer>> insertNote(final Note note) throws Exception {
        checkTitle(note);
        return noteDao.insertNote(note)
                .delaySubscription(timeDelay, timeUnit)
                .map(aLong -> {
                    long l = aLong;
                    return (int) l;
                })
                .onErrorReturn(throwable -> -1)
                .map((Function<Integer, Resource<Integer>>) integer -> {
                    if (integer > 0) {
                        return Resource.success(integer, INSERT_SUCCESS);
                    }
                    return Resource.error(null, INSERT_FAILURE);
                })
                .subscribeOn(Schedulers.io())
                .toFlowable();
    }

    public Flowable<Resource<Integer>> updateNote(final Note note) throws Exception {
        checkTitle(note);
        return noteDao.updateNote(note)
                .delaySubscription(timeDelay, timeUnit)
                .onErrorReturn(throwable -> -1)
                .map((Function<Integer, Resource<Integer>>) integer -> {
                    if (integer > 0) {
                        return Resource.success(integer, UPDATE_SUCCESS);
                    }
                    return Resource.error(null, UPDATE_FAILURE);
                })
                .subscribeOn(Schedulers.io())
                .toFlowable();
    }

    private void checkTitle(Note note) throws Exception {
        if (note.getTitle() == null) {
            throw new Exception(NOTE_TITLE_NULL);
        }
    }

    public LiveData<Resource<Integer>> deleteNote(final Note note) throws Exception {
        checkId(note);
        return LiveDataReactiveStreams.fromPublisher(noteDao.deleteNote(note)
                .onErrorReturn(throwable -> -1)
                .map((Function<Integer, Resource<Integer>>) integer -> {
                    if (integer > 0) return Resource.success(integer, DELETE_SUCCESS);
                    return Resource.error(null, DELETE_FAILURE);
                })
                .subscribeOn(Schedulers.io())
                .toFlowable());
    }

    private void checkId(Note note) throws Exception {
        if (note.getId() < 0) {
            throw new Exception(INVALID_NOTE_ID);
        }
    }

    public LiveData<List<Note>> getNotes() {
        return noteDao.getNotes();
    }

}
