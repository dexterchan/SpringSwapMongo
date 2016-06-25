package NoSqlEval.Test;

import java.io.FileWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import NoSqlEval.SwapTrade.AssetFlow;
import NoSqlEval.SwapTrade.AssetLeg;
import NoSqlEval.SwapTrade.SwapTrade1;
import NoSqlEval.SwapTrade.SwapTrade1.TradeStatusEnum;

public class UnitTestCase  {
	String[] CustList={"HSBCHKH","GS","MS","JP"};
	String[] bookList= {"BOOK1","BOOK2","BOOK3"};
	String[] TradeStatus= {"NEW","DONE","VER","MAT"};
	
	Random random  = new Random(System.currentTimeMillis());
	
	private String getRandomBook(){
		int num = random.nextInt(bookList.length);
		
		return bookList[num];
	}
//	private String getRandomTradeStatus(){
//		TradeStatusEnum status=TradeStatusEnum.NEW;
//		int num = random.nextInt(4);
//		//status = TradeStatusEnum.values()[num];
//		
//		return TradeStatus[num];
//	}
	private TradeStatusEnum getRandomTradeStatus(){
		TradeStatusEnum status=TradeStatusEnum.NEW;
		int num = random.nextInt(4);
		status = TradeStatusEnum.values()[num];
		
		return status;
	}
	private String getRandomCust(){
		int num = random.nextInt(CustList.length);
		
		return CustList[num]; 
	}
	private int getRandomTenor(){
		int t = random.nextInt(8);
		return 2+t;
	}
	
	
	public SwapTrade1 prepareRandomCCS(String type,double payntl, String payccy,String payindex, double paySpread,double recntl,String recccy,String recindex, double recSpread){
		double rate = 1.0;
		int tenor = this.getRandomTenor();
		SwapTrade1 swp = new SwapTrade1();
		
		
		swp.setBook(this.getRandomBook());
		swp.setLocation("LONDON");
		swp.setCustomer(this.getRandomCust());
		Calendar c = Calendar.getInstance();
		swp.setStartDate(c.getTime());
		Calendar e = (Calendar)c.clone();
		e.add(Calendar.YEAR, tenor);
		swp.setEndDate(e.getTime());
		swp.setSwapType(type);
		
		swp.setTradeStatus(getRandomTradeStatus());
		
				
		//prepare pay asset
		AssetLeg payLeg = new AssetLeg();
		payLeg.setCcy(payccy);
		payLeg.setIndex(payindex);
		payLeg.setNotional(payntl);
		payLeg.setPorr( "P");
		payLeg.setRate(0);
		payLeg.setSpread(paySpread);
		payLeg.setType("CCS");
		for(int i=0;i<2*tenor;i++){
			AssetFlow f = new AssetFlow();
			f.setCcy(payccy);
			double amt = -1*payntl *( rate +paySpread/100)/100 /2;
			f.setAmount(amt);
			Calendar st = Calendar.getInstance();
			st.setTime(swp.getStartDate());
			Calendar et = Calendar.getInstance();
			et.setTime(swp.getStartDate());
			st.add(Calendar.MONTH, 6*i);
			et.add(Calendar.MONTH, 6*(i+1));
			f.setStartDate(st.getTime());
			f.setEndDate(et.getTime());
			f.setPayEnd(true);
			payLeg.insertCashflow(f);
		}
		swp.insertAsset(payLeg);
		
		AssetLeg recLeg = new AssetLeg();
		recLeg.setCcy(recccy);
		recLeg.setIndex(recindex);
		recLeg.setNotional(recntl);
		recLeg.setPorr("R");
		recLeg.setRate(0);
		recLeg.setSpread(recSpread);
		recLeg.setType("CCS");
		for(int i=0;i<2*tenor;i++){
			AssetFlow f = new AssetFlow();
			f.setCcy(recccy);
			double amt = recntl *( rate +paySpread/100)/100 /2;
			f.setAmount(amt);
			Calendar st = Calendar.getInstance();
			st.setTime(swp.getStartDate());
			Calendar et = Calendar.getInstance();
			et.setTime(swp.getStartDate());
			st.add(Calendar.MONTH, 6*i);
			et.add(Calendar.MONTH, 6*(i+1));
			f.setStartDate(st.getTime());
			f.setEndDate(et.getTime());
			f.setPayEnd(true);
			recLeg.insertCashflow(f);
		}
		swp.insertAsset(recLeg);
		
		
		return swp;
	}
	
	
	public SwapTrade1 prepareRandomIRS(double ntl, String ccy,String index, double rate){
		SwapTrade1 swp = new SwapTrade1();
		int tenor = this.getRandomTenor();
		swp.setBook(this.getRandomBook());
		swp.setLocation("LONDON");
		swp.setCustomer(this.getRandomCust());
		Calendar c = Calendar.getInstance();
		swp.setStartDate(c.getTime());
		Calendar e = (Calendar)c.clone();
		e.add(Calendar.YEAR, tenor);
		swp.setEndDate(e.getTime());
		swp.setSwapType("IRS");
		swp.setTradeStatus(getRandomTradeStatus());
		
		
		double rr = random.nextDouble();
		boolean payFix=rr>0.5?true:false;
		
		
		//prepare Fix asset
		AssetLeg FixLeg = new AssetLeg();
		FixLeg.setCcy(ccy);
		FixLeg.setIndex("Fixed");
		FixLeg.setNotional(ntl);
		FixLeg.setPorr( payFix?"P":"R");
		FixLeg.setRate(rate);
		FixLeg.setSpread(0);
		FixLeg.setType("IRS");
		for(int i=0;i<2*tenor;i++){
			AssetFlow f = new AssetFlow();
			f.setCcy(ccy);
			double amt = ntl * rate/100 /2 * (payFix?-1:1);
			f.setAmount(amt);
			Calendar st = Calendar.getInstance();
			st.setTime(swp.getStartDate());
			Calendar et = Calendar.getInstance();
			et.setTime(swp.getStartDate());
			st.add(Calendar.MONTH, 6*i);
			et.add(Calendar.MONTH, 6*(i+1));
			f.setStartDate(st.getTime());
			f.setEndDate(et.getTime());
			f.setPayEnd(true);
			FixLeg.insertCashflow(f);
		}
		swp.insertAsset(FixLeg);
		
		AssetLeg fltLeg = new AssetLeg();
		fltLeg.setCcy(ccy);
		fltLeg.setIndex(index);
		fltLeg.setNotional(ntl);
		fltLeg.setPorr(payFix?"R":"P");
		fltLeg.setRate(0);
		fltLeg.setSpread(0);
		fltLeg.setType("IRS");
		for(int i=0;i<2*tenor;i++){
			AssetFlow f = new AssetFlow();
			f.setCcy(ccy);
			double amt = ntl * rate/100 /2 * (payFix?1:-1);
			f.setAmount(amt);
			Calendar st = Calendar.getInstance();
			st.setTime(swp.getStartDate());
			Calendar et = Calendar.getInstance();
			et.setTime(swp.getStartDate());
			st.add(Calendar.MONTH, 6*i);
			et.add(Calendar.MONTH, 6*(i+1));
			f.setStartDate(st.getTime());
			f.setEndDate(et.getTime());
			f.setPayEnd(true);
			fltLeg.insertCashflow(f);
		}
		swp.insertAsset(fltLeg);
		
		
		return swp;
	}
	
	
	
	
	public List<SwapTrade1> runIRSLst(int num) throws Exception {
		// TODO Auto-generated method stub
		
		List<SwapTrade1> lst = new LinkedList<SwapTrade1>();
		double basentl = 100000000;

		for( int i=0;i<num;i++){
			double ntl = basentl += random.nextGaussian() *  basentl;
			SwapTrade1 swp = prepareRandomIRS(ntl, "USD", "LIBOR", 0.1);
	
			lst.add(swp);
		}
		return lst;
	}
	public List<SwapTrade1> runCCSLst(int num) throws Exception {
		// TODO Auto-generated method stub
		
		List<SwapTrade1> lst = new LinkedList<SwapTrade1>();
		double basentl = 100000000;

		for( int i=0;i<num;i++){
			double direction=random.nextDouble();
			double usdntl = basentl;
			double fxrate = 100 + random.nextGaussian()*10;
			double jpyntl=usdntl * fxrate;
			SwapTrade1 swp=null;
			if(direction<0.5){
				swp = this.prepareRandomCCS("CCSMTM", usdntl, "USD", "LIBOR", 0, jpyntl, "JPY", "LIBOR", 0.1);
			}else{
				swp = this.prepareRandomCCS("CCSMTM", jpyntl, "JPY", "LIBOR", 0.1, usdntl, "USD", "LIBOR", 0);
			}
	
			lst.add(swp);
		}

		return lst;
		
	}
	public void changeTradeStatus(List<SwapTrade1> lst, String toState){
		TradeStatusEnum e=TradeStatusEnum.NEW;
		if(toState.equals("NEW")){
			e=TradeStatusEnum.NEW;
		}else if(toState.equals("DONE")){
			e=TradeStatusEnum.DONE;
		}else if(toState.equals("VER")){
			e=TradeStatusEnum.VER;
		}else if(toState.equals("MAT")){
			e=TradeStatusEnum.MAT;
		}
		for(SwapTrade1 swp : lst){
			swp.setTradeStatus(e);
		}
	}
	
}
