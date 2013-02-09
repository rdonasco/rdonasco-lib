/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.services.utils;

import com.rdonasco.security.services.*;
import com.rdonasco.security.model.Action;
import com.rdonasco.security.model.Capability;
import com.rdonasco.security.model.Resource;
import com.rdonasco.security.model.UserCapability;
import com.rdonasco.security.model.UserSecurityProfile;
import com.rdonasco.security.vo.ActionVO;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.ResourceVO;
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
public class SecurityEntityValueObjectDataUtil
{

	public SecurityEntityValueObjectDataUtil()
	{
	}
	private static final Logger LOG = Logger.getLogger(SecurityEntityValueObjectDataUtil.class.getName());

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
		ResourceVO resourceVO = new ResourceVO();
		resourceVO.setId(Long.MIN_VALUE);
		resourceVO.setDescription("resource");
		resourceVO.setName("resource");
		CapabilityVO capability = new CapabilityVO();
		capability.setId(Long.MIN_VALUE);
		capability.setResource(resourceVO);
		List<ActionVO> actions = createTestDataActionVOList();
		capability.setActions(actions);
		return capability;
	}

	public static UserSecurityProfileVO createTestDataUserSecurityProfileVOwithUserCapability(UserCapabilityVO userCapability)
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

	public static Capability createTestDataCapability()
	{
		Capability capability = new Capability();
		capability.setId(generateRandomID());
		capability.setTitle("test capability");
		capability.setDescription("test capability");
		capability.setActions(new ArrayList<Action>());
		Action action = createTestDataAction();
		capability.getActions().add(action);
		
		Resource resource = createTestDataResource();
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

	private static long MIN_SEED = 1;
	private static long MAX_SEED = Long.MAX_VALUE;
	public static long generateRandomID()
	{
		long randomID = Math.round(Math.random() * (MAX_SEED-MIN_SEED) + MIN_SEED);
		return randomID;
	}

	public static Resource createTestDataResource()
	{
		Resource resource = new Resource();
		resource.setId(generateRandomID());
		resource.setDescription("test resource");
		resource.setName("test resource");
		return resource;
	}

	public static ResourceVO createTestDataResourceVO()
	{
		ResourceVO resourceVO = new ResourceVO();
		resourceVO.setId(generateRandomID());
		resourceVO.setName("test resourceVO");
		resourceVO.setDescription("test resourceVO description");
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
