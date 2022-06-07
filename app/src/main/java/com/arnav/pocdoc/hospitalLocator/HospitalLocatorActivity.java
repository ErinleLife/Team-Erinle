package com.arnav.pocdoc.hospitalLocator;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.arnav.pocdoc.BaseActivity;
import com.arnav.pocdoc.R;
import com.arnav.pocdoc.SimplyRelief.models.DataOTCItem;
import com.arnav.pocdoc.SimplyRelief.models.HospitalLocatorResponse;
import com.arnav.pocdoc.SimplyRelief.models.ResponseCommon;
import com.arnav.pocdoc.databinding.ActivityHospitalLocatorBinding;
import com.arnav.pocdoc.implementor.RecyclerViewItemClickListener;
import com.arnav.pocdoc.otc.Adapter.OTCAdapter;
import com.arnav.pocdoc.retrofit.ApiClient;
import com.arnav.pocdoc.retrofit.ApiInterface;
import com.arnav.pocdoc.retrofit.NetworkRequest;
import com.arnav.pocdoc.utils.Constants;
import com.arnav.pocdoc.utils.Utils;
import com.bumptech.glide.Glide;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class HospitalLocatorActivity extends BaseActivity implements RecyclerViewItemClickListener {

    public static final List<DataOTCItem> listAll = new ArrayList<>();
    private final List<DataOTCItem> list = new ArrayList<>();
    protected ApiInterface apiService;
    protected CompositeSubscription compositeSubscription;
    private ActivityHospitalLocatorBinding binding;
    private static final int PICK_FRONT_IMAGE = 100;
    private static final int PICK_BACK_IMAGE = 101;

    private OTCAdapter adapter;
    private HospitalLocatorResponse result;
    private int selectedPosition = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_hospital_locator);
        binding.setActivity(this);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                0);

        apiService = ApiClient.getClient(this).create(ApiInterface.class);
        compositeSubscription = new CompositeSubscription();

        getSymptomsData();
        setDetailEditText();


        binding.setLifecycleOwner(this);


        binding.checkboxEitherEnterPharmacyInsurance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
//                    binding.checkboxScanPharmacy.setChecked(false);
//                    binding.llInputTablelayout.setVisibility(View.VISIBLE);
//                    binding.llUploadImageTablelayout.setVisibility(View.GONE);

                    setCheckBoxData();
                    binding.checkboxEitherEnterPharmacyInsurance.setChecked(true);
                    binding.checkboxEitherEnterPharmacyInsurance.setEnabled(false);
                    binding.llInputTablelayout.setVisibility(View.VISIBLE);



                }

            }
        });

        binding.checkboxScanPharmacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    setCheckBoxData();
                    binding.checkboxScanPharmacy.setChecked(true);
                    binding.checkboxScanPharmacy.setEnabled(false);
                    binding.llUploadImageTablelayout.setVisibility(View.VISIBLE);

//                    binding.checkboxEitherEnterPharmacyInsurance.setChecked(false);
//                    binding.llInputTablelayout.setVisibility(View.GONE);
//                    binding.llUploadImageTablelayout.setVisibility(View.VISIBLE);
                }

            }
        });

        binding.checkboxSendScreen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
