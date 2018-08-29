package com.hitomi.transferimage.activity.glide;

import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hitomi.tilibrary.style.index.NumberIndexIndicator;
import com.hitomi.tilibrary.style.progress.ProgressPieIndicator;
import com.hitomi.tilibrary.transfer.TransferConfig;
import com.hitomi.tilibrary.transfer.Transferee;
import com.hitomi.transferimage.R;
import com.hitomi.transferimage.activity.BaseActivity;
import com.wepie.glide4loader.Glide4ImageLoader;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;

public class GlideNoThumActivity extends BaseActivity {

    private RequestOptions options;

    {
        sourceImageList = new ArrayList<>();
        sourceImageList.add("http://funny.5dktv.com/sysdata/image/2018/08/02/81d95490f123d71beee48eff8b7cd7ab.jpeg");
        sourceImageList.add("http://funny.5dktv.com/sysdata/image/2018/08/02/e1d163dc2031e65354b0fda374731976.jpeg");
        sourceImageList.add("http://funny.5dktv.com/sysdata/image/2018/08/02/4d9dd29e66aab20a83ed8ebbb86a6fac.jpeg");
        sourceImageList.add("http://funny.5dktv.com/sysdata/image/2018/08/02/ef333327e76cc2fed869ac1b07b762f5.jpeg");
        sourceImageList.add("http://funny.5dktv.com/sysdata/image/2018/08/02/e7d25b54b9465e94cadb7671abecfc2c.jpeg");
        sourceImageList.add("http://funny.5dktv.com/sysdata/image/2018/08/02/bbb28a41bb472190f85e54a53259283e.gif");
        sourceImageList.add("http://funny.5dktv.com/sysdata/image/2018/08/02/03c29c6d21caf27fb8f83c3f08c5a479.jpeg");
        sourceImageList.add("http://funny.5dktv.com/sysdata/image/2018/08/02/693a5e75ed4be70820b0c2d0911c9559.jpeg");
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_grid_view;
    }

    @Override
    protected void initView() {
        gvImages = (GridView) findViewById(R.id.gv_images);
    }

    @Override
    protected void testTransferee() {
        config = TransferConfig.build()
                .setSourceImageList(sourceImageList)
                .setMissPlaceHolder(R.mipmap.ic_empty_photo)
                .setErrorPlaceHolder(R.mipmap.ic_empty_photo)
                .setProgressIndicator(new ProgressPieIndicator())
                .setIndexIndicator(new NumberIndexIndicator())
                .setJustLoadHitImage(true)
                .setImageLoader(Glide4ImageLoader.with(getApplicationContext()))
                .setOnLongClcikListener(new Transferee.OnTransfereeLongClickListener() {
                    @Override
                    public void onLongClick(ImageView imageView, int pos) {
                        saveImageByUniversal(imageView);
                    }
                })
                .create();
        options = new RequestOptions().centerCrop()
                .placeholder(R.mipmap.ic_empty_photo);

        gvImages.setAdapter(new NineGridAdapter());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != WRITE_EXTERNAL_STORAGE) {
            Toast.makeText(this, "请允许获取相册图片文件写入权限", Toast.LENGTH_SHORT).show();
        }
    }

    private class NineGridAdapter extends CommonAdapter<String> {


        public NineGridAdapter() {
            super(GlideNoThumActivity.this, R.layout.item_grid_image, sourceImageList);
        }

        @Override
        protected void convert(ViewHolder viewHolder, final String item, final int position) {
            ImageView imageView = viewHolder.getView(R.id.image_view);

            Glide.with(GlideNoThumActivity.this)
                    .load(item)
                    .apply(options)
                    .into(imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    config.setNowThumbnailIndex(position);
                    config.setOriginImageList(wrapOriginImageViewList(sourceImageList.size()));

                    transferee.apply(config).show(new Transferee.OnTransfereeStateChangeListener() {
                        @Override
                        public void onShow() {
                            Glide.with(GlideNoThumActivity.this).pauseRequests();
                        }

                        @Override
                        public void onDismiss() {
                            Glide.with(GlideNoThumActivity.this).resumeRequests();
                        }
                    });
                }
            });
        }
    }

}
