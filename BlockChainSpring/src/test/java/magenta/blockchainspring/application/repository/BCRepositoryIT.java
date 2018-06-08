package magenta.blockchainspring.application.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.BasicConfigurator;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import magenta.blockchainspring.application.model.AppUser;
import magenta.blockchainspring.application.service.blockchainrepo.DbManager;

public class BCRepositoryIT {
	protected DbManager db;
	protected BCRepository bcRepo;
	private CryptoSuite cryptoSuite;
	private HFCAClient caClient;
	private HFClient client;

	public BCRepositoryIT() {
		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure();
		db = new DbManager();
	}

	@Before
	public void setup() throws InvalidArgumentException, TransactionException, IllegalAccessException,
			InstantiationException, ClassNotFoundException, CryptoException, NoSuchMethodException,
			InvocationTargetException, MalformedURLException {
		cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
		caClient = HFCAClient.createNewInstance(db.getCaAddress(), null);
		caClient.setCryptoSuite(cryptoSuite);
		client = HFClient.createNewInstance();
		client.setCryptoSuite(cryptoSuite);

	}

	@After
	public void afterClass() {
		bcRepo = null;
	}

	@Test
	public void testLoginCorrect() throws Exception {
		bcRepo = new BCRepository(caClient, client, new AppUser("admin", db.getAgency(), db.getOrganization()),
				ChaincodeID.newBuilder().setName("employVisit").build());
		assertEquals(true, bcRepo.login("adminpw"));
	}

	@Test
	public void testLoginUnCorrect() throws Exception {
		String psswd = "Ciao";
		bcRepo = new BCRepository(caClient, client, new AppUser("admin", db.getAgency(), db.getOrganization()),
				ChaincodeID.newBuilder().setName("employVisit").build());
		assertEquals(false, bcRepo.login(psswd));
	}

	@Test
	public void testAddUserRole() {
		AppUser userLogged = new AppUser("admin", db.getAgency(), db.getOrganization());
		AppUser spyUserLogged = Mockito.spy(userLogged);
		bcRepo = new BCRepository(caClient, client, spyUserLogged,
				ChaincodeID.newBuilder().setName("employVisit").build());
		bcRepo.login("adminpw");
		bcRepo.addUserRole("client");
		verify(spyUserLogged, times(1)).addRoles(Mockito.anyString());
	}

	//
	@Test
	public void testQueryDbProposalVerifiedOneArguments() throws InvalidArgumentException, TransactionException,
			ProposalException, InterruptedException, ExecutionException, TimeoutException {
		initBcRepo(true, true);
		String[] args = { "queryAllVisits" };
		assertEquals(
				"[{\"Key\":\"0\", \"Record\":{\"IDhash\":\"6f83a90e58a43520a3f7214328091be808ab9177a4dfad9dcc3107ea81db607d\",\"agency\":\"Magenta3.76\",\"date\":\"2018-04-10\",\"name\":\"Admin\",\"time\":\"09:49\"}}{\"Key\":\"1\", \"Record\":{\"IDhash\":\"cc3b2febe0c83c141c5405daece2f3db67decf4c66c43c9669913996c3c32743\",\"agency\":\"Unifi\",\"date\":\"2018-04-10\",\"name\":\"User2\",\"time\":\"09:48\"}}{\"Key\":\"10\", \"Record\":{\"IDhash\":\"524d9a207e81aff81d050e7ec134642a1ff13f39635542148729dddbc79cd68e\",\"agency\":\"Magenta\",\"date\":\"2018-04-10\",\"name\":\"Admin\",\"time\":\"09:39\"}}{\"Key\":\"11\", \"Record\":{\"IDhash\":\"95763f91f5fc8e956f7565476bb6b77af6bfb870288901f3c18e4d7ad08d606f\",\"agency\":\"Magenta\",\"date\":\"2018-04-10\",\"name\":\"Admin\",\"time\":\"09:38\"}}{\"Key\":\"2\", \"Record\":{\"IDhash\":\"9814c045246960a84f1abdf51b598a09222a2a1f34d8ad911206b8c40bea97d8\",\"agency\":\"Unifi\",\"date\":\"2018-04-10\",\"name\":\"User1\",\"time\":\"09:47\"}}{\"Key\":\"3\", \"Record\":{\"IDhash\":\"d49bd7a0d468b41f83df7340c1ae39a9f6cd2f667a890f39d4ce22ad6da0d3fb\",\"agency\":\"Magenta\",\"date\":\"2018-04-10\",\"name\":\"Admin\",\"time\":\"09:46\"}}{\"Key\":\"4\", \"Record\":{\"IDhash\":\"68581372af425b3243bfde322cff5bb81c2c1d873449f77e9cdeea8c5be113b4\",\"agency\":\"Magenta\",\"date\":\"2018-04-10\",\"name\":\"Admin\",\"time\":\"09:45\"}}{\"Key\":\"5\", \"Record\":{\"IDhash\":\"9fd2ed5905373e8d6d36c1da69fba36aca1939dfe7aa90efb5cba4c8f54f2b5a\",\"agency\":\"Unifi\",\"date\":\"2018-04-10\",\"name\":\"User2\",\"time\":\"09:44\"}}{\"Key\":\"6\", \"Record\":{\"IDhash\":\"cf3409808cba562eecaf899db498e35ac374ffd5c6e37483facbe49248d750e4\",\"agency\":\"Unifi\",\"date\":\"2018-04-10\",\"name\":\"User1\",\"time\":\"09:43\"}}{\"Key\":\"7\", \"Record\":{\"IDhash\":\"3d3f05ece8f1f5378bc7d9bf0220c26f7974f3ca7fb180eae8c0f17497ecf04f\",\"agency\":\"Magenta\",\"date\":\"2018-04-10\",\"name\":\"Admin\",\"time\":\"09:42\"}}{\"Key\":\"8\", \"Record\":{\"IDhash\":\"ff323d55115a0eabee90e28c6c918316459f724123be089a70f969ce6404cf2a\",\"agency\":\"Unifi\",\"date\":\"2018-04-10\",\"name\":\"User2\",\"time\":\"09:41\"}}{\"Key\":\"9\", \"Record\":{\"IDhash\":\"80084bf2fba02475726feb2cab2d8215eab14bc6bdd8bfb2c8151257032ecd8b\",\"agency\":\"Magenta\",\"date\":\"2018-04-10\",\"name\":\"Admin\",\"time\":\"09:40\"}}]",
				bcRepo.queryDB(args));
	}

