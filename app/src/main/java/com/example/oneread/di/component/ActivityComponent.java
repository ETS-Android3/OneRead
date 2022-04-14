package com.example.oneread.di.component;

import com.example.oneread.di.anotation.ActivityScope;
import com.example.oneread.di.module.ActivityModule;
import com.example.oneread.ui.main.MainActivity;
import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

}
