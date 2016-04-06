package mixinan.view;

import wyjinbu.AppTool.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;

public class ChatListView extends ListView implements OnScrollListener{
	View header;
	int headerHeight;
	int firstVisibleItem;
	int scrollState;
	boolean isRemark;
	int startY;
	int state;
	final int NONE = 0;
	final int PULL = 1;
	final int RELEASE = 2;

	public ChatListView(Context context) {
		super(context);
		initView(context);
	}
	public ChatListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	public ChatListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private void initView(Context context){
		LayoutInflater inflater = LayoutInflater.from(context);
		header = inflater.inflate(R.layout.chat_top_info, null);
		measureView(header);
		headerHeight = header.getMeasuredHeight();
		topPadding(-headerHeight);
		this.addHeaderView(header);
		this.setOnScrollListener(this);
	}
	
	private void topPadding(int topPadding){
		header.setPadding(header.getPaddingLeft(), topPadding, 
				header.getPaddingRight(), header.getPaddingBottom());
	}
	
	private void measureView(View view){
		ViewGroup.LayoutParams p = view.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int width = ViewGroup.getChildMeasureSpec(0, 0, p.width);
		int height;
		int tempHeight = p.height;
		if (tempHeight > 0) {
			height = MeasureSpec.makeMeasureSpec(tempHeight, MeasureSpec.EXACTLY);
		}else {
			height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		view.measure(width, height);
	}
	
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		this.scrollState = scrollState;
	}
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
		this.firstVisibleItem = firstVisibleItem;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (firstVisibleItem == 0) {
				isRemark = true;
				startY = (int) ev.getY();
			}
			break;
			
		case MotionEvent.ACTION_MOVE:
			onMove(ev);
			break;
			
		case MotionEvent.ACTION_UP:
			if (state == RELEASE || state == PULL) {
				state = NONE;
				isRemark = false;
				reflashViewByState();
			}
			break;
		}
		return super.onTouchEvent(ev);
	}
	
	private void onMove(MotionEvent ev){
		if (!isRemark) {
			return;
		}
		int endY = (int) ev.getY();
		int space = endY - startY;
		int topPadding = space - headerHeight;
		
		switch (state) {
		case NONE:
			if (space > 0) {
				state = PULL;
				reflashViewByState();
			}
			break;

		case PULL:
			topPadding(topPadding);
			if (space >headerHeight + 30 
					&& scrollState == SCROLL_STATE_TOUCH_SCROLL) {
				state = RELEASE;
				reflashViewByState();
			}
			break;
			
		case RELEASE:
			topPadding(topPadding);
			if (space < headerHeight + 30) {
				state = PULL;
				reflashViewByState();
			}else if(space <= 0){
				state = NONE;
				isRemark = false;
				reflashViewByState();
			}
			break;
		}
	}
	
	private void reflashViewByState(){
		LinearLayout viewGroup = (LinearLayout) header.findViewById(R.id.chat_top_layout);
		
		switch (state) {
		case NONE:
			viewGroup.clearAnimation();
			topPadding(-headerHeight);
			break;
		case PULL:
			viewGroup.clearAnimation();
			Animation animation_info = AnimationUtils.loadAnimation(getContext(), R.anim.chat_list_top_info);
			header.startAnimation(animation_info);
			break;
		case RELEASE:
			viewGroup.clearAnimation();
			break;
		}
	}
	
}
