package NoSqlEval.Utility;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import NoSqlEval.Factory.MongoOperationFactory;

public class TradeIdCounter  {
	@Id
	private String id;
	private int sequenceId;
	int initialValue;
	int length;
	int increment;
	String defaultSuffix;
	String defaultPrefix;
	String prefix;
	
	Map<String,String> suffixMap;
	@Transient
	Map<String,String> prefixMap;
	private static final Logger log= Logger.getLogger(TradeIdCounter.class);
	private static TradeIdCounter counter = null;
		
	public TradeIdCounter(){
	}
	
//	public static TradeIdCounter getTradeIdCounter(String tradetype){
//		TradeIdCounter counter = null;
//		MongoOperations mongoOperation = MongoOperationFactory.getMongoOperation();
//		Query query = new Query();
//		query.addCriteria(Criteria.where("id").is(tradetype));
//		counter = mongoOperation.findOne(query, TradeIdCounter.class);
//		
//		if(counter==null){
//			//Create a counter
//			log.info("Counter:"+tradetype+" not found");
//			log.info("Create a new counter with "+tradetype);
//			synchronized (TradeIdCounter.class){
//				ApplicationContext ctx = new GenericXmlApplicationContext("SpringConfig.xml");
//				counter = (TradeIdCounter)ctx.getBean("TradeIdCounterTemplate");	
//				counter.initializeCounter(tradetype);
//				mongoOperation.save(counter);
//			}
//		}
//		return counter;
//	}
	
	public void initializeCounter(ApplicationContext ctx, String tradeType){
		this.setId(tradeType);
		this.sequenceId=this.initialValue;
		
		this.prefixMap = (Map)ctx.getBean("PrefixMap");
		this.prefix = this.prefixMap.get(tradeType);
		if(this.prefix==null){
			prefix=this.defaultPrefix;
		}
	}
	

	protected String getTradeId(String location){
		String myId="";
		String format = String.format("%%0%dd", this.length-3);
		String suffix = this.suffixMap.get(location);
		if(suffix==null){
			suffix=this.defaultSuffix;
		}
		//Atomic action here
		myId = this.prefix+String.format(format, this.sequenceId)+suffix;
		return myId;
	}
	
	public static String getTradeId(String loc,String tradetype){
		String NextTradeId="";
		
		
		MongoOperations mongoOperation = MongoOperationFactory.getMongoOperation();
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(tradetype));
		if(counter==null){
			counter = mongoOperation.findOne(query, TradeIdCounter.class);
			synchronized (TradeIdCounter.class){
				// Lazy initialization here
				if (counter == null) {
					// Create a counter
					log.info("Counter:" + tradetype + " not found");
					log.info("Create a new counter with " + tradetype);

					ApplicationContext ctx = new GenericXmlApplicationContext("SpringConfig.xml");
					counter = (TradeIdCounter) ctx.getBean("TradeIdCounterTemplate");
					counter.initializeCounter(ctx, tradetype);
					mongoOperation.save(counter);
				}
			}
		}
		
		Update update = new Update();
		update.inc("sequenceId", counter.increment);
		FindAndModifyOptions opt = new FindAndModifyOptions();
		opt.returnNew(true);
		TradeIdCounter newcounter = mongoOperation.findAndModify(query, update,opt, TradeIdCounter.class);
		
		NextTradeId = newcounter.getTradeId(loc);
		
		return NextTradeId;
	}
	
	public int getSequenceId() {
		return sequenceId;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setInitialValue(int initialValue) {
		this.initialValue = initialValue;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public void setIncrement(int increment) {
		this.increment = increment;
	}
	public void setDefaultSuffix(String defaultSuffix) {
		this.defaultSuffix = defaultSuffix;
	}
	public String getDefaultSuffix() {
		return defaultSuffix;
	}
	
	public int getInitialValue() {
		return initialValue;
	}
	
	public int getLength() {
		return length;
	}
	
	public int getIncrement() {
		return increment;
	}
	
	public String getId() {
		return id;
	}

	public void setSequenceId(int sequenceId) {
		this.sequenceId = sequenceId;
	}
	
	
	public Map<String, String> getPrefixMap() {
		return prefixMap;
	}

	public void setPrefixMap(Map<String, String> prefixMap) {
		this.prefixMap = prefixMap;
	}

	public String getDefaultPrefix() {
		return defaultPrefix;
	}

	public void setDefaultPrefix(String defaultPrefix) {
		this.defaultPrefix = defaultPrefix;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	
	public Map<String, String> getSuffixMap() {
		return suffixMap;
	}

	public void setSuffixMap(Map<String, String> suffixMap) {
		this.suffixMap = suffixMap;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.id+","+this.sequenceId;
	}
	
	
	
}
