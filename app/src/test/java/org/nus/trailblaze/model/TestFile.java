package org.nus.trailblaze.model;

import org.junit.Test;
import org.nus.trailblaze.models.File;

import static junit.framework.Assert.assertEquals;

public class TestFile {

    File file1 = new File( "id0", "name", "https://github.com/NUSBigHero6/TrailBlazeLearn",
            3.6f, null, "mimeType");

    @Test
    public void testGetID() {

        assertEquals ("id0", file1.getId());
    }

    @Test
    public void testGetName() {

        assertEquals ("name", file1.getName());
    }

    @Test
    public void testGetUrl() {

        assertEquals ("https://github.com/NUSBigHero6/TrailBlazeLearn", file1.getUrl());
    }

    @Test
    public void testGetSize() {

        assertEquals (3.6f, file1.getSize());
    }

    @Test
    public void testGetUploadDate() {

        assertEquals (null, file1.getUploadDate());
    }
}
