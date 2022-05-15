package com.suonk.oc_project7.repositories.location;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.google.android.gms.location.FusedLocationProviderClient;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LocationRepositoryImplTest {

    @Rule
    public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final FusedLocationProviderClient fusedLocationProviderClientMock = mock(FusedLocationProviderClient.class);

    private LocationRepository locationRepository;

    @Before
    public void setUp() {
        locationRepository = new LocationRepositoryImpl(fusedLocationProviderClientMock);
//        locationRepository.
    }
}