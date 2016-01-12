package arcadia;

import java.util.Calendar;
import org.apache.log4j.Logger;

public class LoanLogger extends Cirrix {

    public static void main(String[] args) throws InterruptedException{
        //grab ordering parameters from configuration file, if it exists
        String configFile = "/Users/ParkerTew/Dropbox (MIT)/Code/Fido_Performance/fidoconfig/Cirrix 1 Order Entry Controls.xlsx";
        // String configFile = "/home/master/fidoconfig/Cirrix 1 Order Entry Controls.xlsx";

        String account = "C1_";
        Config params = new Config (configFile, account);
        params.readFile();

        /////////////////////////////////////////////////////////////////////
        /////////                        SETUP			/////////
        /////////////////////////////////////////////////////////////////////
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.Jdk14Logger");
        String pathToLogs = Consts.PATH_TO_LOGS+LoanLogger.class.getName()+"/";
        Calendar cal = Calendar.getInstance();
        System.setProperty("logfile.name", pathToLogs+cal.get(Calendar.YEAR)+"/"+Consts.MONTHS[cal.get(Calendar.MONTH)]+"/"+cal.get(Calendar.DATE)+"-"+getTimeBlock(cal)+".txt");
        log = Logger.getLogger(LoanLogger.class.getName());
        APIConnection api = new APIConnection(Consts.SCHEME, Consts.HOST, Consts.LISTING_PATH, Consts.C1_REAL_TOKEN, Consts.C1_REAL_AID, log);
        String filepath = "/Users/ParkerTew/Dropbox (MIT)/Code/logs/realisticLoans/cirrix1-real.csv";
        LoanList fast = api.retrieveNewLoanList();
//        LoanList slow = api.retrieveLoanList(contentType, true);
//        slow.removeLoanList(fast);
//        assert(slow.getLoanCount()==0);
//        System.out.println(api.getLoanListAsString(contentType, true));
//        File file;
//        FileWriter fw;
//        BufferedWriter bw;
//        file = new File("/Users/ParkerTew/Dropbox (MIT)/Code/logs/show-all-false.csv");
//        try {
//            // if file doesnt exists, then create it
//            if (!file.exists()) {
//                    file.createNewFile();
//            }
//
//            fw = new FileWriter(file.getAbsoluteFile());
//            bw = new BufferedWriter(fw);
//            bw.write("Total Time,Response Length\n");
//            
//            bw.write(api.getLoanListAsString(Consts.CSV, true));
//            
//            bw.close();
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
        
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
