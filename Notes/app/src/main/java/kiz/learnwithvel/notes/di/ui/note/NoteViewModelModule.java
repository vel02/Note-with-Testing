package kiz.learnwithvel.notes.di.ui.note;

import androidx.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import kiz.learnwithvel.notes.di.ViewModelKey;
import kiz.learnwithvel.notes.ui.note.NoteViewModel;

@Module
public abstract class NoteViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(NoteViewModel.class)
    abstract ViewModel bindNoteViewModel(NoteViewModel viewModel);

}
