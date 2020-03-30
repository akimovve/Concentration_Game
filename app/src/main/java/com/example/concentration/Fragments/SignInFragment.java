package com.example.concentration.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.concentration.Info.User;
import com.example.concentration.R;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class SignInFragment extends Fragment {

    private static final String LOG_TAG = SignInFragment.class.getSimpleName();

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private GoogleSignInClient mGoogleSignInClient;

    private int RC_SIGN_IN = 9001;


    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.google_sign_in_button:
                    signInViaGoogle();
                    break;
                case R.id.facebook_sign_in_button:
                    break;
                case R.id.twitter_sign_in_button:
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        SignInButton googleSignInButton = view.findViewById(R.id.google_sign_in_button);
        LoginButton facebookSignInButton = view.findViewById(R.id.facebook_sign_in_button);
        TwitterLoginButton twitterSignInButton = view.findViewById(R.id.twitter_sign_in_button);

        googleSignInButton.setOnClickListener(mOnClickListener);
        facebookSignInButton.setOnClickListener(mOnClickListener);
        twitterSignInButton.setOnClickListener(mOnClickListener);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthViaGoogle(account);

                Toast.makeText(getActivity(), "Singed In Successfully", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ProfileFragment())
                        .addToBackStack(null)
                        .commit();

            } catch (ApiException e) {
                Toast.makeText(getActivity(), "Sing In Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void signInViaGoogle() {
        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }


    private void firebaseAuthViaGoogle(GoogleSignInAccount acct) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful() && task.getResult().getUser() != null) {
                            Log.d(LOG_TAG, "signInViaGoogleFireBase:success");
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            Log.d(LOG_TAG, "signInViaGoogleFireBase:failed");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }

    private void onAuthSuccess(final FirebaseUser user) {
        DatabaseReference userNameRef = mDatabase.child("users").child(user.getUid());


        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    writeNewUser(user.getUid(), user.getDisplayName(), user.getEmail(), user.getPhotoUrl());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(LOG_TAG, databaseError.getMessage());
            }
        };
        userNameRef.addListenerForSingleValueEvent(eventListener);
    }

    private void writeNewUser(String userId, String name, String email, Uri userPhoto) {

        User user = new User(name, email, userPhoto);
        mDatabase.child("users").child(userId).setValue(user);
        Log.d(LOG_TAG, String.valueOf(user));
    }

}
