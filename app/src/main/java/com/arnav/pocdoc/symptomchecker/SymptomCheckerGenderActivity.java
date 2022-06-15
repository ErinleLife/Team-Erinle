package com.arnav.pocdoc.symptomchecker;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.arnav.pocdoc.BaseActivity;
import com.arnav.pocdoc.R;
import com.arnav.pocdoc.databinding.ActivitySymtomCheckerGenderBinding;
import com.arnav.pocdoc.utils.DialogUtils;

public class SymptomCheckerGenderActivity extends BaseActivity {

    private ActivitySymtomCheckerGenderBinding binding;
    private int selectPosition = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_symtom_checker_gender);
        binding.setActivity(this);
        setUpHeaderView();
        selectPosition = AddSymptomActivity.userGender.toLowerCase().equals(AddSymptomActivity.userGenderFemale)
                ? 1 : AddSymptomActivity.userGender.toLowerCase().equals(AddSymptomActivity.userGenderMale)
                ? 2 : 0;
        updateView();
    }

    private void setUpHeaderView() {
        binding.indicator.setStepCount(6);
        binding.indicator.setCurrentStep(3);
    }

    public void onViewClick(View view) {
        if (view == binding.btnNext) {
            if (selectPosition == 0) {
                showSMessage(getResources().getString(R.string.gender_empty));
            } else {
                if (AddSymptomActivity.userType == AddSymptomActivity.userTypeMySelf) {
                    if (AddSymptomActivity.DOB.equals("")) {
                        start(SymptomCheckerDOBActivity.class);
                    } else {
                        AddSymptomActivity.userDOB = AddSymptomActivity.DOB;
                        start(SymptomCheckerPatientActivity.class);
                    }
                } else {
                    start(SymptomCheckerDOBActivity.class);
                }
            }
        } else if (view == binding.tvFemale) {
            AddSymptomActivity.userGender = AddSymptomActivity.userGenderFemale;
            selectPosition = 1;
            updateView();
        } else if (view == binding.tvMale) {
            AddSymptomActivity.userGender = AddSymptomActivity.userGenderMale;
            selectPosition = 2;
            updateView();
        } else if (view == binding.tvInfo) {
            DialogUtils.showInformationDialog(this, null,
                    getResources().getString(R.string.what_should_i_select),
                    getResources().getString(R.string.what_should_i_select_desc),
                    getResources().getString(R.string.ok));
        }
    }

    private void updateView() {
        binding.tvFemale.setBackground(ContextCompat.getDrawable(this,
                selectPosition == 1 ? R.drawable.bg_gray_round_sel : R.drawable.bg_gray_round));
        binding.tvMale.setBackground(ContextCompat.getDrawable(this,
                selectPosition == 2 ? R.drawable.bg_gray_round_sel : R.drawable.bg_gray_round));
    }
}