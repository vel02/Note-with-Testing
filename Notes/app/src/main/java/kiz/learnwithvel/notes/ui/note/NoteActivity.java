package kiz.learnwithvel.notes.ui.note;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import kiz.learnwithvel.notes.R;
import kiz.learnwithvel.notes.model.Note;
import kiz.learnwithvel.notes.util.DateUtil;
import kiz.learnwithvel.notes.util.LinedEditText;
import kiz.learnwithvel.notes.viewmodel.ViewModelProviderFactory;

public class NoteActivity extends DaggerAppCompatActivity implements
        View.OnClickListener,
        View.OnTouchListener,
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {

    private static final String TAG = "NoteActivity";
    @Inject
    ViewModelProviderFactory providerFactory;
    private NoteViewModel viewModel;
    private GestureDetector gestureDetector;

    private ConstraintLayout parent;
    private RelativeLayout checkContainer, backContainer;
    private ImageButton check, back;
    private LinedEditText linedEditText;
    private TextView viewTitle;
    private EditText editTitle;

    public Note getIncomingIntent() {
        try {
            Note note;
            if (getIntent().hasExtra("intent_note")) {
                note = getIntent().getParcelableExtra("intent_note");
                viewModel.isNewNote(false);
            } else {
                note = new Note("Title", "", DateUtil.getCurrentTimeStamp());
                viewModel.isNewNote(true);
            }
            if (note != null) viewModel.setNote(note);
            return note;
        } catch (Exception e) {
            e.printStackTrace();
            showSnackBar(String.valueOf(R.string.error_intent_note));
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        parent = findViewById(R.id.parent);
        checkContainer = findViewById(R.id.check_container);
        backContainer = findViewById(R.id.back_arrow_container);
        linedEditText = findViewById(R.id.note_text);
        viewTitle = findViewById(R.id.note_text_title);
        editTitle = findViewById(R.id.note_edit_title);
        check = findViewById(R.id.toolbar_check);
        back = findViewById(R.id.toolbar_back_arrow);

        viewModel = new ViewModelProvider(this, providerFactory).get(NoteViewModel.class);
        subscribeObservers();
        setListener();

        if (savedInstanceState == null) {
            getIncomingIntent();
            enableEditMode();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListener() {
        check.setOnClickListener(this);
        back.setOnClickListener(this);
        viewTitle.setOnClickListener(this);
        gestureDetector = new GestureDetector(this, this);
        linedEditText.setOnTouchListener(this);
    }


    private void subscribeObservers() {
        viewModel.observeNote().observe(this, note -> {
            if (note != null) {
                try {
                    setNoteProperties(note);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        viewModel.observeViewState().observe(this, viewState -> {
            if (viewState != null) {
                switch (viewState) {
                    case EDIT:
                        enableContentInteraction();
                        break;
                    case VIEW:
                        disableContentInteraction();
                        break;
                }
            }
        });
    }

    private void saveNote() {
        try {
            viewModel.saveNote().observe(this, integerResource -> {
                if (integerResource != null) {
                    switch (integerResource.status) {
                        case LOADING:
                            break;
                        case ERROR:
                            Log.d(TAG, "onChanged: ERROR... " + integerResource.message);
                            showSnackBar(integerResource.message);
                            break;
                        case SUCCESS:
                            Log.d(TAG, "onChanged: SUCCESS... " + integerResource.message);
                            showSnackBar(integerResource.message);
                            break;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            showSnackBar(e.getMessage());
        }
    }

    private void disableContentInteraction() {
        hideKeyboard(this);

        checkContainer.setVisibility(View.GONE);
        editTitle.setVisibility(View.GONE);
        backContainer.setVisibility(View.VISIBLE);
        viewTitle.setVisibility(View.VISIBLE);

        linedEditText.setKeyListener(null);
        linedEditText.setFocusable(false);
        linedEditText.setFocusableInTouchMode(false);
        linedEditText.setCursorVisible(false);
        linedEditText.clearFocus();
    }

    private void enableContentInteraction() {
        checkContainer.setVisibility(View.VISIBLE);
        editTitle.setVisibility(View.VISIBLE);
        backContainer.setVisibility(View.GONE);
        viewTitle.setVisibility(View.GONE);

        linedEditText.setKeyListener(new EditText(this).getKeyListener());
        linedEditText.setFocusable(true);
        linedEditText.setFocusableInTouchMode(true);
        linedEditText.setCursorVisible(true);
        linedEditText.requestFocus();
    }

    private void enableEditMode() {
        viewModel.setViewState(NoteViewModel.ViewState.EDIT);
    }

    private void disableEditMode() {
        viewModel.setViewState(NoteViewModel.ViewState.VIEW);

        if (!TextUtils.isEmpty(Objects.requireNonNull(linedEditText.getText()).toString())) {
            try {
                viewModel.updateNote(editTitle.getText().toString(), linedEditText.getText().toString());
            } catch (Exception e) {
                e.printStackTrace();
                showSnackBar("Error setting note properties");
            }
        }

        //insert and update
        saveNote();
    }

    public void setNoteProperties(Note note) throws Exception {
        if (note == null) {
            showSnackBar("Error displaying note properties");
            throw new Exception("Error displaying note properties");
        }

        viewTitle.setText(note.getTitle());
        editTitle.setText(note.getTitle());
        linedEditText.setText(note.getContent());

    }

    private void showSnackBar(String message) {
        if (!TextUtils.isEmpty(message)) {
            Snackbar.make(parent, message, Snackbar.LENGTH_SHORT).show();
        }
    }

    private void hideKeyboard(Activity activity) {
        InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view != null) {
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("has_started", true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_check: {
                disableEditMode();
                break;
            }
            case R.id.toolbar_back_arrow: {
                finish();
                break;
            }
            case R.id.note_text_title: {
                enableEditMode();
                editTitle.setSelection(editTitle.getText().toString().length());
                editTitle.requestFocus();
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (viewModel.shouldNavigateBack()) {
            super.onBackPressed();
        } else onClick(check);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        enableEditMode();
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
}