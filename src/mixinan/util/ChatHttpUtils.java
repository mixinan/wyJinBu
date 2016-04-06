package mixinan.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URLEncoder;
import java.util.Date;

import mixinan.entity.ChatMessage;
import mixinan.entity.ChatResult;
import mixinan.entity.ChatMessage.Type;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;


public class ChatHttpUtils {
	private static final String URL = "http://www.tuling123.com/openapi/api";
	private static final String API_KEY = "540e25b7538d5430041e07569e2fae30";

	public static ChatMessage sendMessage(String msg){
		ChatMessage chatMessage = new ChatMessage();
		String jsonRes = doGet(msg);
		Gson gson = new Gson();
		ChatResult result = null;
		try {
			result = gson.fromJson(jsonRes, ChatResult.class);
			chatMessage.setMsg(result.getText());
		} catch (JsonSyntaxException e) {
			chatMessage.setMsg("服务器繁忙，请稍后再试...");
		}
		chatMessage.setDate(new Date());
		chatMessage.setType(Type.INCOMING);
		
		
		return chatMessage;
	}
	
	
	public static String doGet(String msg) {
		String result = "";
		String url = setParams(msg);
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		java.net.URL urlNet;
		try {
			urlNet = new java.net.URL(url);
			HttpURLConnection conn = (HttpURLConnection) urlNet
					.openConnection();
			conn.setReadTimeout(5 * 1000);
			conn.setConnectTimeout(5 * 1000);
			conn.setRequestMethod("GET");
			is = conn.getInputStream();
			int len = -1;
			byte[] buf = new byte[128];
			baos = new ByteArrayOutputStream();
			while ((len = is.read(buf)) != -1) {
				baos.write(buf, 0, len);
			}
			baos.flush();
			result = new String(baos.toByteArray());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null)
					baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	private static String setParams(String msg) {
		String url = "";
		try {
			url = URL + "?key=" + API_KEY + "&info=" + URLEncoder.encode(msg,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url;
	}
}
