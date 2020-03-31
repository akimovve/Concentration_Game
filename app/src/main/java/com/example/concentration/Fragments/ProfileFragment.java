package com.example.concentration.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.concentration.Info.User;
import com.example.concentration.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private TextView userName, userEmail;
    private EditText newName;
    private Button okButton;
    private ImageView userPic;
    private GoogleSignInClient mGoogleSignInClient;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private static final String LOG_TAG = ProfileFragment.class.getSimpleName();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

        initFireBaseGoogleSignIn();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        updateUI();
    }


    private void initFireBaseGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
    }

    private void updateUI() {

        if (user != null) {
            String personGivenName = user.getDisplayName();
            String personEmail = user.getEmail();
            Uri personPhoto = user.getPhotoUrl();

            userName.setText(String.valueOf(personGivenName));
            userEmail.setText(String.valueOf(personEmail));

            if (personPhoto == null) {
                userPic.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_launcher_background));
            } else {
                Glide.with(ProfileFragment.this.getActivity())
                        .load(personPhoto)
                        .into(userPic);
            }
            userPic.setVisibility(View.VISIBLE);
        } else {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new SignInFragment())
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void signOut() {
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
    }

    private void setNewName() {
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
