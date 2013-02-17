/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.utils;

import com.rdonasco.security.model.Action;
import com.rdonasco.security.model.Capability;
import com.rdonasco.security.model.CapabilityAction;
import com.rdonasco.security.model.Resource;
import com.rdonasco.security.model.UserCapability;
import com.rdonasco.security.model.UserSecurityProfile;
import com.rdonasco.security.vo.ActionVO;
import com.rdonasco.security.vo.CapabilityActionVO;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.CapabilityVOBuilder;
import com.rdonasco.security.vo.ResourceVO;
import com.rdonasco.security.vo.ResourceVOBuilder;
import com.rdonasco.security.vo.UserCapabilityVO;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import java.util.List;
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
		assertEquals("login id did not match", userProfileVO.getLoginId(), resultingUserSecurityProfile.getLoginId());
		assertEquals("password did not match", userProfileVO.getPassword(), resultingUserSecurityProfile.getPassword());
		assertEquals("user capabilities did not match", userProfileVO.getCapabilityVOList().size(), resultingUserSecurityProfile.getCapabilities().size());
		UserCapability resultUserCapability = resultingUserSecurityProfile.getCapabilities().iterator().next();
		assertEquals("userCapability.id did not match", userCapability.getId(), resultUserCapability.getId());
		assertEquals("userCapability.userProfile.id did not match", userCapability.getUserProfile().getId(),resultUserCapability.getUserProfile().getId());
		assertEquals("userCapability.userProfile.LoginId did not match", userCapability.getUserProfile().getLoginId(),resultUserCapability.getUserProfile().getLoginId());
		assertEquals("capability.id did not match", capability.getId(), resultUserCapability.getCapability().getId());
		assertEquals("capability.title did not match", capability.getTitle(), resultUserCapability.getCapability().getTitle());
		assertEquals("capability.description did not match", capability.getDescription(), resultUserCapability.getCapability().getDescription());
		assertEquals("capability.actions.size did not match", capability.getActions().size(), resultUserCapability.getCapability().getActions().size());
		assertEquals("capability.resource.id did not match", capability.getResource().getId(), resultUserCapability.getCapability().getResource().getId());
		assertEquals("capability.resource.name did not match", capability.getResource().getName(), resultUserCapability.getCapability().getResource().getName());
		assertEquals("capability.resource.description did not match", capability.getResource().getDescription(), resultUserCapability.getCapability().getResource().getDescription());
		
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
		List<CapabilityAction> actions = com.rdonasco.security.utils.SecurityEntityValueObjectDataUtility.createTestDataCapabilityActionsForWithSize(testCapability,size);
		testCapability.setActions(actions);
		CapabilityVO expResult = new CapabilityVOBuilder()
				.setId(testCapability.getId())
				.setTitle(testCapability.getTitle())
				.setDescription(testCapability.getDescription())
				.createCapabilityVO();		
		CapabilityVO result = SecurityEntityValueObjectConverter.toCapabilityVO(testCapability);
		assertEquals("id did not match",expResult.getId(), result.getId());
		assertEquals("actions.size did not match",size, result.getActions().size());
		assertEquals("description did not match",expResult.getDescription(), result.getDescription());
		assertEquals("title did not match",expResult.getTitle(), result.getTitle());	
		assertNotNull("resource not set",result.getResource());
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
		assertEquals("id did not match",expResult.getId(), result.getId());
		assertEquals("actions.size did not match",1, result.getActions().size());
		assertEquals("description did not match",expResult.getDescription(), result.getDescription());
		assertEquals("title did not match",expResult.getTitle(), result.getTitle());	
		assertNotNull("resource not set",result.getResource());	
		for(CapabilityAction action : result.getActions())
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
		expResult.setDescription(resourceVO.getDescription());
		Resource result = SecurityEntityValueObjectConverter.toResource(resourceVO);
		assertEquals("result.id did not match",expResult.getId(), result.getId());
		assertEquals("result.name did not match",expResult.getName(), result.getName());
		assertEquals("result.description did not match",expResult.getDescription(), result.getDescription());

	}
	
	@Test
	public void testToResourceVO() throws Exception
	{
		System.out.println("toResourceVO");
		Resource resource = com.rdonasco.security.utils.SecurityEntityValueObjectDataUtility.createTestDataResource();
		ResourceVO expResult = new ResourceVOBuilder()
				.setId(resource.getId())
				.setName(resource.getName())
				.setDescription(resource.getDescription())
				.createResourceVO();
		ResourceVO result = SecurityEntityValueObjectConverter.toResourceVO(resource);
		assertEquals("result.id did not match",expResult.getId(), result.getId());
		assertEquals("result.name did not match",expResult.getName(), result.getName());
		assertEquals("result.description did not match",expResult.getDescription(), result.getDescription());		
	}
	
	@Test
	public void testToAction() throws Exception
	{
		System.out.println("toAction");
		ActionVO actionVO = com.rdonasco.security.utils.SecurityEntityValueObjectDataUtility.createTestDataActionVO();
		Action expResult = new Action();
		expResult.setId(actionVO.getId());
		expResult.setName(actionVO.getName());
		expResult.setDescription(actionVO.getDescription());
		Action result = SecurityEntityValueObjectConverter.toAction(actionVO);
		assertEquals("result.id did not match",expResult.getId(), result.getId());
		assertEquals("result.name did not match",expResult.getName(), result.getName());
		assertEquals("result.description did not match",expResult.getDescription(), result.getDescription());		
		
	}
	
	@Test
	public void testToActionVO() throws Exception
	{
		System.out.println("toAtionVO");
		Action action = com.rdonasco.security.utils.SecurityEntityValueObjectDataUtility.createTestDataAction();
		ActionVO expResult = new ActionVO();
		expResult.setId(action.getId());
		expResult.setName(action.getName());
		expResult.setDescription(action.getDescription());
		ActionVO result = SecurityEntityValueObjectConverter.toActionVO(action);	
		assertEquals("result.id did not match",expResult.getId(), result.getId());
		assertEquals("result.name did not match",expResult.getName(), result.getName());
		assertEquals("result.description did not match",expResult.getDescription(), result.getDescription());				
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
		assertEquals(expResult.getId(),result.getId());
		assertEquals(expResult.getCapability(),result.getCapability());
	}
	
	@Test
	public void testToCapabilityActionVO() throws Exception
	{
		System.out.println("toCapabilityActionVO");

		Capability capability = SecurityEntityValueObjectDataUtility.createTestDataCapabilityOnResourceAndAction("cab", "edit");
		
		for(CapabilityAction capabilityAction : capability.getActions())
		{
			CapabilityActionVO capabilityActionVO = SecurityEntityValueObjectConverter.toCapabilityActionVO(capabilityAction);
			assertEquals(capabilityAction.getId(),capabilityActionVO.getId());
			assertEquals(capabilityAction.getAction().getId(),capabilityActionVO.getActionVO().getId());
			assertEquals(capabilityAction.getCapability().getId(),capabilityActionVO.getCapabilityVO().getId());
		}

	}
	
	@Test
	public void testToCapabilityAction() throws Exception
	{
		System.out.println("toCapabilityAction");
		CapabilityVO capabilityVO = SecurityEntityValueObjectDataUtility.createTestDataCapabilityVO();
		for(CapabilityActionVO capabilityActionVO : capabilityVO.getActions())
		{
			CapabilityAction capabilityAction = SecurityEntityValueObjectConverter.toCapabilityAction(capabilityActionVO);
			assertNotNull("action is null",capabilityAction.getAction());
			assertEquals(capabilityActionVO.getActionVO().getId(),capabilityAction.getAction().getId());

		}
		
	}
	
	@Test
	public void testToUserCapabilityVO() throws Exception
	{
		System.out.println("toUserCapabilityVO");
		Action action = SecurityEntityValueObjectDataUtility.createTestDataAction();
		Resource resource = SecurityEntityValueObjectDataUtility.createTestDataResource();		
		UserCapability userCapability = SecurityEntityValueObjectDataUtility.createTestDataUserCapabilityWithResourceAndAction(resource,action);
		UserCapabilityVO userCapabilityVO = SecurityEntityValueObjectConverter.toUserCapabilityVO(userCapability);
		assertNotNull(userCapabilityVO);
		assertEquals(userCapability.getId(),userCapabilityVO.getId());
	}
	
	@Test
	public void testToUserProfileVO() throws Exception
	{
		System.out.println("toUserProfileVO");		
		UserSecurityProfile userSecurityProfile 
				= SecurityEntityValueObjectDataUtility.createTestDataUserSecurityProfileWithCapability("edit","User");
		UserSecurityProfileVO userSecurityProfileVO = SecurityEntityValueObjectConverter.toUserProfileVO(userSecurityProfile);
		assertNotNull(userSecurityProfileVO);
		assertEquals(userSecurityProfile.getId(),userSecurityProfileVO.getId());
	}

}
