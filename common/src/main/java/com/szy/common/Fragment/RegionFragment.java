package com.szy.common.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.szy.common.Adapter.RegionAdapter;
import com.szy.common.Adapter.SelectedRegionAdapter;
import com.szy.common.Constant.ViewType;
import com.szy.common.Other.CommonRequest;
import com.szy.common.R;
import com.szy.common.ResponseModel.Region.ResponseRegionItemModel;
import com.szy.common.ResponseModel.Region.ResponseRegionModel;
import com.szy.common.Util.CommonUtils;
import com.szy.common.Util.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 宗仁 on 16/8/9.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 */
public class RegionFragment extends CommonFragment {
    public static final String KEY_REGION_CODE = "REGION_FRAGMENT_KEY_REGION_CODE";
    public static final String KEY_API = "REGION_FRAGMENT_KEY_API";
    public static final String KEY_HAS_TITLE = "REGION_FRAGMENT_KEY_HAS_TITLE";
    public static final String KEY_SELECTABLE = "REGION_FRAGMENT_KEY_SELECTABLE";
    public static final String KEY_REGION_LIST = "REGION_FRAGMENT_KEY_REGION_LIST";

    public static final int HTTP_WHAT_REGION_LIST = 10;
    public static final int HTTP_WHAT_INIT_REGION_LIST = 20;

    private ImageView mCloseImageView;
    private RecyclerView mRegionRecyclerView;
    private RecyclerView mSelectedRecyclerView;
    private String mApi;
    private LinearLayoutManager mLayoutManager;
    private LinearLayoutManager mSelectedLayoutManager;
    private SelectedRegionAdapter mSelectedRegionAdapter;
    private RegionAdapter mRegionAdapter;
    private List<List<ResponseRegionItemModel>> mData;
    private List<String> autoLoadRegionList;
    private boolean mHasTitle;
    private boolean mSelectable;
    private List<String> mSelectedRegionList;
    private OnRegionSelectListener mListener;
    private RelativeLayout mTitleWrapper;
    private String mRegionCode;
    private List<String> levelNames = new ArrayList<>();

    public static RegionFragment newInstance(String api, boolean hasTitle, boolean selectable, ArrayList<String> selectedRegionList) {
        RegionFragment fragment = new RegionFragment();
        Bundle argument = new Bundle();
        argument.putString(KEY_API, api);
        argument.putBoolean(KEY_HAS_TITLE, hasTitle);
        argument.putBoolean(KEY_SELECTABLE, selectable);
        argument.putStringArrayList(KEY_REGION_LIST, selectedRegionList);
        fragment.setArguments(argument);
        return fragment;
    }

