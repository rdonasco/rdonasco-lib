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
import com.rdonasco.security.vo.CapabilityActionVOBuilder;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.CapabilityVOBuilder;
import com.rdonasco.security.vo.ResourceVO;
import com.rdonasco.security.vo.ResourceVOBuilder;
import com.rdonasco.security.vo.RoleCapabilityVO;
import com.rdonasco.security.vo.RoleCapabilityVOBuilder;
import com.rdonasco.security.vo.RoleVO;
import com.rdonasco.security.vo.RoleVOBuilder;
import com.rdonasco.security.vo.UserCapabilityVO;
import com.rdonasco.security.vo.UserCapabilityVOBuilder;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import com.rdonasco.security.vo.UserSecurityProfileVOBuilder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Roy F. Donasco
 */
public class SecurityEntityValueObjectDataUtility
{

	private static final Logger LOG = Logger.getLogger(SecurityEntityValueObjectDataUtility.class.getName());

	public static List<CapabilityActionVO> createTestDataCapabilityActionVOsFor(
			CapabilityVO capability)
	{
		List<ActionVO> actions = createTestDataActionVOList();
		List<CapabilityActionVO> capabilityActions = new ArrayList<CapabilityActionVO>(actions.size());
		for (ActionVO action : actions)
		{
			capabilityActions.add(new CapabilityActionVOBuilder()
					.setActionVO(action).setCapabilityVO(capability).createCapabilityActionVO());
		}
		return capabilityActions;
	}

	public static List<CapabilityAction> createTestDataCapabilityActionsForWithActionName(
			Capability capability, String actionName)
	{
		List<CapabilityAction> actions = new ArrayList<CapabilityAction>(1);
		CapabilityAction capabilityAction = createTestDataCapabilityActionForWithActionName(actionName, capability);
		actions.add(capabilityAction);
		return actions;
	}

	public static List<CapabilityAction> createTestDataCapabilityActionsForWithSize(
			Capability capability, int size)
	{
		List<CapabilityAction> actions = new ArrayList<CapabilityAction>(size);
		Action action;
		CapabilityAction capabilityAction;
		for (int i = 0; i < size; i++)
		{
			action = new Action();
			action.setId(Long.MIN_VALUE + i);
			action.setName("action name " + i);

			capabilityAction = new CapabilityAction();
			capabilityAction.setId(Long.MIN_VALUE + i);
			capabilityAction.setAction(action);
			capabilityAction.setCapability(capability);
			actions.add(capabilityAction);
		}
		return actions;
	}

	public static CapabilityAction createTestDataCapabilityActionForWithActionName(
			String actionName,
			Capability capability)
	{
		Action action;
		CapabilityAction capabilityAction;
		action = new Action();
		action.setId(generateRandomID());
		action.setName(actionName);
		capabilityAction = new CapabilityAction();
		capabilityAction.setId(generateRandomID());
		capabilityAction.setAction(action);
		capabilityAction.setCapability(capability);
		return capabilityAction;
	}

	static UserSecurityProfile createTestDataUserSecurityProfileWithCapability(
			String action,
			String resource)
	{
		UserSecurityProfile userSecurityProfile = new UserSecurityProfile();
		Capability capability = createTestDataCapabilityOnResourceAndAction(resource, action);
		UserCapability userCapability = new UserCapability();
		userCapability.setCapability(capability);
		List<UserCapability> capabilities = new ArrayList<UserCapability>();
		userCapability.setCapability(capability);
		userCapability.setUserProfile(userSecurityProfile);
		capabilities.add(userCapability);
		userSecurityProfile.setCapabilities(capabilities);
		userSecurityProfile.setRegistrationToken("token");
		userSecurityProfile.setRegistrationTokenExpiration(new Date());

		return userSecurityProfile;
	}

	static UserCapability createTestDataUserCapabilityWithResourceAndAction(
			Resource resource,
			Action action)
	{

		Capability capability = new Capability();
		capability.setTitle("dummy capability");
		capability.setId(generateRandomID());
		capability.setDescription("dummy capability");
		capability.setResource(resource);
		capability.setActions(new ArrayList<CapabilityAction>());
		CapabilityAction capabilityAction = createTestDataCapabilityActionForWithActionName("edit", capability);
		capabilityAction.setAction(action);
		capability.getActions().add(capabilityAction);
		UserCapability userCapability = new UserCapability();
		userCapability.setCapability(capability);
		userCapability.setId(generateRandomID());
		return userCapability;

	}

	static RoleVO createTestRoleVO(String roleName, String... capabilities)
	{
		RoleVOBuilder roleVoBuilder = new RoleVOBuilder()
				.setId(generateRandomID())
				.setName(roleName);
		for (String capabilityName : capabilities)
		{
			roleVoBuilder.addCapability(createTestDataCapabilityVO());
		}
		return roleVoBuilder.createUserRoleVO();
	}

	private static CapabilityVO createTestCapabilityVOWithResourceActionAndTitle(
			ResourceVO resourceVO, ActionVO actionVO,
			final String capabilityTitle)
	{
		CapabilityVO capability = new CapabilityVOBuilder()
				.setId(Long.MIN_VALUE)
				.setResource(resourceVO)
				.addAction(actionVO)
				.setTitle(capabilityTitle)
				.setDescription(capabilityTitle)
				.createCapabilityVO();
		return capability;
	}

	static RoleCapabilityVO createTestRoleCapabilityVO()
	{
		RoleCapabilityVOBuilder builder = new RoleCapabilityVOBuilder()
				.setId(generateRandomID())
				.setRoleVO(createTestRoleVO("Talent Manager"));

		return builder.createRoleCapabilityVO();
	}

