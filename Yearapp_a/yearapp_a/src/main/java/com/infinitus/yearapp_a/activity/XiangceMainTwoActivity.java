package com.infinitus.yearapp_a.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.infinitus.yearapp_a.R;
import com.infinitus.yearapp_a.module.AlbumHelper;
import com.infinitus.yearapp_a.module.ImageBucket;
import com.infinitus.yearapp_a.module.ImageGridAdapter;
import com.infinitus.yearapp_a.module.ImageItem;
import com.infinitus.yearapp_a.utils.Constants;
import com.infinitus.yearapp_a.utils.DialogUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 点击相册某个文件夹跳转过来的页面
 * 当前页面显示该文件夹的图片，用户点击就会打钩选中
 */
public class XiangceMainTwoActivity extends BaseActivity {
    public static final String EXTRA_IMAGE_LIST = "imagelist";

    List<ImageItem> dataList;
    GridView gridView;
    ImageGridAdapter adapter;
    AlbumHelper helper;
    Button bt;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(XiangceMainTwoActivity.this, "最多选择9张图片", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isComeFromBrowseXiangceActivity = getIntent().getBooleanExtra("isComeFromBrowseXiangceActivity", false);
        super.setLeftButtonDrawable(R.drawable.icon_back2);
        if (!isComeFromBrowseXiangceActivity) {
            super.setRightButtonText("完成");
        }
        setContentView(R.layout.act_image_grid);
    }

    @Override
    protected void initLayout(View view) {
        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());

        dataList = (List<ImageItem>) getIntent().getSerializableExtra(EXTRA_IMAGE_LIST);
//        dataList = new ArrayList<ImageItem>();

//        initData();
        initView();
//		bt = (Button) findViewById(R.id.bt);
//		bt.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View v) {
//				ArrayList<String> list = new ArrayList<String>();
//				Collection<String> c = adapter.map.values();
//				Iterator<String> it = c.iterator();
//				for (; it.hasNext();) {
//					list.add(it.next());
//				}
//
//				if (Bimp.act_bool) {
//					setResult(Activity.RESULT_OK);
//					Bimp.act_bool = false;
//				}
//				for (int i = 0; i < list.size(); i++) {
//					if (Bimp.bmp.size() < 9) {
//						try {
//							Bitmap bm = Bimp.revitionImageSize(list.get(i));
//							Bimp.bmp.add(bm);
//
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//					}
//				}
//
//				finish();
//			}
//
//		});
    }

    @Override
    protected void loadData() {
        setNetworkResult(true);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        List<ImageBucket> imageBuckets = helper.getImagesBucketList(false); // 查出的相册是按目录分的
        // 把所有目录的图片全放到一个list
        for (int i = 0; i < imageBuckets.size(); i++) {
            List<ImageItem> imageList = imageBuckets.get(i).imageList;
            for (int j = 0; j < imageList.size(); j++) {
                ImageItem imageItem = imageList.get(j);
                imageItem.isSelected = false;//保证每次进来都未选中状态
            }
            dataList.addAll(imageList);
        }
    }

    private void initView() {
        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new ImageGridAdapter(XiangceMainTwoActivity.this, dataList, mHandler);
        gridView.setAdapter(adapter);
//		adapter.setTextCallback(new TextCallback() {
//			public void onListen(int count) {
//				bt.setText("完成" + "(" + count + ")");
//			}
//		});

        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.notifyDataSetChanged();
            }

        });

    }

    @Override
    protected void onRightButtonClick(View v) {
        Map<Integer, ImageItem> selectMap = adapter.getSelectMap();
        if (selectMap.size() == 0) {
            DialogUtils.showShortToast(this, "还没选图片!");
        } else {
            //把map数据保存到list里面，方便下一个页面的适配器取数据
            List<ImageItem> selectList = new ArrayList<ImageItem>();
            for (int key : selectMap.keySet()) {
                selectList.add(selectMap.get(key));
            }
            Intent intent = new Intent(this, XiangceMainThreeActivity.class);
            intent.putExtra("selectList", (Serializable) selectList);
            startActivityForResult(intent, Constants.MAKE_XIANG_CE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case Constants.MAKE_XIANG_CE:
                XiangceMainTwoActivity.this.setResult(Constants.MAKE_XIANG_CE);
                XiangceMainTwoActivity.this.finish();
                break;
        }
    }
}
