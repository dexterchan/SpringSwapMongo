package NoSqlEval.Test;

import NoSqlEval.Utility.TradeIdCounter;

public class TestTradeId {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String TradeId="";
		
		TradeId = TradeIdCounter.getTradeId("LONDON","SWAP");
		
		System.out.println(TradeId);
	}

}