	public SecurityEntityValueObjectDataUtility()
	{
	}

	public static UserCapabilityVO createTestDataUserCapabilityVO(
			CapabilityVO capability)
	{
		CapabilityVO capabilityVO = createTestDataCapabilityVO();
		UserCapabilityVO userCapability = new UserCapabilityVOBuilder()
				.setCapability(capability)
				.setId(generateRandomID())
				.createUserCapabilityVO();
		return userCapability;
	}

	public static ActionVO createTestDataActionVO()
	{
		ActionVO action = new ActionVO();
		action.setId(Long.MIN_VALUE);
		action.setName("action");
		return action;
	}

	public static List<ActionVO> createTestDataActionVOList()
	{
		List<ActionVO> actions = new ArrayList<ActionVO>();
		ActionVO action = createTestDataActionVO();
		actions.add(action);
		return actions;
	}

	public static CapabilityVO createTestDataCapabilityVO()
	{
		ResourceVO resourceVO = new ResourceVOBuilder()
				.setId(Long.MIN_VALUE)
				.setName("resource")
				.createResourceVO();
		ActionVO actionVO = createTestDataActionVO();
		final String capabilityTitle = "testCapability";
		CapabilityVO capability = createTestCapabilityVOWithResourceActionAndTitle(resourceVO, actionVO, capabilityTitle);
		List<CapabilityActionVO> capabilityActions = createTestDataCapabilityActionVOsFor(capability);
		capability.setActions(capabilityActions);
		return capability;
	}

//	public static Capability createTestDataCapabilityForResourceAndAction(String resourceName, String actionName)
//	{
//		Resource resource = createTestDataResource(resourceName);
//		Action action = createTestDataActionWithName(actionName);
//		Capability capability = new Capability();
//		capability.setId(Long.MAX_VALUE);
//		capability.setTitle("Test Capability");
//		capability.setDescription("Test Capability Description");
//		capability.setActions(new ArrayList<CapabilityAction>());
//		capability.setResource(resource);
//		CapabilityAction capabilityAction = new CapabilityAction();
//		capabilityAction.setAction(action);
//		capabilityAction.setCapability(capability);
//		capabilityAction.setId(Long.MAX_VALUE);
//		capability.getActions().add(capabilityAction);
//		return capability;
//	}
	public static UserSecurityProfileVO createTestDataUserSecurityProfileVOwithUserCapability(
			UserCapabilityVO userCapability)
	{
		UserSecurityProfileVOBuilder userSecurityProfileVOBuilder = new UserSecurityProfileVOBuilder();
		userSecurityProfileVOBuilder
				.setId(Long.MIN_VALUE)
				.setLoginId("testLoginID")
				.setPassword("testPassword")
				.setRegistrationToken("token")
				.setRegistrationTokenExpiry(new Date())
				.addCapability(userCapability);
		UserSecurityProfileVO userProfileVO = userSecurityProfileVOBuilder.createUserSecurityProfileVO();
		userCapability.setUserProfile(userProfileVO);
		return userProfileVO;
	}

	public static UserSecurityProfileVO createTestDataUserSecurityProfileVOwithoutCapability()
	{
		UserSecurityProfileVO userSecurityProfileVO = new UserSecurityProfileVOBuilder()
				.setId(Long.MIN_VALUE)
				.setLoginId("testLoginID")
				.setRegistrationToken("token")
				.setRegistrationTokenExpiry(new Date())
				.createUserSecurityProfileVO();
		return userSecurityProfileVO;
	}

	public static Capability createTestDataCapabilityOnResourceAndAction(
			String resourceName,
			String actionName)
	{
		Capability capability = new Capability();
		capability.setId(generateRandomID());
		capability.setTitle("test capability");
		capability.setDescription("test capability");

		if (null == actionName)
		{
			capability.setActions(createTestDataCapabilityActionsForWithSize(capability, 3));
		}
		else
		{
			capability.setActions(createTestDataCapabilityActionsForWithActionName(capability, actionName));
		}

		Resource resource = createTestDataResource(resourceName);
		capability.setResource(resource);

		return capability;
	}

	public static Action createTestDataAction()
	{
		Action action = new Action();
		action.setName("action");
		Long randomID = generateRandomID();
		LOG.log(Level.INFO, "generated random ID={0}", randomID);
		action.setId(randomID);
		return action;
	}

	public static Action createTestDataActionWithName(String actionName)
	{
		Action action = createTestDataAction();
		action.setName(actionName);
		return action;
	}
	private static long MIN_SEED = 1;

	private static long MAX_SEED = Long.MAX_VALUE;

	public static long generateRandomID()
	{
		long randomID = Math.round(Math.random() * (MAX_SEED - MIN_SEED) + MIN_SEED);
		return randomID;
	}

	public static Resource createTestDataResource()
	{
		return createTestDataResource(null);
	}

	public static Resource createTestDataResource(String resourceName)
	{
		Resource resource = new Resource();
		resource.setId(generateRandomID());
		resource.setName(resourceName == null ? "test resource" : resourceName);
		return resource;
	}

	public static ResourceVO createTestDataResourceVO()
	{
		ResourceVO resourceVO = new ResourceVOBuilder()
				.setId(generateRandomID())
				.setName("test resourceVO")
				.createResourceVO();
		return resourceVO;
	}

	public static List<Action> createTestDataActions()
	{
		List<Action> actions = new ArrayList<Action>();
		Action action = createTestDataAction();
		actions.add(action);
		return actions;
	}
}
