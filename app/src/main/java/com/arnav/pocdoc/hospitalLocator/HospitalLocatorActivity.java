package com.arnav.pocdoc.hospitalLocator;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.arnav.pocdoc.BaseActivity;
import com.arnav.pocdoc.R;
import com.arnav.pocdoc.SimplyRelief.models.DataOTCItem;
import com.arnav.pocdoc.SimplyRelief.models.HospitalLocatorResponse;
import com.arnav.pocdoc.databinding.ActivityHospitalLocatorBinding;
import com.arnav.pocdoc.otc.Adapter.OTCAdapter;
import com.arnav.pocdoc.retrofit.ApiClient;
import com.arnav.pocdoc.retrofit.ApiInterface;
import com.arnav.pocdoc.retrofit.NetworkRequest;
import com.arnav.pocdoc.utils.CameraGalleryActivity;
import com.arnav.pocdoc.utils.Constants;
import com.arnav.pocdoc.utils.Utils;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class HospitalLocatorActivity extends BaseActivity {

    public static final List<DataOTCItem> listAll = new ArrayList<>();
    private static final int PICK_FRONT_IMAGE = 100;
    private static final int PICK_BACK_IMAGE = 101;
    /**
     * MULTIPLE IMAGE UPLOAD WITH MULTIPART
     */
    private static final String MULTIPART_FORM_DATA = "multipart/form-data";
    private final List<DataOTCItem> list = new ArrayList<>();
    protected ApiInterface apiService;
    protected CompositeSubscription compositeSubscription;
    ArrayList<String> uploadImageArray = new ArrayList<>();
    ArrayList<String> sendScreen_uploadImageArray = new ArrayList<>();
    ArrayList<String> inputTableArrayList = new ArrayList<>();
    boolean isClickUploadImage;
    HospitallLocatorUploadImageAdapter hospitallLocatorUploadImageAdapter;
    HospitallLocatorSendScreenUploadImageAdapter hospitallLocatorSendScreenUploadImageAdapter;
    private ActivityHospitalLocatorBinding binding;
    private OTCAdapter adapter;
    private HospitalLocatorResponse result;
    private int selectedPosition = -1;
    private String imageFront, imageBack;
    private boolean isFrontImage = false;
    private boolean isBackImage = false;
//    @SuppressLint("NotifyDataSetChanged")
//    private final ActivityResultLauncher<Intent> imageResultLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(), result -> {
//                if (result.getResultCode() == Activity.RESULT_OK) {
//                    if (result.getData() != null) {
//                        if (result.getData().getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES) != null) {
//                            ArrayList<MediaFile> files = result.getData().getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
//                            if (files == null || files.size() == 0) return;
//                            if (isFrontImage || isBackImage) {
//                                if (isFrontImage) {
//                                    imageFront = files.get(0);
//                                    binding.tvFrontImage.setVisibility(View.GONE);
//                                    binding.ivFrontImage.setVisibility(View.VISIBLE);
//                                } else {
//                                    imageBack = files.get(0);
//                                    binding.tvBackImage.setVisibility(View.GONE);
//                                    binding.ivBackImage.setVisibility(View.VISIBLE);
//                                }
//                                Glide.with(this)
//                                        .load(files.get(0).getPath())
//                                        .centerCrop()
//                                        .into(isFrontImage ? binding.ivFrontImage : binding.ivBackImage);
//
//                            } else {
//
//                                if (isClickUploadImage) {
//                                    uploadImageArray.add(files.get(0).getPath());
//                                    Collections.reverse(uploadImageArray);
//                                    hospitallLocatorUploadImageAdapter.notifyDataSetChanged();
//                                } else {
//                                    sendScreen_uploadImageArray.add(files.get(0).getPath());
//                                    Collections.reverse(sendScreen_uploadImageArray);
//                                    hospitallLocatorSendScreenUploadImageAdapter.notifyDataSetChanged();
//                                }
//
//                            }
//
//                        }
//                    }
//                }
//            });

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

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                0);

        apiService = ApiClient.getClient(this).create(ApiInterface.class);
        compositeSubscription = new CompositeSubscription();

        getSymptomsData();
        setDetailEditText();
        setOnCheckBoxChange();
        setOnClickListener();
        setRecyclerView();

        binding.setLifecycleOwner(this);

    }

    private void setRecyclerView() {
        binding.rvUploadImage.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        hospitallLocatorUploadImageAdapter = new HospitallLocatorUploadImageAdapter(uploadImageArray, this);
        binding.rvUploadImage.setAdapter(hospitallLocatorUploadImageAdapter);
    }

    private void setOnClickListener() {
        binding.ivUploadImage1.setOnClickListener(view -> {
            isFrontImage = false;
            isBackImage = false;
            isClickUploadImage = true;
            CameraGalleryActivity.image = CameraGalleryActivity.IMAGE.CAMERA;
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

                        ArrayList<String> arr = new ArrayList<>();
                        result = response.body();
                        for (int i = 0; i < result.getData().size(); i++) {
                            arr.add(result.getData().get(i).getName() + ", " + result.getData().get(i).getAddress() + ", " + result.getData().get(i).getZipcode());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.row_hospital, arr);
                        binding.autoCompleteTextView1.setThreshold(1);
                        binding.autoCompleteTextView1.setAdapter(adapter);
                        binding.autoCompleteTextView1.setOnItemClickListener((adapterView, view, position, id) -> {
                            selectedPosition = position;
                            Utils.hideKeyBoard(this);
                        });
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
            if (binding.autoCompleteTextView1.getText().toString().isEmpty()) {
                selectedPosition = 0;
            }
            if (!binding.autoCompleteTextView1.getText().toString().isEmpty() && selectedPosition != -1)
                ;
            if (imageFront != null && imageBack != null) {
//
//                    HashMap<String, String> params = new HashMap<>();
//                    params.put(Constants.pharmacy_id, result.getData().get(0).getId().toString());
//                    params.put(Constants.description, "etDescription");
//                    params.put(Constants.pharmacy_locatore, "pharmacy_locatore");
//                    params.put(Constants.have_pharmacy_insurance, "yes");
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
//
//                    Log.e("TAG", "onViewClick: params >>>>>>>>>>> "+new Gson().toJson(params));
//                    Log.e("TAG", "onViewClick: bodyPhotoFront >>>>>>>>>>> "+new Gson().toJson(bodyPhotoFront));
//                    Log.e("TAG", "onViewClick: bodyPhotoBack >>>>>>>>>>> "+new Gson().toJson(bodyPhotoBack));
//
//                    Subscription subscription = NetworkRequest.performAsyncRequest(apiService.addProductReview(params, bodyPhotoFront, bodyPhotoBack)
//                            , response -> {
//                                hideProgress();
//                                Log.e("TAG", "onViewClick: >>>>>>>>>>>>>>>> "+response.isSuccessful() );
//                                if (response.isSuccessful()) {
//                                    ResponseCommon result = response.body();
//                                    if (result == null)
//                                        return;
//                                    Log.e("TAG", "onViewClick: response >>>>>>>>>>>>>>>> "+response);
//                                    Utils.makeToast(result.getMessage());
//                                    finish();
//                                }
//                                else
//                                {
//                                    Log.e("TAG", "onViewClick: error >>>>> "+response.message() );
//                                }
//                            }
//                            , throwable -> {
//                                hideProgress();
//                                throwable.printStackTrace();
//                            });
//                    compositeSubscription.add(subscription);


                new Thread(() -> {
                    try {
                        ArrayList<String> infoDataArray = new ArrayList<>();
                        String type = "";
                        String discription = "";
                        String have_pharmacy_insurance = "";

                        if (binding.llCheckyesLayout.isShown()) {
                            have_pharmacy_insurance = "yes";
                            if (binding.checkboxEitherEnterPharmacyInsurance.isChecked()) {
                                infoDataArray.add("rx_bin " + binding.etRxbin.getText().toString());
                                infoDataArray.add("rx_pcn " + binding.etRxpcn.getText().toString());
                                infoDataArray.add("rx_id " + binding.etRxid.getText().toString());
                                infoDataArray.add("rx_group " + binding.etRxgroup.getText().toString());
                                type = "pharmacy_insurance";
                                discription = binding.etDesc.getText().toString();
                            } else if (binding.checkboxScanPharmacy.isChecked()) {
                                infoDataArray.addAll(uploadImageArray);
                                type = "scan_pharmacy";
                                discription = binding.etDesc.getText().toString();
                            } else if (binding.checkboxSendScreen.isChecked()) {
                                infoDataArray.addAll(sendScreen_uploadImageArray);
                                type = "screenshot_pharmacy";
                                discription = binding.etDesc.getText().toString();
                            }
                        } else {
                            have_pharmacy_insurance = "no";
                            discription = binding.etDesc.getText().toString();
                        }

                        OkHttpClient client = new OkHttpClient().newBuilder().build();
                        MediaType mediaType = MediaType.parse("text/plain");
                        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                .addFormDataPart(Constants.pharmacy_id, result.getData().get(selectedPosition).getId().toString())
                                .addFormDataPart(Constants.pharmacy_locatore, "368952")
                                .addFormDataPart(Constants.image_front, imageFront, RequestBody.create(MediaType.parse("application/octet-stream"), new File(imageFront)))
                                .addFormDataPart(Constants.image_back, imageBack, RequestBody.create(MediaType.parse("application/octet-stream"), new File(imageBack)))
                                .addFormDataPart(Constants.have_pharmacy_insurance, have_pharmacy_insurance)
                                .addFormDataPart(Constants.type, type)
                                .addFormDataPart(Constants.info, String.valueOf(infoDataArray))
//                                    .addFormDataPart(Constants.info, imageBack.getPath(), RequestBody.create(MediaType.parse("application/octet-stream"), new File(imageBack.getPath())))
                                .addFormDataPart(Constants.description, discription)
                                .build();
                        Request request = new Request.Builder()
                                .url("http://165.22.45.58/api/add-prescription")
                                .method("POST", body)
                                .build();
                        Response response = client.newCall(request).execute();

                        Log.e("TAG", "onViewClick: response >>> " + response.body().string());

                        if (response.isSuccessful()) {
                            finish();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("TAG", "onViewClick: error >>>>>>>>>>>> " + e.getMessage());
                    }

                }).start();
            } else {
                Toast.makeText(getApplicationContext(), "Please choose a front and back image", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.radio_yes:
                if (checked) {
                    binding.llCheckyesLayout.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.radio_no:
                if (checked) {
                    binding.llCheckyesLayout.setVisibility(View.GONE);
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
                                if (isClickUploadImage) {
                                    uploadImageArray.add(data.getStringExtra(Constants.image));
                                    Collections.reverse(uploadImageArray);
                                    hospitallLocatorUploadImageAdapter.notifyDataSetChanged();
                                } else {
                                    sendScreen_uploadImageArray.add(data.getStringExtra(Constants.image));
                                    Collections.reverse(sendScreen_uploadImageArray);
                                    hospitallLocatorSendScreenUploadImageAdapter.notifyDataSetChanged();
                                }
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