    @Override
    public void onClick(View view) {
        ViewType viewType = CommonUtils.commonGetViewTypeOfTag(view);
        int position = CommonUtils.getPositionOfTag(view);
        switch (viewType) {
            case VIEW_TYPE_REGION:
                selectRegion(position);
                break;
            case VIEW_TYPE_PLUS:
                addRegion(position);
                break;
            case VIEW_TYPE_SELECTED_REGION:
                selectSelectedRegion(position);
                break;
            case VIEW_TYPE_CLOSE:
                getActivity().finish();
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLayoutId = R.layout.fragment_region;
        mData = new ArrayList<>();
        mRegionAdapter = new RegionAdapter();
        mRegionAdapter.onClickListener = this;
        mSelectedRegionAdapter = new SelectedRegionAdapter();
        mSelectedRegionAdapter.onClickListener = this;
        autoLoadRegionList = new ArrayList<>();
        Bundle arguments = getArguments();
        if (arguments != null) {
            mApi = arguments.getString(KEY_API);
            mHasTitle = arguments.getBoolean(KEY_HAS_TITLE, true);
            mSelectable = arguments.getBoolean(KEY_SELECTABLE, false);
            mSelectedRegionList = arguments.getStringArrayList(KEY_REGION_LIST);
            if (mSelectedRegionList == null) {
                mSelectedRegionList = new ArrayList<>();
            }
            mRegionCode = arguments.getString(KEY_REGION_CODE);
        }
        mRegionAdapter.selectable = mSelectable;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mCloseImageView = (ImageView) view.findViewById(R.id.fragment_region_closeImageView);
        mRegionRecyclerView = (RecyclerView) view.findViewById(
                R.id.fragment_region_regionRecyclerView);
        mSelectedRecyclerView = (RecyclerView) view.findViewById(
                R.id.fragment_region_selectedRecyclerView);
        mTitleWrapper = (RelativeLayout) view.findViewById(R.id.fragment_region_titleWrapper);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRegionRecyclerView.setLayoutManager(mLayoutManager);
        mRegionRecyclerView.setAdapter(mRegionAdapter);
        mSelectedLayoutManager = new LinearLayoutManager(getContext());
        mSelectedLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mSelectedRecyclerView.setLayoutManager(mSelectedLayoutManager);
        mSelectedRecyclerView.setAdapter(mSelectedRegionAdapter);
        CommonUtils.commonSetViewTypeForTag(mCloseImageView, ViewType.VIEW_TYPE_CLOSE);
        mCloseImageView.setOnClickListener(this);
        if (!mHasTitle) {
            mTitleWrapper.setVisibility(View.GONE);
        }
        //        autoLoadRegion("");
        initRegionList();
        return view;
    }

    @Override
    protected void onRequestFailed(int what, String response) {
        switch (what) {
            case HTTP_WHAT_INIT_REGION_LIST:
                break;
            default:
                super.onRequestFailed(what, response);
        }
    }

    @Override
    protected void onRequestSucceed(int what, String response) {
        switch (what) {
            case HTTP_WHAT_REGION_LIST:
                getRegionCallback(response);
                break;
            case HTTP_WHAT_INIT_REGION_LIST:
                initRegionListCallback(response);
                break;
            default:
                super.onRequestSucceed(what, response);
                break;
        }
    }

    public void setListener(OnRegionSelectListener listener) {
        mListener = listener;
    }

    private void addRegion(int position) {
        List<ResponseRegionItemModel> list = mData.get(mData.size() - 1);
        ResponseRegionItemModel itemModel = list.get(position);
        mSelectedRegionList.add(itemModel.region_code);
        if (mListener != null) {
            mListener.onRegionSelected(itemModel.region_code, itemModel.region_name);
        }
        setUpAdapterData();
    }

    private void getRegion(String parentCode) {
        CommonRequest request = new CommonRequest(mApi, HTTP_WHAT_REGION_LIST);
        request.add("parent_code", parentCode);
        addRequest(request);
    }

    private void getRegionCallback(String response) {
        ResponseRegionModel model = JSON.parseObject(response, ResponseRegionModel.class);
        List<ResponseRegionItemModel> list = model.data.get(0);
        if (list.size() == 0) {
            if (onFinishFragmentListener != null) {
                Intent intent = new Intent();
                intent.putExtra(KEY_REGION_CODE, getSelectedRegionCode());
                intent.putExtra(KEY_REGION_LIST, getSelectedRegionName());
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else {
                setUpSelectedAdapterData();
                setUpAdapterData();
            }
        } else {
            mData.add(list);
            setUpSelectedAdapterData();
            setUpAdapterData();
        }
    }

    private ResponseRegionItemModel getRegionModel(List<ResponseRegionItemModel> list, String regionCode) {
        for (ResponseRegionItemModel item : list) {
            if (item.region_code.equals(regionCode)) {
                return item;
            }
        }
        return null;
    }

    private String getRegionName(List<ResponseRegionItemModel> list, String regionCode) {
        ResponseRegionItemModel ResponseRegionItemModel = getRegionModel(list, regionCode);
        if (ResponseRegionItemModel == null) {
            return "";
        }
        return ResponseRegionItemModel.region_name;
    }

    private List<ResponseRegionItemModel> getSelectedRegion() {
        List<ResponseRegionItemModel> result = new ArrayList<>();
        for (List<ResponseRegionItemModel> list : mData) {
            for (ResponseRegionItemModel item : list) {
                if (item.selected) {
                    result.add(item);
                }
            }
        }
        return result;
    }

    public void setSelectedRegion(List<String> selectedRegionList) {
        mSelectedRegionList = selectedRegionList;
        setUpAdapterData();
        //        mRegionAdapter.notifyDataSetChanged();
        //        mRegionAdapter.selectedRegionList = selectedRegionList;
    }

    private String getSelectedRegionCode() {
        List<ResponseRegionItemModel> list = getSelectedRegion();
        return list.get(list.size() - 1).region_code;
    }

    private String getSelectedRegionName() {
        List<ResponseRegionItemModel> list = getSelectedRegion();
        List<String> nameList = new ArrayList<>();
        for (ResponseRegionItemModel item : list) {
            nameList.add(item.region_name);
        }
        return CommonUtils.join(nameList, " ");
    }

    private void initRegionList() {
        if (CommonUtils.isNull(mRegionCode)) {
            mRegionCode = "";
        }
        CommonRequest request = new CommonRequest(mApi, HTTP_WHAT_INIT_REGION_LIST);
        request.add("region_code", mRegionCode);
        addRequest(request);
    }

    private void initRegionListCallback(String response) {
        ResponseRegionModel model = JSON.parseObject(response, ResponseRegionModel.class);
        levelNames = model.level_names;
        mData = model.data;
        initSelectedRegionList();
        setUpSelectedAdapterData();
        setUpAdapterData();
    }

    private void initSelectedRegionList() {
        for (List<ResponseRegionItemModel> list : mData) {
            for (ResponseRegionItemModel item : list) {
                item.selected = mRegionCode.indexOf(item.region_code) == 0;
            }
        }
    }

    private void removeAfter(List list, int position) {
        for (int i = list.size() - 1; i > position; i--) {
            list.remove(i);
        }
    }

    private void selectRegion(int position) {
        List<ResponseRegionItemModel> list = mData.get(mData.size() - 1);
        ResponseRegionItemModel ResponseRegionItemModel = list.get(position);
        for (ResponseRegionItemModel item : list) {
            item.selected = item.region_code.equals(ResponseRegionItemModel.region_code);
        }
        mRegionCode = ResponseRegionItemModel.region_code;
        getRegion(ResponseRegionItemModel.region_code);
    }

    private void selectSelectedRegion(int position) {
        mRequestQueue.cancelAll();
        if (position == mData.size() - 1) {
            return;
        }
        removeAfter(mData, position);
        setUpAdapterData();
        setUpSelectedAdapterData();
    }

    private void setUpAdapterData() {
        mRegionAdapter.data.clear();
        for (ResponseRegionItemModel item : mData.get(mData.size() - 1)) {
            item.isAdded = mSelectedRegionList.contains(item.region_code);
        }
        if (mData.size() >= 1) {
            mRegionAdapter.data.addAll(mData.get(mData.size() - 1));
        }
        mRegionAdapter.notifyDataSetChanged();
    }

    private void setUpSelectedAdapterData() {
        mSelectedRegionAdapter.data.clear();
        for (List<ResponseRegionItemModel> list : mData) {
            ResponseRegionItemModel selectedItem = new ResponseRegionItemModel();
            selectedItem.region_code = "-1";

            String nextLevel = "";
            if (!"".equals(mRegionCode)) {
                nextLevel = levelNames.get(mRegionCode.split(",").length + 1);
            } else {
                nextLevel = levelNames.get(1);
            }
            selectedItem.region_name = getString(R.string.pleaseSelect) + nextLevel;

            for (ResponseRegionItemModel item : list) {
                if (item.selected) {
                    selectedItem = item;
                }
            }

            mSelectedRegionAdapter.data.add(selectedItem);
        }
        mSelectedRegionAdapter.notifyDataSetChanged();
    }

    public interface OnRegionSelectListener {
        void onRegionSelected(String regionCode, String regionName);
    }
}
