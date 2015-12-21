package com.infinitus.yearapp_a.view;

import com.infinitus.yearapp_a.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class TitleBar extends RelativeLayout implements OnClickListener {
    private static final String TAG = "TitleBar";
    private final int SHOWLOGO = 0;
    private final int SHOWBACK = 1;
    private final int NOTSHOW = 2;

    private TextView mLogoName;
    private TextView mTitle;
    private TextView mIcon1;
    private TextView mIcon2;
    private RelativeLayout mRl;
    private ImageView mBack;
    float scale;

    private String logoName;
    private int logoSize;
    private int logoColor;
    private int logoIcon;
    private String titleText;
    private int titleColor;
    private int titleSize;
    private int icon1;
    private int icon2;
    private String icon2Name;
    private int icon2Size;
    private int icon2Color;
    private int backgroud;

    // private Paint mPaint;
    private TypedArray mTypedArray;
    private IconOnClickListener mListener;
    private TextView mPaddTop;

    public TitleBar(Context context) {
        this(context, null);

    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 将xml挂载到class上
        View.inflate(context, R.layout.title_bar, this);
        //获取定义的属性文件
        mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
        //根据这个值,转换为对应的dp值
        scale = context.getResources().getDisplayMetrics().density;
        Log.d(TAG, "进来了+" + scale);
        // 初始化view
        initView();

        initData();
    }

    /**
     * 功能:初始化数据
     */
    private void initData() {
        // 获得xml里定义的属性,格式为 名称_属性名 后面是默认值
        logoName = mTypedArray.getString(R.styleable.TitleBar_logoName);
        logoSize = mTypedArray.getDimensionPixelSize(R.styleable.TitleBar_logoSize, 10);
        logoColor = mTypedArray.getColor(R.styleable.TitleBar_logoColor, Color.BLACK);
        logoIcon = mTypedArray.getResourceId(R.styleable.TitleBar_logoIcon, 10);

        titleText = mTypedArray.getString(R.styleable.TitleBar_titleText);
        titleSize = mTypedArray.getDimensionPixelSize(R.styleable.TitleBar_titleSize, 15);
        titleColor = mTypedArray.getColor(R.styleable.TitleBar_titleColor, Color.BLACK);
        //boolean titleShow = mTypedArray.getBoolean(R.styleable.TitleBar_titleShow, true);

        icon1 = mTypedArray.getResourceId(R.styleable.TitleBar_icon1, 10);

        icon2 = mTypedArray.getResourceId(R.styleable.TitleBar_icon2, 10);
        icon2Name = mTypedArray.getString(R.styleable.TitleBar_icon2Name);
        icon2Size = mTypedArray.getDimensionPixelSize(R.styleable.TitleBar_icon2Size, 10);
        icon2Color = mTypedArray.getColor(R.styleable.TitleBar_icon2Color, Color.BLACK);

        backgroud = mTypedArray.getColor(R.styleable.TitleBar_backgroud, Color.BLUE);
        //int showBackOrLogo = mTypedArray.getInt(R.styleable.TitleBar_showBackOrLogo, SHOWLOGO);


        //设置背景颜色
        mRl.setBackgroundColor(backgroud);
        //mRl.setBackgroundResource(backgroud);
        if (logoName == null) {
            //没有设置文字,就显示图片
            if (logoIcon == 10) {
                mLogoName.setVisibility(INVISIBLE);
            } else {
                mLogoName.setVisibility(VISIBLE);
                //设置显示图片
                Drawable mLogoIcon = getResources().getDrawable(logoIcon);
                mLogoIcon.setBounds(0, 0, mLogoIcon.getMinimumWidth(), mLogoIcon.getMinimumHeight());
                mLogoName.setCompoundDrawables(mLogoIcon, null, null, null);
                //mLogoName.setLeft(icon2);
            }

        } else {
            //设置了文字
            mLogoName.setText(logoName);
            mLogoName.setTextSize(logoSize / scale);
            mLogoName.setTextColor(logoColor);
            //mLogoName.setVisibility(INVISIBLE);
        }


        //设置显示title
        mTitle.setText(titleText);
        mTitle.setTextSize(titleSize / scale);
        mTitle.setTextColor(titleColor);
        /*if (titleShow)
        {
			mTitle.setVisibility(VISIBLE);
			mTitle.setText(titleText);
			mTitle.setTextSize(titleSize);
			mTitle.setTextColor(titleColor);
		}else {
			mTitle.setVisibility(INVISIBLE);
		}
*/
        if (icon1 != 10) {
            mIcon1.setVisibility(VISIBLE);
            //设置显示图片
            Drawable drawable = getResources().getDrawable(icon1);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mIcon1.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        } else {
            mIcon1.setVisibility(INVISIBLE);
        }

        if (icon2Name == null) {
            //没有设置文字,就显示图片
            if (icon2 == 10) {
                mIcon2.setVisibility(INVISIBLE);
            } else {

                mIcon2.setVisibility(VISIBLE);
                //mIcon2.setWidth((int)(56/scale));
                //设置显示图片
                Drawable drawable = getResources().getDrawable(icon2);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                //mIcon2.setCompoundDrawables(drawable, null, null, null);
                mIcon2.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                //mIcon2.setLeft(icon2);
            }

        } else {
            //设置了文字
            mIcon2.setText(icon2Name);
            mIcon2.setTextSize(icon2Size / scale);
            mIcon2.setTextColor(icon2Color);

            //mIcon2.setVisibility(INVISIBLE);
        }


        // 为了保持以后使用该属性一致性,返回一个绑定资源结束的信号给资源
        mTypedArray.recycle();
    }

    public void setFull(boolean isFull) {
        if (isFull) {
            mPaddTop.setVisibility(View.VISIBLE);
        } else {
                mPaddTop.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化view
     */
    private void initView() {
        // 获取对应的id
        mLogoName = (TextView) findViewById(R.id.tv_logo);
        mPaddTop = (TextView) findViewById(R.id.tv_paddtop);
        mTitle = (TextView) findViewById(R.id.tv_title);
        mIcon2 = (TextView) findViewById(R.id.tv_icon2);
        mIcon1 = (TextView) findViewById(R.id.tv_icon1);
        mRl = (RelativeLayout) findViewById(R.id.rl);
        //mBack = (ImageView) findViewById(R.id.iv_back);


        mIcon1.setOnClickListener(this);
        mIcon2.setOnClickListener(this);
        mLogoName.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == mIcon1) {

            mListener.searchClick();
        } else if (v == mIcon2) {

            mListener.addClick();
        } else if (v == mLogoName) {
            mListener.backClick();
        }

    }

    public void searchClick() {
        //Toast.makeText(getContext(), "搜索", Toast.LENGTH_SHORT).show();
    }

    public void addClick() {
        //Toast.makeText(getContext(), "添加", Toast.LENGTH_SHORT).show();
    }

    public void backClick() {
        //Toast.makeText(getContext(), "返回", Toast.LENGTH_SHORT).show();
    }

    public void setIconOnClickListener(IconOnClickListener listener) {
        this.mListener = listener;
    }

    public interface IconOnClickListener {
        void searchClick();

        void backClick();

        void addClick();
    }

    //给logo设置方法
    public void setLogoName(String name) {
        mLogoName.setText(name);
    }

    public void setLogoColor(int color) {
        mLogoName.setTextColor(color);
    }

    public void setLogoSize(int size) {
        mLogoName.setTextSize(size);
    }

    //给title设置方法
    public void setTitleName(String name) {
        mTitle.setText(name);
    }

    public void setTitleColor(int color) {
        mTitle.setTextColor(color);
    }

    public void setTitleSize(int size) {
        mTitle.setTextSize(size);
    }


    //给icon1设置方法
    /*public void setIcon1Name(String name){
        this.icon2Name
	}
	public void setIcon1Color(int color){
		mIcon1.setTextColor(color);
	}
	public void setIcon1Size(float size){
		mIcon1.setTextSize(size);
	}*/

    //给icon1设置方法
    public void setIcon2Name(String name) {
       mIcon2.setText(name);
    }

    public void setIcon2Color(int color) {
       mIcon2.setTextColor(color);
    }

    public void setIcon2Size(int size) {
       mIcon2.setTextSize(size);
    }

    public void setIcon2Visibility(int visibility) {
        mIcon2.setVisibility(visibility);
    }

	public String getIcon2Name() {
		return mIcon2.getText().toString();
	}

    
    

}
