package com.infinitus.yearapp_a.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;

import com.acbelter.directionalcarousel.CarouselPagerAdapter;
import com.acbelter.directionalcarousel.CarouselViewPager;
import com.infinitus.yearapp_a.R;
import com.infinitus.yearapp_a.activity.xiangcemainfour.MyPageFragment;
import com.infinitus.yearapp_a.activity.xiangcemainfour.MyPageItem;
import com.infinitus.yearapp_a.base.util.PrefsUtil;
import com.infinitus.yearapp_a.module.BitmapCache;
import com.infinitus.yearapp_a.module.BitmapCache.ImageCallback;
import com.infinitus.yearapp_a.module.ImageItem;
import com.infinitus.yearapp_a.utils.Constants;
import com.infinitus.yearapp_a.utils.NativeImageLoader;
import com.infinitus.yearapp_a.utils.NativeImageLoader.NativeImageCallBack;
import com.infinitus.yearapp_a.utils.ObjUtils;
import com.infinitus.yearapp_a.view.TitleBar;
import com.infinitus.yearapp_a.view.TitleBar.IconOnClickListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import at.technikum.mti.fancycoverflow.FancyCoverFlow;
import at.technikum.mti.fancycoverflow.FancyCoverFlowAdapter;

/**
 * 点击相册模板ABC进来的显示图片的界面
 *
 * @author Administrator
 */
public class XiangceMainFourActivity extends FragmentActivity {

    private String Tag = "MakeXiangceStepTwoActivity";

    boolean isComeFromXiangceMainThree;

    String moban;//用于判断相册模板类型

    private TitleBar title_bar;

    private ListView listView;
    private List<ImageItem> dataList;//图片数据源
    private List<Bitmap> bitmaps = new ArrayList<Bitmap>();

    private FancyCoverFlow fancyCoverFlow;
    private FancyCoverFlowSampleAdapter adapter;

    private ViewPager viewPager;
    private List<ImageView> images;

    private CarouselPagerAdapter<MyPageItem> mPagerAdapter;
    private CarouselViewPager mViewPager;
    private ArrayList<MyPageItem> mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		 super.setLeftButtonDrawable(R.drawable.icon_back);
//		 super.setRightButtonDrawable(R.drawable.icon_setting);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        isComeFromXiangceMainThree = getIntent().getBooleanExtra("isComeFromXiangceMainThree", false);
        moban = getIntent().getStringExtra("moban");
        dataList = (List<ImageItem>) getIntent().getSerializableExtra("dataList");

        if (!"C".equals(moban)) {
            setContentView(R.layout.activity_xiangce_image_cover_flow);
//            initTitleBar();
        } else {
            setContentView(R.layout.activity_xiangce_vertical_image_carouse);
        }

        initTitleBar();

