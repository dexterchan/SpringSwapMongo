package NoSqlEval.SwapTrade;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.data.annotation.Id;

public class SwapTrade1 {
	
	public enum TradeStatusEnum{NEW,DONE,VER,MAT};
	@Id
	private String id;

	private String tradeId;
	

	String location;
	String book;
	String customer;
	TradeStatusEnum tradeStatus;
	
	String swapType;
	Date startDate;
	Date endDate;
	
	List<AssetLeg> assets;
	
	public SwapTrade1(){
		assets = new LinkedList<AssetLeg>();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public void insertAsset(AssetLeg f){
		assets.add(f);
	}
	
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getBook() {
		return book;
	}

	public void setBook(String book) {
		this.book = book;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	

	public TradeStatusEnum getTradeStatus() {
		return tradeStatus;
	}


	public void setTradeStatus(TradeStatusEnum tradeStatus) {
		this.tradeStatus = tradeStatus;
	}


	public String getSwapType() {
		return swapType;
	}

	public void setSwapType(String swapType) {
		this.swapType = swapType;
	}

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

	public List<AssetLeg> getAssets() {
		return assets;
	}

	public void setAssets(List<AssetLeg> assets) {
		this.assets = assets;
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
		String str=id+","+this.tradeStatus+","+this.swapType+","+this.customer+","+this.book+","+sf.format(this.startDate)
					+","+sf.format(this.endDate);
		return str;
	}

	
	
	
	
}
