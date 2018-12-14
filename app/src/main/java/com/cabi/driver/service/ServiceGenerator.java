package com.cabi.driver.service;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.cabi.driver.BuildConfig;
import com.cabi.driver.R;
import com.cabi.driver.utils.NC;
import com.cabi.driver.utils.SessionSave;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.concurrent.TimeUnit;

import co.example.developer.myndklibrary.AA;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSink;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by developer on 8/31/16.
 */
public class ServiceGenerator {
    public static final String DYNAMIC_AUTH_KEY = "";
    private final OkHttpClient.Builder httpClient;
    private final Retrofit.Builder builder;
    public static String API_BASE_URL = "";

    public static final String COMPANY_KEY = "=";

    //  public static String API_BASE_URL = "http://revamp.taximobility.com/mobileapi117/index/";
//http://revamp.taximobility.com/


    public ServiceGenerator(Context c, boolean dont_encode) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        Base64EncodeRequestInterceptor b = new Base64EncodeRequestInterceptor(SessionSave.getSession("api_key", c),c);
        DecryptedPayloadInterceptor d = new DecryptedPayloadInterceptor(c);
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient = new OkHttpClient.Builder().connectTimeout(40, TimeUnit.SECONDS).readTimeout(40, TimeUnit.SECONDS);
        httpClient.interceptors().add(logging);
        httpClient.interceptors().add(b);
        if (!dont_encode)
            httpClient.addInterceptor(d);
        builder = new Retrofit.Builder()
                .baseUrl(SessionSave.getSession("base_url", c))
                .addConverterFactory(GsonConverterFactory.create());
    }


    public ServiceGenerator(Context c) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        Base64EncodeRequestInterceptor b = new Base64EncodeRequestInterceptor(SessionSave.getSession("api_key", c),c);
        DecryptedPayloadInterceptor d = new DecryptedPayloadInterceptor(c);

        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient = new OkHttpClient.Builder().connectTimeout(40, TimeUnit.SECONDS).readTimeout(40, TimeUnit.SECONDS);
        httpClient.interceptors().add(logging);
        httpClient.interceptors().add(b);
        httpClient.addInterceptor(d);
        builder =
                new Retrofit.Builder()
                        .baseUrl(SessionSave.getSession("base_url", c))
                        .addConverterFactory(GsonConverterFactory.create());
    }

    public ServiceGenerator(String Url, Context c, boolean dont_encode) {
        API_BASE_URL = Url;
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        Base64EncodeRequestInterceptor b = new Base64EncodeRequestInterceptor(SessionSave.getSession("api_key", c),c);
        DecryptedPayloadInterceptor d = new DecryptedPayloadInterceptor(c);
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient = new OkHttpClient.Builder().connectTimeout(40, TimeUnit.SECONDS).readTimeout(40, TimeUnit.SECONDS);
        httpClient.interceptors().add(logging);
        httpClient.interceptors().add(b);
        if (!dont_encode)
            httpClient.addInterceptor(d);
        builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());
    }
    public ServiceGenerator(Context c, boolean dont_encode,String base_url) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        Base64EncodeRequestInterceptor b = new Base64EncodeRequestInterceptor(SessionSave.getSession("api_key", c), c);
        DecryptedPayloadInterceptor d = new DecryptedPayloadInterceptor(c);
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient = new OkHttpClient.Builder().connectTimeout(40, TimeUnit.SECONDS).readTimeout(40, TimeUnit.SECONDS);

        httpClient.interceptors().add(b);
        if (!dont_encode) {
            httpClient.addInterceptor(d);
            if (BuildConfig.DEBUG)
                httpClient.interceptors().add(logging);
        }

        builder = new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create());
    }

    public <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }


    public class DecryptedPayloadInterceptor implements Interceptor {


        //        public interface DecryptionStrategy {
//            String decrypt(InputStream stream);
//        }
        Context c;

        public DecryptedPayloadInterceptor(Context c) {
            this.c = c;
        }

        @Override
        public Response intercept(final Chain chain) throws IOException {
            Response response = chain.proceed(chain.request());
            //   System.out.println("!!!!*******__" + response.body());
            if (response.isSuccessful()) {
                Response.Builder newResponse = response.newBuilder();
                String contentType = response.header("Content-Type");
                if (TextUtils.isEmpty(contentType)) contentType = "application/json";
                InputStream cryptedStream = response.body().byteStream();
                String decrypted = null;
                ByteArrayOutputStream result = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = cryptedStream.read(buffer)) != -1) {
                    result.write(buffer, 0, length);
                }
                //   System.out.println("!!!!*******__%%" + result.toString("UTF-8"));

                try {
                    if (!result.toString("UTF-8").isEmpty())
                        decrypted = new AA().dd(result.toString("UTF-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (decrypted == null || decrypted.trim().isEmpty()) {

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {

                        @Override
                        public void run() {

                            Toast.makeText(c.getApplicationContext(), NC.getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                try {
                    newResponse.body(ResponseBody.create(MediaType.parse(contentType), decrypted));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Response ress = newResponse.build();
                //  System.out.println("!!!!*******ddd" + ress.body());
                return ress;
            }
            //System.out.println("!!!!*******" + response.body());
            return response;
        }
    }

    public class Base64EncodeRequestInterceptor implements Interceptor {

        // String companyKey ="MhgcMTyOkVExdW4flFwJ6M+KMoNio/tDOy/5W+tNi2c="; //local priyanka 7090
        //  String companyKey = "WKIDMUkfnlx0J9JZQGCgRBeN1AvyH49V60k90PQ2Gi8=";  // demo
      //   String companyKey = "1qfv2Nu6m8JTClxVzn/h4A==";            // live client
         String companyKey = "hTskNtAjkJswl5lxZ1ipYuFIKwUbI3Bt68fioD5/DrQ=";   // development server
         private Context mContext;
        //String companyKey = "WKIDMUkfnlx0J9JZQGCgRBeN1AvyH49V60k90PQ2Gi8=";  // local kumares

//        String companyKey = "w6W7aHwmCywfosY6JzJ/g/FzG7yOWGVqw7noRoOR7Lc="; //local devirani
Base64EncodeRequestInterceptor(String key, Context mContext) {
    if (!key.trim().isEmpty())
        companyKey = key;

    this.mContext = mContext;
}


        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
//            Response res = chain.proceed(originalRequest);
            Request.Builder builder = originalRequest.newBuilder();
            //  System.out.println("*******" + originalRequest.method());
            if (originalRequest.method().equalsIgnoreCase("POST")) {
                builder = originalRequest.newBuilder()
                        .method(originalRequest.method(), encode(originalRequest.body()));

                // System.out.println("*******" + originalRequest.method() + "____" + originalRequest.body());
            }
            // builder.addHeader("Authorization", "1234555555");
            builder.addHeader("Authorization", companyKey);


            return chain.proceed(builder.build());
        }

        private RequestBody encode(final RequestBody body) {
            return new RequestBody() {
                @Override
                public MediaType contentType() {
                    return body.contentType();
                }

                @Override
                public void writeTo(BufferedSink sink) throws IOException {
                    Buffer buffer = new Buffer();
                    body.writeTo(buffer);

                    String to_encode = new String(buffer.readByteArray(), "UTF-8");
                    String encodedString = "";
                    try {
                        encodedString = new AA().ee(to_encode);
                        // System.out.println("tripid**"+new AA().ee("{\"trip_id\":\"2850\"}"));
                    } catch (GeneralSecurityException e) {
                        e.printStackTrace();
                    }
                    System.out.println("encoded__" + encodedString);
                    System.out.println("Encoded   " + encodedString);
                    sink.write(encodedString.getBytes("ISO-8859-1"));
                    buffer.close();
                    sink.close();
                    // System.out.println("*******eeee" + encodedString.toString());
                }
            };
        }
    }


}


//package com.cabi.driver.service;
//
//import android.content.Context;
//import android.os.Handler;
//import android.os.Looper;
//import android.text.TextUtils;
//import android.widget.Toast;
//
//import com.cabi.driver.R;
//import com.cabi.driver.utils.NC;
//import com.cabi.driver.utils.SessionSave;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.concurrent.TimeUnit;
//
//import co.example.developer.myndklibrary.AA;
//import okhttp3.Interceptor;
//import okhttp3.MediaType;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//import okhttp3.ResponseBody;
//import okhttp3.logging.HttpLoggingInterceptor;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
///**
// * Created by developer on 8/31/16.
// */
//public class ServiceGenerator {
//    public static final String DYNAMIC_AUTH_KEY="";
//    private final OkHttpClient.Builder httpClient;
//    private final Retrofit.Builder builder;
//    public static String API_BASE_URL = "";
//
//    public static final String COMPANY_KEY = "=";
//
//    //  public static String API_BASE_URL = "http://revamp.taximobility.com/mobileapi117/index/";
////http://revamp.taximobility.com/
//
//
//
//    public ServiceGenerator(Context c,boolean dont_encode) {
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        Base64EncodeRequestInterceptor b = new Base64EncodeRequestInterceptor(SessionSave.getSession("api_key",c));
//        DecryptedPayloadInterceptor d = new DecryptedPayloadInterceptor(c);
//        // set your desired log level
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//        httpClient = new OkHttpClient.Builder().connectTimeout(40, TimeUnit.SECONDS).readTimeout(40, TimeUnit.SECONDS);
//        httpClient.interceptors().add(logging);
//        httpClient.interceptors().add(b);
//        //   if(!dont_encode)
//        //   httpClient.addInterceptor(d);
//        builder = new Retrofit.Builder()
//                .baseUrl(SessionSave.getSession("base_url", c))
//                .addConverterFactory(GsonConverterFactory.create());
//    }
//
//
//    public ServiceGenerator(Context c) {
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//
//        Base64EncodeRequestInterceptor b = new Base64EncodeRequestInterceptor(SessionSave.getSession("api_key",c));
//        DecryptedPayloadInterceptor d = new DecryptedPayloadInterceptor(c);
//
//        // set your desired log level
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//        httpClient = new OkHttpClient.Builder().connectTimeout(40, TimeUnit.SECONDS).readTimeout(40, TimeUnit.SECONDS);
//        httpClient.interceptors().add(logging);
//        httpClient.interceptors().add(b);
////        httpClient.addInterceptor(d);
//        builder =
//                new Retrofit.Builder()
//                        .baseUrl(SessionSave.getSession("base_url", c))
//                        .addConverterFactory(GsonConverterFactory.create());
//    }
//
//    public ServiceGenerator(String Url,Context c,boolean dont_encode ) {
//        API_BASE_URL = Url;
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        Base64EncodeRequestInterceptor b = new Base64EncodeRequestInterceptor(SessionSave.getSession("api_key",c));
//        DecryptedPayloadInterceptor d = new DecryptedPayloadInterceptor(c);
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//        httpClient = new OkHttpClient.Builder().connectTimeout(40, TimeUnit.SECONDS).readTimeout(40, TimeUnit.SECONDS);
//        httpClient.interceptors().add(logging);
//        httpClient.interceptors().add(b);
////        if (!dont_encode)
////        httpClient.addInterceptor(d);
//        builder =
//                new Retrofit.Builder()
//                        .baseUrl(API_BASE_URL)
//                        .addConverterFactory(GsonConverterFactory.create());
//    }
//
//    public <S> S createService(Class<S> serviceClass) {
//        Retrofit retrofit = builder.client(httpClient.build()).build();
//        return retrofit.create(serviceClass);
//    }
//
//
//
//    public class DecryptedPayloadInterceptor implements Interceptor {
//
//
//        //        public interface DecryptionStrategy {
////            String decrypt(InputStream stream);
////        }
//        Context c;
//        public DecryptedPayloadInterceptor( Context c) {
//            this.c = c;
//        }
//
//        @Override
//        public Response intercept(final Chain chain) throws IOException {
//            Response response = chain.proceed(chain.request());
//            //   System.out.println("!!!!*******__" + response.body());
//            if (response.isSuccessful()) {
//                Response.Builder newResponse = response.newBuilder();
//                String contentType = response.header("Content-Type");
//                if (TextUtils.isEmpty(contentType)) contentType = "application/json";
//                InputStream cryptedStream = response.body().byteStream();
//                String decrypted = null;
//                ByteArrayOutputStream result = new ByteArrayOutputStream();
//                byte[] buffer = new byte[1024];
//                int length;
//                while ((length = cryptedStream.read(buffer)) != -1) {
//                    result.write(buffer, 0, length);
//                }
//                //   System.out.println("!!!!*******__%%" + result.toString("UTF-8"));
//
//                try {
//                    if(!result.toString("UTF-8").isEmpty())
//                        decrypted = new AA().dd( result.toString("UTF-8"));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                if(decrypted==null||decrypted.trim().isEmpty()){
//
//                    Handler handler = new Handler(Looper.getMainLooper());
//                    handler.post(new Runnable() {
//
//                        @Override
//                        public void run() {
//
//                            Toast.makeText(c.getApplicationContext(), NC.getString(R.string.server_error), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//                try {
//                    newResponse.body(ResponseBody.create(MediaType.parse(contentType), decrypted));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                Response ress = newResponse.build();
//                //  System.out.println("!!!!*******ddd" + ress.body());
//                return ress;
//            }
//            //System.out.println("!!!!*******" + response.body());
//            return response;
//        }
//    }
//
//    public class Base64EncodeRequestInterceptor implements Interceptor {
//        String companyKey = "FNpfuspyEAzhjfoh2ONpWK0rsnClVL6OCaasqDQtWdI=";
//
//        Base64EncodeRequestInterceptor(String key) {
//            if(!key.trim().isEmpty())
//                companyKey = key;
//        }
//
//        @Override
//        public okhttp3.Response intercept(Chain chain) throws IOException {
//            Request originalRequest = chain.request();
////            Response res = chain.proceed(originalRequest);
//            Request.Builder builder = originalRequest.newBuilder();
//            //  System.out.println("*******" + originalRequest.method());
//            if (originalRequest.method().equalsIgnoreCase("POST")) {
//                builder = originalRequest.newBuilder()
//                        .method(originalRequest.method(), (originalRequest.body()));
//
//                // System.out.println("*******" + originalRequest.method() + "____" + originalRequest.body());
//            }
//            // builder.addHeader("Authorization", "1234555555");
//            builder.addHeader("Authorization", companyKey);
//
//
//
//            return chain.proceed(builder.build());
//        }
//
////        private RequestBody encode(final RequestBody body) {
////            return new RequestBody() {
////                @Override
////                public MediaType contentType() {
////                    return body.contentType();
////                }
////
////                @Override
////                public void writeTo(BufferedSink sink) throws IOException {
////                    Buffer buffer = new Buffer();
////                    body.writeTo(buffer);
////
////                    String to_encode = new String(buffer.readByteArray(), "UTF-8");
////                    String encodedString = "";
////                    try {
////                        encodedString = new AA().ee(to_encode);
////                       // System.out.println("tripid**"+new AA().ee("{\"trip_id\":\"2850\"}"));
////                    } catch (GeneralSecurityException e) {
////                        e.printStackTrace();
////                    }
////                    System.out.println("encoded__" + encodedString);
//////                    System.out.println("eeeeedfgg" + encodedString);
////                    sink.write(encodedString.getBytes("ISO-8859-1"));
////                    buffer.close();
////                    sink.close();
////                   // System.out.println("*******eeee" + encodedString.toString());
////                }
////            };
////        }
//    }}


//
//
//