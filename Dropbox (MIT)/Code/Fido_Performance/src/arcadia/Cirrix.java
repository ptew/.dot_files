package arcadia;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


import org.apache.log4j.Logger;

public class Cirrix {
    public static Logger log = null;
    public final static String contentType = Consts.CSV;
    public static String IP_Host = null;
    
    /**
     * 
     * @param cal
     * @return Returns time period string for saving the log
     */
    public static String getTimeBlock(Calendar cal){
        int hours = cal.get(Calendar.HOUR);
        for (int i = 0; i<Consts.LISTING_TIMES.length; i++){
            if (hours > Consts.LISTING_TIMES[i]-Consts.TIME_BUFFER && hours < Consts.LISTING_TIMES[i]+Consts.TIME_BUFFER){
                return Consts.LISTING_STRINGS[i];
            }
        }
        DateFormat df = new SimpleDateFormat("HH.mm");
        return df.format(cal.getTime());
    }
    
    public static String getIP() throws UnknownHostException{
        InetAddress ipAddress = InetAddress.getByName("api.lendingclub.com"); // WARNING:  this will return the IP address in the system HOSTS file, if it exists.
        return ipAddress.getHostAddress();
      }	
}
