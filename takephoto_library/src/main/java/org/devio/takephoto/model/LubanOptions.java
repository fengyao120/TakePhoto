package org.devio.takephoto.model;

import java.io.Serializable;

/**
 * <pre>
 * desc  : 改用官方库鲁班实现
 * author: huangys
 * date  : 2019/3/21
 * </pre>
 */
public class LubanOptions implements Serializable {
    /**不压缩的阈值，单位为K*/
    private int ignoreSize = 100;
    private String targetDir;

    private LubanOptions() {
    }

    public int getIgnoreSize() {
        return ignoreSize;
    }

    public void setIgnoreSize(int ignoreSize) {
        this.ignoreSize = ignoreSize;
    }

    public String getTargetDir() {
        return targetDir;
    }

    public void setTargetDir(String targetDir) {
        this.targetDir = targetDir;
    }

    public static class Builder {
        private LubanOptions options;

        public Builder() {
            options = new LubanOptions();
        }

        /**不压缩的阈值，单位为K，默认100*/
        public Builder setIgnoreSize(int ignoreSize) {
            options.ignoreSize = ignoreSize;
            return this;
        }

        /**缓存压缩图片路径*/
        public Builder setTargetDir(String targetDir) {
            options.targetDir = targetDir;
            return this;
        }

        public LubanOptions create() {
            return options;
        }
    }
}
