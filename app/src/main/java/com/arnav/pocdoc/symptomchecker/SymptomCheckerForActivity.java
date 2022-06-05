package com.arnav.pocdoc.symptomchecker;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.arnav.pocdoc.BaseActivity;
import com.arnav.pocdoc.R;
import com.arnav.pocdoc.databinding.ActivitySymtomCheckerForBinding;

public class SymptomCheckerForActivity extends BaseActivity {

    private ActivitySymtomCheckerForBinding binding;
    private int selectPosition = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_symtom_checker_for);
        binding.setActivity(this);
        setUpHeaderView();
        selectPosition = AddSymptomActivity.userType.equals(AddSymptomActivity.userTypeMySelf)
                ? 1 : AddSymptomActivity.userType.equals(AddSymptomActivity.userTypeElse)
                ? 2 : 0;
        updateView();
    }

    private void setUpHeaderView() {
        binding.indicator.setStepCount(6);
        binding.indicator.setCurrentStep(2);
    }

    public void onViewClick(View view) {
        if (view == binding.btnNext) {
            if (selectPosition == 0) {
                showSMessage(getResources().getString(R.string.check_up_for_empty));
            } else {
                if (selectPosition == 1) {

                    if (!AddSymptomActivity.Gender.toLowerCase().equals("male") && !AddSymptomActivity.Gender.toLowerCase().equals("female")) {
                        start(SymptomCheckerGenderActivity.class);
                    } else if (AddSymptomActivity.DOB.equals("")) {
                        AddSymptomActivity.userGender = AddSymptomActivity.Gender;
                        start(SymptomCheckerDOBActivity.class);
                    } else {
                        AddSymptomActivity.userGender = AddSymptomActivity.Gender;
                        AddSymptomActivity.userDOB = AddSymptomActivity.DOB;
                        start(SymptomCheckerPatientActivity.class);
                    }
                } else {
                    AddSymptomActivity.userGender = "";
                    AddSymptomActivity.userDOB = "";
                    start(SymptomCheckerGenderActivity.class);
                }
            }
        } else if (view == binding.tvMySelf) {
            AddSymptomActivity.userType = AddSymptomActivity.userTypeMySelf;
            selectPosition = 1;
            updateView();
        } else if (view == binding.tvSomeoneElse) {
            AddSymptomActivity.userType = AddSymptomActivity.userTypeElse;
            selectPosition = 2;
            updateView();
        }
    }

    private void updateView() {
        binding.tvMySelf.setBackground(ContextCompat.getDrawable(this,
                selectPosition == 1 ? R.drawable.bg_gray_round_sel : R.drawable.bg_gray_round));
        binding.tvSomeoneElse.setBackground(ContextCompat.getDrawable(this,
                selectPosition == 2 ? R.drawable.bg_gray_round_sel : R.drawable.bg_gray_round));
    }
}