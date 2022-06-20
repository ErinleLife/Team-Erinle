package com.arnav.pocdoc.otc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.arnav.pocdoc.BaseActivity;
import com.arnav.pocdoc.R;
import com.arnav.pocdoc.SimplyRelief.models.DataOTCItem;
import com.arnav.pocdoc.SimplyRelief.models.OtcResponse;
import com.arnav.pocdoc.databinding.ActivityOtcBinding;
import com.arnav.pocdoc.implementor.RecyclerViewItemClickListener;
import com.arnav.pocdoc.otc.Adapter.OTCAdapter;
import com.arnav.pocdoc.retrofit.ApiClient;
import com.arnav.pocdoc.retrofit.ApiInterface;
import com.arnav.pocdoc.retrofit.NetworkRequest;
import com.arnav.pocdoc.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class OTCAndNaturalDrugsActivity extends BaseActivity implements RecyclerViewItemClickListener {

    public static final List<DataOTCItem> listAll = new ArrayList<>();
    private final List<DataOTCItem> list = new ArrayList<>();
    protected ApiInterface apiService;
    protected CompositeSubscription compositeSubscription;
    private ActivityOtcBinding binding;
    private Boolean isOTC = true;

    private OTCAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otc);
        binding.setActivity(this);

        apiService = ApiClient.getClient(this).create(ApiInterface.class);
        compositeSubscription = new CompositeSubscription();

        getIntentData();

        setUpHeaderView();
        getSymptomsData();

        binding.setLifecycleOwner(this);
    }

    private void getIntentData() {
        isOTC = getIntent().getExtras().getBoolean(Constants.isOTC);
    }

    private void getSymptomsData() {
        showProgress();

        if (isOTC) {

            Subscription subscription = NetworkRequest.performAsyncRequest(apiService.getOct()
                    , response -> {
                        hideProgress();
                        if (response.isSuccessful()) {

                            OtcResponse result = response.body();
                            listAll.clear();
                            listAll.addAll(result.getData());
                            list.addAll(result.getData());
                            adapter.notifyDataSetChanged();

                        }
                    }
                    , throwable -> {
                        hideProgress();
                        throwable.printStackTrace();
                    });
            compositeSubscription.add(subscription);
        }else {
            Subscription subscription = NetworkRequest.performAsyncRequest(apiService.getNaturalMedicine()
                    , response -> {
                        hideProgress();
                        if (response.isSuccessful()) {

                            OtcResponse result = response.body();
                            listAll.clear();
                            listAll.addAll(result.getData());
                            list.addAll(result.getData());
                            adapter.notifyDataSetChanged();

                        }
                    }
                    , throwable -> {
                        hideProgress();
                        throwable.printStackTrace();
                    });
            compositeSubscription.add(subscription);
        }
    }

    private void setUpHeaderView() {

        if (isOTC) {
            binding.header.tvTitle.setText(getResources().getString(R.string.otc_and_natural_drugs));
        } else {
            binding.header.tvTitle.setText(getResources().getString(R.string.natural_alternative_medicine_without_space));
        }

        adapter = new OTCAdapter(list, getApplicationContext());
        binding.rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnRecyclerViewItemClickListener(this);

//        binding.etBreed.addTextChangedListener(onTextWatcherListener);
    }

    public void onViewClick(View view) {
//        if (view == binding.btnNext) {
//        }
    }

    @Override
    public void onItemClick(int position, int flag, View view) {
        if (flag == 0) {
            Intent intent = new Intent(getApplicationContext(), OTCDetailsActivity.class);
            intent.putExtra(Constants.position, position);
            startActivity(intent);
        }
    }
}