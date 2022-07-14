package com.arnav.pocdoc.symptomchecker;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.arnav.pocdoc.BaseActivity;
import com.arnav.pocdoc.R;
import com.arnav.pocdoc.databinding.ActivitySymtomCheckerPatientBinding;
import com.arnav.pocdoc.implementor.RecyclerViewItemClickListener;
import com.arnav.pocdoc.symptomchecker.adapter.StatementsAdapter;
import com.arnav.pocdoc.symptomchecker.data.DataStatement;
import com.arnav.pocdoc.utils.LogUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SymptomCheckerPatientActivity extends BaseActivity implements RecyclerViewItemClickListener {

    private final List<DataStatement> list = new ArrayList<>();
    private ActivitySymtomCheckerPatientBinding binding;
    private StatementsAdapter adapter;
    String conditions = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_symtom_checker_patient);
        binding.setActivity(this);
        getConditionsDetails();
    }

    private void getConditionsDetails() {
        showProgress();
        FirebaseDatabase.getInstance().getReference()
                .child("medical_id")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        hideProgress();
                        try {
                            if (snapshot.child("conditions").exists() && !snapshot.child("conditions").getValue().toString().equals(getString(R.string.empty_text))) {
                                conditions = snapshot.child("conditions").getValue().toString();
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                        setUpHeaderView();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        LogUtils.Print("onCancelled", "error => " + error.toString());
                        hideProgress();
                        setUpHeaderView();
                    }
                });
    }

    private void setUpHeaderView() {
        binding.indicator.setStepCount(6);
        binding.indicator.setCurrentStep(5);
        list.clear();
        String[] arrStatement = getResources().getStringArray(R.array.arrStatements);
        if (AddSymptomActivity.userGender.toLowerCase().equals(AddSymptomActivity.userGenderMale)) {
            arrStatement = getResources().getStringArray(R.array.arrStatementsMale);
        }

        int[] arrStatementSel = {
                AddSymptomActivity.overweight.equals("yes") || conditions.toLowerCase().contains("overweight") ? 0 : AddSymptomActivity.overweight.equals("no") ? 1 : 2,
                AddSymptomActivity.hypertension.equals("yes") || conditions.toLowerCase().contains("hypertension") ? 0 : AddSymptomActivity.hypertension.equals("no") ? 1 : 2,
                AddSymptomActivity.smokeCigaretter.equals("yes") || conditions.toLowerCase().contains("smoke")? 0 : AddSymptomActivity.smokeCigaretter.equals("no") ? 1 : 2,
                AddSymptomActivity.lungDamage.equals("yes") || conditions.toLowerCase().contains("lung")? 0 : AddSymptomActivity.lungDamage.equals("no") ? 1 : 2,
                AddSymptomActivity.sufferInjur.equals("yes") || conditions.toLowerCase().contains("Injur")? 0 : AddSymptomActivity.sufferInjur.equals("no") ? 1 : 2,
                AddSymptomActivity.pregnant.equals("yes") || conditions.toLowerCase().contains("pregnant")? 0 : AddSymptomActivity.pregnant.equals("no") ? 1 : 2,
                AddSymptomActivity.diabetes.equals("yes") || conditions.toLowerCase().contains("diabetes")? 0 : AddSymptomActivity.diabetes.equals("no") ? 1 : 2,
                AddSymptomActivity.cholesterol.equals("yes") || conditions.toLowerCase().contains("cholesterol")? 0 : AddSymptomActivity.cholesterol.equals("no") ? 1 : 2,
                AddSymptomActivity.recreational_drugs.equals("yes") || conditions.toLowerCase().contains("drugs")? 0 : AddSymptomActivity.recreational_drugs.equals("no") ? 1 : 2,
                AddSymptomActivity.alcohol_use.equals("yes") || conditions.toLowerCase().contains("alcohol")? 0 : AddSymptomActivity.alcohol_use.equals("no") ? 1 : 2
        };

        if (AddSymptomActivity.userGender.toLowerCase().equals(AddSymptomActivity.userGenderMale)) {
            int[] arrStatementSelMale = {
                    AddSymptomActivity.overweight.equals("yes") || conditions.toLowerCase().contains("overweight")? 0 : AddSymptomActivity.overweight.equals("no") ? 1 : 2,
                    AddSymptomActivity.hypertension.equals("yes") || conditions.toLowerCase().contains("hypertension")? 0 : AddSymptomActivity.hypertension.equals("no") ? 1 : 2,
                    AddSymptomActivity.smokeCigaretter.equals("yes") || conditions.toLowerCase().contains("smoke")? 0 : AddSymptomActivity.smokeCigaretter.equals("no") ? 1 : 2,
                    AddSymptomActivity.lungDamage.equals("yes") || conditions.toLowerCase().contains("lung")? 0 : AddSymptomActivity.lungDamage.equals("no") ? 1 : 2,
                    AddSymptomActivity.sufferInjur.equals("yes") || conditions.toLowerCase().contains("Injur")? 0 : AddSymptomActivity.sufferInjur.equals("no") ? 1 : 2,
                    AddSymptomActivity.pregnant.equals("yes") || conditions.toLowerCase().contains("pregnant")? 0 : AddSymptomActivity.pregnant.equals("no") ? 1 : 2,
                    AddSymptomActivity.diabetes.equals("yes") || conditions.toLowerCase().contains("diabetes")? 0 : AddSymptomActivity.diabetes.equals("no") ? 1 : 2,
                    AddSymptomActivity.cholesterol.equals("yes") || conditions.toLowerCase().contains("cholesterol")? 0 : AddSymptomActivity.cholesterol.equals("no") ? 1 : 2,
                    AddSymptomActivity.recreational_drugs.equals("yes") || conditions.toLowerCase().contains("drugs")? 0 : AddSymptomActivity.recreational_drugs.equals("no") ? 1 : 2,
                    AddSymptomActivity.alcohol_use.equals("yes") || conditions.toLowerCase().contains("alcohol")? 0 : AddSymptomActivity.alcohol_use.equals("no") ? 1 : 2
            };
            arrStatementSel = arrStatementSelMale;
        }

        for (int i = 0; i < arrStatement.length; i++) {
            list.add(new DataStatement(arrStatement[i], arrStatementSel[i]));
        }

        adapter = new StatementsAdapter(list);
        binding.rv.setAdapter(adapter);
        binding.rv.setHasFixedSize(true);
        adapter.notifyDataSetChanged();
        adapter.setRecyclerViewItemClickListener(this);
    }

    public void onViewClick(View view) {
        if (view == binding.btnNext) {
            start(AddSymptomActivity.class);
        }
    }

    @Override
    public void onItemClick(int position, int flag, View view) {
        list.get(position).setSelectedItem(flag);
        switch (position) {
            case 0:
                AddSymptomActivity.overweight = flag == 0 ? "yes" : flag == 1 ? "no" : "dont know";
                break;
            case 1:
                AddSymptomActivity.hypertension = flag == 0 ? "yes" : flag == 1 ? "no" : "dont know";
                break;
            case 2:
                AddSymptomActivity.smokeCigaretter = flag == 0 ? "yes" : flag == 1 ? "no" : "dont know";
                break;
            case 3:
                AddSymptomActivity.lungDamage = flag == 0 ? "yes" : flag == 1 ? "no" : "dont know";
                break;
            case 4:
                AddSymptomActivity.sufferInjur = flag == 0 ? "yes" : flag == 1 ? "no" : "dont know";
                break;
            case 5:
                AddSymptomActivity.pregnant = flag == 0 ? "yes" : flag == 1 ? "no" : "dont know";
                break;
            case 6:
                AddSymptomActivity.cholesterol = flag == 0 ? "yes" : flag == 1 ? "no" : "dont know";
                break;
            case 7:
                AddSymptomActivity.diabetes = flag == 0 ? "yes" : flag == 1 ? "no" : "dont know";
                break;
            case 8:
                AddSymptomActivity.recreational_drugs = flag == 0 ? "yes" : flag == 1 ? "no" : "dont know";
                break;
            case 9:
                AddSymptomActivity.alcohol_use = flag == 0 ? "yes" : flag == 1 ? "no" : "dont know";
                if (flag == 0) {
                    list.add(new DataStatement("(9.1) Have any liver damage?", 2));
                    adapter.notifyItemInserted(10);
                    binding.rv.scrollToPosition(list.size() - 1);
                } else {
                    if (list.size() > 10) {
                        list.remove(10);
                        adapter.notifyItemRemoved(10);
                    }
                }
                break;
            case 10:
                AddSymptomActivity.liver_damage = flag == 0 ? "yes" : flag == 1 ? "no" : "dont know";
                break;
        }
    }
}