/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.utils;

import com.rdonasco.security.model.Action;
import com.rdonasco.security.model.Application;
import com.rdonasco.security.model.ApplicationHost;
import com.rdonasco.security.model.Capability;
import com.rdonasco.security.model.CapabilityAction;
import com.rdonasco.security.model.Resource;
import com.rdonasco.security.model.Role;
import com.rdonasco.security.model.RoleCapability;
import com.rdonasco.security.model.SecurityGroup;
import com.rdonasco.security.model.SecurityGroupRole;
import com.rdonasco.security.model.UserCapability;
import com.rdonasco.security.model.UserSecurityProfile;
import com.rdonasco.security.vo.ActionVO;
import com.rdonasco.security.vo.ApplicationHostVO;
import com.rdonasco.security.vo.ApplicationVO;
import com.rdonasco.security.vo.ApplicationVOBuilder;
import com.rdonasco.security.vo.CapabilityActionVO;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.CapabilityVOBuilder;
import com.rdonasco.security.vo.ResourceVO;
import com.rdonasco.security.vo.ResourceVOBuilder;
import com.rdonasco.security.vo.RoleCapabilityVO;
import com.rdonasco.security.vo.RoleVO;
import com.rdonasco.security.vo.SecurityGroupRoleVO;
import com.rdonasco.security.vo.SecurityGroupRoleVOBuilder;
import com.rdonasco.security.vo.SecurityGroupVO;
import com.rdonasco.security.vo.SecurityGroupVOBuilder;
import com.rdonasco.security.vo.UserCapabilityVO;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Roy F. Donasco
 */
public class SecurityEntityValueObjectConverterTest
{

	private static final String ID_MISMATCH = "id mismatch";

	public SecurityEntityValueObjectConverterTest()
	{
	}
	private static final Logger LOG = Logger.getLogger(SecurityEntityValueObjectConverterTest.class.getName());

	@BeforeClass
	public static void setUpClass()
	{
	}

	@AfterClass
	public static void tearDownClass()
	{
	}

	@Before
	public void setUp()
	{
	}

	@After
	public void tearDown()
	{
	}

	/**
	 * Test of toUserProfile method, of class
	 * SecurityEntityValueObjectConverter.
	 */
	@Test
	public void testToUserProfileNoCapability() throws Exception
	{
		System.out.println("toUserProfileNoCapability");
		UserSecurityProfileVO userSecurityProfileVO = com.rdonasco.security.utils.SecurityEntityValueObjectDataUtility.createTestDataUserSecurityProfileVOwithoutCapability();
		UserSecurityProfile result = SecurityEntityValueObjectConverter.toUserProfile(userSecurityProfileVO);
		assertNotNull(result);
		assertNotNull(result.getId());
		assertNotNull(result.getRegistrationToken());
		assertNotNull(result.getRegistrationTokenExpiration());
		assertEquals(userSecurityProfileVO.getRegistrationToken(), result.getRegistrationToken());
		assertEquals(userSecurityProfileVO.getRegistrationTokenExpiration(), result.getRegistrationTokenExpiration());
		assertEquals(new Long(Long.MIN_VALUE), result.getId());

	}

