package org.jssvc.lib.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import org.jssvc.lib.R;

/**
 * <pre>
 *     author : TOC_010
 *     time   : 2017/08/24
 *     desc   : 加载图片
 *     version: 1.0
 * </pre>
 */
public class ImageLoader {

  public static void with(Context context, ImageView view, String url) {
    Uri uri = Uri.parse(url);
    Glide.with(context)
        .load(uri)
        //.fitCenter().centerCrop()
        .diskCacheStrategy(DiskCacheStrategy.RESULT)
        .placeholder(R.drawable.icon_loading)
        .error(R.drawable.icon_loading_err)
        .into(view);
  }
}
