package arcadia;

import java.io.*;
import java.util.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import static org.apache.commons.lang3.StringUtils.INDEX_NOT_FOUND;


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
                    .setParameter("showAll",String.valueOf(false))
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
    }

    /**
     * 
     * @param contentType - String value to specify file format of return string
     * @param showAll - Boolean value whether to grab all loans or only recently posted loans
     * 
     * @return 	String list of all listed loans if showAll is true and recent loans if showAll 
     * 			is false, in specified format
     */
    public String getLoanListAsString(boolean showAll) {
        HttpGet getLoans;
        if (showAll) {
            getLoans = new HttpGet(this.showAllListingURI);
        } else {
            getLoans = new HttpGet(this.listingURI);
        }        
        getLoans.setHeader("Accept", Consts.CSV);
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
    
    public String getLoanListAsString(String contentType, boolean showAll) {
        HttpGet getLoans;
        if (showAll) {
            getLoans = new HttpGet(this.showAllListingURI);
        } else {
            getLoans = new HttpGet(this.listingURI);
        }        
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
    
    public static String remove(final String str, final char remove) {
        if (str.indexOf(remove) == INDEX_NOT_FOUND) {
            return str;
        }

        final char[] chars = str.toCharArray();
        int pos = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != remove) {
                chars[pos++] = chars[i];
            }
        }
        return new String(chars, 0, pos);
    }
    
    public static String replaceTextualCommas(String str) {
        boolean startText = false;
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '"' ) {
                startText = !startText;
            } else if (chars[i] == ',' && startText) {
                chars[i] = '/';
            }
        }
        return new String(chars, 0, chars.length);
    }
    
    public static String replaceNewlines(String str) {
        if (str.indexOf('\n') == INDEX_NOT_FOUND) {
            return str;
        }

        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '\n') {
                chars[i] = ',';
            }
        }
        return new String(chars, 0, chars.length);
    }
    
    public String[] parseLoanCSVStringCustom(String loans) {
        return remove(replaceNewlines(replaceTextualCommas(loans)), '"').split(",");
    }
    
    private LoanList retrieveLoanList(boolean showAll) {
        String loans = getLoanListAsString(showAll);
        String[] loanData = parseLoanCSVStringCustom(loans);
        LoanList loanList = new LoanList(new LinkedList<Loan>());
        final int LOAN_DATA_LENGTH = 104;       
        for (int i=LOAN_DATA_LENGTH; i < loanData.length - LOAN_DATA_LENGTH; i+=LOAN_DATA_LENGTH) {
            loanList.addLoan(new Loan(Arrays.copyOfRange(loanData, i, i + LOAN_DATA_LENGTH)));
        }
 
        return loanList;
    }
    
    public LoanList retrieveNewLoanList() {
        return retrieveLoanList(false);
    }
    
    public LoanList retrieveAllLoanList() {
        return retrieveLoanList(true);
    }
    
    static String readFile(String path, Charset encoding) 
      throws IOException 
    {
      byte[] encoded = Files.readAllBytes(Paths.get(path));
      return new String(encoded, encoding);
    }
    
    public LoanList retrieveLoanListFromCSV(String contentType, String filepath) {
        String[] loanData;
        LoanList loanList = new LoanList(new LinkedList<Loan>());

        try {
            loanData = readFile(filepath, StandardCharsets.UTF_8).replaceAll("\"","").replaceAll("\r|\n", ",").split(",");
            final int LOAN_DATA_LENGTH = 104; 
            for (int i=LOAN_DATA_LENGTH; i < loanData.length - LOAN_DATA_LENGTH; i+=LOAN_DATA_LENGTH) {
                loanList.addLoan(new Loan(Arrays.copyOfRange(loanData, i, i + LOAN_DATA_LENGTH)));
            }
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(APIConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return loanList;
    }

    public LoanList oldRetrieveLoanList(String contentType, boolean showAll){
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
        StringBuilder loanOrders= new StringBuilder();
        for (Loan l : orderList.getLoans()) {
            if (l.getReqAmount() > 0.0){
                loanOrders.append("{\"loanId\":"+l.getLoanID()+",\"requestedAmount\":"+l.getReqAmount()+"},");
            }
        }
        
        String order = loanOrders.toString();
        // delete the extra comma
        if (!order.equals("")){
            order = order.substring(0, order.length()-1);
        }
        String postamble = "]}";
        return preamble+order+postamble;
    }
    
    public String placeOrderCSV(String orderRequest){
        HttpPost placeOrder = new HttpPost(orderURI);
        placeOrder.setHeader("Accept", Consts.CSV);
        placeOrder.setHeader("Authorization",authToken);
        placeOrder.setHeader("Connection", "keep-alive");     
        StringEntity payload = new StringEntity(orderRequest, ContentType.TEXT_PLAIN);
        placeOrder.setEntity(payload);

        ResponseHandler<String> rh = new BasicResponseHandler();
        String orderConfirmationAsString = "";
        log.info("Order would be placed now.");
//        try {
//            orderConfirmationAsString = client.execute(placeOrder,rh);
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return orderConfirmationAsString;
    }

    public String placeOrder(String orderRequest, String  contentType){
        HttpPost placeOrder = new HttpPost(orderURI);
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