//                    binding.checkboxEitherEnterPharmacyInsurance.setChecked(false);
//                    binding.llInputTablelayout.setVisibility(View.GONE);
//                    binding.llUploadImageTablelayout.setVisibility(View.VISIBLE);

                    setCheckBoxData();
                    binding.checkboxSendScreen.setChecked(true);
                    binding.checkboxSendScreen.setEnabled(false);
                    binding.llUploadImageSendScreen.setVisibility(View.VISIBLE);

                }

            }
        });

    }

    private void setCheckBoxData()
    {
        binding.checkboxScanPharmacy.setChecked(false);
        binding.checkboxEitherEnterPharmacyInsurance.setChecked(false);
        binding.checkboxSendScreen.setChecked(false);

        binding.checkboxScanPharmacy.setEnabled(true);
        binding.checkboxEitherEnterPharmacyInsurance.setEnabled(true);
        binding.checkboxSendScreen.setEnabled(true);

        binding.llUploadImageTablelayout.setVisibility(View.GONE);
        binding.llInputTablelayout.setVisibility(View.GONE);
        binding.llUploadImageSendScreen.setVisibility(View.GONE);

    }

    private void setDetailEditText() {

        binding.inputDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                binding.tvInputtextCount.setText(charSequence.length() + "/" + "150");

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.uploadImageDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                binding.tvUploadImageTextCount.setText(charSequence.length() + "/" + "150");

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.etCheckNODescription.addTextChangedListener(new TextWatcher() {
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

        binding.uploadImageDescriptionSendScreen.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                binding.tvSendScreenUploadImageTextCount.setText(charSequence.length() + "/" + "150");

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

                        ArrayList<String> arr = new ArrayList<>();
                        result = response.body();
                        for (int i = 0; i < result.getData().size(); i++) {
                            arr.add(result.getData().get(i).getName() + ", " + result.getData().get(i).getAddress() + ", " + result.getData().get(i).getZipcode());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.row_hospital, arr);
//                        binding.autoCompleteTextView1.setThreshold(1);
//                        binding.autoCompleteTextView1.setAdapter(adapter);
//                        binding.autoCompleteTextView1.setOnItemClickListener((adapterView, view, position, id) -> selectedPosition = position);
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
            openFrontImageDialog();
        } else if (view == binding.tvFrontImage || view == binding.ivFrontImage) {
            isFrontImage = true;
            openFrontImageDialog();
        } else if (view == binding.btnNext) {
//            if (binding.autoCompleteTextView1.getText().toString().isEmpty()) {
//                selectedPosition = 0;
//            }
//            if (!binding.autoCompleteTextView1.getText().toString().isEmpty() && selectedPosition != -1) {
//                if (imageFront != null && imageBack != null) {
//                    showProgress();
//                    HashMap<String, RequestBody> params = new HashMap<>();
//                    params.put(Constants.pharmacy_id, createRequestBody(result.getData().get(selectedPosition).getId().toString()));
//                    params.put(Constants.description, createRequestBody(binding.etDescription.getText().toString().trim()));
//                    MultipartBody.Part bodyPhotoFront = null;
//                    if (imageFront != null) {
//                        File file = new File(imageFront.getPath());
//                        bodyPhotoFront = MultipartBody.Part.createFormData(Constants.image_front,
//                                file.getName(), createRequestBody(file));
//                    }
//                    MultipartBody.Part bodyPhotoBack = null;
//                    if (imageBack != null) {
//                        File file = new File(imageBack.getPath());
//                        bodyPhotoBack = MultipartBody.Part.createFormData(Constants.image_back,
//                                file.getName(), createRequestBody(file));
//                    }
//                    Subscription subscription = NetworkRequest.performAsyncRequest(apiService.addProductReview(params, bodyPhotoFront, bodyPhotoBack)
//                            , response -> {
//                                hideProgress();
//                                if (response.isSuccessful()) {
//                                    ResponseCommon result = response.body();
//                                    if (result == null) return;
//                                    Utils.makeToast(result.getMessage());
//                                    finish();
//                                }
//                            }
//                            , throwable -> {
//                                hideProgress();
//                                throwable.printStackTrace();
//                            });
//                    compositeSubscription.add(subscription);
//                } else {
//                    Toast.makeText(getApplicationContext(), "Please choose a front and back image", Toast.LENGTH_LONG).show();
//                }
//            } else {
//                Toast.makeText(getApplicationContext(), "Please choose pharmacy", Toast.LENGTH_LONG).show();
//            }
        }
    }

    private MediaFile imageFront, imageBack;
    private boolean isFrontImage = false;

    private void openFrontImageDialog() {
        Intent intent = new Intent(this, FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                .setCheckPermission(true)
                .setShowImages(true)
                .setShowVideos(false)
                .enableImageCapture(true)
                .enableVideoCapture(false)
                .setSingleChoiceMode(false)
                .setMaxSelection(1)
                .build());
        imageResultLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> imageResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (result.getData() != null) {
                        if (result.getData().getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES) != null) {
                            ArrayList<MediaFile> files = result.getData().getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
                            if (files == null || files.size() == 0) return;
                            if (isFrontImage) {
                                imageFront = files.get(0);
                                binding.tvFrontImage.setVisibility(View.GONE);
                                binding.ivFrontImage.setVisibility(View.VISIBLE);
                            } else {
                                imageBack = files.get(0);
                                binding.tvBackImage.setVisibility(View.GONE);
                                binding.ivBackImage.setVisibility(View.VISIBLE);
                            }
                            Glide.with(this)
                                    .load(files.get(0).getPath())
                                    .centerCrop()
                                    .into(isFrontImage ? binding.ivFrontImage : binding.ivBackImage);
                        }
                    }
                }
            });

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_yes:
                if (checked)
                {
                    binding.llCheckyesLayout.setVisibility(View.VISIBLE);
                    binding.llChecknoLayout.setVisibility(View.GONE);
                }
                    // Pirates are the best
                break;
            case R.id.radio_no:
                if (checked)
                {
                    binding.llCheckyesLayout.setVisibility(View.GONE);
                    binding.llChecknoLayout.setVisibility(View.VISIBLE);
                }
                    // Ninjas rule


                break;
        }

    }

    @Override
    public void onItemClick(int position, int flag, View view) {
        if (flag == 0) {
//            Intent intent = new Intent(getApplicationContext(), OTCDetailsActivity.class);
//            intent.putExtra(Constants.position,position);
//            startActivity(intent);
        }
    }

    /**
     * MULTIPLE IMAGE UPLOAD WITH MULTIPART
     */
    private static final String MULTIPART_FORM_DATA = "multipart/form-data";

    public static RequestBody createRequestBody(@NonNull String s) {
        return RequestBody.create(s, MediaType.parse(MULTIPART_FORM_DATA));
    }

    public static RequestBody createRequestBody(@NonNull File file) {
        return RequestBody.create(file, MediaType.parse(MULTIPART_FORM_DATA));
    }
}