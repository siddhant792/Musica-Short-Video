package com.mark.tiktok20.All;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mark.tiktok20.R;
import com.mark.tiktok20.models.User;

import cn.ymex.popup.controller.ProgressController;
import cn.ymex.popup.dialog.DialogManager;
import cn.ymex.popup.dialog.PopupDialog;

public class Register_user extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    RelativeLayout buttongoogle;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int DIALOUGE = 1;
    DialogManager dialogManager;
    private FirebaseDatabase database;
    ImageView terms;
    String userId;
    private DatabaseReference userRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user!=null){
            userId = mAuth.getCurrentUser().getUid();
            //checkExistence();
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        mAuth = FirebaseAuth.getInstance();
        buttongoogle = findViewById(R.id.signingoogle);
        terms = findViewById(R.id.privacy_button);
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register_user.this,Terms_Conditions.class);
                startActivity(intent);
            }
        });
        buttongoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        dialogManager = new DialogManager();
        PopupDialog.create(Register_user.this)
                .manageMe(dialogManager)
                .priority(DIALOUGE)
                .controller(ProgressController.build().message("Please Wait"));
        createRequest();
    }

    private void createRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
                dialogManager.show(DIALOUGE);
            } catch (ApiException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            mAuth = FirebaseAuth.getInstance();
                            userId = mAuth.getCurrentUser().getUid();
                            userRef = FirebaseDatabase.getInstance().getReference("users");
                            setDatainFirebase();
                        } else {
                            Toast.makeText(Register_user.this, "Auth Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setDatainFirebase() {

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i=0;
                for (DataSnapshot zoneSnapshot: snapshot.getChildren()){
                    if (zoneSnapshot.getKey().equals(userId)){
                        i++;
                    }
                }
                if (i==0){
                    Log.e("Status","Not Found");
                    GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(Register_user.this);
                    int random = (int) (Math.random() * (10000000 - 0)) + 0;
                    if (signInAccount!=null){
                        String name1 = signInAccount.getDisplayName()+"."+random;
                        String name2 = name1.toLowerCase();
                        String usernamefinal = name2.replaceAll("\\s+","");
                        String namefinal = signInAccount.getDisplayName();
                        Log.e("Name",namefinal);
                        String gender = "Not Specified";
                        String biofinal = "Hello there ! My name is "+namefinal.toLowerCase();
                        String uploadpicurl = signInAccount.getPhotoUrl().toString();
                        User user = new User(namefinal,usernamefinal,gender,biofinal,uploadpicurl,userId,0,0,0);
                        userRef.child(userId).setValue(user);
                        dialogManager.destroy();
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(Register_user.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Log.e("Status","Found");
                    dialogManager.destroy();
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(Register_user.this, "Welcome Back", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }
}