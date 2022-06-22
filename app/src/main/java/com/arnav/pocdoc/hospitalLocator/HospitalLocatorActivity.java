package com.arnav.pocdoc.hospitalLocator;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.arnav.pocdoc.BaseActivity;
import com.arnav.pocdoc.R;
import com.arnav.pocdoc.SimplyRelief.models.HospitalLocatorResponse;
import com.arnav.pocdoc.databinding.ActivityHospitalLocatorBinding;
import com.arnav.pocdoc.retrofit.ApiClient;
import com.arnav.pocdoc.retrofit.ApiInterface;
import com.arnav.pocdoc.retrofit.NetworkRequest;
import com.arnav.pocdoc.utils.CameraGalleryActivity;
import com.arnav.pocdoc.utils.Constants;
import com.arnav.pocdoc.utils.LogUtils;
import com.arnav.pocdoc.utils.Utils;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class HospitalLocatorActivity extends BaseActivity {

    private static final String MULTIPART_FORM_DATA = "multipart/form-data";
    protected ApiInterface apiService;
    protected CompositeSubscription compositeSubscription;
    private ActivityHospitalLocatorBinding binding;
    private int pharmacyPosition = -1;
    private final ArrayList<String> arr = new ArrayList<>();

    private HospitalLocatorResponse result;
    private int selectedPosition = -1;
    private final ArrayList<String> uploadImageArray = new ArrayList<>();
    private boolean isClickUploadImage;
    private HospitallLocatorUploadImageAdapter hospitallLocatorUploadImageAdapter;
    private String imageFront, imageBack;
    private boolean isFrontImage = false;
    private boolean isBackImage = false;

    public static RequestBody createRequestBody(@NonNull String s) {
        return RequestBody.create(s, MediaType.parse(MULTIPART_FORM_DATA));
    }

    public static RequestBody createRequestBody(@NonNull File file) {
        return RequestBody.create(file, MediaType.parse(MULTIPART_FORM_DATA));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_hospital_locator);
        binding.setActivity(this);
        apiService = ApiClient.getClient(this).create(ApiInterface.class);
        compositeSubscription = new CompositeSubscription();
        getIntentData();
        getSymptomsData();
        setDetailEditText();
        setOnCheckBoxChange();
        setOnClickListener();
        setRecyclerView();
        binding.setLifecycleOwner(this);
    }

    private void getIntentData() {
        if (getIntent().getExtras() != null) {
            pharmacyPosition = getIntent().getExtras().getInt(Constants.pharmacyPosition);
//            selectedPosition = pharmacyPosition;
        }
        binding.header.tvTitle.setText(getResources().getString(R.string.pharmacy_locator));
    }

    private void setRecyclerView() {
        hospitallLocatorUploadImageAdapter = new HospitallLocatorUploadImageAdapter(uploadImageArray, this);
        binding.rvUploadImage.setAdapter(hospitallLocatorUploadImageAdapter);
        hospitallLocatorUploadImageAdapter.setRecyclerViewItemClickListener((position, flag, view) -> {
            uploadImageArray.remove(position);
            hospitallLocatorUploadImageAdapter.notifyItemRemoved(position);
        });
    }

    private void setOnClickListener() {
        binding.ivUploadImage1.setOnClickListener(view -> {
            isFrontImage = false;
            isBackImage = false;
            isClickUploadImage = true;
            CameraGalleryActivity.image = CameraGalleryActivity.IMAGE.AllNoCrop;
            Intent intent = new Intent(this, CameraGalleryActivity.class);
            startActivityForResult(intent, Constants.REQUEST_CODE_CAMERA);
        });
    }

    private void setOnCheckBoxChange() {
        binding.clEitherEnterPharmacyInsurance.setOnClickListener(view -> {
            setCheckBoxData();
            binding.checkboxEitherEnterPharmacyInsurance.setChecked(true);
            binding.checkboxEitherEnterPharmacyInsurance.setEnabled(false);
            binding.llUploadImageTablelayout.setVisibility(View.GONE);
            binding.llInputTablelayout.setVisibility(View.VISIBLE);
        });
        binding.checkboxEitherEnterPharmacyInsurance.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                setCheckBoxData();
                binding.checkboxEitherEnterPharmacyInsurance.setChecked(true);
                binding.checkboxEitherEnterPharmacyInsurance.setEnabled(false);
                binding.llUploadImageTablelayout.setVisibility(View.GONE);
                binding.llInputTablelayout.setVisibility(View.VISIBLE);
            }
        });

        binding.clScanPharmacy.setOnClickListener(view -> {
            setCheckBoxData();
            binding.checkboxScanPharmacy.setChecked(true);
            binding.checkboxScanPharmacy.setEnabled(false);
            binding.llUploadImageTablelayout.setVisibility(View.VISIBLE);
            binding.llInputTablelayout.setVisibility(View.GONE);
        });
        binding.checkboxScanPharmacy.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                setCheckBoxData();
                binding.checkboxScanPharmacy.setChecked(true);
                binding.checkboxScanPharmacy.setEnabled(false);
                binding.llUploadImageTablelayout.setVisibility(View.VISIBLE);
                binding.llInputTablelayout.setVisibility(View.GONE);
            }
        });

        binding.clSendScreenShotInsuranceInfo.setOnClickListener(view -> {
            setCheckBoxData();
            binding.checkboxSendScreen.setChecked(true);
            binding.checkboxSendScreen.setEnabled(false);
            binding.llUploadImageTablelayout.setVisibility(View.VISIBLE);
            binding.llInputTablelayout.setVisibility(View.GONE);
        });
        binding.checkboxSendScreen.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                setCheckBoxData();
                binding.checkboxSendScreen.setChecked(true);
                binding.checkboxSendScreen.setEnabled(false);
                binding.llUploadImageTablelayout.setVisibility(View.VISIBLE);
                binding.llInputTablelayout.setVisibility(View.GONE);
            }
        });
    }

    private void setCheckBoxData() {
        binding.checkboxScanPharmacy.setChecked(false);
        binding.checkboxEitherEnterPharmacyInsurance.setChecked(false);
        binding.checkboxSendScreen.setChecked(false);

        binding.checkboxScanPharmacy.setEnabled(true);
        binding.checkboxEitherEnterPharmacyInsurance.setEnabled(true);
        binding.checkboxSendScreen.setEnabled(true);

        binding.llUploadImageTablelayout.setVisibility(View.GONE);
        binding.llInputTablelayout.setVisibility(View.GONE);
    }

    private void setDetailEditText() {
        binding.etDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                binding.tvCheckNOtextCount.setText(charSequence.length() + "/" + "500");

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void getSymptomsData() {
        showProgress();
        Subscription subscription = NetworkRequest.performAsyncRequest(apiService.getPharmacy()
                , response -> {
                    hideProgress();
                    if (response.isSuccessful()) {
                        result = response.body();
                        for (int i = 0; i < result.getData().size(); i++) {
                            arr.add(result.getData().get(i).getName() + ", " + result.getData().get(i).getAddress() + ", " + result.getData().get(i).getZipcode());
                            if (pharmacyPosition >= 0) {
                                if (pharmacyPosition == result.getData().get(i).getId()) {
                                    selectedPosition = i;
                                }
                            }
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.row_hospital, arr);
                        binding.autoCompleteTextView1.setThreshold(1);
                        binding.autoCompleteTextView1.setAdapter(adapter);
                        binding.autoCompleteTextView1.setOnItemClickListener((adapterView, view, position, id) -> {
                            selectedPosition = position;
                            Utils.hideKeyBoard(this);
                        });
                        if (pharmacyPosition >= 0) {
                            binding.autoCompleteTextView1.setText(arr.get(selectedPosition), false);
                        }
                    }
                }
                , throwable -> {
                    hideProgress();
                    throwable.printStackTrace();
                });
        compositeSubscription.add(subscription);
    }

    public void onViewClick(View view) {
        if (view == binding.tvBackImage || view == binding.ivBackImage) {
            isFrontImage = false;
            isBackImage = true;
//            openFrontImageDialog();
            CameraGalleryActivity.image = CameraGalleryActivity.IMAGE.CAMERA;
            Intent intent = new Intent(this, CameraGalleryActivity.class);
            startActivityForResult(intent, Constants.REQUEST_CODE_CAMERA);
        } else if (view == binding.tvFrontImage || view == binding.ivFrontImage) {
            isFrontImage = true;
            isBackImage = false;
//            openFrontImageDialog();
            CameraGalleryActivity.image = CameraGalleryActivity.IMAGE.CAMERA;
            Intent intent = new Intent(this, CameraGalleryActivity.class);
            startActivityForResult(intent, Constants.REQUEST_CODE_CAMERA);
        } else if (view == binding.btnNext) {
            if (selectedPosition == -1 || binding.autoCompleteTextView1.getText().toString().isEmpty()) {
                showMessage("Please select pharmacy");
                return;
            }
            if (imageFront == null) {
                showMessage("Please add front prescriptions image.");
                return;
            }
            if (imageBack == null) {
                showMessage("Please add back prescriptions image.");
                return;
            }

            showProgress();
            new Thread(() -> {
                try {
                    ArrayList<String> infoDataArray = new ArrayList<>();
                    String type = "";
                    String have_pharmacy_insurance;
                    if (binding.llCheckyesLayout.isShown()) {
                        have_pharmacy_insurance = "yes";
                        if (binding.checkboxEitherEnterPharmacyInsurance.isChecked()) {
                            infoDataArray.add("rx_bin " + binding.etRxbin.getText().toString());
                            infoDataArray.add("rx_pcn " + binding.etRxpcn.getText().toString());
                            infoDataArray.add("rx_id " + binding.etRxid.getText().toString());
                            infoDataArray.add("rx_group " + binding.etRxgroup.getText().toString());
                            type = "pharmacy_insurance";
                        } else if (binding.checkboxScanPharmacy.isChecked()) {
                            infoDataArray.addAll(uploadImageArray);
                            type = "scan_pharmacy";
                        } else if (binding.checkboxSendScreen.isChecked()) {
                            infoDataArray.addAll(uploadImageArray);
                            type = "screenshot_pharmacy";
                        }
                    } else {
                        have_pharmacy_insurance = "no";
                    }
                    String desc = binding.etDesc.getText().toString();
                    OkHttpClient client = new OkHttpClient().newBuilder().build();
                    MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                            .addFormDataPart(Constants.pharmacy_id, result.getData().get(selectedPosition).getId().toString())
                            .addFormDataPart(Constants.pharmacy_locatore, result.getData().get(selectedPosition).getZipcode())
                            .addFormDataPart(Constants.image_front, imageFront, RequestBody.create(MediaType.parse("application/octet-stream"), new File(imageFront)))
                            .addFormDataPart(Constants.image_back, imageBack, RequestBody.create(MediaType.parse("application/octet-stream"), new File(imageBack)))
                            .addFormDataPart(Constants.have_pharmacy_insurance, have_pharmacy_insurance)
                            .addFormDataPart(Constants.type, type)
                            .addFormDataPart(Constants.description, desc);

                    if (have_pharmacy_insurance.equals("yes")) {
                        if (binding.checkboxEitherEnterPharmacyInsurance.isChecked()) {
                            body.addFormDataPart(Constants.info, String.valueOf(infoDataArray));
                        } else {
                            for (int i = 0; i < infoDataArray.size(); i++) {
                                body.addFormDataPart(Constants.info + "[" + i + "]", infoDataArray.get(i), RequestBody.create(MediaType.parse("application/octet-stream"), new File(infoDataArray.get(i))));
                            }
                        }
                    }

                    RequestBody requestBody = body.build();
                    Request request = new Request.Builder()
                            .url("http://165.22.45.58/api/add-prescription")
                            .method("POST", requestBody)
                            .build();
                    Response response = client.newCall(request).execute();

                    hideProgress();
                    if (response.isSuccessful()) {
                        showMessage("Prescription successfully added.");
                        finish();
                    } else {
                        showMessage(getResources().getString(R.string.server_error));
                    }
                } catch (Exception e) {
                    hideProgress();
                    e.printStackTrace();
                    LogUtils.Print("TAG", "error >>>>>>>>>>>> " + e.getMessage());
                }
            }).start();
        }
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.radio_yes:
                if (checked) {
                    binding.llCheckyesLayout.setVisibility(View.VISIBLE);
                    if (binding.etDesc.getText().toString().trim().equals(getResources().getString(R.string.cash_paying_desc))) {
                        binding.etDesc.setText("");
                    }
                }
                break;
            case R.id.radio_no:
                if (checked) {
                    binding.llCheckyesLayout.setVisibility(View.GONE);
                    binding.etDesc.setText(getResources().getString(R.string.cash_paying_desc));
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_CAMERA) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    try {
                        if (data.getStringExtra(Constants.image) != null) {
                            if (isFrontImage || isBackImage) {
                                if (isFrontImage) {
                                    imageFront = data.getStringExtra(Constants.image);
                                    binding.tvFrontImage.setVisibility(View.GONE);
                                    binding.ivFrontImage.setVisibility(View.VISIBLE);
                                } else {
                                    imageBack = data.getStringExtra(Constants.image);
                                    binding.tvBackImage.setVisibility(View.GONE);
                                    binding.ivBackImage.setVisibility(View.VISIBLE);
                                }
                                Glide.with(this)
                                        .load(data.getStringExtra(Constants.image))
                                        .centerCrop()
                                        .into(isFrontImage ? binding.ivFrontImage : binding.ivBackImage);
                            } else {
                                uploadImageArray.add(data.getStringExtra(Constants.image));
                                Collections.reverse(uploadImageArray);
                                hospitallLocatorUploadImageAdapter.notifyDataSetChanged();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Utils.makeToast(getResources().getString(R.string.server_error));
                    }
                }
            }
        }
    }
}