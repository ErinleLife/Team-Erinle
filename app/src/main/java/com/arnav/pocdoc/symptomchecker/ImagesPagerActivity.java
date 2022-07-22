package com.arnav.pocdoc.symptomchecker;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.viewpager.widget.ViewPager;

import com.arnav.pocdoc.R;
import com.arnav.pocdoc.databinding.ActivityImagesPagerBinding;
import com.arnav.pocdoc.symptomchecker.adapter.ImagesPagerAdapter;
import com.arnav.pocdoc.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class ImagesPagerActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    //Data binding
    private ActivityImagesPagerBinding binding;
    private MutableLiveData<String> liveData;

    private int position;
    private List<String> list = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_images_pager);
        liveData = new MutableLiveData<>();
        binding.setActivity(this);
        getDataFromIntent();
        setUpViewPager();
    }

    /**
     * GET DATA FROM INTENT
     */
    private void getDataFromIntent() {
        if (getIntent() != null) {
            position = getIntent().getIntExtra(Constants.position, -1);
            list = getIntent().getExtras().getStringArrayList(Constants.data);
//            if (list != null && list.size() > 0 && list.get(list.size() - 1).isLastImage()) {
//                list.remove(list.size() - 1);
//            }
        }
    }

    /**
     * SET UP VIEW PAGER
     */
    private void setUpViewPager() {
        binding.pager.setAdapter(new ImagesPagerAdapter(list, this));
        binding.pager.setCurrentItem(position);
        binding.pager.addOnPageChangeListener(this);

        liveData.setValue((position + 1) + "/" + list.size());
        binding.setData(liveData);
    }

    /**
     * SET UP CLICK EVENT
     *
     * @param view
     */
    public void performClick(View view) {
        if (view.getId() == R.id.tvClose) {
            onBackPressed();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        this.position = position;
        liveData.setValue((position + 1) + "/" + list.size());
        binding.setData(liveData);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
