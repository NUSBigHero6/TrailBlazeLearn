package org.nus.trailblaze;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Button;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.Task;

import org.nus.trailblaze.dao.FacebookDao;
import org.nus.trailblaze.listeners.FacebookRegistryListener;
import org.nus.trailblaze.listeners.SignInFailureListener;
import org.nus.trailblaze.listeners.SignInListener;

import org.nus.trailblaze.dao.GoogleDao;
import org.nus.trailblaze.views.ContributedItemDocActivity;
import org.nus.trailblaze.views.ContributedItemImageActivity;
import org.nus.trailblaze.views.ContributedItemMediaActivity;
import org.nus.trailblaze.views.RoleToggler;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailBlazeMainActivity extends AppCompatActivity {


    public static final int GOOGLE_SIGN = 9001;
    public static final int FACEBOOK_SIGN = 64206;

    public FirebaseUser loggedInUser;

    // required for google Login
    private GoogleSignInClient gClient;
    private GoogleDao gDao;


    // required for facebook login
    private LoginManager fmanager;
    private CallbackManager mCallback;
    private FacebookDao fDao;

    private  Button btn_create_ContributedItem_Doc;
    private  Button btn_create_ContributedItem_Img;
    private  Button btn_ContributedItemList;
    private Button btn_create_ContributedItem_Media;
    @BindView(R.id.progressBar)
    ProgressBar bar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // invoking the constructor and initializing the layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trail_blaze_main);
        FirebaseApp.initializeApp(this);
        ButterKnife.bind(this);

        btn_create_ContributedItem_Doc=(Button)findViewById(R.id.btn_create_ContributedItem_Doc);
        btn_create_ContributedItem_Doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ContributedItemDocActivity.class));
            }
        });

        btn_create_ContributedItem_Img=(Button)findViewById(R.id.btn_create_ContributedItem_Img);
        btn_create_ContributedItem_Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ContributedItemImageActivity.class));
            }
        });

        btn_create_ContributedItem_Media=(Button)findViewById(R.id.btn_create_ContributedItem_Media);
        btn_create_ContributedItem_Media.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ContributedItemMediaActivity.class));
            }
        });

        btn_ContributedItemList=(Button)findViewById(R.id.btn_ContributedItemList);
        // CallbackManager to initalize the Facebook button
        fmanager = LoginManager.getInstance();
        mCallback = CallbackManager.Factory.create();
        gDao = new GoogleDao();
        fDao = new FacebookDao();
        gClient = gDao.getClient(this);


        Context currentContext = getApplicationContext();
        FacebookSdk.sdkInitialize(currentContext);


        Log.d("client id", getString(R.string.default_web_client_id));

        // if the user has already logged in send them strait to next activity.
        loggedInUser = fDao.getCurrent();
        if(loggedInUser != null){
            Intent next = new Intent(this, RoleToggler.class);
            next.putExtra("user", this.fDao.fromFirebaseUser(loggedInUser));
            startActivity(next);
        }

        // initalize a callback to facebook button
        fmanager.registerCallback(mCallback, new FacebookRegistryListener(this, this.fDao, RoleToggler.class));
    }

    // Google click listener
    public void googleClickListener(View view){
        Intent intent = gClient.getSignInIntent();
        startActivityForResult(intent, GOOGLE_SIGN);
    }

    // facebook click iistener
    public void facebookClickListener(View view){
        fmanager.logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
    }

    // update on google account selected.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        Log.d("act", "got the code");
        Log.d("mtg", String.valueOf(requestCode));
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GOOGLE_SIGN){
            Log.d(String.valueOf(requestCode), data.toString());
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {

                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("account", account.toString());
                bar.setVisibility(bar.VISIBLE);
                this.gDao
                        .createFirebaseGoogleAuth(account)
                        .addOnCompleteListener(this,
                                new SignInListener(this, this.gDao, RoleToggler.class))
                        .addOnFailureListener(this, new SignInFailureListener(this));
            }
            catch (ApiException e){
                // handle exeception here.
                Log.d("Exception", e.toString());
                e.printStackTrace();
            }

        } else if(requestCode == FACEBOOK_SIGN) {
            bar.setVisibility(bar.VISIBLE);
            mCallback.onActivityResult(requestCode, resultCode, data);
        }
    }

}
