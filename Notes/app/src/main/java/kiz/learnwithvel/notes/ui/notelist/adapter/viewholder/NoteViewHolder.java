package kiz.learnwithvel.notes.ui.notelist.adapter.viewholder;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import kiz.learnwithvel.notes.R;
import kiz.learnwithvel.notes.model.Note;
import kiz.learnwithvel.notes.ui.notelist.adapter.BaseViewHolder;
import kiz.learnwithvel.notes.util.DateUtil;

import static kiz.learnwithvel.notes.ui.notelist.adapter.NoteRecyclerAdapter.OnNoteListener;

public class NoteViewHolder extends BaseViewHolder implements View.OnClickListener {

    private final OnNoteListener listener;
    private TextView title, timestamp;

    public NoteViewHolder(@NonNull View itemView, OnNoteListener listener) {
        super(itemView);
        this.listener = listener;
        this.title = itemView.findViewById(R.id.note_title);
        this.timestamp = itemView.findViewById(R.id.note_timestamp);
        itemView.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBind(Note note) {
        super.onBind(note);
        String month = DateUtil.getMonthFromNumber(note.getTimestamp().substring(0, 2));
        String year = note.getTimestamp().substring(3);
        this.title.setText(note.getTitle());
        this.timestamp.setText(month + " " + year);
    }

    @Override
    protected void clear() {
        this.title.setText("");
        this.timestamp.setText("");
    }

    @Override
    public void onClick(View view) {
        listener.onNoteClick(getNote());
    }
}
