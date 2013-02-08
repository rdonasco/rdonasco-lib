/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.services;

import com.rdonasco.security.model.Action;
import com.rdonasco.security.model.Capability;
import com.rdonasco.security.model.Resource;
import com.rdonasco.security.model.UserCapability;
import com.rdonasco.security.model.UserSecurityProfile;
import com.rdonasco.security.services.utils.SecurityEntityValueObjectDataUtil;
import com.rdonasco.security.vo.ActionVO;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.ResourceVO;
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
		UserSecurityProfileVO userSecurityProfileVO = SecurityEntityValueObjectDataUtil.createTestDataUserSecurityProfileVOwithoutCapability();
		UserSecurityProfile result = SecurityEntityValueObjectConverter.toUserProfile(userSecurityProfileVO);
		assertNotNull(result);
		assertNotNull(result.getId());
		assertEquals(new Long(Long.MIN_VALUE), result.getId());

	}

	@Test
	public void testToUserProfileWithCapability() throws Exception
	{
		System.out.println("toUserProfileWithCapability");
		CapabilityVO capability = SecurityEntityValueObjectDataUtil.createTestDataCapabilityVO();
		UserCapabilityVO userCapability = SecurityEntityValueObjectDataUtil.createTestDataUserCapabilityVO(capability);
		UserSecurityProfileVO userProfileVO = SecurityEntityValueObjectDataUtil.createTestDataUserSecurityProfileVOwithUserCapability(userCapability);
		UserSecurityProfile resultingUserSecurityProfile = SecurityEntityValueObjectConverter
				.toUserProfile(userProfileVO);
		assertNotNull(resultingUserSecurityProfile);
		assertEquals("id did not match", userProfileVO.getId(), resultingUserSecurityProfile.getId());
		assertEquals("login id did not match", userProfileVO.getLoginId(), resultingUserSecurityProfile.getLoginId());
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
		Capability testCapability = SecurityEntityValueObjectDataUtil.createTestDataCapability();
		List<Action> actions = SecurityEntityValueObjectDataUtil.createTestDataActions();
		testCapability.setActions(actions);
		CapabilityVO expResult = new CapabilityVO();
		expResult.setId(testCapability.getId());
		expResult.setTitle(testCapability.getTitle());
		expResult.setDescription(testCapability.getDescription());		
		CapabilityVO result = SecurityEntityValueObjectConverter.toCapabilityVO(testCapability);
		assertEquals("id did not match",expResult.getId(), result.getId());
		assertEquals("actions.size did not match",1, result.getActions().size());
		assertEquals("description did not match",expResult.getDescription(), result.getDescription());
		assertEquals("title did not match",expResult.getTitle(), result.getTitle());	
		assertNotNull("resource not set",result.getResource());
	}
	
	
	@Test
	public void testToCapability() throws Exception
	{
		System.out.println("toCapability");
		CapabilityVO testCapabilityVO = SecurityEntityValueObjectDataUtil.createTestDataCapabilityVO();
		List<ActionVO> actions = SecurityEntityValueObjectDataUtil.createTestDataActionVOList();
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
	}

	/**
	 * Test of toResource method, of class SecurityEntityValueObjectConverter.
	 */
	@Test
	public void testToResource() throws Exception
	{
		System.out.println("toResource");
		ResourceVO resourceVO = SecurityEntityValueObjectDataUtil.createTestDataResourceVO();
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
		Resource resource = SecurityEntityValueObjectDataUtil.createTestDataResource();
		ResourceVO expResult = new ResourceVO();
		expResult.setId(resource.getId());
		expResult.setName(resource.getName());
		expResult.setDescription(resource.getDescription());
		ResourceVO result = SecurityEntityValueObjectConverter.toResourceVO(resource);
		assertEquals("result.id did not match",expResult.getId(), result.getId());
		assertEquals("result.name did not match",expResult.getName(), result.getName());
		assertEquals("result.description did not match",expResult.getDescription(), result.getDescription());		
	}
	
	@Test
	public void testToAction() throws Exception
	{
		System.out.println("toAction");
		ActionVO actionVO = SecurityEntityValueObjectDataUtil.createTestDataActionVO();
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
		Action action = SecurityEntityValueObjectDataUtil.createTestDataAction();
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
		CapabilityVO capabilityVO = SecurityEntityValueObjectDataUtil.createTestDataCapabilityVO();
		UserCapabilityVO userCapabilityVO = SecurityEntityValueObjectDataUtil.createTestDataUserCapabilityVO(capabilityVO);
		UserCapability expResult = new UserCapability();
		expResult.setId(userCapabilityVO.getId());
		expResult.setCapability(SecurityEntityValueObjectConverter.toCapability(capabilityVO));
		UserCapability result = SecurityEntityValueObjectConverter.toUserCapability(userCapabilityVO);
		assertEquals(expResult.getId(),result.getId());
		assertEquals(expResult.getCapability(),result.getCapability());
	}

