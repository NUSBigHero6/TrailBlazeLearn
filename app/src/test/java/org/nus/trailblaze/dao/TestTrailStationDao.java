package org.nus.trailblaze.dao;

/**
 * Created by AswathyLeelakumari on 31/3/2018.
 */

// To be tested.
import org.nus.trailblaze.models.Location;
import org.nus.trailblaze.models.TrailStation;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.nus.trailblaze.models.Trainer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FirebaseAuth.class, FirebaseFirestore.class})

public class TestTrailStationDao {

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

    String instruction = "1.a direction or order.\"he issued instructions to the sheriff\"synonyms:" +
            "\torder, command, directive, direction, decree, edict, injunction, mandate, dictate, " +
            "commandment, diktat, demand, bidding, requirement, stipulation, charge, ruling, mandate" +
            ", pronouncement; summons, writ, subpoena, warrant; informalsay-so; literarybehest; rare" +
            "rescript\"if a prisoner disobeys an instruction, he will be punished";

    Location location = new Location(1.2966426, 103.7763939, "NUS");


    @Test
    public void testCreateTrailStation(){

        PowerMockito.mockStatic(FirebaseAuth.class);
        PowerMockito.mockStatic(FacebookAuthProvider.class);
        PowerMockito.mockStatic(FirebaseFirestore.class);
        PowerMockito.when(FirebaseFirestore.getInstance()).thenReturn(this.store);

        // creating an empty trail station
        TrailStation station = new TrailStation();
        station.setId("1803301243");
        station.setName("NUSISS");
        station.setInstruction(instruction);
        station.setLocation(location);
        station.setTrailId("180330-nus walk");
        station.setSequence("1");

        // creating a station information.
        TrailStationDao dao = new TrailStationDao(this.cref);
        when(this.cref.document("1803301243")).thenReturn(dref);
        when(dref.set(station)).thenReturn(complete);
        assertThat(dao.createNewStation(station), is(complete));

    }

    @Test
    public void testGetStationById(){
        PowerMockito.mockStatic(FirebaseAuth.class);
        PowerMockito.mockStatic(FacebookAuthProvider.class);
        PowerMockito.mockStatic(FirebaseFirestore.class);
        PowerMockito.when(FirebaseFirestore.getInstance()).thenReturn(this.store);

        // Test station get by id
        TrailStationDao dao = new TrailStationDao(this.cref);

        when(this.cref.whereEqualTo("id", "1803301243")).thenReturn(query);
        when(query.get()).thenReturn(this.empty);
        assertThat(dao.getStationById("1803301243"), is(empty));
    }

}
