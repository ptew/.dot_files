package arcadia;

import net.minidev.json.JSONObject;

public class OrderConfirmation {
	final private long loanID, orderInstructID;
	final private String executionStatus;
	final private double requestedAmount, investedAmount;
	
	
	/**
	 * Constructs an OrderConfirmation object from a JSONObject obj with all the fields and the correct orderInstructID
	 * @param obj
	 */
	public OrderConfirmation(JSONObject obj, long orderInstructID) {
		this.loanID = Long.valueOf((int) obj.get("loanId"));
		this.orderInstructID = orderInstructID;
		this.executionStatus = obj.get("executionStatus").toString();
		this.requestedAmount = (double) obj.get("requestedAmount");
		this.investedAmount = (double) obj.get("investedAmount");
	}


	public long getLoanID() {
		return loanID;
	}


	public long getOrderInstructID() {
		return orderInstructID;
	}


	public String getExecutionStatus() {
		return executionStatus;
	}


	public double getRequestedAmount() {
		return requestedAmount;
	}


	public double getInvestedAmount() {
		return investedAmount;
	}
	
	@Override
	public String toString(){
		StringBuilder result = new StringBuilder();
//		String NEWLINE = System.getProperty("line.separator");
          String NEWLINE = "\t";  //use tabs instead of newlines
//		result.append("OrderConfirmation Object { "+NEWLINE);
		result.append("  OrderInstructID: "+orderInstructID+NEWLINE);
		result.append("  ID: "+loanID+NEWLINE);
		result.append("     "+this.executionStatus+NEWLINE);
		result.append("     RequestedAmount: "+this.requestedAmount+NEWLINE);
		result.append("     InvestedAmount: "+this.investedAmount+NEWLINE);
//		result.append(" }");
		
		return result.toString();
	}
	
}
