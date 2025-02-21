package net.esliceu.Rest_Api_Forum.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static public String getTime(){
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }
}
