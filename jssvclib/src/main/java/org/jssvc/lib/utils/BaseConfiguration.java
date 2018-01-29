package org.jssvc.lib.utils;

import android.content.Context;

/**
 * Created by jjj on 2018/1/29.
 *
 * @description: 全局配置
 */

public class BaseConfiguration {
    final Context context;

    public BaseConfiguration(final Builder builder) {
        this.context = builder.context;
    }

    public static class Builder {
        private Context context;

        public Builder(Context context) {
            this.context = context;
        }

        public BaseConfiguration build() {
            return new BaseConfiguration(this);
        }
    }
}
