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
		
		// ��ȡ���ݣ�����ִ��updateListView()
		biz.loadAllGirls(page);
		
		bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// �ж�����״̬
				if (NetworkUtil.getNetWorkType(GirlActivity.this) == NetworkUtil.NONETWORK) {
					mToastUtil.showToast(GirlActivity.this, "����û�����簡...");
				} else {
					biz.loadAllGirls(page);
				}
			}
		});
	}

	private void setViews() {
		listView = (ReflashListView) findViewById(R.id.girl_listview);
		// û������ʱ����ʾ
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
	 * listView �������ײ��ļ����¼�ʵ��
	 */
	@Override
	public void onLoad() {
		page += 1;
		biz.loadAllGirls(page);
	}

	/**
	 * ִ��biz��÷����ᱻִ�С��õ����ݣ���ʾ������б�
	 */
	public void updateListView(List<Girl> data) {
		girls = data;
		if (adapter == null) {
			adapter = new GirlAdapter(this, girls);
			listView.setAdapter(adapter);
		} else {
			adapter.update(girls);
			// ��ʾ���µ���������
			int count = data.size();
			if (count == 0) {
				mToastUtil.showToast(this, "û��������");
			}else{
				mToastUtil.showToast(this, "����" + count + "��ͼƬ");
			}
		}
	}

	/**
	 * ����б�����¼�����
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String url = ((Girl) parent.getAdapter().getItem(position)).getUrl();
		// ����Activity�鿴��ͼ
		Intent intent = new Intent(this, ImageActivity.class);
		intent.putExtra("girl", url);
		startActivity(intent);
	}

	/**
	 * ������ť�ĵ���¼�����
	 */
	public void doClick(View view) {
		if (view.isSelected()) {
			mToastUtil.showToast(this, "�Ѿ��ڸ���ҳ����");
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
	 * ������������֣���ʾ�Ի���
	 */
	private void showDialogInfo() {
AlertDialog.Builder builder = new AlertDialog.Builder(GirlActivity.this, style.Theme_Transparent);
		
		View view = LayoutInflater.from(this).inflate(
				R.layout.dialog, null);
		TextView dialogText = (TextView) view.findViewById(R.id.dialog);
		dialogText.setText(getString(R.string.dialog_girl));
		builder.setView(view).setPositiveButton("ȥ���ǹ�������",
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
	 * ȥ���ɻ�����Ӫ����վ
	 */
	private void goTheirNet() {
		Intent intent = new Intent(this, PageActivity.class);
		String net = "http://gank.io/api";
		intent.putExtra("click", net);
		startActivity(intent);
	}

	/**
	 * �ٰ�һ���˳�Ӧ��
	 */
	private long lastBackTime = 0;

	@Override
	public void onBackPressed() {
		if (System.currentTimeMillis() - lastBackTime < 1500) {
			SysApplication.getInstance().exit();
		}
		lastBackTime = System.currentTimeMillis();
		mToastUtil.showToast(this, "�ٰ�һ���˳�");
	}

	/**
	 * ���������¼�
	 */
	@Override
	public boolean onLongClick(View v) {
		// ����������ť����ת����
		rotateTitle();
		return true;
	}

	/**
	 * ��ת����
	 */
	public void rotateTitle() {
		Animation rAnim = AnimationUtils.loadAnimation(this, R.anim.rotate);
		tvTitle.startAnimation(rAnim);
	}
}