package com.arnav.pocdoc.symptomchecker;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.arnav.pocdoc.BaseActivity;
import com.arnav.pocdoc.R;
import com.arnav.pocdoc.databinding.ActivitySymtomCheckerBinding;

public class SymptomCheckerActivity extends BaseActivity {

    private ActivitySymtomCheckerBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_symtom_checker);
        binding.setActivity(this);
        setUpHeaderView();
    }

    private void setUpHeaderView() {
        binding.indicator.setStepCount(6);
        binding.indicator.setCurrentStep(0);
    }

    public void onViewClick(View view) {
        if (view == binding.btnNext) {
            start(SymptomCheckerTCActivity.class);
        }
    }
}