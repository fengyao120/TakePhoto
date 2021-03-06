package org.devio.simple;

import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.compress.CompressConfig;
import org.devio.takephoto.model.CropOptions;
import org.devio.takephoto.model.LubanOptions;
import org.devio.takephoto.model.TakePhotoOptions;

import java.io.File;


/**
 * - 支持通过相机拍照获取图片
 * - 支持从相册选择图片
 * - 支持从文件选择图片
 * - 支持多图选择
 * - 支持批量图片裁切
 * - 支持批量图片压缩
 * - 支持对图片进行压缩
 * - 支持对图片进行裁剪
 * - 支持对裁剪及压缩参数自定义
 * - 提供自带裁剪工具(可选)
 * - 支持智能选取及裁剪异常处理
 * - 支持因拍照Activity被回收后的自动恢复
 * Author: crazycodeboy
 * Date: 2016/9/21 0007 20:10
 * Version:4.0.0
 * 技术博文：http://www.devio.org
 * GitHub:https://github.com/crazycodeboy
 * Email:crazycodeboy@gmail.com
 */
public class CustomHelper {
    private View rootView;
    private RadioGroup rgCrop, rgCompress, rgFrom, rgCropSize, rgCropTool, rgShowProgressBar, rgPickTool, rgCorrectTool,
        rgRawFile;
    private EditText etCropHeight, etCropWidth, etLimit;

    public static CustomHelper of(View rootView) {
        return new CustomHelper(rootView);
    }

    private CustomHelper(View rootView) {
        this.rootView = rootView;
        init();
    }

    private void init() {
        rgCrop = (RadioGroup) rootView.findViewById(R.id.rgCrop);
        rgCompress = (RadioGroup) rootView.findViewById(R.id.rgCompress);
        rgCropSize = (RadioGroup) rootView.findViewById(R.id.rgCropSize);
        rgFrom = (RadioGroup) rootView.findViewById(R.id.rgFrom);
        rgPickTool = (RadioGroup) rootView.findViewById(R.id.rgPickTool);
        rgRawFile = (RadioGroup) rootView.findViewById(R.id.rgRawFile);
        rgCorrectTool = (RadioGroup) rootView.findViewById(R.id.rgCorrectTool);
        rgShowProgressBar = (RadioGroup) rootView.findViewById(R.id.rgShowProgressBar);
        rgCropTool = (RadioGroup) rootView.findViewById(R.id.rgCropTool);
        etCropHeight = (EditText) rootView.findViewById(R.id.etCropHeight);
        etCropWidth = (EditText) rootView.findViewById(R.id.etCropWidth);
        etLimit = (EditText) rootView.findViewById(R.id.etLimit);



    }

    public void onClick(View view, TakePhoto takePhoto) {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        Uri imageUri = Uri.fromFile(file);

        configCompress(takePhoto);
        configTakePhotoOption(takePhoto);
        switch (view.getId()) {
            case R.id.btnPickBySelect:
                int limit = Integer.parseInt(etLimit.getText().toString());
                if (limit > 1) {
                    if (rgCrop.getCheckedRadioButtonId() == R.id.rbCropYes) {
                        takePhoto.onPickMultipleWithCrop(limit, getCropOptions());
                    } else {
                        takePhoto.onPickMultiple(limit);
                    }
                    return;
                }
                if (rgFrom.getCheckedRadioButtonId() == R.id.rbFile) {
                    if (rgCrop.getCheckedRadioButtonId() == R.id.rbCropYes) {
                        takePhoto.onPickFromDocumentsWithCrop(imageUri, getCropOptions());
                    } else {
                        takePhoto.onPickFromDocuments();
                    }
                    return;
                } else {
                    if (rgCrop.getCheckedRadioButtonId() == R.id.rbCropYes) {
                        takePhoto.onPickFromGalleryWithCrop(imageUri, getCropOptions());
                    } else {
                        takePhoto.onPickFromGallery();
                    }
                }
                break;
            case R.id.btnPickByTake:
                if (rgCrop.getCheckedRadioButtonId() == R.id.rbCropYes) {
                    takePhoto.onPickFromCaptureWithCrop(imageUri, getCropOptions());
                } else {
                    takePhoto.onPickFromCapture(imageUri);
                }
                break;
            default:
                break;
        }
    }

    private void configTakePhotoOption(TakePhoto takePhoto) {
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        if (rgPickTool.getCheckedRadioButtonId() == R.id.rbPickWithOwn) {
            builder.setWithOwnGallery(true);
        }
        if (rgCorrectTool.getCheckedRadioButtonId() == R.id.rbCorrectYes) {
            builder.setCorrectImage(true);
        }
        takePhoto.setTakePhotoOptions(builder.create());

    }

    private void configCompress(TakePhoto takePhoto) {
        if (rgCompress.getCheckedRadioButtonId() != R.id.rbCompressYes) {
            takePhoto.onEnableCompress(null, false);
            return;
        }
        boolean showProgressBar = rgShowProgressBar.getCheckedRadioButtonId() == R.id.rbShowYes ? true : false;
        boolean enableRawFile = rgRawFile.getCheckedRadioButtonId() == R.id.rbRawYes ? true : false;


        LubanOptions option = new LubanOptions.Builder().setTargetDir(getPath()).create();
        CompressConfig config = CompressConfig.ofLuban(option);
        config.enableReserveRaw(enableRawFile);


        takePhoto.onEnableCompress(config, showProgressBar);
    }

    private String getPath() {
        String path = Environment.getExternalStorageDirectory() + "/TakePhotoLuban/";
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }

    private CropOptions getCropOptions() {
        if (rgCrop.getCheckedRadioButtonId() != R.id.rbCropYes) {
            return null;
        }
        int height = Integer.parseInt(etCropHeight.getText().toString());
        int width = Integer.parseInt(etCropWidth.getText().toString());
        boolean withWonCrop = rgCropTool.getCheckedRadioButtonId() == R.id.rbCropOwn ? true : false;

        CropOptions.Builder builder = new CropOptions.Builder();

        if (rgCropSize.getCheckedRadioButtonId() == R.id.rbAspect) {
            builder.setAspectX(width).setAspectY(height);
        } else {
            builder.setOutputX(width).setOutputY(height);
        }
        builder.setWithOwnCrop(withWonCrop);
        return builder.create();
    }

}
