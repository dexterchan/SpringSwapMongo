package NoSqlEval.Test;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import NoSqlEval.SwapTrade.SwapTrade1;

public interface SwapTradeRepository extends MongoRepository<SwapTrade1, String> {
	
    public List<SwapTrade1> findByTradeStatus(String tradeStatus);

}
