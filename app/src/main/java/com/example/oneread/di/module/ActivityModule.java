package com.example.oneread.di.module;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import com.example.oneread.di.anotation.ActivityContext;
import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

@Module
public class ActivityModule {

    private AppCompatActivity activity;

    public ActivityModule(AppCompatActivity activity) {this.activity = activity;}

    @Provides
    @ActivityContext
    public Context provideActivityContext() {
        return activity;
    }

    @Provides
    AppCompatActivity provideActivity() {
        return activity;
    }

    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

}
