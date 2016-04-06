package mixinan.app;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import android.app.Application;
import android.graphics.Bitmap.CompressFormat;

public class MyApplication extends Application{
	 @Override
	    public void onCreate() {
	    	super.onCreate();
	    	
	    	// 创建默认的ImageLoader配置参数
			ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)  
	        .memoryCacheExtraOptions(480, 800) // default = device screen dimensions  
	        .discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75, null)  
	        .threadPoolSize(3) // default  
	        .threadPriority(Thread.NORM_PRIORITY - 1) // default  
	        .tasksProcessingOrder(QueueProcessingType.FIFO) // default  
	        .denyCacheImageMultipleSizesInMemory()  
	        .memoryCache(new LruMemoryCache(2 * 1024 * 1024))  
	        .memoryCacheSize(2 * 1024 * 1024)  
	        .memoryCacheSizePercentage(13) // default  
	        .discCache(new UnlimitedDiscCache(getCacheDir())) // default  
	        .discCacheSize(50 * 1024 * 1024)  
	        .discCacheFileCount(100)  
	        .discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default  
	        .imageDownloader(new BaseImageDownloader(this)) // default  
	        .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default  
	        .writeDebugLogs()  
	        .build(); 
			ImageLoader.getInstance().init(configuration);
	    }
	    
}
