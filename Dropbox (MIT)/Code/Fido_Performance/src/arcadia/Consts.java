package arcadia;

import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.net.*;

public class Consts {
    //API CONNECTION

    public static final String SCHEME = "https";
    public static final String VERSION = "v1/";
//    public static final String HOST = "api-sandbox.lendingclub.com/api/investor/"+VERSION;
    public static final String HOST = "api.lendingclub.com/api/investor/"+VERSION;
    public static final String LISTING_PATH = "loans/listing";
    public static final int SANDBOX_AID = 10698237;
//    public static final int C1_REAL_AID = SANDBOX_AID;
    public static final int C2_REAL_AID = SANDBOX_AID;
    public static final int CIDF_REAL_AID = SANDBOX_AID;
    public static final int CCYM1_REAL_AID = SANDBOX_AID;
    public static final int C2a_REAL_AID = SANDBOX_AID;
    
    public static final int C1_REAL_AID = 1647714;
//    public static final int C2_REAL_AID = 9366713;
//    public static final int CIDF_REAL_AID = 57853824;
//    public static final int CCYM1_REAL_AID = 000000000000000;
//    public static final int C2a_REAL_AID = 61698826;

//	public static final String C1_ORDER_PATH = "accounts/"+C1_REAL_AID+"/orders";
//	public static final String C2_ORDER_PATH = "accounts/"+C2_REAL_AID+"/orders";
//	public static final String CIDF_ORDER_PATH = "accounts/"+CIDF_REAL_AID+"/orders";
//	public static final String REAL_TOKEN = "";
    public static final String SANDBOX_TOKEN = "xO5Ae0FwNxXciC4XPzLH6kt3U0A=";
    public static final String C1_REAL_TOKEN = "xEvi0iPy5GgPhIc3JjnUtX/P+jU=";
    public static final String C2_REAL_TOKEN = SANDBOX_TOKEN;
    public static final String C2a_REAL_TOKEN = SANDBOX_TOKEN;
    public static final String CIDF_REAL_TOKEN = SANDBOX_TOKEN;
    public static final String CCYM1_REAL_TOKEN = SANDBOX_TOKEN;
//    public static final String C1_REAL_TOKEN = "";
//    public static final String C2_REAL_TOKEN = "";
//    public static final String C2a_REAL_TOKEN = "";
//    public static final String CIDF_REAL_TOKEN = "";
//    public static final String CCYM1_REAL_TOKEN = "";

    public static final String JSON = "application/json";
    public static final String XML = "application/xml";
    public static final String CSV = "text/plain";
    public static final double PARTIAL_FRACTION = 0.5;

