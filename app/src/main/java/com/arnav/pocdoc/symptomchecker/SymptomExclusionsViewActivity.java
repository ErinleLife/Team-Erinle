package com.arnav.pocdoc.symptomchecker;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.arnav.pocdoc.BaseActivity;
import com.arnav.pocdoc.R;
import com.arnav.pocdoc.SimplyRelief.models.DataReliefItem;
import com.arnav.pocdoc.databinding.ActivityExclusionsViewBinding;

import java.util.ArrayList;
import java.util.List;

public class SymptomExclusionsViewActivity extends BaseActivity {

    private final List<DataReliefItem> list = new ArrayList<>();
    private ActivityExclusionsViewBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_exclusions_view);
        binding.setActivity(this);

        setUpHeaderView();
        binding.setLifecycleOwner(this);
    }


    public void onViewClick(View view) {
        if (view == binding.btnNext) {
            start(SymptomResultActivity.class);
        }
    }

    private void setUpHeaderView() {
        list.clear();
        for (int i = 0; i < AddSymptomActivity.listAll.size(); i++) {
            if (AddSymptomActivity.listAll.get(i).isSelect()) {
                list.add(AddSymptomActivity.listAll.get(i));
            }
        }

        StringBuilder strExclusions = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            strExclusions.append("● ").append(list.get(i).getExclusions());

            if (list.size() != (i + 1)) {
                strExclusions.append("\n");
            }
        }

        binding.tvExclusions.setText(strExclusions.toString());
    }
}