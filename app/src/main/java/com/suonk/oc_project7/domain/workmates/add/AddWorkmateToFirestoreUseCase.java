package com.suonk.oc_project7.domain.workmates.add;

import androidx.annotation.NonNull;

import com.suonk.oc_project7.model.data.user.CustomFirebaseUser;
import com.suonk.oc_project7.model.data.workmate.Workmate;
import com.suonk.oc_project7.repositories.user.UserRepository;
import com.suonk.oc_project7.repositories.workmates.WorkmatesRepository;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AddWorkmateToFirestoreUseCase {

    @NonNull
    private final WorkmatesRepository workmatesRepository;

    @NonNull
    private final UserRepository userRepository;

    @Inject
    public AddWorkmateToFirestoreUseCase(@NonNull WorkmatesRepository workmatesRepository, @NonNull UserRepository userRepository) {
        this.workmatesRepository = workmatesRepository;
        this.userRepository = userRepository;
    }

    public void addWorkmateToFirestore() {
        if (userRepository.getCustomFirebaseUser() != null) {
            CustomFirebaseUser user = userRepository.getCustomFirebaseUser();
            final String id = user.getId();

            final Workmate workmateToAdd = new Workmate(
                    id,
                    user.getDisplayName(),
                    user.getEmail(),
                    user.getPhotoUrl(),
                    "",
                    "",
                    new ArrayList<>());

            workmatesRepository.addWorkmateToFirestore(id, workmateToAdd);
        }
    }
}
