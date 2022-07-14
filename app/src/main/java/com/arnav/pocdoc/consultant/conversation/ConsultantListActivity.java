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
import com.arnav.pocdoc.consultant.chat.ChatActivity;
import com.arnav.pocdoc.consultant.conversation.adapter.ConsultantListAdapter;
import com.arnav.pocdoc.consultant.newconversation.NewConsultantListActivity;
import com.arnav.pocdoc.data.model.cosultantlist.DataConsultant;
import com.arnav.pocdoc.data.model.cosultantlist.ResponseConsultantList;
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
    private final List<DataConsultant> list = new ArrayList<>();

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

        list.add(new DataConsultant());
        list.add(new DataConsultant());
        list.add(new DataConsultant());
        list.add(new DataConsultant());
        list.add(new DataConsultant());
        list.add(new DataConsultant());

        adapter = new ConsultantListAdapter(this, list);
        binding.rv.setAdapter(adapter);
        adapter.setRecyclerViewItemClickListener(this);

        binding.mSwipeRefreshLayout.setOnRefreshListener(() -> {
            getChatList();
        });
    }

    public void getChatList() {
        showProgress();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(Constants.type, "");
        Call<ResponseConsultantList> call = apiInterface.getContacts(hashMap);
        call.enqueue(new Callback<ResponseConsultantList>() {
            @Override
            public void onResponse(@NotNull Call<ResponseConsultantList> call, @NonNull Response<ResponseConsultantList> response) {
                hideLoaders();
                ResponseConsultantList result = response.body();
//                if (result == null) return;
//                if (result.getStatus() == Constants.RESPONSE_SUCCESS_FLAG) {
//                    if (result.getData() != null) {
//                        if (offset == Constants.pagination_start_offset) {
//                            list.clear();
//                        }
//                        list.addAll(result.getData());
//                        adapter.notifyDataSetChanged();
//
//                        if (result.getData().size() == 0 || result.getData().size() != Constants.PAGINATION_LIMIT) {
//                            offset = Constants.pagination_last_offset;
//                        } else {
//                            offset = offset + 1;
//                        }
//
//                        if (list.size() > 0) {
//                            binding.error.emptyView.setVisibility(View.GONE);
//                        } else {
//                            binding.error.emptyView.setVisibility(View.VISIBLE);
//                        }
//                    }
//                } else {
//                    binding.error.emptyView.setVisibility(View.VISIBLE);
//                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseConsultantList> call, @NonNull Throwable t) {
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
        start(ChatActivity.class);
    }
}