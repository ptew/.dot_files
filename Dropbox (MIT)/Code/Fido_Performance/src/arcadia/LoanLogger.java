package arcadia;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.commons.lang3.StringUtils;

import org.apache.log4j.Logger;
import org.perf4j.log4j.Log4JStopWatch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class LoanLogger {
    private static Logger log = null;
    private static String contentType = Consts.CSV;

    /**
     * 
     * @param cal
     * @return Returns time period string for saving the log
     */
    private static String getTimeBlock(Calendar cal){
        int hours = cal.get(Calendar.HOUR);
        for (int i = 0; i<Consts.LISTING_TIMES.length; i++){
                if (hours > Consts.LISTING_TIMES[i]-Consts.TIME_BUFFER && hours < Consts.LISTING_TIMES[i]+Consts.TIME_BUFFER){
                        System.out.println("here");
                        return Consts.LISTING_STRINGS[i];
                }
        }
        DateFormat df = new SimpleDateFormat("HH.mm");
        return df.format(cal.getTime());
    }
//	
    public static void main(String[] args) throws InterruptedException{
        //grab ordering parameters from configuration file, if it exists
        String configFile = "/Users/ParkerTew/Dropbox (MIT)/Code/Fido_Performance/fidoconfig/Cirrix 1 Order Entry Controls.xlsx";
        // String configFile = "/home/master/fidoconfig/Cirrix 1 Order Entry Controls.xlsx";

        String account = "C1_";
        Config params = new Config (configFile, account);
        params.readFile();

        /////////////////////////////////////////////////////////////////////
        /////////				 CONTROL PARAMETERS					/////////
        /////////////////////////////////////////////////////////////////////
        double purchaseTarget = params.orderMax;
        double availableCash = purchaseTarget;
        boolean watchForNew = false;
//		if (params.watch.equals("YES")){ //"Do you want this ordering session to wait for the new listings?"
//			watchForNew = true;
//		}else if (params.watch.equals("NO")){
//			watchForNew = false;
//		}
        boolean isWholeValid = true, isFractionalValid = false;
        boolean is36Valid, is60Valid;
        if (params.termAllowance.equals("YES")){ //"Do you want to restrict your oders to 36M loans?"
                is36Valid=true;
                is60Valid=false;
        } else {                     
                is36Valid=true;
                is60Valid=true;
        }

        /////////////////////////////////////////////////////////////////////
        /////////						SETUP						/////////
        /////////////////////////////////////////////////////////////////////
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.Jdk14Logger");
        String pathToLogs = Consts.PATH_TO_LOGS+LoanLogger.class.getName()+"/";
        Calendar cal = Calendar.getInstance();
        System.setProperty("logfile.name", pathToLogs+cal.get(Calendar.YEAR)+"/"+Consts.MONTHS[cal.get(Calendar.MONTH)]+"/"+cal.get(Calendar.DATE)+"-"+getTimeBlock(cal)+".txt");
        log = Logger.getLogger(LoanLogger.class.getName());
        APIConnection api = new APIConnection(Consts.SCHEME, Consts.HOST, Consts.LISTING_PATH, Consts.C1_REAL_TOKEN, Consts.C1_REAL_AID, log);
        File file;
        FileWriter fw;
        BufferedWriter bw;
        file = new File("/Users/ParkerTew/Dropbox (MIT)/Code/logs/csv-timing.csv");
        try {
            // if file doesnt exists, then create it
            if (!file.exists()) {
                    file.createNewFile();
            }

            fw = new FileWriter(file.getAbsoluteFile());
            bw = new BufferedWriter(fw);
            bw.write("Total Time,Response Length\n");
            for (int i=0; i < 50; i++) {
                bw.write(api.getLoanRetrievalTime(Consts.CSV, true));
                Thread.sleep(1000);
            }
            bw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
//        file = new File("/Users/ParkerTew/Dropbox (MIT)/Code/logs/json-timing.csv");
//        try {
//            // if file doesnt exists, then create it
//            if (!file.exists()) {
//                    file.createNewFile();
//            }
//
//            fw = new FileWriter(file.getAbsoluteFile());
//            bw = new BufferedWriter(fw);
//            bw.write("Total Time,Response Length\n");
//            for (int i=0; i < 3; i++) {
//                bw.write(api.getLoanRetrievalTime(Consts.JSON, true));
//                Thread.sleep(1000);
//            }
//            bw.close();
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//        
//        file = new File("/Users/ParkerTew/Dropbox (MIT)/Code/logs/xml-timing.csv");
//        try {
//            // if file doesnt exists, then create it
//            if (!file.exists()) {
//                    file.createNewFile();
//            }
//
//            fw = new FileWriter(file.getAbsoluteFile());
//            bw = new BufferedWriter(fw);
//            bw.write("Total Time,Response Length\n");
//            for (int i=0; i < 50; i++) {
//                bw.write(api.getLoanRetrievalTime(Consts.XML, true));
//                Thread.sleep(1000);
//            }
//            bw.close();
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        } 
    }
}
