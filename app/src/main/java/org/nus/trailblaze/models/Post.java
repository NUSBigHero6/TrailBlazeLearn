package org.nus.trailblaze.models;

import java.util.Date;

/**
 * Created by plasmashadow on 3/11/18.
 */

public class Post extends Item{
    private String message;

    public Post(){}
    public Post(String id, User user, Date createdDate, File file, String message) {
        super(id, user, createdDate, file);
        this.message = message;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
