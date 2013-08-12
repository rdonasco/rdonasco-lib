package com.rdonasco.security.services;

import com.rdonasco.config.dao.ConfigElementDAO;
import com.rdonasco.config.data.ConfigElement;
import com.rdonasco.config.parsers.ValueParser;
import com.rdonasco.config.services.ConfigDataManagerLocal;
import com.rdonasco.config.util.ConfigDataValueObjectConverter;
import com.rdonasco.config.vo.ConfigAttributeVO;
import com.rdonasco.security.dao.ActionDAO;
import com.rdonasco.security.exceptions.ApplicationNotTrustedException;
import com.rdonasco.security.exceptions.DefaultAdminSecurityProfileAlreadyExist;
import com.rdonasco.security.exceptions.SecurityAuthenticationException;
import com.rdonasco.security.exceptions.SecurityAuthorizationException;
import com.rdonasco.security.exceptions.SecurityProfileNotFoundException;
import com.rdonasco.security.model.Action;
import com.rdonasco.security.vo.AccessRightsVO;
import com.rdonasco.security.vo.AccessRightsVOBuilder;
import com.rdonasco.security.vo.ActionVO;
import com.rdonasco.security.vo.CapabilityActionVO;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.UserCapabilityVO;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import javax.ejb.EJB;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.rdonasco.security.utils.ArchiveCreator;
import com.rdonasco.security.utils.EncryptionUtil;
import com.rdonasco.security.utils.SecurityConstants;
import com.rdonasco.security.utils.UserSecurityProfileTestUtility;
import com.rdonasco.security.vo.ApplicationVO;
import com.rdonasco.security.vo.UserSecurityProfileVOBuilder;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.mockito.Mockito.*;

/**
 * Unit test for simple App.
 */
@RunWith(Arquillian.class)
public class SystemSecurityManagerLocalTest
{

	private static final Logger LOG = Logger.getLogger(SystemSecurityManagerLocalTest.class.getName());
	private static final String CONSTANT_ADMIN = "admin";
	@EJB
	private SystemSecurityManagerLocal systemSecurityManager;
	@EJB
	private CapabilityManagerLocal capabilityManager;
	@EJB
	private SystemSecurityInitializerLocal systemSecurityInitializerLocal;
	@EJB
	private ApplicationManagerLocal applicationManager;
	@EJB
	private ConfigDataManagerLocal configDataManager;
	private UserSecurityProfileTestUtility userSecurityProfileTestUtility;

	@Deployment
	public static JavaArchive createTestArchive()
	{
		JavaArchive archive = ArchiveCreator.createCommonArchive()
				.addPackage(ConfigElementDAO.class.getPackage())
				.addPackage(ValueParser.class.getPackage())
				.addPackage(ConfigElement.class.getPackage())
				.addPackage(ConfigDataManagerLocal.class.getPackage())
				.addPackage(ConfigAttributeVO.class.getPackage())
				.addPackage(ConfigDataValueObjectConverter.class.getPackage())
				.addPackage(ActionDAO.class.getPackage())
				.addPackage(SystemSecurityManagerLocal.class.getPackage())
				.addPackage(ActionVO.class.getPackage())
				.addPackage(Action.class.getPackage());

		return archive;
	}

	@Before
	public void setUp()
	{
		userSecurityProfileTestUtility = new UserSecurityProfileTestUtility(capabilityManager, systemSecurityManager, applicationManager);
	}

	@Test
	public void testCreateDefaultAdminSecurityProfile() throws Exception
	{
		System.out.println("CreateDefaultAdminSecurityProfile");
		SystemSecurityManagerImpl systemSecurityManager = new SystemSecurityManagerImpl();
		final UserSecurityProfileManagerLocal userSecurityProfileManagerMock = mock(UserSecurityProfileManagerLocal.class);
		systemSecurityManager.setUserSecurityProfileManager(userSecurityProfileManagerMock);
		String password = EncryptionUtil.encryptWithPassword(CONSTANT_ADMIN, CONSTANT_ADMIN);
		when(userSecurityProfileManagerMock.findAllProfiles()).thenReturn(new ArrayList<UserSecurityProfileVO>());
		when(userSecurityProfileManagerMock.createNewUserSecurityProfile(any(UserSecurityProfileVO.class))).thenReturn(new UserSecurityProfileVOBuilder()
				.setId(Long.MIN_VALUE)
				.setLoginId(CONSTANT_ADMIN)
				.setPassword(password)
				.createUserSecurityProfileVO());
		UserSecurityProfileVO newAdminProfile = systemSecurityManager.createDefaultAdminSecurityProfile();
		assertNotNull("null admin profile", newAdminProfile);
		assertEquals("wrong admin id", CONSTANT_ADMIN, newAdminProfile.getLogonId());
		assertEquals("wrong admin password", password, newAdminProfile.getPassword());
	}

