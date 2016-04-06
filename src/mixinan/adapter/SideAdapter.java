package mixinan.adapter;

import java.util.List;

import wyjinbu.AppTool.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SideAdapter extends BaseAdapter<String>{

	public SideAdapter(Context context, List<String> data) {
		super(context, data);
	}
	
	private class ViewHolder{
		TextView right_char;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = getLayoutInflater().inflate(R.layout.item_side, null);
			holder.right_char = (TextView) convertView.findViewById(R.id.tv_item_side);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.right_char.setText(getData().get(position));
		
		return convertView;
	}

}
