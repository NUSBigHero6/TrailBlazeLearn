package org.nus.trailblaze.model;

import org.junit.Test;
import org.nus.trailblaze.models.Post;

import static junit.framework.Assert.assertEquals;

public class TestPost {

    Post post1 = new Post ("123@id", null, null, null, "Hello World!");

    @Test
    public void testGetID() {

        assertEquals ("123@id", post1.getId());
    }

    @Test
    public void testGetUser() {

        assertEquals (null, post1.getUser());
    }

    @Test
    public void testGetCreatedDate() {

        assertEquals (null, post1.getCreatedDate());
    }

    @Test
    public void testGetMessage() {

        assertEquals ("Hello World!", post1.getMessage());
    }
}
