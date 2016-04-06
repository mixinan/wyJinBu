package mixinan.adapter;

import java.util.ArrayList;
import java.util.List;

import mixinan.entity.Girl;

import wyjinbu.AppTool.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class GirlAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<Girl> data;
	private DisplayImageOptions options;
	private ImageLoader imageLoader;

	public GirlAdapter(Context context) {
		super();
		this.context = context;
	}

	public GirlAdapter(Context context, List<Girl> girls) {
		this.context = context;
		setData(girls);
		inflater = LayoutInflater.from(this.context);
		imageLoader = ImageLoader.getInstance();

		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.monkey_not_see)
				.showImageOnFail(R.drawable.monkey_not_see).cacheInMemory(true)
				.cacheOnDisc(true).displayer(new RoundedBitmapDisplayer(15))
				.build();
	}

	public void setData(List<Girl> data) {
		if (data == null) {
			data = new ArrayList<Girl>();
		}
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	class ViewHolder {
		TextView tvName, tvTime;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ImageView imageView;
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_girl_listview, null);
			imageView = (ImageView) convertView.findViewById(R.id.imageView);
			holder.tvName = (TextView) convertView
					.findViewById(R.id.girl_tv_name);
			holder.tvTime = (TextView) convertView
					.findViewById(R.id.girl_tv_time);
			convertView.setTag(holder);
		} else {
			imageView = (ImageView) convertView.findViewById(R.id.imageView);
			holder = (ViewHolder) convertView.getTag();
		}
		Girl girl = data.get(position);
		holder.tvName.setText(girl.getWho());
		holder.tvTime.setText(girl.getCreatedAt());
		// 将图片显示任务增加到执行池，图片将被显示到ImageView当轮到此ImageView
		imageLoader.displayImage(girl.getUrl(), imageView, options);

		return convertView;
	}

	public void update(List<Girl> girls) {
//		data.clear();
		data.addAll(girls);
		notifyDataSetChanged();
	}

}