	@Test
	public void testToUserProfileWithCapability() throws Exception
	{
		System.out.println("toUserProfileWithCapability");
		CapabilityVO capability = com.rdonasco.security.utils.SecurityEntityValueObjectDataUtility.createTestDataCapabilityVO();
		UserCapabilityVO userCapability = com.rdonasco.security.utils.SecurityEntityValueObjectDataUtility.createTestDataUserCapabilityVO(capability);
		UserSecurityProfileVO userProfileVO = com.rdonasco.security.utils.SecurityEntityValueObjectDataUtility.createTestDataUserSecurityProfileVOwithUserCapability(userCapability);
		UserSecurityProfile resultingUserSecurityProfile = SecurityEntityValueObjectConverter
				.toUserProfile(userProfileVO);
		assertNotNull(resultingUserSecurityProfile);
		assertEquals("id did not match", userProfileVO.getId(), resultingUserSecurityProfile.getId());
		assertEquals("login id did not match", userProfileVO.getLogonId(), resultingUserSecurityProfile.getLogonId());
		assertEquals("password did not match", userProfileVO.getPassword(), resultingUserSecurityProfile.getPassword());
		assertEquals("user capabilities did not match", userProfileVO.getCapabilities().size(), resultingUserSecurityProfile.getCapabilities().size());
		UserCapability resultUserCapability = resultingUserSecurityProfile.getCapabilities().iterator().next();
		assertEquals("userCapability.id did not match", userCapability.getId(), resultUserCapability.getId());
		assertEquals("userCapability.userProfile.id did not match", userCapability.getUserProfile().getId(), resultUserCapability.getUserProfile().getId());
		assertEquals("userCapability.userProfile.LoginId did not match", userCapability.getUserProfile().getLogonId(), resultUserCapability.getUserProfile().getLogonId());
		assertEquals("capability.id did not match", capability.getId(), resultUserCapability.getCapability().getId());
		assertEquals("capability.title did not match", capability.getTitle(), resultUserCapability.getCapability().getTitle());
		assertEquals("capability.description did not match", capability.getDescription(), resultUserCapability.getCapability().getDescription());
		assertEquals("capability.actions.size did not match", capability.getActions().size(), resultUserCapability.getCapability().getActions().size());
		assertEquals("capability.resource.id did not match", capability.getResource().getId(), resultUserCapability.getCapability().getResource().getId());
		assertEquals("capability.resource.name did not match", capability.getResource().getName(), resultUserCapability.getCapability().getResource().getName());

	}

	/**
	 * Test of toCapabilityVO method, of class
	 * SecurityEntityValueObjectConverter.
	 */
	@Test
	public void testToCapabilityVO() throws Exception
	{
		System.out.println("toCapabilityVO");
		int size = 3;
		Capability testCapability = com.rdonasco.security.utils.SecurityEntityValueObjectDataUtility.createTestDataCapabilityOnResourceAndAction("User", "Add");
		List<CapabilityAction> actions = com.rdonasco.security.utils.SecurityEntityValueObjectDataUtility.createTestDataCapabilityActionsForWithSize(testCapability, size);
		testCapability.setActions(actions);
		CapabilityVO expResult = new CapabilityVOBuilder()
				.setId(testCapability.getId())
				.setTitle(testCapability.getTitle())
				.setDescription(testCapability.getDescription())
				.createCapabilityVO();
		CapabilityVO result = SecurityEntityValueObjectConverter.toCapabilityVO(testCapability);
		assertEquals("id did not match", expResult.getId(), result.getId());
		assertEquals("actions.size did not match", size, result.getActions().size());
		assertEquals("description did not match", expResult.getDescription(), result.getDescription());
		assertEquals("title did not match", expResult.getTitle(), result.getTitle());
		assertNotNull("resource not set", result.getResource());
	}

	@Test
	public void testToCapability() throws Exception
	{
		System.out.println("toCapability");
		CapabilityVO testCapabilityVO = com.rdonasco.security.utils.SecurityEntityValueObjectDataUtility.createTestDataCapabilityVO();
//		List<ActionVO> actions = com.rdonasco.security.utils.SecurityEntityValueObjectDataUtility.createTestDataActionVOList();
		Capability expResult = new Capability();
		expResult.setId(testCapabilityVO.getId());
		expResult.setTitle(testCapabilityVO.getTitle());
		expResult.setDescription(testCapabilityVO.getDescription());
		Capability result = SecurityEntityValueObjectConverter.toCapability(testCapabilityVO);
		assertEquals("id did not match", expResult.getId(), result.getId());
		assertEquals("actions.size did not match", 1, result.getActions().size());
		assertEquals("description did not match", expResult.getDescription(), result.getDescription());
		assertEquals("title did not match", expResult.getTitle(), result.getTitle());
		assertNotNull("resource not set", result.getResource());
		for (CapabilityAction action : result.getActions())
		{
			assertNotNull(action.getId());
		}
	}