//	private UserCapabilityVO createTestDataUserCapabilityVO(
//			CapabilityVO capability)
//	{
//		UserCapabilityVO userCapability = new UserCapabilityVO();
//		userCapability.setId(Long.MIN_VALUE);
//		userCapability.setCapability(capability);
//		return userCapability;
//	}
//
//	private ActionVO createTestDataActionVO()
//	{
//		ActionVO action = new ActionVO();
//		action.setId(Long.MIN_VALUE);
//		action.setName("action");
//		action.setDescription("action");
//		return action;
//	}
//
//	private List<ActionVO> createTestDataActionVOList()
//	{
//		List<ActionVO> actions = new ArrayList<ActionVO>();
//		ActionVO action = createTestDataActionVO();
//		actions.add(action);
//		return actions;
//	}
//
//	private CapabilityVO createTestDataCapabilityVO()
//	{
//		ResourceVO resourceVO = new ResourceVO();
//		resourceVO.setId(Long.MIN_VALUE);
//		resourceVO.setDescription("resource");
//		resourceVO.setName("resource");
//		CapabilityVO capability = new CapabilityVO();
//		capability.setId(Long.MIN_VALUE);
//		capability.setResource(resourceVO);
//		List<ActionVO> actions = createTestDataActionVOList();
//		capability.setActions(actions);
//		return capability;
//	}
//
//	private UserSecurityProfileVO createTestDataUserSecurityProfileVOwithUserCapability(UserCapabilityVO userCapability)
//	{
//		UserSecurityProfileVOBuilder userSecurityProfileVOBuilder = new UserSecurityProfileVOBuilder();
//		userSecurityProfileVOBuilder
//				.setId(Long.MIN_VALUE)
//				.setLoginId(anyString())
//				.addCapability(userCapability);
//		UserSecurityProfileVO userProfileVO = userSecurityProfileVOBuilder.createUserSecurityProfileVO();
//		userCapability.setUserProfile(userProfileVO);
//		return userProfileVO;
//	}
//
//	private UserSecurityProfileVO createTestDataUserSecurityProfileVOwithoutCapability()
//	{
//		UserSecurityProfileVO userSecurityProfileVO = new UserSecurityProfileVOBuilder()
//				.setId(Long.MIN_VALUE)
//				.setLoginId(anyString())
//				.createUserSecurityProfileVO();
//		return userSecurityProfileVO;
//	}
//
//	private Capability createTestDataCapability()
//	{
//		Capability capability = new Capability();
//		capability.setId(generateRandomID());
//		capability.setTitle("test capability");
//		capability.setDescription("test capability");
//		capability.setActions(new ArrayList<Action>());
//		Action action = createTestDataAction();
//		capability.getActions().add(action);
//		
//		Resource resource = createTestDataResource();
//		capability.setResource(resource);
//		
//		return capability;
//	}
//
//	private Action createTestDataAction()
//	{
//		Action action = new Action();
//		action.setDescription("test action");
//		action.setName("action");
//		Long randomID = generateRandomID();
//		LOG.log(Level.INFO, "generated random ID={0}", randomID);
//		action.setId(randomID);
//		return action;
//	}
//
//	private static long MIN_SEED = 1;
//	private static long MAX_SEED = Long.MAX_VALUE;
//	private long generateRandomID()
//	{
//		long randomID = Math.round(Math.random() * (MAX_SEED-MIN_SEED) + MIN_SEED);
//		return randomID;
//	}
//
//	private Resource createTestDataResource()
//	{
//		Resource resource = new Resource();
//		resource.setId(generateRandomID());
//		resource.setDescription("test resource");
//		resource.setName("test resource");
//		return resource;
//	}
//
//	private ResourceVO createTestDataResourceVO()
//	{
//		ResourceVO resourceVO = new ResourceVO();
//		resourceVO.setId(generateRandomID());
//		resourceVO.setName("test resourceVO");
//		resourceVO.setDescription("test resourceVO description");
//		return resourceVO;
//	}
//
//	private List<Action> createTestDataActions()
//	{
//		List<Action> actions = new ArrayList<Action>();
//		Action action = createTestDataAction();
//		actions.add(action);
//		return actions;
//	}
}
