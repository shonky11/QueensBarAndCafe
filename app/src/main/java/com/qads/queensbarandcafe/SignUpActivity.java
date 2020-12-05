package com.qads.queensbarandcafe;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;

    private SignInButton mSignInButton;
    private ImageView sign_in_button;

    private LinearLayout login_layout;
    private CardView login_card;

    private GoogleSignInClient mSignInClient;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Assign fields
        mFirebaseAuth = FirebaseAuth.getInstance();
        //mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        sign_in_button = (ImageView) findViewById(R.id.sign_in_button_image);

        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        login_layout = findViewById(R.id.login_layout);
        login_card = (CardView) findViewById(R.id.login_card);

        login_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mSignInClient = GoogleSignIn.getClient(this, gso);

        // Set click listeners
        //mSignInButton.setOnClickListener(this);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseAuth.addAuthStateListener(mAuthStateListener);
        }

        /*FirebaseUser user = mFirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            Intent intent = new Intent(getApplicationContext(), OpeningTitlePage.class);
            startActivity(intent);
        }*/
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    //add required sign in method that actually presents the user with the google sign in ui
    private void signIn() {
        Intent signInIntent = mSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //handles the sign in result - if google sign in was successful, use the account to authenticate with firebase
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent in signIn()
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                final GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                            Log.d(TAG, "onComplete: " + mFirebaseAuth.getUid());
                            final DocumentReference docIdRef = rootRef.collection("users").document(Objects.requireNonNull(mFirebaseAuth.getUid()));
                            docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task1) {
                                    if (task1.isSuccessful()) {
                                        DocumentSnapshot document = task1.getResult();
                                        Log.d(TAG, "Document exists!");
                                        FirebaseMessaging.getInstance().subscribeToTopic(Objects.requireNonNull(mFirebaseAuth.getUid()))
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (!task.isSuccessful()) {
                                                                Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                                                            }

                                                        }
                                                });
                                        startActivity(new Intent(SignUpActivity.this, MainActivity.class));

                                        finish();
                                    } else {
                                        Log.d(TAG, "Failed with: ", task1.getException());
                                    }
                                }
                            });
                        }
                    }
                });
    }
}