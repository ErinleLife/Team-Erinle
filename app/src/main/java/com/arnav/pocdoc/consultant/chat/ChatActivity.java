package com.arnav.pocdoc.consultant.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.arnav.pocdoc.BaseActivity;
import com.arnav.pocdoc.R;
import com.arnav.pocdoc.SimplyRelief.models.ResponseCommon;
import com.arnav.pocdoc.base.BaseApplication;
import com.arnav.pocdoc.data.model.chat.DataChat;
import com.arnav.pocdoc.data.model.chat.ResponseChat;
import com.arnav.pocdoc.data.model.conversation.DataConversation;
import com.arnav.pocdoc.data.network.APIClient;
import com.arnav.pocdoc.databinding.ActivityChatBinding;
import com.arnav.pocdoc.implementor.PushUpdateListener;
import com.arnav.pocdoc.implementor.RecyclerViewItemClickListener;
import com.arnav.pocdoc.services.MyFirebaseMessagingService;
import com.arnav.pocdoc.utils.CameraGalleryActivity;
import com.arnav.pocdoc.utils.Constants;
import com.arnav.pocdoc.utils.EndlessScrollRecyclerViewListener;
import com.arnav.pocdoc.utils.LogUtils;
import com.arnav.pocdoc.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends BaseActivity implements RecyclerViewItemClickListener, PushUpdateListener {

    private static final String TAG = "ChatActivity";

    private ChatAdapter adapter;
    private final List<DataChat> list = new ArrayList<>();

    private boolean mIsLoading;
    private Integer offset = Constants.pagination_start_offset;
    private String dateHeaders = "";
    private String tempDateDateHeaders = "";
    private String isFirstDate = "";

    private ActivityChatBinding binding;
    private DataConversation data;

    private final AtomicBoolean mIsFirstListener = new AtomicBoolean(true);
    private DatabaseReference mReference;
    private DatabaseReference mDatabase;
    private boolean needToUpdatePreviousScreen;
    private String conversationId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        binding.setActivity(this);
        initViews();
        binding.setLifecycleOwner(this);
    }

    private void initViews() {
        getDataFromIntent();
        setupListeners();
        setupFirebaseDatabase();
        getChatList(0);
    }

    /**
     * Get the data from intent
     */
    private void getDataFromIntent() {
        data = getIntent().getParcelableExtra(Constants.data);
        int fromId = data.getId();
        int toID = Integer.parseInt(BaseApplication.preferences.getUserId());
        conversationId = (fromId > toID) ? toID + "_" + fromId : fromId + "_" + toID;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyFirebaseMessagingService.setOnChatMessageListener(true, conversationId, this);
        LogUtils.Print(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyFirebaseMessagingService.setOnChatMessageListener(true, "pause", this);
        LogUtils.Print(TAG, "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyFirebaseMessagingService.setOnChatMessageListener(true, "", null);
        if (mReference != null) {
            mReference.removeEventListener(mPostListener);
        }
        LogUtils.Print(TAG, "onDestroy");
    }

    /**
     * Setup listeners
     */
    private void setupListeners() {
        binding.header.tvTitle.setText(getResources().getString(R.string.consultant));

        binding.etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.btnNext.setEnabled(!binding.etMessage.getText().toString().trim().isEmpty());
                binding.btnNext.setAlpha(!binding.etMessage.getText().toString().trim().isEmpty() ? 1f : 0.5f);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.btnNext.setEnabled(!binding.etMessage.getText().toString().trim().isEmpty());
        binding.btnNext.setAlpha(!binding.etMessage.getText().toString().trim().isEmpty() ? 1f : 0.5f);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.addOnScrollListener(new EndlessScrollRecyclerViewListener() {
            @Override
            public void onLoadMore() {
                if (!mIsLoading
                        && !Objects.equals(offset, Constants.pagination_start_offset)
                        && !Objects.equals(offset, Constants.pagination_last_offset)) {
                    getChatList(1);
                }
            }
        });
        adapter = new ChatAdapter(this);
        adapter.setRecyclerViewItemClickListener(this);
        binding.recyclerView.setAdapter(adapter);
    }

    /**
     * Setup firebase database
     */
    private void setupFirebaseDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if (conversationId.equals("") || conversationId.equals("0")) return;
        mReference = mDatabase
                .child("Conversation")
                .child(String.valueOf(conversationId));
        mReference.addValueEventListener(mPostListener);
    }

    public void onViewClick(View view) {
        if (view == binding.btnNext) {
            sendMessage(Constants.TEXT, "");
        } else if (view == binding.ivChooseImage) {
            CameraGalleryActivity.image = CameraGalleryActivity.IMAGE.AllNoCrop;
            Intent intent = new Intent(this, CameraGalleryActivity.class);
            mediaUploadResultLauncher.launch(intent);
        }
    }

    private final ActivityResultLauncher<Intent> mediaUploadResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (result.getData() != null) {
                        try {
                            if (result.getData().getStringExtra(Constants.image) != null &&
                                    !result.getData().getStringExtra(Constants.image).isEmpty()) {
                                sendMessage(Constants.IMAGE, result.getData().getStringExtra(Constants.image));
                            } else {
                                showMessage("No image found.");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            showMessage(getResources().getString(R.string.server_error));
                        }
                    }
                }
            });


    private void sendMessage(String from, String imagePath) {
        HashMap<String, RequestBody> params = new HashMap<>();
        params.put(Constants.from_id, APIClient.createRequestBody(BaseApplication.preferences.getUserId()));
        params.put(Constants.to_id, APIClient.createRequestBody(String.valueOf(data.getId())));
        params.put(Constants.message, APIClient.createRequestBody(binding.etMessage.getText().toString().trim()));

        MultipartBody.Part[] body = null;
        if (imagePath != null && !imagePath.isEmpty()) {
            body = new MultipartBody.Part[1];
            File file = new File(imagePath);
            body[0] = MultipartBody.Part.createFormData(Constants.file, file.getName(), APIClient.createRequestBody(file));
        }
        binding.etMessage.setText("");
        binding.progressSend.setVisibility(View.VISIBLE);
        Call<ResponseCommon> call = apiInterface.sendMessage(params, body);
        call.enqueue(new Callback<ResponseCommon>() {
            @Override
            public void onResponse(@NonNull Call<ResponseCommon> call, @NonNull Response<ResponseCommon> response) {
                binding.progressSend.setVisibility(View.GONE);
                ResponseCommon result = response.body();
                if (result == null) return;
                needToUpdatePreviousScreen = true;
//                if (conversationId.equals("") || conversationId.equals("0")) {
//                    conversationId = String.valueOf(message.getLast_message_id());
//
//                    mReference = mDatabase
//                            .child("Conversation")
//                            .child(String.valueOf(conversationId));
//                    mIsFirstListener.set(false);
//                    mReference.addValueEventListener(mPostListener);
//                }
                mDatabase.child("Conversation")
                        .child(String.valueOf(conversationId))
                        .setValue(result.getData());

//                if (result.getStatus() == Constants.RESPONSE_SUCCESS_FLAG) {
//                    showMessage(result.getMsg());
//                    needToUpdate = true;
//                    onBackPressed();
//                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseCommon> call, @NonNull Throwable t) {
                binding.progressSend.setVisibility(View.GONE);
                call.cancel();
                showMessage(getResources().getString(R.string.server_error));
            }
        });
    }

    public void getChatList(int from) {
        if (from == 0) {
            offset = Constants.pagination_start_offset;
        }
        mIsLoading = true;
        showProgress();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(Constants.from_id, String.valueOf(BaseApplication.preferences.getUserId()));
        hashMap.put(Constants.to_id, String.valueOf(data.getId()));
        hashMap.put(Constants.per_page, String.valueOf(Constants.PAGE_LIMIT));
        hashMap.put(Constants.page, String.valueOf(offset));
        Call<ResponseChat> call = apiInterface.chatList(hashMap);
        call.enqueue(new Callback<ResponseChat>() {
            @Override
            public void onResponse(@NotNull Call<ResponseChat> call, @NonNull Response<ResponseChat> response) {
                mIsLoading = false;
                hideProgress();
                ResponseChat result = response.body();
                if (result == null) return;
                if (offset == Constants.pagination_start_offset) {
                    list.clear();
                    if (result.getData().size() == 0)
                        mIsFirstListener.set(false);
                }
                adapter.setBaseURL(result.getAttachmentUrl());

                List<DataChat> tempChat = new ArrayList<>();

                for (DataChat chat : result.getData()) {
                    if (dateHeaders.equals("")) {
                        isFirstDate = Utils.GetDateOnRequireFormat(chat.getCreated_at(), Constants.DATE_YYYY_MM_DD_HH_MM_AA_FORMAT, Constants.DATE_EEE_DD_MM_FORMAT);
                        dateHeaders = Utils.GetDateOnRequireFormat(chat.getCreated_at(), Constants.DATE_YYYY_MM_DD_HH_MM_AA_FORMAT, Constants.DATE_EEE_DD_MM_FORMAT);
                        tempDateDateHeaders = chat.getCreated_at();
                    }
                    String dateString = Utils.GetDateOnRequireFormat(chat.getCreated_at(), Constants.DATE_YYYY_MM_DD_HH_MM_AA_FORMAT, Constants.DATE_EEE_DD_MM_FORMAT);
//            dateHeaders = chat.getChat_date();
                    if (!dateString.equals(dateHeaders)) {
                        DataChat temp = new DataChat();
                        temp.setCreated_at(tempDateDateHeaders);
                        temp.setType(Constants.HEADER);
                        tempDateDateHeaders = chat.getCreated_at();
                        dateHeaders = Utils.GetDateOnRequireFormat(chat.getCreated_at(), Constants.DATE_YYYY_MM_DD_HH_MM_AA_FORMAT, Constants.DATE_EEE_DD_MM_FORMAT);
                        tempChat.add(temp);
                    }
                    chat.setType(chat.getAttachment() != null && !chat.getAttachment().isEmpty() ? Constants.IMAGE : Constants.TEXT);
                    tempChat.add(chat);
                }

                if (result.getData().size() < Constants.PAGE_LIMIT) {
                    if (adapter.getItemCount() != 0 || result.getData().size() != 0) {
                        DataChat temp = new DataChat();
                        temp.setCreated_at(tempDateDateHeaders);
                        temp.setType(Constants.HEADER);
                        dateHeaders = Utils.GetDateOnRequireFormat(tempDateDateHeaders, Constants.DATE_YYYY_MM_DD_HH_MM_AA_FORMAT, Constants.DATE_EEE_DD_MM_FORMAT);
                        tempChat.add(temp);
                    }
                }

                adapter.addItems(tempChat);
                //Set Offset
                if (offset == Constants.pagination_start_offset)
                    scrollRecyclerView();
                if (result.getData().size() == 0 || result.getData().size() != Constants.PAGE_LIMIT) {
                    offset = Constants.pagination_last_offset;
                } else {
                    offset = offset + 1;
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseChat> call, @NonNull Throwable t) {
                mIsLoading = false;
                hideProgress();
                call.cancel();
                showMessage(getResources().getString(R.string.server_error));
            }
        });
    }

    /**
     * Scrolls the list to the last item
     */
    private void scrollRecyclerView() {
        binding.recyclerView.scrollToPosition(0);
    }

    @Override
    public void onItemClick(int position, int flag, View view) {

    }

    @Override
    public void onBackPressed() {
        Utils.hideKeyBoard(this);
        if (needToUpdatePreviousScreen) {
            Intent intent = new Intent();
            intent.putExtra(Constants.data, true);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    private final ValueEventListener mPostListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
            if (mIsFirstListener.get()) {
                mIsFirstListener.set(false);
                return;
            }
            if (!dataSnapshot.hasChildren()) {
                return;
            }
            DataChat dataChat = dataSnapshot.getValue(DataChat.class);
            if (dataChat == null) return;
            tempDateDateHeaders = dataChat.getCreated_at();

            if (dateHeaders.equals("")) {
                isFirstDate = Utils.GetDateOnRequireFormat(dataChat.getCreated_at(), Constants.DATE_YYYY_MM_DD_HH_MM_AA_FORMAT, Constants.DATE_EEE_DD_MM_FORMAT);
                dateHeaders = Utils.GetDateOnRequireFormat(dataChat.getCreated_at(), Constants.DATE_YYYY_MM_DD_HH_MM_AA_FORMAT, Constants.DATE_EEE_DD_MM_FORMAT);
            }

            String dateString = Utils.GetDateOnRequireFormat(dataChat.getCreated_at(), Constants.DATE_YYYY_MM_DD_HH_MM_AA_FORMAT, Constants.DATE_EEE_DD_MM_FORMAT);

            if (!dateHeaders.equals("") && !dateString.equals(dateHeaders)) {
                if (!isFirstDate.equals(dateString)) {
                    DataChat temp = new DataChat();
                    temp.setCreated_at(tempDateDateHeaders);
                    temp.setType(Constants.HEADER);
                    tempDateDateHeaders = dataChat.getCreated_at();
                    dateHeaders = Utils.GetDateOnRequireFormat(dataChat.getCreated_at(), Constants.DATE_YYYY_MM_DD_HH_MM_AA_FORMAT, Constants.DATE_EEE_DD_MM_FORMAT);
                    adapter.addItem(temp);
                }
            } else {
                if (adapter.getItemCount() < 2) {
                    DataChat temp = new DataChat();
                    temp.setCreated_at(tempDateDateHeaders);
                    temp.setType(Constants.HEADER);
                    tempDateDateHeaders = dataChat.getCreated_at();
                    dateHeaders = Utils.GetDateOnRequireFormat(dataChat.getCreated_at(), Constants.DATE_YYYY_MM_DD_HH_MM_AA_FORMAT, Constants.DATE_EEE_DD_MM_FORMAT);
                    adapter.addItem(temp);
                }
            }

            dataChat.setType(dataChat.getAttachment() != null && !dataChat.getAttachment().isEmpty() ? Constants.IMAGE : Constants.TEXT);
            adapter.addItem(dataChat);
            scrollRecyclerView();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            LogUtils.Print(TAG, "loadPost:onCancelled" + databaseError.toException());
        }
    };

    @Override
    public void onPushReceived(DataChat dataChat) {
        mDatabase.child("Conversation")
                .child(String.valueOf(conversationId))
                .setValue(dataChat);
    }
}