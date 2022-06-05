package com.arnav.pocdoc.SimplyRelief;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.arnav.pocdoc.R;
import com.arnav.pocdoc.SimplyRelief.models.DataReliefItem;
import com.arnav.pocdoc.retrofit.ApiClient;
import com.arnav.pocdoc.retrofit.ApiInterface;
import com.arnav.pocdoc.utils.Utils;

import rx.subscriptions.CompositeSubscription;

public class SimplyReliefDetail extends AppCompatActivity {

    protected ApiInterface apiService;
    protected CompositeSubscription compositeSubscription;
    Toolbar toolbar;
    DataReliefItem item;

    public static Intent getIntent(Context context, DataReliefItem item) {
        Intent intent = new Intent(context, SimplyReliefDetail.class);
        intent.putExtra("item", item);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simply_relief_detail);

        apiService = ApiClient.getClient(this).create(ApiInterface.class);
        compositeSubscription = new CompositeSubscription();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Simply Relief");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        item = (DataReliefItem) getIntent().getSerializableExtra("item");

        TextView primary = findViewById(R.id.primary);
        TextView primarySymptom = findViewById(R.id.primarySymptom);
        TextView symptomGroup = findViewById(R.id.symptomGroup);
        TextView assessment = findViewById(R.id.assessment);
        TextView exclusions = findViewById(R.id.exclusions);
        TextView recommendation = findViewById(R.id.recommendation);
        TextView natural = findViewById(R.id.natural);
        TextView prevention = findViewById(R.id.prevention);
        TextView specific = findViewById(R.id.specific);

        primary.setText(item.getPrimary());
        primarySymptom.setText(item.getPrimarySymptom());
        symptomGroup.setText(item.getSymptomGroup());
        assessment.setText(item.getAssessment());
        exclusions.setText(item.getExclusions());
        if (item.getRecomendation() != null && item.getRecomendation().size() > 0) {
            StringBuilder strMsg = new StringBuilder();
            for (int i = 0; i < item.getRecomendation().size(); i++) {
                strMsg.append("").append(item.getRecomendation().get(i).getName()).append(", ");
            }
            recommendation.setText(Utils.removeLastChar(strMsg.toString()));
        }
        if (item.getNatural() != null && item.getNatural().size() > 0) {
            StringBuilder strMsg = new StringBuilder();
            for (int i = 0; i < item.getNatural().size(); i++) {
                strMsg.append("").append(item.getNatural().get(i).getName()).append(", ");
            }
            natural.setText(Utils.removeLastChar(strMsg.toString()));
        }
        if (item.getPrevention() != null && item.getPrevention().size() > 0) {
            StringBuilder strMsg = new StringBuilder();
            for (int i = 0; i < item.getPrevention().size(); i++) {
                strMsg.append("").append(item.getPrevention().get(i).getName()).append(", ");
            }
            prevention.setText(Utils.removeLastChar(strMsg.toString()));
        }
        specific.setText(item.getSpecific());

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
