package org.devio.takephoto.compress;

import android.content.Context;
import android.text.TextUtils;

import org.devio.takephoto.model.TImage;

import java.io.File;
import java.util.ArrayList;


 /**
  * <pre>
  * desc  : 只保留鲁班压缩
  * author: huangys
  * date  : 2019/3/21
  * </pre>
  */
 public class CompressImageImpl {
     public static CompressImage of(Context context, CompressConfig config, ArrayList<TImage> images, CompressImage.CompressListener listener) {
         return new CompressWithLuBan(context, config, images, listener);
     }
 }
