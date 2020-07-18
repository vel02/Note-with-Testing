package kiz.learnwithvel.notes.ui.note;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import kiz.learnwithvel.notes.ui.Resource;

public abstract class InsertUpdateResource<T> {

    public static final String ACTION_INSERT = "ACTION_INSERT";
    public static final String ACTION_UPDATE = "ACTION_UPDATE";
    private MediatorLiveData<Resource<T>> result = new MediatorLiveData<>();

    public InsertUpdateResource() {

        result.setValue(Resource.loading(null));
        try {
            final LiveData<Resource<T>> source = getAction();
            result.addSource(source, tResource -> {
                result.removeSource(source);
                result.setValue(tResource);
                setNoteIdIfIsNewNote(tResource);
                completed();
            });
        } catch (Exception e) {
            e.printStackTrace();
            result.setValue(Resource.error(null, "Something went wrong"));
        }

    }


    private void setNoteIdIfIsNewNote(Resource<T> resource) {
        if (resource.data != null) {
            if (resource.data.getClass() == Integer.class) {
                int id = (Integer) resource.data;
                if (definedAction().equals(ACTION_INSERT)) {
                    if (id >= 0) {
                        setNoteId(id);
                    }
                }
            }
        }
    }

    protected abstract void completed();

    protected abstract void setNoteId(int id);

    protected abstract String definedAction();

    protected abstract LiveData<Resource<T>> getAction() throws Exception;

    public LiveData<Resource<T>> getAsLiveData() {
        return result;
    }
}
