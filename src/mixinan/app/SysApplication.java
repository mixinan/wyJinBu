package mixinan.app;  
  
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
/** 
 * һ���� �����������к�̨activity 
 * @author Administrator 
 * 
 */  
public class SysApplication extends Application {  
    //����list��������ÿһ��activity�ǹؼ�  
    private List<Activity> mList = new ArrayList<Activity>();  
    //Ϊ��ʵ��ÿ��ʹ�ø���ʱ�������µĶ���������ľ�̬����  
    private static SysApplication instance;   
    private SysApplication(){}  
    //ʵ����һ��  
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