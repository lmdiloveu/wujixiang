package com.infinitus.yearapp_a.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.infinitus.yearapp_a.R;
import com.infinitus.yearapp_a.module.AlbumHelper;
import com.infinitus.yearapp_a.module.ImageBucket;
import com.infinitus.yearapp_a.module.ImageBucketAdapter;

import java.io.Serializable;
import java.util.List;

/**
 * 点击【制作相册】打开的相册以文件夹的九宫格显示
 */
public class XiangceMainOneActivity extends Activity {
    List<ImageBucket> dataList;
    GridView gridView;
    ImageBucketAdapter adapter;// 自定义的适配器
    AlbumHelper helper;
    public static final String EXTRA_IMAGE_LIST = "imagelist";
    public static Bitmap bimap;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiangce_main_one);
        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());
        initData();
        initView();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        dataList = helper.getImagesBucketList(false);
        bimap = bitmap;
    }

    /**
     * 初始化view视图
     */
    private void initView() {
        gridView = (GridView) findViewById(R.id.gridview);
        adapter = new ImageBucketAdapter(XiangceMainOneActivity.this, dataList);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(XiangceMainOneActivity.this, XiangceMainTwoActivity.class);
                intent.putExtra(XiangceMainOneActivity.EXTRA_IMAGE_LIST, (Serializable) dataList.get(position).imageList);
                startActivityForResult(intent, 100);
                finish();
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {

            case 100:
                setResult(Activity.RESULT_OK);
                finish();
                break;

            default:
                break;
        }
    }
}
