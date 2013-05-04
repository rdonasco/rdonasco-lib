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
import com.rdonasco.security.model.Role;
import com.rdonasco.security.model.RoleCapability;
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
import com.rdonasco.security.vo.UserCapabilityVO;
import com.rdonasco.security.vo.UserCapabilityVOBuilder;
import com.rdonasco.security.vo.RoleVO;
import com.rdonasco.security.vo.RoleVOBuilder;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import com.rdonasco.security.vo.UserSecurityProfileVOBuilder;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;
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
		userSecurityProfile.setId(userSecurityProfileVO.getId());
		userSecurityProfile.setLogonId(userSecurityProfileVO.getLogonId());
		userSecurityProfile.setPassword(userSecurityProfileVO.getPassword());
		userSecurityProfile.setRegistrationToken(userSecurityProfileVO.getRegistrationToken());
		userSecurityProfile.setRegistrationTokenExpiration(userSecurityProfileVO.getRegistrationTokenExpiration());
		if (null == userSecurityProfileVO.getId())
		{
			userSecurityProfile.setId(userSecurityProfileVO.getId());
		}
		userSecurityProfile.setCapabilities(new ArrayList<UserCapability>(userSecurityProfileVO.getCapabilities().size()));
		UserCapability userCapability = null;
		Capability capability = null;
		for (UserCapabilityVO userCapabilityVO : userSecurityProfileVO.getCapabilities())
		{
			userCapability = new UserCapability();
			capability = new Capability();
			userCapability.setCapability(capability);
			userCapability.setUserProfile(userSecurityProfile);
			userCapability.setId(userCapabilityVO.getId());
			Resource resource = new Resource();
			if (null != userCapabilityVO.getCapability().getResource())
			{
				BEAN_UTILS.copyProperties(resource, userCapabilityVO.getCapability().getResource());
			}
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
					.setDescription(capability.getDescription());
			if (null != capability.getResource())
			{
				capabilityVOBuilder.setResource(toResourceVO(capability.getResource()));
			}
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
		if (null != capabilityVO.getResource())
		{
			capability.setResource(toResource(capabilityVO.getResource()));
		}
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

	public static List<ResourceVO> toResourceVOList(List<Resource> resources)
			throws IllegalAccessException, InvocationTargetException
	{
		List<ResourceVO> resourceVoList = new ArrayList<ResourceVO>(resources.size());
		for (Resource resource : resources)
		{
			resourceVoList.add(toResourceVO(resource));
		}
		return resourceVoList;
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

	public static List<ActionVO> toActionVOList(List<Action> actions) throws
			IllegalAccessException, InvocationTargetException
	{
		List<ActionVO> actionVOList = new ArrayList<ActionVO>(actions.size());
		for (Action action : actions)
		{
			actionVOList.add(toActionVO(action));
		}
		return actionVOList;
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
		CapabilityVO capabilityVO = toCapabilityVO(capabilityAction.getCapability());
		capabilityActionVO.setCapabilityVO(capabilityVO);
		return capabilityActionVO;
	}

	public static UserSecurityProfileVO toUserProfileVO(
			UserSecurityProfile profileToConvert) throws IllegalAccessException,
			InvocationTargetException
	{
		UserCapabilityVO userCapabilityVO;
		List<UserCapabilityVO> userCapabilityVOList = new ArrayList<UserCapabilityVO>();
		for (UserCapability userCapability : profileToConvert.getCapabilities())
		{
			userCapabilityVO = toUserCapabilityVO(userCapability);
			userCapabilityVOList.add(userCapabilityVO);
		}
		UserSecurityProfileVO userSecurityProfileVO = new UserSecurityProfileVOBuilder()
				.setId(profileToConvert.getId())
				.setLoginId(profileToConvert.getLogonId())
				.setPassword(profileToConvert.getPassword())
				.setCapabilities(userCapabilityVOList)
				.setRegistrationToken(profileToConvert.getRegistrationToken())
				.setRegistrationTokenExpiry(profileToConvert.getRegistrationTokenExpiration())
				.createUserSecurityProfileVO();
		return userSecurityProfileVO;
	}

	public static UserCapabilityVO toUserCapabilityVO(
			UserCapability userCapability)
			throws IllegalAccessException, InvocationTargetException
	{
		UserCapabilityVO userCapabilityVO = new UserCapabilityVOBuilder()
				.setCapability(toCapabilityVO(userCapability.getCapability()))
				.setId(userCapability.getId())
				.createUserCapabilityVO();
		return userCapabilityVO;
	}

	public static List<UserSecurityProfileVO> toUserProfileVOList(
			List<UserSecurityProfile> allProfiles) throws IllegalAccessException,
			InvocationTargetException
	{
		List<UserSecurityProfileVO> convertedList = new ArrayList<UserSecurityProfileVO>(allProfiles.size());
		for (UserSecurityProfile profile : allProfiles)
		{
			convertedList.add(toUserProfileVO(profile));
		}
		return convertedList;
	}

	public static RoleVO toRoleVO(Role userRole) throws IllegalAccessException,
			InvocationTargetException
	{
		RoleVO userRoleVO = null;
		if (null != userRole)
		{

			userRoleVO = new RoleVOBuilder()
					.setId(userRole.getId())
					.setName(userRole.getName())
					.setRoleCapabilities(toRoleCapabilityVOList(userRole.getCapabilities()))
					.createUserRoleVO();
		}
		return userRoleVO;
	}

	public static List<RoleVO> toRoleVOList(
			List<Role> userRoles) throws IllegalAccessException,
			InvocationTargetException
	{
		List<RoleVO> userRoleVOs = new ArrayList<RoleVO>(userRoles.size());
		for (Role userRole : userRoles)
		{
			userRoleVOs.add(toRoleVO(userRole));
		}

		return userRoleVOs;
	}

	public static RoleCapabilityVO toRoleCapabilityVO(
			RoleCapability roleCapability) throws IllegalAccessException,
			InvocationTargetException
	{
		RoleCapabilityVO roleCapabilityVO = null;
		if (null != roleCapability)
		{
			RoleVO roleVO = null;
			if (roleCapability.getRole() != null)
			{
				roleVO = new RoleVOBuilder()
						.setId(roleCapability.getRole().getId())
						.setName(roleCapability.getRole().getName())
						.createUserRoleVO();
			}
			roleCapabilityVO = new RoleCapabilityVOBuilder()
					.setCapabilityVO(toCapabilityVO(roleCapability.getCapability()))
					.setId(roleCapability.getId())
					.setRoleVO(roleVO)
					.createRoleCapabilityVO();
		}
		return roleCapabilityVO;
	}

	public static List<RoleCapabilityVO> toRoleCapabilityVOList(
			Collection<RoleCapability> capabilities) throws
			IllegalAccessException, InvocationTargetException
	{
		List<RoleCapabilityVO> roleCapabilities = null;
		if (null != capabilities)
		{
			roleCapabilities = new ArrayList<RoleCapabilityVO>(capabilities.size());
			for (RoleCapability roleCapability : capabilities)
			{
				roleCapabilities.add(toRoleCapabilityVO(roleCapability));
			}
		}
		return roleCapabilities;
	}

	public static Role toRole(RoleVO userRoleVO) throws
			IllegalAccessException, InvocationTargetException
	{
		Role userRole = null;
		if (null != userRoleVO)
		{
			userRole = new Role();
			BeanUtils.copyProperties(userRole, userRoleVO);
			for (RoleCapabilityVO roleCapabilityVO : userRoleVO.getRoleCapabilities())
			{
				RoleCapability roleCapability = new RoleCapability();
				Capability capability = toCapability(roleCapabilityVO.getCapabilityVO());
				roleCapability.setCapability(capability);
				roleCapability.setId(roleCapabilityVO.getId());
				roleCapability.setRole(userRole);
				userRole.getCapabilities().add(roleCapability);
			}
		}
		return userRole;
	}

	public static RoleCapability toRoleCapability(
			RoleCapabilityVO roleCapabilityVO) throws IllegalAccessException,
			InvocationTargetException
	{
		RoleCapability roleCapability = null;
		if (null != roleCapabilityVO)
		{
			roleCapability = new RoleCapability();
			roleCapability.setId(roleCapabilityVO.getId());
			if (roleCapabilityVO.getCapabilityVO() != null)
			{
				roleCapability.setCapability(toCapability(roleCapabilityVO.getCapabilityVO()));
			}
			Role role = new Role();
			BeanUtils.copyProperties(role, roleCapabilityVO.getRoleVO());
			roleCapability.setRole(role);
		}
		return roleCapability;
	}
}
