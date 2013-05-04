package com.rdonasco.security.services;

import com.rdonasco.config.dao.ConfigElementDAO;
import com.rdonasco.config.data.ConfigElement;
import com.rdonasco.config.parsers.ValueParser;
import com.rdonasco.config.services.ConfigDataManagerLocal;
import com.rdonasco.config.util.ConfigDataValueObjectConverter;
import com.rdonasco.config.vo.ConfigAttributeVO;
import com.rdonasco.security.dao.ActionDAO;
import com.rdonasco.security.exceptions.CapabilityManagerException;
import com.rdonasco.security.exceptions.SecurityManagerException;
import com.rdonasco.security.exceptions.SecurityProfileNotFoundException;
import com.rdonasco.security.model.Action;
import com.rdonasco.security.utils.SecurityEntityValueObjectDataUtility;
import com.rdonasco.security.vo.AccessRightsVO;
import com.rdonasco.security.vo.AccessRightsVOBuilder;
import com.rdonasco.security.vo.ActionVO;
import com.rdonasco.security.vo.CapabilityActionVO;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.CapabilityVOBuilder;
import com.rdonasco.security.vo.ResourceVO;
import com.rdonasco.security.vo.ResourceVOBuilder;
import com.rdonasco.security.vo.UserCapabilityVO;
import com.rdonasco.security.vo.UserCapabilityVOBuilder;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import com.rdonasco.security.vo.UserSecurityProfileVOBuilder;
import javax.ejb.EJB;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.rdonasco.security.utils.ArchiveCreator;
import com.rdonasco.security.utils.CapabilityTestUtility;
import com.rdonasco.security.utils.RoleTestUtility;
import com.rdonasco.security.utils.UserSecurityProfileTestUtility;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 * Unit test for simple App.
 */
@RunWith(Arquillian.class)
public class SystemSecurityManagerLocalTest
{

