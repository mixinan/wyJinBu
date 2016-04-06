package mixinan.app;  
  
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
/** 
 * 一个类 用来结束所有后台activity 
 * @author Administrator 
 * 
 */  
public class SysApplication extends Application {  
    //运用list来保存们每一个activity是关键  
    private List<Activity> mList = new ArrayList<Activity>();  
    //为了实现每次使用该类时不创建新的对象而创建的静态对象  
    private static SysApplication instance;   
    private SysApplication(){}  
    //实例化一次  
    public synchronized static SysApplication getInstance(){   
        if (null == instance) instance = new SysApplication();      
        return instance;   
    }   
    public void addActivity(Activity activity) {   
        mList.add(activity);   
    }   
    public void exit() {   
        try {   
            for (Activity activity:mList) {   
                if (activity != null)   
                    activity.finish();   
            }   
        } catch (Exception e) {   
            e.printStackTrace();   
        } finally {   
            System.exit(0);   
        }   
    }   
    public void onLowMemory() {   
        super.onLowMemory();       
        System.gc();   
    }    
    public void removeActivity(Activity activity){
    	if (activity != null) {
			activity.finish();
		}
    }
    
    
}