package com.arnav.pocdoc.utils;

import android.Manifest;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;


import com.arnav.pocdoc.R;
import com.arnav.pocdoc.implementor.DialogButtonClickListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class CameraGalleryActivity extends AppCompatActivity {

    private static final String TAG = "CameraGalleryActivity";
    public static IMAGE image = IMAGE.ALL;
    private final int CAMERA_REQUEST = 0, ACTION_REQUEST_GALLERY = 1;
    //image upload
    private String imagePath = "";
    private Uri mImageCaptureUri;
    private File file;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_permission);
        chkPermission();
    }

    /**
     * CHECK PERMISSION FOR READ,WRITE STORAGE AND CAMERA
     */
    private void chkPermission() {
        if (!(Utils.checkPermission(CameraGalleryActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                Utils.checkPermission(CameraGalleryActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE))) {
            Utils.showPermissionsDialog(CameraGalleryActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.REQUEST_CODE_STORAGE_PERMISSION);
            return;
        }
        if (!(Utils.checkPermission(CameraGalleryActivity.this, Manifest.permission.CAMERA))) {
            Utils.showPermissionsDialog(CameraGalleryActivity.this, new String[]{Manifest.permission.CAMERA},
                    Constants.REQUEST_CODE_CAMERA_PERMISSION);
            return;
        }
        permissionGrantedSuccessFully();
    }

    private void permissionGrantedSuccessFully() {
        if (image == IMAGE.GALLERY) {
            launchGallery();
        } else if (image == IMAGE.CAMERA) {
            launchCamera();
        } else {
            showImageChooserDialog();
        }
    }

    private void resultFailed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void resultSuccess(String imagePath) {
        Intent intent = new Intent();
        intent.putExtra(Constants.image, imagePath);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void resultSuccess(ArrayList<String> imagePaths) {
        Intent intent = new Intent();
        intent.putExtra(Constants.images, imagePaths);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**************************************************************************************
     * ********************************* CAMERA *******************************************
     * ************************************************************************************
     * OPEN IMAGE CHOOSE DIALOG
     */
    private void showImageChooserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CameraGalleryActivity.this,
                R.style.MyAlertDialogStyle);
        CharSequence[] option = new CharSequence[]{getResources().getString(R.string.text_gallery),
                getResources().getString(R.string.text_camera)};
        builder.setItems(option, (dialog, which) -> {
            if (which == 0) {
                dialog.cancel();
                launchGallery();
            } else {
                dialog.cancel();
                launchCamera();
            }
        }).setPositiveButton(getResources().getString(R.string.text_cancel), (dialog, id) -> {
            dialog.cancel();
            resultFailed();
        });
        builder.setCancelable(false);
        builder.show();
    }

    /**
     * LAUNCH GALLERY FOR CHOOSE IMAGE
     */
    public void launchGallery() {
        Intent i = new Intent(
                Intent.ACTION_GET_CONTENT,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(i, ACTION_REQUEST_GALLERY);
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), ACTION_REQUEST_GALLERY);

    }

    /**
     * open camera to capture image
     */
    public void launchCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String filename = "IMG_AK_" + System.currentTimeMillis() + ".jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
