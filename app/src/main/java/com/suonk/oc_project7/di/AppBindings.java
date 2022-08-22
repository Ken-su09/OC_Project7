package com.suonk.oc_project7.di;

import com.suonk.oc_project7.repositories.current_location.CurrentLocationRepository;
import com.suonk.oc_project7.repositories.current_location.CurrentLocationRepositoryImpl;
import com.suonk.oc_project7.repositories.current_user_search.CurrentUserSearchRepository;
import com.suonk.oc_project7.repositories.current_user_search.CurrentUserSearchRepositoryImpl;
import com.suonk.oc_project7.repositories.places.PlacesRepository;
import com.suonk.oc_project7.repositories.places.PlacesRepositoryImpl;
import com.suonk.oc_project7.repositories.restaurants.RestaurantsRepository;
import com.suonk.oc_project7.repositories.restaurants.RestaurantsRepositoryImpl;
import com.suonk.oc_project7.repositories.workmates.WorkmatesRepository;
import com.suonk.oc_project7.repositories.workmates.WorkmatesRepositoryImpl;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@InstallIn(SingletonComponent.class)
@Module
public abstract class AppBindings {

    @Binds
    @Singleton
    abstract CurrentLocationRepository bindLocationRepository(CurrentLocationRepositoryImpl impl);

    @Binds
    @Singleton
    abstract PlacesRepository bindPlacesRepository(PlacesRepositoryImpl impl);

    @Binds
    @Singleton
    abstract RestaurantsRepository bindRestaurantsRepository(RestaurantsRepositoryImpl impl);

    @Binds
    @Singleton
    abstract WorkmatesRepository bindWorkmatesRepository(WorkmatesRepositoryImpl impl);

    @Binds
    @Singleton
    abstract CurrentUserSearchRepository bindCurrentUserSearchRepository(CurrentUserSearchRepositoryImpl impl);
}
