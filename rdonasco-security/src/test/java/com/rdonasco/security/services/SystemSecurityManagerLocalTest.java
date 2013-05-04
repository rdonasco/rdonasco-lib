package com.rdonasco.security.services;

import com.rdonasco.config.dao.ConfigElementDAO;
import com.rdonasco.config.data.ConfigElement;
import com.rdonasco.config.parsers.ValueParser;
import com.rdonasco.config.services.ConfigDataManagerLocal;
import com.rdonasco.config.util.ConfigDataValueObjectConverter;
import com.rdonasco.config.vo.ConfigAttributeVO;
import com.rdonasco.security.dao.ActionDAO;
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
import com.rdonasco.security.utils.UserSecurityProfileTestUtility;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 * Unit test for simple App.
 */
@RunWith(Arquillian.class)
public class SystemSecurityManagerLocalTest
{
	private static final Logger LOG = Logger.getLogger(SystemSecurityManagerLocalTest.class.getName());

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
				.setUserProfileVO(createdUser)
				.createAccessRightsVO();
		systemSecurityManager.checkAccessRights(accessRights);
	}

	@Test(expected = Exception.class)
	public void testCheckAccessRights() throws Exception
	{
		System.out.println("CheckAccessRights");
		UserSecurityProfileVO createdUser = userSecurityProfileTestUtility.createNewUserSecurityProfileWithCapability();
		CapabilityVO additionalCapability = userSecurityProfileTestUtility.createTestDataCapabilityWithActionAndResourceName("fire", "employee");
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
		UserSecurityProfileVO createdUser = userSecurityProfileTestUtility.createNewUserSecurityProfileWithCapability();
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
		UserSecurityProfileVO createdUser = userSecurityProfileTestUtility.createNewUserSecurityProfileWithCapability();
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
		AccessRightsVO accessRights = new AccessRightsVOBuilder()
				.setActionAsString("logon")
				.setResourceAsString("system")
				.setUserProfileVO(createdUser)
				.createAccessRightsVO();

		systemSecurityManager.checkAccessRights(accessRights);
	}

}