//        file = new File(Constants.APP_ROOT_FOLDER
//                + "/" + filename);

        file = new File(getExternalCacheDir(), filename);
        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            mImageCaptureUri = FileProvider.getUriForFile(this,
                    getPackageName() + ".provider",
                    file);
        } else {
            mImageCaptureUri = Uri.fromFile(file);
        }
        imagePath = file.getAbsolutePath();
        cameraIntent.putExtra(
                MediaStore.EXTRA_OUTPUT,
                mImageCaptureUri);

        startActivityForResult(cameraIntent,
                CAMERA_REQUEST);
    }

    /**
     * OPEN CROP ACTIVITY FOR CROP IMAGE
     *
     * @param source
     */
    private void beginCrop(Uri source) {
        LogUtils.Print(TAG, "....BEGIN CROP....");
        File file = null;
        file = new File(Constants.IMAGE_ROOT_FOLDER);
        file.mkdirs();

        file = new File(Constants.IMAGE_ROOT_FOLDER + "/" + Constants.IMAGE_FILE_NAME_PREFIX.replace("X", Utils.GetCurrentNanoTime()));

        LogUtils.Print(TAG, "file::" + file);
        Uri destination = Uri.fromFile(file);
        // Crop.of(source, destination).asSquare().start(this);
        startCrop(source);
    }

    /**
     * LAUNCH CAMERA FOR CAPTURE IMAGE
     */
    /*public void launchCamera() {
        Intent cameraIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            String filename = "IMG_AK_" + System.currentTimeMillis() + ".jpg";
            ContentValues values = new ContentValues();
//        values.put(MediaStore.Images.Media.TITLE, filename);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");

            File fileRoot = new File(Constants.IMAGE_ROOT_FOLDER);
            fileRoot.mkdirs();

            File file = new File(Constants.IMAGE_ROOT_FOLDER + "/" + filename);

            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mImageCaptureUri = getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                mImageCaptureUri = FileProvider.getUriForFile(CameraGalleryActivity.this,
//                        getPackageName() + ".provider",
//                        file);
//            } else {
//                mImageCaptureUri = Uri.fromFile(file);
//            }
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }*/

    /**
     * START ACTIVITY
     *
     * @param imageUri
     */
    private void startCrop(Uri imageUri) {
//        LogUtils.Print(TAG, "IMAGE URI..." + imageUri);
//        Intent intent = new Intent(CameraGalleryActivity.this, CropActivity.class);
//        intent.setData(imageUri);
//        intent.putExtra(Constants.FLAG_IS_SQUARE, true);
//        startActivityForResult(intent, Constants.FLAG_CROP);
    }

    /**
     * GET ABSOLUTE PATH FROM URI
     *
     * @param contentUri
     * @return
     */
    public String getRealPathFromURI1(Uri contentUri) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return contentUri.getPath();
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.Print(TAG, "requestCode=======" + requestCode);
        LogUtils.Print(TAG, "resultCode=======" + resultCode);
        LogUtils.Print(TAG, "onActivityResult=======" + data);
        switch (requestCode) {
            case Constants.REQUEST_CODE_PERMISSION_RESULT:
                if (Utils.checkPermission(CameraGalleryActivity.this, Manifest.permission.CAMERA) &&
                        Utils.checkPermission(CameraGalleryActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                        Utils.checkPermission(CameraGalleryActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    permissionGrantedSuccessFully();
                } else {
                    resultFailed();
                }
                break;
            case CAMERA_REQUEST:
                if (resultCode == RESULT_OK) {
                    try {
                        LogUtils.Print(TAG, "INSIDE CAMERA RESULT=======" + mImageCaptureUri);
//                        imagePath = Utils.getRealPathFromURI(mImageCaptureUri, this);
                        if (image != IMAGE.ALL) {
                            resultSuccess(imagePath);
                        } else {
                            beginCrop(mImageCaptureUri);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Utils.showSnackBar(getResources().getString(R.string.dialog_file_not_found), this);
                        resultFailed();
                    }
                } else {
                    resultFailed();
                }
                break;
            case ACTION_REQUEST_GALLERY:
                if (resultCode == RESULT_OK) {
                    LogUtils.Print(TAG, "INSIDE GALLERY RESULT=======");
                    try {
                        if (data.getClipData() != null) {
                            ClipData mClipData = data.getClipData();
                            if (data.getClipData().getItemCount() > 5) {
                                Utils.makeToast("You can not select more than 5 images. Please deselect another image before trying to select again.");
                                launchGallery();
                            } else {
                                ArrayList<Uri> mArrayUri = new ArrayList<>();
                                ArrayList<String> imagePaths = new ArrayList<>();
                                for (int i = 0; i < mClipData.getItemCount(); i++) {

                                    ClipData.Item item = mClipData.getItemAt(i);
                                    Uri mGalleryImageCaptureUri = item.getUri();
                                    mImageCaptureUri = item.getUri();
//                                imagePath = Utils.getRealPathFromURI(mGalleryImageCaptureUri, this);
                                    imagePath = getImageFilePath(mGalleryImageCaptureUri);
                                    imagePaths.add(imagePath);
                                }
                                Collections.reverse(imagePaths);
                                if (image != IMAGE.ALL) {
                                    resultSuccess(imagePaths);
                                } else {
                                    beginCrop(mImageCaptureUri);
                                }
                                LogUtils.Print(TAG, "IMAGE PATH GALLERY.." + imagePath);
                                LogUtils.Print("LOG_TAG", "Selected Images" + mArrayUri.size());
                            }
                        }
                        if (data.getData() != null) {
                            Uri mGalleryImageCaptureUri = data.getData();
                            mImageCaptureUri = data.getData();
//                            imagePath = Utils.getRealPathFromURI(mGalleryImageCaptureUri, this);
                            imagePath = getImageFilePath(mGalleryImageCaptureUri);
                            if (image != IMAGE.ALL) {
                                resultSuccess(imagePath);
                            } else {
                                beginCrop(mImageCaptureUri);
                            }
                            LogUtils.Print(TAG, "IMAGE PATH GALLERY.." + imagePath);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Utils.showSnackBar(getResources().getString(R.string.dialog_file_not_found), this);
                        resultFailed();
                    }
                } else {
                    resultFailed();
                }
                break;
            case Constants.FLAG_CROP:
                if (resultCode == RESULT_OK) {
                    try {
                        imagePath = data.getStringExtra(Constants.image);
                        LogUtils.Print(TAG, "IMAGE PATH GALLERY.." + imagePath);
                        file = new File(imagePath);
                        if (file.exists()) {
                            resultSuccess(imagePath);
                        } else {
                            imagePath = "";
                            Utils.showSnackBar(getResources().getString(R.string.dialog_file_not_found), this);
                            resultFailed();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Utils.showSnackBar(getResources().getString(R.string.dialog_file_not_found), this);
                        resultFailed();
                    }
                } else {
                    resultFailed();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LogUtils.Print(TAG, "requestCode=======" + requestCode);
        switch (requestCode) {
            case Constants.REQUEST_CODE_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chkPermission();
                } else {
                    DialogUtils.showConfirmationDialog(CameraGalleryActivity.this,
                            new DialogButtonClickListener() {
                                @Override
                                public void onPositiveButtonClick() {
                                    if (isCameraPermissionDeny()) {//retry
                                        chkPermission();
                                    } else {//setting
                                        navigateUserToAppSettingDetailsScreen();
                                    }
                                }

                                @Override
                                public void onNegativeButtonClick() {//i am sure
                                    resultFailed();
                                }
                            }, getResources().getString(R.string.text_camera_permission_msg_profile),
                            isCameraPermissionDeny() ?
                                    getResources().getString(R.string.text_retry) :
                                    getResources().getString(R.string.text_settings),
                            getResources().getString(R.string.text_i_am_sure));
                }
                break;
            case Constants.REQUEST_CODE_STORAGE_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chkPermission();
                } else {
                    DialogUtils.showConfirmationDialog(CameraGalleryActivity.this,
                            new DialogButtonClickListener() {
                                @Override
                                public void onPositiveButtonClick() {
                                    if (isStoragePermissionDeny()) {//retry
                                        chkPermission();
                                    } else {//setting
                                        navigateUserToAppSettingDetailsScreen();
                                    }
                                }

                                @Override
                                public void onNegativeButtonClick() {//i am sure
                                    resultFailed();
                                }
                            }, getResources().getString(R.string.text_save_profile_picture_msg),
                            isStoragePermissionDeny() ?
                                    getResources().getString(R.string.text_retry) :
                                    getResources().getString(R.string.text_settings),
                            getResources().getString(R.string.text_i_am_sure));
                }
            }
        }
    }

    private boolean isCameraPermissionDeny() {
        return Utils.shouldShowRequestPermissionRationale(CameraGalleryActivity.this,
                Manifest.permission.CAMERA);
    }

    private boolean isStoragePermissionDeny() {
        return Utils.shouldShowRequestPermissionRationale(CameraGalleryActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void navigateUserToAppSettingDetailsScreen() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, Constants.REQUEST_CODE_PERMISSION_RESULT);
    }

    private String getImageFilePath(Uri uri) {
        String path = "";
        File file = new File(uri.getPath());
        String[] filePath = file.getPath().split(":");
        String image_id = filePath[filePath.length - 1];

        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{image_id}, null);
        if (cursor != null) {
            cursor.moveToFirst();
            imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            path = imagePath;
            cursor.close();
        }
        return path;
    }

    public enum IMAGE {
        ALL,
        CAMERA,
        GALLERY,
        AllNoCrop
    }
}
