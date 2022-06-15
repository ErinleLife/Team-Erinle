package com.arnav.pocdoc.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arnav.pocdoc.BaseActivity;
import com.arnav.pocdoc.Onboarding.Introduction;
import com.arnav.pocdoc.R;
import com.arnav.pocdoc.base.BaseApplication;
import com.arnav.pocdoc.data.register.ResponseRegistration;
import com.arnav.pocdoc.retrofit.ApiClient;
import com.arnav.pocdoc.retrofit.ApiInterface;
import com.arnav.pocdoc.retrofit.NetworkRequest;
import com.arnav.pocdoc.utils.Constants;
import com.arnav.pocdoc.utils.Utils;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

import es.dmoral.toasty.Toasty;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class Registration extends BaseActivity {
    private static final String TAG = "EmailPassword";
    private static final int RC_SIGN_IN = 9001;
    protected ApiInterface apiService;
    protected CompositeSubscription compositeSubscription;
    Button login, register;
    MaterialProgressBar bar;
    EditText email, password, firstName, lastName, confirmPassword;
    SignInButton google;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount account;
    private CheckBox tvAgreeText;
    private TextView tvPP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        apiService = ApiClient.getClient(this).create(ApiInterface.class);
        compositeSubscription = new CompositeSubscription();

        login = findViewById(R.id.haveaccount);
        register = findViewById(R.id.register);

        bar = findViewById(R.id.regbar);

        email = findViewById(R.id.email_reg);
        password = findViewById(R.id.password_reg);
        firstName = findViewById(R.id.firstname);
        lastName = findViewById(R.id.lastname);
        confirmPassword = findViewById(R.id.confirm);
        google = findViewById(R.id.sign_up_button);
        tvAgreeText = findViewById(R.id.tvAgreeText);
        tvPP = findViewById(R.id.tvPP);
        tvPP.setOnClickListener(v -> {
            startActivity(new Intent(Registration.this, PrivacyPolicyActivity.class));
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.auth_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(view -> createAccount());

        google.setOnClickListener(view -> {
            bar.setVisibility(View.VISIBLE);
            signUpWithGoogle();
        });

        login.setOnClickListener(view -> {
            bar.setVisibility(View.VISIBLE);
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            bar.setVisibility(View.GONE);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            bar.setVisibility(View.GONE);
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle();
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Toasty.error(Registration.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT, true).show();
            }
        }
    }

    private void firebaseAuthWithGoogle() {
        showProgress();
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        BaseApplication.preferences.putString(Constants.uID, user != null ? user.getUid() : "");
                        initializeMedicalID(user.getEmail());
                        submitRegistration(2, user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        hideProgress();
                        Toasty.error(getApplicationContext(), "Sign Up Failed.", Toast.LENGTH_SHORT, true).show();
                    }
                });
    }

    private void submitRegistration(int from, FirebaseUser user) {
        HashMap<String, String> params = new HashMap<>();
        if (from == 1) {
            params.put("name", firstName.getText().toString().trim() + " " + lastName.getText().toString().trim());
            params.put("email", email.getText().toString().trim());
            params.put("password", password.getText().toString().trim());
        } else {
            params.put("name", account.getDisplayName());
            params.put("email", account.getEmail());
            params.put("password", account.getId());
        }
        Subscription subscription = NetworkRequest.performAsyncRequest(apiService.singUp(params)
                , response -> {
                    hideProgress();
                    if (response.isSuccessful()) {
                        ResponseRegistration result = response.body();
                        BaseApplication.preferences.storeUserDetails(result);

                        Toasty.success(getApplicationContext(), "You Registered Successfully", Toast.LENGTH_SHORT, true).show();
                        Intent intent = new Intent(getApplicationContext(), Introduction.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                        finish();
                    } else {
                        Toasty.error(Registration.this, getResources().getString(R.string.details_wrong), Toast.LENGTH_SHORT, true).show();
                    }
                }
                , throwable -> {
                    hideProgress();
                    throwable.printStackTrace();
                });
        compositeSubscription.add(subscription);
    }

    private void initializeMedicalID(String email) {
        // Initialize values for Medical ID
        String empty_text = getString(R.string.empty_text);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("medical_id").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));

        Random generator = new Random();
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            id.append(generator.nextInt(10));
        }

        if (!firstName.getText().toString().trim().isEmpty()) {
            reference.child("firstName").setValue(firstName.getText().toString().trim());
            reference.child("lastName").setValue(lastName.getText().toString().trim());
        } else {
            if (mAuth.getCurrentUser().getDisplayName() != null) {
                reference.child("firstName").setValue(mAuth.getCurrentUser().getDisplayName());
                reference.child("lastName").setValue("");
            }
        }

        reference.child("patientID").setValue(id.toString());
        reference.child("location").setValue(empty_text);
        reference.child("email").setValue(email);
        reference.child("insurance").setValue(empty_text);

        reference.child("age").setValue(empty_text);
        reference.child("gender").setValue("male");
        reference.child("height").setValue(empty_text);
        reference.child("weight").setValue(empty_text);
        reference.child("bloodType").setValue(empty_text);
        reference.child("race").setValue(empty_text);

        reference.child("conditions").setValue(empty_text);
        reference.child("familyHistory").setValue(empty_text);
        reference.child("bloodPressure").setValue(empty_text);
        reference.child("sugar").setValue(empty_text);
        reference.child("allergies").setValue(empty_text);
        reference.child("habits").setValue(empty_text);
        reference.child("otc").setValue(empty_text);
        reference.child("rx").setValue(empty_text);
    }

    private void createAccount() {
        String emailInput = email.getText().toString();
        String passwordInput = password.getText().toString();
        Log.d(TAG, "createAccount:" + emailInput);
        if (!validateForm()) {
            return;
        }

        showProgress();
        mAuth.createUserWithEmailAndPassword(emailInput, passwordInput)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        BaseApplication.preferences.putString(Constants.uID, user != null ? user.getUid() : "");
                        initializeMedicalID(emailInput);
                        submitRegistration(1, user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        hideProgress();
//                        Toasty.error(Registration.this, "Authentication failed.", Toast.LENGTH_SHORT, true).show();
                        Toasty.error(Registration.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT, true).show();
                    }
                });
    }

    private void signUpWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
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

        String confirmInput = confirmPassword.getText().toString();
        if (confirmInput.isEmpty()) {
            confirmPassword.setError("Required.");
            valid = false;
        } else {
            confirmPassword.setError(null);
        }

        if (!passwordInput.equals(confirmInput)) {
            confirmPassword.setError("Password Does Not Match");
            valid = false;
        } else {
            confirmPassword.setError(null);
        }

        if (!tvAgreeText.isChecked()) {
            valid = false;
            Utils.showSnackBar("Please read and accept privacy policy", tvAgreeText);
        }

        return valid;
    }
}