package NoSqlEval.SwapTrade;

import java.util.Date;

public class AssetFlow {
	Date startDate;
	Date endDate;
	boolean payEnd;
	String ccy;
	double amount;
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public boolean isPayEnd() {
		return payEnd;
	}
	public void setPayEnd(boolean payEnd) {
		this.payEnd = payEnd;
	}
	public String getCcy() {
		return ccy;
	}
	public void setCcy(String ccy) {
		this.ccy = ccy;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
}
