package mixinan.view;

import mixinan.util.NetworkUtil;
import mixinan.util.ToastUtil;
import wyjinbu.AppTool.R;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ReflashListView extends ListView implements OnScrollListener {
	int footerHeight;
	int totalItemCount;
	int lastVisibleItem;
	int scrollState;
	boolean isRemark;
	int startY;
	int state;
	final int NONE = 0;
	final int PULL = 1;
	final int RELEASE = 2;
	final int REFLASHING = 3;
	private View footer;
	IloadListener iLoadListener;
	private Context context;

	public ReflashListView(Context context) {
		super(context);
		initView(context);
	}

	public ReflashListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public ReflashListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private void initView(Context context) {
		this.context = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		footer = inflater.inflate(R.layout.girl_listview_footer, null);
		measureView(footer);
		footerHeight = footer.getMeasuredHeight();
		bottomPadding(-footerHeight);
		this.addFooterView(footer);
		this.setOnScrollListener(this);
	}

	private void bottomPadding(int bottomPadding) {
		footer.setPadding(footer.getPaddingLeft(), footer.getPaddingTop(),
				footer.getPaddingRight(), bottomPadding);
	}

	private void measureView(View view) {
		ViewGroup.LayoutParams p = view.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int width = ViewGroup.getChildMeasureSpec(0, 0, p.width);
		int height;
		int tempHeight = p.height;
		if (tempHeight > 0) {
			height = MeasureSpec.makeMeasureSpec(tempHeight,
					MeasureSpec.EXACTLY);
		} else {
			height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		view.measure(width, height);
	}

	/**
	 * 滑动监听事件
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		this.scrollState = scrollState;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.totalItemCount = totalItemCount;
		this.lastVisibleItem = firstVisibleItem + visibleItemCount;
	}

	/**
	 * 触摸监听事件
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 到达底部
			if (lastVisibleItem == totalItemCount) {
				isRemark = true;
				startY = (int) ev.getY();
			}
			break;

		case MotionEvent.ACTION_MOVE:
			onMove(ev);
			break;

		case MotionEvent.ACTION_UP:
			if (state == RELEASE) {
				state = REFLASHING;
				// 判断有没有联网
				if (NetworkUtil.getNetWorkType(context) == NetworkUtil.NONETWORK) {
					new ToastUtil().showToast(context, "断网啦，请连网...");
					reflashComplete();
				} else {
					// 加载数据
					reflashViewByState();
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							iLoadListener.onLoad();
							reflashComplete();
						}
					}, 888);
				}
			} else if (state == PULL) {
				state = NONE;
				isRemark = false;
				reflashViewByState();
			}
			break;

		}
		return super.onTouchEvent(ev);
	}

	private void onMove(MotionEvent ev) {
		if (!isRemark) {
			return;
		}
		int endY = (int) ev.getY();
		int fingerMoveSpace = startY - endY;
		int paddingBottom = fingerMoveSpace - footerHeight;

		switch (state) {
		case NONE:
			if (fingerMoveSpace > 0) {
				state = PULL;
				reflashViewByState();
			}
			break;

		case PULL:
			bottomPadding(paddingBottom);
			if (fingerMoveSpace > footerHeight + 50
					&& scrollState == SCROLL_STATE_TOUCH_SCROLL) {
				state = RELEASE;
				reflashViewByState();
			}
			break;

		case RELEASE:
			bottomPadding(paddingBottom);
			if (fingerMoveSpace < footerHeight + 50) {
				state = PULL;
				reflashViewByState();
			} else if (fingerMoveSpace <= 0) {
				state = NONE;
				isRemark = false;
				reflashViewByState();
			}
			break;
			
		case REFLASHING:
			break;
		}
	}

	private void reflashViewByState() {
		TextView tip = (TextView) footer.findViewById(R.id.girl_reflush);
		ProgressBar progress = (ProgressBar) footer
				.findViewById(R.id.girl_progressBar);

		switch (state) {
		case NONE:
			bottomPadding(-footerHeight);
			break;
		case PULL:
			progress.setVisibility(View.GONE);
			tip.setText("上拉可以刷新");
			break;
		case RELEASE:
			progress.setVisibility(View.GONE);
			tip.setText("松开可以刷新");
			break;
		case REFLASHING:
			bottomPadding(0);
			progress.setVisibility(View.VISIBLE);
			tip.setText("正在刷新");
			break;

		}
	}

	public void reflashComplete() {
		state = NONE;
		isRemark = false;
		reflashViewByState();
	}

	public void setInterface(IloadListener iLoadListener) {
		this.iLoadListener = iLoadListener;
	}

	public interface IloadListener {
		public void onLoad();
	}

}
