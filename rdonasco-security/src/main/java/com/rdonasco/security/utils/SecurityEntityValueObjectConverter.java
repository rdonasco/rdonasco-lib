/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.utils;

import com.rdonasco.security.model.Action;
import com.rdonasco.security.model.Capability;
import com.rdonasco.security.model.Resource;
import com.rdonasco.security.model.UserCapability;
import com.rdonasco.security.model.UserSecurityProfile;
import com.rdonasco.security.vo.ActionVO;
import com.rdonasco.security.vo.CapabilityVO;
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

	public static UserSecurityProfile toUserProfile(UserSecurityProfileVO userSecurityProfileVO) 
			throws IllegalAccessException, InvocationTargetException
	{
		UserSecurityProfile userSecurityProfile = new UserSecurityProfile();
		BEAN_UTILS.copyProperties(userSecurityProfile, userSecurityProfileVO);
		if(null == userSecurityProfileVO.getId())
		{
			userSecurityProfile.setId(userSecurityProfileVO.getId());
		}
		userSecurityProfile.setCapabilities(new ArrayList<UserCapability>(userSecurityProfileVO.getCapabilityVOList().size()));
		UserCapability userCapability = null;
		Capability capability = null;
		for(UserCapabilityVO userCapabilityVO : userSecurityProfileVO.getCapabilityVOList())
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
			List<Action> actions = new ArrayList<Action>();
			Action action = null;
			for(ActionVO actionVO : userCapabilityVO.getCapability().getActions())
			{
				action = new Action();
				BEAN_UTILS.copyProperties(action, actionVO);
				actions.add(action);
			}
			capability.setActions(actions);			
			
			userSecurityProfile.getCapabilities().add(userCapability);
		}
		return userSecurityProfile;
	}

	public static CapabilityVO toCapabilityVO(Capability capability)
			throws IllegalAccessException, InvocationTargetException
	{
		CapabilityVO capabilityVO = new CapabilityVO();
		if(capability.getActions() != null)
		{
			List<ActionVO> actionVOList = new ArrayList<ActionVO>(capability.getActions().size());
			for(Action action : capability.getActions())
			{
				actionVOList.add(toActionVO(action));
			}
			capabilityVO.setActions(actionVOList);			
		}
		capabilityVO.setDescription(capability.getDescription());
		capabilityVO.setId(capability.getId());
		capabilityVO.setResource(toResourceVO(capability.getResource()));
		capabilityVO.setTitle(capability.getTitle());
		
		return capabilityVO;
	}
	
	public static Capability toCapability(CapabilityVO capabilityVO) throws IllegalAccessException, InvocationTargetException
	{
		Capability capability = new Capability();
		if(capabilityVO.getActions() != null)
		{
			List<Action> actions = new ArrayList<Action>(capabilityVO.getActions().size());
			for(ActionVO actionVO : capabilityVO.getActions())
			{
				actions.add(toAction(actionVO));
			}
			capability.setActions(actions);
		}
		capability.setDescription(capabilityVO.getDescription());
		capability.setId(capabilityVO.getId());
		capability.setResource(toResource(capabilityVO.getResource()));
		capability.setTitle(capabilityVO.getTitle());				
		
		return capability;
	}			

	public static Resource toResource(ResourceVO resourceVO) 
			throws IllegalAccessException, InvocationTargetException
	{
		Resource resource = new Resource();
		BEAN_UTILS.copyProperties(resource, resourceVO);
		return resource;
	}

	public static ResourceVO toResourceVO(Resource resource) throws IllegalAccessException, InvocationTargetException
	{
		ResourceVO resourceVO = new ResourceVOBuilder()
				.createResourceVO();
		BEAN_UTILS.copyProperties(resourceVO, resource);
		return resourceVO;
	}

	public static Action toAction(ActionVO actionVO) throws IllegalAccessException, InvocationTargetException
	{
		Action action = new Action();
		BEAN_UTILS.copyProperties(action, actionVO);
		if(null == actionVO.getId())
		{
			action.setId(null);
		}
		return action;
	}

	public static ActionVO toActionVO(Action action) throws IllegalAccessException, InvocationTargetException
	{
		ActionVO actionVO = new ActionVO();
		BEAN_UTILS.copyProperties(actionVO, action);
		if(null == action.getId())
		{
			actionVO.setId(null);
		}
		return actionVO;
	}

	public static UserCapability toUserCapability(UserCapabilityVO userCapabilityVO) 
			throws IllegalAccessException, InvocationTargetException
	{
		UserCapability userCapability = new UserCapability();
		userCapability.setId(userCapabilityVO.getId());
		userCapability.setCapability(toCapability(userCapabilityVO.getCapability()));
		if(null != userCapabilityVO.getUserProfile())
		{
			userCapability.setUserProfile(toUserProfile(userCapabilityVO.getUserProfile()));
		}
		return userCapability;
	}


}
