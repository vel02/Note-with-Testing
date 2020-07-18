package kiz.learnwithvel.notes.di.ui.notelist;

import androidx.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import kiz.learnwithvel.notes.di.ViewModelKey;
import kiz.learnwithvel.notes.ui.notelist.NoteListViewModel;

@Module
public abstract class NoteListViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(NoteListViewModel.class)
    abstract ViewModel bindNoteListViewModel(NoteListViewModel viewModel);

}
