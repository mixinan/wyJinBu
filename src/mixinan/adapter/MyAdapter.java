package mixinan.adapter;

import java.util.ArrayList;
import java.util.List;

import mixinan.entity.Bt;

import wyjinbu.AppTool.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter{
	private Context context;
	private List<Bt> data;
	private LayoutInflater inflater;
	
	public MyAdapter(Context context, List<Bt> data) {
		super();
		this.context = context;
		this.setData(data);
		inflater = LayoutInflater.from(this.context);
	}

	public void setData(List<Bt> data) {
		if (data == null) {
			data = new ArrayList<Bt>();
		}
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}


	private class ViewHolder{
		TextView name;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_blog, null);
			holder.name = (TextView) convertView.findViewById(R.id.item_tv_blog_name);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Bt bt = data.get(position);
		holder.name.setText(bt.getName());
		
		return convertView;
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
}
