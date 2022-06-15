package com.arnav.pocdoc.symptomchecker;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.arnav.pocdoc.BaseActivity;
import com.arnav.pocdoc.R;
import com.arnav.pocdoc.databinding.ActivitySymtomCheckerDobBinding;
import com.arnav.pocdoc.implementor.DialogDateClickListener;
import com.arnav.pocdoc.utils.DatePickerUtils;
import com.arnav.pocdoc.utils.Utils;

import java.util.Calendar;
import java.util.Date;

public class SymptomCheckerDOBActivity extends BaseActivity {

    private ActivitySymtomCheckerDobBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_symtom_checker_dob);
        binding.setActivity(this);
        setUpHeaderView();
    }

    private void setUpHeaderView() {
        binding.indicator.setStepCount(6);
        binding.indicator.setCurrentStep(4);
        binding.tvAge.setVisibility(View.GONE);

        if (!AddSymptomActivity.userDOB.equals("")) {
            printDifference(AddSymptomActivity.userDOB);
        }
    }

    public void onViewClick(View view) {
        if (view == binding.btnNext) {
            start(SymptomCheckerPatientActivity.class);
        } else if (view == binding.tvDD || view == binding.tvMM || view == binding.tvYYYY) {
            DatePickerUtils.openCustomDateDialog(this,
                    "", DatePickerUtils.DATE_DD_MM_YYYY_FORMAT,
                    Utils.getCurrentDate(DatePickerUtils.DATE_DD_MM_YYYY_FORMAT), DatePickerUtils.DATE_DD_MM_YYYY_FORMAT,
                    binding.tvDD.getText().toString().trim() + "/" + binding.tvMM.getText().toString().trim() + "/" +
                            binding.tvYYYY.getText().toString().trim(), DatePickerUtils.DATE_DD_MM_YYYY_FORMAT,
                    DatePickerUtils.DATE_DD_MM_YYYY_FORMAT, new DialogDateClickListener() {
                        @Override
                        public void onDateClick(String date) {
                            AddSymptomActivity.userDOB = date;
                            printDifference(date);
                        }

                        @Override
                        public void onCancelClick() {

                        }
                    });
        }
    }

    public void printDifference(String date) {
        Date selectedDate = Utils.parseDate(date, DatePickerUtils.DATE_DD_MM_YYYY_FORMAT);
        binding.tvDD.setText(Utils.GetDateOnRequireFormat(date, DatePickerUtils.DATE_DD_MM_YYYY_FORMAT, "dd"));
        binding.tvMM.setText(Utils.GetDateOnRequireFormat(date, DatePickerUtils.DATE_DD_MM_YYYY_FORMAT, "MM"));
        binding.tvYYYY.setText(Utils.GetDateOnRequireFormat(date, DatePickerUtils.DATE_DD_MM_YYYY_FORMAT, "yyyy"));
        binding.tvAge.setVisibility(View.VISIBLE);
        calculateAge(getAge(selectedDate), selectedDate);
    }

    private String getAge(Date dateAge) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.setTime(dateAge);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        int ageInt = age;
        return Integer.toString(ageInt);
    }

    private void calculateAge(String age, Date birthDate) {
        int years;
        int months;
        int days;

        //create calendar object for birth day
        Calendar birthDay = Calendar.getInstance();
        birthDay.setTimeInMillis(birthDate.getTime());

        //create calendar object for current day
        long currentTime = System.currentTimeMillis();
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(currentTime);

        //Get difference between years
        years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
        int currMonth = now.get(Calendar.MONTH) + 1;
        int birthMonth = birthDay.get(Calendar.MONTH) + 1;

        //Get difference between months
        months = currMonth - birthMonth;

        //if month difference is in negative then reduce years by one
        //and calculate the number of months.
        if (months < 0) {
            years--;
            months = 12 - birthMonth + currMonth;
            if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
                months--;
        } else if (months == 0 && now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
            years--;
            months = 11;
        }

        //Calculate the days
        if (now.get(Calendar.DATE) > birthDay.get(Calendar.DATE))
            days = now.get(Calendar.DATE) - birthDay.get(Calendar.DATE);
        else if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
            int today = now.get(Calendar.DAY_OF_MONTH);
            now.add(Calendar.MONTH, -1);
            days = now.getActualMaximum(Calendar.DAY_OF_MONTH) - birthDay.get(Calendar.DAY_OF_MONTH) + today;
        } else {
            days = 0;
            if (months == 12) {
                years++;
                months = 0;
            }
        }

        binding.tvAge.setText("Age: " + age + ", " +
                " Years: " + years + ", " +
                " Months:  " + months + ", " +
                " days:  " + days);
    }
}