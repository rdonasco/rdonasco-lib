/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.utils;

import com.rdonasco.security.model.Action;
import com.rdonasco.security.model.Capability;
import com.rdonasco.security.model.CapabilityAction;
import com.rdonasco.security.model.Resource;
import com.rdonasco.security.vo.ActionVO;
import com.rdonasco.security.vo.CapabilityActionVO;
import com.rdonasco.security.vo.CapabilityActionVOBuilder;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.CapabilityVOBuilder;
import com.rdonasco.security.vo.ResourceVO;
import com.rdonasco.security.vo.ResourceVOBuilder;
import com.rdonasco.security.vo.UserCapabilityVO;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import com.rdonasco.security.vo.UserSecurityProfileVOBuilder;
import java.util.ArrayList;
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
	
	public static List<CapabilityAction> createTestDataActionsForWithActionName(
			Capability capability, String actionName)
	{
		List<CapabilityAction> actions = new ArrayList<CapabilityAction>(1);
		CapabilityAction capabilityAction = createTestDataCapabilityActionForWithActionName(actionName, capability);
		actions.add(capabilityAction);
		return actions;
	}

	public static List<CapabilityAction> createTestDataActionsForWithSize(
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
			action.setDescription("action description " + i);

			capabilityAction = new CapabilityAction();
			capabilityAction.setId(Long.MIN_VALUE + i);
			capabilityAction.setAction(action);
			capabilityAction.setCapability(capability);
			actions.add(capabilityAction);
		}
		return actions;
	}

	public static CapabilityAction createTestDataCapabilityActionForWithActionName(String actionName,
			Capability capability)
	{
		Action action;
		CapabilityAction capabilityAction;
		action = new Action();
		action.setId(Long.MIN_VALUE);
		action.setName(actionName);
		action.setDescription("action description for " + actionName);
		capabilityAction = new CapabilityAction();
		capabilityAction.setId(Long.MIN_VALUE);
		capabilityAction.setAction(action);
		capabilityAction.setCapability(capability);
		return capabilityAction;
	}

	public SecurityEntityValueObjectDataUtility()
	{
	}

	public static UserCapabilityVO createTestDataUserCapabilityVO(
			CapabilityVO capability)
	{
		UserCapabilityVO userCapability = new UserCapabilityVO();
		userCapability.setId(Long.MIN_VALUE);
		userCapability.setCapability(capability);
		return userCapability;
	}

	public static ActionVO createTestDataActionVO()
	{
		ActionVO action = new ActionVO();
		action.setId(Long.MIN_VALUE);
		action.setName("action");
		action.setDescription("action");
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
				.setDescription("resource")
				.createResourceVO();
		ActionVO actionVO = createTestDataActionVO();
		CapabilityVO capability = new CapabilityVOBuilder()
				.setId(Long.MIN_VALUE)
				.setResource(resourceVO)
				.addAction(actionVO)
				.setTitle("testCapability")
				.setDescription("testCapability")
				.createCapabilityVO();
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
				.createUserSecurityProfileVO();
		return userSecurityProfileVO;
	}

	public static Capability createTestDataCapabilityOnResourceAndAction(String resourceName,
			String actionName)
	{
		Capability capability = new Capability();
		capability.setId(generateRandomID());
		capability.setTitle("test capability");
		capability.setDescription("test capability");

		if (null == actionName)
		{
			capability.setActions(createTestDataActionsForWithSize(capability, 3));
		}
		else
		{
			capability.setActions(createTestDataActionsForWithActionName(capability, actionName));
		}

		Resource resource = createTestDataResource(resourceName);
		capability.setResource(resource);

		return capability;
	}

	public static Action createTestDataAction()
	{
		Action action = new Action();
		action.setDescription("test action");
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
		resource.setDescription("test resource");
		resource.setName(resourceName == null ? "test resource" : resourceName);
		return resource;
	}

	public static ResourceVO createTestDataResourceVO()
	{
		ResourceVO resourceVO = new ResourceVOBuilder()
				.setId(generateRandomID())
				.setName("test resourceVO")
				.setDescription("test resourceVO description")
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
