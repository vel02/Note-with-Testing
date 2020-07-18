package kiz.learnwithvel.notes.persistence;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import kiz.learnwithvel.notes.model.Note;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "note_db";

    public abstract NoteDao getNoteDao();

}
