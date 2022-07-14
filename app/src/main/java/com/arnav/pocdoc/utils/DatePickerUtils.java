package com.arnav.pocdoc.utils;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;

import com.arnav.pocdoc.implementor.DialogDateClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DatePickerUtils {

    public static final String DATE_MM_DD_YYYY_FORMAT = "MM/dd/yyyy";
    public static final String DATE_DD_MM_YYYY_FORMAT_ = "dd-MM-yyyy";
    public static final String TIME_HH_MM_SS_FORMAT = "HH:mm:ss";
    public static final String TIME_HH_MM_aa_FORMAT = "hh:mm a";
    public static final String DATE_DD_MM_YYYY_FORMAT = "dd/MM/yyyy";
    public static final String DATE_YYYY_MM_DD_FORMAT = "yyyy-MM-dd";
    public static final int TIME_PICKER_INTERVAL = 5;
    private static final String TAG = "DatePickerUtils";
    private static final String strReturnDateFormat = DATE_MM_DD_YYYY_FORMAT;
    private static final String strReturnTimeFormat = TIME_HH_MM_SS_FORMAT;
    // DATE PICKER
    private static Calendar calendar;
    private static int year, month, day;
    // TIME PICKER
    private static int hour, minute;

    public static void getDefaultDateDialog(Context context, final DialogDateClickListener listener) {
        openCustomDateDialog(context, "", "", "",
                "", "", "", strReturnDateFormat, listener);
    }

    public static void getFormatedDateDialog(Context context, String strReturnDateFormat
            , final DialogDateClickListener listener) {
        openCustomDateDialog(context, "", "", "",
                "", "", "", strReturnDateFormat, listener);
    }

    public static void getSelectedDateDialog(Context context, String strSelDate, String strSelDateFormat,
                                             String strReturnDateFormat, final DialogDateClickListener listener) {
        openCustomDateDialog(context, "", "", "", "",
                strSelDate, strSelDateFormat, strReturnDateFormat, listener);
    }

    public static void getMinDateDialog(Context context, String strSelDate, String strSelDateFormat,
                                        String strMinDate, String strMinDateFormat,
                                        String strReturnDateFormat, final DialogDateClickListener listener) {
        openCustomDateDialog(context, strMinDate, strMinDateFormat, "", "",
                strSelDate, strSelDateFormat, strReturnDateFormat, listener);
    }

    public static void getMaxDateDialog(Context context, String strSelDate, String strSelDateFormat,
                                        String strMaxDate, String strMaxDateFormat,
                                        String strReturnDateFormat, final DialogDateClickListener listener) {
        openCustomDateDialog(context, "", "", strMaxDate, strMaxDateFormat,
                strSelDate, strSelDateFormat, strReturnDateFormat, listener);
    }

    public static void openCustomDateDialog(Context context,
                                            String strMinDate, String strMinDateFormat,
                                            String strMaxDate, String strMaxDateFormat,
                                            String strSelDate, String strSelDateFormat,
                                            String strReturnDateFormat, final DialogDateClickListener listener) {
        calendar = Calendar.getInstance(Locale.ENGLISH);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        if (!strSelDate.equals("") && !strSelDate.equals("//")) {
            try {
                Date date = new SimpleDateFormat(strSelDateFormat, Locale.ENGLISH)
                        .parse(strSelDate);
                long timeInMillis = date.getTime();
                Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                calendar.setTimeInMillis(timeInMillis);
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        DatePickerDialog dpd = new DatePickerDialog(context,
                (view, year, monthOfYear, dayOfMonth) -> {
                    String date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                    try {
                        String strReturnDate = Utils.GetDateOnRequireFormat(date, DATE_DD_MM_YYYY_FORMAT_,
                                strReturnDateFormat);
                        if (listener != null) {
                            LogUtils.Print(TAG, "Selected date ==> " + strReturnDate);
                            listener.onDateClick(strReturnDate);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, year, (month), day);

        try {
            if (!strMinDate.equals("")) {
                Date selectedDate_min = new SimpleDateFormat(strMinDateFormat, Locale.ENGLISH)
                        .parse(strMinDate);
                dpd.getDatePicker().setMinDate(selectedDate_min.getTime());
            }

            if (!strMaxDate.equals("")) {
                Date selectedDate_max = new SimpleDateFormat(strMaxDateFormat, Locale.ENGLISH)
                        .parse(strMaxDate);
                dpd.getDatePicker().setMaxDate(selectedDate_max.getTime());
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        dpd.setOnCancelListener(dialog -> {
            if (listener != null) {
                listener.onCancelClick();
            }
        });

        dpd.show();
    }

    public static void openTimePickerDialog(Context context, String strSelTime, String strSelTimeFormat, String strReturnTimeFormat, final DialogDateClickListener listener) {
        if (!strSelTime.equalsIgnoreCase("")) {
            calendar = Utils.parseCalendar(strSelTime, strSelTimeFormat);
        } else {
            calendar = Calendar.getInstance(Locale.ENGLISH);
        }
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, (timePicker, selectedHour, selectedMinute) -> {

            Calendar datetime = Calendar.getInstance();
            datetime.set(Calendar.HOUR_OF_DAY, selectedHour);
            datetime.set(Calendar.MINUTE, selectedMinute);

            String startDur = "";
            if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                startDur = "AM";
            else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                startDur = "PM";

            String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ? "12" : datetime.get(Calendar.HOUR) + "";
            String min = selectedMinute <= 9 ? "0" + selectedMinute : "" + selectedMinute;
            String strTime = strHrsToShow + ":" + min + " " + startDur;
            strTime = Utils.GetDateOnRequireFormat(strTime, TIME_HH_MM_aa_FORMAT,
                    strReturnTimeFormat);
            if (listener != null) {
                LogUtils.Print(TAG, "Selected date ==> " + strTime);
                listener.onDateClick(strTime);
            }

        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Start Time");

        mTimePicker.setOnCancelListener(dialog -> {
            if (listener != null) {
                listener.onCancelClick();
            }
        });

        mTimePicker.show();
    }
}