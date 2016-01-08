package arcadia;

import java.io.*;
import java.util.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;

import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.*;
import org.apache.log4j.Logger;

import net.minidev.json.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.http.Header;


/**
 * Class that exclusively interacts with the LC API
 */
public class APIConnection {	
    public String scheme, host, listingPath, orderPath;
    private String authToken;
    private int aid;
    private CloseableHttpClient client;
    private Logger log;
    private volatile boolean watching;
    private URI showAllListingURI;
    private URI listingURI;
    private URI orderURI;
    private HttpPost httpOrder;

    /**
     * constructor for an APIConnection
     * @param scheme String scheme of url (i.e. https://)
     * @param host String hostname of url
     * @param path String path of url
     * @param authToken String user authorization token
     * 
     */
//        public APIConnection(String scheme, String host, String listingPath, String orderPath, String authToken, int aid, Logger log){
    public APIConnection(String scheme, String host, String listingPath, String authToken, int aid, Logger log){
        this.scheme = scheme;
        this.host = host;
        this.listingPath = listingPath;
    //        this.orderPath = orderPath;  //probably can remove the orderPath from the list of variables passed between this and the main classes, and between the consts and the main classes
        this.orderPath = "accounts/"+ aid +"/orders";
        this.authToken = authToken;
        this.client = HttpClientBuilder.create().build();
        this.log = log;
        this.watching = false;
        this.aid = aid;
        try {
            this.showAllListingURI = new URIBuilder()
                    .setScheme(scheme)
                    .setHost(host)
                    .setPath(listingPath)
                    .setParameter("showAll",String.valueOf(true))
                    .build();
        } catch (URISyntaxException e) {
            System.out.println("Failed to create URI for show all listing." + e);
        }
        
        try {
            this.listingURI = new URIBuilder()
                    .setScheme(scheme)
                    .setHost(host)
                    .setPath(listingPath)
                    .setParameter("showAll",String.valueOf(true))
                    .build();
        } catch (URISyntaxException e) {
            System.out.println("Failed to create URI for listing." + e);
        }
        
        try {
                this.orderURI = new URIBuilder()
                .setScheme(scheme)
                .setHost(host)
                .setPath(orderPath)
                .build();
        } catch (URISyntaxException e) {
            System.out.println("Failed to create URI for order." + e);
        }
        
        this.httpOrder  = new HttpPost(this.orderURI);
        this.httpOrder.setHeader("Accept", Consts.CSV);
        this.httpOrder.setHeader("Authorization",authToken);
        this.httpOrder.setHeader("Connection", "keep-alive");
    }

    /**
     * 
     * @param contentType - String value to specify file format of return string
     * @param showAll - Boolean value whether to grab all loans or only recently posted loans
     * 
     * @return 	String list of all listed loans if showAll is true and recent loans if showAll 
     * 			is false, in specified format
     */
    public String getLoanListAsString(String contentType, boolean showAll) {
        HttpGet getLoans = new HttpGet(this.listingURI);
        getLoans.setHeader("Accept", contentType);
        getLoans.setHeader("Authorization",authToken);
        getLoans.setHeader("Connection", "keep-alive");
        ResponseHandler<String> rh = new BasicResponseHandler();
        
        String loanListAsString = "";
        try {
            loanListAsString = client.execute(getLoans,rh);
        } catch (Exception e) {
            System.out.println("Failed to excute get loans from client." + e);
        }

        return loanListAsString;
    }
    
    public String getLoanRetrievalTime(String contentType, boolean showAll) {
        HttpGet getLoans = new HttpGet(this.listingURI);
        getLoans.setHeader("Accept", contentType);
        getLoans.setHeader("Authorization",authToken);
        getLoans.setHeader("Connection", "keep-alive");
        ResponseHandler<String> rh = new BasicResponseHandler();
        
        double time =0;
        String loanListAsString = "";
        try {
            long start = System.currentTimeMillis();
            loanListAsString = client.execute(getLoans,rh);
            long end = System.currentTimeMillis();
            time = (end - start)/1000.0;
        } catch (Exception e) {
            System.out.println("Failed to excute get loans from client." + e);
        }

        return Double.toString(time) + "," + loanListAsString.length() + "\n";
    }

