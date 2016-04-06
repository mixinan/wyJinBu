package mixinan.adapter;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;

public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {
	private Context mContext;
	private List<T> mData;
	private LayoutInflater mLayoutInflater;

	public BaseAdapter(Context context, List<T> data) {
		setContext(context);
		setData(data);
		mLayoutInflater = LayoutInflater.from(mContext);
	}

	public LayoutInflater getLayoutInflater() {
		return mLayoutInflater;
	}

	public void setContext(Context context) {
		this.mContext = context;
	}

	public Context getContext(){
		return mContext;
	}
	
	public void setData(List<T> data) {
		if (data == null) {
			data = new ArrayList<T>();
		}
		mData = data;
	}

	public List<T> getData(){
		return mData;
	}
	
	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}
