package kiz.learnwithvel.notes.ui.notelist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import kiz.learnwithvel.notes.model.Note;
import kiz.learnwithvel.notes.repository.NoteRepository;
import kiz.learnwithvel.notes.ui.Resource;

public class NoteListViewModel extends ViewModel {

    private final NoteRepository repository;

    private MediatorLiveData<List<Note>> notes = new MediatorLiveData<>();

    @Inject
    public NoteListViewModel(NoteRepository repository) {
        this.repository = repository;
    }

    public LiveData<Resource<Integer>> deleteNote(final Note note) throws Exception {
        return repository.deleteNote(note);
    }

    public LiveData<List<Note>> observeNotes() {
        return notes;
    }

    public void getNotes() {
        final LiveData<List<Note>> source = repository.getNotes();
        notes.addSource(source, noteList -> {
            notes.removeSource(source);
            if (noteList != null) {
                notes.setValue(noteList);
            }
        });
    }
}
