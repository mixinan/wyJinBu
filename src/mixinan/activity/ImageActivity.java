package mixinan.activity;

import mixinan.view.ZoomImageView;
import wyjinbu.AppTool.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageActivity extends Activity implements OnClickListener{
	private ZoomImageView iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);

		iv = (ZoomImageView) findViewById(R.id.iv_big);
		iv.setOnClickListener(this);
		Intent intent = getIntent();
		String url = intent.getStringExtra("girl");
		ImageLoader.getInstance().displayImage(url, iv);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		
		startActivity(new Intent(this, GirlActivity.class));
		overridePendingTransition(0, 0);
	}

	@Override
	public void onClick(View v) {
		startActivity(new Intent(this, GirlActivity.class));
		overridePendingTransition(0, 0);
	}
}
