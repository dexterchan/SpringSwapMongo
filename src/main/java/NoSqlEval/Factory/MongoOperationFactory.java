package NoSqlEval.Factory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;

public class MongoOperationFactory {
	static MongoOperations mongoOperation =null;
	
	public static MongoOperations getMongoOperation(){
		if(mongoOperation==null){
			//Double check idiom here
			synchronized  (MongoOperationFactory.class){
				if(mongoOperation==null){
					ApplicationContext ctx = new GenericXmlApplicationContext("SpringConfig.xml");
					mongoOperation = (MongoOperations)ctx.getBean("mongoTemplate");	
				}
			}
		}
		return mongoOperation;
	}
}
