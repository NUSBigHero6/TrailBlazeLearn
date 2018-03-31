
package org.nus.trailblaze.adaptors;

/**
 * Created by liu.cao on 26/3/2018.
 */

import  org.junit.Test;
import org.nus.trailblaze.adapters.IntentHelper;

import static junit.framework.Assert.assertTrue;

public class TestIntentHelper {

    @Test
    public void testgetMimeTypes_null() {
        String[] mimeType=null;
        mimeType=IntentHelper.getMimeTypes("");
        assertTrue(mimeType!=null);
        assertTrue(mimeType[0]=="");
    }


@Test
public void testgetMimeTypes_document()
{
    String[] mimeType=null;
    mimeType= IntentHelper.getMimeTypes("document");
    assertTrue(mimeType!=null);
    assertTrue(mimeType[0]=="text/plain" );
    assertTrue(mimeType[1]=="application/pdf" );


}
    @Test
    public void testgetMimeTypes_image()
    {
        String[] mimeType=null;
        mimeType=IntentHelper.getMimeTypes("image");
        assertTrue(mimeType!=null);
        assertTrue(mimeType[0]=="image/*");
    }

    @Test
    public void testgetMimeTypes_media()
    {
        String[] mimeType=null;
        mimeType=IntentHelper.getMimeTypes("audio");
        assertTrue(mimeType!=null);
        assertTrue(mimeType[0]=="");

        mimeType=IntentHelper.getMimeTypes("audio/video");
        assertTrue(mimeType!=null);
        assertTrue(mimeType[0]=="audio/*");
        assertTrue(mimeType[1]=="video/mp4");
    }
}