	@EJB
	private SystemSecurityManagerLocal systemSecurityManager;
	@EJB
	private CapabilityManagerLocal capabilityManager;
	@EJB
	private SystemSecurityInitializerLocal systemSecurityInitializerLocal;

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
		userSecurityProfileTestUtility = new UserSecurityProfileTestUtility(capabilityManager, systemSecurityManager);
	}

	@Test
	public void testCreateSecurityProfileWithoutCapability() throws Exception
	{
		System.out.println("createSecurityProfileWithoutCapability");

		UserSecurityProfileVO createdUser = createNewUserSecurityProfile();
		assertNotNull(createdUser);

	}

	@Test
	public void testCreateSecurityProfileWithCapability() throws Exception
	{
		System.out.println("createSecurityProfileWithCapability");

		UserSecurityProfileVO createdUser = createNewUserSecurityProfileWithCapability();

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
		UserSecurityProfileVO createdUser = createNewUserSecurityProfileWithCapability();
		CapabilityVO additionalCapability = createTestDataCapabilityWithActionAndResourceName("fire", "employee");
		systemSecurityManager.addCapabilityForUser(createdUser, additionalCapability);
		String actionName = null;
		for (CapabilityActionVO action : additionalCapability.getActions())
		{
			actionName = action.getActionVO().getName();
		}
		AccessRightsVO accessRights = new AccessRightsVOBuilder()
				.setActionAsString(actionName)
				.setResourceAsString(additionalCapability.getResource().getName())
				.setUserProfileVO(createdUser)
				.createAccessRightsVO();
		systemSecurityManager.checkAccessRights(accessRights);
	}

	@Test(expected = Exception.class)
	public void testCheckAccessRights() throws Exception
	{
		System.out.println("CheckAccessRights");
		UserSecurityProfileVO createdUser = createNewUserSecurityProfileWithCapability();
		CapabilityVO additionalCapability = createTestDataCapabilityWithActionAndResourceName("fire", "employee");
		systemSecurityManager.addCapabilityForUser(createdUser, additionalCapability);
		String actionName = "doit";
		AccessRightsVO accessRights = new AccessRightsVOBuilder()
				.setActionAsString(actionName)
				.setResourceAsString(additionalCapability.getResource().getName())
				.setUserProfileVO(createdUser)
				.createAccessRightsVO();
		try
		{
			systemSecurityManager.checkAccessRights(accessRights);
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	@Test
	public void testCheckAccessRightsOnNonRestrictedResourceToCreateDefaultCapability()
			throws Exception
	{
		System.out.println("CheckAccessRightsOnNonRestrictedResourceToCreateDefaultCapability");
		UserSecurityProfileVO createdUser = createNewUserSecurityProfileWithCapability();
		String actionName = "do";
		String resourceName = "her";
		String capabilityTitle = "do her";
		AccessRightsVO accessRights = new AccessRightsVOBuilder()
				.setActionAsString(actionName)
				.setResourceAsString(resourceName)
				.setUserProfileVO(createdUser)
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
		UserSecurityProfileVO createdUser = createNewUserSecurityProfileWithCapability();
		String actionName = "enter";
		String resourceName = "the dragon";
		String capabilityTitle = String.format("%1s %2s", actionName, resourceName);
		AccessRightsVO accessRights = new AccessRightsVOBuilder()
				.setActionAsString(actionName)
				.setResourceAsString(resourceName)
				.setUserProfileVO(createdUser)
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
		UserSecurityProfileVO createdUser = createNewUserSecurityProfileWithCapability();
		systemSecurityManager.removeSecurityProfile(createdUser);
		try
		{
			systemSecurityManager.findSecurityProfileWithLogonID(createdUser.getLogonId());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
	}

	@Test
	public void testCreateDefaultCapability() throws Exception
	{
		System.out.println("createDefaultCapability");
		systemSecurityInitializerLocal.initializeDefaultSystemAccessCapabilities();
		UserSecurityProfileVO createdUser = createNewUserSecurityProfileWithCapability();
		systemSecurityManager.setupDefaultCapabilitiesForUser(createdUser);
		AccessRightsVO accessRights = new AccessRightsVOBuilder()
				.setActionAsString("logon")
				.setResourceAsString("system")
				.setUserProfileVO(createdUser)
				.createAccessRightsVO();

		systemSecurityManager.checkAccessRights(accessRights);
	}

	// ------ utility methods below here ------ //
	private ActionVO createTestDataActionNamed(String name) throws
			CapabilityManagerException
	{
		ActionVO savedAction = capabilityManager.findOrAddActionNamedAs(name);
		return savedAction;
	}

	private ResourceVO createTestDataResourceNamed(String name) throws
			CapabilityManagerException
	{
		ResourceVO resourceToAdd = new ResourceVOBuilder()
				.setName(name)
				.createResourceVO();

		ResourceVO resourceAdded = capabilityManager.addResource(resourceToAdd);
		return resourceAdded;
	}

	private CapabilityVO createTestDataCapabilityWithActionAndResourceName(
			final String actionName,
			final String resourceName) throws CapabilityManagerException
	{
		ActionVO action = createTestDataActionNamed(actionName);
		ResourceVO resource = createTestDataResourceNamed(resourceName + SecurityEntityValueObjectDataUtility.generateRandomID());
		final String capabilityTitle = "capability to " + action.getName() + " " + resource.getName();
		CapabilityVO capabilityVO = new CapabilityVOBuilder()
				.addAction(action)
				.setResource(resource)
				.setTitle(capabilityTitle)
				.setDescription(capabilityTitle + " description")
				.createCapabilityVO();
		CapabilityVO savedCapabilityVO = capabilityManager.createNewCapability(capabilityVO);
		return savedCapabilityVO;
	}

	private UserCapabilityVO createTestDataUserCapabilityVO(
			CapabilityVO capabilityVO)
	{
		UserCapabilityVO userCapabilityVO = new UserCapabilityVOBuilder()
				.setCapability(capabilityVO)
				.createUserCapabilityVO();
		return userCapabilityVO;
	}

	private UserSecurityProfileVO createTestDataUserProfileWithCapability()
			throws CapabilityManagerException
	{
		UserSecurityProfileVO userProfile = createTestDataWithoutCapability();
		CapabilityVO capabilityVO = createTestDataCapabilityWithActionAndResourceName("edit", "pets");
		UserCapabilityVO userCapabilityVO = createTestDataUserCapabilityVO(capabilityVO);
		userProfile.addCapbility(userCapabilityVO);
		return userProfile;
	}

	private UserSecurityProfileVO createTestDataWithoutCapability()
	{
		UserSecurityProfileVO userProfile = new UserSecurityProfileVOBuilder()
				.setLoginId("rdonasco" + SecurityEntityValueObjectDataUtility.generateRandomID())
				.setPassword("rdonasco")
				.createUserSecurityProfileVO();
		return userProfile;
	}

	private UserSecurityProfileVO createNewUserSecurityProfile() throws
			SecurityManagerException
	{
		UserSecurityProfileVO userProfile = createTestDataWithoutCapability();
		UserSecurityProfileVO createdUser = systemSecurityManager.createNewSecurityProfile(userProfile);
		return createdUser;
	}

	private UserSecurityProfileVO createNewUserSecurityProfileWithCapability()
			throws SecurityManagerException, CapabilityManagerException
	{
		UserSecurityProfileVO userProfile = createTestDataUserProfileWithCapability();
		UserSecurityProfileVO createdUser = systemSecurityManager.createNewSecurityProfile(userProfile);
		return createdUser;
	}
}
