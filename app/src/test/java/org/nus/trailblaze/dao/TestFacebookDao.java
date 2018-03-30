package org.nus.trailblaze.dao;


import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.mock;

import com.facebook.AccessToken;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.api.mockito.PowerMockito;
/**
 * Created by plasmashadow on 3/21/18.
 */


@RunWith(PowerMockRunner.class)
@PrepareForTest({FacebookAuthProvider.class, FirebaseAuth.class, AccessToken.class, FirebaseFirestore.class})
public class TestFacebookDao {

    @Mock
    FirebaseAuth auth;

    @Mock
    FirebaseFirestore store;

    @Mock
    AuthCredential cred;

    @Mock
    AccessToken token;

    @Test
    public void testFacebookCreateCreds(){
        PowerMockito.mockStatic(FirebaseAuth.class);
        PowerMockito.mockStatic(FacebookAuthProvider.class);
        PowerMockito.mockStatic(FirebaseFirestore.class);
        PowerMockito.when(FirebaseFirestore.getInstance()).thenReturn(this.store);
        FacebookDao dao = new FacebookDao(this.auth);
        PowerMockito.when(token.getToken()).thenReturn("sometoken");
        PowerMockito.when(FacebookAuthProvider.getCredential("sometoken")).thenReturn(this.cred);
        dao.createFirebaseFacebookAuth(this.token);
        verify(this.token).getToken();
        verify(this.auth).signInWithCredential(this.cred);
    }
}
