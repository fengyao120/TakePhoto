## 在原项目的基础上做如下调整
- 改用鲁班官方库实现压缩
- 去掉鸡肋的自带压缩相关代码
- 由于鲁班压缩已经较好的控制了尺寸和大小，去掉不必要的参数配置，增加压缩缓存目录和压缩阀值参数
- 去掉对RxJava相关库的引用，有效减少未使用RxJava的项目安装包大小
- 将对鲁班压缩库、多图选择库、裁剪库的依赖，外放到实际使用项目中引用，便于控制引用库的版本统一
- 修正原鲁班压缩成功回调，不在UI线程问题
- 修正多图选择出现删除原图和来源标识错误问题
- 升级 gradle 到 3.3.1
- 升级 compileSdkVersion 和 com.android.support 到28
## 使用方式示例
[takephoto.aar 下载](./takephoto_library/aar/takephoto.aar)

```java

    implementation rootProject.ext.support["design"]//用自己项目中的版本
    implementation rootProject.ext.support["appcompat-v7"]//用自己项目中的版本
    implementation rootProject.ext.support["support-v4"]//用自己项目中的版本

    implementation (name: 'takephoto', ext: 'aar')
    implementation 'top.zibin:Luban:1.1.8'//官方鲁班
    implementation ('com.darsh.multipleimageselect:multipleimageselect:1.0.5'){//多图选择库
        exclude group: 'com.android.support'
        exclude group: 'com.github.bumptech.glide', module:'glide'
    }
	implementation ('com.soundcloud.android.crop:lib_crop:1.0.0'){//裁剪库
        exclude group: 'com.android.support'
    }
    implementation ('com.github.bumptech.glide:glide:4.8.0'){//用自己项目中的版本
        exclude group: 'com.android.support'
    }
```

