package mixinan.activity;

import mixinan.util.TimeCompare;
//import util.ToastUtil;
import wyjinbu.AppTool.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class FirstActivity extends Activity {

	private Handler handler;
	private TextView tvTime;
	private TextView tvTextToday;
	private TextView tvTextAndroid;
	private TextView tvTextNian;
	private boolean isSelected;
	private TimeCompare timeCompare;
	private String gapDays;
	private String today;
	private String nian;

	public static final String PREFS_NAME = "MyPrefsFile";
	public static final String FIRST_RUN = "first";
	private boolean isFirstRun;
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);

		sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		editor = sp.edit();
		
		isFirstRun = sp.getBoolean(FIRST_RUN, true);

		if (isFirstRun) {
			// 显示文本
			showText();  
			//保存、提交flag
			editor.putBoolean(FIRST_RUN, false);
			editor.commit();
		} else {
			// 开启聊天界面
			startActivity(new Intent(this, ChatActivity.class));
			overridePendingTransition(0, 0);
			finish();
		}
	}
	
	

	private void showText() {
		tvTime = (TextView) findViewById(R.id.tv_time);
		tvTextToday = (TextView) findViewById(R.id.tv_text_today);
		tvTextAndroid = (TextView) findViewById(R.id.tv_text_android);
		tvTextNian = (TextView) findViewById(R.id.tv_text_nian);
		timeCompare = new TimeCompare();

		new InnerThread().start();

		tvTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isSelected = true;
				startActivity(new Intent(FirstActivity.this, ChatActivity.class));
				overridePendingTransition(0, 0);
				finish();
			}
		});

		handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				if (!isSelected) {
					startActivity(new Intent(FirstActivity.this,
							ChatActivity.class));
					overridePendingTransition(0, 0);
				}
				finish();
			}
		}, 5000);
	}



	private int i;
	private class InnerThread extends Thread {
		@Override
		public void run() {

			gapDays = timeCompare.getGapDays("2015-10-26");
			nian = timeCompare.getGapDays("2016-02-08");
			today = timeCompare.getFormattedDate(System.currentTimeMillis());

			try {
				Thread.sleep(800);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			runOnUiThread(new Runnable() {
				public void run() {
					tvTextToday.setText("今天是" + today);
				}
			});
			try {
				Thread.sleep(800);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			runOnUiThread(new Runnable() {
				public void run() {
					tvTextAndroid.setText("学习Android的第 " + gapDays + " 天");
				}
			});
			try {
				Thread.sleep(800);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			runOnUiThread(new Runnable() {
				public void run() {
					tvTextNian.setText("2016年已经过了 " + nian + " 天");
				}
			});

			for (i = 3; i > 0; i--) {

				runOnUiThread(new Runnable() {
					public void run() {
						tvTime.setText(i + "s" + "\n");
					}
				});

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
