package com.arnav.pocdoc;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.arnav.pocdoc.Authentication.Login;
import com.arnav.pocdoc.base.BaseApplication;
import com.arnav.pocdoc.utils.Constants;
import com.arnav.pocdoc.utils.LogUtils;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SplashActivity extends AppCompatActivity {
    public static final List<KeyPairBoolData> foods = new ArrayList<>();
    static final ArrayList<Symptom> symptoms = new ArrayList<>();
    ImageView ivSplash;
    LottieAnimationView lottieAnimationView;
    Context context;
//    OkHttpClient client = new OkHttpClient();
//    Request request;

//    public static ArrayList<Symptom> getSymptoms() {
//        return symptoms;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        ivSplash = findViewById(R.id.ivSplash);
        Glide.with(this).asGif().load(R.drawable.splash_logo).into(ivSplash);
        getFireBaseToken();
//        request = new Request.Builder()
//                .url(getString(R.string.base_api_url) + "/symptoms")
//                .get()
//                .build();

        context = this;

        try {
            JSONArray jArray = new JSONArray(loadJSONFromAsset());

            Log.e("MAINACTIVITY", "JSON ARRAY CREATED");
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jo_inside = jArray.getJSONObject(i);
                KeyPairBoolData obj = new KeyPairBoolData();
                obj.setName(jo_inside.getString("foods").toUpperCase());
                obj.setId(i + 1);
                obj.setSelected(false);
                foods.add(obj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                runOnUiThread(() -> {
//                    e.printStackTrace();
////                    Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                try {
//                    JSONArray response_json = new JSONObject(response.body().string()).optJSONArray("symptoms");
//                    for (int i = 0; i < response_json.length(); i++) {
//                        JSONObject symptom = response_json.getJSONObject(i);
//                        String id = symptom.optString("id");
//                        String name = symptom.optString("name");
//                        Log.e("NAME-->", "-->" + name);
//                        symptoms.add(new Symptom(id, name));
//                    }
//                } catch (JSONException exception) {
//                    exception.printStackTrace();
//                }
//
//            }
//        });

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                SplashActivity.this.runOnUiThread(() -> {
                    if (!TextUtils.isEmpty(BaseApplication.preferences.getUserId())) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                        intent.putExtra("user", user);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        startActivity(new Intent(SplashActivity.this, Login.class));
                    }
                    finish();
                });

            }
        }, 6000);
    }

    /**
     * GET FIRE-BASE TOKEN REGISTRATION ID AND SAVE INTO PREFERENCE FOR PUSH NOTIFICATION
     */
    private void getFireBaseToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) return;
                    String deviceToken = task.getResult();
                    LogUtils.Print("TAG", "fcm_registration_id --> $deviceToken");
                    BaseApplication.preferences.putString(Constants.fcm_registration_id, deviceToken);
                });
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("foods.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
