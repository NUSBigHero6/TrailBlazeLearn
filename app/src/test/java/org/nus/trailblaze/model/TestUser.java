package org.nus.trailblaze.model;

import org.junit.Test;
import org.nus.trailblaze.models.User;

import static junit.framework.Assert.assertEquals;

public class TestUser {

    User user1 = new User ("id-123", "NUSBigHero6", "NUSBigHero6@gmail.com");

    @Test
    public void testGetID() {

        assertEquals ("id-123", user1.getId());
    }

    @Test
    public void testGetName() {

        assertEquals ("NUSBigHero6", user1.getName());
    }

    @Test
    public void testGetEmail() {

        assertEquals ("NUSBigHero6@gmail.com", user1.getEmail());
    }
}
