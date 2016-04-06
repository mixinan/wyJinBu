package mixinan.biz;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import mixinan.activity.GirlActivity;
import mixinan.entity.Girl;
import mixinan.util.GlobalConsts;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class GirlBiz {
	private Context context;

	public GirlBiz(Context context) {
		this.context = context;
	}

	public void loadAllGirls(int page) {
		AsyncHttpClient client = new AsyncHttpClient();
		String url = GlobalConsts.GANK_URL + page;

		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				String respText;
				try {
					respText = new String(arg2, "utf-8");
					GirlActivity girlActivity = (GirlActivity) context;
					try {
						girlActivity.updateListView(parseGirls(respText));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {

			}
		});
	}

	protected List<Girl> parseGirls(String respText) throws JSONException {
		List<Girl> girls = new ArrayList<Girl>();
		JSONObject obj = new JSONObject(respText);

		JSONArray array = obj.getJSONArray("results");
		for (int i = 0; i < array.length(); i++) {
			JSONObject object = array.getJSONObject(i);
			Girl girl = new Girl();
			girl.setWho(object.getString("who"));
			girl.setUrl(object.getString("url"));
			girl.setCreatedAt(object.getString("createdAt"));
			girls.add(girl);
		}
		return girls;
	}

}
