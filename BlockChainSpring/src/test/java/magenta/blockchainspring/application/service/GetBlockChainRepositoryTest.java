package magenta.blockchainspring.application.service;
//package magenta.BlockChainSpring.Application.Service;
//
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import com.fasterxml.jackson.databind.Module.SetupContext;
//
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class GetBlockChainRepositoryTest {
//
//	/**/
//	@Autowired
//	GetBlockChainRepository repository;	
//	
//	
//	@Before
//	public void Setup() {
//		repository = mock(GetBlockChainRepository.class);
//		when(repository.bazza()).thenReturn("banzai2");
//	}
//	
//	@Test
//	public void test() {
//		Assert.assertEquals("banzai2", repository.bazza() );
//	}		
//	/**/
//	
//	/**/
//	//@MockBean
//	
//	
//	@Test
//	public void testMockabilty() {
//		GetBlockChainRepository mockedRepository = mock(GetBlockChainRepository.class);
//		when(mockedRepository.bazza()).thenReturn("bazza2");
//		Assert.assertEquals("bazza2", mockedRepository.bazza() );
//	}
//	/**/
//}
