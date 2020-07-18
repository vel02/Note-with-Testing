package kiz.learnwithvel.notes.ui.notelist.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kiz.learnwithvel.notes.R;
import kiz.learnwithvel.notes.model.Note;
import kiz.learnwithvel.notes.ui.notelist.adapter.viewholder.NoteViewHolder;

public class NoteRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private final OnNoteListener listener;
    private List<Note> noteList = new ArrayList<>();

    public NoteRecyclerAdapter(OnNoteListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_note_list_item, parent, false);
        return new NoteViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(noteList.get(position));
    }

    @Override
    public int getItemCount() {
        return ((noteList != null && noteList.size() > 0) ? noteList.size() : 0);
    }

    public void addNotes(final List<Note> notes) {
        this.noteList = notes;
        notifyDataSetChanged();
    }

    public Note getNote(final int position) {
        if (position >= 0) {
            return noteList.get(position);
        }
        return null;
    }

    public void removeNote(final Note note) {
        if (note != null) {
            this.noteList.remove(note);
        }
        notifyDataSetChanged();
    }

    public interface OnNoteListener {
        void onNoteClick(final Note note);
    }
}
