package org.nus.trailblaze;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import android.app.Activity;
import android.text.TextUtils;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.Mock;
import static org.junit.Assert.*;
import org.nus.trailblaze.dao.EmailLoginDao;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.api.mockito.PowerMockito;

/**
 * Created by plasmashadow on 3/30/18.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({GoogleAuthProvider.class, FirebaseAuth.class, GoogleSignIn.class, FirebaseApp.class, FirebaseFirestore.class})
public class TestEmailDao {

    @Mock
    FirebaseAuth auth;

    @Mock
    GoogleSignInAccount account;

    @Mock
    GoogleSignInOptions opts;

    @Mock
    FirebaseFirestore store;

    @Mock
    GoogleSignInClient client;

    @Mock
    AuthCredential cred;

    @Mock
    Activity act;

    @Test
    public void testEmailAccountCreation(){
        PowerMockito.mockStatic(FirebaseAuth.class);
        PowerMockito.mockStatic(FirebaseFirestore.class);
        PowerMockito.when(FirebaseFirestore.getInstance()).thenReturn(this.store);
        EmailLoginDao dao = new EmailLoginDao(this.auth);
        dao.createFirebaseEmailAccount("username", "password");
        verify(this.auth).signInWithEmailAndPassword("username", "password");
    }
}
