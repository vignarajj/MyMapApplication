package com.vig.myapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.appolica.flubber.Flubber;
import com.google.gson.Gson;
import com.vig.myapp.adapter.DataAdapter;
import com.vig.myapp.custom.TextSansRegular;
import com.vig.myapp.interfaces.ResponseListener;
import com.vig.myapp.interfaces.UserSelectedListener;
import com.vig.myapp.models.Location;
import com.vig.myapp.models.UserData;
import com.vig.myapp.request.WebServices;
import com.vig.myapp.util.NetworkUtil;
import com.vig.myapp.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.vig.myapp.util.Utils.list;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.list_users)
    RecyclerView listUsers;
    @Bind(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;
    @Bind(R.id.txt_message)
    TextSansRegular txtMessage;
    @Bind(R.id.txt_refresh)
    TextSansRegular txtRefresh;

    Context context;


    public static String TAG = MainActivity.class.getSimpleName();
    public DataAdapter adapter;
    LinearLayoutManager layoutManager;
    boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Users");
        context = MainActivity.this;
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listUsers.setLayoutManager(layoutManager);
        swipeContainer.setVisibility(View.GONE);
        swipeContainer.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        swipeContainer.setOnRefreshListener(refreshListener);
        refreshData();
    }

    public SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshData();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        loadList();
    }

    @OnClick(R.id.txt_refresh)
    public void refreshData() {
        if (NetworkUtil.getConnectivityStatus(context)) {
            appendUserData();
        } else {
            Utils.showErrorToast(context, "No Internet Connection");
            txtMessage.setVisibility(View.VISIBLE);
            txtRefresh.setVisibility(View.VISIBLE);
            txtMessage.setText("No Internet Connection");
            if (Utils.isCacheExist(getBaseContext())) {
                String data = Utils.retrieveCache(getBaseContext());
                if (!data.equals("")) {
                    showOfflineView(data);
                }
            }
        }
    }

    public void loadList() {
        if (list.size() > 0) {
            Collections.sort(list, new Comparator<UserData>() {
                @Override
                public int compare(UserData o1, UserData o2) {
                    return o1.getName().compareToIgnoreCase(o2.getName());
                }
            });

            adapter = new DataAdapter(context, list, listener);
            listUsers.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            swipeContainer.setVisibility(View.VISIBLE);
            txtMessage.setVisibility(View.GONE);
            txtRefresh.setVisibility(View.GONE);
        }
    }

    public void appendUserData() {
        swipeContainer.setRefreshing(true);
        WebServices.getInstance().getUsersData(context, new ResponseListener() {
            @Override
            public void onResponse(final String responseStr) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray arr = new JSONArray(responseStr);
                            if (arr.length() > 0) {
                                if (list.size() > 0) {
                                    list.clear();
                                }
                                if (Utils.isCacheExist(getBaseContext())) {
                                    Utils.clearCache(getBaseContext());
                                    Utils.storeToCache(context, responseStr);
                                } else {
                                    Utils.storeToCache(context, responseStr);
                                }
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject obj = arr.getJSONObject(i);
                                    JSONObject locObj = obj.getJSONObject("location");
                                    String latitude = locObj.optString("latitude");
                                    String longitude = locObj.optString("longitude");
                                    String picture = obj.optString("picture");
                                    String _id = obj.optString("_id");
                                    String name = obj.optString("name");
                                    String email = obj.optString("email");
                                    list.add(new UserData(_id, name, email, picture, new Location(latitude, longitude)));
                                }

                                loadList();
                                Flubber.with()
                                        .animation(Flubber.AnimationPreset.SLIDE_RIGHT)
                                        .repeatCount(0)
                                        .duration(2000)
                                        .createFor(swipeContainer)
                                        .start();
                            }
                        } catch (JSONException e) {
                            Utils.largeLog(TAG, e.getMessage());
                        }
                        swipeContainer.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeContainer.setRefreshing(false);
                        swipeContainer.setVisibility(View.GONE);
                        txtMessage.setText(message);
                    }
                });
            }
        });
    }

    public void showOfflineView(final String responseStr) {
        swipeContainer.setRefreshing(true);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONArray arr = new JSONArray(responseStr);
                    if (arr.length() > 0) {
                        if (list.size() > 0) {
                            list.clear();
                        }
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);
                            JSONObject locObj = obj.getJSONObject("location");
                            String latitude = locObj.optString("latitude");
                            String longitude = locObj.optString("longitude");
                            String picture = obj.optString("picture");
                            String _id = obj.optString("_id");
                            String name = obj.optString("name");
                            String email = obj.optString("email");
                            list.add(new UserData(_id, name, email, picture, new Location(latitude, longitude)));
                        }

                        loadList();
                        Flubber.with()
                                .animation(Flubber.AnimationPreset.SLIDE_RIGHT)
                                .repeatCount(0)
                                .duration(2000)
                                .createFor(swipeContainer)
                                .start();
                    }
                } catch (JSONException e) {
                    Utils.largeLog(TAG, e.getMessage());
                }
                swipeContainer.setRefreshing(false);
            }
        });
    }

    public UserSelectedListener listener = new UserSelectedListener() {
        @Override
        public void onItemClick(int position) {
            UserData data = list.get(position);
            if (data != null) {
                String mData = new Gson().toJson(data);
                Log.i("data", mData);
                Intent detailIntent = new Intent();
                detailIntent.putExtra("user_data", mData);
                detailIntent.setClass(MainActivity.this, DetailsActivity.class);
                startActivity(detailIntent);
            }
        }
    };

}
