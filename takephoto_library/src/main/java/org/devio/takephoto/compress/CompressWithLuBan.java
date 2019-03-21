package org.devio.takephoto.compress;

import android.content.Context;

import org.devio.takephoto.model.LubanOptions;
import org.devio.takephoto.model.TImage;

import java.io.File;
import java.util.ArrayList;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;


 /**
  * <pre>
  * desc  : 改用官方库鲁班实现
  * author: huangys
  * date  : 2019/3/21
  * </pre>
  */
public class CompressWithLuBan implements CompressImage {
    private ArrayList<TImage> images;
    private CompressListener listener;
    private Context context;
    private LubanOptions options;
    private ArrayList<File> files = new ArrayList<>();

    public CompressWithLuBan(Context context, CompressConfig config, ArrayList<TImage> images, CompressListener listener) {
        options = config.getLubanOptions();
        this.images = images;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public void compress() {
        if (images == null || images.isEmpty()) {
            listener.onCompressFailed(images, " images is null");
            return;
        }
        for (TImage image : images) {
            if (image == null) {
                listener.onCompressFailed(images, " There are pictures of compress  is null.");
                return;
            }
            files.add(new File(image.getOriginalPath()));
        }
        if (images.size() == 1) {
            compressOne();
        } else {
            compressMulti();
        }
    }

    private void compressOne() {
        Luban.with(context)
                .load(files.get(0))
                .ignoreBy(options.getIgnoreSize())
                .setTargetDir(options.getTargetDir())
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() { }

                    @Override
                    public void onSuccess(File file) {
                        TImage image = images.get(0);
                        image.setCompressPath(file.getPath());
                        image.setCompressed(true);
                        listener.onCompressSuccess(images);
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onCompressFailed(images, e.getMessage() + " is compress failures");
                    }
                }).launch();
    }

    private int compressTime = 0;//压缩执行次数
    private void compressMulti() {
        compressTime = 0;
        Luban.with(context)
                .load(files)
                .ignoreBy(options.getIgnoreSize())
                .setTargetDir(options.getTargetDir())
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        compressTime++;
                    }

                    @Override
                    public void onSuccess(File file) {
                        handleCompressCallBack(file);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                }).launch();
    }

    private void handleCompressCallBack(File file) {
        TImage image = images.get(compressTime - 1);
        image.setCompressed(true);
        image.setCompressPath(file.getPath());

        if(compressTime >= images.size()) {//压缩次数等于图片数，表示压缩完成
            for(TImage temp:images){
                if (!temp.isCompressed()) {//存在一张未压缩的图片，则调用压缩失败
                    listener.onCompressFailed(images, "存在压缩失败的图片");
                    return;
                }
            }
            listener.onCompressSuccess(images);
        }
    }
}
