package com.rdonasco.security.services;

import com.rdonasco.security.dao.ActionDAO;
import com.rdonasco.security.exceptions.CapabilityManagerException;
import com.rdonasco.security.exceptions.SecurityProfileNotFoundException;
import com.rdonasco.security.model.Action;
import com.rdonasco.security.utils.SecurityEntityValueObjectDataUtility;
import com.rdonasco.security.vo.ActionVO;
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
import test.util.ArchiveCreator;
import static org.junit.Assert.*;

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

	@Deployment
	public static JavaArchive createTestArchive()
	{
		JavaArchive archive = ArchiveCreator.createCommonArchive()
				.addPackage(ActionDAO.class.getPackage())
				.addPackage(SystemSecurityManagerLocal.class.getPackage())
				.addPackage(ActionVO.class.getPackage())
				.addPackage(Action.class.getPackage());

		return archive;
	}
	
	@Test
	public void testCreateSecurityProfileWithoutCapability() throws Exception
	{
		System.out.println("createSecurityProfileWithoutCapability");
		UserSecurityProfileVO userProfile = createTestDataWithoutCapability();
		
		UserSecurityProfileVO createdUser = systemSecurityManager.createNewSecurityProfile(userProfile);
		assertNotNull(createdUser);
		
	}
	
	@Test
	public void testCreateSecurityProfileWithCapability() throws Exception
	{
		System.out.println("createSecurityProfileWithCapability");
		UserSecurityProfileVO userProfile = createTestDataUserProfileWithCapability();
		
		UserSecurityProfileVO createdUser = systemSecurityManager.createNewSecurityProfile(userProfile);
		
		assertNotNull("user not created",createdUser);
		assertNotNull("id is null",createdUser.getId());
		assertNotNull("capabilities is not null",createdUser.getCapabilityVOList());
		assertTrue("capabilities.size() is zero",createdUser.getCapabilityVOList().size() > 0);
		for(UserCapabilityVO savedUserCapabilityVO : createdUser.getCapabilityVOList())
		{
			assertNotNull(savedUserCapabilityVO.getId());
			System.out.println("savedUserCapabilityVO.toString()=" + savedUserCapabilityVO.toString());
		}
	}
	
	@Test(expected=SecurityProfileNotFoundException.class)
	public void testRemoveSecurityProfile() throws Exception
	{
		System.out.println("removeSecurityProfile");
		UserSecurityProfileVO userSecurityProfileVO = createTestDataUserProfileWithCapability();
		UserSecurityProfileVO createdUser = systemSecurityManager.createNewSecurityProfile(userSecurityProfileVO);
		systemSecurityManager.removeSecurityProfile(createdUser);
		systemSecurityManager.findSecurityProfileWithLogonID(createdUser.getLogonId());
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
				.setDescription(name + "description")
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

	private UserCapabilityVO createTestDataUserCapabilityVO(CapabilityVO capabilityVO)
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
				.setLoginId("rdonasco"+SecurityEntityValueObjectDataUtility.generateRandomID())
				.setPassword("rdonasco")
				.createUserSecurityProfileVO();
		return userProfile;
	}
}
