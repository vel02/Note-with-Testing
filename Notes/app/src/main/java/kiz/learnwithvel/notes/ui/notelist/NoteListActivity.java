package kiz.learnwithvel.notes.ui.notelist;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import kiz.learnwithvel.notes.R;
import kiz.learnwithvel.notes.model.Note;
import kiz.learnwithvel.notes.ui.Resource;
import kiz.learnwithvel.notes.ui.note.NoteActivity;
import kiz.learnwithvel.notes.ui.notelist.adapter.NoteRecyclerAdapter;
import kiz.learnwithvel.notes.util.VerticalSpacingItemDecorator;
import kiz.learnwithvel.notes.viewmodel.ViewModelProviderFactory;

public class NoteListActivity extends DaggerAppCompatActivity implements NoteRecyclerAdapter.OnNoteListener, View.OnClickListener {

    private static final String TAG = "NoteListActivity";

    @Inject
    ViewModelProviderFactory providerFactory;
    private NoteListViewModel viewModel;
    private RecyclerView recyclerView;
    private NoteRecyclerAdapter adapter;

    private CoordinatorLayout parent;

    private ItemTouchHelper.SimpleCallback simpleCallback
            = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final Note note = adapter.getNote(viewHolder.getAdapterPosition());
            adapter.removeNote(note);

            try {
                final LiveData<Resource<Integer>> deleteNote = viewModel.deleteNote(note);
                deleteNote.observe(NoteListActivity.this, new Observer<Resource<Integer>>() {
                    @Override
                    public void onChanged(Resource<Integer> integerResource) {
                        deleteNote.removeObserver(this);
                        if (integerResource != null) {
                            showSnackBar(integerResource.message);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                showSnackBar(e.getMessage());
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        setSupportActionBar(findViewById(R.id.toolbar));
        Objects.requireNonNull(getSupportActionBar()).setTitle("Notes");
        recyclerView = findViewById(R.id.recyclerview);
        parent = findViewById(R.id.parent);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
        initRecyclerView();

        viewModel = new ViewModelProvider(this, providerFactory).get(NoteListViewModel.class);

    }

    @Override
    protected void onResume() {
        super.onResume();
        subscribeObservers();
    }

    private void subscribeObservers() {
        viewModel.getNotes();
        viewModel.observeNotes().observe(this, notes -> {
            if (notes != null) {
                adapter.addNotes(notes);
            }
        });
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        recyclerView.addItemDecoration(itemDecorator);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
        adapter = new NoteRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onNoteClick(Note note) {
        Intent intent = new Intent(this, NoteActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("intent_note", note);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab) {
            Intent intent = new Intent(this, NoteActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
    }

    private void showSnackBar(String message) {
        if (!TextUtils.isEmpty(message)) {

            Snackbar.make(parent, message, Snackbar.LENGTH_SHORT).show();
        }
    }
}