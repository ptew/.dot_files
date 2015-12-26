package arcadia;

import java.util.*;

public class Scorer {
	private TreeMap<Integer, Double> inquiriesBonusMap, ficoBonusMap;
	private Map<Integer, Double> termBonusMap;
	private Map<String, Double> usageBonusMap, stateBonusMap, gradeBonusMap;
	private double willCompeteBonus;

	public Scorer(TreeMap<Integer, Double> inquiriesBonus, TreeMap<Integer, Double> ficoBonus, Map<String, Double> usageBonus, Map<String, Double> stateBonus, Map<String, Double> gradeBonus, Map<Integer, Double> termBonus, double willCompeteBonus){
		this.inquiriesBonusMap = inquiriesBonus;
		this.ficoBonusMap = ficoBonus;
		this.usageBonusMap = usageBonus;
		this.stateBonusMap = stateBonus;
		this.gradeBonusMap = gradeBonus;
		this.termBonusMap = termBonus;	
		this.willCompeteBonus = willCompeteBonus;
	}
	
	/**
	 * Computes the score of l by summing appropriate bonuses
	 * @param l
	 * @return
	 */
	public double score(Loan l){	
		double inqBonus = inquiriesBonusMap.floorEntry(l.getInqLast6Mths()).getValue();
		double ficoBonus = ficoBonusMap.floorEntry(l.getFicoRangeLow()).getValue();
		Double usageBonusObj = usageBonusMap.get(l.getPurpose().toUpperCase());
		double usageBonus = (usageBonusObj==null) ? 0.0 : usageBonusObj;
		Double stateBonusObj = stateBonusMap.get(l.getAddrState());
		double stateBonus = (stateBonusObj==null) ? 0.0 : stateBonusObj;
		Double gradeBonusObj = gradeBonusMap.get(l.getSubgrade());
		double gradeBonus = (gradeBonusObj==null) ? 0.0 : gradeBonusObj;
		double termBonus = termBonusMap.get(l.getTerm());
		double willCompeteBonus = (l.getInitialListStatus()=="W" || l.getFundedAmount() >= (l.getLoanAmount()/2.0)) ? this.willCompeteBonus : 0;
		
		double score = inqBonus+ficoBonus+usageBonus+stateBonus+gradeBonus+termBonus+willCompeteBonus;
		return score;
	}
	
	
}