	/**
	 * Test of toResource method, of class SecurityEntityValueObjectConverter.
	 */
	@Test
	public void testToResource() throws Exception
	{
		System.out.println("toResource");
		ResourceVO resourceVO = com.rdonasco.security.utils.SecurityEntityValueObjectDataUtility.createTestDataResourceVO();
		Resource expResult = new Resource();
		expResult.setId(resourceVO.getId());
		expResult.setName(resourceVO.getName());
		Resource result = SecurityEntityValueObjectConverter.toResource(resourceVO);
		assertEquals("result.id did not match", expResult.getId(), result.getId());
		assertEquals("result.name did not match", expResult.getName(), result.getName());

	}

	@Test
	public void testToResourceVO() throws Exception
	{
		System.out.println("toResourceVO");
		Resource resource = com.rdonasco.security.utils.SecurityEntityValueObjectDataUtility.createTestDataResource();
		ResourceVO expResult = new ResourceVOBuilder()
				.setId(resource.getId())
				.setName(resource.getName())
				.createResourceVO();
		ResourceVO result = SecurityEntityValueObjectConverter.toResourceVO(resource);
		assertEquals("result.id did not match", expResult.getId(), result.getId());
		assertEquals("result.name did not match", expResult.getName(), result.getName());
	}

	@Test
	public void testToAction() throws Exception
	{
		System.out.println("toAction");
		ActionVO actionVO = com.rdonasco.security.utils.SecurityEntityValueObjectDataUtility.createTestDataActionVO();
		Action expResult = new Action();
		expResult.setId(actionVO.getId());
		expResult.setName(actionVO.getName());
		Action result = SecurityEntityValueObjectConverter.toAction(actionVO);
		assertEquals("result.id did not match", expResult.getId(), result.getId());
		assertEquals("result.name did not match", expResult.getName(), result.getName());

	}

	@Test
	public void testToActionVO() throws Exception
	{
		System.out.println("toAtionVO");
		Action action = com.rdonasco.security.utils.SecurityEntityValueObjectDataUtility.createTestDataAction();
		ActionVO expResult = new ActionVO();
		expResult.setId(action.getId());
		expResult.setName(action.getName());
		ActionVO result = SecurityEntityValueObjectConverter.toActionVO(action);
		assertEquals("result.id did not match", expResult.getId(), result.getId());
		assertEquals("result.name did not match", expResult.getName(), result.getName());
	}

	@Test
	public void testToUserCapability() throws Exception
	{
		System.out.println("toUserCapability");
		CapabilityVO capabilityVO = com.rdonasco.security.utils.SecurityEntityValueObjectDataUtility.createTestDataCapabilityVO();
		UserCapabilityVO userCapabilityVO = com.rdonasco.security.utils.SecurityEntityValueObjectDataUtility.createTestDataUserCapabilityVO(capabilityVO);
		UserCapability expResult = new UserCapability();
		expResult.setId(userCapabilityVO.getId());
		expResult.setCapability(SecurityEntityValueObjectConverter.toCapability(capabilityVO));
		UserCapability result = SecurityEntityValueObjectConverter.toUserCapability(userCapabilityVO);
		assertEquals(expResult.getId(), result.getId());
		assertEquals(expResult.getCapability(), result.getCapability());
	}

	@Test
	public void testToCapabilityActionVO() throws Exception
	{
		System.out.println("toCapabilityActionVO");

		Capability capability = SecurityEntityValueObjectDataUtility.createTestDataCapabilityOnResourceAndAction("cab", "edit");

		for (CapabilityAction capabilityAction : capability.getActions())
		{
			CapabilityActionVO capabilityActionVO = SecurityEntityValueObjectConverter.toCapabilityActionVO(capabilityAction);
			assertEquals(capabilityAction.getId(), capabilityActionVO.getId());
			assertEquals(capabilityAction.getAction().getId(), capabilityActionVO.getActionVO().getId());
			assertEquals(capabilityAction.getCapability().getId(), capabilityActionVO.getCapabilityVO().getId());
		}

	}

