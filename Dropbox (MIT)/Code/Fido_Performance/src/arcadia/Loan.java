package arcadia;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import net.minidev.json.JSONObject;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class Loan implements Comparable<Loan>{
	private double score;
	private double reqAmount;
	final private long loanID, memberID;
	final private double loanAmount, fundedAmount, intRate, expDefaultRate, serviceFeeRate, 
		installment, annualInc, percentBcGt75, bcUtil, dti,delinqAmnt, revolBal, revolUtil;   
	final private int term, empLength, investorCount, accNowDelinq, accOpenPast24Mths, 
		bcOpenToBuy, delinq2Yrs, ficoRangeLow, ficoRangeHigh, inqLast6Mths, mthsSinceLastDelinq, 
		mthsSinceLastRecord, mthsSinceRecentInq, mthsSinceRecentRevolDelinq, mthsSinceRecentBc,
		mortAcc, openAcc, pubRec, totalBalExMort, totalBcLimit, totalAcc, totalIlHighCreditLimit,
		numRevAccts, mthsSinceRecentBcDlq, pubRecBankruptcies, numAcctsEver120Ppd, 
		chargeoffWithin12Mths, collections12MthsExMed, taxLiens, mthsSinceLastMajorDerog, 
		numSats, numTlOpPast12m, moSinRcntTl, totHiCredLim, totCurBal, avgCurBal, numBcTl, 
		numActvBcTl, numBcSats, pctTlNvrDlq, numTl90gDpd24m, numTl30dpd, numTl120dpd2m, numIlTl, 
		moSinOldIlAcct, numActvRevTl,moSinOldRevTlOp, moSinRcntRevTlOp, totalRevHiLim, 
		numRevTlBalGt0, numOpRevTl,totCollAmt; 

	final private String grade, subgrade, acceptD, expD, listD, creditPullD, reviewStatusD, 
		desc, addrZip, addrState, ilsExpD, empTitle, earliestCrLine;
	final private String homeOwnership; 	//(RENT, OWN, MORTGAGE, OTHER)
	final private String isIncV;			//?????
	final private String reviewStatus; 		// (APPROVED, NOT_APPROVED)
	final private String purpose;			// (DEBT_CONSOLIDATION, MEDICAL, HOME_IMPROVEMENT, 
											// RENEWABLE_ENERGY, SMALL_BUSINESS, WEDDING, 
											// VACATION, MOVING, HOUSE, CAR, MAJOR_PURCHASE, 
											// OTHER)
	final private String initialListStatus; //(W,F)	
	
	
	/**
	 * Constructor for Loan instance 
	 * 
	 *
	 */
	public Loan(long loanID, long memberID, double loanAmount, double fundedAmount, int term, double intRate, double expDefaultRate, 
			double serviceFeeRate, double installment, String grade, String subgrade, int empLength, String homeOwnership, 
			double annualInc, String isIncV, String acceptD, String expD, String listD, String creditPullD, String reviewStatusD, 
			String reviewStatus, String desc, String purpose, String addrZip, String addrState, int investorCount, String ilsExpD, 
			String initialListStatus, String empTitle, int accNowDelinq, int accOpenPast24Mths, int bcOpenToBuy, double percentBcGt75, 
			double bcUtil, double dti, int delinq2Yrs, double delinqAmnt, String earliestCrLine, int ficoRangeLow, int ficoRangeHigh, 
			int inqLast6Mths, int mthsSinceLastDelinq, int mthsSinceLastRecord, int mthsSinceRecentInq, int mthsSinceRecentRevolDelinq, 
			int mthsSinceRecentBc, int mortAcc, int openAcc, int pubRec, int totalBalExMort, double revolBal, double revolUtil,
			int totalBcLimit, int totalAcc, int totalIlHighCreditLimit, int numRevAccts, int mthsSinceRecentBcDlq, int pubRecBankruptcies, 
			int numAcctsEver120Ppd,	int chargeoffWithin12Mths, int collections12MthsExMed, int taxLiens, int mthsSinceLastMajorDerog, 
			int numSats, int numTlOpPast12m, int moSinRcntTl, int totHiCredLim, int totCurBal, int avgCurBal, int numBcTl, 
			int numActvBcTl, int numBcSats, int pctTlNvrDlq, int numTl90gDpd24m, int numTl30dpd, int numTl120dpd2m, 
			int numIlTl, int moSinOldIlAcct, int numActvRevTl, int moSinOldRevTlOp, int moSinRcntRevTlOp, int totalRevHiLim, 
			int numRevTlBalGt0, int numOpRevTl, int totCollAmt) {
		this.loanID = loanID;
		this.memberID = memberID;
		this.loanAmount = loanAmount;
		this.fundedAmount = fundedAmount;
		this.term = term;
		this.intRate = intRate;
		this.expDefaultRate = expDefaultRate;
		this.serviceFeeRate = serviceFeeRate;
		this.installment = installment;
		this.grade = grade;
		this.subgrade = subgrade;
		this.empLength = empLength;
		this.homeOwnership = homeOwnership;
		this.annualInc = annualInc;
		this.isIncV = isIncV;
		this.acceptD = acceptD;
		this.expD = expD;
		this.listD = listD;
		this.creditPullD = creditPullD;
		this.reviewStatusD = reviewStatusD;
		this.reviewStatus = reviewStatus;
		this.desc = desc;
		this.purpose = purpose;
		this.addrZip = addrZip;
		this.addrState = addrState;
		this.investorCount = investorCount;
		this.ilsExpD = ilsExpD;
		this.initialListStatus = initialListStatus;
		this.empTitle = empTitle;
		this.accNowDelinq = accNowDelinq;
		this.accOpenPast24Mths = accOpenPast24Mths;
		this.bcOpenToBuy = bcOpenToBuy;
		this.percentBcGt75 = percentBcGt75;
		this.bcUtil = bcUtil;
		this.dti = dti;
		this.delinq2Yrs = delinq2Yrs;
		this.delinqAmnt	= delinqAmnt;
		this.earliestCrLine = earliestCrLine;
		this.ficoRangeLow = ficoRangeLow;
		this.ficoRangeHigh = ficoRangeHigh;
		this.inqLast6Mths = inqLast6Mths;
		this.mthsSinceLastDelinq = mthsSinceLastDelinq;
		this.mthsSinceLastRecord = mthsSinceLastRecord;
		this.mthsSinceRecentInq = mthsSinceRecentInq;
		this.mthsSinceRecentRevolDelinq = mthsSinceRecentRevolDelinq;
		this.mthsSinceRecentBc = mthsSinceRecentBc;
		this.mortAcc = mortAcc;
		this.openAcc = openAcc;
		this.pubRec = pubRec;
		this.totalBalExMort = totalBalExMort;
		this.revolBal = revolBal;
		this.revolUtil = revolUtil;
		this.totalBcLimit = totalBcLimit;
		this.totalAcc = totalAcc;
		this.totalIlHighCreditLimit = totalIlHighCreditLimit;
		this.numRevAccts = numRevAccts;
		this.mthsSinceRecentBcDlq = mthsSinceRecentBcDlq;
		this.pubRecBankruptcies = pubRecBankruptcies;
		this.numAcctsEver120Ppd = numAcctsEver120Ppd;
		this.chargeoffWithin12Mths = chargeoffWithin12Mths;
		this.collections12MthsExMed = collections12MthsExMed;
		this.taxLiens = taxLiens;
		this.mthsSinceLastMajorDerog = mthsSinceLastMajorDerog;
		this.numSats = numSats;
		this.numTlOpPast12m = numTlOpPast12m;
		this.moSinRcntTl = moSinRcntTl;
		this.totHiCredLim = totHiCredLim;
		this.totCurBal = totCurBal;
		this.avgCurBal = avgCurBal;
		this.numBcTl = numBcTl;
		this.numActvBcTl = numActvBcTl;
		this.numBcSats = numBcSats;
		this.pctTlNvrDlq = pctTlNvrDlq;
		this.numTl90gDpd24m = numTl90gDpd24m;
		this.numTl30dpd = numTl30dpd;
		this.numTl120dpd2m = numTl120dpd2m;
		this.numIlTl = numIlTl;
		this.moSinOldIlAcct = moSinOldIlAcct;
		this.numActvRevTl = numActvRevTl;
		this.moSinOldRevTlOp = moSinOldRevTlOp;
		this.moSinRcntRevTlOp = moSinRcntRevTlOp;
		this.totalRevHiLim = totalRevHiLim;
		this.numRevTlBalGt0 = numRevTlBalGt0;
		this.numOpRevTl = numOpRevTl;
		this.totCollAmt = totCollAmt;	
	}
        
    public Loan(String[] params){
        assert(params.length == 104);
        this.loanID =  Long.parseLong(params[0]);
        this.memberID = Long.valueOf(params[1]);          
        this.loanAmount = Double.parseDouble(params[2]);
        this.fundedAmount = Double.parseDouble(params[3]);
        this.term = Integer.parseInt(params[4]);
        this.intRate = Double.parseDouble(params[5]);
        this.expDefaultRate = Double.parseDouble(params[6]);
        this.serviceFeeRate = Double.parseDouble(params[7]);
        this.installment = Double.parseDouble(params[8]);
        this.grade = params[9];
        this.subgrade = params[10];
        this.empLength = ("".equals(params[11].replaceAll("\\D+", ""))) ? 0 : Integer.parseInt(params[11].replaceAll("\\D+", ""));
        this.homeOwnership = params[12];
        this.annualInc = ("".equals(params[13])) ? 0 : Double.parseDouble(params[13]);    
        this.isIncV = params[14];
        this.acceptD = params[15];
        this.expD = params[16];
        this.listD = params[17];
        this.creditPullD = params[18];
        this.reviewStatusD = params[19];
        this.reviewStatus = params[20];
        this.desc = params[21];
        this.purpose = params[22];
        this.addrZip = params[23];
        this.addrState = params[24];
        this.investorCount = ("".equals(params[25])) ? 0 : Integer.parseInt(params[25]);
        this.initialListStatus = params[26];
        this.empTitle = params[27];
        this.ilsExpD = params[28];
        this.accNowDelinq = ("".equals(params[29])) ? 0 : Integer.parseInt(params[29]);
        this.accOpenPast24Mths = ("".equals(params[30])) ? 0 : Integer.parseInt(params[30]);
        this.bcOpenToBuy = (params[31].trim().length() == 0) ? 0 : Integer.parseInt(params[31]);
        this.bcUtil = (params[32].trim().length() == 0) ? 0 : Double.parseDouble(params[32]);
        this.dti = ("".equals(params[33])) ? 0 : Double.parseDouble(params[33]);
        this.delinq2Yrs = ("".equals(params[34])) ? 0 : Integer.parseInt(params[34]);
        this.delinqAmnt = ("".equals(params[35])) ? 0 : Double.parseDouble(params[35]);
        this.earliestCrLine = params[36];
        this.ficoRangeLow = ("".equals(params[37])) ? 0 : Integer.parseInt(params[37]);
        this.ficoRangeHigh = ("".equals(params[38])) ? 0 : Integer.parseInt(params[38]);
        this.inqLast6Mths = ("".equals(params[39])) ? 0 : Integer.parseInt(params[39]);
        this.mthsSinceLastDelinq = (params[40].trim().length() == 0) ? 0 : Integer.parseInt(params[40]);
        this.mthsSinceLastRecord = (params[41].trim().length() == 0) ? 0 : Integer.parseInt(params[41]);
        this.mthsSinceRecentInq = (params[42].trim().length() == 0) ? 0 : Integer.parseInt(params[42]);
        this.mthsSinceRecentRevolDelinq = (params[43].trim().length() == 0) ? 0 : Integer.parseInt(params[43]);
        this.mthsSinceRecentBc = (params[44].trim().length() == 0) ? 0 : Integer.parseInt(params[44]);
        this.mortAcc = ("".equals(params[45])) ? 0 : Integer.parseInt(params[45]);
        this.openAcc = ("".equals(params[46])) ? 0 : Integer.parseInt(params[46]);
        this.pubRec = ("".equals(params[47])) ? 0 : Integer.parseInt(params[47]);
        this.totalBalExMort = ("".equals(params[48])) ? 0 : Integer.parseInt(params[48]);
        this.revolBal = ("".equals(params[49])) ? 0 : Double.parseDouble(params[49]);
        this.revolUtil = (params[50].trim().length() == 0) ? 0 : Double.parseDouble(params[50]);
        this.totalBcLimit = ("".equals(params[51])) ? 0 : Integer.parseInt(params[51]);
        this.totalAcc = ("".equals(params[52])) ? 0 : Integer.parseInt(params[52]);
        this.totalIlHighCreditLimit = ("".equals(params[53])) ? 0 : Integer.parseInt(params[53]);
        this.numRevAccts = ("".equals(params[54])) ? 0 : Integer.parseInt(params[54]);
        this.mthsSinceRecentBcDlq = (params[55].trim().length() == 0) ? 0 : Integer.parseInt(params[55]);
        this.pubRecBankruptcies = ("".equals(params[56])) ? 0 : Integer.parseInt(params[56]);
        this.numAcctsEver120Ppd = ("".equals(params[57])) ? 0 : Integer.parseInt(params[57]);
        this.chargeoffWithin12Mths = ("".equals(params[58])) ? 0 : Integer.parseInt(params[58]);
        this.collections12MthsExMed = ("".equals(params[59])) ? 0 : Integer.parseInt(params[59]);
        this.taxLiens = ("".equals(params[60])) ? 0 : Integer.parseInt(params[60]);
        this.mthsSinceLastMajorDerog = ("".equals(params[61]) || "null".equals(params[61])) ? 0 : Integer.parseInt(params[61]);
        this.numSats = ("".equals(params[62])) ? 0 : Integer.parseInt(params[62]);
        this.numTlOpPast12m = ("".equals(params[63])) ? 0 : Integer.parseInt(params[63]);
        this.moSinRcntTl = ("".equals(params[64])) ? 0 : Integer.parseInt(params[64]);
        this.totHiCredLim = ("".equals(params[65])) ? 0 : Integer.parseInt(params[65]);
        this.totCurBal = ("".equals(params[66])) ? 0 : Integer.parseInt(params[66]);
        this.avgCurBal = ("".equals(params[67])) ? 0 : Integer.parseInt(params[67]);
        this.numBcTl = ("".equals(params[68])) ? 0 : Integer.parseInt(params[68]);
        this.numActvBcTl = ("".equals(params[69])) ? 0 : Integer.parseInt(params[69]);
        this.numBcSats = ("".equals(params[70])) ? 0 : Integer.parseInt(params[70]);
        this.pctTlNvrDlq = ("".equals(params[71])) ? 0 : Integer.parseInt(params[71]);
        this.numTl90gDpd24m = ("".equals(params[72])) ? 0 : Integer.parseInt(params[72]);
        this.numTl30dpd = ("".equals(params[73])) ? 0 : Integer.parseInt(params[73]);
        this.numTl120dpd2m = (params[74].trim().length() == 0) ? 0 : Integer.parseInt(params[74]);
        this.numIlTl = ("".equals(params[75])) ? 0 : Integer.parseInt(params[75]);
        this.moSinOldIlAcct = (params[76].trim().length() == 0) ? 0 : Integer.parseInt(params[76]);
        this.numActvRevTl = ("".equals(params[77])) ? 0 : Integer.parseInt(params[77]);
        this.moSinOldRevTlOp = ("".equals(params[78])) ? 0 : Integer.parseInt(params[78]);
        this.moSinRcntRevTlOp = ("".equals(params[79])) ? 0 : Integer.parseInt(params[79]);
        this.totalRevHiLim = ("".equals(params[80])) ? 0 : Integer.parseInt(params[80]);
        this.numRevTlBalGt0 = ("".equals(params[81])) ? 0 : Integer.parseInt(params[81]);
        this.numOpRevTl = ("".equals(params[82])) ? 0 : Integer.parseInt(params[82]);
        this.totCollAmt = ("".equals(params[83])) ? 0 : Integer.parseInt(params[83]);
        this.percentBcGt75 = (params[84].trim().length() == 0) ? 0 : Double.parseDouble(params[84]);
    }
	/**
	 * Constructs a Loan object from a JSONObject obj with all the fields
	 * @param obj
	 */
	public Loan(String contentType, Object loanObject){
           if (contentType.equals("application/json"))
        {   
          JSONObject obj = (JSONObject) loanObject;
 		this.memberID = Long.valueOf(((int) obj.get("memberId")));          
		this.loanID =  Long.valueOf((int) obj.get("id"));
		this.loanAmount = (double) obj.get("loanAmount");
 		this.fundedAmount = ((double) obj.get("fundedAmount"));
		this.term = ((int) obj.get("term"));
		this.intRate = ((double) obj.get("intRate"));
 		this.expDefaultRate = ((double) obj.get("expDefaultRate"));
		this.serviceFeeRate = ((double) obj.get("serviceFeeRate"));
		this.installment = ((double) obj.get("installment"));  
		this.grade = (String) obj.get("grade");
		this.subgrade = (String) obj.get("subGrade");
		Object empLength = obj.get("empLength");
		this.empLength = (empLength==null) ? 0 : (int) empLength;
		Object homeOwnership = obj.get("homeOwnership");
		this.homeOwnership = (homeOwnership==null) ? "" : (String) homeOwnership;
		Object annualInc = obj.get("annualInc");
		this.annualInc = (annualInc==null) ? 0 : (double) annualInc;
		Object isIncV = obj.get("isIncV");
		this.isIncV = (isIncV==null) ? "" : (String) isIncV;
		this.acceptD = (String) obj.get("acceptD");
		this.expD = (String) obj.get("expD");
		this.listD = (String) obj.get("listD");
		this.creditPullD = (String) obj.get("creditPullD");
		Object reviewStatusD = obj.get("reviewStatusD");
		this.reviewStatusD = (reviewStatusD==null) ? "" : (String) reviewStatusD;
		this.reviewStatus = (String) obj.get("reviewStatus");
		Object desc = obj.get("desc");
		this.desc = (desc==null) ? "" : (String) desc;
		this.purpose = (String) obj.get("purpose");
		Object addrZip = obj.get("addrZip");
		this.addrZip = (addrZip==null) ? "" : (String) addrZip;
		Object addrState = obj.get("addrState");
		this.addrState = (addrState==null) ? "" : (String) addrState;
		Object investorCountObj = obj.get("investorCount");
		this.investorCount = (investorCountObj==null) ? 0 : (int) investorCountObj;
		this.ilsExpD = (String) obj.get("ilsExpD");
		this.initialListStatus = (String) obj.get("initialListStatus");
		Object empTitle = obj.get("empTitle");
		this.empTitle = (empTitle==null) ? "" : (String) empTitle;
		Object accNowDelinq = obj.get("accNowDelinq");
		this.accNowDelinq = (accNowDelinq==null) ? 0 : (int) accNowDelinq;
		Object accOpenPast24Mths = obj.get("accOpenPast24Mths");
		this.accOpenPast24Mths = (accOpenPast24Mths==null) ? 0 : (int) accOpenPast24Mths;
		Object bcOpenToBuy = obj.get("bcOpenToBuy");
		this.bcOpenToBuy = (bcOpenToBuy==null) ? 0 : (int) bcOpenToBuy;
		Object percentBcGt75 = obj.get("percentBcGt75");
		this.percentBcGt75 = (percentBcGt75==null) ? 0 : (double) percentBcGt75;
		Object bcUtil = obj.get("bcUtil");
		this.bcUtil = (bcUtil==null) ? 0 : (double) bcUtil;
		Object dti = obj.get("dti");
		this.dti = (dti==null) ? 0 : (double) dti;
		Object delinq2Yrs = obj.get("delinq2Yrs");
		this.delinq2Yrs = (delinq2Yrs==null) ? 0 : (int) delinq2Yrs;
		Object delinqAmnt = obj.get("delinqAmnt");
		this.delinqAmnt = (delinqAmnt==null) ? 0 : (double) delinqAmnt;
		Object earliestCrLine = obj.get("earliestCrLine");
		this.earliestCrLine = (earliestCrLine==null) ? "" : (String) earliestCrLine;
		Object ficoRangeLow = obj.get("ficoRangeLow");
		this.ficoRangeLow = (ficoRangeLow==null) ? 0 : (int) ficoRangeLow;
		Object ficoRangeHigh = obj.get("ficoRangeHigh");
		this.ficoRangeHigh = (ficoRangeHigh==null) ? 0 : (int) ficoRangeHigh;
		Object inqLast6Mths = obj.get("inqLast6Mths");
		this.inqLast6Mths = (inqLast6Mths==null) ? 0 : (int) inqLast6Mths;
		Object mthsSinceLastDelinq = obj.get("mthsSinceLastDelinq");
		this.mthsSinceLastDelinq = (mthsSinceLastDelinq==null) ? 0 : (int) mthsSinceLastDelinq;
		Object mthsSinceLastRecord = obj.get("mthsSinceLastRecord");
		this.mthsSinceLastRecord = (mthsSinceLastRecord==null) ? 0 : (int) mthsSinceLastRecord;
		Object mthsSinceRecentInq = obj.get("mthsSinceRecentInq");
		this.mthsSinceRecentInq = (mthsSinceRecentInq==null) ? 0 : (int) mthsSinceRecentInq;
		Object mthsSinceRecentRevolDelinq = obj.get("mthsSinceRecentRevolDelinq");
		this.mthsSinceRecentRevolDelinq = (mthsSinceRecentRevolDelinq==null) ? 0 : (int) mthsSinceRecentRevolDelinq;
		Object mthsSinceRecentBc = obj.get("mthsSinceRecentBc");
		this.mthsSinceRecentBc = (mthsSinceRecentBc==null) ? 0 : (int) mthsSinceRecentBc;
		Object mortAcc = obj.get("mortAcc");
		this.mortAcc = (mortAcc==null) ? 0 : (int) mortAcc;
		Object openAcc = obj.get("openAcc");
		this.openAcc = (openAcc==null) ? 0 : (int) openAcc;
		Object pubRec = obj.get("pubRec");
		this.pubRec = (pubRec==null) ? 0 : (int) pubRec;
		Object totalBalExMort = obj.get("totalBalExMort");
		this.totalBalExMort = (totalBalExMort==null) ? 0 : (int) totalBalExMort;
		Object revolBal = obj.get("revolBal");
		this.revolBal = (revolBal==null) ? 0 : (double) revolBal;
		Object revolUtil = obj.get("revolUtil");
		this.revolUtil = (revolUtil==null) ? 0 : (double) revolUtil;
		Object totalBcLimit = obj.get("totalBcLimit");
		this.totalBcLimit = (totalBcLimit==null) ? 0 : (int) totalBcLimit;
		Object totalAcc = obj.get("totalAcc");
		this.totalAcc = (totalAcc==null) ? 0 : (int) totalAcc;
		Object totalIlHighCreditLimit = obj.get("totalIlHighCreditLimit");
		this.totalIlHighCreditLimit = (totalIlHighCreditLimit==null) ? 0 : (int) totalIlHighCreditLimit;
		Object numRevAccts = obj.get("numRevAccts");
		this.numRevAccts = (numRevAccts==null) ? 0 : (int) numRevAccts;
		Object mthsSinceRecentBcDlq = obj.get("mthsSinceRecentBcDlq");
		this.mthsSinceRecentBcDlq = (mthsSinceRecentBcDlq==null) ? 0 : (int) mthsSinceRecentBcDlq;
		Object pubRecBankruptcies = obj.get("pubRecBankruptcies");
		this.pubRecBankruptcies = (pubRecBankruptcies==null) ? 0 : (int) pubRecBankruptcies;
		Object numAcctsEver120Ppd = obj.get("numAcctsEver120Ppd");
		this.numAcctsEver120Ppd = (numAcctsEver120Ppd==null) ? 0 : (int) numAcctsEver120Ppd;
		Object chargeoffWithin12Mths = obj.get("chargeoffWithin12Mths");
		this.chargeoffWithin12Mths = (chargeoffWithin12Mths==null) ? 0 : (int) chargeoffWithin12Mths;
		Object collections12MthsExMed = obj.get("collections12MthsExMed");
		this.collections12MthsExMed = (collections12MthsExMed==null) ? 0 : (int) collections12MthsExMed;
		Object taxLiens = obj.get("taxLiens");
		this.taxLiens = (taxLiens==null) ? 0 : (int) taxLiens;
		Object mthsSinceLastMajorDerog = obj.get("mthsSinceLastMajorDerog");
		this.mthsSinceLastMajorDerog = (mthsSinceLastMajorDerog==null) ? 0 : (int) mthsSinceLastMajorDerog;
		Object numSats = obj.get("numSats");
		this.numSats = (numSats==null) ? 0 : (int) numSats;
		Object numTlOpPast12m = obj.get("numTlOpPast12m");
		this.numTlOpPast12m = (numTlOpPast12m==null) ? 0 : (int) numTlOpPast12m;
		Object moSinRcntTl = obj.get("moSinRcntTl");
		this.moSinRcntTl = (moSinRcntTl==null) ? 0 : (int) moSinRcntTl;
		Object totHiCredLim = obj.get("totHiCredLim");
		this.totHiCredLim = (totHiCredLim==null) ? 0 : (int) totHiCredLim;
		Object totCurBal = obj.get("totCurBal");
		this.totCurBal = (totCurBal==null) ? 0 : (int) totCurBal;
		Object avgCurBal = obj.get("avgCurBal");
		this.avgCurBal = (avgCurBal==null) ? 0 : (int) avgCurBal;
		Object numBcTl = obj.get("numBcTl");
		this.numBcTl = (numBcTl==null) ? 0 : (int) numBcTl;
		Object numActvBcTl = obj.get("numActvBcTl");
		this.numActvBcTl = (numActvBcTl==null) ? 0 : (int) numActvBcTl;
		Object numBcSats = obj.get("numBcSats");
		this.numBcSats = (numBcSats==null) ? 0 : (int) numBcSats;
		Object pctTlNvrDlq = obj.get("pctTlNvrDlq");
		this.pctTlNvrDlq = (pctTlNvrDlq==null) ? 0 : (int) pctTlNvrDlq;
		Object numTl90gDpd24m = obj.get("numTl90gDpd24m");
		this.numTl90gDpd24m = (numTl90gDpd24m==null) ? 0 : (int) numTl90gDpd24m;
		Object numTl30dpd = obj.get("numTl30dpd");
		this.numTl30dpd = (numTl30dpd==null) ? 0 : (int) numTl30dpd;
		Object numTl120dpd2m = obj.get("numTl120dpd2m");
		this.numTl120dpd2m = (numTl120dpd2m==null) ? 0 : (int) numTl120dpd2m;
		Object numIlTl = obj.get("numIlTl");
		this.numIlTl = (numIlTl==null) ? 0 : (int) numIlTl;
		Object moSinOldIlAcct = obj.get("moSinOldIlAcct");
		this.moSinOldIlAcct = (moSinOldIlAcct==null) ? 0 : (int) moSinOldIlAcct;
		Object numActvRevTl = obj.get("numActvRevTl");
		this.numActvRevTl = (numActvRevTl==null) ? 0 : (int) numActvRevTl;
		Object moSinOldRevTlOp = obj.get("moSinOldRevTlOp");
		this.moSinOldRevTlOp = (moSinOldRevTlOp==null) ? 0 : (int) moSinOldRevTlOp;
		Object moSinRcntRevTlOp = obj.get("moSinRcntRevTlOp");
		this.moSinRcntRevTlOp = (moSinRcntRevTlOp==null) ? 0 : (int) moSinRcntRevTlOp;
		Object totalRevHiLim = obj.get("totalRevHiLim");
		this.totalRevHiLim = (totalRevHiLim==null) ? 0 : (int) totalRevHiLim;
		Object numRevTlBalGt0 = obj.get("numRevTlBalGt0");
		this.numRevTlBalGt0 = (numRevTlBalGt0==null) ? 0 : (int) numRevTlBalGt0;
		Object numOpRevTl = obj.get("numOpRevTl");
		this.numOpRevTl = (numOpRevTl==null) ? 0 : (int) numOpRevTl;
		Object totCollAmt = obj.get("totCollAmt");
		this.totCollAmt = (totCollAmt==null) ? 0 : (int) totCollAmt;
          
        } else { //if (contentType.equals("text/plain")) 
             
           CSVRecord  obj3 = (CSVRecord) loanObject;

          this.loanID =  Long.valueOf(Integer.parseInt((String)obj3.get("LOAN_ID")));
		this.memberID = Long.valueOf(Integer.parseInt((String) obj3.get("MEMBER_ID")));          
		this.loanAmount = Double.parseDouble((String) obj3.get("LOAN_AMOUNT"));                
		this.fundedAmount = Double.parseDouble((String) obj3.get("FUNDED_AMOUNT"));
		this.term = Integer.parseInt((String) obj3.get("TERM"));
          Object intRateString = obj3.get("INTEREST_RATE");
          this.intRate =Double.parseDouble((String) intRateString);
		this.expDefaultRate = Double.parseDouble((String) obj3.get("EXPECTED_DEFAULT_RATE"));
		this.serviceFeeRate = Double.parseDouble((String) obj3.get("SERVICE_FEE_RATE"));
		this.installment = Double.parseDouble((String) obj3.get("INSTALLMENT"));
		this.grade = (String) obj3.get("GRADE");
		this.subgrade = (String) obj3.get("SUBGRADE");
		Object empLength = obj3.get("EMPLOYMENT_LENGTH");
          this.empLength = (empLength.equals("")) ? 0 : Integer.parseInt((String)empLength);
          Object homeOwnership = obj3.get("HOME_OWNERSHIP");
		this.homeOwnership = (homeOwnership.equals("")) ? "" : (String) homeOwnership;
		Object annualInc = obj3.get("ANNUAL_INCOME");
		this.annualInc = (annualInc.equals("")) ? 0 : Double.parseDouble((String) annualInc);
		Object isIncV = obj3.get("ANNUAL_INCOME_VERIFIED");
		this.isIncV = (isIncV.equals("")) ? "" : (String) isIncV;
		this.acceptD = (String) obj3.get("ACCEPTED_DATE");
		this.expD = (String) obj3.get("EXPIRATION_DATE");
		this.listD = (String) obj3.get("LISTING_DATE");
		this.creditPullD = (String) obj3.get("CREDIT_PULL_DATE");
		Object reviewStatusD = obj3.get("REVIEW_STATUS_DATE");
		this.reviewStatusD = (reviewStatusD.equals("")) ? "" : (String) reviewStatusD;
		this.reviewStatus = (String) obj3.get("REVIEW_STATUS");
		Object desc = obj3.get("DESCRIPTION");
		this.desc = (desc.equals("")) ? "" : (String) desc;
		this.purpose = (String) obj3.get("PURPOSE");
		Object addrZip = obj3.get("ADDRESS_ZIP");
		this.addrZip = (addrZip.equals("")) ? "" : (String) addrZip;
		Object addrState = obj3.get("ADDRESS_STATE");
		this.addrState = (addrState.equals("")) ? "" : (String) addrState;
		Object investorCountObj = obj3.get("INVESTOR_COUNT");
		this.investorCount = (investorCountObj.equals("")) ? 0 : Integer.parseInt((String) investorCountObj);
		this.ilsExpD = (String) obj3.get("INITIAL_LIST_STATUS_EXPIRE_DATE");
		this.initialListStatus = (String) obj3.get("INITIAL_LIST_STATUS");
		Object empTitle = obj3.get("EMP_TITLE");
		this.empTitle = (empTitle.equals("")) ? "" : (String) empTitle;
		Object accNowDelinq = obj3.get("ACCOUNTS_NOW_DELINQUENT");
		this.accNowDelinq = (accNowDelinq.equals("")) ? 0 : Integer.parseInt((String) accNowDelinq);
		Object accOpenPast24Mths = obj3.get("ACCOUNTS_OPEN_IN_PAST_24_MTHS");
		this.accOpenPast24Mths = (accOpenPast24Mths.equals("")) ? 0 : Integer.parseInt((String) accOpenPast24Mths);
		Object bcOpenToBuy = obj3.get("BANK_CARDS_AMOUNT_OPEN_TO_BUY");
		this.bcOpenToBuy = (bcOpenToBuy.equals("")) ? 0 : Integer.parseInt((String) bcOpenToBuy);
		Object percentBcGt75 = obj3.get("PERCENT_OF_BANK_CARDS_OVER_75_PERCENT_UTIL");
		this.percentBcGt75 = (percentBcGt75.equals("")) ? 0 : Double.parseDouble((String) percentBcGt75);
		Object bcUtil = obj3.get("BANK_CARDS_UTILIZATION_RATIO");
		this.bcUtil = (bcUtil.equals("")) ? 0 : Double.parseDouble((String) bcUtil);
		Object dti = obj3.get("DEBT_TO_INCOME_RATIO");
		this.dti = (dti.equals("")) ? 0 : Double.parseDouble((String) dti);
		Object delinq2Yrs = obj3.get("DELINQUENCIES_IN_LAST_2_YEARS");
		this.delinq2Yrs = (delinq2Yrs.equals("")) ? 0 : Integer.parseInt((String) delinq2Yrs);
		Object delinqAmnt = obj3.get("DELINQUENT_AMOUNT");
		this.delinqAmnt = (delinqAmnt.equals("")) ? 0 : Double.parseDouble((String) delinqAmnt);
		Object earliestCrLine = obj3.get("EARLIEST_CREDIT_LINE_DATE");
		this.earliestCrLine = (earliestCrLine.equals("")) ? "" : (String) earliestCrLine;
		Object ficoRangeLow = obj3.get("FICO_RANGE_LOW");
		this.ficoRangeLow = (ficoRangeLow.equals("")) ? 0 : Integer.parseInt((String) ficoRangeLow);
		Object ficoRangeHigh = obj3.get("FICO_RANGE_HIGH");
		this.ficoRangeHigh = (ficoRangeHigh.equals("")) ? 0 : Integer.parseInt((String) ficoRangeHigh);
          Object inqLast6Mths = obj3.get("INQUIRIES_IN_LAST_6_MONTHS");
		this.inqLast6Mths = (inqLast6Mths.equals("")) ? 0 : Integer.parseInt((String) inqLast6Mths);
		Object mthsSinceLastDelinq = obj3.get("MONTHS_SINCE_LAST_DELINQUENCY");
		this.mthsSinceLastDelinq = (mthsSinceLastDelinq.equals("")) ? 0 : Integer.parseInt((String) mthsSinceLastDelinq);
          
          Object mthsSinceLastRecord = obj3.get("MONTHS_SINCE_LAST_RECORD");

		this.mthsSinceLastRecord = (mthsSinceLastRecord.equals("")) ? 0 : Integer.parseInt((String) mthsSinceLastRecord);

          Object mthsSinceRecentInq = obj3.get("MONTHS_SINCE_LAST_INQUIRY");
          this.mthsSinceRecentInq = (mthsSinceRecentInq.equals("")) ? 0 : Integer.parseInt((String) mthsSinceRecentInq);
		Object mthsSinceRecentRevolDelinq = obj3.get("MONTHS_SINCE_RECENT_REVOLVING_DELINQUENCY");
		this.mthsSinceRecentRevolDelinq = (mthsSinceRecentRevolDelinq.equals("")) ? 0 : Integer.parseInt((String) mthsSinceRecentRevolDelinq);
		Object mthsSinceRecentBc = obj3.get("MONTHS_SINCE_RECENT_BANK_CARD");
          this.mthsSinceRecentBc = (mthsSinceRecentBc.equals("")) ? 0 : Integer.parseInt((String) mthsSinceRecentBc);
		Object mortAcc = obj3.get("MORTGAGE_ACCOUNTS");
		this.mortAcc = (mortAcc.equals("")) ? 0 : Integer.parseInt((String) mortAcc);
		Object openAcc = obj3.get("OPEN_ACCOUNTS");
		this.openAcc = (openAcc.equals("")) ? 0 : Integer.parseInt((String) openAcc);
		Object pubRec = obj3.get("PUBLIC_RECORDS");
		this.pubRec = (pubRec.equals("")) ? 0 : Integer.parseInt((String) pubRec);
		Object totalBalExMort = obj3.get("TOTAL_BALANCE_EXCLUDING_MORTGAGE");
		this.totalBalExMort = (totalBalExMort.equals("")) ? 0 : Integer.parseInt((String) totalBalExMort);
		Object revolBal = obj3.get("REVOLVING_BALANCE");
		this.revolBal = (revolBal.equals("")) ? 0 : Double.parseDouble((String) revolBal);
		Object revolUtil = obj3.get("REVOLVING_UTILIZATION");
		this.revolUtil = (revolUtil.equals("")) ? 0 : Double.parseDouble((String) revolUtil);
		Object totalBcLimit = obj3.get("TOTAL_BANK_CARD_LIMIT");
		this.totalBcLimit = (totalBcLimit.equals("")) ? 0 : Integer.parseInt((String) totalBcLimit);
		Object totalAcc = obj3.get("TOTAL_ACCOUNTS");
		this.totalAcc = (totalAcc.equals("")) ? 0 : Integer.parseInt((String) totalAcc);
		Object totalIlHighCreditLimit = obj3.get("TOTAL_IL_HIGH_CREDIT_LIMIT");
		this.totalIlHighCreditLimit = (totalIlHighCreditLimit.equals("")) ? 0 : Integer.parseInt((String) totalIlHighCreditLimit);
		Object numRevAccts = obj3.get("NUM_REV_ACCTS");
		this.numRevAccts = (numRevAccts.equals("")) ? 0 : Integer.parseInt((String) numRevAccts);
//
          Object mthsSinceRecentBcDlq = obj3.get("MTHS_SINCE_RECENT_BC_DLQ");
		this.mthsSinceRecentBcDlq = (mthsSinceRecentBcDlq.equals("")) ? 0 : Integer.parseInt((String) mthsSinceRecentBcDlq);
		Object pubRecBankruptcies = obj3.get("PUB_REC_BANKRUPTCIES");
		this.pubRecBankruptcies = (pubRecBankruptcies.equals("")) ? 0 : Integer.parseInt((String) pubRecBankruptcies);
		Object numAcctsEver120Ppd = obj3.get("NUM_ACCTS_EVER_120_PPD");
		this.numAcctsEver120Ppd = (numAcctsEver120Ppd.equals("")) ? 0 : Integer.parseInt((String) numAcctsEver120Ppd);
		Object chargeoffWithin12Mths = obj3.get("CHARGEOFF_WITHIN_12_MTHS");
		this.chargeoffWithin12Mths = (chargeoffWithin12Mths.equals("")) ? 0 : Integer.parseInt((String) chargeoffWithin12Mths);
		Object collections12MthsExMed = obj3.get("COLLECTIONS_12_MTHS_EXMED");
		this.collections12MthsExMed = (collections12MthsExMed.equals("")) ? 0 : Integer.parseInt((String) collections12MthsExMed);
		Object taxLiens = obj3.get("TAX_LIENS");
		this.taxLiens = (taxLiens.equals("")) ? 0 : Integer.parseInt((String) taxLiens);
		Object mthsSinceLastMajorDerog = obj3.get("MTHS_SINCE_LAST_MAJOR_DEROG");
		this.mthsSinceLastMajorDerog = (mthsSinceLastMajorDerog.equals("")) ? 0 : Integer.parseInt((String) mthsSinceLastMajorDerog);
		Object numSats = obj3.get("NUM_STATS");
		this.numSats = (numSats.equals("")) ? 0 : Integer.parseInt((String) numSats);
		Object numTlOpPast12m = obj3.get("NUM_TL_OP_PAST_12_M");
		this.numTlOpPast12m = (numTlOpPast12m.equals("")) ? 0 : Integer.parseInt((String) numTlOpPast12m);
		Object moSinRcntTl = obj3.get("MO_SIN_RCNT_TL");
		this.moSinRcntTl = (moSinRcntTl.equals("")) ? 0 : Integer.parseInt((String) moSinRcntTl);
		Object totHiCredLim = obj3.get("TO_HI_CRED_LIM");
		this.totHiCredLim = (totHiCredLim.equals("")) ? 0 : Integer.parseInt((String) totHiCredLim);
		Object totCurBal = obj3.get("TO_CUR_BAL");
		this.totCurBal = (totCurBal.equals("")) ? 0 : Integer.parseInt((String) totCurBal);
		Object avgCurBal = obj3.get("AVG_CUR_BAL");
		this.avgCurBal = (avgCurBal.equals("")) ? 0 : Integer.parseInt((String) avgCurBal);
		Object numBcTl = obj3.get("NUM_BC_TL");
		this.numBcTl = (numBcTl.equals("")) ? 0 : Integer.parseInt((String) numBcTl);
		Object numActvBcTl = obj3.get("NUM_ACTV_BC_TL");
		this.numActvBcTl = (numActvBcTl.equals("")) ? 0 : Integer.parseInt((String) numActvBcTl);
		Object numBcSats = obj3.get("NUM_BC_STATS");
		this.numBcSats = (numBcSats.equals("")) ? 0 : Integer.parseInt((String) numBcSats);
		Object pctTlNvrDlq = obj3.get("PCT_TL_NVR_DLQ");
		this.pctTlNvrDlq = (pctTlNvrDlq.equals("")) ? 0 : Integer.parseInt((String) pctTlNvrDlq);
		Object numTl90gDpd24m = obj3.get("NUM_TL_90_G_DPD_24_M");
		this.numTl90gDpd24m = (numTl90gDpd24m.equals("")) ? 0 : Integer.parseInt((String) numTl90gDpd24m);
		Object numTl30dpd = obj3.get("NUM_TL_30_DPD");
		this.numTl30dpd = (numTl30dpd.equals("")) ? 0 : Integer.parseInt((String) numTl30dpd);
		Object numTl120dpd2m = obj3.get("NUM_TL_120_DPD_2_M");
		this.numTl120dpd2m = (numTl120dpd2m.equals("")) ? 0 : Integer.parseInt((String) numTl120dpd2m);
		Object numIlTl = obj3.get("NUM_IL_TL");
		this.numIlTl = (numIlTl.equals("")) ? 0 : Integer.parseInt((String) numIlTl);
		Object moSinOldIlAcct = obj3.get("MO_SIN_OLD_IL_ACCT");
		this.moSinOldIlAcct = (moSinOldIlAcct.equals("")) ? 0 : Integer.parseInt((String) moSinOldIlAcct);
		Object numActvRevTl = obj3.get("NUM_ACTV_REV_TL");
		this.numActvRevTl = (numActvRevTl.equals("")) ? 0 : Integer.parseInt((String) numActvRevTl);
		Object moSinOldRevTlOp = obj3.get("MO_SIN_OLD_REV_TL_OP");
		this.moSinOldRevTlOp = (moSinOldRevTlOp.equals("")) ? 0 : Integer.parseInt((String) moSinOldRevTlOp);
		Object moSinRcntRevTlOp = obj3.get("MO_SIN_RCNT_REV_TL_OP");
		this.moSinRcntRevTlOp = (moSinRcntRevTlOp.equals("")) ? 0 : Integer.parseInt((String) moSinRcntRevTlOp);
		Object totalRevHiLim = obj3.get("TOTAL_REV_HI_LIM");
		this.totalRevHiLim = (totalRevHiLim.equals("")) ? 0 : Integer.parseInt((String) totalRevHiLim);
		Object numRevTlBalGt0 = obj3.get("NUM_REV_TL_BAL_GT0");
		this.numRevTlBalGt0 = (numRevTlBalGt0.equals("")) ? 0 : Integer.parseInt((String) numRevTlBalGt0);
		Object numOpRevTl = obj3.get("NUM_OP_REV_TL");
		this.numOpRevTl = (numOpRevTl.equals("")) ? 0 : Integer.parseInt((String) numOpRevTl);
		Object totCollAmt = obj3.get("TOT_COLL_AMT");
		this.totCollAmt = (totCollAmt.equals("")) ? 0 : Integer.parseInt((String) totCollAmt);          
             }
          
	}
	
	public double getScore() {
		return score;
	}

	public double getReqAmount() {
		return reqAmount;
	}
	
	public long getLoanID() {
		return loanID;
	}

	public long getMemberID() {
		return memberID;
	}

	public double getLoanAmount() {
		return loanAmount;
	}

	public double getFundedAmount() {
		return fundedAmount;
	}

	public double getIntRate() {
		return intRate;
	}

	public double getExpDefaultRate() {
		return expDefaultRate;
	}

	public double getServiceFeeRate() {
		return serviceFeeRate;
	}

	public double getInstallment() {
		return installment;
	}

	public double getAnnualInc() {
		return annualInc;
	}

	public double getPercentBcGt75() {
		return percentBcGt75;
	}

	public double getBcUtil() {
		return bcUtil;
	}

	public double getDti() {
		return dti;
	}

	public double getDelinqAmnt() {
		return delinqAmnt;
	}

	public double getRevolBal() {
		return revolBal;
	}

	public double getRevolUtil() {
		return revolUtil;
	}

	public int getTerm() {
		return term;
	}

	public int getEmpLength() {
		return empLength;
	}

	public int getInvestorCount() {
		return investorCount;
	}

	public int getAccNowDelinq() {
		return accNowDelinq;
	}

	public int getAccOpenPast24Mths() {
		return accOpenPast24Mths;
	}

	public int getBcOpenToBuy() {
		return bcOpenToBuy;
	}

	public int getDelinq2Yrs() {
		return delinq2Yrs;
	}

	public int getFicoRangeLow() {
		return ficoRangeLow;
	}

	public int getFicoRangeHigh() {
		return ficoRangeHigh;
	}

	public int getInqLast6Mths() {
		return inqLast6Mths;
	}

	public int getMthsSinceLastDelinq() {
		return mthsSinceLastDelinq;
	}

	public int getMthsSinceLastRecord() {
		return mthsSinceLastRecord;
	}

	public int getMthsSinceRecentInq() {
		return mthsSinceRecentInq;
	}

	public int getMthsSinceRecentRevolDelinq() {
		return mthsSinceRecentRevolDelinq;
	}

	public int getMthsSinceRecentBc() {
		return mthsSinceRecentBc;
	}

	public int getMortAcc() {
		return mortAcc;
	}

	public int getOpenAcc() {
		return openAcc;
	}

	public int getPubRec() {
		return pubRec;
	}

	public int getTotalBalExMort() {
		return totalBalExMort;
	}

	public int getTotalBcLimit() {
		return totalBcLimit;
	}

	public int getTotalAcc() {
		return totalAcc;
	}

	public int getTotalIlHighCreditLimit() {
		return totalIlHighCreditLimit;
	}

	public int getNumRevAccts() {
		return numRevAccts;
	}

	public int getMthsSinceRecentBcDlq() {
		return mthsSinceRecentBcDlq;
	}

	public int getPubRecBankruptcies() {
		return pubRecBankruptcies;
	}

	public int getNumAcctsEver120Ppd() {
		return numAcctsEver120Ppd;
	}

	public int getChargeoffWithin12Mths() {
		return chargeoffWithin12Mths;
	}

	public int getCollections12MthsExMed() {
		return collections12MthsExMed;
	}

	public int getTaxLiens() {
		return taxLiens;
	}

	public int getMthsSinceLastMajorDerog() {
		return mthsSinceLastMajorDerog;
	}

	public int getNumSats() {
		return numSats;
	}

	public int getNumTlOpPast12m() {
		return numTlOpPast12m;
	}

	public int getMoSinRcntTl() {
		return moSinRcntTl;
	}

	public int getTotHiCredLim() {
		return totHiCredLim;
	}

	public int getTotCurBal() {
		return totCurBal;
	}

	public int getAvgCurBal() {
		return avgCurBal;
	}

	public int getNumBcTl() {
		return numBcTl;
	}

	public int getNumActvBcTl() {
		return numActvBcTl;
	}

	public int getNumBcSats() {
		return numBcSats;
	}

	public int getPctTlNvrDlq() {
		return pctTlNvrDlq;
	}

	public int getNumTl90gDpd24m() {
		return numTl90gDpd24m;
	}

	public int getNumTl30dpd() {
		return numTl30dpd;
	}

	public int getNumTl120dpd2m() {
		return numTl120dpd2m;
	}

	public int getNumIlTl() {
		return numIlTl;
	}

	public int getMoSinOldIlAcct() {
		return moSinOldIlAcct;
	}

	public int getNumActvRevTl() {
		return numActvRevTl;
	}

	public int getMoSinOldRevTlOp() {
		return moSinOldRevTlOp;
	}

	public int getMoSinRcntRevTlOp() {
		return moSinRcntRevTlOp;
	}

	public int getTotalRevHiLim() {
		return totalRevHiLim;
	}

	public int getNumRevTlBalGt0() {
		return numRevTlBalGt0;
	}

	public int getNumOpRevTl() {
		return numOpRevTl;
	}

	public int getTotCollAmt() {
		return totCollAmt;
	}

	public String getGrade() {
		return grade;
	}

	public String getSubgrade() {
		return subgrade;
	}

	public String getAcceptD() {
		return acceptD;
	}

	public String getExpD() {
		return expD;
	}

	public String getListD() {
		return listD;
	}

	public String getCreditPullD() {
		return creditPullD;
	}

	public String getReviewStatusD() {
		return reviewStatusD;
	}

	public String getDesc() {
		return desc;
	}

	public String getAddrZip() {
		return addrZip;
	}

	public String getAddrState() {
		return addrState;
	}

	public String getIlsExpD() {
		return ilsExpD;
	}

	public String getEmpTitle() {
		return empTitle;
	}

	public String getEarliestCrLine() {
		return earliestCrLine;
	}

	public String getHomeOwnership() {
		return homeOwnership;
	}

	public String getIsIncV() {
		return isIncV;
	}

	public String getReviewStatus() {
		return reviewStatus;
	}

	public String getPurpose() {
		return purpose;
	}

	public String getInitialListStatus() {
		return initialListStatus;
	}

	public void setScore(double score) {
		this.score = score;
	}
	
	public void setReqAmount(double reqAmount) {
		this.reqAmount = reqAmount;
	}
	
	@Override
	public int compareTo(Loan o) {
		return (int) Math.floor((this.getScore()-o.getScore()));
	}

	@Override
	public String toString(){
		StringBuilder result = new StringBuilder();
//		String NEWLINE = System.getProperty("line.separator");
                String TAB = "\t";
		result.append("  ID: " + loanID + TAB);
		result.append("  Amount: " + loanAmount + TAB);
                result.append("  RequestAmount: " + reqAmount + TAB);
		result.append(" " + addrState + TAB);
		result.append("  Rate(%): " + this.intRate + TAB);
		result.append("  " + this.term + " " + this.subgrade + TAB);
                result.append("  DTI: " + this.dti + TAB);          
		result.append("  InqLast6Mths: " + this.inqLast6Mths + TAB);                
		result.append("  FICO: " + this.ficoRangeLow + TAB);
		result.append("  EmpLength: " + this.empLength + TAB);
		result.append("  EmpTitle: " + this.empTitle + TAB);                
//		result.append("  Score: " + score + TAB);
		
		return result.toString();
	}
	
	@Override
	public boolean equals(Object obj){
		if (!(obj instanceof Loan)){
			return false;
		}
		if (obj==this){
			return true;
		}
		
		Loan other = (Loan) obj;
		return new EqualsBuilder().
			append(loanID, other.loanID).
			isEquals();
	}
	
	@Override
	public int hashCode(){
		return new HashCodeBuilder(17,31).
				append(loanID).
				append(memberID).
				toHashCode();
	}
	
	
	
}