    public LoanList retrieveLoanList(String contentType, boolean showAll){
        String listAsString = getLoanListAsString(contentType, showAll);
        LoanList loanList = new LoanList(new LinkedList<Loan>());
        if (!listAsString.equals("")) {
            switch (contentType) {
                case "text/plain":
                    try { 
                        CSVFormat csvStringFormat = CSVFormat.DEFAULT.withHeader().withQuote('"');
                        StringReader listAsReader = new StringReader(listAsString);
                        CSVParser csvStringParser = new CSVParser(listAsReader, csvStringFormat);
                        List csvRecords = csvStringParser.getRecords();
                        for (int i=1;i< csvRecords.size(); i++) {
                            loanList.addLoan(new Loan(contentType,(Object) csvRecords.get(i)));
                        }
                    } catch (Exception e) {
                        System.out.println("Couldn't parse CSV LoanList." + e);
                    }
                    break;
                case "application/json":
                    try {
                        JSONObject obj = (JSONObject) JSONValue.parse(listAsString);
//                            String asOfDate = (String) obj.get("asOfDate");
                        JSONArray loans = (JSONArray) obj.get("loans");
                        if (loans!=null){
                            for (int i =0; i < loans.size(); i++) {
                                loanList.addLoan(new Loan(contentType,(Object) loans.get(i)));
                            }
                        }
                    } catch (ClassCastException e) {
                        System.out.println("Couldn't parse JSON LoanList." + e);
                    }
                    break;
                case "application/xml":
                    break;
            }
        }

        return loanList;
    } 

    /**
     * signal that we are watching the loan listings for new loans on multiple threads
     */
    public void startWatching(){
        this.watching = true;
    }

    /**
     * signal that we have completed watching the loan listings for new loans, multiple threads ready to close
     */
    public void stopWatching(){
        this.watching = false;
    }

    /**
     * @return true if currently watching for new loans on multiple threads, false otherwise
     */
    public boolean isWatching(){
        return this.watching;
    }

    /**
     * Orders nonzero requested amounts for each loan in the given LoanList
     * @param orderList
     * @return returns the formatted payload String to submit an order for the given LoanList
     */
    public String formatOrderRequest(LoanList orderList){
        String preamble = "{\"aid\":"+this.aid+",\"orders\":[";
        String loanOrders="";
        for (Loan l : orderList.getLoans()){
            if (l.getReqAmount() > 0.0){
                loanOrders+="{\"loanId\":"+l.getLoanID()+",\"requestedAmount\":"+l.getReqAmount()+"},";
            }
        }
        
        //delete the extra comma
        if (!loanOrders.equals("")){
            loanOrders = loanOrders.substring(0, loanOrders.length()-1);
        }
        String postamble = "]}";
        return preamble+loanOrders+postamble;
    }

    public String benchmarkPlaceOrder(String orderRequest, String  contentType) {
        this.httpOrder.setEntity(new StringEntity(orderRequest, ContentType.TEXT_PLAIN));
        ResponseHandler<String> rh = new BasicResponseHandler();
        String orderConfirmationAsString = "";
        return orderConfirmationAsString;
    }

    public String placeOrder(String orderRequest, String  contentType){
        URI uri = null;
        try {
                uri = new URIBuilder()
                .setScheme(scheme)
                .setHost(host)
                .setPath(orderPath)
                .build();
        } catch (URISyntaxException e1) {
                e1.printStackTrace();
        }
        
//		System.out.println(uri);
        HttpPost placeOrder = new HttpPost(uri);
        placeOrder.setHeader("Accept", contentType);
        placeOrder.setHeader("Authorization",authToken);
        placeOrder.setHeader("Connection", "keep-alive");
        ContentType ct = null;
        switch (contentType){
            case Consts.JSON:
                ct = ContentType.APPLICATION_JSON;
                break;
            case Consts.XML:
                ct = ContentType.APPLICATION_XML;
                break;
            case Consts.CSV:
                ct = ContentType.TEXT_PLAIN;
                break;
        }
        StringEntity payload = new StringEntity(orderRequest, ct);
        placeOrder.setEntity(payload);

        ResponseHandler<String> rh = new BasicResponseHandler();
        String orderConfirmationAsString = "";
//        try {
//            orderConfirmationAsString = client.execute(placeOrder,rh);
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return orderConfirmationAsString;
    }

    public List<OrderConfirmation> parseOrderConfirmation(String orderConfirmationAsString, String contentType){
        List<OrderConfirmation> ocList = new ArrayList<OrderConfirmation>();
        switch (contentType) {
            case "application/json":
                try {
                    JSONObject obj = (JSONObject) JSONValue.parse(orderConfirmationAsString);
                    
                    long orderInstructID;
                    if (obj.get("orderInstructId")==null) {
                        orderInstructID = 99999;
                    } else {
                        orderInstructID = Long.valueOf((int) obj.get("orderInstructId"));
                    }
                    
                    JSONArray orderConfirmations = (JSONArray) obj.get("orderConfirmations");
                    log.info("Completed the order request. These are the confirmed loan results:");
                    
                    if (orderConfirmations!=null) {
                        for (int i =0; i < orderConfirmations.size(); i++) {
                            //change size() to length() when changing json jar from net.minidev to org.json
                            OrderConfirmation oc = new OrderConfirmation((JSONObject) orderConfirmations.get(i), orderInstructID);
                            ocList.add(oc);
                        }
                    }
                } catch (Exception e){
                     System.out.println("Error: " + e);
                }
                break;
            case "application/xml":
                break;
            case "text/plain":
                break;
        }
        
        return ocList;
    }
}
