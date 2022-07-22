package com.arnav.pocdoc.symptomchecker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.arnav.pocdoc.BaseActivity;
import com.arnav.pocdoc.MainMenu;
import com.arnav.pocdoc.R;
import com.arnav.pocdoc.SimplyRelief.models.DataReliefItem;
import com.arnav.pocdoc.SimplyRelief.models.Recomendation;
import com.arnav.pocdoc.SpeakToDoc.SpeakToDoc;
import com.arnav.pocdoc.databinding.ActivitySymtomCheckerResultBinding;
import com.arnav.pocdoc.implementor.RecyclerNaturalViewItemClickListener;
import com.arnav.pocdoc.implementor.RecyclerViewItemClickListener;
import com.arnav.pocdoc.symptomchecker.adapter.NaturalAdapter;
import com.arnav.pocdoc.symptomchecker.adapter.PreventionAdapter;
import com.arnav.pocdoc.symptomchecker.adapter.RecommendationAdapter;
import com.arnav.pocdoc.utils.Constants;
import com.arnav.pocdoc.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SymptomResultActivity extends BaseActivity implements RecyclerViewItemClickListener, RecyclerNaturalViewItemClickListener {

    private final List<DataReliefItem> list = new ArrayList<>();
    private final ArrayList<Recomendation> listRecommendation = new ArrayList<>();
    private final List<Recomendation> listNatural = new ArrayList<>();
    private final List<Recomendation> listPrevention = new ArrayList<>();
    private ActivitySymtomCheckerResultBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_symtom_checker_result);
        binding.setActivity(this);
        setUpHeaderView();
    }

    private void setUpHeaderView() {
        int intVal = AddSymptomActivity.lungDamage.equalsIgnoreCase("yes") ||
                AddSymptomActivity.hypertension.equalsIgnoreCase("yes") ||
                AddSymptomActivity.pregnant.equalsIgnoreCase("yes") ? 1 : 0;
        binding.tvTC.setText(intVal == 1 ? getResources().getString(R.string.monitory_exclusions_desc) :
                getResources().getString(R.string.go_to_emergency));

        list.clear();
        for (int i = 0; i < AddSymptomActivity.listAll.size(); i++) {
            if (AddSymptomActivity.listAll.get(i).isSelect()) {
                list.add(AddSymptomActivity.listAll.get(i));

                for (int j = 0; j < AddSymptomActivity.listAll.get(i).getRecomendation().size(); j++) {
                    Recomendation recomendation = AddSymptomActivity.listAll.get(i).getRecomendation().get(j);
                    if (!listRecommendation.contains(recomendation)) {
                        listRecommendation.add(recomendation);
                    }
                }
                for (int j = 0; j < AddSymptomActivity.listAll.get(i).getNatural().size(); j++) {
                    Recomendation recomendation = AddSymptomActivity.listAll.get(i).getNatural().get(j);
                    if (!listNatural.contains(recomendation)) {
                        listNatural.add(recomendation);
                    }
                }
                for (int j = 0; j < AddSymptomActivity.listAll.get(i).getPrevention().size(); j++) {
                    Recomendation recomendation = AddSymptomActivity.listAll.get(i).getPrevention().get(j);
                    if (!listPrevention.contains(recomendation)) {
                        listPrevention.add(recomendation);
                    }
                }
            }
        }

        RecommendationAdapter adapter = new RecommendationAdapter(this, listRecommendation);
        NaturalAdapter adapterNatural = new NaturalAdapter(this, listNatural);
        PreventionAdapter adapterPrevention = new PreventionAdapter(this, listPrevention);
        binding.rv.setAdapter(adapter);
        binding.rvNatural.setAdapter(adapterNatural);
        binding.rvPrevention.setAdapter(adapterPrevention);
        adapter.notifyDataSetChanged();
        adapterNatural.notifyDataSetChanged();
        adapterPrevention.notifyDataSetChanged();
        adapter.setRecyclerViewItemClickListener(this);
        adapterNatural.setRecyclerNaturalViewItemClickListener(this);
        adapterPrevention.setRecyclerViewItemClickListener(this);
        binding.rBar.setOnClickListener(v -> start(SymptomCheckerFeedbackActivity.class));


        StringBuilder strPrimarySymptom = new StringBuilder();
        StringBuilder strGroupSymptom = new StringBuilder();
        StringBuilder strAssessment = new StringBuilder();
//        StringBuilder strExclusions = new StringBuilder();
//        StringBuilder strNatural = new StringBuilder();
//        StringBuilder strPrevention = new StringBuilder();
        StringBuilder strSpecific = new StringBuilder();
        StringBuilder strPrimary = new StringBuilder();

        List<String> preventionList = new ArrayList<>();
        List<String> specificList = new ArrayList<>();
        List<String> primaryList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            strPrimarySymptom.append("● ").append(list.get(i).getPrimarySymptom());
            strGroupSymptom.append("● ").append(list.get(i).getSymptomGroup());
            strAssessment.append("● ").append(list.get(i).getAssessment());

//            if (!strPrevention.toString().contains(list.get(i).getPrevention().toString())) {
//                if (!list.get(i).getPrevention().equals("-")) {
//                    strPrevention.append(list.get(i).getPrevention());
//                    preventionList.add("● " + list.get(i).getPrevention());
//                }
//            }
            if (!strSpecific.toString().contains(list.get(i).getSpecific())) {
                strSpecific.append(list.get(i).getSpecific());
                specificList.add("● " + list.get(i).getSpecific());
            }
            if (!strPrimary.toString().contains(list.get(i).getPrimary())) {
                strPrimary.append(list.get(i).getPrimary());
                primaryList.add("● " + list.get(i).getPrimary());
            }
            if (list.size() != (i + 1)) {
                strPrimarySymptom.append("\n");
                strGroupSymptom.append("\n");
                strAssessment.append("\n");
            }
        }

