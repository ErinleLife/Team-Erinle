package com.arnav.pocdoc.otc;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.arnav.pocdoc.BaseActivity;
import com.arnav.pocdoc.R;
import com.arnav.pocdoc.SimplyRelief.models.DataOTCItem;
import com.arnav.pocdoc.SimplyRelief.models.Recomendation;
import com.arnav.pocdoc.databinding.ActivityOtcDetailsBinding;
import com.arnav.pocdoc.implementor.RecyclerNaturalViewItemClickListener;
import com.arnav.pocdoc.implementor.RecyclerViewItemClickListener;
import com.arnav.pocdoc.symptomchecker.ImagesPagerActivity;
import com.arnav.pocdoc.symptomchecker.adapter.NaturalAdapter;
import com.arnav.pocdoc.symptomchecker.adapter.RecommendationAdapter;
import com.arnav.pocdoc.utils.Constants;
import com.arnav.pocdoc.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class OTCDetailsActivity extends BaseActivity implements RecyclerViewItemClickListener, RecyclerNaturalViewItemClickListener {

    private final List<DataOTCItem> list = new ArrayList<>();
    private final ArrayList<Recomendation> listRecommendation = new ArrayList<>();
    private final List<Recomendation> listNatural = new ArrayList<>();

    private ActivityOtcDetailsBinding binding;

    private int selectedPosition = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otc_details);
        binding.setActivity(this);
        getIntentValue();
        setUpHeaderView();
    }

    private void getIntentValue() {
        selectedPosition = getIntent().getIntExtra(Constants.position, -1);
    }

    private void setUpHeaderView() {

        list.clear();
        if (selectedPosition > -1) {
            binding.etBreed.addTextChangedListener(onTextWatcherListener);
            binding.header.tvTitle.setText(OTCAndNaturalDrugsActivity.listAll.get(selectedPosition).getTitle());

            list.add(OTCAndNaturalDrugsActivity.listAll.get(selectedPosition));

            for (int j = 0; j < OTCAndNaturalDrugsActivity.listAll.get(selectedPosition).getRecomendation().size(); j++) {
                Recomendation recomendation = OTCAndNaturalDrugsActivity.listAll.get(selectedPosition).getRecomendation().get(j);
                if (!listRecommendation.contains(recomendation)) {
                    listRecommendation.add(recomendation);
                }
            }
            for (int j = 0; j < OTCAndNaturalDrugsActivity.listAll.get(selectedPosition).getNatural().size(); j++) {
                Recomendation recomendation = OTCAndNaturalDrugsActivity.listAll.get(selectedPosition).getNatural().get(j);
                if (!listNatural.contains(recomendation)) {
                    listNatural.add(recomendation);
                }
            }

            RecommendationAdapter adapter = new RecommendationAdapter(this, listRecommendation);
            NaturalAdapter adapterNatural = new NaturalAdapter(this, listNatural);
            binding.rv.setAdapter(adapter);
            binding.rvNatural.setAdapter(adapterNatural);
            adapter.notifyDataSetChanged();
            adapterNatural.notifyDataSetChanged();
            adapter.setRecyclerViewItemClickListener(this);
            adapterNatural.setRecyclerNaturalViewItemClickListener(this);
        }
    }

    public TextWatcher onTextWatcherListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().length() == 0) {
                listRecommendation.clear();
                for (int j = 0; j < OTCAndNaturalDrugsActivity.listAll.get(selectedPosition).getRecomendation().size(); j++) {
                    Recomendation recomendation = OTCAndNaturalDrugsActivity.listAll.get(selectedPosition).getRecomendation().get(j);
                    if (!listRecommendation.contains(recomendation)) {
                        listRecommendation.add(recomendation);
                    }
                }
                listNatural.clear();
                for (int j = 0; j < OTCAndNaturalDrugsActivity.listAll.get(selectedPosition).getNatural().size(); j++) {
                    Recomendation recomendation = OTCAndNaturalDrugsActivity.listAll.get(selectedPosition).getNatural().get(j);
                    if (!listNatural.contains(recomendation)) {
                        listNatural.add(recomendation);
                    }
                }
            } else {
                listRecommendation.clear();
                final ArrayList<Recomendation> dataBreedList = new ArrayList<>();

                for (int j = 0; j < OTCAndNaturalDrugsActivity.listAll.get(selectedPosition).getRecomendation().size(); j++) {
                    Recomendation recomendation = OTCAndNaturalDrugsActivity.listAll.get(selectedPosition).getRecomendation().get(j);
                    if (recomendation.getName().toLowerCase().contains(s.toString().toLowerCase())) {
                        dataBreedList.add(recomendation);
                    }
                }
                listRecommendation.addAll(dataBreedList);


                listNatural.clear();
                final ArrayList<Recomendation> dataBreedListNatural = new ArrayList<>();

                for (int j = 0; j < OTCAndNaturalDrugsActivity.listAll.get(selectedPosition).getNatural().size(); j++) {
                    Recomendation recomendation = OTCAndNaturalDrugsActivity.listAll.get(selectedPosition).getNatural().get(j);
                    if (recomendation.getName().toLowerCase().contains(s.toString().toLowerCase())) {
                        dataBreedListNatural.add(recomendation);
                    }
                }
                listNatural.addAll(dataBreedListNatural);

            }
            if (binding.rv.getAdapter() != null)
                binding.rv.getAdapter().notifyDataSetChanged();
            if (binding.rvNatural.getAdapter() != null)
                binding.rvNatural.getAdapter().notifyDataSetChanged();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    public void onViewClick(View view) {

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
}