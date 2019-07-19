package com.codebosses.flicks.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codebosses.flicks.FlicksApplication;
import com.codebosses.flicks.R;
import com.codebosses.flicks.common.Constants;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.pojo.user.UserModel;
import com.codebosses.flicks.utils.FontUtils;
import com.codebosses.flicks.utils.ValidUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jaeger.library.StatusBarUtil;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1004;

    //    Android fields....
    @BindView(R.id.buttonGoogleLogIn)
    Button buttonGoogleLogIn;
    @BindView(R.id.buttonFacebookLogIn)
    Button buttonFacebookLogIn;
    @BindView(R.id.buttonTwitterLogIn)
    Button buttonTwitterLogIn;
    private SweetAlertDialog sweetAlertDialog;
    //    Font fields....

    //    TODO: Google sign in fields....
    private GoogleSignInClient mGoogleSignInClient;

    //    Firebase fields....
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        ValidUtils.transparentStatusAndNavigation(this);

//        Setting custom font....

//        Firebase fields initialization....
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#03a9f4"));
        sweetAlertDialog.setTitleText(getResources().getString(R.string.trying_to_login));
        sweetAlertDialog.setCancelable(false);

    }

    @OnClick(R.id.buttonGoogleLogIn)
    public void onGoogleClick(View view) {
        if (!ValidUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, getResources().getString(R.string.internet_problem), Toast.LENGTH_SHORT).show();
            return;
        }
        googleSignIn();
    }

    @OnClick(R.id.buttonFacebookLogIn)
    public void onFacebookClick(View view) {

    }

    @OnClick(R.id.buttonTwitterLogIn)
    public void onTwitterClick(View view) {

    }

    //    TODO: Method to start activity for google sign in....
    private void googleSignIn() {
        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    //    TODO: Method to sign in with google using firebase....
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        sweetAlertDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                            if (currentUser != null) {
                                final String email, name, userId;
                                String originalPieceOfUrl = "s96-c/photo.jpg";
                                String newPieceOfUrlToAdd = "s400-c/photo.jpg";
                                String profileImage, profileImageThumbnail;
                                userId = currentUser.getUid();
                                email = currentUser.getEmail();
                                name = currentUser.getDisplayName();
                                String phoneNumber = "";
                                profileImage = "";
                                profileImageThumbnail = "";
                                if (currentUser.getPhoneNumber() != null) {
                                    phoneNumber = currentUser.getPhoneNumber();
                                }
                                if (currentUser.getPhotoUrl() != null) {
                                    profileImage = currentUser.getPhotoUrl().toString();
                                    profileImage = profileImage.replace(originalPieceOfUrl, newPieceOfUrlToAdd);
                                    profileImageThumbnail = currentUser.getPhotoUrl().toString();
                                }
                                checkUserAlreadyExist(userId, email, name, phoneNumber, profileImage, profileImageThumbnail, Constants.GMAIL);
                            }
                        } else {
                            sweetAlertDialog.dismiss();
                            if (task.getException() != null) {
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                                L.showSnackBar(resources.getString(R.string.google_login_error), relativeLayoutWelcome);
//                                L.showSnackBar(EmailPasswordLogInActivity.this, task.getException().getMessage().toString(), scrollViewLogIn);
                            }
//                            sweetAlertDialog.dismiss();
                        }
//                        imageButtonFacebook.setEnabled(true);
                    }
                });
    }

    private void checkUserAlreadyExist(String userId, String email, String name, String phoneNumber, String profileImage, String profileImageThumbnail, String loginType) {
        firebaseFirestore.collection("User")
                .document(userId)
                .get()
                .addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            UserModel userModel = documentSnapshot.toObject(UserModel.class);
                            if (userModel != null) {
                                saveUserDataToPreference(userModel);
                            }
                        } else {
                            UserModel userModel = new UserModel(userId, name, email, phoneNumber, "", profileImage, profileImageThumbnail, loginType);
                            createUserInFirestore(userModel);
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                sweetAlertDialog.dismiss();
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createUserInFirestore(UserModel userModel) {
        firebaseFirestore.collection("Users")
                .document(userModel.getUserId())
                .set(userModel)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        saveUserDataToPreference(userModel);
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                sweetAlertDialog.dismiss();
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserDataToPreference(UserModel userModel) {
        sweetAlertDialog.dismiss();
        FlicksApplication.putStringValue(EndpointKeys.USER_NAME, userModel.getUserName());
        FlicksApplication.putStringValue(EndpointKeys.USER_EMAIl, userModel.getUserEmail());
        FlicksApplication.putStringValue(EndpointKeys.USER_PHONE, userModel.getUserPhone());
        FlicksApplication.putStringValue(EndpointKeys.USER_IMAGE, userModel.getUserProfileImage());
        FlicksApplication.putStringValue(EndpointKeys.USER_IMAGE_THUMB, userModel.getUserProfileImageThumbnail());
        FlicksApplication.putStringValue(EndpointKeys.USER_ACCOUNT_TYPE, userModel.getUserAccountType());
        startMainActivity();
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}