package com.arnav.pocdoc.SimplyRelief;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arnav.pocdoc.Authentication.Login;
import com.arnav.pocdoc.Diary;
import com.arnav.pocdoc.MainMenu;
import com.arnav.pocdoc.MedicalId;
import com.arnav.pocdoc.R;
import com.arnav.pocdoc.Reminder;
import com.arnav.pocdoc.SimplyRelief.adapter.SimplyReliefListAdapter;
import com.arnav.pocdoc.SimplyRelief.models.DataReliefItem;
import com.arnav.pocdoc.SimplyRelief.models.SimplyReliefResponse;
import com.arnav.pocdoc.base.BaseApplication;
import com.arnav.pocdoc.maps.MapsActivity;
import com.arnav.pocdoc.retrofit.ApiClient;
import com.arnav.pocdoc.retrofit.ApiInterface;
import com.arnav.pocdoc.retrofit.NetworkRequest;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class SimplyReliefSearch extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final int currentPage = 1;
    private final Handler handler = new Handler();
    protected ApiInterface apiService;
    protected CompositeSubscription compositeSubscription;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    AppCompatEditText editSearch;
    ImageView imgClear;
    RecyclerView recyclerView;
    RecyclerView recyclerViewSearch;
    ArrayList<DataReliefItem> searchList = new ArrayList<>();
    SimplyReliefListAdapter searchAdapter;
    ArrayList<DataReliefItem> allSimpleReliefList = new ArrayList<>();
    SimplyReliefListAdapter adapter;
    Subscription searchSubscription;
    private boolean isLoading = true;
    private boolean isLoadMore = true;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simply_relief_search);

        apiService = ApiClient.getClient(this).create(ApiInterface.class);
        compositeSubscription = new CompositeSubscription();

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Simply Relief");
        toolbar.setTitle("Simply Relief");

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setCheckedItem(R.id.nav_home);

        editSearch = findViewById(R.id.edtSearch);
        imgClear = findViewById(R.id.imgClear);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerViewSearch = findViewById(R.id.recyclerViewSearch);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(this));

        imgClear.setOnClickListener(v -> editSearch.setText(""));
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    imgClear.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    recyclerViewSearch.setVisibility(View.VISIBLE);
                } else {
                    imgClear.setVisibility(View.GONE);
                    recyclerViewSearch.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }

                handler.removeCallbacks(runnable);
                runnable = () -> onDelayerQueryTextChange(s);
                handler.postDelayed(runnable, 400);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading && isLoadMore) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == allSimpleReliefList.size() - 1) {
                        getData();
                    }
                }
            }
        });
        adapter = new SimplyReliefListAdapter(this, allSimpleReliefList, detail -> {
            startActivity(SimplyReliefDetail.getIntent(this, detail));
        });
        recyclerView.setAdapter(adapter);

        searchAdapter = new SimplyReliefListAdapter(this, searchList, detail -> {
            startActivity(SimplyReliefDetail.getIntent(this, detail));
        });
        recyclerViewSearch.setAdapter(searchAdapter);

        addLoadingItem();
        getData();
    }

    private void getData() {
        isLoading = true;

        Subscription subscription = NetworkRequest.performAsyncRequest(apiService.getSimplyReliefData("", String.valueOf(currentPage))
                , response -> {

                    if (response.isSuccessful()) {
                        SimplyReliefResponse list = response.body();
                        boolean isCheckForData = false;

                        if (list.getData() != null && list.getData() != null && list.getData().size() > 0) {
                            isCheckForData = true;

                            allSimpleReliefList.remove(allSimpleReliefList.size() - 1);
                            allSimpleReliefList.addAll(list.getData());
                            allSimpleReliefList.add(null);

                            adapter.notifyDataSetChanged();

                        }

                        if (isCheckForData) {
//                            if (currentPage < 0) {
//                                currentPage++;
//                                isLoading = false;
//                                isLoadMore = true;
//                            } else {
                            isLoading = false;
                            isLoadMore = false;
//                            }
                        } else {
                            isLoading = false;
                            isLoadMore = false;
                        }
                    } else {
                        isLoading = false;
                        isLoadMore = false;
//                        showNetworkErrorDialog();
                    }

                    if (!isLoadMore) {
                        removeLoadingItem();
                    }
                }
                , throwable -> {

                    removeLoadingItem();
                    adapter.notifyDataSetChanged();

                    isLoadMore = false;
                    isLoading = false;

                    throwable.printStackTrace();
//                    showNetworkErrorDialog();
                });

        compositeSubscription.add(subscription);
    }

    private void addLoadingItem() {
        new Handler(Looper.myLooper()).post(() -> {
            allSimpleReliefList.add(null);
            adapter.notifyItemInserted(allSimpleReliefList.size() - 1);
            recyclerView.scrollToPosition(allSimpleReliefList.size() - 1);
        });
    }

    private void removeLoadingItem() {
        if (allSimpleReliefList.size() > 0) {
            allSimpleReliefList.remove(allSimpleReliefList.size() - 1);
            adapter.notifyDataSetChanged();
        }
    }

    private void onDelayerQueryTextChange(CharSequence s) {
        searchList.clear();
        searchAdapter.notifyDataSetChanged();

        if (searchSubscription != null && !searchSubscription.isUnsubscribed()) {
            searchSubscription.unsubscribe();
        }

        if (s.toString().trim().isEmpty()) {
            return;
        }

        searchSubscription = NetworkRequest.performAsyncRequest(apiService.getSimplyReliefData(s.toString(), "1")
                , response -> {

                    if (response.isSuccessful()) {
                        SimplyReliefResponse list = response.body();
                        if (list.getData() != null && list.getData() != null && list.getData().size() > 0) {
                            searchList.addAll(list.getData());
                            searchAdapter.notifyDataSetChanged();
                        }
                    }
                }

                , throwable -> {
//                    showProgress(false);
//                    showSimpleDialog(getString(R.string.NETWORK_ISSUE_MSG));
                    throwable.printStackTrace();
                });

        compositeSubscription.add(searchSubscription);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_home:
                startActivity(new Intent(getApplicationContext(), MainMenu.class));
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
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Download Erinle Today!");
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

    @Override
    protected void onDestroy() {
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
        compositeSubscription.unsubscribe();
        super.onDestroy();
    }
}
