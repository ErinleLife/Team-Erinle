package com.arnav.pocdoc;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.arnav.pocdoc.Authentication.Login;
import com.arnav.pocdoc.FoodAllergy.FoodImageInput;
import com.arnav.pocdoc.SpeakToDoc.SpeakToDoc;
import com.arnav.pocdoc.activateLocator.ActivateLocatorActivity;
import com.arnav.pocdoc.base.BaseApplication;
import com.arnav.pocdoc.hospitalLocator.HospitalLocatorActivity;
import com.arnav.pocdoc.maps.MapsActivity;
import com.arnav.pocdoc.otc.OTCAndNaturalDrugsActivity;
import com.arnav.pocdoc.symptomchecker.AddSymptomActivity;
import com.arnav.pocdoc.symptomchecker.SymptomCheckerActivity;
import com.arnav.pocdoc.utils.Constants;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import es.dmoral.toasty.Toasty;


public class MainMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    CardView emergency;
    Button reminder;

    RelativeLayout rel_home, rel_speak_doc, rel_rx_management, rel_profile;
    private TextView txtWelcome;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.toolbar_color));

        FirebaseDatabase.getInstance().getReference().child("medical_id").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d("medical_id", "onDataChange: " + snapshot.toString());
                try {

                    if (snapshot.child("age").exists() && !snapshot.child("age").getValue().toString().equals(getString(R.string.empty_text))) {
                        if (snapshot.child("age").getValue().toString().contains("/")) {
                            AddSymptomActivity.DOB = snapshot.child("age").getValue().toString();
                        }
                    }

                    if (snapshot.child("gender").exists() && !snapshot.child("gender").getValue().toString().equals(getString(R.string.empty_text))) {
                        AddSymptomActivity.Gender = snapshot.child("gender").getValue().toString();
                    }
                    if (snapshot.child("gender").getValue().toString().equals(getString(R.string.empty_text))) {
                        AddSymptomActivity.Gender = "";
                    }


                } catch (NullPointerException e) {
                    e.printStackTrace();
                }


//                medication_names.setText(snapshot.child("rx").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
//        emergency = findViewById(R.id.emergency_call);
        txtWelcome = findViewById(R.id.txtWelcome);
//        med_diary = findViewById(R.id.go_diary);
//        allergy = findViewById(R.id.go_symptom);
//        go_simply_relief = findViewById(R.id.go_simply_relief);
//        go_symptomchecker = findViewById(R.id.go_symptomchecker);


        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(R.drawable.menu_icon);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.menu_icon));
//        drawerLayout.setHomeAsUpIndicator(R.drawable.ic_your_drawer_icon);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_icon);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setCheckedItem(R.id.nav_home);

//        emergency.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showEmergencyDialog();
//            }
//        });

        rel_speak_doc = findViewById(R.id.rel_speak_doc);
        rel_speak_doc.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), SpeakToDoc.class));
        });

        rel_rx_management = findViewById(R.id.rel_rx_management);
        rel_rx_management.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Reminder.class);
            startActivity(intent);
        });

        rel_profile = findViewById(R.id.rel_profile);
        rel_profile.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), MedicalId.class));
        });

//        go_simply_relief.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), SimplyReliefSearch.class));
//            }
//        });

//        go_symptomchecker.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), SymptomCheckerActivity.class));
//            }
//        });

//        med_diary.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), OTCAndNaturalDrugsActivity.class));
//            }
//        });

//        locator.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), HospitalLocatorActivity.class));
//            }
//        });

//        go_activate_locator.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), ActivateLocatorActivity.class));
//            }
//        });

//        allergy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), FoodImageInput.class));
//            }
//        });

//        go_remind.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), Reminder.class);
//                startActivity(intent);
//            }
//        });

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e("FIREBASEUSER-->", "" + firebaseUser + " Referance-->" + reference + " snapshot->" + snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.e("FIREBASEUSER-->", "" + firebaseUser + " Referance-->" + reference);

        FirebaseDatabase.getInstance().getReference().child("medical_id").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String firstName = snapshot.child("firstName").getValue().toString();
                    txtWelcome.setText("Welcome, " + firstName);

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_home:
                break;
            case R.id.nav_signout:
                BaseApplication.preferences.clear();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
                Toasty.success(getApplicationContext(), "Logged Out!", Toast.LENGTH_LONG, true).show();
                //Toast.makeText(getApplicationContext(), "Signed Out",Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_diary:
                Intent diary = new Intent(getApplicationContext(), Diary.class);
                startActivity(diary);
                break;
            case R.id.nav_hospital:
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                break;
            case R.id.nav_symptom:
                startActivity(new Intent(getApplicationContext(), FoodImageInput.class));
                break;
            case R.id.nav_remind:
                startActivity(new Intent(getApplicationContext(), Reminder.class));
                break;
            case R.id.nav_id:
                startActivity(new Intent(getApplicationContext(), MedicalId.class));
                break;
            case R.id.nav_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Download ERINLE Today!");
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    public void showEmergencyDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.dialog_emg_title)
                .setMessage("Are you sure you want to make a call to emergency services?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Call", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // TODO replace with 911
                        String dial = "tel:911";
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(dial));
                        startActivity(intent);
                        dialogInterface.dismiss();
                    }
                })
                .show();

    }

    public void symptom_checker_btn(View view) {
        startActivity(new Intent(getApplicationContext(), SymptomCheckerActivity.class));
    }

    public void otc_natural_btn(View view) {
        Intent intent = new Intent(getApplicationContext(), OTCAndNaturalDrugsActivity.class);
        intent.putExtra(Constants.isOTC,true);
        startActivity(intent);
    }

    public void fitnes_center_btn(View view) {
        startActivity(new Intent(getApplicationContext(), FoodImageInput.class));
    }

    public void diet_nutiontion_btn(View view) {
        Intent intent = new Intent(getApplicationContext(), OTCAndNaturalDrugsActivity.class);
        intent.putExtra(Constants.isOTC,false);
        startActivity(intent);
    }

    public void pharmacy_locator_btn(View view) {
        startActivity(new Intent(getApplicationContext(), HospitalLocatorActivity.class));
    }

    public void activate_locator_btn(View view) {
        startActivity(new Intent(getApplicationContext(), ActivateLocatorActivity.class));
    }
}