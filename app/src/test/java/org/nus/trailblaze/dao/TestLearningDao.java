package org.nus.trailblaze.dao;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.mock;

import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.Task;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.Mock;
import static org.junit.Assert.*;

import org.nus.trailblaze.models.Trainer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.api.mockito.PowerMockito;

/**
 * Created by plasmashadow on 3/24/18.
 */

// To be tested.
import org.nus.trailblaze.models.LearningTrail;

import java.util.Date;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FirebaseAuth.class, FirebaseFirestore.class})
public class TestLearningDao {

    @Mock
    FirebaseAuth auth;

    @Mock
    FirebaseFirestore store;

    @Mock
    CollectionReference cref;

    @Mock
    Task<QuerySnapshot> empty;

    @Mock
    Task<Void> complete;

    @Mock
    Query query;

    @Mock
    DocumentReference dref;


    @Test
    public void testcreateLearningTrail(){

        PowerMockito.mockStatic(FirebaseAuth.class);
        PowerMockito.mockStatic(FacebookAuthProvider.class);
        PowerMockito.mockStatic(FirebaseFirestore.class);
        PowerMockito.when(FirebaseFirestore.getInstance()).thenReturn(this.store);

        // creating an empty learning trail
        LearningTrail trail = new LearningTrail();
        trail.setId("cheekit");
        trail.setName("cheekit");
        trail.setTrailDate(new Date());
        trail.setTrainer(new Trainer("1", "323", "323"));

        // creating a trail information.
        LearningTrailDao dao = new LearningTrailDao(this.cref);
        when(this.cref.document("cheekit")).thenReturn(dref);
        when(dref.set(trail)).thenReturn(complete);
        assertThat(dao.createNewTrail(trail), is(complete));

    }

    @Test
    public void testGetTrailById(){
        PowerMockito.mockStatic(FirebaseAuth.class);
        PowerMockito.mockStatic(FacebookAuthProvider.class);
        PowerMockito.mockStatic(FirebaseFirestore.class);
        PowerMockito.when(FirebaseFirestore.getInstance()).thenReturn(this.store);

        // Test learning get by id
        LearningTrailDao dao = new LearningTrailDao(this.cref);

        when(this.cref.whereEqualTo("name", "cheekit")).thenReturn(query);
        when(query.get()).thenReturn(this.empty);
        assertThat(dao.getTrailById("cheekit"), is(empty));
    }


    @Test
    public void testGetTrails(){
        PowerMockito.mockStatic(FirebaseAuth.class);
        PowerMockito.mockStatic(FacebookAuthProvider.class);
        PowerMockito.mockStatic(FirebaseFirestore.class);
        PowerMockito.when(FirebaseFirestore.getInstance()).thenReturn(this.store);

        // Test learning get by id
        LearningTrailDao dao = new LearningTrailDao(this.cref);
        Trainer mockTrainer = new Trainer("cheekit", "cheekit", "cheekit@gmail.com");

        when(this.cref.whereEqualTo("trainer_id", "cheekit")).thenReturn(query);
        when(query.get()).thenReturn(this.empty);
        assertThat(dao.getTrails(mockTrainer), is(empty));
    }

}
