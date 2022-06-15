package com.arnav.pocdoc.symptomchecker;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.arnav.pocdoc.BaseActivity;
import com.arnav.pocdoc.R;
import com.arnav.pocdoc.SimplyRelief.models.DataReliefItem;
import com.arnav.pocdoc.SimplyRelief.models.ResponseCommon;
import com.arnav.pocdoc.SimplyRelief.models.SimplyReliefResponse;
import com.arnav.pocdoc.base.BaseApplication;
import com.arnav.pocdoc.databinding.ActivitySymtomAddBinding;
import com.arnav.pocdoc.implementor.RecyclerViewItemClickListener;
import com.arnav.pocdoc.retrofit.ApiClient;
import com.arnav.pocdoc.retrofit.ApiInterface;
import com.arnav.pocdoc.retrofit.NetworkRequest;
import com.arnav.pocdoc.symptomchecker.adapter.SymptomsAdapter;
import com.arnav.pocdoc.utils.DatePickerUtils;
import com.arnav.pocdoc.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class AddSymptomActivity extends BaseActivity implements RecyclerViewItemClickListener {

    public static final List<DataReliefItem> listAll = new ArrayList<>();
    public static String userType = "";
    public static String userTypeMySelf = "myself";
    public static String userTypeElse = "else";
    public static String userGender = "";
    public static String Gender = "";
    public static String userGenderFemale = "female";
    public static String userGenderMale = "male";
    public static String userDOB = "";
    public static String DOB = "";
    public static String overweight = "dont know";
    public static String hypertension = "dont know";
    public static String smokeCigaretter = "dont know";
    public static String lungDamage = "dont know";
    public static String sufferInjur = "dont know";
    public static String pregnant = "dont know";
    public static String diabetes = "dont know";
    public static String cholesterol = "dont know";
    public static String recreational_drugs = "dont know";
    public static String alcohol_use = "dont know";
    public static String liver_damage = "dont know";
    private final List<DataReliefItem> list = new ArrayList<>();
    protected ApiInterface apiService;
    protected CompositeSubscription compositeSubscription;
    private ActivitySymtomAddBinding binding;


    public static void resetValue(){
        userType = "";
        Gender = "";
        userDOB = "";
        overweight = "";
        hypertension = "";
        smokeCigaretter = "";
        lungDamage = "";
        sufferInjur = "";
        pregnant = "";
        diabetes = "";
        cholesterol = "";
        recreational_drugs = "";
        alcohol_use = "";
        liver_damage = "";
    }
    /**
     * CHECK VALIDATION
     *
     * @return
     */
/*    private boolean isValidInput() {
        if (binding.chipGroup.getChildCount() == 0) {
            Utils.setErrorOnTextInputLayout(binding.tlName,
                    getString(R.string.add_your_symptoms_error));
            binding.etBreed.requestFocus();
            return false;
        } else {
            Utils.setErrorOnTextInputLayout(binding.tlName, null);
            return true;
        }
    }*/

    public TextWatcher onTextWatcherListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().length() == 0) {
                list.clear();
                list.addAll(listAll);
            } else {
                list.clear();
                List<DataReliefItem> dataBreedList = new ArrayList<>();
                for (int i = 0; i < listAll.size(); i++) {
                    if (listAll.get(i).getPrimarySymptom().toLowerCase().contains(s.toString().toLowerCase()) ||
                            listAll.get(i).getSymptomGroup().toLowerCase().contains(s.toString().toLowerCase()) ||
                            listAll.get(i).getPrimary().toLowerCase().contains(s.toString().toLowerCase()) ||
                            listAll.get(i).getSpecific().toLowerCase().contains(s.toString().toLowerCase()) ||
                            listAll.get(i).getAssessment().toLowerCase().contains(s.toString().toLowerCase()) ||
                            listAll.get(i).getExclusions().toLowerCase().contains(s.toString().toLowerCase())) {
                        dataBreedList.add(listAll.get(i));
                    }
                }
                list.addAll(dataBreedList);
            }
            if (binding.rv.getAdapter() != null)
                binding.rv.getAdapter().notifyDataSetChanged();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private SymptomsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_symtom_add);
        binding.setActivity(this);

        apiService = ApiClient.getClient(this).create(ApiInterface.class);
        compositeSubscription = new CompositeSubscription();

        setUpHeaderView();
        getSymptomsData();

        binding.setLifecycleOwner(this);
    }

    private void getSymptomsData() {
        showProgress();
        Subscription subscription = NetworkRequest.performAsyncRequest(apiService.getSymptom()
                , response -> {
                    hideProgress();
                    if (response.isSuccessful()) {
                        SimplyReliefResponse result = response.body();
                        listAll.clear();
                        listAll.addAll(result.getData());
                        list.addAll(result.getData());
                        adapter.notifyDataSetChanged();
                    }
                }
                , throwable -> {
                    hideProgress();
                    throwable.printStackTrace();
                });
        compositeSubscription.add(subscription);
    }

    private void setUpHeaderView() {
        binding.indicator.setStepCount(6);
        binding.indicator.setCurrentStep(6);

        adapter = new SymptomsAdapter(list, getApplicationContext());
        binding.rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnRecyclerViewItemClickListener(this);

        binding.etBreed.addTextChangedListener(onTextWatcherListener);
    }

    public void onViewClick(View view) {
        if (view == binding.btnNext) {
//            if (isValidInput()) {
////                if (DogMeetDateActivity.addDogAPI != null)
////                    DogMeetDateActivity.addDogAPI.setPet_breed_ids(strFinalIds);
////                Intent intent = new Intent(activity.get(), DogBirthdayActivity.class);
////                NavigatorManager.startNewActivity(activity.get(), intent);
//                LogUtils.Print("TAG", "strFinalIds -> " + strFinalIds);
            submitSymptomsData();
//                start(SymptomResultActivity.class);
//            }
        }
    }

    private void submitSymptomsData() {
        showProgress();
        HashMap<String, String> params = new HashMap<>();
        params.put("user_type", userType);
        params.put("gender", userGender);
        params.put("dob", Utils.GetDateOnRequireFormat(userDOB, DatePickerUtils.DATE_DD_MM_YYYY_FORMAT, DatePickerUtils.DATE_YYYY_MM_DD_FORMAT));
        params.put("overweight", overweight);
        params.put("hypertension", hypertension);
        params.put("smoke_cigaretter", smokeCigaretter);
        params.put("lung_damage", lungDamage);
        params.put("suffer_injur", sufferInjur);
        if (AddSymptomActivity.userGender.toLowerCase().equals(AddSymptomActivity.userGenderMale)) {
            params.put("pregnant", "dont know");
        } else {
            params.put("pregnant", pregnant);
        }
        params.put("diabetes", diabetes);
        params.put("cholesterol", cholesterol);
        params.put("recreational_drugs", recreational_drugs);
        params.put("alcohol_use", alcohol_use);
        params.put("liver_damage", liver_damage);

        StringBuilder strIds = new StringBuilder();
        for (int i = 0; i < listAll.size(); i++) {
            if (listAll.get(i).isSelect())
                strIds.append(listAll.get(i).getId()).append(",");
        }
        String strFinalIds = Utils.removeLastChar(strIds.toString());
        params.put("symptom", strFinalIds);
        params.put("user", BaseApplication.preferences.getUserId());
        Subscription subscription = NetworkRequest.performAsyncRequest(apiService.addSymptom(params)
                , response -> {
                    hideProgress();
                    if (response.isSuccessful()) {
                        ResponseCommon result = response.body();
                        if (result == null) return;
//                        showMessage(result.getMessage());
                        start(SymptomExclusionsViewActivity.class);
                    }
                }
                , throwable -> {
                    hideProgress();
                    throwable.printStackTrace();
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void onItemClick(int position, int flag, View view) {
        if (flag == 0) {
            if (list.get(position).isSelect()) {
                //remove chip from group
                list.get(position).setSelect(false);
                listAll.get(listAll.indexOf(list.get(position))).setSelect(false);
/*                for (int i = 0; i < binding.chipGroup.getChildCount(); i++) {
                    Chip chip = (Chip) binding.chipGroup.getChildAt(i);
                    if (chip.getId() == list.get(position).getId()) {
                        binding.chipGroup.removeView(chip);
                        binding.chipGroup.invalidate();
                        break;
                    }
                }*/
                if (binding.rv.getAdapter() != null)
                    binding.rv.getAdapter().notifyDataSetChanged();
            } else {
/*                View inflateView = LayoutInflater.from(this).inflate(R.layout.item_filter_chip, binding.chipGroup, false);
                Chip chip = inflateView.findViewById(R.id.chips_item_filter);
                chip.setId(list.get(position).getId());
                chip.setTag(list.get(position).getId());
                chip.setText(list.get(position).getPrimarySymptom());

                //remove chip from group
                chip.setOnClickListener(v -> {
                    LogUtils.Print("TAG", "v.getId()->" + v.getId());
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getId() == v.getId()) {
                            list.get(i).setSelect(false);
                            listAll.get(listAll.indexOf(list.get(i))).setSelect(false);
                            break;
                        }
                    }
                    if (binding.rv.getAdapter() != null)
                        binding.rv.getAdapter().notifyDataSetChanged();
                    binding.chipGroup.removeView(v);
                    binding.chipGroup.invalidate();
                });

                //add chip into group
                binding.chipGroup.addView(chip);*/

                list.get(position).setSelect(true);
                listAll.get(listAll.indexOf(list.get(position))).setSelect(true);

                Utils.setErrorOnTextInputLayout(binding.tlName, null);
            }
        }
    }
}