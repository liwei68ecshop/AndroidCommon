package com.szy.common.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.szy.common.Interface.ListViewFragmentChange;
import com.szy.common.Interface.ListViewItem;
import com.szy.common.R;

import java.util.ArrayList;

/**
 * Created by zongren on 16/5/17.
 * 秦皇岛商之翼网络科技有限公司 All Rights Reserved.
 */
public abstract class ListViewFragment<T extends ListViewItem> extends CommonFragment {
    protected static final String KEY_LIST_VIEW_DATA = "com.szy.common.KEY_LIST_VIEW_DATA";
    private static final String TAG = "ListViewFragment";
    protected ArrayList<T> mDataList;
    protected T mParentData;
    protected ListViewFragmentChange<T> mListViewFragmentChange;
    protected BaseAdapter mBaseAdapter;

    protected ListView mListView;

    public abstract boolean isEnd(int position);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mParentData = arguments.getParcelable(KEY_LIST_VIEW_DATA);
        }
        mLayoutId = R.layout.fragment_list_view;
        mDataList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view != null) {
            mListView = (ListView) view.findViewById(R.id.fragment_listView_listView);
            mBaseAdapter = new BaseAdapter() {
                @Override
                public int getCount() {
                    return mDataList.size();
                }

                @Override
                public Object getItem(int position) {
                    return mDataList.get(position);
                }

                @Override
                public long getItemId(int position) {
                    return position;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    if (convertView == null) {
                        convertView = LayoutInflater.from(getActivity()).inflate(
                                R.layout.fragment_list_view_item, parent, false);
                        ((TextView) convertView).setText(
                                mDataList.get(position).getListItemTitle());
                    }
                    return convertView;
                }
            };
        }

        if (mListView != null) {
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (mListViewFragmentChange != null) {
                        if (isEnd(position)) {
                            mListViewFragmentChange.onFinishFragment(mDataList.get(position));
                        } else {
                            mListViewFragmentChange.openNextFragment(mDataList.get(position));
                        }
                    }
                }
            });
            mListView.setAdapter(mBaseAdapter);
        }

        return view;
    }

    public void setListViewFragmentChange(ListViewFragmentChange<T> listViewFragmentChange) {
        mListViewFragmentChange = listViewFragmentChange;
    }
}
