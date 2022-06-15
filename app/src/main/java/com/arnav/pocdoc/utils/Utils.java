package com.arnav.pocdoc.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.arnav.pocdoc.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static final String NUMBERS_AND_LETTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String NUMBERS = "0123456789";
    public static final String LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String CAPITAL_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LOWER_CASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String TAG = "Utils";
    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    public static String PREFERENCE_NAME = "GLOBAL_DATA";

    private static Context context;

    public Utils(Context context) {
        Utils.context = context;
    }

    /**
     * ***************************************************************************
     * ****************************** MESSAGE UTILS ******************************
     * ***************************************************************************
     */
    public static void setErrorOnTextInputLayout(TextInputLayout textInputLayout, String text) {
        if (textInputLayout != null) {
            if (text != null) {
                textInputLayout.setErrorEnabled(true);
                textInputLayout.setError(text);
            } else {
                textInputLayout.setError(null);
                textInputLayout.setErrorEnabled(false);
            }
        }
    }

    //Show Toast
    public static void makeToast(Context ctx, String text) {
        if (!isEmpty(text)) {
            Toast.makeText(ctx, text, Toast.LENGTH_SHORT).show();
        }
    }

    public static void makeToast(String text) {
        if (!isEmpty(text) && context != null) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        }
    }

    //SnackBar
    public static void showSnackBar(String text, View view) {
        if (view != null) {
            Snackbar snackbar;
            snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorPrimary));
            TextView textView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setTextColor(ContextCompat.getColor(view.getContext(), R.color.white));
            snackbar.show();
        }
    }

    //SnackBar
    public static void showSnackBar(String text, Activity activity) {
        if (activity != null && !activity.isFinishing() &&
                activity.findViewById(android.R.id.content) != null) {
            Snackbar snackbar;
            snackbar = Snackbar.make(activity.findViewById(android.R.id.content), text, Snackbar.LENGTH_SHORT);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorPrimary));
            TextView textView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setTextColor(ContextCompat.getColor(activity, R.color.white));
            snackbar.show();
        }
    }

    //SnackBarIndefinite
    public static void showSnackBarIndefinite(String text, View view) {
        Snackbar.make(view, text, Snackbar.LENGTH_INDEFINITE).show();
    }

    /**
     * ***************************************************************************
     * **************************** VALIDATION UTILS *****************************
     * ***************************************************************************
     */
    public static boolean isValidEmail(String email) {
        boolean check;
        Pattern p;
        Matcher m;

        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        p = Pattern.compile(EMAIL_STRING);

        m = p.matcher(email);
        check = m.matches();

        return check;
    }

    public static boolean isValidPassword(String password) {
        boolean check;
        Pattern p;
        Matcher m;

        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
        p = Pattern.compile(passwordRegex);
        m = p.matcher(password);
        check = m.matches();
        return check;
    }

    public static boolean isValidName(String name) {
        boolean check;
        Pattern p;
        Matcher m;

        String NAME_STRING = "[a-zA-z]+([ '-][a-zA-Z]+)*";

        p = Pattern.compile(NAME_STRING);

        m = p.matcher(name);
        check = m.matches();

        return check;
    }

    public static boolean isEmpty(CharSequence str) {
        return (str == null || str.length() == 0);
    }

    //attachment file validation
    public static boolean checkFileIsAttachmentFile(String str) {
        boolean check;
        Pattern p;
        Matcher m;
        // String PHONE_STRING = "^.*\.(jpg|JPG|gif|GIF|doc|DOC|pdf|PDF)$";
        String FILE_STRING = "^.*\\.(doc|DOC|pdf|PDF)$";
        p = Pattern.compile(FILE_STRING);
        m = p.matcher(str);
        check = m.matches();
        return check;
    }

    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";

        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));

        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) +
                " " + units[digitGroups];
    }

    public static String removeLastChar(String str, char sign) {
        if (!str.equalsIgnoreCase("") && str.length() > 0 && str.charAt(str.length() - 1) == sign) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public static String removeLastChar(String str) {
        return removeLastChar(str.trim(), ',');
    }

    /**
     * ***************************************************************************
     * **************************** KEYBOARD UTILS *****************************
     * ***************************************************************************
     */
    //hide keyboard
    public static void hideKeyBoard(EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager
                .hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static void hideKeyBoard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showSoftKeyboard(View view) {
        showSoftKeyboard(view, null);
    }

    public static void showSoftKeyboard(View view, ResultReceiver resultReceiver) {
        Configuration config = view.getContext().getResources()
                .getConfiguration();
        if (config.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            InputMethodManager imm = (InputMethodManager) view.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);

            if (resultReceiver != null) {
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT,
                        resultReceiver);
            } else {
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }

    public static void changeKeyboardFocus(EditText editText) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * ***************************************************************************
     * ******************************* DATE UTILS ********************************
     * ***************************************************************************
     */
    public static String GetDateOnRequireFormat(String date,
                                                String givenformat, String resultformat) {
        String result = "";
        SimpleDateFormat sdf;
        SimpleDateFormat sdf1;
        try {
            if (date != null) {
                sdf = new SimpleDateFormat(givenformat, Locale.US);
                sdf1 = new SimpleDateFormat(resultformat, Locale.US);
                result = sdf1.format(sdf.parse(date));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            sdf = null;
            sdf1 = null;
        }
        return result;
    }

    private static int getTimeDistanceInMinutesForTotalTime(long time, long now) {
        long timeDistance = now - time;
        return Math.round((Math.abs(timeDistance) / 1000) / 60);
    }

    private static int getTimeDistanceInMinutes(long time) {
        long timeDistance = currentDate().getTime() - time;
        return Math.round((Math.abs(timeDistance) / 1000) / 60);
    }

    public static Calendar getDateFromTimeStampInDate(String milliSeconds) {
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.valueOf(milliSeconds));
        return calendar;
    }

    public static Date currentDate() {
       /* TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Calendar calendar = Calendar.getInstance(timeZone);
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(Constants.DATE_FULL_FORMAT, Locale.US);
        simpleDateFormat.setTimeZone(timeZone);
        return calendar.getTime();*/
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss", Locale.ENGLISH);
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));

//Local time zone
        SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss", Locale.ENGLISH);

//Time in GMT
     /*   try {
            return dateFormatLocal.parse(dateFormatGmt.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        //return new Date();
        return Calendar.getInstance().getTime();
    }

    public static boolean checkTimeBetweenTwoTimes(String strCTime, String fromTime, String toTime, String format) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);

            Date time1 = dateFormat.parse(fromTime);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(time1);

            Date time2 = dateFormat.parse(toTime);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(time2);

            Date d = dateFormat.parse(strCTime);
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(d);

            Date x = calendar3.getTime();
            if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean dateIsBeforeThanProvidedDate(String date_first, String date_second, String dateformat) {
        boolean is_before = false;
        try {

            SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
            Date date1 = sdf.parse(date_first);
            Date date2 = sdf.parse(date_second);

            System.out.println(sdf.format(date1));
            System.out.println(sdf.format(date2));

            if (date1.compareTo(date2) > 0) {
                is_before = true;
            }
            LogUtils.Print("DateIsBeforeThanProvidedDate", "date1.compareTo(date2)..." + date1.compareTo(date2));


        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        LogUtils.Print("is_before", "is_before..." + is_before);
        LogUtils.Print("DateIsBeforeThanProvidedDate", "date_first..." + date_first);
        LogUtils.Print("DateIsBeforeThanProvidedDate", "date_second..." + date_second);

        return is_before;
    }

    public static boolean dateIsBeforeStrickThanProvidedDate(String date_first, String date_second, String dateformat) {
        boolean is_before = false;
        try {

            SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
            Date date1 = sdf.parse(date_first);
            Date date2 = sdf.parse(date_second);

            System.out.println(sdf.format(date1));
            System.out.println(sdf.format(date2));

            if (date1.compareTo(date2) >= 0) {
                is_before = true;
            }


        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        return is_before;
    }

    public static boolean dateIsBeforeThanOrEqualProvidedDate(String date_first, String date_second, String dateformat) {
        boolean is_before = false;
        try {

            SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
            Date date1 = sdf.parse(date_first);
            Date date2 = sdf.parse(date_second);

            System.out.println(sdf.format(date1));
            System.out.println(sdf.format(date2));

            if (date1.compareTo(date2) <= 0) {
                is_before = true;
            }


        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        return is_before;
    }

    public static boolean isEqualDate(String date_first, String date_second, String dateformat) {
        boolean is_equal = false;
        try {

            SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
            Date date1 = sdf.parse(date_first);
            Date date2 = sdf.parse(date_second);

            System.out.println(sdf.format(date1));
            System.out.println(sdf.format(date2));

            if (date1.compareTo(date2) == 0) {
                is_equal = true;
            }


        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        return is_equal;
    }

    public static String GetCurrentTimeStamp() {
        Long tsLong = System.currentTimeMillis();
        return tsLong.toString();
    }

    public static Long GetCurrentTimeStampLong() {
        return System.currentTimeMillis();
    }

    public static long GetCurrentTimeStampInLong() {
        return System.currentTimeMillis();
    }

    public static int GetTimeFromLocalToUTC(long time) {
        int totalSecond = 0;
        try {
//            SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_YYYY_MM_DD_HH_MM_SS_FORMAT, Locale.ENGLISH);
            Date date = new Date(time);
//            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
//            Date utcDate = parseDate(dateFormat.format(date), Constants.DATE_YYYY_MM_DD_HH_MM_SS_FORMAT);
//            LogUtils.Print(TAG, "utcDate => " + utcDate);

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int hours = cal.get(Calendar.HOUR_OF_DAY);
            int minutes = cal.get(Calendar.MINUTE);
            int second = cal.get(Calendar.SECOND);
            if (hours != 0) {
                totalSecond += (hours * 3600);
            }
            if (minutes != 0) {
                totalSecond += (minutes * 60);
            }
            totalSecond += second;
            return totalSecond;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalSecond;
    }

    public static String GetUTCToLocalDateFromSeconds(long time, String dateFormat) {
        SimpleDateFormat df = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time * 1000);
        Date date = null;
        try {
            date = df.parse(df.format(calendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        df.setTimeZone(TimeZone.getDefault());
        return df.format(date);
    }

    public static String GetUTCToLocalDateFromMilliSeconds(long time, String dateFormat) {
        SimpleDateFormat df = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = df.parse(df.format(new Date(time)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        df.setTimeZone(TimeZone.getDefault());
        return df.format(date);
    }

    public static String getDifferentTimezone() {
        TimeZone tz = TimeZone.getDefault();
        Calendar cal = GregorianCalendar.getInstance(tz);
        int offsetInMillis = tz.getOffset(cal.getTimeInMillis());
        String sign = offsetInMillis >= 0 ? "+" : "-";
        return String.format(sign + "%02d hours " + sign + "%02d minutes", Math.abs(offsetInMillis / 3600000), Math.abs((offsetInMillis / 60000) % 60));
    }

    public static String getDifferentTimezoneActual() {
        TimeZone tz = TimeZone.getDefault();
        Calendar cal = GregorianCalendar.getInstance(tz);
        int offsetInMillis = tz.getOffset(cal.getTimeInMillis());
        String sign = offsetInMillis >= 0 ? "-" : "+";//reverse sign as per logic
        return String.format(sign + "%02d hours " + sign + "%02d minutes", Math.abs(offsetInMillis / 3600000), Math.abs((offsetInMillis / 60000) % 60));
    }

    public static HashMap<String, Integer> getEventTimezoneForDB() {
        TimeZone tz = TimeZone.getDefault();
        Calendar cal = GregorianCalendar.getInstance(tz);
        int offsetInMillis = tz.getOffset(cal.getTimeInMillis());
        String sign = offsetInMillis >= 0 ? "+" : "-";
        int hr = Integer.parseInt((sign + Math.abs(offsetInMillis / 3600000)));
        int min = Integer.parseInt((sign + Math.abs((offsetInMillis / 60000) % 60)));
        HashMap<String, Integer> hashMap = new HashMap<>();
        hashMap.put("hour", hr);
        hashMap.put("minute", min);
        return hashMap;
    }

    public static String utcToLocalDateInString(String dateStr, String strFormat, String strReturnFormat) {
        SimpleDateFormat df = new SimpleDateFormat(strFormat, Locale.ENGLISH);
        Date date = null;
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            LogUtils.Print(TAG, "utcToLocalDateInString: " + e.getMessage());
        }
        LogUtils.Print(TAG, "date: " + date);

        SimpleDateFormat dfr = new SimpleDateFormat(strReturnFormat, Locale.ENGLISH);
        return dfr.format(date);
    }

    public static String utcToLocalDateInString1(String dateStr, String strFormat, String strReturnFormat) {
        SimpleDateFormat df = new SimpleDateFormat(strFormat, Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            LogUtils.Print(TAG, "utcToLocalDateInString: " + e.getMessage());
        }
        LogUtils.Print(TAG, "date: " + date);

        SimpleDateFormat dfr = new SimpleDateFormat(strReturnFormat, Locale.ENGLISH);
        dfr.setTimeZone(TimeZone.getDefault());
        return dfr.format(date);
    }

    private static String getWeeklyDay(String strDay) {
        switch (strDay) {
            case "Sunday":
                return "2021-04-18";
            case "Monday":
                return "2021-04-19";
            case "Tuesday":
                return "2021-04-20";
            case "Wednesday":
                return "2021-04-21";
            case "Thursday":
                return "2021-04-22";
            case "Friday":
                return "2021-04-23";
            case "Saturday":
            default:
                return "2021-04-17";
        }
    }

    public static long GetCurrentUTCTimeStamp(String strDat, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        long timeInMilliseconds = 0;
        try {
            Date mDate = sdf.parse(strDat);
            sdf.setTimeZone(TimeZone.getDefault());
            timeInMilliseconds = mDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeInMilliseconds;
    }

    public static Date getOneDayMinusFromStringDate(String strDate, String format) {
        final SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        Date date = null;
        try {
            date = sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, 15);
        cal.add(Calendar.DATE, -1);
        LogUtils.Print(TAG, "One day --> " + cal.getTime());
        return cal.getTime();
    }

    public static int getDateFromString(String strDate, String format) {
        final SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        Date date = null;
        try {
            date = sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int age = Calendar.getInstance().get(Calendar.YEAR) - cal.get(Calendar.YEAR);
        if (age < 18)
            return 1;
        else if (age > 18 && age < 35)
            return 2;
        else if (age > 35 && age < 50)
            return 3;
        else return 4;
    }

    public static Date getCalendarFromString(String strDate, String format) {
        final SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        Date date = null;
        try {
            date = sdf.parse(strDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getWeekDayFromString(String strDate, String dateFormat, String dayFormat) {
        SimpleDateFormat inFormat = new SimpleDateFormat(dateFormat);
        Date date = null;
        try {
            date = inFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat outFormat = new SimpleDateFormat(dayFormat);
        return outFormat.format(date);
    }

    public static String utcToLocalDateInString(Date sourceDate, String strFormat) {
        SimpleDateFormat df = new SimpleDateFormat(strFormat, Locale.ENGLISH);
        Date date = null;
        try {
            date = df.parse(df.format(sourceDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return df.format(date);
    }

    public static String utcToLocalDateInString1(Date sourceDate, String strFormat) {
        SimpleDateFormat df = new SimpleDateFormat(strFormat, Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = df.parse(df.format(sourceDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        df.setTimeZone(TimeZone.getDefault());
        return df.format(date);
    }

    public static String GetDefaultTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        try {
            return "" + tz.getID();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String GetCurrentNanoTime() {
        Long tsLong = System.nanoTime();
        String ts = tsLong.toString();
        return ts;
    }

    public static String GetDateFromTimeStamp(long milliSeconds, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static String getTimeFromMilliSeconds(long milliSeconds) {
        // long minutes = (milliseconds / 1000) / 60;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliSeconds);
        // long seconds = (milliseconds / 1000);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliSeconds);

        return String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds);
    }

    public static String addDayInDate(String dt, String dateFormat, int num) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, 1);
        return sdf.format(c.getTime());
    }

    public static Calendar getnthDayOfCurrentMonth(int day) {
        Calendar c = Calendar.getInstance();   // this takes current date
        c.set(Calendar.DAY_OF_MONTH, day);
        System.out.println(c.getTime());       // this returns java.util.Date
        LogUtils.Print(TAG, "getFirstDayOfCurrentMonth --> " + c.getTime());
        return c;
    }

    public static Calendar getLastDayOfCurrentMonth() {
        Calendar c = Calendar.getInstance();   // this takes current date
        c.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
        System.out.println(c.getTime());       // this returns java.util.Date
        LogUtils.Print(TAG, "getLastDayOfCurrentMonth --> " + c.getTime());
        return c;
    }

    public static String getStringDateFromCalendar(Calendar calendar, String inputFormat) {
        SimpleDateFormat format1 = new SimpleDateFormat(inputFormat, Locale.ENGLISH);
        LogUtils.Print(TAG, "getStringDateFromCalendar --> " + calendar.getTime());
        return format1.format(calendar.getTime());
    }

    public static String getDateFromLocalDate(LocalDate localDate, String dateFormat) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
            Date date = new SimpleDateFormat(dateFormat, Locale.ENGLISH).parse(localDate.toString());
            df.setTimeZone(TimeZone.getDefault());
            return df.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getCountAction(Context context, String completed, String total) {
        return completed + "/" + total;
    }

    public static Date parseDate(String date, String inputFormat) {
        SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat, Locale.ENGLISH);
        try {
            return inputParser.parse(date);
        } catch (ParseException e) {
            return new Date(0);
        }
    }

    public static String dateToString(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.format(date);
    }

    public static Calendar parseCalendar(String date, String inputFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(inputFormat, Locale.ENGLISH);
        Calendar cal = null;
        try {
            cal = Calendar.getInstance();
            cal.setTime(sdf.parse(date));// all done
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }

    public static String getAppName(Context context, String packageName) {
        String applicationName;

        if (packageName == null) {
            packageName = context.getPackageName();
        }

        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    packageName, 0);
            applicationName = context
                    .getString(packageInfo.applicationInfo.labelRes);
        } catch (Exception e) {
            LogUtils.Print("error", "Failed to get version number." + e);
            applicationName = "";
        }

        return applicationName;
    }

    public static String getAppVersionNumber(Context context, String packageName) {
        String versionName;

        if (packageName == null) {
            packageName = context.getPackageName();
        }

        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    packageName, 0);
            versionName = packageInfo.versionName;
        } catch (Exception e) {
            // LogUtils.Print("Failed to get version number.",""+e);
            e.printStackTrace();
            versionName = "";
        }

        return versionName;
    }

    // -------------------------------------------------//
    // ------------ parseDate----------------------//
    // -------------------------------------------------//

    public static String getAppVersionCode(Context context, String packageName) {
        String versionCode;

        if (packageName == null) {
            packageName = context.getPackageName();
        }

        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    packageName, 0);
            versionCode = Integer.toString(packageInfo.versionCode);
        } catch (Exception e) {
            LogUtils.Print("Failed ", e.getMessage());
            versionCode = "";
        }

        return versionCode;
    }

    public static int getSdkVersion() {
        try {
            return Build.VERSION.class.getField("SDK_INT").getInt(null);
        } catch (Exception e) {
            return 3;
        }
    }

    public static boolean isEmulator() {
        return Build.MODEL.equals("sdk") || Build.MODEL.equals("google_sdk");
    }

    /**
     * ***************************************************************************
     * ************************* NAVIGATE INTENT UTILS ***************************
     * ***************************************************************************
     */
    public static void startWebActivity(Context context, String url) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        if (!url.startsWith("http"))
            url = "https://" + url;
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    public static void startWebSearchActivity(Context context, String url) {
        final Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, url);
        context.startActivity(intent);
    }

    public static void startEmailActivity(Activity activity, String email) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        activity.startActivity(intent);
    }

    public static void startEmailActivity(Context context, String to,
                                          String subject, String body) {
        final Intent intent = new Intent(Intent.ACTION_SEND);
        //intent.setType("message/rfc822");
        intent.setType("text/plain");
        if (!TextUtils.isEmpty(to)) {
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
        }
        if (!TextUtils.isEmpty(subject)) {
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }
        if (!TextUtils.isEmpty(body)) {
            intent.putExtra(Intent.EXTRA_TEXT, body);
        }

        final PackageManager pm = context.getPackageManager();
        try {
            if (pm.queryIntentActivities(intent,
                    PackageManager.MATCH_DEFAULT_ONLY).size() == 0) {
                intent.setType("text/plain");
            }
        } catch (Exception e) {
            LogUtils.Print("Error.", e.getMessage());
        }

        context.startActivity(intent);
    }

    public static void startGoogleMapNavigationActivity(Context context, double source_lat, double source_lng, double dest_lat, double dest_lng) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?" + "saddr=" + source_lat
                        + "," + source_lng + "&daddr=" + dest_lat + "," + dest_lng));
        context.startActivity(intent);
    }

    public static void startGoogleMapNavigationActivity(Context context, double latitude, double longitude) {
        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        context.startActivity(intent);
    }

    public static void startVideoPlayerActivity(Context context, String path) {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(path), "video/mp4");
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sharePostViaIntent(Context context, String textToShare, String imgURL) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, textToShare);
        if (!imgURL.equals("")) {
            Uri imageUri = Uri.parse(imgURL);
            intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        }
        intent.setType("text/plain");
        context.startActivity(Intent.createChooser(intent, "Share via..."));
    }

    public static void navigateUserToStore(Context context) {
        final String appPackageName = context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + appPackageName)));
        } catch (ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    /**
     * ***************************************************************************
     * ***************************** IMAGE FILE UTILS ****************************
     * ***************************************************************************
     */
    public static String getRealPathFromURI(Uri contentUri, Activity activity) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = activity.managedQuery(contentUri, proj, null,
                    null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return contentUri.getPath();
        }
    }

    // ---------------------------------------------------//
    // -------------- get Screen height ----------------//
    // -------------------------------------------------//
    public static int getScreenWidth(Activity context) {
        Display display = context.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public static int getScreenHeight(Activity context) {
        Display display = context.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    /*public static void makeNativeCall(Activity activity, String mobileNo) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobileNo));
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        activity.startActivity(intent);
    }*/

    /**
     * ***************************************************************************
     * **************************** PERMISSION UTILS ***************************
     * ***************************************************************************
     */
    public static boolean checkPermission(Context context, String permission) {
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void showPermissionsDialog(Activity activity, String[] permissions, int REQUEST_CODE) {
        ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE);
    }

    public static boolean shouldShowRequestPermissionRationale(Activity activity, String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }

    /**
     * ***************************************************************************
     * ************************* OTHER UTILS **************************
     * ***************************************************************************
     */
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    public static int getToolBarHeight(Context context) {
        int[] attrs = new int[]{R.attr.actionBarSize};
        TypedArray ta = context.obtainStyledAttributes(attrs);
        int toolBarHeight = ta.getDimensionPixelSize(0, -1);
        ta.recycle();
        return toolBarHeight;
    }

    /**
     * return device width
     *
     * @param context
     */
    public static int getDeviceWidth(Context context) {
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            return displayMetrics.widthPixels;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getstringfromArray(String[] array) {
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            strBuilder.append(array[i] + ",");
        }
        return strBuilder.toString();
    }

    public static String getTimeFromMinutes(String strMinutes) {
        try {
            int t = Integer.parseInt(strMinutes);
            int hours = t / 60; //since both are ints, you get an int
            int minutes = t % 60;
            System.out.printf("%d:%02d", hours, minutes);
            return hours + ":" + minutes;
        } catch (Exception e) {
            return "-";
        }
    }

    public static boolean isFileLessThan2MB(String strFile) {
        File file = new File(strFile);
        // Get length of file in bytes
        long fileSizeInBytes = file.length();
        // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
        long fileSizeInKB = fileSizeInBytes / 1024;
        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        long fileSizeInMB = fileSizeInKB / 1024;
        LogUtils.Print(TAG, "fileSizeInMB-> " + fileSizeInMB);
        return (fileSizeInMB < 2);
    }

    //phone number validation
    public static boolean isValidPhoneNumber(String phone) {
        boolean check;
        Pattern p;
        Matcher m;
        // String PHONE_STRING = "^[+]?[0-9]{10,13}$";
        String PHONE_STRING = "^\\+(?:[0-9]●?){6,14}[0-9]$";
        p = Pattern.compile(PHONE_STRING);
        m = p.matcher(phone);
        check = m.matches();
        return (phone.length() == 8);
        //return check;
    }

    public static void setViewVisibility(View view, int visibility) {
        if (view != null) {
            view.setVisibility(visibility);
        }
    }

    // -------------------------------------------------//
    // --------set color to swipe refresh layout-------//
    // -------------------------------------------------//
    public static void setColorToSwipeRefreshLayout(SwipeRefreshLayout srl) {
        try {
            srl.setColorSchemeResources(R.color.colorPrimary);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * get ratting from string to float
     *
     * @param ratting avg ratting
     * @return float value of ratting
     */
    public static Float getRatting(String ratting) {
        float rate = 0;
        try {
            rate = Float.valueOf(ratting);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rate;
    }

    public static String getCurrentDate(String format) {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        return sdf.format(date);
    }

    public static void downloadFile(Context activity, String url) {
        String name = url.substring(url.lastIndexOf("/") + 1);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //allow type of network to download file(s) by default both are allowed
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle(name);
        request.setDescription("The file is downloading...");


        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "" + name);

        //get download service, and enqueue file
        DownloadManager manager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
        if (manager != null) {
            manager.enqueue(request);
        }
    }

   /* public static String getWeekDayName(CalendarDay date) {
        String name = "";
        switch (date.getDate().getDayOfWeek()) {
            case SUNDAY:
                name = "Sun";
                break;
            case MONDAY:
                name = "Mon";
                break;
            case TUESDAY:
                name = "Tue";
                break;
            case WEDNESDAY:
                name = "Wed";
                break;
            case THURSDAY:
                name = "Thu";
                break;
            case FRIDAY:
                name = "Fri";
                break;
            case SATURDAY:
                name = "Sat";
                break;
        }
        return name;
    }*/

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    // Method for converting DP/DIP value to pixels
    public static int getPixelsFromDPs(Context activity, int dps) {
        Resources r = activity.getResources();
        return (int) (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dps, r.getDisplayMetrics()));
    }

    /**
     * ***************************************************************************
     * ***************************** APP INFO UTILS ******************************
     * ***************************************************************************
     */
    public static String GetDeviceID(Context ctx) {
        return Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);

    }

    /**
     * Returns the consumer friendly device name
     */
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }

    public static String streamToString(InputStream is) throws IOException {
        String str = "";

        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is));

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                reader.close();
            } finally {
                is.close();
            }

            str = sb.toString();
        }

        return str;
    }

    public static float convertSpToPixels(float sp, Context context) {

        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return sp / scaledDensity;
//        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    /**
     * Method for return file path of Gallery image/ Document / Video / Audio
     *
     * @param context - context of the application or class
     * @param uri     - uri to get the path
     * @return - path of the selected image file from gallery
     */
    public static String getPath(final Context context, final Uri uri) {
        // check here to KITKAT or new version
        final boolean isKitKat = true;

        // DocumentProvider
        if (!DocumentsContract.isDocumentUri(context, uri)) {
            if ("content".equalsIgnoreCase(uri.getScheme())) {
                // Return the remote address
                if (isGooglePhotosUri(uri))
                    return uri.getLastPathSegment();

                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        } else { // MediaStore (and general)
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);

                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;

                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }


        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       - The context.
     * @param uri           - The Uri to query.
     * @param selection     - (Optional) Filter used in the query.
     * @param selectionArgs - (Optional) Selection arguments used in the query.
     * @return - The value of the _data column, which is typically a file path.
     */
    private static String getDataColumn(Context context, Uri uri,
                                        String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);

            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return null;
    }

    /**
     * @param uri - The Uri to check.
     * @return - Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri - The Uri to check.
     * @return - Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri - The Uri to check.
     * @return - Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri - The Uri to check.
     * @return - Whether the Uri authority is Google Photos.
     */
    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * #################### HASH TAG ####################
     *
     * @param text
     * @return
     */
    public static String getHashTagFromString(CharSequence text, char keyword) {
        String strHashTag = "";
        if (text.toString().contains("" + keyword)) {
            if (isHashTagWord(text, text.toString().lastIndexOf(keyword))) {
                int index = text.toString().lastIndexOf(keyword);
                while (index < text.length() - 1) {
                    char sign = text.charAt(index);
                    int nextNotLetterDigitCharIndex = index + 1; // we assume it is next. if if was not changed by findNextValidHashTagChar then index will be incremented by 1
                    if (sign == keyword) {
                        nextNotLetterDigitCharIndex = findNextValidHashTagChar(text, text.toString().lastIndexOf(keyword));
                        String strText = text.toString().substring(text.toString().lastIndexOf(keyword) + 1, nextNotLetterDigitCharIndex);
                        LogUtils.Print("GET HASH-TAG FROM STRING", "TEXT --> " + strText);
                        strHashTag = strText;
                    }
                    index = nextNotLetterDigitCharIndex;
                }
            }
        }
        return strHashTag;
    }

    private static int findNextValidHashTagChar(CharSequence text, int start) {
        int nonLetterDigitCharIndex = -1; // skip first sign '#"
        for (int index = start + 1; index < text.length(); index++) {
            char sign = text.charAt(index);
            boolean isValidSign = Character.isLetterOrDigit(sign) || sign != ' ';// || MyUtils.additionalSymbols.contains(sign)
            if (!isValidSign) {
                nonLetterDigitCharIndex = index;
                break;
            }
        }
        if (nonLetterDigitCharIndex == -1) {
            // we didn't find non-letter. We are at the end of text
            nonLetterDigitCharIndex = text.length();
        }
        return nonLetterDigitCharIndex;
    }

    private static boolean isHashTagWord(CharSequence text, int start) {
        boolean isValid = true;
        for (int index = start + 1; index < text.length(); index++) {
            char sign = text.charAt(index);
            boolean isValidSign = Character.isLetterOrDigit(sign) || sign == '_' || sign != ' ';// || MyUtils.additionalSymbols.contains(sign)
            if (!isValidSign) {
                isValid = false;
                break;
            }
        }
        return isValid;
    }

    public static void playAssetSound(Activity activity, int file, int volume) {
        try {
            float log1 = Float.parseFloat(String.valueOf(volume)) / 100.0f;
            MediaPlayer player = MediaPlayer.create(activity, file);
            player.setVolume(log1, log1);
            player.setOnCompletionListener(mediaPlayer -> {
                player.stop();
                player.release();
            });
            player.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        AssetFileDescriptor afd = null;
//        try {
//            afd = activity.getResources().getAssets().openFd(fileName);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        MediaPlayer player = new MediaPlayer();
//        try {
//            assert afd != null;
//            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            player.prepare();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        player.start();
    }

    public static void showFlash(Activity activity) {
        boolean isFlashAvailable = activity.getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
        if (!isFlashAvailable) {
            makeToast(activity, "Flash not available in this device...");
            return;
        }
        CameraManager mCameraManager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        String mCameraId = "0";
        try {
            mCameraId = mCameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        switchFlashLight(mCameraManager, true, mCameraId);
        String finalMCameraId = mCameraId;
        new Handler(Looper.getMainLooper()).postDelayed(() -> switchFlashLight(mCameraManager, false, finalMCameraId), 500);
    }

    private static void switchFlashLight(CameraManager mCameraManager, boolean status, String mCameraId) {
        try {
            mCameraManager.setTorchMode(mCameraId, status);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {
        tv.setTag(tv.getText());
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);
        if (str.contains(spanableText)) {
            ssb.setSpan(new MySpannable(false) {
                @Override
                public void onClick(View widget) {
                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "See Less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 2, ".. See More", true);
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;
    }

    public static Date getFutureDay(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        while (calendar.get(Calendar.DAY_OF_WEEK) != day) {
            calendar.add(Calendar.DATE, 1);
        }
        return calendar.getTime();
    }

    public static Date getFuture10AMDay(int day) {
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 10);
        c.add(Calendar.DATE, day);
        return c.getTime();
    }

    public static void updateStatusBarColor(Activity activity, int color) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(activity, color));
    }

    public static Date addMonthInDate(long timestamp) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timestamp);
        c.add(Calendar.MONTH, 1);
        return c.getTime();
    }

    public static Date longToDate(long timestamp) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timestamp);
        return c.getTime();
    }

    @NonNull
    public static Uri saveBitmap(@NonNull final Context context, @NonNull final Bitmap bitmap,
                                 @NonNull final Bitmap.CompressFormat format, @NonNull final String mimeType,
                                 @NonNull final String displayName, @Nullable final String subFolder) throws IOException {
        final ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName);
        values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM);
        final ContentResolver resolver = context.getContentResolver();
        Uri uri = null;
        try {
            final Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            uri = resolver.insert(contentUri, values);
            if (uri == null)
                throw new IOException("Failed to create new MediaStore record.");
            try (final OutputStream stream = resolver.openOutputStream(uri)) {
                if (stream == null)
                    throw new IOException("Failed to get output stream.");
                if (!bitmap.compress(format, 95, stream))
                    throw new IOException("Failed to save bitmap.");
            }
        } catch (IOException e) {
            if (uri != null) {
                // Don't leave an orphan entry in the MediaStore
                resolver.delete(uri, null, null);
            }
            throw e;
        }
        return uri;
    }

    public static void watchYoutubeVideo(Activity activity, String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            activity.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            activity.startActivity(webIntent);
        }
    }

    public static String getEventViewsDate(Activity activity) {
        return "";
    }

    public static void viewGoneAnimator(final View view) {
//        view.animate().alpha(0f).setDuration(1000);
        view.animate()
                .alpha(0f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
                    }
                });

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setStartOffset(500);
        fadeOut.setDuration(1000); //time in milliseconds

    }

    public static void viewVisibleAnimator(Activity activity, View view) {
        Animation mLoadAnimation = AnimationUtils.loadAnimation(activity, android.R.anim.fade_in);
        mLoadAnimation.setDuration(500);
        view.startAnimation(mLoadAnimation);
        view.setVisibility(View.VISIBLE);
    }

    private String getRealPathFromURI(Context ctx, String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = ctx.getContentResolver().query(contentUri, null, null,
                null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor
                    .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public static class MySpannable extends ClickableSpan {

        private boolean isUnderline = true;

        /**
         * Constructor
         */
        public MySpannable(boolean isUnderline) {
            this.isUnderline = isUnderline;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(isUnderline);
            ds.setColor(Color.parseColor("#1b76d3"));
        }

        @Override
        public void onClick(View widget) {
        }
    }
}