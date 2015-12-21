package com.infinitus.yearapp_a.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.infinitus.yearapp_a.R;
import com.infinitus.yearapp_a.adapter.BrowseXiangCeTwoImageGridAdapter;
import com.infinitus.yearapp_a.module.AlbumHelper;
import com.infinitus.yearapp_a.module.ImageBucket;
import com.infinitus.yearapp_a.module.ImageItem;
import com.infinitus.yearapp_a.utils.Constants;

import java.util.List;

/**
 * 浏览相册第二步，这里点击图片后，直接调到美颜相框那里
 */
public class BrowseXiangCeTwoActivity extends BaseActivity {
    public static final String EXTRA_IMAGE_LIST = "imagelist";

    List<ImageItem> dataList;
    GridView gridView;
    BrowseXiangCeTwoImageGridAdapter adapter;
    AlbumHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setLeftButtonDrawable(R.drawable.icon_back2);
        super.setRightButtonText("分享");
        setContentView(R.layout.act_image_grid);
    }

    @Override
    protected void initLayout(View view) {
        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());
        //获取上个界面传递过来某个文件夹下面的所有图片
        dataList = (List<ImageItem>) getIntent().getSerializableExtra(EXTRA_IMAGE_LIST);
//      dataList = new ArrayList<ImageItem>();

        initView();
    }

    @Override
    protected void loadData() {
        setNetworkResult(true);
    }

    private void initView() {
        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new BrowseXiangCeTwoImageGridAdapter(BrowseXiangCeTwoActivity.this, dataList);
        gridView.setAdapter(adapter);

        //TODO 小李这里你需要处理图片的点击事件，跳到美颜相框那边
        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageItem imageItem = dataList.get(position);//图片路径就在这个对象里面，获取它的属性，有缩略图和原图两个地址，你自己取
                String imagePath = imageItem.imagePath;
                Uri uri = Uri.parse(imagePath);
                Intent intent = new Intent(BrowseXiangCeTwoActivity.this, PhotoAddFrameActivity.class);
                intent.putExtra(Constants.EXTRA_KEY_IMAGE_URI, uri);
                intent.putExtra(Constants.EXTRA_KEY_PHOTO_EDIT, false);
                startActivity(intent);
                finish();
            }

        });
    }

    @Override
    protected void onRightButtonClick(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case Constants.MAKE_XIANG_CE:
                BrowseXiangCeTwoActivity.this.setResult(Constants.MAKE_XIANG_CE);
                BrowseXiangCeTwoActivity.this.finish();
                break;
        }
    }
}
