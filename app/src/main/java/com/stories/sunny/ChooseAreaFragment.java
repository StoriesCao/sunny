package com.stories.sunny;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.stories.sunny.db_model.City;
import com.stories.sunny.db_model.CityStoraged;
import com.stories.sunny.db_model.County;
import com.stories.sunny.db_model.Province;
import com.stories.sunny.util.HttpUtil;
import com.stories.sunny.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Charlottecao on 9/1/17.
 */

public class ChooseAreaFragment extends Fragment {

    private static final String TAG = "ChooseAreaFragment";

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    private int currentLevel;
    private Province selectedProvince;
    private City selectedCity;

    private ArrayAdapter<String> mAdapter; //listview adapter
    private List<Province> mProvinceList;
    private List<City> mCityList;
    private List<County> mCountyList;
    private List<String> mLocationNameList = new ArrayList<>();
    private List<CityStoraged> mCityStoragedsList;

    private Button mBackButton;
    private TextView mTitle;
    private ListView mListView;

    private ProgressDialog mProgressDialog;

    int j = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.choose_area, container, false);

        mTitle = (TextView) view.findViewById(R.id.activity_choose_area_title);
        mBackButton = (Button) view.findViewById(R.id.activity_choose_area_back_button);

        mListView = (ListView) view.findViewById(R.id.activity_choose_area_list_view);
        mAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mLocationNameList);
        mListView.setAdapter(mAdapter);

        return view;

    }

   @Override
   public void onActivityCreated(Bundle savedInstanceState) {
       super.onActivityCreated(savedInstanceState);
       mCityStoragedsList = DataSupport.findAll(CityStoraged.class); // 查询已经添加的城市
       queryProvinces();
       mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               if (currentLevel == LEVEL_PROVINCE) {
                   selectedProvince = mProvinceList.get(i);
                   queryCities();
               } else if (currentLevel == LEVEL_CITY) {
                   selectedCity = mCityList.get(i);
                   queryCounties();
               } else if (currentLevel == LEVEL_COUNTY) {
                   County county = mCountyList.get(i);

                   for (j = 0; j < mCityStoragedsList.size(); j++) {
                       if (county.getName().equals(mCityStoragedsList.get(j).getCityStoragedName())) {
                           Log.d(TAG, "county: " + county.getName() + " List: " + mCityStoragedsList.get(j).getCityStoragedName());
                           Snackbar.make(view, "已经添加该城市", Snackbar.LENGTH_SHORT)
                                   .setAction("查看", new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           startActivity(new Intent(getActivity(), MainActivity.class));
                                       }
                                   }).show();
                           break;
                       }

                       if (j == mCityStoragedsList.size() - 1){
                           /* ***将选择的城市添加到城市管理数据库*** */
                           CityStoraged city = new CityStoraged();
                           city.setCityStoragedName(county.getName());
                           city.setWeatherId(county.getWeatherId());
                           city.save();

                           startActivity(new Intent(getActivity(), MainActivity.class));
                           getActivity().finish();
                       }
                   }

                   Log.d(TAG, "j: " + j + " Size: " + mCityStoragedsList.size());
               }
           }
       });

       mBackButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (currentLevel == LEVEL_COUNTY) {
                   queryCities();
               } else if (currentLevel == LEVEL_CITY) {
                   queryProvinces();
               }
           }
       });
   }

    private void queryProvinces() {
        mTitle.setText("中国");
        mBackButton.setVisibility(View.GONE);
        mProvinceList = DataSupport.findAll(Province.class);
        if (mProvinceList.size() > 0) {
            mLocationNameList.clear();
            for (Province province : mProvinceList) {
                mLocationNameList.add(province.getName());
            }
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        } else {
            String address = "http://guolin.tech/api/china";
            queryFromServer(address, "province");
        }

    }

    private void queryCities() {
        mTitle.setText(selectedProvince.getName());
        mBackButton.setVisibility(View.VISIBLE);
        mCityList = DataSupport.where("provinceid=?", String.valueOf(selectedProvince.getProvinceId())).find(City.class);
        if (mCityList.size() > 0) {
            mLocationNameList.clear();
            for (City city : mCityList) {
                mLocationNameList.add(city.getName());
            }
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            currentLevel = LEVEL_CITY;
        } else {
            String address = "http://guolin.tech/api/china/" + selectedProvince.getProvinceId();
            queryFromServer(address, "city");
        }
    }

    private void queryCounties() {
        mTitle.setText(selectedCity.getName());
        mCountyList = DataSupport.where("cityid=?", String.valueOf(selectedCity.getCityId())).find(County.class);
        if (mCountyList.size() > 0) {
            mLocationNameList.clear();
            for (County county : mCountyList) {
                mLocationNameList.add(county.getName());
            }
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        } else {
            String address = "http://guolin.tech/api/china/" + selectedProvince.getProvinceId() + "/" + selectedCity.getCityId();
            queryFromServer(address, "county");
        }

    }

    /**
     * Receive parse and put data that from server in DB.
     * @param address
     * @param type
     */
    private void queryFromServer(String address, final String type) {
        showProcessDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProcessDialog();
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean handleResult = false;
                if ("province".equals(type)) {
                    handleResult = Utility.parseProvinceJson(responseText);
                } else if ("city".equals(type)){
                    handleResult = Utility.parseCityJson(responseText, selectedProvince.getProvinceId());
                } else if ("county".equals(type)) {
                    handleResult = Utility.parseCountyJson(responseText, selectedCity.getCityId());
                }
                if (handleResult) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProcessDialog();
                            if ("province".equals(type)) {
                                queryProvinces();
                            } else if ("city".equals(type)){
                                queryCities();
                            } else if ("county".equals(type)) {
                                queryCounties();
                            }
                        }
                    });
                }
            }
        });
    }

    /*Show the ProcessDialog*/
    private void showProcessDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("正在加载,请稍后……");
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }

    /*Close the ProcessDialog*/
    private void closeProcessDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
