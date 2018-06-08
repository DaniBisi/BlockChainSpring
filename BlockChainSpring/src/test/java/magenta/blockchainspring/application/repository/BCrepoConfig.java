package magenta.blockchainspring.application.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import magenta.blockchainspring.application.service.blockchainrepo.DbManager;
import magenta.blockchainspring.application.service.blockchainrepo.GetBlockChainRepository;

@Configuration
public class BCrepoConfig {
	@Bean
	public DbManager db() {
		DbManager db = new DbManager();
		return db;
	}

	@Bean
	public GetBlockChainRepository gBCRepository() {
		GetBlockChainRepository gBCRepository = new GetBlockChainRepository();
		return gBCRepository;
	}
}