	@Test(expected = DefaultAdminSecurityProfileAlreadyExist.class)
	public void testCreateDefaultAdminSecurityProfileWhenItAlreadyExist() throws
			Exception
	{
		System.out.println("CreateDefaultAdminSecurityProfileWhenItAlreadyExist");
		SystemSecurityManagerImpl systemSecurityManager = new SystemSecurityManagerImpl();
		final UserSecurityProfileManagerLocal userSecurityProfileManagerMock = mock(UserSecurityProfileManagerLocal.class);
		systemSecurityManager.setUserSecurityProfileManager(userSecurityProfileManagerMock);
		String password = EncryptionUtil.encryptWithPassword(CONSTANT_ADMIN, CONSTANT_ADMIN);
		final ArrayList<UserSecurityProfileVO> userProfileList = new ArrayList<UserSecurityProfileVO>();
		final UserSecurityProfileVO adminUserProfileVO = new UserSecurityProfileVOBuilder()
				.setId(Long.MIN_VALUE)
				.setLoginId(CONSTANT_ADMIN)
				.setPassword(password)
				.createUserSecurityProfileVO();
		userProfileList.add(adminUserProfileVO);
		when(userSecurityProfileManagerMock.findAllProfiles()).thenReturn(userProfileList);
		systemSecurityManager.createDefaultAdminSecurityProfile();
	}

	@Test
	public void testCreateSecurityProfileWithoutCapability() throws Exception
	{
		System.out.println("createSecurityProfileWithoutCapability");

		UserSecurityProfileVO createdUser = userSecurityProfileTestUtility.createNewUserSecurityProfile();
		assertNotNull(createdUser);

	}

	@Test
	public void testCreateSecurityProfileWithCapability() throws Exception
	{
		System.out.println("createSecurityProfileWithCapability");

		UserSecurityProfileVO createdUser = userSecurityProfileTestUtility.createNewUserSecurityProfileWithCapability();

		assertNotNull("user not created", createdUser);
		assertNotNull("id is null", createdUser.getId());
		assertNotNull("capabilities is not null", createdUser.getCapabilities());
		assertTrue("capabilities.size() is zero", createdUser.getCapabilities().size() > 0);
		for (UserCapabilityVO savedUserCapabilityVO : createdUser.getCapabilities())
		{
			assertNotNull(savedUserCapabilityVO.getId());
			System.out.println("savedUserCapabilityVO.toString()=" + savedUserCapabilityVO.toString());
		}
	}

	@Test
	public void testAddCapability() throws Exception
	{
		System.out.println("addCapability");
		UserSecurityProfileVO createdUser = userSecurityProfileTestUtility.createNewUserSecurityProfileWithCapability();
		CapabilityVO additionalCapability = userSecurityProfileTestUtility.createTestDataCapabilityWithActionAndResourceName("fire", "employee");
		systemSecurityManager.addCapabilityForUser(createdUser, additionalCapability);
		String actionName = null;
		for (CapabilityActionVO action : additionalCapability.getActions())
		{
			actionName = action.getActionVO().getName();
		}
		AccessRightsVO accessRights = new AccessRightsVOBuilder()
				.setActionAsString(actionName)
				.setResourceAsString(additionalCapability.getResource().getName())
				.setApplicationID(additionalCapability.getApplicationVO().getId())
				.setApplicationToken(additionalCapability.getApplicationVO().getToken())
				.setUserProfileVO(createdUser)
				.createAccessRightsVO();
		systemSecurityManager.checkAccessRights(accessRights);
	}
	
	@Test(expected = SecurityAuthorizationException.class)
	public void testCheckInvalidApplication() throws Throwable
	{
		System.out.println("CheckInvalidApplication");
		UserSecurityProfileVO createdUser = userSecurityProfileTestUtility.createNewUserSecurityProfileWithCapability();
		CapabilityVO additionalCapability = userSecurityProfileTestUtility.createTestDataCapabilityWithActionAndResourceName("fire", "employee");
		ApplicationVO applicationVO = userSecurityProfileTestUtility.createApplicationNamed("invalid application");
		systemSecurityManager.addCapabilityForUser(createdUser, additionalCapability);
		String actionName = null;
		for (CapabilityActionVO action : additionalCapability.getActions())
		{
			actionName = action.getActionVO().getName();
		}
		AccessRightsVO accessRights = new AccessRightsVOBuilder()
				.setActionAsString(actionName)
				.setResourceAsString(additionalCapability.getResource().getName())
				.setApplicationID(applicationVO.getId())
				.setApplicationToken(applicationVO.getToken())
				.setUserProfileVO(createdUser)
				.createAccessRightsVO();
		checkAccessAndThrowRealCause(accessRights);
	}	

