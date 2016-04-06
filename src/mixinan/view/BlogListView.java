package mixinan.view;

import wyjinbu.AppTool.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

public class BlogListView extends ListView {
	View header;

	public BlogListView(Context context) {
		super(context);
		initView(context);
	}
	public BlogListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	public BlogListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private void initView(Context context){
		LayoutInflater inflater = LayoutInflater.from(context);
		header = inflater.inflate(R.layout.blog_list_header, null);
		this.addHeaderView(header);
	}
	
}
