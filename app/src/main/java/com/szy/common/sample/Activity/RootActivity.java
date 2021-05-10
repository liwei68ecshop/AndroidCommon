package com.szy.common.sample.Activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.szy.common.sample.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zongren.pullablelayout.View.PullableListView;

public class RootActivity extends AppCompatActivity {
    static final List<String> mList = new ArrayList() {{
        add("YSPEmptyRecyclerViewActivity");
        add("YSPEmptyRelativeLayoutActivity");
        add("YSPEmptyLinearLayoutActivity");
    }};

    @BindView(R.id.activity_root_listView)
    PullableListView mListViewCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
        ButterKnife.bind(this);
        mListViewCompat.setAdapter(new ArrayAdapter(this, R.layout.activity_root_item, mList));

        mListViewCompat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String className = mList.get(position);
                String fullClassName = getPackageName() + ".Activity." + className;
                try {
                    Intent intent = new Intent(RootActivity.this, Class.forName(fullClassName));
                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    Toast.makeText(RootActivity.this,
                            "ClassNotFoundException occurred,class name is " + className,
                            Toast.LENGTH_SHORT).show();
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(RootActivity.this,
                            "ActivityNotFoundException occurred,activity name is " + className,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
