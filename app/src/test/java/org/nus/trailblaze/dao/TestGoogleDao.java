package org.nus.trailblaze.dao;

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

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.api.mockito.PowerMockito;
/**
 * Created by plasmashadow on 3/18/18.
 */


// This tests doesn't reflect on coverage.
// https://groups.google.com/forum/#!topic/jacoco/wxXMr-vk8xU

@RunWith(PowerMockRunner.class)
@PrepareForTest({GoogleAuthProvider.class, FirebaseAuth.class, GoogleSignIn.class, FirebaseApp.class, FirebaseFirestore.class})
public class TestGoogleDao {

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
    public void testFirebaseGooglePass(){
        PowerMockito.mockStatic(FirebaseApp.class);
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.mockStatic(GoogleAuthProvider.class);
        PowerMockito.mockStatic(FirebaseFirestore.class);
        PowerMockito.when(FirebaseFirestore.getInstance()).thenReturn(this.store);
        GoogleDao dao = new GoogleDao(this.auth);
        PowerMockito.when(account.getIdToken()).thenReturn("sometoken");
        PowerMockito.when(GoogleAuthProvider.getCredential("sometoken", null)).thenReturn(cred);
        dao.createFirebaseGoogleAuth(account);
        verify(auth).signInWithCredential(cred);
    }

    @Test
    public void testGetSignInClient(){
        PowerMockito.mockStatic(FirebaseApp.class);
        PowerMockito.mockStatic(GoogleSignIn.class);
        PowerMockito.mockStatic(FirebaseFirestore.class);
        PowerMockito.when(FirebaseFirestore.getInstance()).thenReturn(this.store);
        GoogleDao dao = new GoogleDao(this.auth);
        dao = spy(dao);
        doReturn(opts).when(dao).getOptions();
        PowerMockito.when(GoogleSignIn.getClient(this.act, opts)).thenReturn(client);
        assertThat(dao.getClient(this.act), is(client));
    }

    @Test
    public void testGetCurrentUser(){
        PowerMockito.mockStatic(FirebaseApp.class);
        PowerMockito.mockStatic(FirebaseFirestore.class);
        PowerMockito.when(FirebaseFirestore.getInstance()).thenReturn(this.store);
        GoogleDao dao = new GoogleDao(this.auth);
        FirebaseUser user = mock(FirebaseUser.class);
        when(this.auth.getCurrentUser()).thenReturn(user);
        assertThat(dao.getCurrent(), is(user));
    }


}
