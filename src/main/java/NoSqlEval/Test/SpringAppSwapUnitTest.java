package NoSqlEval.Test;

import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import NoSqlEval.SwapTrade.SwapTrade1;



@SpringBootApplication
public class SpringAppSwapUnitTest implements CommandLineRunner{
	@Autowired
	private SwapTradeRepository repository;
	
	private static final Logger log= Logger.getLogger(SpringAppSwapUnitTest.class);
	Random random = new Random(System.currentTimeMillis() * Thread.currentThread().getId());
	
	MongoOperations mongoOperation =null;
	
	public SpringAppSwapUnitTest(){
		ApplicationContext ctx = new GenericXmlApplicationContext("SpringConfig.xml");
		mongoOperation = (MongoOperations)ctx.getBean("mongoTemplate");	
		
	}
	
	public static void main(String[] args) {
		SpringApplication.run(SpringAppSwapUnitTest.class, args);
	}
	
	public List<SwapTrade1> findTradeRegex(String fieldName,String regex){
		List<SwapTrade1> tradeLst=null;
		Query query = new Query();
		query.addCriteria(Criteria.where(fieldName).regex(regex));
		tradeLst = mongoOperation.find(query, SwapTrade1.class);
		
		
		
		return tradeLst;
	}
	
	public List<SwapTrade1> findTradeNotional(String fieldName,double amt){
		List<SwapTrade1> tradeLst=null;
		Query query = new Query();
		query.addCriteria(Criteria.where(fieldName).gt(amt).and("assets.ccy").is("JPY"));
		tradeLst = mongoOperation.find(query, SwapTrade1.class);
		
		
		
		return tradeLst;
	}
	
	public void findUpdateTrade(String fromState, String toState)throws Exception{
		UnitTestCase u = new UnitTestCase();
		List<SwapTrade1> swpLst =repository.findByTradeStatus(fromState);
		u.changeTradeStatus(swpLst, toState);
		log.info("Running update");
		repository.save(swpLst);
		log.info("finishing update");

	}
	public void insertTrade(int numTrades)throws Exception{
		
		UnitTestCase u = new UnitTestCase();
		List<SwapTrade1> swpLst = null;
		
		int maxSize = 1000;
		
		log.info("Running insertion");
		while(numTrades>0) {
			int size=numTrades;
			if(numTrades> maxSize){
				size=maxSize;
			}
			if(random.nextBoolean()){
				swpLst = u.runIRSLst(size);
			}else{
				swpLst = u.runCCSLst(size);
			}
			
			repository.save(swpLst);
			numTrades-=size;
		}
		log.info("Running insertion end");

	}

	@Override
	public void run(String... args) throws Exception {
		String Action="";
		int numTrade=0;
		for ( int i=0;i<args.length;i++){
			if(args[i].equals("-ACTION")){
				if(i<args.length-1){
					Action=args[i+1];
					i++;
					log.info("Action:"+Action);
				}else{
					throw new Exception("No action para");
				}
			}else if(args[i].equals("-NUMTRADE")){
				if(i<args.length-1){
					numTrade=Integer.parseInt(args[i+1]);
					i++;
					log.info("numTrade:"+numTrade);
				}else{
					throw new Exception("No num of trades");
				}
			}
		}
		
		if(Action.equals("INSERT")){
			insertTrade(numTrade);
		}else if(Action.equals("UPDATE")){
			findUpdateTrade("DONE","VER");
		}else if(Action.equals("REGEX")){
			List<SwapTrade1> lst=this.findTradeRegex("swapType", ".*MTM");
			for (SwapTrade1 swp : lst){
				log.debug(swp.toString());
			}
		}else if(Action.equals("NTL")){
			List<SwapTrade1> lst=this.findTradeNotional("assets.notional", 1000000);
			log.debug("Number of trades:"+lst.size());
			for (SwapTrade1 swp : lst){
				log.debug(swp.toString());
			}
		}
	}
}
