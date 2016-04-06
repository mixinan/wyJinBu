package mixinan.adapter;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import mixinan.entity.Bt;

import wyjinbu.AppTool.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;
import android.widget.TextView;


public class BtAdapter extends BaseAdapter<Bt> implements SectionIndexer{
	
	public BtAdapter(Context context, List<Bt> data) {
		super(context, data);
	}

	private class ViewHolder{
		TextView name, firstChar;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = getLayoutInflater().inflate(R.layout.item_blog,null);
			holder.name = (TextView) convertView.findViewById(R.id.item_tv_blog_name);
			holder.firstChar = (TextView) convertView.findViewById(R.id.item_tv_blog_section);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if (position == getPositionForSection(getSectionForPosition(position))) {
			holder.firstChar.setVisibility(View.VISIBLE);
		}else {
			holder.firstChar.setVisibility(View.GONE);
		}
			
			Bt bt = getData().get(position);
			holder.name.setText(bt.getName());
			holder.firstChar.setText(""+((char)getSectionForPosition(position)));
		
		return convertView;
	}
	
	@Override
	public Object[] getSections() {
		Set<String> set = new HashSet<String>();
		for (int i = 0; i < getCount(); i++) {
			set.add(""+((char)getSectionForPosition(i)));
		}
		String[] arr = new String[set.size()];
		int i = 0;
		for (String string : set) {
			arr[i]=string;
			i++;
		}
		
		return arr;
	}
	
	
	
	@Override
	public int getPositionForSection(int section) {
		int currentSection;
		
		for (int i = 0; i < getCount(); i++) {
			currentSection = getSectionForPosition(i);
			if (currentSection == section) {
				return i;
			}
		}
		return -1;
	}
	
	
	
	@Override
	public int getSectionForPosition(int position) {
		return getData().get(position).getPinyin().toUpperCase(Locale.CHINA).charAt(0);
	}

}
