package com.stories.sunny.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Charlottecao on 8/31/17.
 */

public class HttpUtil {
    /**
     * Http request.
     * @param address the request address
     * @param callback the interface to callball
     */
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