    // LOGGING
    public static final String PATH_TO_LOGS = "../logs/";
    public static final String[] MONTHS = new String[]{"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    public static final int[] LISTING_TIMES = new int[]{14,18,22,2}; //hours UTC
    public static final String[] LISTING_STRINGS = new String[]{"9am(EST)","1pm(EST)","5pm(EST)","9pm(EST)"};
    public static final int TIME_BUFFER = 1; //hours

   /////////////////////////////////////////////////////////////////
   /////////  Set up parameters for generalized models, independent of LC account
   /////////   In the array list below, the first element is the MAX_Inquires allowed for a LEVEL_1 model (normal).  The second element is the MAX_Inquiries allowed for a LEVEL_2 model (more restrictive), and the third element is for a LEVEL_3 model (most restrictive).  The number preceeding the arraylist is the maximum DTI associated with that corresponding arraylist.  HF_... refers to FICO scores >=700.  LF_... refers to FICO scores <700.
   /////////////////////////////////////////////////////////////////
   public static final TreeMap<Double, List<Double>> HF_DTI_INQ, LF_DTI_INQ;
   public static final HashMap<String, Double> MVM_CHECK; // a filter to exclude loans not currently permitted under the Madden v. Midland litigation
        
        static{
            HF_DTI_INQ = new TreeMap<>();        
            HF_DTI_INQ.put(15., Arrays.asList(9999., 4., 1.));        
            HF_DTI_INQ.put(20., Arrays.asList(3., 2., 1.));        
            HF_DTI_INQ.put(25., Arrays.asList(3., 2., -9999.));        
            HF_DTI_INQ.put(30., Arrays.asList(0., 0., -9999.));        
            HF_DTI_INQ.put(9999., Arrays.asList(0., 0., -9999.));   
            
            LF_DTI_INQ = new TreeMap<>();        
            LF_DTI_INQ.put(15., Arrays.asList(9999., 2., 1.));        
            LF_DTI_INQ.put(20., Arrays.asList(3., 1., 0.));        
            LF_DTI_INQ.put(25., Arrays.asList(-9999., -9999., -9999.));        
            LF_DTI_INQ.put(30., Arrays.asList(-9999., -9999., -9999.));        
            LF_DTI_INQ.put(9999., Arrays.asList(-9999., -9999., -9999.));
        
            MVM_CHECK = new HashMap<>();
            MVM_CHECK.put("NY", 16.);
            MVM_CHECK.put("CT", 12.);
            MVM_CHECK.put("VT", 12.);
            
        }

	
	///////////////////////////////////////////////////
	//////		   	CIRRIX IDF
        //////  this buys whole loans, but under the SVB credit facility.  Need to exclude disallowed states.  Although
        /////   Cirrix II has a license in TX, Cirrix IDF does not.
	///////////////////////////////////////////////////
	public static final int CIDF_WATCH_ITERATIONS = 125;
	public static final int CIDF_EMP_LENGTH_MIN = 24;
        public static final double CIDF_MIN_INCOME = 40000;
        public static final double CIDF_DTI_MAX = 25;
        public static final double CIDF_DTI_MAX_36 = 25;
        public static final double CIDF_DTI_MAX_60 = 20;
	public static final int CIDF_FICO_LOW_THRESHHOLD = 680;
	public static final double CIDF_MIN_SCORE_CUTOFF = -4.0;
	public static final double CIDF_WILL_COMPETE_BONUS = 0.0;
	public static final Map<Integer, Double> CIDF_TERM_BONUSES;
	public static final TreeMap<Integer, Double> CIDF_INQUIRIES_BONUSES, CIDF_FICO_BONUSES, CIDF_DTI_TERM_CHECK;
	public static final Map<String, Double> CIDF_USAGE_BONUSES, CIDF_STATE_BONUSES, CIDF_GRADE_BONUSES, CIDF_STATE_LOAN_AMOUNT_CHECK;
	public static final TreeMap<Integer, Integer> CIDF_FICO_INQUIRIES_CHECK;
	public static final Map<Integer, List<String>> CIDF_TERM_SUBGRADE_CHECK;
	public static final List<String> CIDF_INVALID_SUBGRADES_36, CIDF_INVALID_SUBGRADES_60, CIDF_INVALID_PURPOSES, CIDF_INVALID_GRADES;
	public static final Map<Integer, Map<String, Integer>> CIDF_FICO_LOAN_CHECK;
	public static final Map<String, Integer> CIDF_FICO_MINS_FOR_36, CIDF_FICO_MINS_FOR_60;
 
        public static final ArrayList<Double> CIDF_DTI_INQ_FICO_1, CIDF_DTI_INQ_FICO_2;
        public static final TreeMap<Integer, ArrayList<Double>> CIDF_DTI_INQ_FICO;
        /**
         * DTI/MAX_INQ_criteria for different FICO ranges
         */
        static {
           CIDF_DTI_INQ_FICO_1 = new ArrayList<>();
           CIDF_DTI_INQ_FICO_2 = new ArrayList<>();
           CIDF_DTI_INQ_FICO = new TreeMap<>();
           CIDF_DTI_INQ_FICO_1.addAll(Arrays.asList(20., 2.));
           CIDF_DTI_INQ_FICO.put(600, CIDF_DTI_INQ_FICO_1);
           CIDF_DTI_INQ_FICO_2.addAll(Arrays.asList(30., 4.));
           CIDF_DTI_INQ_FICO.put(700, CIDF_DTI_INQ_FICO_2);
         
        }
           
	
	/**
	 * inquiriesBonus, used in Scorer
	 * maps num inquiries in last six months to bonus
	 * everything over the last entry is mapped to the last entry
	 */
	static {
		CIDF_INQUIRIES_BONUSES= new TreeMap<Integer, Double>();
		CIDF_INQUIRIES_BONUSES.put(0,3.0);
		CIDF_INQUIRIES_BONUSES.put(1,0.75);
		CIDF_INQUIRIES_BONUSES.put(2,0.0);
		CIDF_INQUIRIES_BONUSES.put(3,-0.5);
		CIDF_INQUIRIES_BONUSES.put(4,-1.0);
		CIDF_INQUIRIES_BONUSES.put(5,-3.0);
		CIDF_INQUIRIES_BONUSES.put(6,-6.0);
		CIDF_INQUIRIES_BONUSES.put(7,-10.0);
	}
	
	/**
	 * ficoBonuses, used in Scorer
	 * maps ficoLow in each range to bonuses
	 * everything over the last entry is mapped to the last entry
	 */
	static {
		CIDF_FICO_BONUSES= new TreeMap<Integer, Double>();
		CIDF_FICO_BONUSES.put(660,-1.0);
		CIDF_FICO_BONUSES.put(670,-0.5);
		CIDF_FICO_BONUSES.put(680,0.0);
		CIDF_FICO_BONUSES.put(700,1.0);
		CIDF_FICO_BONUSES.put(710,2.0);
		CIDF_FICO_BONUSES.put(735,3.0);
	}
	
	/**
	 * usageBonuses, used in Scorer
	 * maps usage String to bonuses
	 */
	static {
		CIDF_USAGE_BONUSES = new HashMap<String, Double>();
		CIDF_USAGE_BONUSES.put("DEBT_CONSOLIDATION", 1.5);
		CIDF_USAGE_BONUSES.put("CREDIT_CARD", 2.0);
		CIDF_USAGE_BONUSES.put("HOUSE", 1.0);
		CIDF_USAGE_BONUSES.put("OTHER", -4.0);
		CIDF_USAGE_BONUSES.put("HOME_IMPROVEMENT", -4.0);
		CIDF_USAGE_BONUSES.put("MAJOR_PURPOSE", 0.0);
		CIDF_USAGE_BONUSES.put("WEDDING", 1.0);
		CIDF_USAGE_BONUSES.put("VACATION", 0.0);
		CIDF_USAGE_BONUSES.put("CAR", 3.0);
		CIDF_USAGE_BONUSES.put("MEDICAL", -7.0);
		CIDF_USAGE_BONUSES.put("RENEWABLE_ENERGY", 0.0);
		CIDF_USAGE_BONUSES.put("MOVING", -10.0);
		CIDF_USAGE_BONUSES.put("SMALL_BUSINESS",0.0);
	}
	
	/**
	 * stateBonuses, used in Scorer
	 * maps state String to bonus
	 * any non-key state is mapped to 0
	 */
	static {
		CIDF_STATE_BONUSES = new HashMap<String, Double>();
		CIDF_STATE_BONUSES.put("CA", -1.0);
		CIDF_STATE_BONUSES.put("CT", 0.25);
		CIDF_STATE_BONUSES.put("MA", 0.25);
		CIDF_STATE_BONUSES.put("NV", -0.5);
	}
	
	/**
	 * gradeBonuses, used in Scorer
	 * maps grade/subgrade Strings to bonuses
	 */
	static{
		CIDF_GRADE_BONUSES = new HashMap<String, Double>();
		CIDF_GRADE_BONUSES.put("A1",2.0);
		CIDF_GRADE_BONUSES.put("A2",2.0);
		CIDF_GRADE_BONUSES.put("A3",2.0);
		CIDF_GRADE_BONUSES.put("A4",2.0);
		CIDF_GRADE_BONUSES.put("A5",2.0);
		CIDF_GRADE_BONUSES.put("B1",2.0);
		CIDF_GRADE_BONUSES.put("B2",2.0);
		CIDF_GRADE_BONUSES.put("B3",2.0);
		CIDF_GRADE_BONUSES.put("B4",2.0);
		CIDF_GRADE_BONUSES.put("B5",2.0);
		CIDF_GRADE_BONUSES.put("C1",2.0);
		CIDF_GRADE_BONUSES.put("C2",2.0);
		CIDF_GRADE_BONUSES.put("C3",2.0);
		CIDF_GRADE_BONUSES.put("C4",2.0);
		CIDF_GRADE_BONUSES.put("C5",2.0);
		CIDF_GRADE_BONUSES.put("D1",2.0);
		CIDF_GRADE_BONUSES.put("D2",2.0);
		CIDF_GRADE_BONUSES.put("D3",2.0);
		CIDF_GRADE_BONUSES.put("D4",2.0);
		CIDF_GRADE_BONUSES.put("D5",2.0);
		CIDF_GRADE_BONUSES.put("E1",2.0);
		CIDF_GRADE_BONUSES.put("E2",2.0);
		CIDF_GRADE_BONUSES.put("E3",2.0);
		CIDF_GRADE_BONUSES.put("E4",2.0);
		CIDF_GRADE_BONUSES.put("E5",2.0);
		CIDF_GRADE_BONUSES.put("F1",2.0);
		CIDF_GRADE_BONUSES.put("F2",2.0);
		CIDF_GRADE_BONUSES.put("F3",2.0);
		CIDF_GRADE_BONUSES.put("F4",2.0);
		CIDF_GRADE_BONUSES.put("F5",2.0);
		CIDF_GRADE_BONUSES.put("G1",2.0);
		CIDF_GRADE_BONUSES.put("G2",2.0);
		CIDF_GRADE_BONUSES.put("G3",2.0);
		CIDF_GRADE_BONUSES.put("G4",2.0);
		CIDF_GRADE_BONUSES.put("G5",2.0);
	}
	
	/**
	 * termBonuses, used in Scorer
	 * maps term length in months to bonuses
	 */
	static{
		CIDF_TERM_BONUSES = new HashMap<Integer, Double>();
		CIDF_TERM_BONUSES.put(36,1.5);
		CIDF_TERM_BONUSES.put(60,0.0);
	}
	
	/**
	 * ficoInquiriesCheck, used in Filter
	 * maps ficoLow range to maximum number of inquries in last 6 months
	 * everything over the last entry is mapped to the last entry
	 * 
	 */
	static {
		CIDF_FICO_INQUIRIES_CHECK= new TreeMap<Integer, Integer>();
		CIDF_FICO_INQUIRIES_CHECK.put(660,2);
		CIDF_FICO_INQUIRIES_CHECK.put(670,2);
		CIDF_FICO_INQUIRIES_CHECK.put(690,2);
		CIDF_FICO_INQUIRIES_CHECK.put(700,4);
		CIDF_FICO_INQUIRIES_CHECK.put(735,Integer.MAX_VALUE);
	}

	/**
	 * stateLoanAmountCheck, used in Filter
	 * maps state String to the minimum loan amount
	 * if state is not listed, it has no minimum loan amount
      * for minimum=99999.9, state is disallowed per local laws
	 */
	static{
		CIDF_STATE_LOAN_AMOUNT_CHECK = new HashMap<String, Double>();
		CIDF_STATE_LOAN_AMOUNT_CHECK.put("NJ", 99999.9);
		CIDF_STATE_LOAN_AMOUNT_CHECK.put("PA", 99999.9);
		CIDF_STATE_LOAN_AMOUNT_CHECK.put("OK", 99999.9);
		CIDF_STATE_LOAN_AMOUNT_CHECK.put("NH", 99999.9);
		CIDF_STATE_LOAN_AMOUNT_CHECK.put("WV", 99999.9);
		CIDF_STATE_LOAN_AMOUNT_CHECK.put("CO", 99999.9);
		CIDF_STATE_LOAN_AMOUNT_CHECK.put("KS", 99999.9);
		CIDF_STATE_LOAN_AMOUNT_CHECK.put("SC", 99999.9);
		CIDF_STATE_LOAN_AMOUNT_CHECK.put("TX", 99999.9);
		CIDF_STATE_LOAN_AMOUNT_CHECK.put("WY", 99999.9);
		CIDF_STATE_LOAN_AMOUNT_CHECK.put("MA", 6000.0);
		CIDF_STATE_LOAN_AMOUNT_CHECK.put("OH", 5000.0);
                // temporarily halt CA loan purchases to stay in compliance
		CIDF_STATE_LOAN_AMOUNT_CHECK.put("CA", 99999.9);
	}
	
	/**
	 * invalidSubgradesFor36
	 * list of invalid subgrades for term length 36
	 */
	static {
		CIDF_INVALID_SUBGRADES_36 = new ArrayList<String>();
		CIDF_INVALID_SUBGRADES_36.add("A1");
		CIDF_INVALID_SUBGRADES_36.add("A2");
		CIDF_INVALID_SUBGRADES_36.add("A3");
		CIDF_INVALID_SUBGRADES_36.add("A4");
		CIDF_INVALID_SUBGRADES_36.add("A5");
		CIDF_INVALID_SUBGRADES_36.add("B1");
		CIDF_INVALID_SUBGRADES_36.add("B2");
 //         CIDF_INVALID_SUBGRADES_36.add("B3");
		CIDF_INVALID_SUBGRADES_36.add("F1");
		CIDF_INVALID_SUBGRADES_36.add("F2");
		CIDF_INVALID_SUBGRADES_36.add("F3");
		CIDF_INVALID_SUBGRADES_36.add("F4");
		CIDF_INVALID_SUBGRADES_36.add("F5");
		CIDF_INVALID_SUBGRADES_36.add("G1");
		CIDF_INVALID_SUBGRADES_36.add("G2");
		CIDF_INVALID_SUBGRADES_36.add("G3");
		CIDF_INVALID_SUBGRADES_36.add("G4");
		CIDF_INVALID_SUBGRADES_36.add("G5");
          //"maintenance mode"
		
		CIDF_INVALID_SUBGRADES_36.add("B4");
/*		CIDF_INVALID_SUBGRADES_36.add("B5");
*/

          // end maintenance mode
	}
	
	/**
	 * invalidSubgradesFor60
	 * list of invalid subgrades for term length 60
	 */
	static {
		CIDF_INVALID_SUBGRADES_60 = new ArrayList<String>();
		CIDF_INVALID_SUBGRADES_60.add("A1");
		CIDF_INVALID_SUBGRADES_60.add("A2");
		CIDF_INVALID_SUBGRADES_60.add("A3");
		CIDF_INVALID_SUBGRADES_60.add("A4");
		CIDF_INVALID_SUBGRADES_60.add("A5");
		CIDF_INVALID_SUBGRADES_60.add("B1");
		CIDF_INVALID_SUBGRADES_60.add("B2");
		CIDF_INVALID_SUBGRADES_60.add("B3");
		CIDF_INVALID_SUBGRADES_60.add("B4");
		CIDF_INVALID_SUBGRADES_60.add("B5");
		CIDF_INVALID_SUBGRADES_60.add("C1");
		CIDF_INVALID_SUBGRADES_60.add("C2");
		CIDF_INVALID_SUBGRADES_60.add("C3");
		CIDF_INVALID_SUBGRADES_60.add("D1");
		CIDF_INVALID_SUBGRADES_60.add("G1");
		CIDF_INVALID_SUBGRADES_60.add("G2");
		CIDF_INVALID_SUBGRADES_60.add("G3");
		CIDF_INVALID_SUBGRADES_60.add("G4");
		CIDF_INVALID_SUBGRADES_60.add("G5");
          //"temporary mode" (JBG 2/20/15)
		CIDF_INVALID_SUBGRADES_60.add("D1");
		CIDF_INVALID_SUBGRADES_60.add("D2");
		CIDF_INVALID_SUBGRADES_60.add("D3");
/*		CIDF_INVALID_SUBGRADES_60.add("D4");
		CIDF_INVALID_SUBGRADES_60.add("D5");
		CIDF_INVALID_SUBGRADES_60.add("E1");
		CIDF_INVALID_SUBGRADES_60.add("E2");
		CIDF_INVALID_SUBGRADES_60.add("E3");
		CIDF_INVALID_SUBGRADES_60.add("E4");
		CIDF_INVALID_SUBGRADES_60.add("E5");
*/         
// removed maintenance mode on 2/26/2015
	}
     /**
	 * invalidgrades
	 * list of invalid grades
	 */
      static {
           CIDF_INVALID_GRADES = new ArrayList<String>();
/*           CIDF_INVALID_GRADES.add("A");
           CIDF_INVALID_GRADES.add("G");
           CIDF_INVALID_GRADES.add("B");
           CIDF_INVALID_GRADES.add("E");
           CIDF_INVALID_GRADES.add("F");
*/           
           

      }	
	/**
	 * invalid purposes for C2 loans, used in Filter
	 * lists all valid purposes for loans
	 */
	static{
		CIDF_INVALID_PURPOSES = new ArrayList<String>();
/*		CIDF_INVALID_PURPOSES.add("MEDICAL");
		CIDF_INVALID_PURPOSES.add("RENEWABLE_ENERGY");
		CIDF_INVALID_PURPOSES.add("VACATION");
		CIDF_INVALID_PURPOSES.add("MOVING");
		CIDF_INVALID_PURPOSES.add("CAR");
*/
		CIDF_INVALID_PURPOSES.add("SMALL_BUSINESS");
          
	}
	
	/**
	 * termSubgradeCheck, used in Filter
	 * maps term length of Loan to all invalid subgrades for this term length
	 */
	static {
		CIDF_TERM_SUBGRADE_CHECK = new HashMap<Integer, List<String>>();
		CIDF_TERM_SUBGRADE_CHECK.put(36, CIDF_INVALID_SUBGRADES_36);
		CIDF_TERM_SUBGRADE_CHECK.put(60, CIDF_INVALID_SUBGRADES_60);
		
	}

                /**
         * DTITermCheck, used in Filter
         * maps maximum DTI of Loan for each term
         */
        static {
            CIDF_DTI_TERM_CHECK = new TreeMap<Integer, Double>();
            CIDF_DTI_TERM_CHECK.put(36, CIDF_DTI_MAX_36);        
            CIDF_DTI_TERM_CHECK.put(60, CIDF_DTI_MAX_60);
        }
	/**
	 * fico minimum values for 36 term, used in Filter
	 * maps grade to its minimum fico value
	 */
	static {
		CIDF_FICO_MINS_FOR_36 = new HashMap<String, Integer>();
		CIDF_FICO_MINS_FOR_36.put("A",880);
		CIDF_FICO_MINS_FOR_36.put("B",700); // while in rampup mode, want FICO > 700 7/29/2015;  afterwards, we will revert to 0
		CIDF_FICO_MINS_FOR_36.put("C",700);
		CIDF_FICO_MINS_FOR_36.put("D",700);
		CIDF_FICO_MINS_FOR_36.put("E",700);
		CIDF_FICO_MINS_FOR_36.put("F",700);
		CIDF_FICO_MINS_FOR_36.put("G",880);
	}
	
	/**
	 * fico minimum values for 60 term, used in Filter
	 * maps grade to its minimum fico value
	 */
	static {
		CIDF_FICO_MINS_FOR_60 = new HashMap<String, Integer>();
		CIDF_FICO_MINS_FOR_60.put("A",880);
		CIDF_FICO_MINS_FOR_60.put("B",700); // while in rampup mode, want FICO > 700 7/29/2015;  afterwards, we will revert to 0
		CIDF_FICO_MINS_FOR_60.put("C",700);
		CIDF_FICO_MINS_FOR_60.put("D",700);
		CIDF_FICO_MINS_FOR_60.put("E",700);
		CIDF_FICO_MINS_FOR_60.put("F",700);
		CIDF_FICO_MINS_FOR_60.put("G",880);
	}
	
	/**
	 * ficoLoanCheck, used in Filter
	 * maps term length to subgrade-fico limit mapping
	 */
	static{
		CIDF_FICO_LOAN_CHECK = new HashMap<Integer, Map<String, Integer>>();
		CIDF_FICO_LOAN_CHECK.put(36, CIDF_FICO_MINS_FOR_36);
		CIDF_FICO_LOAN_CHECK.put(60, CIDF_FICO_MINS_FOR_60);
	}
		
	///////////////////////////////////////////////////
	//////		   	CIRRIX 2 and 2a				///////////
	///////////////////////////////////////////////////
	public static final int C2_WATCH_ITERATIONS = 125;
	public static final int C2_EMP_LENGTH_MIN = 24;
        public static final double C2_MIN_INCOME = 40000;
     public static final double C2_DTI_MAX = 25;
     public static final double C2_DTI_MAX_36 = 25;
     public static final double C2_DTI_MAX_60 = 20;
	public static final int C2_FICO_LOW_THRESHHOLD = 735;
	public static final double C2_MIN_SCORE_CUTOFF = -4.0;
	public static final double C2_WILL_COMPETE_BONUS = 0.0;
	public static final Map<Integer, Double> C2_TERM_BONUSES;
	public static final TreeMap<Integer, Double> C2_INQUIRIES_BONUSES, C2_FICO_BONUSES, C2_DTI_TERM_CHECK;
	public static final Map<String, Double> C2_USAGE_BONUSES, C2_STATE_BONUSES, C2_GRADE_BONUSES, C2_STATE_LOAN_AMOUNT_CHECK;
	public static final TreeMap<Integer, Integer> C2_FICO_INQUIRIES_CHECK;
	public static final Map<Integer, List<String>> C2_TERM_SUBGRADE_CHECK;
	public static final List<String> C2_INVALID_SUBGRADES_36, C2_INVALID_SUBGRADES_60, C2_INVALID_PURPOSES, C2_INVALID_GRADES;
	public static final Map<Integer, Map<String, Integer>> C2_FICO_LOAN_CHECK;
	public static final Map<String, Integer> C2_FICO_MINS_FOR_36, C2_FICO_MINS_FOR_60;
        public static final ArrayList<Double> C2_DTI_INQ_FICO_1, C2_DTI_INQ_FICO_2;
        public static final TreeMap<Integer, ArrayList<Double>> C2_DTI_INQ_FICO;
        /**
         * DTI/MAX_INQ_criteria for different FICO ranges
         */
        static {
           C2_DTI_INQ_FICO_1 = new ArrayList<>();
           C2_DTI_INQ_FICO_2 = new ArrayList<>();
           C2_DTI_INQ_FICO = new TreeMap<>();
           C2_DTI_INQ_FICO_1.addAll(Arrays.asList(30., 4.));
           C2_DTI_INQ_FICO.put(700, C2_DTI_INQ_FICO_1);
           C2_DTI_INQ_FICO_2.addAll(Arrays.asList(20., 2.));
           C2_DTI_INQ_FICO.put(600, C2_DTI_INQ_FICO_2);          
        }	
	/**
	 * inquiriesBonus, used in Scorer
	 * maps num inquiries in last six months to bonus
	 * everything over the last entry is mapped to the last entry
	 */
	static {
		C2_INQUIRIES_BONUSES= new TreeMap<Integer, Double>();
		C2_INQUIRIES_BONUSES.put(0,3.0);
		C2_INQUIRIES_BONUSES.put(1,0.75);
		C2_INQUIRIES_BONUSES.put(2,0.0);
		C2_INQUIRIES_BONUSES.put(3,-0.5);
		C2_INQUIRIES_BONUSES.put(4,-1.0);
		C2_INQUIRIES_BONUSES.put(5,-3.0);
		C2_INQUIRIES_BONUSES.put(6,-6.0);
		C2_INQUIRIES_BONUSES.put(7,-10.0);
	}
	
	/**
	 * ficoBonuses, used in Scorer
	 * maps ficoLow in each range to bonuses
	 * everything over the last entry is mapped to the last entry
	 */
	static {
		C2_FICO_BONUSES= new TreeMap<Integer, Double>();
		C2_FICO_BONUSES.put(660,-1.0);
		C2_FICO_BONUSES.put(670,-0.5);
		C2_FICO_BONUSES.put(680,0.0);
		C2_FICO_BONUSES.put(700,1.0);
		C2_FICO_BONUSES.put(710,2.0);
		C2_FICO_BONUSES.put(735,3.0);
	}
	
	/**
	 * usageBonuses, used in Scorer
	 * maps usage String to bonuses
	 */
	static {
		C2_USAGE_BONUSES = new HashMap<String, Double>();
		C2_USAGE_BONUSES.put("DEBT_CONSOLIDATION", 1.5);
		C2_USAGE_BONUSES.put("CREDIT_CARD", 2.0);
		C2_USAGE_BONUSES.put("HOUSE", 1.0);
		C2_USAGE_BONUSES.put("OTHER", -4.0);
		C2_USAGE_BONUSES.put("HOME_IMPROVEMENT", -4.0);
		C2_USAGE_BONUSES.put("MAJOR_PURPOSE", 0.0);
		C2_USAGE_BONUSES.put("WEDDING", 1.0);
		C2_USAGE_BONUSES.put("VACATION", 0.0);
		C2_USAGE_BONUSES.put("CAR", 3.0);
		C2_USAGE_BONUSES.put("MEDICAL", -7.0);
		C2_USAGE_BONUSES.put("RENEWABLE_ENERGY", 0.0);
		C2_USAGE_BONUSES.put("MOVING", -10.0);
		C2_USAGE_BONUSES.put("SMALL_BUSINESS",0.0);
	}
	
	/**
	 * stateBonuses, used in Scorer
	 * maps state String to bonus
	 * any non-key state is mapped to 0
	 */
	static {
		C2_STATE_BONUSES = new HashMap<String, Double>();
		C2_STATE_BONUSES.put("CA", -1.0);
		C2_STATE_BONUSES.put("CT", 0.25);
		C2_STATE_BONUSES.put("MA", 0.25);
		C2_STATE_BONUSES.put("NV", -0.5);
	}
	
	/**
	 * gradeBonuses, used in Scorer
	 * maps grade/subgrade Strings to bonuses
	 */
	static{
		C2_GRADE_BONUSES = new HashMap<String, Double>();
		C2_GRADE_BONUSES.put("A1",2.0);
		C2_GRADE_BONUSES.put("A2",2.0);
		C2_GRADE_BONUSES.put("A3",2.0);
		C2_GRADE_BONUSES.put("A4",2.0);
		C2_GRADE_BONUSES.put("A5",2.0);
		C2_GRADE_BONUSES.put("B1",2.0);
		C2_GRADE_BONUSES.put("B2",2.0);
		C2_GRADE_BONUSES.put("B3",2.0);
		C2_GRADE_BONUSES.put("B4",2.0);
		C2_GRADE_BONUSES.put("B5",2.0);
		C2_GRADE_BONUSES.put("C1",2.0);
		C2_GRADE_BONUSES.put("C2",2.0);
		C2_GRADE_BONUSES.put("C3",2.0);
		C2_GRADE_BONUSES.put("C4",2.0);
		C2_GRADE_BONUSES.put("C5",2.0);
		C2_GRADE_BONUSES.put("D1",2.0);
		C2_GRADE_BONUSES.put("D2",2.0);
		C2_GRADE_BONUSES.put("D3",2.0);
		C2_GRADE_BONUSES.put("D4",2.0);
		C2_GRADE_BONUSES.put("D5",2.0);
		C2_GRADE_BONUSES.put("E1",2.0);
		C2_GRADE_BONUSES.put("E2",2.0);
		C2_GRADE_BONUSES.put("E3",2.0);
		C2_GRADE_BONUSES.put("E4",2.0);
		C2_GRADE_BONUSES.put("E5",2.0);
		C2_GRADE_BONUSES.put("F1",2.0);
		C2_GRADE_BONUSES.put("F2",2.0);
		C2_GRADE_BONUSES.put("F3",2.0);
		C2_GRADE_BONUSES.put("F4",2.0);
		C2_GRADE_BONUSES.put("F5",2.0);
		C2_GRADE_BONUSES.put("G1",2.0);
		C2_GRADE_BONUSES.put("G2",2.0);
		C2_GRADE_BONUSES.put("G3",2.0);
		C2_GRADE_BONUSES.put("G4",2.0);
		C2_GRADE_BONUSES.put("G5",2.0);
	}
	
	/**
	 * termBonuses, used in Scorer
	 * maps term length in months to bonuses
	 */
	static{
		C2_TERM_BONUSES = new HashMap<Integer, Double>();
		C2_TERM_BONUSES.put(36,1.5);
		C2_TERM_BONUSES.put(60,0.0);
	}
	
	/**
	 * ficoInquiriesCheck, used in Filter
	 * maps ficoLow range to maximum number of inquries in last 6 months
	 * everything over the last entry is mapped to the last entry
	 * 
	 */
	static {
		C2_FICO_INQUIRIES_CHECK= new TreeMap<Integer, Integer>();
		C2_FICO_INQUIRIES_CHECK.put(660,2);
		C2_FICO_INQUIRIES_CHECK.put(670,2);
		C2_FICO_INQUIRIES_CHECK.put(690,2);
		C2_FICO_INQUIRIES_CHECK.put(700,4);
		C2_FICO_INQUIRIES_CHECK.put(735,Integer.MAX_VALUE);
	}

	/**
	 * stateLoanAmountCheck, used in Filter
	 * maps state String to the minimum loan amount
	 * if state is not listed, it has no minimum loan amount
      * for minimum=99999.9, state is disallowed per local laws
	 */
	static{
		C2_STATE_LOAN_AMOUNT_CHECK = new HashMap<String, Double>();
		C2_STATE_LOAN_AMOUNT_CHECK.put("NJ", 99999.9);
		C2_STATE_LOAN_AMOUNT_CHECK.put("PA", 99999.9);
		C2_STATE_LOAN_AMOUNT_CHECK.put("OK", 99999.9);
		C2_STATE_LOAN_AMOUNT_CHECK.put("NH", 99999.9);
		C2_STATE_LOAN_AMOUNT_CHECK.put("WV", 99999.9);
		C2_STATE_LOAN_AMOUNT_CHECK.put("CO", 99999.9);
		C2_STATE_LOAN_AMOUNT_CHECK.put("KS", 99999.9);
		C2_STATE_LOAN_AMOUNT_CHECK.put("SC", 99999.9);
		C2_STATE_LOAN_AMOUNT_CHECK.put("WY", 99999.9);
		C2_STATE_LOAN_AMOUNT_CHECK.put("MA", 6000.0);
		C2_STATE_LOAN_AMOUNT_CHECK.put("OH", 5000.0);
	}
	
	/**
	 * invalidSubgradesFor36
	 * list of invalid subgrades for term length 36
	 */
	static {
		C2_INVALID_SUBGRADES_36 = new ArrayList<String>();
/*		C2_INVALID_SUBGRADES_36.add("A1");
		C2_INVALID_SUBGRADES_36.add("A2");
		C2_INVALID_SUBGRADES_36.add("A3");
		C2_INVALID_SUBGRADES_36.add("A4");
		C2_INVALID_SUBGRADES_36.add("A5");
*/		C2_INVALID_SUBGRADES_36.add("B1");
		C2_INVALID_SUBGRADES_36.add("B2");
//                C2_INVALID_SUBGRADES_36.add("B3");
		C2_INVALID_SUBGRADES_36.add("F1");
		C2_INVALID_SUBGRADES_36.add("F2");
		C2_INVALID_SUBGRADES_36.add("F3");
		C2_INVALID_SUBGRADES_36.add("F4");
		C2_INVALID_SUBGRADES_36.add("F5");
	}
	
	/**
	 * invalidSubgradesFor60
	 * list of invalid subgrades for term length 60
	 */
	static {
		C2_INVALID_SUBGRADES_60 = new ArrayList<String>();
/*		C2_INVALID_SUBGRADES_60.add("A1");
		C2_INVALID_SUBGRADES_60.add("A2");
		C2_INVALID_SUBGRADES_60.add("A3");
		C2_INVALID_SUBGRADES_60.add("A4");
		C2_INVALID_SUBGRADES_60.add("A5");
*/		C2_INVALID_SUBGRADES_60.add("B1");
		C2_INVALID_SUBGRADES_60.add("B2");
		C2_INVALID_SUBGRADES_60.add("B3");
		C2_INVALID_SUBGRADES_60.add("B4");
//		C2_INVALID_SUBGRADES_60.add("B5");
		C2_INVALID_SUBGRADES_60.add("C1");
		C2_INVALID_SUBGRADES_60.add("C2");
		C2_INVALID_SUBGRADES_60.add("C3");
//		C2_INVALID_SUBGRADES_60.add("C4");
//		C2_INVALID_SUBGRADES_60.add("C5");
		C2_INVALID_SUBGRADES_60.add("D1");
/*		C2_INVALID_SUBGRADES_60.add("G1");
		C2_INVALID_SUBGRADES_60.add("G2");
		C2_INVALID_SUBGRADES_60.add("G3");
		C2_INVALID_SUBGRADES_60.add("G4");
		C2_INVALID_SUBGRADES_60.add("G5");
*/          //"temporary mode" (JBG 2/20/15)
		C2_INVALID_SUBGRADES_60.add("D2");
/*		C2_INVALID_SUBGRADES_60.add("D3");
		C2_INVALID_SUBGRADES_60.add("D4");
		C2_INVALID_SUBGRADES_60.add("D5");
*/		C2_INVALID_SUBGRADES_60.add("E1");
		C2_INVALID_SUBGRADES_60.add("E2");
		C2_INVALID_SUBGRADES_60.add("E3");
		C2_INVALID_SUBGRADES_60.add("E4");
		C2_INVALID_SUBGRADES_60.add("E5");
         

// removed maintenance mode on 2/26/2015
	}
        
	/**
	 * termSubgradeCheck, used in Filter
	 * maps term length of Loan to all invalid subgrades for this term length
	 */
	static {
		C2_TERM_SUBGRADE_CHECK = new HashMap<Integer, List<String>>();
		C2_TERM_SUBGRADE_CHECK.put(36, C2_INVALID_SUBGRADES_36);
		C2_TERM_SUBGRADE_CHECK.put(60, C2_INVALID_SUBGRADES_60);
		
	}
        
        /**
         * DTITermCheck, used in Filter
         * maps maximum DTI of Loan for each term
         */
        static {
            C2_DTI_TERM_CHECK = new TreeMap<Integer, Double>();
            C2_DTI_TERM_CHECK.put(36, C2_DTI_MAX_36);        
            C2_DTI_TERM_CHECK.put(60, C2_DTI_MAX_60);
        }
        
     /**
	 * invalidgrades
	 * list of invalid grades
	 */
      static {
           C2_INVALID_GRADES = new ArrayList<String>();
           C2_INVALID_GRADES.add("A");
           C2_INVALID_GRADES.add("G");
           

      }	
	/**
	 * invalid purposes for C2 loans, used in Filter
	 * lists all valid purposes for loans
	 */
	static{
		C2_INVALID_PURPOSES = new ArrayList<String>();
/*		C2_INVALID_PURPOSES.add("MEDICAL");
		C2_INVALID_PURPOSES.add("RENEWABLE_ENERGY");
		C2_INVALID_PURPOSES.add("VACATION");
		C2_INVALID_PURPOSES.add("MOVING");
		C2_INVALID_PURPOSES.add("CAR");
*/
		C2_INVALID_PURPOSES.add("SMALL_BUSINESS");
          
	}
	


	/**
	 * fico minimum values for 36 term, used in Filter
	 * maps grade to its minimum fico value
	 */
	static {
		C2_FICO_MINS_FOR_36 = new HashMap<String, Integer>();
		C2_FICO_MINS_FOR_36.put("A",880);
		C2_FICO_MINS_FOR_36.put("B",0);
		C2_FICO_MINS_FOR_36.put("C",0);
		C2_FICO_MINS_FOR_36.put("D",0);
		C2_FICO_MINS_FOR_36.put("E",0);
		C2_FICO_MINS_FOR_36.put("F",0);
		C2_FICO_MINS_FOR_36.put("G",880);
	}
	
	/**
	 * fico minimum values for 60 term, used in Filter
	 * maps grade to its minimum fico value
	 */
	static {
		C2_FICO_MINS_FOR_60 = new HashMap<String, Integer>();
		C2_FICO_MINS_FOR_60.put("A",880);
		C2_FICO_MINS_FOR_60.put("B",0);
		C2_FICO_MINS_FOR_60.put("C",0);
		C2_FICO_MINS_FOR_60.put("D",0);
		C2_FICO_MINS_FOR_60.put("E",0);
		C2_FICO_MINS_FOR_60.put("F",0);
		C2_FICO_MINS_FOR_60.put("G",880);
	}
	
	/**
	 * ficoLoanCheck, used in Filter
	 * maps term length to subgrade-fico limit mapping
	 */
	static{
		C2_FICO_LOAN_CHECK = new HashMap<Integer, Map<String, Integer>>();
		C2_FICO_LOAN_CHECK.put(36, C2_FICO_MINS_FOR_36);
		C2_FICO_LOAN_CHECK.put(60, C2_FICO_MINS_FOR_60);
	}

     
	///////////////////////////////////////////////////
	//////		   	CIRRIX 1				///////////
	///////////////////////////////////////////////////
	public static final int C1_WATCH_ITERATIONS = 50;
	public static final double C1_MIN_SCORE_CUTOFF = -10.0;
	public static final double C1_PREORDER_THRESHHOLD = 8.0;
	public static final int C1_EMP_LENGTH_MIN = 2;
        public static final double C1_MIN_INCOME = 40000;
    public static final double C1_DTI_MAX = 100;     
     public static final double C1_DTI_MAX_36 = 20;     
     public static final double C1_DTI_MAX_60 = 25;     
	public static final int C1_FICO_LOW_THRESHHOLD = 680;
	public static final double C1_WILL_COMPETE_BONUS = 1.0;
	public static final List<String> C1_INVALID_SUBGRADES_36, C1_INVALID_SUBGRADES_60, C1_INVALID_PURPOSES, C1_INVALID_GRADES;	
	public static final Map<Integer, List<String>> C1_TERM_SUBGRADE_CHECK;
	public static final Map<String, Double> C1_STATE_LOAN_AMOUNT_CHECK;
	public static final Map<Integer, Map<String, Integer>> C1_FICO_LOAN_CHECK;
	public static final Map<String, Integer> C1_FICO_MINS_FOR_36, C1_FICO_MINS_FOR_60;
	public static final TreeMap<Integer, Integer> C1_FICO_INQUIRIES_CHECK;
	public static final Map<Integer, Double> C1_TERM_BONUSES;
	public static final TreeMap<Integer, Double> C1_INQUIRIES_BONUSES, C1_FICO_BONUSES, C1_DTI_TERM_CHECK;
	public static final Map<String, Double> C1_USAGE_BONUSES, C1_STATE_BONUSES, C1_GRADE_BONUSES;
        public static final ArrayList<Double> C1_DTI_INQ_FICO_1, C1_DTI_INQ_FICO_2;
        public static final TreeMap<Integer, ArrayList<Double>> C1_DTI_INQ_FICO;
        /**
         * DTI/MAX_INQ_criteria for different FICO ranges
         */
        static {
           C1_DTI_INQ_FICO_1 = new ArrayList<>();
           C1_DTI_INQ_FICO_2 = new ArrayList<>();
           C1_DTI_INQ_FICO = new TreeMap<>();
           C1_DTI_INQ_FICO_1.addAll(Arrays.asList(30., 4.));
           C1_DTI_INQ_FICO.put(700, C1_DTI_INQ_FICO_1);
           C1_DTI_INQ_FICO_2.addAll(Arrays.asList(20., 2.));
           C1_DTI_INQ_FICO.put(600, C1_DTI_INQ_FICO_2);          
        }	
	/**
	 * inquiriesBonus, used in Scorer
	 * maps num inquiries in last six months to bonus
	 * everything over the last entry is mapped to the last entry
	 */
	static {
		C1_INQUIRIES_BONUSES= new TreeMap<Integer, Double>();
		C1_INQUIRIES_BONUSES.put(0,3.0);
		C1_INQUIRIES_BONUSES.put(1,0.75);
		C1_INQUIRIES_BONUSES.put(2,0.0);
		C1_INQUIRIES_BONUSES.put(3,-0.5);
		C1_INQUIRIES_BONUSES.put(4,-1.0);
		C1_INQUIRIES_BONUSES.put(5,-3.0);
		C1_INQUIRIES_BONUSES.put(6,-6.0);
		C1_INQUIRIES_BONUSES.put(7,-10.0);
	}
	
	/**
	 * termBonuses, used in Scorer
	 * maps term length in months to bonuses
	 */
	static{
		C1_TERM_BONUSES = new HashMap<Integer, Double>();
		C1_TERM_BONUSES.put(36,0.0);
		C1_TERM_BONUSES.put(60,1.0);
	}
	
	/**
	 * ficoBonuses, used in Scorer
	 * maps ficoLow in each range to bonuses
	 * everything over the last entry is mapped to the last entry
	 */
	static {
		C1_FICO_BONUSES= new TreeMap<Integer, Double>();
		C1_FICO_BONUSES.put(660,-1.0);
		C1_FICO_BONUSES.put(670,-0.5);
		C1_FICO_BONUSES.put(680,0.0);
		C1_FICO_BONUSES.put(700,1.0);
		C1_FICO_BONUSES.put(710,2.0);
		C1_FICO_BONUSES.put(735,3.0);
	}
	
	/**
	 * usageBonuses, used in Scorer
	 * maps usage String to bonuses
	 */
	static {
		C1_USAGE_BONUSES = new HashMap<String, Double>();
		C1_USAGE_BONUSES.put("DEBT_CONSOLIDATION", 1.5);
		C1_USAGE_BONUSES.put("CREDIT_CARD", 2.0);
		C1_USAGE_BONUSES.put("HOUSE", 1.0);
		C1_USAGE_BONUSES.put("OTHER", -4.0);
		C1_USAGE_BONUSES.put("HOME_IMPROVEMENT", -4.0);
		C1_USAGE_BONUSES.put("MAJOR_PURPOSE", 0.0);
		C1_USAGE_BONUSES.put("WEDDING", 1.0);
		C1_USAGE_BONUSES.put("VACATION", 0.0);
		C1_USAGE_BONUSES.put("CAR", 3.0);
		C1_USAGE_BONUSES.put("MEDICAL", -7.0);
		C1_USAGE_BONUSES.put("RENEWABLE_ENERGY", 0.0);
		C1_USAGE_BONUSES.put("MOVING", -10.0);
		C1_USAGE_BONUSES.put("SMALL_BUSINESS",0.0);
	}
	
	/**
	 * stateBonuses, used in Scorer
	 * maps state String to bonus
	 * any non-key state is mapped to 0
	 */
	static {
		C1_STATE_BONUSES = new HashMap<String, Double>();
		C1_STATE_BONUSES.put("CA", -1.0);
		C1_STATE_BONUSES.put("CT", 0.25);
		C1_STATE_BONUSES.put("MA", 0.25);
		C1_STATE_BONUSES.put("NV", -0.5);
	}
	
	/**
	 * gradeBonuses, used in Scorer
	 * maps grade/subgrade Strings to bonuses
	 */
	static{
		C1_GRADE_BONUSES = new HashMap<String, Double>();
		C1_GRADE_BONUSES.put("A1",2.0);
		C1_GRADE_BONUSES.put("A2",2.0);
		C1_GRADE_BONUSES.put("A3",2.0);
		C1_GRADE_BONUSES.put("A4",2.0);
		C1_GRADE_BONUSES.put("A5",2.0);
		C1_GRADE_BONUSES.put("B1",-10.0);
		C1_GRADE_BONUSES.put("B2",-5.0);
		C1_GRADE_BONUSES.put("B3",-2.0);
		C1_GRADE_BONUSES.put("B4",-1.5);
		C1_GRADE_BONUSES.put("B5",-1.0);
		C1_GRADE_BONUSES.put("C1",-0.25);
		C1_GRADE_BONUSES.put("C2",0.0);
		C1_GRADE_BONUSES.put("C3",0.25);
		C1_GRADE_BONUSES.put("C4",0.5);
		C1_GRADE_BONUSES.put("C5",1.5);
		C1_GRADE_BONUSES.put("D1",1.5);
		C1_GRADE_BONUSES.put("D2",2.0);
		C1_GRADE_BONUSES.put("D3",2.0);
		C1_GRADE_BONUSES.put("D4",2.0);
		C1_GRADE_BONUSES.put("D5",2.0);
		C1_GRADE_BONUSES.put("E1",2.0);
		C1_GRADE_BONUSES.put("E2",2.0);
		C1_GRADE_BONUSES.put("E3",2.0);
		C1_GRADE_BONUSES.put("E4",2.0);
		C1_GRADE_BONUSES.put("E5",2.0);
		C1_GRADE_BONUSES.put("F1",2.0);
		C1_GRADE_BONUSES.put("F2",2.0);
		C1_GRADE_BONUSES.put("F3",2.0);
		C1_GRADE_BONUSES.put("F4",2.0);
		C1_GRADE_BONUSES.put("F5",2.0);
		C1_GRADE_BONUSES.put("G1",2.0);
		C1_GRADE_BONUSES.put("G2",2.0);
		C1_GRADE_BONUSES.put("G3",2.0);
		C1_GRADE_BONUSES.put("G4",2.0);
		C1_GRADE_BONUSES.put("G5",2.0);
	}
	/// NOW FOR THE FILTER PARAMTERS   /////////////////////////////////////////////////////
	 
	/**
	 * invalidSubgradesFor36
	 * list of invalid subgrades for term length 36
	 */
	static {
		C1_INVALID_SUBGRADES_36 = new ArrayList<String>();
		C1_INVALID_SUBGRADES_36.add("A1");
		C1_INVALID_SUBGRADES_36.add("A2");
		C1_INVALID_SUBGRADES_36.add("A3");
		C1_INVALID_SUBGRADES_36.add("A4");
		C1_INVALID_SUBGRADES_36.add("A5");

                C1_INVALID_SUBGRADES_36.add("B1");
                
		C1_INVALID_SUBGRADES_36.add("B2");
		C1_INVALID_SUBGRADES_36.add("B3");
		C1_INVALID_SUBGRADES_36.add("B4");
//		C1_INVALID_SUBGRADES_36.add("B5");
/*		C1_INVALID_SUBGRADES_36.add("C1");
		C1_INVALID_SUBGRADES_36.add("C2");
		C1_INVALID_SUBGRADES_36.add("C3");
		C1_INVALID_SUBGRADES_36.add("C4");
		C1_INVALID_SUBGRADES_36.add("C5");
		C1_INVALID_SUBGRADES_36.add("D1");
		C1_INVALID_SUBGRADES_36.add("D2");
		C1_INVALID_SUBGRADES_36.add("D3");
		C1_INVALID_SUBGRADES_36.add("D4");
		C1_INVALID_SUBGRADES_36.add("D5");
		C1_INVALID_SUBGRADES_36.add("E1");
		C1_INVALID_SUBGRADES_36.add("E2");
		C1_INVALID_SUBGRADES_36.add("E3");
		C1_INVALID_SUBGRADES_36.add("E4");
		C1_INVALID_SUBGRADES_36.add("E5");
*/                
		C1_INVALID_SUBGRADES_36.add("F1");
		C1_INVALID_SUBGRADES_36.add("F2");
		C1_INVALID_SUBGRADES_36.add("F3");
		C1_INVALID_SUBGRADES_36.add("F4");
		C1_INVALID_SUBGRADES_36.add("F5");
		C1_INVALID_SUBGRADES_36.add("G1");
		C1_INVALID_SUBGRADES_36.add("G2");
		C1_INVALID_SUBGRADES_36.add("G3");
		C1_INVALID_SUBGRADES_36.add("G4");
		C1_INVALID_SUBGRADES_36.add("G5");

        }

	/**
	 * invalidSubgradesFor60
	 * list of invalid subgrades for term length 60
	 */
	static {
		C1_INVALID_SUBGRADES_60 = new ArrayList<String>();
		C1_INVALID_SUBGRADES_60.add("A1");
		C1_INVALID_SUBGRADES_60.add("A2");
		C1_INVALID_SUBGRADES_60.add("A3");
		C1_INVALID_SUBGRADES_60.add("A4");
		C1_INVALID_SUBGRADES_60.add("A5");

                C1_INVALID_SUBGRADES_60.add("B1");
		C1_INVALID_SUBGRADES_60.add("B2");
		C1_INVALID_SUBGRADES_60.add("B3");
		C1_INVALID_SUBGRADES_60.add("B4");
		C1_INVALID_SUBGRADES_60.add("B5");
		C1_INVALID_SUBGRADES_60.add("C1");
		C1_INVALID_SUBGRADES_60.add("C2");
		C1_INVALID_SUBGRADES_60.add("C3");
                
		C1_INVALID_SUBGRADES_60.add("C4");
		C1_INVALID_SUBGRADES_60.add("C5");
                
		C1_INVALID_SUBGRADES_60.add("G1");
		C1_INVALID_SUBGRADES_60.add("G2");
		C1_INVALID_SUBGRADES_60.add("G3");
		C1_INVALID_SUBGRADES_60.add("G4");
		C1_INVALID_SUBGRADES_60.add("G5");

        }
	
	/**
	 * termSubgradeCheck, used in Filter
	 * maps term length of Loan to all invalid subgrades for this term length
	 */
	static {
		C1_TERM_SUBGRADE_CHECK = new HashMap<Integer, List<String>>();
		C1_TERM_SUBGRADE_CHECK.put(36, C1_INVALID_SUBGRADES_36);
		C1_TERM_SUBGRADE_CHECK.put(60, C1_INVALID_SUBGRADES_60);		
	}

        /**
         * DTITermCheck, used in Filter
         * maps maximum DTI of Loan for each term
         */
        static {
            C1_DTI_TERM_CHECK = new TreeMap<Integer, Double>();
            C1_DTI_TERM_CHECK.put(36, C1_DTI_MAX_36);        
            C1_DTI_TERM_CHECK.put(60, C1_DTI_MAX_60);
        }        
     /**
	 * invalidgrades
	 * list of invalid grades
	 */
      static {
           C1_INVALID_GRADES = new ArrayList<String>();
//           C1_INVALID_GRADES.add("A");
//           C1_INVALID_GRADES.add("G");

      }	
	/**
	 * stateLoanAmountCheck, used in Filter
	 * maps state String to the maximum loan amount
	 * if state is not listed, it has no maximum loan amount
	 */
	static{
		C1_STATE_LOAN_AMOUNT_CHECK = new HashMap<String, Double>();
	}
	
	/**
	 * invalid purposes for C1 loans, used in Filter
	 * lists all valid purposes for loans
	 */
	static{
		C1_INVALID_PURPOSES = new ArrayList<String>();
		C1_INVALID_PURPOSES.add("CAR");
		C1_INVALID_PURPOSES.add("SMALL_BUSINESS");
		C1_INVALID_PURPOSES.add("BUSINESS");
		C1_INVALID_PURPOSES.add("MEDICAL");
		C1_INVALID_PURPOSES.add("VACATION");
		C1_INVALID_PURPOSES.add("MOVING");
	}
	
	/**
	 * fico minimum values for 36 term, used in Filter
	 * maps grade to its minimum fico value
	 */
	static {
		C1_FICO_MINS_FOR_36 = new HashMap<String, Integer>();
		C1_FICO_MINS_FOR_36.put("A",0);
		C1_FICO_MINS_FOR_36.put("B",690);
		C1_FICO_MINS_FOR_36.put("C",680);
		C1_FICO_MINS_FOR_36.put("D",660);
		C1_FICO_MINS_FOR_36.put("E",660);
		C1_FICO_MINS_FOR_36.put("F",0);
		C1_FICO_MINS_FOR_36.put("G",0);
	}
	
	/**
	 * fico minimum values for 60 term, used in Filter
	 * maps grade to its minimum fico value
	 */
	static {
		C1_FICO_MINS_FOR_60 = new HashMap<String, Integer>();
		C1_FICO_MINS_FOR_60.put("A",0);
		C1_FICO_MINS_FOR_60.put("B",0);
		C1_FICO_MINS_FOR_60.put("C",700);
		C1_FICO_MINS_FOR_60.put("D",690);
		C1_FICO_MINS_FOR_60.put("E",680);
		C1_FICO_MINS_FOR_60.put("F",660);
		C1_FICO_MINS_FOR_60.put("G",0);
	}
	
	/**
	 * ficoLoanCheck, used in Filter
	 * maps term length to subgrade-fico limit mapping
	 */
	static{
		C1_FICO_LOAN_CHECK = new HashMap<Integer, Map<String, Integer>>();
		C1_FICO_LOAN_CHECK.put(36, C1_FICO_MINS_FOR_36);
		C1_FICO_LOAN_CHECK.put(60, C1_FICO_MINS_FOR_60);
	}
	
	/**
	 * ficoInquiriesCheck, used in Filter
	 * maps ficoLow range to maximum number of inquries in last 6 months
	 * everything over the last entry is mapped to the last entry
	 * 
	 */
	static {
		C1_FICO_INQUIRIES_CHECK= new TreeMap<Integer, Integer>();
		C1_FICO_INQUIRIES_CHECK.put(660,0);
		C1_FICO_INQUIRIES_CHECK.put(670,1);
		C1_FICO_INQUIRIES_CHECK.put(690,2);
		C1_FICO_INQUIRIES_CHECK.put(700,4);
		C1_FICO_INQUIRIES_CHECK.put(735,Integer.MAX_VALUE);
	}
        

		
	private Consts(){
		throw new AssertionError();
	}
}