        if ("A".equals(moban)) {
            initMobanA();
        } else if ("B".equals(moban)) {
            initMobanB();
        } else {
            initMobanC();
        }


    }


    private void initTitleBar() {
        title_bar = (TitleBar) findViewById(R.id.title_bar);

        if(!isComeFromXiangceMainThree){
            title_bar.setIcon2Visibility(View.GONE);
        }

        title_bar.setIconOnClickListener(new IconOnClickListener() {

            @Override
            public void searchClick() {

            }

            @Override
            public void backClick() {
                //点击取消返回图片集合给上一个页面，以免上一个页面选择别的相册模板进来后，图片数据没了
                Intent intent = new Intent();
                intent.putExtra("dataList", (Serializable) dataList);
                XiangceMainFourActivity.this.setResult(Constants.MAKE_XIANG_CE, intent);
//                XiangceMainFourActivity.this.setResult(Constants.MAKE_XIANG_CE);
                XiangceMainFourActivity.this.finish();
            }

            @Override
            public void addClick() {
                int xiangce_num = PrefsUtil.prefs("xiangce").getInt("xiangce_num", 0);//获取当前相册个数
                int position = xiangce_num + 1;//相册个数加1
                //保存相册的集合到文件中，方便浏览相册的时候读取出来，暂时只保存一个相册，相册命名规则"xiangce"+xiangce_num
                ObjUtils.saveObject(XiangceMainFourActivity.this, dataList, "xiangce" + position);//保存当前相册所有图片的集合List对象
                ObjUtils.saveObject(XiangceMainFourActivity.this, moban, "xiangce" + position + "moban");//保存当前相册所选模板的字符串对象，电子相册可通过这里对象判断相册的模板
                PrefsUtil.prefs("xiangce").putInt("xiangce_num", position);//保存相册的个数

//                //点击取消或者完成都返回图片集合给上一个页面，以免上一个页面选择别的相册模板进来后，图片数据没了
//                Intent intent = new Intent();
//                intent.putExtra("dataList", (Serializable) dataList);
//                XiangceMainFourActivity.this.setResult(Constants.MAKE_XIANG_CE, intent);
//                XiangceMainFourActivity.this.finish();

                //清除activity任务栈，直接开新的任务栈跳到主页面
                Intent intent = new Intent(XiangceMainFourActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//
                startActivity(intent);
                XiangceMainFourActivity.this.finish();
            }
        });

    }

    private void initMobanA() {
        this.fancyCoverFlow = (FancyCoverFlow) findViewById(R.id.fancyCoverFlow);
        fancyCoverFlow.setVisibility(View.VISIBLE);

        this.fancyCoverFlow.setUnselectedAlpha(1.0f);
        this.fancyCoverFlow.setUnselectedSaturation(0.0f);
        this.fancyCoverFlow.setUnselectedScale(0.5f);
//		this.fancyCoverFlow.setSpacing(2);
        this.fancyCoverFlow.setMaxRotation(-45);
        this.fancyCoverFlow.setScaleDownGravity(0.2f);
        this.fancyCoverFlow.setActionDistance(FancyCoverFlow.ACTION_DISTANCE_AUTO);
//		this.fancyCoverFlow.setActionDistance(100);

        fancyCoverFlow.setReflectionEnabled(true);
        fancyCoverFlow.setReflectionRatio(0.3f);
        fancyCoverFlow.setReflectionGap(0);

        adapter = new FancyCoverFlowSampleAdapter(bitmaps);
        fancyCoverFlow.setAdapter(adapter);

        if (dataList != null) {
            for (int i = 0; i < dataList.size(); i++) {
                NativeImageLoader.getInstance().loadNativeImage(dataList.get(i).imagePath, new NativeImageCallBack() {

                    @Override
                    public void onImageLoader(Bitmap bitmap, String path) {
//							bitmaps.add(bitmap);
                        adapter.add(bitmap);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }

    private void initMobanB() {
        this.viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setVisibility(View.VISIBLE);


        final MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter();

        images = new ArrayList<ImageView>();
        for (int i = 0; i < dataList.size(); i++) {
            final ImageView imageView = new ImageView(XiangceMainFourActivity.this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.y1200)));
            images.add(imageView);
        }

        viewPager.setAdapter(myViewPagerAdapter);

        for (int i = 0; i < dataList.size(); i++) {
            final ImageView imageView = images.get(i);
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            Point mPoint = new Point();
            mPoint.set(getResources().getDimensionPixelSize(R.dimen.y1200), getResources().getDimensionPixelSize(R.dimen.y1200));
            NativeImageLoader.getInstance().loadNativeImage(dataList.get(i).imagePath,mPoint,new NativeImageCallBack() {

                @Override
                public void onImageLoader(Bitmap bitmap, String path) {
                    imageView.setImageBitmap(bitmap);
                    myViewPagerAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private void initMobanC() {
        mItems = new ArrayList<MyPageItem>(dataList.size());
        for (int i = 0; i < dataList.size(); i++) {
            mItems.add(new MyPageItem(dataList.get(i).imagePath));
        }

        mViewPager = (CarouselViewPager) findViewById(R.id.carousel_pager);
        mViewPager.setVisibility(View.VISIBLE);
        mPagerAdapter = new CarouselPagerAdapter<MyPageItem>(getSupportFragmentManager(),
                MyPageFragment.class, R.layout.page_layout, mItems);
//        mPagerAdapter.setOnPageClickListener(this);

        mViewPager.setAdapter(mPagerAdapter);
    }


//	@Override
//	protected void initLayout(View view) {
//	
//		
//	}

//	@Override
//	protected void loadData() {
//		setNetworkResult(true);
//	}

//	@Override
//	protected void onRightButtonClick(View v) {
//		super.onRightButtonClick(v);
//	}

    class FancyCoverFlowSampleAdapter extends FancyCoverFlowAdapter {

        private List<ImageItem> dataList;
        private List<Bitmap> bitmaps;
        private BitmapCache cache;
        private int[] images = {R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4, R
                .drawable.image5, R.drawable.image6,};

        //		public FancyCoverFlowSampleAdapter(List<ImageItem> dataList ) {
//			super();
//			this.dataList = dataList;
//			cache = new BitmapCache();
//		}
        public FancyCoverFlowSampleAdapter(List<Bitmap> bitmaps) {
            super();
            this.bitmaps = bitmaps;
            cache = new BitmapCache();
        }

//		@Override
//		public int getCount() {
//			return dataList != null ? dataList.size() : 0;
////			return images.length;
//		}


        @Override
        public int getCount() {
            return bitmaps != null ? bitmaps.size() : 0;
//			return images.length;
        }

//		@Override
//		public Integer getItem(int i) {
//			return images[i];
//		}

        @Override
        public Bitmap getItem(int i) {
            return bitmaps.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getCoverFlowItem(int i, View reuseableView, ViewGroup viewGroup) {
            ImageView imageView = null;

            if (reuseableView != null) {
                imageView = (ImageView) reuseableView;
            } else {
                imageView = new ImageView(viewGroup.getContext());
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//                imageView.setLayoutParams(new FancyCoverFlow.LayoutParams(getResources().getDimensionPixelSize(R
//                        .dimen.y300), getResources().getDimensionPixelSize(R.dimen.x450)));
                imageView.setLayoutParams(new FancyCoverFlow.LayoutParams(900,1200));
            }

//			imageView.setImageResource(this.getItem(i));
            imageView.setImageBitmap(bitmaps.get(i));
//			ImageItem imageItem = dataList.get(i);
//			imageView.setTag(imageItem.imagePath);
//			cache.displayBmp(imageView, imageItem.thumbnailPath, imageItem.imagePath, callback);
//			ImageLoader.getInstance().loadImage(imageItem.imagePath, imageView, false);
            return imageView;
        }

        ImageCallback callback = new ImageCallback() {
            public void imageLoad(ImageView imageView, Bitmap bitmap, Object... params) {
                if (imageView != null && bitmap != null) {
                    String url = (String) params[0];
                    if (url != null && url.equals((String) imageView.getTag())) {
                        ((ImageView) imageView).setImageBitmap(bitmap);
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e(Tag, "callback, bmp not match");
                    }
                } else {
                    Log.e(Tag, "callback, bmp null");
                }
            }
        };

        /**
         * 要把从地址变成bitmap后加载进来
         */
        public void add(Bitmap bitmap) {
            bitmaps.add(bitmap);
        }
    }


    class MyViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(images.get(position));
            return images.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(images.get(position));
        }
    }

}
