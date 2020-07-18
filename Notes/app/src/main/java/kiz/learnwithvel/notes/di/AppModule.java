package kiz.learnwithvel.notes.di;

import android.app.Application;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import kiz.learnwithvel.notes.persistence.NoteDao;
import kiz.learnwithvel.notes.persistence.NoteDatabase;
import kiz.learnwithvel.notes.repository.NoteRepository;

import static kiz.learnwithvel.notes.persistence.NoteDatabase.DATABASE_NAME;

@Module
public class AppModule {

    @Singleton
    @Provides
    static NoteDatabase provideNoteDatabase(Application application) {
        return Room.databaseBuilder(
                application.getApplicationContext(),
                NoteDatabase.class,
                DATABASE_NAME).build();
    }

    @Singleton
    @Provides
    static NoteDao provideNoteDao(NoteDatabase noteDatabase) {
        return noteDatabase.getNoteDao();
    }

    @Singleton
    @Provides
    static NoteRepository provideNoteRepository(NoteDao noteDao) {
        return new NoteRepository(noteDao);
    }

}
