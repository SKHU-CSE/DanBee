package danbee.com;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HistoryItem {
    private String startDate;
    private String endDate;
    private int kickId;
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    HistoryItem(String startDate, String endDate, int kickId) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.kickId = kickId;
    }

    public String getDate(){
        try {
            Date sDate = format1.parse(startDate);  //시작날짜
            Date eDate = format1.parse(endDate);    //종료 날짜
            Calendar c1 = Calendar.getInstance();
            c1.setTime(sDate);

            int y1 = c1.get(Calendar.YEAR);
            String M1 = changeString(c1.get(Calendar.MONTH)+1);
            String d1= changeString(c1.get(Calendar.DAY_OF_MONTH));
            String h1 = changeString(c1.get(Calendar.HOUR_OF_DAY));
            String m1 = changeString(c1.get(Calendar.MINUTE));

            c1.setTime(eDate);

            int y2 = c1.get(Calendar.YEAR);
            String M2 = changeString(c1.get(Calendar.MONTH)+1);
            String d2= changeString(c1.get(Calendar.DAY_OF_MONTH));
            String h2 = changeString(c1.get(Calendar.HOUR_OF_DAY));
            String m2 = changeString(c1.get(Calendar.MINUTE));


            String date = y1 + "." + M1 + "." + d1 + "  (" + h1 + " : " + m1 + " - " + h2 + " : " + m2 + ")";
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("test", "getDate: err"+e);
        }
        return "";
    }

    String changeString(int d){
        if( d < 10 ){
            return String.format("0%d", d);
        }
        return String.valueOf(d);
    }

    public String getTime(){
        try {
            Date sTime = format1.parse(startDate);
            Date eTime = format1.parse(endDate);

            long duration = (eTime.getTime() - sTime.getTime())/1000;
            long time;

            if (duration < 60){
                time = 1;
            }else {
                time = (eTime.getTime() - sTime.getTime())/60000;
            }

            Log.d("test", "gettime: "+duration);
            return String.valueOf(time);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("test", "gettime: err"+e);
        }
        return "";
    }

    public int getKickId() {
        return kickId;
    }

    public void setKickId(int kickId) {
        this.kickId = kickId;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
