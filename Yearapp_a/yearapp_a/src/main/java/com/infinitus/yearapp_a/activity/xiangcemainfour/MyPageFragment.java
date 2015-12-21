package com.infinitus.yearapp_a.activity.xiangcemainfour;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.acbelter.directionalcarousel.page.PageFragment;
import com.acbelter.directionalcarousel.page.PageLayout;
import com.infinitus.yearapp_a.R;
import com.infinitus.yearapp_a.utils.NativeImageLoader;

public class MyPageFragment extends PageFragment<MyPageItem> {
    @Override
    public View setupPage(PageLayout pageLayout, MyPageItem pageItem) {
        View pageContent = pageLayout.findViewById(R.id.page_content);

        /*TextView title = (TextView) pageContent.findViewById(R.id.title);
        title.setText(pageItem.getTitle());*/

        final ImageView imageView = (ImageView) pageLayout.findViewById(R.id.imageView);
        NativeImageLoader.getInstance().loadNativeImage(pageItem.getTitle(), new NativeImageLoader.NativeImageCallBack() {

            @Override
            public void onImageLoader(Bitmap bitmap, String path) {
                    imageView.setImageBitmap(bitmap);
            }
        });
        return pageContent;
    }
}
