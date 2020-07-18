package kiz.learnwithvel.notes.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Single;
import kiz.learnwithvel.notes.model.Note;

@Dao
public interface NoteDao {

    @Insert
    Single<Long> insertNote(Note note) throws Exception;

    @Query("SELECT * FROM notes_tb")
    LiveData<List<Note>> getNotes();

    @Delete
    Single<Integer> deleteNote(Note note) throws Exception;

    @Update
    Single<Integer> updateNote(Note note) throws Exception;

}
