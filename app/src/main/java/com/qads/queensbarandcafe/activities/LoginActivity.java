package com.qads.queensbarandcafe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.qads.queensbarandcafe.R;
import com.qads.queensbarandcafe.models.UserModel;

public class LoginActivity extends AppCompatActivity {

    EditText txtfirstname, txtlastname;
    CardView signupbtn;
    TextView privacyPolicy;

    // Firebase instance variable
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase mFirebaseDatabase;
    //private DatabaseReference mFirebaseDatabaseReference;

    //this will get the current instance of the document in cloud firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    //this retrieves the entire document with this specific uid - needed to update the profile information
    private DocumentReference docRef = db.collection("users").document(userid);

    private DocumentReference docRef2 = db.collection("settings").document("link");

    private String TAG = "MyActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtfirstname = (EditText) findViewById(R.id.first);
        txtlastname = (EditText) findViewById(R.id.last);
        privacyPolicy = (TextView) findViewById(R.id.privacy_policy_hyperlink);
        privacyPolicy.setMovementMethod(LinkMovementMethod.getInstance());

        //gets current instance of the database
        mFirebaseAuth = FirebaseAuth.getInstance();
        signupbtn = (CardView) findViewById(R.id.signupbtn);

        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        final String email = user.getEmail();
        final String crsid = mFirebaseAuth.getCurrentUser().getEmail().split("@")[0];

        //need to add the privacy policy website
        //need to add the signout button

        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (document.exists()){
                        String privacyPolicytext = String.valueOf(document.get("link"));
                        privacyPolicy.setText(privacyPolicytext);
                    }
                }else{
                    Toast.makeText(getApplication(), "No Privacy Policy", Toast.LENGTH_SHORT).show();
                }
            }
        });


        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String first_name = txtfirstname.getText().toString().trim();
                String last_name = txtlastname.getText().toString().trim();

                if(first_name.isEmpty() || last_name.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please fill out all the fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    UserModel users = new UserModel(crsid, mFirebaseAuth.getUid(), first_name, last_name, email);
                    docRef.set(users)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot written with ID: ");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }

            }
        });

    }
}
