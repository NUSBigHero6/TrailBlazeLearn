package org.nus.trailblaze.dao;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.Task;

/**
 * Created by plasmashadow on 3/30/18.
 */

public class EmailLoginDao extends AuthDao {

    public EmailLoginDao(){
        super();
    }

    public EmailLoginDao(FirebaseAuth auth){
        super(auth);
    }

    public Task<AuthResult> createFirebaseEmailAccount(String email, String password){
        return this.getAuth().signInWithEmailAndPassword(email,password);
    }
}