	@Test
	public void testToCapabilityAction() throws Exception
	{
		System.out.println("toCapabilityAction");
		CapabilityVO capabilityVO = SecurityEntityValueObjectDataUtility.createTestDataCapabilityVO();
		for (CapabilityActionVO capabilityActionVO : capabilityVO.getActions())
		{
			CapabilityAction capabilityAction = SecurityEntityValueObjectConverter.toCapabilityAction(capabilityActionVO);
			assertNotNull("action is null", capabilityAction.getAction());
			assertEquals(capabilityActionVO.getActionVO().getId(), capabilityAction.getAction().getId());

		}

	}

	@Test
	public void testToUserCapabilityVO() throws Exception
	{
		System.out.println("toUserCapabilityVO");
		Action action = SecurityEntityValueObjectDataUtility.createTestDataAction();
		Resource resource = SecurityEntityValueObjectDataUtility.createTestDataResource();
		UserCapability userCapability = SecurityEntityValueObjectDataUtility.createTestDataUserCapabilityWithResourceAndAction(resource, action);
		UserCapabilityVO userCapabilityVO = SecurityEntityValueObjectConverter.toUserCapabilityVO(userCapability);
		assertNotNull(userCapabilityVO);
		assertEquals(userCapability.getId(), userCapabilityVO.getId());
		assertNotNull(userCapabilityVO.getCapability());
		assertEquals(userCapability.getCapability().getId(), userCapabilityVO.getCapability().getId());
		if (userCapability.getUserProfile() != null)
		{
			assertNotNull(userCapabilityVO.getUserProfile());
			assertEquals(userCapability.getUserProfile().getId(), userCapabilityVO.getUserProfile().getId());
		}



	}

	@Test
	public void testToUserProfileVO() throws Exception
	{
		System.out.println("toUserProfileVO");
		UserSecurityProfile userSecurityProfile = SecurityEntityValueObjectDataUtility.createTestDataUserSecurityProfileWithCapability("edit", "User");
		UserSecurityProfileVO result = SecurityEntityValueObjectConverter.toUserProfileVO(userSecurityProfile);
		assertNotNull(result);
		assertEquals(userSecurityProfile.getId(), result.getId());
		assertNotNull(result.getRegistrationToken());
		assertNotNull(result.getRegistrationTokenExpiration());
		assertEquals(userSecurityProfile.getRegistrationToken(), result.getRegistrationToken());
		assertEquals(userSecurityProfile.getRegistrationTokenExpiration(), result.getRegistrationTokenExpiration());
	}

	@Test
	public void testToRole() throws Exception
	{
		System.out.println("toRole");
		String[] capabilities =
		{
			"Manage User", "Manage User Capability"
		};
		RoleVO roleVO = SecurityEntityValueObjectDataUtility.createTestRoleVO("user manager", capabilities);
		Role role = SecurityEntityValueObjectConverter.toRole(roleVO);
		assertNotNull("converted to null", role);
		assertEquals(ID_MISMATCH, roleVO.getId(), role.getId());
		assertEquals("name mismatch", roleVO.getName(), role.getName());
		assertNotNull("null capablities", role.getCapabilities());
		assertEquals("capabilities.size mismatch", capabilities.length, role.getCapabilities().size());
		Map<Long, RoleCapability> roleCapabilities = new HashMap<Long, RoleCapability>();
		for (RoleCapability roleCapability : role.getCapabilities())
		{
			roleCapabilities.put(roleCapability.getId(), roleCapability);
		}
		for (RoleCapabilityVO roleCapabilityVO : roleVO.getRoleCapabilities())
		{
			assertNotNull("roleCapability not converted", roleCapabilities.get(roleCapabilityVO.getId()));
		}
	}

	@Test
	public void testToRoleCapability() throws Exception
	{
		System.out.println("toRoleCapability");
		RoleCapabilityVO roleCapabilityVO = SecurityEntityValueObjectDataUtility
				.createTestRoleCapabilityVO();
		RoleCapability roleCapability = SecurityEntityValueObjectConverter.toRoleCapability(roleCapabilityVO);
		assertNotNull("not Converted", roleCapability);
		assertEquals(ID_MISMATCH, roleCapabilityVO.getId(), roleCapability.getId());
		assertEquals("role.id mismatch", roleCapabilityVO.getRoleVO().getId(), roleCapability.getRole().getId());
		assertEquals("capability.id mismatch", roleCapabilityVO.getCapabilityVO().getId(), roleCapability.getCapability().getId());
	}

