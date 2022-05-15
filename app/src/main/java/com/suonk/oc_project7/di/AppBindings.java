package com.suonk.oc_project7.di;

import com.suonk.oc_project7.repositories.location.LocationRepository;
import com.suonk.oc_project7.repositories.location.LocationRepositoryImpl;
import com.suonk.oc_project7.repositories.places.PlacesRepository;
import com.suonk.oc_project7.repositories.places.PlacesRepositoryImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@InstallIn(SingletonComponent.class)
@Module
public abstract class AppBindings {

    @Binds
    abstract LocationRepository bindLocationRepository(LocationRepositoryImpl impl);

    @Binds
    abstract PlacesRepository bindPlacesRepository(PlacesRepositoryImpl impl);
}
