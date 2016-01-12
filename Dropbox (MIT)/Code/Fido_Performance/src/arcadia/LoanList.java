package arcadia;
import java.util.*;

/**
 * Class representing a list of Loan objects
 * @author danielzuo
 *
 */
public class LoanList {
	private List<Loan> loans;
	private boolean poison = false;

	/**
	 * Constructor for LoanList, creates from a list of Loans
	 * @param loans must be LinkedList since faster insertions and deletions, and we are not accessing elements from indices
	 */
	public LoanList(LinkedList<Loan> loans){
		this.loans = loans;
	}
	
	/**
	 * returns Loan in this list with the given loanID. If no such loan exists, returns null.
	 * @param loanID to be matched
	 * @return 
	 */
	public Loan getLoanFromID(long loanID){
		for (Loan l : loans){
			if (l.getLoanID()==loanID){
				return l;
			}
		}
		return null;
	}
	
	
	public List<Loan> getLoans() {
		return loans;
	}
	
	/**
	 * add a new loan to this LoanList, even if already exists in this LoanList
	 * @param l Loan object to be added
	 */
	public void addLoan(Loan l){
		loans.add(l);
	}
	
	/**
	 * remove an existing loan from this LoanList
	 * if loan is not previously in LoanList this does nothing
	 * @param l Loan object to be removed
	 */
	public void removeLoan(Loan l){
		loans.remove(l);
	}
	
	/**
	 * remove all loans in given LoanList from this LoanList
	 * if any loan is not previously in this LoanList this does nothing
	 * @param l Loan object to be removed
	 */
	public void removeLoanList(LoanList ll){
		for (Loan l : ll.getLoans()){
			this.removeLoan(l);
		}
	}
	
	/**
	 * 
	 * @return number of loans in this LoanList
	 */
	public int getLoanCount(){
		return loans.size();
	}
	
	/**
	 * removes all Loans which do not pass the checkLoan method of Filter instance
	 * @param f Filter instance
	 */
	public void filter(Filter f){
		ListIterator<Loan> i = loans.listIterator();
		while (i.hasNext()){
			Loan next = i.next();
			if (!f.checkLoan(next)){
				//loan was filtered out
				i.remove();
			}
		}
	}
	
	/**
	 * scores all Loans with Scorer instance and removes all which do not meet minScoreCutoff
	 * @param s Scorer instance
	 * @param minScoreCutoff minimum score required to remain in LoanList
	 */
	public void scoreAllLoans(Scorer s, double minScoreCutoff){
		ListIterator<Loan> i = loans.listIterator();
		while (i.hasNext()){
			Loan next = i.next();
			next.setScore(s.score(next));
			if (next.getScore() < minScoreCutoff){
				i.remove();
			}
		}
	}
	
	/**
	 * sorts loans in descending order
	 */
	public void sortLoans(){
		Collections.sort(loans, Collections.reverseOrder());
	}
	
	/**
	 * Merges this list with all the loans in other by appending other loans to the end of the list, 
	 * allows no duplicates and leaves other unchanged
	 * @param other other list to be merged
	 */
	
	public void merge(LoanList other){
		Set<Loan> setAll = new HashSet<Loan>(loans);
		setAll.addAll(other.getLoans());
		loans.clear();
		loans.addAll(setAll);
	}
	
	/**
	 * Sets the requested amounts for each loan in the list, for ordering purposes.
	 * List must be ordered by score, and does not request more than availableCash
	 * @return 
	 */
	public void setFractionalRequestAmounts(double availableCash, double fractionalMax){
            for (Loan l : loans){
                double reqAmount = fractionalMax * l.getLoanAmount();

                if (reqAmount<availableCash){
                        l.setReqAmount(reqAmount);
                        availableCash-=reqAmount;
                }
            }
	}
        
        public void setRequestAmounts(double availableCash){
            for (Loan l : loans){
                double reqAmount = l.getLoanAmount();

                if (reqAmount<availableCash){
                        l.setReqAmount(reqAmount);
                        availableCash-=reqAmount;
                }
            }
	}
	
	
	@Override
	public String toString(){
		StringBuilder result  = new StringBuilder();
		String NEWLINE = System.getProperty("line.separator");
		
//		result.append("\nLoanList {"+NEWLINE);
		for (Loan i : loans){
			result.append(" "+i+NEWLINE);
		}
//		result.append("}");
		
		return result.toString();
	}
	/**
	 * 
	 * @return if this LoanList is a poison pill
	 */
	public boolean isPoison(){
		return this.poison;
	}
	
	/**
	 * sets this LoanList to be a poison pill
	 */
	public void makePoisonous(){
		this.poison = true;
	}
	
	/**
	 * removes all duplicate loans from this LoanList
	 */
	public void removeDuplicates(){
		HashSet<Loan> hs = new HashSet<Loan>();
		hs.addAll(loans);
		loans.clear();
		loans.addAll(hs);
	}
	
	
	
}
