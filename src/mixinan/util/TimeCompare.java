package mixinan.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeCompare {
	private long currentTimeMillis;
	private long otherTimeMillis;
	private long result;
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
	private static Date date = new Date();
	
	public String getFormattedDate(long timeMillis){
		date.setTime(timeMillis);
		return sdf.format(date);
	}
	
	
	public String getGapDays(String formattedDay) {

		try {
			otherTimeMillis = sdf.parse(formattedDay).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		currentTimeMillis = System.currentTimeMillis();
		
		if (currentTimeMillis > otherTimeMillis) {
			result =(currentTimeMillis - otherTimeMillis)/(1000*60*60*24);
		} else {
			result = (otherTimeMillis - currentTimeMillis)/(1000*60*60*24);
		}
		String gapDays = String.valueOf(result);
		return gapDays;
	}
	
	
}

	