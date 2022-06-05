package com.arnav.pocdoc.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.arnav.pocdoc.BaseActivity;
import com.arnav.pocdoc.MainMenu;
import com.arnav.pocdoc.R;
import com.arnav.pocdoc.base.BaseApplication;
import com.arnav.pocdoc.data.register.ResponseRegistration;
import com.arnav.pocdoc.retrofit.ApiClient;
import com.arnav.pocdoc.retrofit.ApiInterface;
import com.arnav.pocdoc.retrofit.NetworkRequest;
import com.arnav.pocdoc.utils.Constants;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


public class Login extends BaseActivity {
    private static final String TAG = "EmailPassword";
    private static final int RC_SIGN_IN = 9001;
    protected ApiInterface apiService;
    protected CompositeSubscription compositeSubscription;
    Button register, signIn;
    MaterialProgressBar bar;
    EditText email, password;
    SignInButton google;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        apiService = ApiClient.getClient(this).create(ApiInterface.class);
        compositeSubscription = new CompositeSubscription();

        register = findViewById(R.id.noaccount);
        signIn = findViewById(R.id.yes);

        bar = findViewById(R.id.bar);

        email = findViewById(R.id.email_login);
        password = findViewById(R.id.password_login);
        google = findViewById(R.id.sign_in_button);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.auth_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(view -> {
            bar.setVisibility(View.VISIBLE);
            Intent intent = new Intent(getApplicationContext(), Registration.class);
            startActivity(intent);
            bar.setVisibility(View.GONE);
        });
        signIn.setOnClickListener(view -> signIn());
        google.setOnClickListener(view -> {
            bar.setVisibility(View.VISIBLE);
            signInWithGoogle();
        });
    }

    private void signIn() {
        String emailInput = email.getText().toString();
        String passwordInput = password.getText().toString();
        Log.d(TAG, "signIn:" + emailInput);
        if (!validateForm()) {
            return;
        }

        bar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(emailInput, passwordInput)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        BaseApplication.preferences.putString(Constants.uID, user != null ? user.getUid() : "");
                        submitLogin(1, user);
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toasty.error(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT, true).show();
                    }
                    bar.setVisibility(View.GONE);
                });
    }

    private void firebaseAuthWithGoogle() {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        BaseApplication.preferences.putString(Constants.uID, user != null ? user.getUid() : "");
                        submitLogin(2, user);
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toasty.error(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT, true).show();
                    }
                });
    }

    private void submitLogin(int from, FirebaseUser user) {
        HashMap<String, String> params = new HashMap<>();
        if (from == 1) {
            params.put("email", email.getText().toString().trim());
            params.put("password", password.getText().toString().trim());
        } else {
            params.put("email", account.getEmail());
            params.put("password", account.getId());
        }
        Subscription subscription = NetworkRequest.performAsyncRequest(apiService.login(params)
                , response -> {
                    hideProgress();
                    if (response.isSuccessful()) {
                        ResponseRegistration result = response.body();
                        BaseApplication.preferences.storeUserDetails(result);

                        Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                        intent.putExtra("user", user);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        Toasty.success(getApplicationContext(), "Logged In Successfully", Toast.LENGTH_SHORT, true).show();
                        finish();
                    } else {
                        if (from == 1) {
                            Toasty.error(Login.this, getResources().getString(R.string.details_wrong), Toast.LENGTH_SHORT, true).show();
                        } else {
                            Toasty.error(Login.this, getResources().getString(R.string.you_dont_text), Toast.LENGTH_SHORT, true).show();
                        }
                    }
                }
                , throwable -> {
                    hideProgress();
                    throwable.printStackTrace();
                });
        compositeSubscription.add(subscription);
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle();
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
//                Toasty.error(getApplicationContext(), "Authentication Failed.", Toast.LENGTH_SHORT, true).show();
                Toasty.error(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT, true).show();
            }

            bar.setVisibility(View.GONE);
        }
    }

    public void forgetPassword(View view) {
        String emailInput = email.getText().toString();
        if (TextUtils.isEmpty(emailInput)) {
            email.setError("Required.");
            return;
        }

        mAuth.sendPasswordResetEmail(emailInput)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Email sent.");
                        Toasty.info(getApplicationContext(), "Password Reset Sent", Toast.LENGTH_LONG, true).show();
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;
        String emailInput = email.getText().toString();
        if (TextUtils.isEmpty(emailInput)) {
            email.setError("Required.");
            valid = false;
        } else {
            email.setError(null);
        }

        String passwordInput = password.getText().toString();
        if (passwordInput.isEmpty()) {
            password.setError("Required.");
            valid = false;
        } else {
            password.setError(null);
        }
        return valid;
    }
}
