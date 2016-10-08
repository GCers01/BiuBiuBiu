package me.price.nicelife.okhttp;

import android.content.Context;
import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import me.price.nicelife.utils.Utils;

public class OkHttpUtil {
    private static final OkHttpClient mOkHttpClient = new OkHttpClient();


    static{
        mOkHttpClient.setConnectTimeout(300, TimeUnit.SECONDS);
    }

    public static void setCookieHandler(Context context){
        mOkHttpClient.setCookieHandler(new CookieManager(
                new PersistentCookieStore(context),
                CookiePolicy.ACCEPT_ALL));
    }
    public static Response execute(Request request) throws IOException{
        return mOkHttpClient.newCall(request).execute();
    }
    /**
     * 开启异步线程访问网络
     * @param request
     * @param responseCallback
     */
    public static void enqueue(Request request, Callback responseCallback){
        mOkHttpClient.newCall(request).enqueue(responseCallback);
    }
    /**
     * 开启异步线程访问网络, 且不在意返回结果（实现空callback）
     * @param request
     */
    public static void enqueue(Request request){
        mOkHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {

            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

            }
        });
    }
    public static String getStringFromServer(String url) throws IOException{
        Request request = new Request.Builder().url(url).build();
        Response response = execute(request);
        if (response.isSuccessful()) {
            String responseUrl = response.body().string();
            return responseUrl;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    /**
     * 为HttpGet 的 url 方便的添加1个name value 参数。
     * @param url
     * @param name
     * @param value
     * @return
     */
    public static String attachHttpGetParam(String url, String name, String value){
        return url + "?" + name + "=" + value;
    }

    public static void login(String userName, String passWord) {

        RequestBody formBody = new FormEncodingBuilder()
                .add("username", userName)
                .add("password", passWord)
                .build();
        final Request request = new Request.Builder()
                .url(Utils.webUrl + "login")
                .post(formBody)
                .build();
        OkHttpUtil.enqueue(request, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    HashMap<String, String> files = Utils.toHashMap(body);
                    if (files.get("result").equals("true")) {
                        Log.e("login", "true");
                    } else {
                        Log.e("login", "false");
                    }
                }
            }
        });
    }

    public static void register(String userName, String passWord) {

        RequestBody formBody = new FormEncodingBuilder()
                .add("username", userName)
                .add("password", passWord)
                .build();
        final Request request = new Request.Builder()
                .url(Utils.webUrl + "register")
                .post(formBody)
                .build();
        OkHttpUtil.enqueue(request, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    HashMap<String, String> files = Utils.toHashMap(body);
                    if (files.get("result").equals("true")) {
                        Log.e("register", "true");
                    }
                    else {
                        Log.e("register", "false");
                    }
                }
            }
        });
    }
    public static void android_test_login() {

        final Request request = new Request.Builder()
                .url(Utils.webUrl + "android_login_test")
                .build();
        OkHttpUtil.enqueue(request, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    HashMap<String, String> files = Utils.toHashMap(body);
                    if (files.get("result").equals("true")) {
                        Log.e("android_login_test", "true");
                    }
                    else {
                        Log.e("android_login_test", "false");
                    }
                }
            }
        });


    }
}