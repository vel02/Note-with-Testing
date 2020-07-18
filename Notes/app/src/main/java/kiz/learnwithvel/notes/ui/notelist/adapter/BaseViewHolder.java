package kiz.learnwithvel.notes.ui.notelist.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import kiz.learnwithvel.notes.model.Note;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    private Note note;

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    protected abstract void clear();

    public void onBind(final Note note) {
        this.note = note;
        this.clear();
    }

    public Note getNote() {
        return note;
    }
}