//        strPrevention = new StringBuilder();
        strSpecific = new StringBuilder();
        strPrimary = new StringBuilder();

//        for (int i = 0; i < preventionList.size(); i++) {
//            strPrevention.append(preventionList.get(i));
//            if (preventionList.size() != (i + 1)) {
//                strPrevention.append("\n");
//            }
//        }
        for (int i = 0; i < specificList.size(); i++) {
            strSpecific.append(specificList.get(i));
            if (specificList.size() != (i + 1)) {
                strSpecific.append("\n");
            }
        }
        for (int i = 0; i < primaryList.size(); i++) {
            strPrimary.append(primaryList.get(i));
            if (primaryList.size() != (i + 1)) {
                strPrimary.append("\n");
            }
        }

        binding.tvPrimarySymptom.setText(strPrimarySymptom.toString());
        binding.tvSymptomGroup.setText(strGroupSymptom.toString());
        binding.tvAssessment.setText(strAssessment.toString());
//        binding.tvExclusions.setText(strExclusions.toString());
//        binding.tvNatural.setText(strNatural.toString());
//        binding.tvPrevention.setText(strPrevention.toString());
        binding.tvSpecific.setText(strSpecific.toString());
        binding.tvPrimary.setText(strPrimary.toString());
    }

    public void onViewClick(View view) {
        if (view == binding.btnNext) {
            AddSymptomActivity.resetValue();
            startWithClearStack(MainMenu.class);
        } else if (view == binding.clBottomReview) {
            start(SymptomCheckerFeedbackActivity.class);
        } else if (view == binding.btnHeyDoc) {
            start(SpeakToDoc.class);
        }
    }

    @Override
    public void onItemClick(int position, int flag, View view) {
        if (flag == 1) {
            Utils.startWebActivity(this, listRecommendation.get(position).getUrl());
        } else if (flag == 2) {
            ArrayList<String> zoomArray = new ArrayList<>();

            if (listRecommendation.get(position).getFrontImage() != null && !listRecommendation.get(position).getFrontImage().equals("")) {
                zoomArray.add(listRecommendation.get(position).getFrontImage());
            }
            if (listRecommendation.get(position).getBackImage() != null && !listRecommendation.get(position).getBackImage().equals("")) {
                zoomArray.add(listRecommendation.get(position).getBackImage());
            }

            Intent intent = new Intent(this, ImagesPagerActivity.class);
            intent.putExtra(Constants.position, 0);
            intent.putStringArrayListExtra(Constants.data, zoomArray);
            startActivity(intent);
        } else if (flag == 11) {
            if (listPrevention.get(position).getUrl() != null && !listPrevention.get(position).getUrl().equals(""))
                Utils.startWebActivity(this, listPrevention.get(position).getUrl());
        } else if (flag == 22) {
            ArrayList<String> zoomArray = new ArrayList<>();

            if (listPrevention.get(position).getFrontImage() != null && !listPrevention.get(position).getFrontImage().equals("")) {
                zoomArray.add(listPrevention.get(position).getFrontImage());
            }
            if (listPrevention.get(position).getBackImage() != null && !listPrevention.get(position).getBackImage().equals("")) {
                zoomArray.add(listPrevention.get(position).getBackImage());
            }

            Intent intent = new Intent(this, ImagesPagerActivity.class);
            intent.putExtra(Constants.position, 0);
            intent.putStringArrayListExtra(Constants.data, zoomArray);
            startActivity(intent);
        }
    }

    @Override
    public void onItemClickNatural(int position, int flag, View view) {
        if (flag == 1) {
            Utils.startWebActivity(this, listNatural.get(position).getUrl());
        } else if (flag == 2) {
            ArrayList<String> zoomArray = new ArrayList<>();

            if (listNatural.get(position).getFrontImage() != null && !listNatural.get(position).getFrontImage().equals("")) {
                zoomArray.add(listNatural.get(position).getFrontImage());
            }
            if (listNatural.get(position).getBackImage() != null && !listNatural.get(position).getBackImage().equals("")) {
                zoomArray.add(listNatural.get(position).getBackImage());
            }

            Intent intent = new Intent(this, ImagesPagerActivity.class);
            intent.putExtra(Constants.position, 0);
            intent.putStringArrayListExtra(Constants.data, zoomArray);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        AddSymptomActivity.resetValue();
        startWithClearStack(MainMenu.class);
    }
}