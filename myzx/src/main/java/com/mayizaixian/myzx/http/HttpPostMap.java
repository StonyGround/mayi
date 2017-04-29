package com.mayizaixian.myzx.http;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mayizaixian.myzx.utils.CacheUtils;
import com.mayizaixian.myzx.utils.ConstantUtils;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/16.
 */

public class HttpPostMap {

    private Map<String, String> map;
    private String path;

    public static boolean createNewTask = false;

    private Context context;

    public HttpPostMap(Context context, String path, Map<String, String> map) {
        this.context = context;
        if (!path.contains(ConstantUtils.HOST)) {
            path = ConstantUtils.HOST + path;
        }
//        String key = context.getSharedPreferences(Contants.USER_INFO, Context.MODE_APPEND).getString(Contants.USER_KEY, "");

//        if (!key.equals(""))
//            map.put(Contants.KEY, key);

        this.map = map;
        this.path = path;
    }

    /*public HttpPostMap(String path,Map<String, String> map) {
        if(!path.contains(Contants.HOST) && !path.contains(Contants.HOST_LOCAL)){
            path = Contants.HOST+path;
        }
        this.map = map;
        this.path = path;
    }*/

    public void postSingleBackInBackground(final HttpCallBack httpCallBack, final Handler handler) {
        createNewTask = true;
        ThreadPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                if (handler != null) {
                    createNewTask = false;
                    handler.sendEmptyMessage(httpCallBack.handleStr(postMap()) ? ConstantUtils.SUCESS : ConstantUtils.FAILURE);
                }
            }
        });
    }

    public void postSingleBackInMain(final Handler handler) {
        createNewTask = true;
        ThreadPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                if (handler == null) return;
                createNewTask = false;
                Message msg = handler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString(ConstantUtils.DATA_ON_NET, postMap());
                msg.setData(bundle);
                msg.sendToTarget();
            }
        });
    }

    public void postRunnable(Runnable runnable) {
        ThreadPool.getInstance().execute(runnable);
    }

    public void postBackInBackground(final HttpCallBack httpCallBack, final Handler handler) {
        ThreadPool.getFixedInstance().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        if (handler != null)
                            handler.sendEmptyMessage(httpCallBack.handleStr(postMap()) ? ConstantUtils.SUCESS : ConstantUtils.FAILURE);
                    }
                });
    }

    public void postBackInMain(final Handler handler) {
        ThreadPool.getFixedInstance().execute(new Runnable() {
            public void run() {
                if (handler == null) return;
                Message msg = handler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString(ConstantUtils.DATA_ON_NET, postMap());
                msg.setData(bundle);
//                Log.e("Test", "register"+msg.getData().getString(Contants.DATA_ON_NET));
                msg.sendToTarget();
            }
        });
    }

    private String postMap() {
        StringBuilder builder = new StringBuilder();

//        HttpClient hc = new DefaultHttpClient();
//        org.apache.http.client.methods.HttpPostMap hp = new org.apache.http.client.methods.HttpPostMap(path);
//
//        //设置请求超时
//        hc.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
//        hc.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);

        try {
            URL url = new URL(path+CacheUtils.getString(context, ConstantUtils.SP_TOKEN));

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

//            if (map.isEmpty()) {
//                conn.setRequestMethod("GET");
//                conn.setRequestProperty("User-Agent", "MAYI_ZX_ANDROID");
//            } else {
                //因为这个是post请求，需要设置为true
                conn.setDoOutput(true);
                conn.setDoInput(true);
                //设置以POST方式
                conn.setRequestProperty("User-Agent", "MAYI_ZX_ANDROID");
                conn.setRequestProperty("Content-Type", "application/Json; charset=UTF-8");
                conn.setRequestMethod("POST");
                //POST请求不能使用缓存
                conn.setUseCaches(false);
                conn.setConnectTimeout(30000); //30秒连接超时
                conn.setReadTimeout(30000);
//            conn.setInstanceFollowRedirects(true);


            if (!map.isEmpty()) {
                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                StringBuffer requestParameter = new StringBuffer();
//                requestParameter.append("?access_token=").append(CacheUtils.getString(context, ConstantUtils.SP_TOKEN));
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    requestParameter.append(entry.getKey())
                            .append("=")
                            .append(URLEncoder.encode(entry.getValue(), "UTF-8"))
                            .append("&");
                }
                requestParameter.deleteCharAt(requestParameter.length() - 1);
                Log.e("HttpPostMap","requestParameter----"+requestParameter.toString());
                out.writeBytes(requestParameter.toString());

                out.flush();
                out.close();
            }
            conn.connect();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader in = new InputStreamReader(
                        conn.getInputStream());
                BufferedReader br = new BufferedReader(in);
                String line = null;
                while ((line = br.readLine()) != null) {
                    builder.append(line);
                }
                in.close();
            }
            conn.disconnect();
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try {
//            //设置请求参数
//            hp.setEntity(new UrlEncodedFormEntity(map2List(), HTTP.UTF_8));
//
//            //发送post请求
//            HttpResponse response = hc.execute(hp);
//            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//
//                InputStream in = response.getEntity().getContent();
//
//                byte[] b = new byte[512];
//
//                int len;
//
//                while ((len = in.read(b)) != -1) {
//                    builder.append(new String(b, 0, len));
//                }
//
//                in.close();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return builder.toString();
    }

    private List<NameValuePair> map2List() {
        List<NameValuePair> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return list;
    }
}
