package arcadia;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Filter {
	private final TreeMap<Integer, Integer> ficoInquiriesCheck;
	private final Map<String, Double> stateLoanAmountCheck;
	private final Map<Integer,List<String>> termSubgradeCheck;
	private final boolean isWholeValid, isFractionalValid, is36Valid, is60Valid;
	private final int empLengthMin, ficoLowThreshhold;
        private final double dtiMax, minIncome;
        private final TreeMap<Integer, Double> dtiTermCheck;
        private final TreeMap<Integer, ArrayList<Double>> dtiInqFico;
	private final List<String> purposeCheck, gradeCheck;
	private final Map<Integer, Map<String, Integer>> ficoLoanCheck;
        private final TreeMap<Integer, String> creditModel;
	
	public Filter(double minIncome, TreeMap<Integer, ArrayList<Double>> dtiInqFico, TreeMap<Integer, Integer> ficoInquiriesCheck, Map<String, Double> stateLoanAmountCheck, Map<Integer, Map<String, Integer>> ficoLoanCheck, Map<Integer,List<String>> termSubgradeCheck, List<String> gradeCheck, List<String> purposeCheck, int empLengthMin, double dtiMax, TreeMap<Integer, Double> dtiTermCheck, int ficoLowThreshhold, boolean isWholeValid, boolean isFractionalValid, boolean is36Valid, boolean is60Valid, TreeMap<Integer, String> creditModel){
		this.ficoInquiriesCheck = ficoInquiriesCheck;
		this.stateLoanAmountCheck = stateLoanAmountCheck;
		this.termSubgradeCheck = termSubgradeCheck;
		this.empLengthMin = empLengthMin;
                this.dtiMax = dtiMax;
                this.minIncome = minIncome;
                this.dtiTermCheck = dtiTermCheck;
                this.dtiInqFico = dtiInqFico;
		this.ficoLowThreshhold = ficoLowThreshhold;
		this.purposeCheck = purposeCheck;
                this.gradeCheck = gradeCheck;
		this.ficoLoanCheck = ficoLoanCheck;
		this.is36Valid = is36Valid;
		this.is60Valid = is60Valid;
		this.isFractionalValid = isFractionalValid;
		this.isWholeValid = isWholeValid;
                this.creditModel = creditModel;
	}
	
	public boolean checkLoan(Loan l){
            TreeMap <Double, List<Double>> modelMatrix = new TreeMap<>();
            if (l.getFicoRangeLow() <700) { modelMatrix = Consts.LF_DTI_INQ;
                } else { 
                modelMatrix = Consts.HF_DTI_INQ;
            }
            //final index represents creditModel level  0=LEVEL_1, 2=LEVEL3//
//            System.out.println("DTI: " + l.getDti() + "  " + creditModel.get(l.getTerm()) + "  FICO " +l.getFicoRangeLow() + "  maxinq: " + modelMatrix.ceilingEntry(l.getDti()).getValue().get(2) + "  InqLast6Mths: " + l.getInqLast6Mths());
                        
/*		if ((!isFractionalValid && l.getInitialListStatus().equals("F")) || (!isWholeValid && l.getInitialListStatus().equals("W"))){
			//if the loan is invalid status (whole or fractional)
			return false;
		}
*/		
		if ((!is36Valid && l.getTerm()==36) || (!is60Valid && l.getTerm()==60)){
			//if term is invalid (36 or 60)
			return false;
		}
		if (stateLoanAmountCheck.get(l.getAddrState())!=null){
			//then this state has a minimum threshhold
			if (l.getLoanAmount() < stateLoanAmountCheck.get(l.getAddrState())){
				//if loan amount falls below the threshhold for this state
				return false;
			}
		}
                
               if (Consts.MVM_CHECK.get(l.getAddrState())!=null){
			//then this state has a maximum allowable interest rate
			if (l.getIntRate() > Consts.MVM_CHECK.get(l.getAddrState())){
				//if loan amount falls below the threshhold for this state
				return false;
			}
		}
                
          if (gradeCheck.contains(l.getGrade())){
			//if grade is not allowed for this PORTFOLIO
			return false;
		}		

          if (termSubgradeCheck.get(l.getTerm()).contains(l.getSubgrade())){
			//if subgrade is not allowed for this term length
			return false;
		}
		if (l.getEmpTitle().equals("")){
			//if borrower is unemployed
			return false;
		}
		if (l.getEmpLength() < empLengthMin){
			//if borrower has not been employed for long enough
			return false;
		}
		if (l.getAnnualInc() < minIncome){
			//if borrower is not making enough money
			return false;
		}                
   // replacing FICO/MAX Inquiries check with DTI check      
 
		if (l.getFicoRangeLow()<=ficoLowThreshhold){
			//if ficoLow is below threshhold, we must check for excessive inquiries
			if (l.getInqLast6Mths() > ficoInquiriesCheck.floorEntry(l.getFicoRangeLow()).getValue()){
				//if there are excessive inquiries for this ficoLow level
                   
				return false;
			}
		}

/*           if (l.getDti() > dtiMax){
              //if borrower has too high a dti ratio
              return false;
          }
 */          
           if (l.getDti() > dtiTermCheck.get(l.getTerm()))    {
                        //if dti for this term is greater than the max allowed
                        return false;
           }
            if (purposeCheck.contains(l.getPurpose().toUpperCase())){
			//if purpose does not match valid purposes
			return false;
		}
		
            if (l.getFicoRangeLow()<ficoLoanCheck.get(l.getTerm()).get(l.getGrade())){
			//if loan of specific grade and term doesn't meet fico minimum
			return false;
		}
            
            if(l.getDti() > dtiInqFico.floorEntry(l.getFicoRangeLow()).getValue().get(0)) {
                        // if loan of specific FICO range has DTI that exceeds certain value
                        return false;
                }
            
            if(l.getInqLast6Mths() > dtiInqFico.floorEntry(l.getFicoRangeLow()).getValue().get(1)) {
                        // if loan of specific FICO range has InqLast6Mths that exceeds certain value
                        return false;
                }

            int index = 0;
                 String model = creditModel.get(l.getTerm());
                 switch (model) {
			case "LEVEL_1":
				index = 0;
				break;
			case "LEVEL_2":
				index = 1;
				break;
			case "LEVEL_3":
				index = 2;
				break;
		}
//                 System.out.println("index = " + index + " for model = " + model + "  FICO= " + l.getFicoRangeLow() + " DTI=" + l.getDti() + " inqlast6mths= " + l.getInqLast6Mths());
//                 System.out.println("inqthresh = " + modelMatrix.ceilingEntry(l.getDti()).getValue().get(index));
             if (l.getInqLast6Mths() > modelMatrix.ceilingEntry(l.getDti()).getValue().get(index))        {
           // if loan of specific DTI has InqLast6Mths that exceeds certain value (value depends on creditModel level
            return false;
           }
		return true;
}
}