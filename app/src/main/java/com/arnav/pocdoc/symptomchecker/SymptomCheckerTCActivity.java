package com.arnav.pocdoc.symptomchecker;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.arnav.pocdoc.Authentication.PrivacyPolicyActivity;
import com.arnav.pocdoc.BaseActivity;
import com.arnav.pocdoc.R;
import com.arnav.pocdoc.databinding.ActivitySymtomCheckerTcBinding;

public class SymptomCheckerTCActivity extends BaseActivity {

    private ActivitySymtomCheckerTcBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_symtom_checker_tc);
        binding.setActivity(this);
        setUpHeaderView();
    }

    private void setUpHeaderView() {
        binding.indicator.setStepCount(6);
        binding.indicator.setCurrentStep(1);

        setTermsText();
    }

    private void setTermsText() {
        String strText = getResources().getString(R.string.pp_desc);
        Spannable span = Spannable.Factory.getInstance().newSpannable(strText);

        ClickableSpan cs = new ClickableSpan() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SymptomCheckerTCActivity.this, PrivacyPolicyActivity.class));
            }

            @Override
            public void updateDrawState(final TextPaint textPaint) {
                textPaint.setColor(ContextCompat.getColor(SymptomCheckerTCActivity.this, R.color.colorPrimary));
            }
        };
        ClickableSpan cs1 = new ClickableSpan() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SymptomCheckerTCActivity.this, PrivacyPolicyActivity.class));
            }

            @Override
            public void updateDrawState(final TextPaint textPaint) {
                textPaint.setColor(ContextCompat.getColor(SymptomCheckerTCActivity.this, R.color.colorPrimary));
            }
        };

        span.setSpan(cs, 18, 34, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(cs1, 39, strText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.tvAgreeText.setText(span);
        binding.tvAgreeText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void onViewClick(View view) {
        if (view == binding.btnNext) {
            if (!binding.tvAgreeText.isChecked()) {
                showSMessage(getResources().getString(R.string.pp_invalid));
            } else if (!binding.tvAgreeHealth.isChecked()) {
                showSMessage(getResources().getString(R.string.health_desc_invalid));
            } else {
                start(SymptomCheckerForActivity.class);
            }
        }
    }
}