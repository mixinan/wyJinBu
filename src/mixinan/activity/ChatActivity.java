package mixinan.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mixinan.adapter.ChatMessageAdapter;
import mixinan.app.SysApplication;
import mixinan.entity.ChatMessage;
import mixinan.entity.ChatMessage.Type;
import mixinan.util.ChatHttpUtils;
import mixinan.util.NetworkUtil;
import mixinan.util.ToastUtil;
import wyjinbu.AppTool.R;
import wyjinbu.AppTool.R.style;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ChatActivity extends Activity implements OnScrollListener,
		TextWatcher, OnLongClickListener, OnItemClickListener,
		OnItemLongClickListener {
	private Button btTool;
	private ToastUtil mToastUtil;
	private ListView listView;
	private ChatMessageAdapter adapter;
	private List<ChatMessage> data;
	private EditText et;
	private int scrollState;
	private ImageView iv_et_delete;
	private View chatSendView;
	private LinearLayout topBar;
	private TextView topBar_name;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			// �ȴ����գ����߳�������ݵķ���
			ChatMessage fromMessge = (ChatMessage) msg.obj;
			data.add(fromMessge);
			adapter.notifyDataSetChanged();
			listView.setSelection(data.size() - 1);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);

		SysApplication.getInstance().addActivity(this);
		mToastUtil = new ToastUtil();
		data = new ArrayList<ChatMessage>();

		initViews();
		initAnimation();

		// �ж�����״̬
		if (NetworkUtil.getNetWorkType(this) == NetworkUtil.NONETWORK) {
			data.add(new ChatMessage("��û�����������ȿ�������...", Type.INCOMING,
					new Date()));
		} else {
			data.add(new ChatMessage(
					"hi������С��������Ů����������������wifi�������Ҵ��롣����Ժ����ģ�Ҳ���Գ����������������ġ�",
					Type.INCOMING, new Date()));
		}

		adapter = new ChatMessageAdapter(this, data);
		listView.setAdapter(adapter);

		setListeners();
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void initViews() {
		btTool = (Button) findViewById(R.id.btChat);
		iv_et_delete = (ImageView) findViewById(R.id.chat_delete);
		chatSendView = findViewById(R.id.input_view);
		topBar = (LinearLayout) findViewById(R.id.chat_top_bar);
		topBar_name = (TextView) findViewById(R.id.chat_topbar_name);
		btTool.setSelected(true);
		listView = (ListView) findViewById(R.id.chat_listview);
		et = (EditText) findViewById(R.id.chat_et);
	}

	/**
	 * ��ʼ����
	 */
	private void initAnimation() {
		// ��ʼ�����٣���������
		TranslateAnimation tAnim = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 1.1f, Animation.RELATIVE_TO_SELF,
				0f);
		tAnim.setDuration(300);
		tAnim.setFillAfter(true);
		chatSendView.startAnimation(tAnim);

		// ��ʼ�����ڣ�����������
		Animation titleAnim = AnimationUtils.loadAnimation(this,
				R.anim.chat_init_title);
		topBar.startAnimation(titleAnim);

		// ��ʼ�����ۣ�����������
		TranslateAnimation tAnim_listView = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF,
				0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
				0f);
		tAnim_listView.setDuration(500);
		tAnim_listView.setFillAfter(true);
		tAnim_listView.setStartOffset(1635);
		listView.startAnimation(tAnim_listView);
	}

	/**
	 * �󶨸��ּ����¼�
	 */
	private void setListeners() {
		listView.setOnScrollListener(this);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
		et.addTextChangedListener(this);
		topBar_name.setOnLongClickListener(this);
		btTool.setOnLongClickListener(this);
	}

	
	/**
	 * ���»ص�ǰ̨��ȥ���ı��򽹵㣬Ŀ�ģ��������뷨�Զ����������ֽ������ࡣ
	 */
	@Override
	protected void onResume() {
		super.onResume();
		et.clearFocus();
	}

	
	/**
	 * ����¼�����
	 */
	public void doClick(View view) {
		if (view.isSelected()) {
			mToastUtil.showToast(this, "�Ѿ�������ҳ����");
			return;
		}

		switch (view.getId()) {
		case R.id.btGirl:
			startActivity(new Intent(this, GirlActivity.class));
			overridePendingTransition(0, 0);
			mToastUtil.cancelToast();
			break;

		case R.id.btBlog:
			startActivity(new Intent(this, BlogActivity.class));
			overridePendingTransition(0, 0);
			mToastUtil.cancelToast();
			break;

		case R.id.chat_delete:
			et.setText("");
			iv_et_delete.setVisibility(View.GONE);
			break;

		case R.id.chat_top_bar:
			listView.smoothScrollToPosition(0);
			break;
			
		case R.id.chat_topbar_name:
			showDialogInfo();
			break;

		case R.id.chat_bt:
			final String toMsg = et.getText().toString();
			if (TextUtils.isEmpty(toMsg)) {
				mToastUtil.showToast(this, "д�㶫���ٷ���...");
				return;
			}

			ChatMessage toMessage = new ChatMessage();
			toMessage.setDate(new Date());
			toMessage.setMsg(toMsg);
			toMessage.setType(Type.OUTCOMING);
			data.add(toMessage);
			adapter.notifyDataSetChanged();
			listView.setSelection(data.size() - 1);

			if (NetworkUtil.getNetWorkType(this) == NetworkUtil.NONETWORK) {
				data.add(new ChatMessage("û�������뿪������...", Type.INCOMING,
						new Date()));
			} else {
				et.setText("");
				iv_et_delete.setVisibility(View.GONE);

				new Thread() {
					public void run() {
						ChatMessage fromMessage = ChatHttpUtils
								.sendMessage(toMsg);
						Message m = Message.obtain();
						m.obj = fromMessage;
						handler.sendMessage(m);
					};
				}.start();
			}
			break;
		}
	}

	
	/**
	 * ������������֣���ʾ�Ի���
	 */
	private void showDialogInfo() {
		AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this, style.Theme_Transparent);
		
		View view = LayoutInflater.from(this).inflate(
				R.layout.dialog, null);
		TextView dialogText = (TextView) view.findViewById(R.id.dialog);
		dialogText.setText(getString(R.string.dialog_chat));
		builder.setView(view).setPositiveButton("�˽����",
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
	 * ȥͼ������˹���
	 */
	protected void goTheirNet() {
		Intent intent = new Intent(this, PageActivity.class);
		String net = "http://www.tuling123.com/";
		intent.putExtra("click", net);
		startActivity(intent);
	}

	
	/**
	 * �ٰ�һ���˳�
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
	 * ���������¼�,�������뷨
	 */

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(ChatActivity.this.getCurrentFocus()
					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		this.scrollState = scrollState;
	}

	/**
	 * �����ı��������¼�
	 */

	@Override
	public void afterTextChanged(Editable s) {
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (count > 0) {
			iv_et_delete.setVisibility(View.VISIBLE);
		} else {
			iv_et_delete.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onLongClick(View v) {
		switch (v.getId()) {
		case R.id.btChat:
			Animation rAnim = AnimationUtils.loadAnimation(this, R.anim.rotate);
			topBar_name.startAnimation(rAnim);
			break;

		case R.id.chat_topbar_name:
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			Uri net = Uri
					.parse("http://wpd.b.qq.com/cgi/get_m_sign.php?uin=467662473");
			intent.setData(net);
			startActivity(intent);
			break;
		}
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		mToastUtil.showToast(this, "���ǵ�" + position + "����Ϣ��"
				+ "�������ܣ�coming soon...");
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		Animation rAnim = AnimationUtils.loadAnimation(this, R.anim.rotate);
		view.startAnimation(rAnim);
		return true;
	}

}