	private void initBcRepo(boolean login, boolean initChannel) throws InvalidArgumentException, TransactionException {
		bcRepo = new BCRepository(caClient, client, new AppUser("admin", db.getAgency(), db.getOrganization()),
				ChaincodeID.newBuilder().setName("employVisit").build());
		if (login) {
			bcRepo.login("adminpw");
		}
		if (initChannel) {
			bcRepo.setEventList(db.getEventList());
			bcRepo.setOrdererList(db.getOrderList());
			bcRepo.setPeerList(db.getPeerList());
			bcRepo.initChannel("mychannel");
		}
	}

	@Test
	public void testQueryDbProposalVerifiedMoreArguments() throws InvalidArgumentException, ProposalException,
			InterruptedException, ExecutionException, TimeoutException, TransactionException {
		initBcRepo(true, true);
		String[] args = { "queryAllVisits", "1" };
		assertEquals(
				"[{\"Key\":\"1\", \"Record\":{\"IDhash\":\"cc3b2febe0c83c141c5405daece2f3db67decf4c66c43c9669913996c3c32743\",\"agency\":\"Unifi\",\"date\":\"2018-04-10\",\"name\":\"User2\",\"time\":\"09:48\"}}{\"Key\":\"10\", \"Record\":{\"IDhash\":\"524d9a207e81aff81d050e7ec134642a1ff13f39635542148729dddbc79cd68e\",\"agency\":\"Magenta\",\"date\":\"2018-04-10\",\"name\":\"Admin\",\"time\":\"09:39\"}}{\"Key\":\"11\", \"Record\":{\"IDhash\":\"95763f91f5fc8e956f7565476bb6b77af6bfb870288901f3c18e4d7ad08d606f\",\"agency\":\"Magenta\",\"date\":\"2018-04-10\",\"name\":\"Admin\",\"time\":\"09:38\"}}]",
				bcRepo.queryDB(args));
	}

	@Test
	public void testQueryLastVisits() throws InvalidArgumentException, ProposalException, InterruptedException,
			ExecutionException, TimeoutException, TransactionException {
		initBcRepo(true, true);
		String[] args = { "lastInsertedId" };
		assertEquals(
				"[{\"Key\":\"11\", \"Record\":{\"IDhash\":\"95763f91f5fc8e956f7565476bb6b77af6bfb870288901f3c18e4d7ad08d606f\",\"agency\":\"Magenta\",\"date\":\"2018-04-10\",\"name\":\"Admin\",\"time\":\"09:38\"}}]",
				bcRepo.queryDB(args));
	}

	@Test
	public void testQueryBlock() throws InvalidArgumentException, TransactionException, ProposalException {
		initBcRepo(true, true);
		String[] response = bcRepo.queryBlock();
		assertNotNull(response);
	}
}