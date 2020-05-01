package com.example.concentration.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.concentration.R;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ProfileFragment extends Fragment {

    private static final String LOG_TAG = ProfileFragment.class.getSimpleName();

    private TextView userName, userEmail;
    private EditText newName;
    private Button okButton;
    private ImageView userPic;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");
        String idToken = "922738481147-a328d75if7kk2k0gfbblqrua2fvkk4f9.apps.googleusercontent.com";
        //String idToken = getString(R.string.default_web_client_id);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(idToken)
                .requestEmail()
                .build();

        mAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Button signOutButton = view.findViewById(R.id.sign_out_button);
        Button newUserNameButton = view.findViewById(R.id.change_name_button);
        okButton = view.findViewById(R.id.ok_new_button);
        okButton.setVisibility(View.INVISIBLE);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        newUserNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                okButton.setVisibility(View.VISIBLE);
                setNewName();
            }
        });

        userName = view.findViewById(R.id.user_name);
        userEmail = view.findViewById(R.id.user_email);
        userPic = view.findViewById(R.id.profile_image);
        newName = view.findViewById(R.id.enter_new_name);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "onStart");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user) {
        Log.d(LOG_TAG, String.valueOf(user));
        if (user != null) {
            String personGivenName = user.getDisplayName();
            String personEmail = user.getEmail();
            Uri personPhoto = user.getPhotoUrl();

            userName.setText(String.valueOf(personGivenName));
            userEmail.setText(String.valueOf(personEmail));

            Glide.with(getActivity())
                    .load(personPhoto)
                    .into(userPic);

            userPic.setVisibility(View.VISIBLE);
        } else {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new SignInFragment())
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void signOut() {
        mAuth.signOut();
        // GOOGLE
        mGoogleSignInClient.signOut().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                Toast.makeText(getActivity(), "Signed out Successfully", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SignInFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
        // FACEBOOK
        LoginManager.getInstance().logOut();
    }

    private void setNewName() {
        final FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            return;
        }

        userName.setVisibility(View.INVISIBLE);
        newName.setVisibility(View.VISIBLE);
        newName.setHint(user.getDisplayName());

        newName.requestFocus();
        InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.showSoftInput(newName, InputMethodManager.SHOW_IMPLICIT);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = newName.getText().toString();

                if (!name.isEmpty()) {

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(LOG_TAG, "User profile updated.");
                                    } else {
                                        Log.d(LOG_TAG, "User profile wasn't updated.");
                                    }
                                }
                            });
                }

                userName.setVisibility(View.VISIBLE);
                newName.setVisibility(View.INVISIBLE);
                okButton.setVisibility(View.INVISIBLE);

                newName.clearFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
    }
}
