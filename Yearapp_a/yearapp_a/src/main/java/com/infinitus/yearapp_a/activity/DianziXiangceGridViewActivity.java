package com.infinitus.yearapp_a.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.infinitus.yearapp_a.R;
import com.infinitus.yearapp_a.base.util.PrefsUtil;
import com.infinitus.yearapp_a.module.BitmapCache;
import com.infinitus.yearapp_a.module.BitmapCache.ImageCallback;
import com.infinitus.yearapp_a.module.ImageItem;
import com.infinitus.yearapp_a.utils.Constants;
import com.infinitus.yearapp_a.utils.ObjUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 制作相册 完成后 显示电子相册的界面
 *
 * @author Administrator
 */
public class DianziXiangceGridViewActivity extends BaseActivity {

    private String Tag = "MakeXiangceStepTwoActivity";

    private List<ImageItem> dataList;

    private GridView gridView;

    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setLeftButtonDrawable(R.drawable.icon_back);
        super.setRightButtonDrawable(R.drawable.icon_setting);
        setContentView(R.layout.activity_dianzi_xiangce_gridview);

        dataList = new ArrayList<ImageItem>();
        //获取sp保存了相册个数，循环把相册list对象第一张图片拿出来
        int xiangce_num = PrefsUtil.prefs("xiangce").getInt("xiangce_num", 0);
        if(xiangce_num != 0){
            for (int i = 1; i < xiangce_num+1; i++) {
                List<ImageItem> images = (List<ImageItem>) ObjUtils.getObject(this, "xiangce" + i);
                dataList.add(images.get(0));
            }
        }

    }

    @Override
    protected void initLayout(View view) {
        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        myAdapter = new MyAdapter(this);
        gridView.setAdapter(myAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int xiangce_position = position + 1;
                Intent intent = new Intent(DianziXiangceGridViewActivity.this, XiangceMainFourActivity.class);
                intent.putExtra("dataList", (Serializable)ObjUtils.getObject(DianziXiangceGridViewActivity.this, "xiangce" + xiangce_position) );//传递当前点击图片对应的相册集合
                intent.putExtra("moban",(String)ObjUtils.getObject(DianziXiangceGridViewActivity.this,"xiangce" + xiangce_position+"moban"));//传递当前点击图片对应的相册模板
                startActivityForResult(intent, Constants.MAKE_XIANG_CE);
            }
        });

        ImageView iv_logo_bg = aq.id(R.id.iv_logo_background).getImageView();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.logo_bg_in);
        iv_logo_bg.startAnimation(animation);
    }

    @Override
    protected void loadData() {
        setNetworkResult(true);
    }

    @Override
    protected void onRightButtonClick(View v) {
        Intent intent = new Intent(this, XiangceMainFourActivity.class);
        intent.putExtra("dataList", (Serializable) dataList);
        startActivity(intent);
    }

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
                convertView = View.inflate(context, R.layout.activity_dianzi_xiangce_gridview_item, null);
                holder.iv = (ImageView) convertView.findViewById(R.id.iv_image);
                holder.tv = (TextView) convertView.findViewById(R.id.tv_name);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            final ImageItem item = dataList.get(position);
            holder.tv.setText(item.imageId);
            holder.iv.setTag(item.imagePath);
            cache.displayBmp(holder.iv, item.thumbnailPath, item.imagePath, callback);//
//			ImageLoader.getInstance().loadImage(item.imagePath, holder.iv, false);
            return convertView;
        }

        class Holder {
            private ImageView iv;
            private TextView tv;
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
