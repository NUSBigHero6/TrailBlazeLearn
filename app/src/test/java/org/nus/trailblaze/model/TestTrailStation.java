package org.nus.trailblaze.model;

import org.junit.Test;
import org.nus.trailblaze.models.TrailStation;

import static junit.framework.Assert.assertEquals;

public class TestTrailStation {

    String instruction = "1.a direction or order.\"he issued instructions to the sheriff\"synonyms:" +
            "\torder, command, directive, direction, decree, edict, injunction, mandate, dictate, " +
            "commandment, diktat, demand, bidding, requirement, stipulation, charge, ruling, mandate" +
            ", pronouncement; summons, writ, subpoena, warrant; informalsay-so; literarybehest; rare" +
            "rescript\"if a prisoner disobeys an instruction, he will be punished";

    TrailStation trailStation1 = new TrailStation("id_321", "Park", instruction, "1", "180330-small walk");

    @Test
    public void testGetID() {

        assertEquals("id_321", trailStation1.getId());
    }

    @Test
    public void testGetLocation() {

        assertEquals( null, trailStation1.getLocation());
    }

    @Test
    public void testGetName() {

        assertEquals("Park", trailStation1.getName());
    }

    @Test
    public void testGetInstruction() {

        assertEquals(instruction, trailStation1.getInstruction());
    }
}
