//package com.Taximobility.driver.service;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.os.AsyncTask;
//import android.util.Base64;
//import android.view.View;
//import android.widget.ImageView;
//
//import com.cabi.driver.R;
//import com.cabi.driver.data.CommonData;
//import com.cabi.driver.interfaces.APIResult;
//import com.cabi.driver.utils.SessionSave;
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
//
//import java.io.BufferedWriter;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.io.OutputStreamWriter;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//import okhttp3.Interceptor;
//import okhttp3.MediaType;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okio.Buffer;
//import okio.BufferedSink;
//
///**
// * @author developer This AsyncTask used to communicate the application with server over HTTP request/response. Constructor get the input details List<NameValuePair>,POST or GET then url. In pre execute,Show the progress dialog. In Background,Connect and get the response. In Post execute, Return the result with interface. This class call the API without any progress on UI.
// */
//public class APIService extends AsyncTask<String, String, String>
//{
//	public Context mContext;
//	private boolean isSuccess = true;
//	private boolean GetMethod = true;
//	private Dialog mDialog;
//	private final String data;
//	private final APIResult response;
//	private InputStream in;
//
//	public APIService(Context ctx, APIResult res, String data, boolean getmethod)
//	{
//		mContext = ctx;
//		response = res;
//		this.data = data;
//		GetMethod = getmethod;
//	}
//
//	@Override
//	protected void onPreExecute()
//	{
//		// TODO Auto-generated method stub
//		super.onPreExecute();
//		View view = View.inflate(mContext, R.layout.progress_bar, null);
//		mDialog = new Dialog(mContext, R.style.dialogwinddow);
//		mDialog.setContentView(view);
//		mDialog.setCancelable(false);
//		mDialog.show();
//
//
//	}
//
//	@Override
//	protected String doInBackground(String... params) {
//		// TODO Auto-generated method stub
//
//		URL url = null;
//		String result = "s";
//		HttpURLConnection httpconnection = null;
//		InputStream inputstream = null;
//		System.out.println("naga_post");
//		try {
//			System.out.println("in_try");
//			String uri = ServiceGenerator.API_BASE_URL +ServiceGenerator.COMPANY_KEY+"/"+ "lang=" + SessionSave.getSession("Lang", mContext) + "&" + params[0]+"&" + "encode="+ServiceGenerator.DYNAMIC_AUTH_KEY;
//			url = new URL(uri);
//			System.out.println("url" + url);
//			httpconnection = (HttpURLConnection) url.openConnection();
//
//			if (!GetMethod) {
//				httpconnection.setReadTimeout(100000);
//				httpconnection.setConnectTimeout(150000);
//				httpconnection.setRequestMethod("POST");
//				httpconnection.setDoInput(true);
//				httpconnection.setDoOutput(true);
//				httpconnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
//				String query = data.toString();
//				byte[] data1 = query.getBytes("UTF-8");
//				String base64 = Base64.encodeToString(data1, Base64.DEFAULT);
//				System.out.println("query" + query.toString());
//				OutputStream os = httpconnection.getOutputStream();
//				BufferedWriter writer = new BufferedWriter(
//						new OutputStreamWriter(os, "UTF-8"));
//				writer.write(base64);
//				writer.flush();
//				writer.close();
//				os.close();
//			}
//
//			inputstream = httpconnection.getInputStream();
//
//			InputStreamReader isw = new InputStreamReader(inputstream);
//			StringBuilder stringBuffer = new StringBuilder();
//			// byte[] buffer=new byte[1024];
//
//			int read = -1;
//			while ((read = (isw.read())) != -1) {
//				stringBuffer.append((char) read);
//				publishProgress("sdf");
//
//			}
//
//			result = stringBuffer.toString();
//
//		} catch (Exception e) {
//			System.out.println("eeeee" + e + "\n");
//		} finally {
//			System.out.print("naga_string_finally");
//			try {
//				if (inputstream != null)
//					inputstream.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				System.out.println("eeeee" + e + "\n");
//				e.printStackTrace();
//			}
//
//		}
//
//		return result;
//	}
//
//	@Override
//	protected void onPostExecute(String result)
//	{
//		// TODO Auto-generated method stub
//		super.onPostExecute(result);
//		if (mDialog.isShowing())
//		{
//			mDialog.dismiss();
//		}
//		response.getResult(isSuccess, result);
//	}
//
//	public class Base64EncodeRequestInterceptor implements Interceptor {
//
//		@Override
//		public okhttp3.Response intercept(Chain chain) throws IOException {
//			Request originalRequest = chain.request();
//			Request.Builder builder = originalRequest.newBuilder();
//			System.out.println("*******"+originalRequest.method());
//			if (originalRequest.method().equalsIgnoreCase("POST")) {
//				builder = originalRequest.newBuilder()
//						.method(originalRequest.method(), encode(originalRequest.body()));
//				System.out.println("*******"+originalRequest.method()+"____"+originalRequest.body());
//			}
//
//
//			return chain.proceed(builder.build());
//		}
//
//		private RequestBody encode(final RequestBody body) {
//			return new RequestBody() {
//				@Override
//				public MediaType contentType() {
//					return body.contentType();
//				}
//
//				@Override
//				public void writeTo(BufferedSink sink) throws IOException {
//					Buffer buffer = new Buffer();
//					body.writeTo(buffer);
//					byte[] encoded = Base64.encode(buffer.readByteArray(), Base64.DEFAULT);
//					sink.write(encoded);
//					buffer.close();
//					sink.close();
//					System.out.println("*******eeee"+encoded.toString());
//				}
//			};
//		}
//	}
//
//}