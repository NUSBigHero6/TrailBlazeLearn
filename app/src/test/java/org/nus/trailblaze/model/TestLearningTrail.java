package org.nus.trailblaze.model;

import org.junit.Test;
import java.util.UUID;
import org.nus.trailblaze.models.LearningTrail;

import static junit.framework.Assert.*;

public class TestLearningTrail {

    String trailID1 = UUID.randomUUID().toString();
    String expectedTrailID1 = trailID1;
    String trailID2 = UUID.randomUUID().toString();
    String expectedTrailID2 = trailID2;

    LearningTrail learningTrail1 = new LearningTrail (trailID1, null, "180101-Walk", null);
    LearningTrail learningTrail2 = new LearningTrail (trailID2, null, "170202-Run", null);

    @Test
    public void testGetName() {

        assertEquals ("180101-Walk", learningTrail1.getName());
        assertEquals ("170202-Run", learningTrail2.getName());
    }

    @Test
    public void testGetTrailID() {

        assertEquals (expectedTrailID1, trailID1);
        assertEquals (expectedTrailID2, trailID2);
    }
}
