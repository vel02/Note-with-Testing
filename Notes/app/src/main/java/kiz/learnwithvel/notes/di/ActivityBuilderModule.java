package kiz.learnwithvel.notes.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import kiz.learnwithvel.notes.di.ui.note.NoteViewModelModule;
import kiz.learnwithvel.notes.di.ui.notelist.NoteListViewModelModule;
import kiz.learnwithvel.notes.ui.note.NoteActivity;
import kiz.learnwithvel.notes.ui.notelist.NoteListActivity;

@Module
public abstract class ActivityBuilderModule {


    @ContributesAndroidInjector(
            modules = {NoteViewModelModule.class}
    )
    abstract NoteActivity contributeNoteActivity();

    @ContributesAndroidInjector(
            modules = {NoteListViewModelModule.class}
    )
    abstract NoteListActivity contributeNoteListActivity();

}
