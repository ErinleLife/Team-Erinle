package com.arnav.pocdoc.consultant.conversation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.arnav.pocdoc.BaseActivity;
import com.arnav.pocdoc.R;
import com.arnav.pocdoc.base.BaseApplication;
import com.arnav.pocdoc.consultant.chat.ChatActivity;
import com.arnav.pocdoc.consultant.conversation.adapter.ConsultantListAdapter;
import com.arnav.pocdoc.consultant.newconversation.NewConsultantListActivity;
import com.arnav.pocdoc.data.model.conversation.DataConversation;
import com.arnav.pocdoc.data.model.conversation.ResponseConversation;
import com.arnav.pocdoc.databinding.ActivityConsultantListBinding;
import com.arnav.pocdoc.implementor.RecyclerViewItemClickListener;
import com.arnav.pocdoc.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsultantListActivity extends BaseActivity implements RecyclerViewItemClickListener {
    private ConsultantListAdapter adapter;
    private final List<DataConversation> list = new ArrayList<>();

    private ActivityConsultantListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_consultant_list);
        binding.setActivity(this);
        setUpHeaderView();
        binding.setLifecycleOwner(this);
    }

    private void setUpHeaderView() {
        binding.header.tvTitle.setText(getResources().getString(R.string.consultant));

        adapter = new ConsultantListAdapter(this, list);
        binding.rv.setAdapter(adapter);
        adapter.setRecyclerViewItemClickListener(this);

        getChatList();

        binding.mSwipeRefreshLayout.setOnRefreshListener(this::getChatList);
    }

    public void getChatList() {
        if (!binding.mSwipeRefreshLayout.isRefreshing())
            showProgress();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(Constants.user_id, BaseApplication.preferences.getUserId());
        Call<ResponseConversation> call = apiInterface.getConversation(hashMap);
        call.enqueue(new Callback<ResponseConversation>() {
            @Override
            public void onResponse(@NotNull Call<ResponseConversation> call, @NonNull Response<ResponseConversation> response) {
                hideLoaders();
                ResponseConversation result = response.body();
                if (result == null) return;
                if (result.getData() != null) {
                    adapter.setBaseURL(result.getImageUrl());
                    list.clear();
                    list.addAll(result.getData());
                    adapter.notifyDataSetChanged();

                    if (list.size() > 0) {
                        binding.tvEmpty.setVisibility(View.GONE);
                    } else {
                        binding.tvEmpty.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseConversation> call, @NonNull Throwable t) {
                hideLoaders();
                call.cancel();
                showMessage(getResources().getString(R.string.server_error));
            }
        });
    }

    private void hideLoaders() {
        hideProgress();
        if (binding.mSwipeRefreshLayout.isRefreshing())
            binding.mSwipeRefreshLayout.setRefreshing(false);
    }

    public void onViewClicked(View view) {
        if (view == binding.addConversation) {
            Intent intent1 = new Intent(this, NewConsultantListActivity.class);
            createGroupResultLauncher.launch(intent1);
        }
    }

    private final ActivityResultLauncher<Intent> createGroupResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {

                }
            });

    @Override
    public void onItemClick(int position, int flag, View view) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(Constants.data, list.get(position));
        startActivity(intent);
    }
}