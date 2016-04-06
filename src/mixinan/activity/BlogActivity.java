package mixinan.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import mixinan.adapter.BtAdapter;
import mixinan.adapter.SideAdapter;
import mixinan.app.SysApplication;
import mixinan.entity.Bt;
import mixinan.util.ToastUtil;
import mixinan.view.BlogListView;
import wyjinbu.AppTool.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class BlogActivity extends Activity implements OnItemClickListener,
		OnLongClickListener, OnScrollListener {
	private Button btBlog;
	private TextView tvTitle;
	private TextView tvHide;
	private ToastUtil mToastUtil;
	// private LinearLayout bottomBar;

	private List<Bt> data;
	private List<String> sideData;
	private BlogListView blogListView;
	private View listHeader;
	private ListView sideListView;

	private BtAdapter blogAdapter;
	private SideAdapter sideAdapter;

	private int scrollState;
	private Intent openUrl_Intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blog);

		SysApplication.getInstance().addActivity(BlogActivity.this);
		openUrl_Intent = new Intent(this, PageActivity.class);

		// bottomBar = (LinearLayout) findViewById(R.id.bottom_bar);
		btBlog = (Button) findViewById(R.id.btBlog);
		btBlog.setSelected(true);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvHide = (TextView) findViewById(R.id.blog_hide_info);
		mToastUtil = new ToastUtil();

		showBlogList(data);

		// 列表的显示动画
		LayoutAnimationController c = new LayoutAnimationController(
				AnimationUtils.loadAnimation(this, R.anim.blog_item));
		c.setDelay(0.2f);
		c.setOrder(LayoutAnimationController.ORDER_NORMAL);
		blogListView.setLayoutAnimation(c);

		sideListView = (ListView) findViewById(R.id.listview_side);
		sideData = setSideData();
		sideAdapter = new SideAdapter(this, sideData);
		sideListView.setAdapter(sideAdapter);

		blogListView.setOnItemClickListener(this);
		sideListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String str = sideData.get(position);
				char ch = str.charAt(0);
				int moveToPosition = blogAdapter.getPositionForSection(ch);
				if (moveToPosition != -1) {
					blogListView.smoothScrollToPosition(moveToPosition);
				}
			}
		});

		// 点击标题文字，显示dialog
		tvTitle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						BlogActivity.this, R.style.Theme_Transparent);

				View view = LayoutInflater.from(BlogActivity.this).inflate(
						R.layout.dialog, null);
				TextView dialogText = (TextView) view.findViewById(R.id.dialog);
				dialogText.setText(getString(R.string.dialog_blog));
				builder.setView(view).setPositiveButton("现在分享",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								chatWithMe();
							}
						});
				AlertDialog dialog = builder.create();
				dialog.show();
				dialog.setCancelable(true);
			}
		});

		btBlog.setOnLongClickListener(this);
		blogListView.setOnScrollListener(this);

		// Animation breathAnim = AnimationUtils
		// .loadAnimation(this, R.anim.breath);
		// bottomBar.startAnimation(breathAnim);
	}

	private List<Bt> setData() {
		data = new ArrayList<Bt>();
		Collections
				.addAll(data,
						new Bt(
								"LayoutAnimation",
								"http://blog.csdn.net/imdxt1986/article/details/6952943",
								"动画layoutanimation"),
						new Bt(
								"animation",
								"http://www.360doc.com/content/13/0102/22/6541311_257754535.shtml",
								"动画animation"),
						new Bt(
								"layoutAnimation",
								"http://blog.csdn.net/zhy_cheng/article/details/7954105",
								"动画layout"),
						new Bt("数据存储Shared Preferences",
								"http://bbs.csdn.net/topics/380252082",
								"数据sharedpreferences"),
						new Bt(
								"属性动画1",
								"http://blog.csdn.net/lmj623565791/article/details/38067475",
								"动画PropertyAnimation1"),
						new Bt(
								"属性动画2",
								"http://www.android100.org/html/201506/25/157994.html",
								"动画PropertyAnimation2"),
						new Bt("属性动画3",
								"http://www.2cto.com/kf/201411/353170.html",
								"动画PropertyAnimation3"),
						new Bt("结束所有activity",
								"http://java--hhf.iteye.com/blog/1826880",
								"组件activity"),

						new Bt(
								"9patch图片",
								"http://www.cnblogs.com/xiaoran1129/archive/2012/07/04/2576461.html",
								"控件ninepatch"),
						new Bt(
								"下拉刷新",
								"http://blog.csdn.net/guolin_blog/article/details/9255575",
								"控件refresh"),
						new Bt(
								"判断程序是否首次运行",
								"http://biancheng.dnbcw.info/shouji/452664.html",
								"数据SharedPreferences1"),
						new Bt("判断软件是否第一次运行",
								"http://www.jb51.net/article/64737.htm",
								"数据SharedPreferences2"),
						new Bt(
								"自定义dialog大小和位置",
								"http://blog.csdn.net/yu75567218/article/details/41923347",
								"控件dialog"),
						new Bt(
								"dialog自定义布局",
								"http://blog.csdn.net/wd_2076295/article/details/8508994",
								"控件dialog1"),
						new Bt(
								"LayoutAnimation两种方法",
								"http://www.cnblogs.com/0616--ataozhijia/p/3904674.html",
								"动画layoutanimationtwo"),
						new Bt("LayoutAnimationController",
								"http://www.educity.cn/wenda/101449.html",
								"动画LayoutAnimationController"),
						new Bt(
								"列表波动效果",
								"http://www.itlanbao.com/code/20151203/10000/100674.html",
								"动画listview"),
						new Bt("干货集中营", "http://gank.io/api", "Apigankio"),
						new Bt(
								"解析json格式数据",
								"http://blog.chinaunix.net/uid-25885064-id-3402449.html",
								"数据json"),

						new Bt(
								"正确处理listview的position",
								"http://blog.csdn.net/meegomeego/article/details/46907759",
								"控件listviewposition"),
						new Bt(
								"模仿锤子手机“飞行模式”dialog样式",
								"http://blog.csdn.net/qq_32467939/article/details/50603207",
								"控件dialog4"),
						new Bt(
								"去除自定义dialog黑边",
								"http://blog.csdn.net/yu75567218/article/details/41912487",
								"控件dialog3"),
						new Bt(
								"自定义Dialog背景透明及显示位置",
								"http://blog.csdn.net/ConanYang/article/details/7676518",
								"控件dialog2"));
		return data;
	}

	private void showBlogList(List<Bt> data) {
		blogListView = (BlogListView) findViewById(R.id.listView_blog);
		listHeader = blogListView.findViewById(R.id.myblog);
		data = setData();
		sortBlogListByPinyin(data);
		blogAdapter = new BtAdapter(this, data);
		blogListView.setAdapter(blogAdapter);
	}

	private List<String> setSideData() {
		List<String> data = new ArrayList<String>();
		String[] arr = (String[]) blogAdapter.getSections();
		Arrays.sort(arr);
		data = Arrays.asList(arr);
		return data;
	}

	private void sortBlogListByPinyin(List<Bt> data) {
		Collections.sort(data, new Comparator<Bt>() {
			@Override
			public int compare(Bt lhs, Bt rhs) {
				return lhs.getPinyin().compareTo(rhs.getPinyin());
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position,
			long arg3) {

		// headView 成为ListView 的第一项
//			String net = ((Bt) parent.getAdapter().getItem(position)).getUri();
//			openUrl_Intent.putExtra("click", net);
//			startActivity(openUrl_Intent);
//		}
		if (position >= 1) {
			String net = data.get(position - 1).getUri();
			openUrl_Intent.putExtra("click", net);
			startActivity(openUrl_Intent);
		}else {
			goMyBlog();
		}
	}

	/**
	 * 与我聊天
	 */
	private void chatWithMe() {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		Uri net = Uri
				.parse("http://wpd.b.qq.com/cgi/get_m_sign.php?uin=467662473");
		intent.setData(net);
		startActivity(intent);
	}

	/**
	 * 所有的doClick 事件
	 */
	public void doClick(View view) {
		if (view.isSelected()) {
			mToastUtil.showToast(this, "已经在博客页面了");
			return;
		}

		switch (view.getId()) {
		case R.id.btGirl:
			startActivity(new Intent(this, GirlActivity.class));
			overridePendingTransition(0, 0);
			mToastUtil.cancelToast();
			break;

		case R.id.btChat:
			startActivity(new Intent(this, ChatActivity.class));
			overridePendingTransition(0, 0);
			mToastUtil.cancelToast();
			break;

		case R.id.blog_top_bar:
			blogListView.smoothScrollToPosition(0);
			break;

		case R.id.myblog:
			goMyBlog();
			break;
		}
	}

	private void goMyBlog() {
		String net = "http://blog.csdn.net/qq_32467939";
		openUrl_Intent.putExtra("click", net);
		startActivity(openUrl_Intent);
	}

	/**
	 * 再按一次退出
	 */
	private long lastBackTime = 0;

	@Override
	public void onBackPressed() {
		if (System.currentTimeMillis() - lastBackTime < 1500) {
			SysApplication.getInstance().exit();
		}
		lastBackTime = System.currentTimeMillis();
		mToastUtil.showToast(this, "再按一次退出");
	}

	/**
	 * 长按监听事件
	 */
	@Override
	public boolean onLongClick(View v) {
		Animation rAnim = AnimationUtils.loadAnimation(this, R.anim.rotate);
		tvTitle.startAnimation(rAnim);
		return true;
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

		if (firstVisibleItem == 0
				&& scrollState == OnScrollListener.SCROLL_STATE_FLING) {
			// 先可见，然后变暗
			tvHide.setVisibility(View.VISIBLE);
			Animation aAnim = AnimationUtils.loadAnimation(this,
					R.anim.alpha_out);
			tvHide.startAnimation(aAnim);

			// 列表头下移 出现
			TranslateAnimation tAnim = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
					0f, Animation.RELATIVE_TO_SELF, -1f,
					Animation.RELATIVE_TO_SELF, 0f);

			tAnim.setDuration(222);
			tAnim.setFillAfter(true);
			listHeader.startAnimation(tAnim);

		} else if (firstVisibleItem == 0
				&& scrollState == SCROLL_STATE_TOUCH_SCROLL) {
			ScaleAnimation sAnim = new ScaleAnimation(1.0f, 1.0f, 1.2f, 2.0f,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 1.0f);
			sAnim.setDuration(555);
			sAnim.setFillAfter(false);
			listHeader.startAnimation(sAnim);
		}
	}

}