	@Test
	public void testToRoleVO() throws Exception
	{
		System.out.println("toRoleVO");
		Role role = SecurityEntityValueObjectDataUtility.createTestDataRole();
		RoleVO roleVO = SecurityEntityValueObjectConverter.toRoleVO(role);
		assertEquals(ID_MISMATCH, role.getId(), roleVO.getId());
		assertEquals("name did not match", roleVO.getName(), role.getName());
		assertNotNull("capabilities not converted", roleVO.getRoleCapabilities());
		assertEquals("capabilities size did not match", role.getCapabilities().size(), roleVO.getRoleCapabilities().size());
		Map<Long, RoleCapabilityVO> roleCapabilities = new HashMap<Long, RoleCapabilityVO>();
		for (RoleCapabilityVO roleCapabilityVO : roleVO.getRoleCapabilities())
		{
			roleCapabilities.put(roleCapabilityVO.getId(), roleCapabilityVO);
		}
		for (RoleCapability roleCapability : role.getCapabilities())
		{
			assertNotNull("roleCapability not converted", roleCapabilities.get(roleCapability.getId()));
			assertNotNull("role not copied", roleCapability.getRole());
			assertEquals("role id mismatch", role.getId(), roleCapability.getRole().getId());
		}
	}

	@Test
	public void testToRoleCapabilityVO() throws Exception
	{
		System.out.println("toRoleCapabilityVO");
		RoleCapability roleCapability = SecurityEntityValueObjectDataUtility.createTestDataRoleCapabilityForRole(SecurityEntityValueObjectDataUtility.createTestDataRole());
		RoleCapabilityVO roleCapabilityVO = SecurityEntityValueObjectConverter.toRoleCapabilityVO(roleCapability);
		assertEquals(ID_MISMATCH, roleCapability.getId(), roleCapabilityVO.getId());
		assertEquals("role id mismatch", roleCapability.getRole().getId(), roleCapabilityVO.getRoleVO().getId());

	}

	@Test
	public void testToSecurityGroup() throws Exception
	{
		System.out.println("toSecurityGroup");
		List<SecurityGroupRoleVO> securityGroupRoles = new ArrayList<SecurityGroupRoleVO>();
		SecurityGroupVO securityGroupVO = new SecurityGroupVOBuilder()
				.setId(Long.MIN_VALUE)
				.setName("testGroup")
				.setSecurityGroupRoles(securityGroupRoles)
				.createSecurityGroupVO();
		SecurityGroupRoleVO securityGroupRoleVO = new SecurityGroupRoleVOBuilder()
				.setRole(SecurityEntityValueObjectDataUtility.createTestRoleVO("user manager", "manage users"))
				.setSecurityGroup(securityGroupVO)
				.createSecurityGroupRoleVO();
		securityGroupRoles.add(securityGroupRoleVO);
		SecurityGroup securityGroup = SecurityEntityValueObjectConverter.toSecurityGroup(securityGroupVO);
		assertNotNull(securityGroup);
		assertEquals("id mismatch", securityGroupVO.getId(), securityGroup.getId());
		assertEquals("name mismatch", securityGroupVO.getName(), securityGroup.getName());
		assertNotNull("null group roles", securityGroup.getGroupRoles());
		assertTrue(securityGroup.getGroupRoles().size() > 0);
		for (SecurityGroupRole role : securityGroup.getGroupRoles())
		{
			assertEquals("parent group mismatch", securityGroupVO.getId(), role.getSecurityGroup().getId());
		}
	}

