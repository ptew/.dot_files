package arcadia;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.net.*;
import org.apache.log4j.Logger;

public class Cirrix2a extends Cirrix {
        
    public static void main(String[] args){
        //grab ordering parameters from configuration file, if it exists
        String configFile = "/home/master/fidoconfig/Cirrix 2a Order Entry Controls.xlsx";
        String account = "C2a_";
        Config params = new Config (configFile, account);
        params.readFile();

        /////////////////////////////////////////////////////////////////////
        /////////               CONTROL PARAMETERS                  /////////
        /////////////////////////////////////////////////////////////////////
        double purchaseTarget = params.orderMax;
        double availableCash = purchaseTarget;
        boolean watchForNew;
        if (params.watch.equals("YES")){ //"Do you want this ordering session to wait for the new listings?"
            watchForNew = true;
        }else if (params.watch.equals("NO")){
            watchForNew = false;
        }
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
        ////////			     SETUP			    /////////
        /////////////////////////////////////////////////////////////////////
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.Jdk14Logger");
        String pathToLogs = Consts.PATH_TO_LOGS+Cirrix2.class.getName()+"/";
        Calendar cal = Calendar.getInstance();
        System.setProperty("logfile.name", pathToLogs+cal.get(Calendar.YEAR)+"/"+Consts.MONTHS[cal.get(Calendar.MONTH)]+"/"+cal.get(Calendar.DATE)+"-"+getTimeBlock(cal)+".txt");
        log = Logger.getLogger(Cirrix2.class.getName());
        
        try {
            IP_Host = getIP();
            log.info("IP_Host: " + IP_Host);
        } catch (UnknownHostException e) {
            log.info("Error (UnknownHostException): " + e);
        }

        APIConnection api = new APIConnection(Consts.SCHEME, Consts.HOST, Consts.LISTING_PATH, Consts.C2a_REAL_TOKEN, Consts.C2a_REAL_AID, log);
        Filter filt = new Filter(Consts.C2_MIN_INCOME, Consts.C2_DTI_INQ_FICO, Consts.C2_FICO_INQUIRIES_CHECK, Consts.C2_STATE_LOAN_AMOUNT_CHECK, Consts.C2_FICO_LOAN_CHECK, params.TERM_SUBGRADE_CHECK, Consts.C2_INVALID_GRADES, Consts.C2_INVALID_PURPOSES, Consts.C2_EMP_LENGTH_MIN, Consts.C2_DTI_MAX, Consts.C2_DTI_TERM_CHECK, Consts.C2_FICO_LOW_THRESHHOLD, isWholeValid, isFractionalValid, is36Valid, is60Valid, params.creditModel);

        /////////////////////////////////////////////////////////////////////
        ////////  		  RETRIEVAL			    /////////
        /////////////////////////////////////////////////////////////////////
        log.info("Purchase Limit: " + purchaseTarget);
        log.info("Wait for new listings?: " + params.watch);
        log.info("Restrict order to 36M loans?: " + params.termAllowance);
        log.info("Credit models: " + params.creditModel);
        log.info("Excluded 36M subgrades: " + params.INVALID_SUBGRADES_36);
        log.info("Excluded 60M subgrades: " + params.INVALID_SUBGRADES_60);
        log.info("Invalid purposes: " + Consts.C2_INVALID_PURPOSES);
        log.info("Loans retrieved using contentType: " + contentType);
        LoanList currentOrder;
        LoanList alreadyOrdered = new LoanList(new LinkedList<Loan>());
        int investedNotes = 0;
        for (int t = 0; t < Consts.C2_WATCH_ITERATIONS; t++){
            currentOrder = api.retrieveNewLoanList();
            currentOrder.removeLoanList(alreadyOrdered);
            log.info("t= "+t+" ; "+currentOrder.getLoanCount()+" new loans retrieved");
            if (currentOrder.getLoanCount()>0){
                currentOrder.filter(filt);
                log.info("Filtered. "+currentOrder.getLoanCount()+" loans left");
                log.info("No scoring or sorting. We have developed the following prioritized list of "+currentOrder.getLoanCount()+" desirable loans.");
                if (currentOrder.getLoanCount()>0){
                    log.info(currentOrder);
                    currentOrder.setRequestAmounts(availableCash);
                    String orderRequest = api.formatOrderRequest(currentOrder);
                    if (orderRequest.contains("loanId")){
                        log.info("Loans selected for order: " + currentOrder);
                        log.info("Order Request: " + orderRequest);
                        String confirmation = api.placeOrder(orderRequest, Consts.JSON);
                        List<OrderConfirmation> ocList = api.parseOrderConfirmation(confirmation, Consts.JSON);
                        for (OrderConfirmation oc : ocList){
                            log.info("Order Confirmation: " + oc.toString());
                            if (oc.getInvestedAmount()>0){
                                availableCash-=oc.getInvestedAmount();
                                investedNotes++;
                                alreadyOrdered.addLoan(currentOrder.getLoanFromID(oc.getLoanID()));
                            }
                        }
                    }
                }
            }
        }
        
        log.info("Run completed. $ "+(purchaseTarget-availableCash)+" spent in "+investedNotes+" notes.");
    }
}
