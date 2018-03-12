package com.kmo.drom.kmoappdrom.Cicerone;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kmoaz on 07.03.2018.
 */

@Module
public class LocalNavigationModule {
    @Provides
    @Singleton
    LocalCiceroneHolder provideLocalNavigationHolder() {
        return new LocalCiceroneHolder();
    }
}