	@Test(expected = SecurityAuthorizationException.class)
	public void testCheckUnAuthorisedAccess() throws Throwable
	{
		System.out.println("CheckUnAuthorisedAccess");
		UserSecurityProfileVO createdUser = userSecurityProfileTestUtility.createNewUserSecurityProfileWithCapability();
		CapabilityVO additionalCapability = userSecurityProfileTestUtility.createTestDataCapabilityWithActionAndResourceName("fire", "employee");
		systemSecurityManager.addCapabilityForUser(createdUser, additionalCapability);
		String actionName = "doit";
		AccessRightsVO accessRights = new AccessRightsVOBuilder()
				.setActionAsString(actionName)
				.setResourceAsString(additionalCapability.getResource().getName())
				.setUserProfileVO(createdUser)
				.setApplicationID(additionalCapability.getApplicationVO().getId())
				.setApplicationToken(additionalCapability.getApplicationVO().getToken())				
				.createAccessRightsVO();
		checkAccessAndThrowRealCause(accessRights);
	}

	@Test(expected = ApplicationNotTrustedException.class)
	public void testCheckAccessRightsWithoutApplicationID() throws Throwable
	{
		System.out.println("CheckAccessRightsWithoutApplicationID");
		UserSecurityProfileVO createdUser = userSecurityProfileTestUtility.createNewUserSecurityProfileWithCapability();
		CapabilityVO additionalCapability = userSecurityProfileTestUtility.createTestDataCapabilityWithActionAndResourceName("fire", "employee");
		systemSecurityManager.addCapabilityForUser(createdUser, additionalCapability);
		String actionName = "doit";
		AccessRightsVO accessRights = new AccessRightsVOBuilder()
				.setActionAsString(actionName)
				.setResourceAsString(additionalCapability.getResource().getName())
				.setUserProfileVO(createdUser)
				.createAccessRightsVO();
		checkAccessAndThrowRealCause(accessRights);
	}
	
	@Test(expected = ApplicationNotTrustedException.class)
	public void testCheckAccessRightsWithoutApplicationToken() throws Throwable
	{
		System.out.println("CheckAccessRightsWithoutApplicationToken");
		UserSecurityProfileVO createdUser = userSecurityProfileTestUtility.createNewUserSecurityProfileWithCapability();
		CapabilityVO additionalCapability = userSecurityProfileTestUtility.createTestDataCapabilityWithActionAndResourceName("fire", "employee");
		systemSecurityManager.addCapabilityForUser(createdUser, additionalCapability);
		String actionName = "doit";
		AccessRightsVO accessRights = new AccessRightsVOBuilder()
				.setActionAsString(actionName)
				.setResourceAsString(additionalCapability.getResource().getName())
				.setUserProfileVO(createdUser)
				.setApplicationID(additionalCapability.getApplicationVO().getId())
				.createAccessRightsVO();
		checkAccessAndThrowRealCause(accessRights);
	}	
	
	@Test(expected = ApplicationNotTrustedException.class)
	public void testCheckAccessRightsWithMismatchedToken() throws Throwable
	{
		System.out.println("CheckAccessRightsWithMismatchedToken");
		UserSecurityProfileVO createdUser = userSecurityProfileTestUtility.createNewUserSecurityProfileWithCapability();
		CapabilityVO additionalCapability = userSecurityProfileTestUtility.createTestDataCapabilityWithActionAndResourceName("fire", "employee");
		systemSecurityManager.addCapabilityForUser(createdUser, additionalCapability);
		String actionName = "doit";
		AccessRightsVO accessRights = new AccessRightsVOBuilder()
				.setActionAsString(actionName)
				.setResourceAsString(additionalCapability.getResource().getName())
				.setUserProfileVO(createdUser)
				.setApplicationID(additionalCapability.getApplicationVO().getId())
				.setApplicationToken("bla bla bla")
				.createAccessRightsVO();
		checkAccessAndThrowRealCause(accessRights);
	}		

	@Test
	public void testCheckAccessRightsOnNonRestrictedResourceToCreateDefaultCapability()
			throws Exception
	{
		System.out.println("CheckAccessRightsOnNonRestrictedResourceToCreateDefaultCapability");
		UserSecurityProfileVO createdUser = userSecurityProfileTestUtility.createNewUserSecurityProfileWithCapability();
		ApplicationVO nonRestrictedApp = userSecurityProfileTestUtility.createApplicationNamed("non-restricted-app");
		String actionName = "do";
		String resourceName = "her";
		String capabilityTitle = "do her";
		AccessRightsVO accessRights = new AccessRightsVOBuilder()
				.setActionAsString(actionName)
				.setResourceAsString(resourceName)
				.setUserProfileVO(createdUser)
				.setApplicationID(nonRestrictedApp.getId())
				.setApplicationToken(nonRestrictedApp.getToken())
				.createAccessRightsVO();

		systemSecurityManager.checkAccessRights(accessRights);
		CapabilityVO capabilityVO = capabilityManager.findCapabilityWithTitle(capabilityTitle);
		assertNotNull(capabilityVO);
		assertEquals(capabilityTitle, capabilityVO.getTitle());

	}

