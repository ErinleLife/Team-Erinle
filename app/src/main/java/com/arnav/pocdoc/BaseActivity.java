package com.arnav.pocdoc;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.arnav.pocdoc.utils.ProgressDialog;
import com.arnav.pocdoc.utils.Utils;

import java.util.List;

abstract public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    }

    public void showProgress(ProgressBar progressBar, boolean isVisible) {
        if (isVisible) progressBar.setVisibility(View.VISIBLE);
        else progressBar.setVisibility(View.GONE);
    }

    /**
     * DISABLE VIEW THAT NEED TO BE DURING PROGRESS
     *
     * @param isVisible
     */
    protected void disableView(List<View> viewList, Boolean isVisible) {
        if (viewList != null && viewList.size() > 0) {
            for (int i = 0; i < viewList.size(); i++) {
                viewList.get(i).setAlpha((isVisible) ? 0.5f : 1f);
                viewList.get(i).setClickable(!isVisible);
            }
        }
    }

    /**
     * SHOW/HIDE PROGRESSBAR
     *
     * @param isVisible
     */
    protected void showProgressBar(View view, Boolean isVisible, List<View> viewList) {
        if (isVisible) view.setVisibility(View.VISIBLE);
        else view.setVisibility(View.GONE);
        disableView(viewList, isVisible);
    }


    /**
     * SET TITLE
     *
     * @param stringResourceId
     */
    protected void setHeaderTitle(int stringResourceId, TextView textView) {
        if (textView != null) {
            textView.setText(getString(stringResourceId));
        }
    }

    protected void setHeaderTitleString(String stringResourceId, TextView textView, ImageView imageView) {
        if (textView != null) {
            textView.setText(stringResourceId);
        }
        if (imageView != null) {
            imageView.setOnClickListener(view -> onBackPressed());
        }
    }

    protected void setHeaderTitle(int stringResourceId, TextView textView, ImageView imageView) {
        if (textView != null) {
            textView.setText(getString(stringResourceId));
        }
        if (imageView != null) {
            imageView.setOnClickListener(view -> {
                onBackPressed();
            });
        }
    }

    protected void setBackPressListener(ImageView imageView) {
        if (imageView != null) {
            imageView.setOnClickListener(view -> onBackPressed());
        }
    }

    /**
     * SHOW ERROR MSG
     *
     * @param isVisible
     */
    protected void showErrorMsg(View view, Boolean isVisible) {
        if (isVisible) view.setVisibility(View.VISIBLE);
        else view.setVisibility(View.GONE);

    }

    @Override
    public void applyOverrideConfiguration(Configuration overrideConfiguration) {
        if (overrideConfiguration != null) {
            int uiMode = overrideConfiguration.uiMode;
            overrideConfiguration.setTo(getBaseContext().getResources().getConfiguration());
            overrideConfiguration.uiMode = uiMode;
        }
        super.applyOverrideConfiguration(overrideConfiguration);
    }

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(MyContextWrapper.wrap(newBase, baseApplication.locale));
//        super.attachBaseContext(BaseApplication.updateResources(newBase, Utils.getString(Constants.language)));
//        super.attachBaseContext(LocaleHelper.onAttach(newBase));
//    }

    public void start(Class<? extends Activity> activity) {
        startActivity(new Intent(this, activity));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    public void startWithClearStack(Class<? extends Activity> activity) {
        startActivity(new Intent(this, activity).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        this.finish();
    }

    private void navLoginScreen() {
//        start(LoginActivity.class);
    }

    public void showProgress(SwipeRefreshLayout srl) {
        if (!srl.isRefreshing()) {
            showProgress();
        }
    }

    public void hideProgress(SwipeRefreshLayout srl) {
        if (srl.isRefreshing()) {
            srl.setRefreshing(false);
        }
        hideProgress();
    }

    public void showProgress() {
        ProgressDialog.getInstance().show(this);
    }

    public void hideProgress() {
        ProgressDialog.getInstance().dismiss();
    }

    public void showMessage(String message) {
        Utils.makeToast(this, message);
    }

    protected void showSMessage(String message) {
        Utils.showSnackBar(message, this);
    }
}