package com.infinitus.yearapp_a.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.infinitus.yearapp_a.R;
import com.infinitus.yearapp_a.module.BitmapCache;
import com.infinitus.yearapp_a.module.BitmapCache.ImageCallback;
import com.infinitus.yearapp_a.module.ImageItem;
import com.infinitus.yearapp_a.utils.Constants;
import com.infinitus.yearapp_a.utils.imageloader.ImageLoader;

import java.io.Serializable;
import java.util.List;

/**
 * 制作相册  相册模板  A B C
 *
 * @author Administrator
 */
public class XiangceMainThreeActivity extends BaseActivity implements OnClickListener {

    private String Tag = "MakeXiangceStepTwoActivity";

    private ListView listView;
    private List<ImageItem> dataList;

    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setLeftButtonDrawable(R.drawable.icon_back);
//        super.setRightButtonDrawable(R.drawable.icon_setting);
        setContentView(R.layout.activity_make_xiangce_step_two);

        dataList = (List<ImageItem>) getIntent().getSerializableExtra("selectList");
    }

    @Override
    protected void initLayout(View view) {
        //原来切图是显示选中图片列表的，现在只需要显示相册ABC三个按钮即可
//		listView = aq.id(R.id.listView).getListView();
//		adapter = new MyAdapter(this);
//		listView.setAdapter(adapter);

//        Button btn_xiangce_A = aq.id(R.id.btn_xiangce_A).getButton();
//        btn_xiangce_A.setOnClickListener(this);
//        Button btn_xiangce_B = aq.id(R.id.btn_xiangce_B).getButton();
//        btn_xiangce_B.setOnClickListener(this);
//        Button btn_xiangce_C = aq.id(R.id.btn_xiangce_C).getButton();
//        btn_xiangce_C.setOnClickListener(this);

        LinearLayout ll_xiangce_A = (LinearLayout) aq.id(R.id.ll_xiangce_A).getView();
        ll_xiangce_A.setOnClickListener(this);
        LinearLayout ll_xiangce_B = (LinearLayout) aq.id(R.id.ll_xiangce_B).getView();
        ll_xiangce_B.setOnClickListener(this);
        LinearLayout ll_xiangce_C = (LinearLayout) aq.id(R.id.ll_xiangce_C).getView();
        ll_xiangce_C.setOnClickListener(this);


        ImageView iv_logo_bg = aq.id(R.id.iv_logo_background).getImageView();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.logo_bg_in);
        iv_logo_bg.startAnimation(animation);
    }

    @Override
    protected void loadData() {
        setNetworkResult(true);
    }

    @Override
    protected void onLeftButtonClick() {
        this.setResult(Constants.KEEP_CURRENT_ACTIVITY);
        super.onLeftButtonClick();
    }

    @Override
    protected void onRightButtonClick(View v) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case Constants.MAKE_XIANG_CE:
                dataList = (List<ImageItem>) data.getSerializableExtra("dataList");
//                XiangceMainThreeActivity.this.setResult(Constants.MAKE_XIANG_CE);
//                XiangceMainThreeActivity.this.finish();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ll_xiangce_A:
                intent = new Intent(this, XiangceMainFourActivity.class);
                intent.putExtra("dataList", (Serializable) dataList);
                intent.putExtra("moban","A");//代表模板A
                intent.putExtra("isComeFromXiangceMainThree",true);
                startActivityForResult(intent, Constants.MAKE_XIANG_CE);
                break;
            case R.id.ll_xiangce_B:
                intent = new Intent(this, XiangceMainFourActivity.class);
                intent.putExtra("dataList", (Serializable) dataList);
                intent.putExtra("moban","B");//代表模板B
                intent.putExtra("isComeFromXiangceMainThree",true);
                startActivityForResult(intent, Constants.MAKE_XIANG_CE);
                break;
            case R.id.ll_xiangce_C:
                intent = new Intent(this, XiangceMainFourActivity.class);
                intent.putExtra("dataList", (Serializable) dataList);
                intent.putExtra("moban","C");//代表模板C
                intent.putExtra("isComeFromXiangceMainThree",true);
                startActivityForResult(intent, Constants.MAKE_XIANG_CE);
                break;
        }
    }

    //原来切图是显示选中图片列表的适配器，现在只需要显示相册ABC三个按钮即可
    class MyAdapter extends BaseAdapter {

        private Context context;
        private BitmapCache cache;

        public MyAdapter(Context context) {
            this.context = context;
            cache = new BitmapCache();
        }

        @Override
        public int getCount() {
            return dataList != null ? dataList.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Holder holder;

            if (convertView == null) {
                holder = new Holder();
                convertView = View.inflate(context, R.layout.activity_make_xiangce_step_two_item, null);
                holder.iv = (ImageView) convertView.findViewById(R.id.iv_image);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            final ImageItem item = dataList.get(position);
            holder.iv.setTag(item.imagePath);
//			cache.displayBmp(holder.iv, item.thumbnailPath, item.imagePath, callback);//
            ImageLoader.getInstance().loadImage(item.imagePath, holder.iv, false);
            return convertView;
        }

        class Holder {
            private ImageView iv;
        }

        ImageCallback callback = new ImageCallback() {
            public void imageLoad(ImageView imageView, Bitmap bitmap, Object... params) {
                if (imageView != null && bitmap != null) {
                    String url = (String) params[0];
                    if (url != null && url.equals((String) imageView.getTag())) {
                        ((ImageView) imageView).setImageBitmap(bitmap);
                    } else {
                        Log.e(Tag, "callback, bmp not match");
                    }
                } else {
                    Log.e(Tag, "callback, bmp null");
                }
            }
        };

    }

}