	@Test
	public void testCheckAccessRightsOnNonRestrictedResourceToCreateDefaultCapabilityWithoutDuplication()
			throws Exception
	{
		System.out.println("CheckAccessRightsOnNonRestrictedResourceToCreateDefaultCapabilityWithoutDuplication");
		UserSecurityProfileVO createdUser = userSecurityProfileTestUtility.createNewUserSecurityProfileWithCapability();
		String actionName = "enter";
		String resourceName = "the dragon";
		String capabilityTitle = String.format("%1s %2s", actionName, resourceName);
		ApplicationVO nonRestrictedApp = userSecurityProfileTestUtility.createApplicationNamed("non-restricted-app-resource");
		AccessRightsVO accessRights = new AccessRightsVOBuilder()
				.setActionAsString(actionName)
				.setResourceAsString(resourceName)
				.setUserProfileVO(createdUser)
				.setApplicationID(nonRestrictedApp.getId())
				.setApplicationToken(nonRestrictedApp.getToken())				
				.createAccessRightsVO();

		systemSecurityManager.checkAccessRights(accessRights);
		systemSecurityManager.checkAccessRights(accessRights);
		systemSecurityManager.checkAccessRights(accessRights);
		CapabilityVO capabilityVO = capabilityManager.findCapabilityWithTitle(capabilityTitle);
		assertNotNull(capabilityVO);
		assertEquals(capabilityTitle, capabilityVO.getTitle());

	}

	@Test(expected = SecurityProfileNotFoundException.class)
	public void testRemoveSecurityProfile() throws Exception
	{
		System.out.println("removeSecurityProfile");
		UserSecurityProfileVO createdUser = userSecurityProfileTestUtility.createNewUserSecurityProfileWithCapability();
		systemSecurityManager.removeSecurityProfile(createdUser);
		try
		{
			systemSecurityManager.findSecurityProfileWithLogonID(createdUser.getLogonId());
		}
		catch (Exception e)
		{
			LOG.log(Level.SEVERE, e.getMessage(), e);
			throw e;
		}
	}

	@Test
	public void testCreateDefaultCapability() throws Exception
	{
		System.out.println("createDefaultCapability");
		systemSecurityInitializerLocal.initializeDefaultSystemAccessCapabilities();
		UserSecurityProfileVO createdUser = userSecurityProfileTestUtility.createNewUserSecurityProfileWithCapability();
		systemSecurityManager.setupDefaultCapabilitiesForUser(createdUser);
		Long applicationID = configDataManager.loadValue(SecurityConstants.CONFIG_SYSTEM_APPLICATION_ID, Long.class);
		ApplicationVO applicationVO = applicationManager.loadApplicationWithID(applicationID);
		
		AccessRightsVO accessRights = new AccessRightsVOBuilder()
				.setActionAsString("logon")
				.setResourceAsString("system")
				.setUserProfileVO(createdUser)
				.setApplicationID(applicationVO.getId())
				.setApplicationToken(applicationVO.getToken())
				.createAccessRightsVO();

		systemSecurityManager.checkAccessRights(accessRights);
	}

	@Test
	public void testFindSecurityProfileWithLogonIDandPassword() throws Exception
	{
		System.out.println("findSecurityProfileWithLogonIDandPassword");
		UserSecurityProfileVO newUser = userSecurityProfileTestUtility.createNewUserSecurityProfile();
		UserSecurityProfileVO foundUser = systemSecurityManager.findSecurityProfileWithLogonIDandPassword(newUser.getLogonId(), "rdonasco");
		assertNotNull("user not found", foundUser);
	}

	@Test(expected = SecurityAuthenticationException.class)
	public void testFindSecurityProfileWithLogonIDandWrongPassword() throws
			Exception
	{
		System.out.println("FindSecurityProfileWithLogonIDandWrongPassword");
		UserSecurityProfileVO newUser = userSecurityProfileTestUtility.createNewUserSecurityProfile();
		systemSecurityManager.findSecurityProfileWithLogonIDandPassword(newUser.getLogonId(), "xrdonasco");
	}

	void checkAccessAndThrowRealCause(AccessRightsVO accessRights) throws
			Throwable
	{
		try
		{
			systemSecurityManager.checkAccessRights(accessRights);
		}
		catch (Exception e)
		{
			if(e.getCause() != null)
			{
				throw e.getCause();
			}
			else
			{
				throw e;
			}
		}
	}
}
