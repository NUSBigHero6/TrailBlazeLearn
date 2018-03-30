package org.nus.trailblaze.models;

/**
 * Created by plasmashadow on 3/11/18.
 */

public class Trainer extends User {

    public Trainer(){}

    public Trainer(String id, String name, String email) {
        super(id, name, email);
    }

    public static Trainer fromUser(User user){
        return new Trainer(user.getId(), user.getName(), user.getEmail());
    }
}
