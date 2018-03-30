package org.nus.trailblaze.dao;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import static org.mockito.Mockito.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.Executor;

/**
 * Created by plasmashadow on 3/11/18.
 */

@RunWith(MockitoJUnitRunner.class)
public class TestUserDao {

    public static final Task<DocumentSnapshot> emptyDocRef = new Task<DocumentSnapshot>() {
        @Override
        public boolean isComplete() {
            return false;
        }

        @Override
        public boolean isSuccessful() {
            return false;
        }

        @Override
        public DocumentSnapshot getResult() {
            return null;
        }

        @Override
        public <X extends Throwable> DocumentSnapshot getResult(@NonNull Class<X> aClass) throws X {
            return null;
        }

        @Nullable
        @Override
        public Exception getException() {
            return null;
        }

        @NonNull
        @Override
        public Task<DocumentSnapshot> addOnSuccessListener(@NonNull OnSuccessListener<? super DocumentSnapshot> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<DocumentSnapshot> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super DocumentSnapshot> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<DocumentSnapshot> addOnSuccessListener(@NonNull Activity activity, @NonNull OnSuccessListener<? super DocumentSnapshot> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<DocumentSnapshot> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<DocumentSnapshot> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<DocumentSnapshot> addOnFailureListener(@NonNull Activity activity, @NonNull OnFailureListener onFailureListener) {
            return null;
        }
    };

    @Mock
    CollectionReference cref;

    @Mock
    DocumentReference dref;



    @Test
    public void testGetUser(){
        UserDao userDao = new UserDao(cref);
        when(dref.get()).thenReturn(emptyDocRef);
        when(cref.document("someid")).thenReturn(dref);
        assertThat(userDao.getUser("someid"), is(emptyDocRef));
    }
}
