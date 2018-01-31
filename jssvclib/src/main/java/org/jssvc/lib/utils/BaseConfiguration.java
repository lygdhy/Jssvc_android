package org.jssvc.lib.utils;

import android.content.Context;

/**
 * Created by jjj on 2018/1/29.
 *
 * @description: 全局配置
 */

public class BaseConfiguration {
    final Context context;
    final String baseUrl;

    public BaseConfiguration(final Builder builder) {
        this.context = builder.context;
        this.baseUrl = builder.baseUrl;
    }

    public static class Builder {
        private Context context;
        private String baseUrl;

        public Builder(Context context) {
            this.context = context;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        public Builder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public BaseConfiguration build() {
            return new BaseConfiguration(this);
        }
    }
}
