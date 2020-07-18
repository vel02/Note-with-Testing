package kiz.learnwithvel.notes;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import kiz.learnwithvel.notes.di.DaggerAppComponent;

public class BaseApplication extends DaggerApplication {
    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }
}