	@Test
	public void testToSecurityGroupVO() throws Exception
	{
		System.out.println("toSecurityGroupVO");
		List<SecurityGroupRole> securityGroupRoles = new ArrayList<SecurityGroupRole>();
		SecurityGroup securityGroup = new SecurityGroup();
		securityGroup.setId(Long.MIN_VALUE);
		securityGroup.setName("testGroup");
		securityGroup.setGroupRoles(securityGroupRoles);
		SecurityGroupRole securityGroupRole = new SecurityGroupRole();
		securityGroupRole.setId(Long.MIN_VALUE);
		securityGroupRole.setRole(SecurityEntityValueObjectDataUtility.createTestDataRole());
		securityGroupRole.setSecurityGroup(securityGroup);
		securityGroupRoles.add(securityGroupRole);
		SecurityGroupVO securityGroupVO = SecurityEntityValueObjectConverter.toSecurityGroupVO(securityGroup);
		SecurityGroupRoleVO expectedRole = new SecurityGroupRoleVOBuilder()
				.setId(securityGroupRole.getId())
				.setRole(SecurityEntityValueObjectConverter.toRoleVO(securityGroupRole.getRole()))
				.createSecurityGroupRoleVO();
		assertNotNull("null conversion", securityGroupVO);
		assertEquals("id mismatch", securityGroup.getId(), securityGroupVO.getId());
		assertEquals("name mismatch", securityGroup.getName(), securityGroupVO.getName());
		assertNotNull("empty roles", securityGroupVO.getGroupRoleVOs());
		Iterator<SecurityGroupRoleVO> iterator = securityGroupVO.getGroupRoleVOs().iterator();
		if (iterator.hasNext())
		{
			SecurityGroupRoleVO role = iterator.next();
			assertEquals("role id mismatch", expectedRole.getId(), role.getId());
			assertEquals("role name mismatch", expectedRole.getRoleVO().getName(), role.getRoleVO().getName());
			assertEquals("role parent group id mismatch", securityGroup.getId(), role.getSecurityGroup().getId());
		}
		else
		{
			fail("role not included");
		}

	}

	@Test
	public void testToApplicationVO() throws Exception
	{
		System.out.println("toApplicationVO");
		Application application = new Application();
		application.setId(Long.MIN_VALUE);
		application.setName("Security");
		application.setToken("theToken");
		application.setHosts(new ArrayList<ApplicationHost>());
		ApplicationHost applicationHost = new ApplicationHost();
		applicationHost.setApplication(application);
		applicationHost.setId(Long.MIN_VALUE);
		applicationHost.setHostNameOrIpAddress("roy.donasco.com");
		application.getHosts().add(applicationHost);
		ApplicationVO applicationVO = SecurityEntityValueObjectConverter.toApplicationVO(application);
		assertEquals("applicationVO id mismatch", application.getId(), applicationVO.getId());
		assertEquals("applicationVO name mismatch", application.getName(), applicationVO.getName());
		assertEquals("applicationVO token mismatch", application.getToken(), applicationVO.getToken());
		ApplicationHostVO hostVO = applicationVO.getHosts().get(0);
		assertEquals("application host id is not converted", applicationHost.getId(), hostVO.getId());
		assertEquals("application host is not converted", applicationHost.getHostNameOrIpAddress(), hostVO.getHostNameOrIpAddress());
	}

	@Test
	public void testToApplication() throws Exception
	{
		System.out.println("toApplication");
		ApplicationHostVO applicationHostVO = new ApplicationHostVO();
		applicationHostVO.setId(Long.MIN_VALUE);
		applicationHostVO.setHostNameOrIpAddress("roy.donasco.com");
		ApplicationVO applicationVO = new ApplicationVOBuilder()
				.setId(Long.MIN_VALUE)
				.setName("Security")
				.setToken("Token")
				.addHost(applicationHostVO)
				.createApplicationVO();
		Application application = SecurityEntityValueObjectConverter.toApplication(applicationVO);
		assertEquals("application id mismatch", applicationVO.getId(), application.getId());
		assertEquals("application name mismatch", applicationVO.getName(), application.getName());
		assertEquals("application token mismatch", applicationVO.getToken(), application.getToken());
		ApplicationHost applicationHost = application.getHosts().iterator().next();
		assertEquals("applicationVO host id is not converted", applicationHostVO.getId(), applicationHost.getId());
		assertEquals("applicationVO  host is not converted", applicationHostVO.getHostNameOrIpAddress(), applicationHost.getHostNameOrIpAddress());

	}
}
