package com.codebosses.flicks.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.codebosses.flicks.FlicksApplication;
import com.codebosses.flicks.R;
import com.codebosses.flicks.common.Constants;
import com.codebosses.flicks.database.DatabaseClient;
import com.codebosses.flicks.database.entities.MovieEntity;
import com.codebosses.flicks.database.entities.TvShowEntity;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.endpoints.EndpointUrl;
import com.codebosses.flicks.pojo.user.UserModel;
import com.codebosses.flicks.utils.FontUtils;
import com.codebosses.flicks.utils.ValidUtils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
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
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.textViewLogoLogIn)
    TextView textViewLogo;
    @BindView(R.id.textViewLoginDescriptionLogIn)
    TextView textViewDescription;
    @BindView(R.id.textViewTermsServices)
    TextView textViewTerms;
    private SweetAlertDialog sweetAlertDialog;

    //    Font fields....
    private FontUtils fontUtils;

    //    TODO: Google sign in fields....
    private GoogleSignInClient mGoogleSignInClient;

    //    TODO: Facebook sign in fields....
    private CallbackManager facebookManager;

    //    Firebase fields....
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private DatabaseClient databaseClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        ValidUtils.transparentStatusAndNavigation(this);

//        Setting custom font....
        fontUtils = FontUtils.getFontUtils(this);
        fontUtils.setTextViewBoldFont(textViewLogo);
        fontUtils.setTextViewRegularFont(textViewDescription);
        fontUtils.setButtonRegularFont(buttonGoogleLogIn);
        fontUtils.setButtonRegularFont(buttonFacebookLogIn);
        fontUtils.setButtonRegularFont(buttonTwitterLogIn);
        fontUtils.setTextViewRegularFont(textViewTerms);

        databaseClient = DatabaseClient.getDatabaseClient(this);

//        Firebase fields initialization....
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        facebookManager = CallbackManager.Factory.create();

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
        if (!ValidUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, getResources().getString(R.string.internet_problem), Toast.LENGTH_SHORT).show();
            return;
        }
        registerCallback();
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
        }else{
            facebookManager.onActivityResult(requestCode, resultCode, data);
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
                                saveFavoriteToDatabase(userModel);
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
                        saveFavoriteToDatabase(userModel);
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                sweetAlertDialog.dismiss();
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveFavoriteToDatabase(UserModel userModel) {
        firebaseFirestore.collection("Favorites")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("Movies")
                .get()
                .addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null && queryDocumentSnapshots.size() > 0) {
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                MovieEntity movieEntity = snapshot.toObject(MovieEntity.class);
                                new AddToFavoriteListTask().execute(movieEntity);
                            }
                        }
                        firebaseFirestore.collection("Favorites")
                                .document(firebaseAuth.getCurrentUser().getUid())
                                .collection("TvShows")
                                .get()
                                .addOnSuccessListener(LoginActivity.this, new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        sweetAlertDialog.dismiss();
                                        if (queryDocumentSnapshots != null && queryDocumentSnapshots.size() > 0) {
                                            for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                                TvShowEntity tvShowEntity = snapshot.toObject(TvShowEntity.class);
                                                new AddToFavoriteTvShowTask().execute(tvShowEntity);
                                            }
                                        }
                                        saveUserDataToPreference(userModel);
                                    }
                                }).addOnFailureListener(LoginActivity.this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                sweetAlertDialog.dismiss();
                                saveUserDataToPreference(userModel);
                            }
                        });
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                sweetAlertDialog.dismiss();
                saveUserDataToPreference(userModel);
            }
        });
    }

    //    TODO: Method to trigger facebook sign in....
    private void registerCallback() {
        LoginManager loginManager = LoginManager.getInstance();
        List<String> permissionList = new ArrayList<>();
        permissionList.add(EndpointKeys.EMAIL);
        permissionList.add(EndpointKeys.PUBLIC_PROFILE);
        loginManager.logInWithReadPermissions(this, permissionList);
        loginManager.registerCallback(facebookManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                if (error != null) {
                    Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Facebook Error", error.getMessage());
                }
            }
        });
    }

    //    TODO: Method to handle facebook access token using firebase....
    private void handleFacebookAccessToken(AccessToken accessToken) {
        sweetAlertDialog.show();
        final AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                            String facebookUserId = "";
                            if (currentUser != null) {
                                String email;
                                final String name;
                                final String userId;
                                String profileImage = "";
                                String profileImageThumbnail = "";
                                String phoneNumber = "";

                                userId = currentUser.getUid();
                                email = currentUser.getEmail();
                                name = currentUser.getDisplayName();
                                if(email == null){
                                    email = "";
                                }
                                if(currentUser.getPhoneNumber() != null){
                                    phoneNumber = currentUser.getPhoneNumber();
                                }

                                if (currentUser.getPhotoUrl() != null) {
                                    profileImageThumbnail = currentUser.getPhotoUrl().toString();
                                    for (UserInfo profile : currentUser.getProviderData()) {
                                        if (FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                                            facebookUserId = profile.getUid();
                                            profileImage = EndpointUrl.FACEBOOK_GRAPH_BASE_URL + facebookUserId + EndpointUrl.FACEBOOK_GRAPH_QUALITY_URL;
                                        }
                                    }
                                }
                                checkUserAlreadyExist(userId, email, name,phoneNumber , profileImage, profileImageThumbnail, Constants.FACEBOOK);
                            }
                        } else {
                            if (task.getException() != null) {
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                Log.e("Facebook Error", task.getException().getMessage());
                            }
                            sweetAlertDialog.dismiss();
                        }
                    }
                });
    }

    private void saveUserDataToPreference(UserModel userModel) {
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

    class AddToFavoriteListTask extends AsyncTask<MovieEntity, Void, Void> {

        @Override
        protected Void doInBackground(MovieEntity... movieEntities) {
            databaseClient.getFlicksDatabase().getFlicksDao().insertMovie(movieEntities[0]);
            return null;
        }

    }

    class AddToFavoriteTvShowTask extends AsyncTask<TvShowEntity, Void, Void> {

        @Override
        protected Void doInBackground(TvShowEntity... tvShowEntities) {
            databaseClient.getFlicksDatabase().getFlicksDao().insertTvShow(tvShowEntities[0]);
            return null;
        }
    }

    @OnClick(R.id.imageViewBackLogIn)
    public void onBackClick(View view) {
        this.finish();
    }

}