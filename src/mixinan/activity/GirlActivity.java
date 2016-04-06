package mixinan.activity;

import java.util.List;

import mixinan.adapter.GirlAdapter;
import mixinan.app.SysApplication;
import mixinan.biz.GirlBiz;
import mixinan.entity.Girl;
import mixinan.util.NetworkUtil;
import mixinan.util.ToastUtil;
import mixinan.view.ReflashListView;
import mixinan.view.ReflashListView.IloadListener;
import wyjinbu.AppTool.R;
import wyjinbu.AppTool.R.style;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

public class GirlActivity extends Activity implements OnLongClickListener,
		IloadListener, OnItemClickListener {
	private Button btNews;
	private TextView tvTitle;
	private ToastUtil mToastUtil;

	private ReflashListView listView;
	private List<Girl> girls;
	private int page = 1;
	private GirlAdapter adapter;
	private GirlBiz biz;
	private Button bt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_girl);

		SysApplication.getInstance().addActivity(this);
		mToastUtil = new ToastUtil();
		biz = new GirlBiz(this);
		
		setViews();
		setListeners();
		
		// 获取数据，并且执行updateListView()
		biz.loadAllGirls(page);
		
		bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 判断联网状态
				if (NetworkUtil.getNetWorkType(GirlActivity.this) == NetworkUtil.NONETWORK) {
					mToastUtil.showToast(GirlActivity.this, "还是没有网络啊...");
				} else {
					biz.loadAllGirls(page);
				}
			}
		});
	}

	private void setViews() {
		listView = (ReflashListView) findViewById(R.id.girl_listview);
		// 没有数据时的显示
		bt = (Button) findViewById(R.id.girl_reLoad);
		listView.setEmptyView(findViewById(R.id.empty_girl));
		tvTitle = (TextView) findViewById(R.id.girl_topbar_name);
		btNews = (Button) findViewById(R.id.btGirl);
		btNews.setSelected(true);
	}

	private void setListeners() {
		listView.setInterface(this);
		listView.setOnItemClickListener(this);
		btNews.setOnLongClickListener(this);
	}

	/**
	 * listView 滑动到底部的监听事件实现
	 */
	@Override
	public void onLoad() {
		page += 1;
		biz.loadAllGirls(page);
	}

	/**
	 * 执行biz后该方法会被执行。得到数据，显示或更新列表
	 */
	public void updateListView(List<Girl> data) {
		girls = data;
		if (adapter == null) {
			adapter = new GirlAdapter(this, girls);
			listView.setAdapter(adapter);
		} else {
			adapter.update(girls);
			// 提示更新的数据条数
			int count = data.size();
			if (count == 0) {
				mToastUtil.showToast(this, "没有数据了");
			}else{
				mToastUtil.showToast(this, "更新" + count + "张图片");
			}
		}
	}

	/**
	 * 点击列表项的事件处理
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String url = ((Girl) parent.getAdapter().getItem(position)).getUrl();
		// 启动Activity查看大图
		Intent intent = new Intent(this, ImageActivity.class);
		intent.putExtra("girl", url);
		startActivity(intent);
	}

	/**
	 * 导航按钮的点击事件处理
	 */
	public void doClick(View view) {
		if (view.isSelected()) {
			mToastUtil.showToast(this, "已经在福利页面了");
			return;
		}

		switch (view.getId()) {
		case R.id.btChat:
			startActivity(new Intent(this, ChatActivity.class));
			overridePendingTransition(0, 0);
			mToastUtil.cancelToast();
			break;

		case R.id.btBlog:
			startActivity(new Intent(this, BlogActivity.class));
			overridePendingTransition(0, 0);
			mToastUtil.cancelToast();
			break;

		case R.id.girl_top_bar:
			listView.smoothScrollToPosition(0);
			break;
			
		case R.id.girl_topbar_name:
			showDialogInfo();
			break;
		}
	}

	/**
	 * 点击标题栏文字，显示对话框
	 */
	private void showDialogInfo() {
AlertDialog.Builder builder = new AlertDialog.Builder(GirlActivity.this, style.Theme_Transparent);
		
		View view = LayoutInflater.from(this).inflate(
				R.layout.dialog, null);
		TextView dialogText = (TextView) view.findViewById(R.id.dialog);
		dialogText.setText(getString(R.string.dialog_girl));
		builder.setView(view).setPositiveButton("去他们官网看看",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						goTheirNet();
					}
				});
		AlertDialog dialog = builder.create();
		dialog.show();
		dialog.setCancelable(true);
	}

	/**
	 * 去“干货集中营”网站
	 */
	private void goTheirNet() {
		Intent intent = new Intent(this, PageActivity.class);
		String net = "http://gank.io/api";
		intent.putExtra("click", net);
		startActivity(intent);
	}

	/**
	 * 再按一次退出应用
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
		// 长按导航按钮，旋转标题
		rotateTitle();
		return true;
	}

	/**
	 * 旋转标题
	 */
	public void rotateTitle() {
		Animation rAnim = AnimationUtils.loadAnimation(this, R.anim.rotate);
		tvTitle.startAnimation(rAnim);
	}
}