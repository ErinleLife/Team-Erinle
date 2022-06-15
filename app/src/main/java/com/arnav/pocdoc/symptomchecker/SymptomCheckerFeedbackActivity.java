package com.arnav.pocdoc.symptomchecker;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.arnav.pocdoc.BaseActivity;
import com.arnav.pocdoc.MainMenu;
import com.arnav.pocdoc.R;
import com.arnav.pocdoc.SimplyRelief.models.ResponseCommon;
import com.arnav.pocdoc.base.BaseApplication;
import com.arnav.pocdoc.databinding.ActivitySymtomCheckerFeedbackBinding;
import com.arnav.pocdoc.retrofit.ApiClient;
import com.arnav.pocdoc.retrofit.ApiInterface;
import com.arnav.pocdoc.retrofit.NetworkRequest;

import java.util.HashMap;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class SymptomCheckerFeedbackActivity extends BaseActivity {

    protected ApiInterface apiService;
    protected CompositeSubscription compositeSubscription;
    private ActivitySymtomCheckerFeedbackBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_symtom_checker_feedback);
        binding.setActivity(this);

        apiService = ApiClient.getClient(this).create(ApiInterface.class);
        compositeSubscription = new CompositeSubscription();

        binding.setLifecycleOwner(this);
    }

    public void onViewClick(View view) {
        if (view == binding.header.tvBack) {
            onBackPressed();
        } else if (view == binding.btnSubmit) {
            if (binding.rBar.getRating() == 0) {
                showSMessage(getResources().getString(R.string.select_rating_error));
            } else if (binding.etTitle.getText().toString().trim().isEmpty()) {
                showSMessage(getResources().getString(R.string.enter_title_err));
            } else if (binding.etComment.getText().toString().trim().isEmpty()) {
                showSMessage(getResources().getString(R.string.enter_review_err));
            } else {
                submitRating();
            }
        }
    }

    private void submitRating() {
        showProgress();
        HashMap<String, String> params = new HashMap<>();
        params.put("rating", String.valueOf(binding.rBar.getRating()));
        params.put("title", binding.etTitle.getText().toString().trim());
        params.put("review", binding.etComment.getText().toString().trim());
        params.put("user", BaseApplication.preferences.getUserId());
        Subscription subscription = NetworkRequest.performAsyncRequest(apiService.addRating(params)
                , response -> {
                    hideProgress();
                    if (response.isSuccessful()) {
                        ResponseCommon result = response.body();
                        if (result == null) return;
                        showMessage(result.getMessage());
                        AddSymptomActivity.resetValue();
                        startWithClearStack(MainMenu.class);
                    }
                }
                , throwable -> {
                    hideProgress();
                    throwable.printStackTrace();
                });
        compositeSubscription.add(subscription);
    }
}