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
import com.rdonasco.security.vo.UserCapabilityVO;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.BeanUtilsBean2;

/**
 *
 * @author Roy F. Donasco
 */
public class SecurityEntityValueObjectConverter
{

	static
	{
		BeanUtilsBean.setInstance(new BeanUtilsBean2());
	}
	static final BeanUtilsBean BEAN_UTILS = BeanUtilsBean.getInstance();

	public static UserSecurityProfile toUserProfile(
			UserSecurityProfileVO userSecurityProfileVO)
			throws IllegalAccessException, InvocationTargetException
	{
		UserSecurityProfile userSecurityProfile = new UserSecurityProfile();
		BEAN_UTILS.copyProperties(userSecurityProfile, userSecurityProfileVO);
		if (null == userSecurityProfileVO.getId())
		{
			userSecurityProfile.setId(userSecurityProfileVO.getId());
		}
		userSecurityProfile.setCapabilities(new ArrayList<UserCapability>(userSecurityProfileVO.getCapabilityVOList().size()));
		UserCapability userCapability = null;
		Capability capability = null;
		for (UserCapabilityVO userCapabilityVO : userSecurityProfileVO.getCapabilityVOList())
		{
			userCapability = new UserCapability();
			capability = new Capability();
			userCapability.setCapability(capability);
			userCapability.setUserProfile(userSecurityProfile);
			userCapability.setId(userCapabilityVO.getId());
			Resource resource = new Resource();
			BEAN_UTILS.copyProperties(resource, userCapabilityVO.getCapability().getResource());
			capability.setResource(resource);
			capability.setDescription(userCapabilityVO.getCapability().getDescription());
			capability.setId(userCapabilityVO.getCapability().getId());
			capability.setTitle(userCapabilityVO.getCapability().getTitle());
			List<CapabilityAction> actions = new ArrayList<CapabilityAction>();
			CapabilityAction capabilityAction;
			for (CapabilityActionVO capabilityActionVO : userCapabilityVO.getCapability().getActions())
			{
				capabilityAction = toCapabilityAction(capabilityActionVO);

				actions.add(capabilityAction);
			}
			capability.setActions(actions);

			userSecurityProfile.getCapabilities().add(userCapability);
		}
		return userSecurityProfile;
	}

	public static CapabilityVO toCapabilityVO(Capability capability)
			throws IllegalAccessException, InvocationTargetException
	{
		CapabilityVO capabilityVO = null;
		if (null != capability)
		{
			CapabilityVOBuilder capabilityVOBuilder = new CapabilityVOBuilder()
					.setId(capability.getId())
					.setTitle(capability.getTitle())
					.setDescription(capability.getDescription())
					.setResource(toResourceVO(capability.getResource()));
			if (capability.getActions() != null)
			{
				List<CapabilityActionVO> actionVOList = new ArrayList<CapabilityActionVO>(capability.getActions().size());
				CapabilityActionVO capabilityActionVO;
				for (CapabilityAction capabilityAction : capability.getActions())
				{
					CapabilityAction actionCapabilityToConvert = new CapabilityAction();
					actionCapabilityToConvert.setAction(capabilityAction.getAction());
					actionCapabilityToConvert.setId(capabilityAction.getId());
					capabilityActionVO = toCapabilityActionVO(actionCapabilityToConvert);
					capabilityActionVO.setCapabilityVO(capabilityVO);
					actionVOList.add(capabilityActionVO);
				}
				capabilityVOBuilder.setActions(actionVOList);
				capabilityVO = capabilityVOBuilder.createCapabilityVO();
			}
		}
		return capabilityVO;
	}

	public static Capability toCapability(CapabilityVO capabilityVO) throws
			IllegalAccessException, InvocationTargetException
	{
		Capability capability = new Capability();
		capability.setDescription(capabilityVO.getDescription());
		capability.setId(capabilityVO.getId());
		capability.setResource(toResource(capabilityVO.getResource()));
		capability.setTitle(capabilityVO.getTitle());		
		if (capabilityVO.getActions() != null)
		{
			List<CapabilityAction> capabilityActions = new ArrayList<CapabilityAction>(capabilityVO.getActions().size());
			CapabilityAction capabilityAction;
			for (CapabilityActionVO capabilityActionVO : capabilityVO.getActions())
			{
				capabilityAction = toCapabilityAction(capabilityActionVO);
				capabilityAction.setCapability(capability);
				capabilityActions.add(capabilityAction);
			}
			capability.setActions(capabilityActions);
		}


		return capability;
	}

	public static Resource toResource(ResourceVO resourceVO)
			throws IllegalAccessException, InvocationTargetException
	{
		Resource resource = new Resource();
		BEAN_UTILS.copyProperties(resource, resourceVO);
		return resource;
	}

	public static ResourceVO toResourceVO(Resource resource) throws
			IllegalAccessException, InvocationTargetException
	{
		ResourceVO resourceVO = new ResourceVOBuilder()
				.createResourceVO();
		BEAN_UTILS.copyProperties(resourceVO, resource);
		return resourceVO;
	}

	public static Action toAction(ActionVO actionVO) throws
			IllegalAccessException, InvocationTargetException
	{
		Action action = new Action();
		BEAN_UTILS.copyProperties(action, actionVO);
		if (null == actionVO.getId())
		{
			action.setId(null);
		}
		return action;
	}

	public static ActionVO toActionVO(Action action) throws
			IllegalAccessException, InvocationTargetException
	{
		ActionVO actionVO = new ActionVO();
		BEAN_UTILS.copyProperties(actionVO, action);
		if (null == action.getId())
		{
			actionVO.setId(null);
		}
		return actionVO;
	}

	public static UserCapability toUserCapability(
			UserCapabilityVO userCapabilityVO)
			throws IllegalAccessException, InvocationTargetException
	{
		UserCapability userCapability = new UserCapability();
		userCapability.setId(userCapabilityVO.getId());
		userCapability.setCapability(toCapability(userCapabilityVO.getCapability()));
		if (null != userCapabilityVO.getUserProfile())
		{
			userCapability.setUserProfile(toUserProfile(userCapabilityVO.getUserProfile()));
		}
		return userCapability;
	}

	public static CapabilityAction toCapabilityAction(
			CapabilityActionVO capabilityActionVO)
			throws IllegalAccessException, InvocationTargetException
	{
		CapabilityAction capabilityAction = new CapabilityAction();
		BEAN_UTILS.copyProperties(capabilityAction, capabilityActionVO);
		capabilityAction.setAction(toAction(capabilityActionVO.getActionVO()));
//		if(null != capabilityActionVO.getCapabilityVO())
//		{
//			capabilityAction.setCapability(toCapability(capabilityActionVO.getCapabilityVO()));
//		}
		return capabilityAction;
	}

	public static CapabilityActionVO toCapabilityActionVO(
			CapabilityAction capabilityAction)
			throws IllegalAccessException, InvocationTargetException
	{

		CapabilityActionVO capabilityActionVO = new CapabilityActionVOBuilder()
				.setActionVO(toActionVO(capabilityAction.getAction()))
				.setId(capabilityAction.getId())
				.createCapabilityActionVO();
		if (null != capabilityAction)
		{
			CapabilityVO capabilityVO = toCapabilityVO(capabilityAction.getCapability());
			capabilityActionVO.setCapabilityVO(capabilityVO);
		}
		return capabilityActionVO;
	}
}
