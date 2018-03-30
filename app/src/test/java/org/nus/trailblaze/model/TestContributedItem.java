package org.nus.trailblaze.model;

import org.junit.Test;
import org.nus.trailblaze.models.ContributedItem;

import static junit.framework.Assert.assertEquals;

public class TestContributedItem {

    ContributedItem contributedItem1 = new ContributedItem("String id", null, null,
            null, "String description");

    @Test
    public void testGetID() {

        assertEquals ("String id", contributedItem1.getId());
    }

    @Test
    public void testGetUser() {

        assertEquals (null, contributedItem1.getUser());
    }

    @Test
    public void testGetCreatedDate() {

        assertEquals (null, contributedItem1.getCreatedDate());
    }

    @Test
    public void testGetDescription() {

        assertEquals ("String description", contributedItem1.getDescription());
    }

}


