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
import com.rdonasco.security.model.UserCapability;
import com.rdonasco.security.model.Role;
import com.rdonasco.security.model.RoleCapability;
import com.rdonasco.security.model.SecurityGroup;
import com.rdonasco.security.model.SecurityGroupRole;
import com.rdonasco.security.model.UserGroup;
import com.rdonasco.security.model.UserRole;
import com.rdonasco.security.model.UserSecurityProfile;
import com.rdonasco.security.vo.ActionVO;
import com.rdonasco.security.vo.ApplicationHostVO;
import com.rdonasco.security.vo.ApplicationVO;
import com.rdonasco.security.vo.ApplicationVOBuilder;
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
import com.rdonasco.security.vo.SecurityGroupRoleVO;
import com.rdonasco.security.vo.SecurityGroupRoleVOBuilder;
import com.rdonasco.security.vo.SecurityGroupVO;
import com.rdonasco.security.vo.SecurityGroupVOBuilder;
import com.rdonasco.security.vo.UserGroupVO;
import com.rdonasco.security.vo.UserGroupVOBuilder;
import com.rdonasco.security.vo.UserRoleVO;
import com.rdonasco.security.vo.UserRoleVOBuilder;
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

	private static final BeanUtilsBean BEAN_UTILS = BeanUtilsBean.getInstance();

	static
	{
		BeanUtilsBean.setInstance(new BeanUtilsBean2());
	}

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
		UserCapability userCapability;
		Capability capability;
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
		List<UserRole> userRoles = toUserRoles(userSecurityProfile, userSecurityProfileVO.getRoles());
		userSecurityProfile.setRoles(userRoles);
		List<UserGroup> userGroups = toUserGroups(userSecurityProfile, userSecurityProfileVO.getGroups());
		userSecurityProfile.setGroups(userGroups);
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
			if(null != capability.getApplication())
			{
				capabilityVOBuilder.setApplication(toApplicationVO(capability.getApplication()));
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
		if(null != capabilityVO.getApplicationVO())
		{
			capability.setApplication(toApplication(capabilityVO.getApplicationVO()));
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
		List<UserRoleVO> userRoleVOList = new ArrayList<UserRoleVO>();
		List<UserGroupVO> userGroupVOList = new ArrayList<UserGroupVO>();

		UserSecurityProfileVO userSecurityProfileVO = new UserSecurityProfileVOBuilder()
				.setId(profileToConvert.getId())
				.setLoginId(profileToConvert.getLogonId())
				.setPassword(profileToConvert.getPassword())
				.setCapabilities(userCapabilityVOList)
				.setRoles(userRoleVOList)
				.setGroups(userGroupVOList)
				.setRegistrationToken(profileToConvert.getRegistrationToken())
				.setRegistrationTokenExpiry(profileToConvert.getRegistrationTokenExpiration())
				.createUserSecurityProfileVO();
		for (UserRole userRole : profileToConvert.getRoles())
		{
			userRoleVOList.add(toUserRoleVO(userSecurityProfileVO, userRole));
		}
		for (UserGroup userGroup : profileToConvert.getGroups())
		{
			userGroupVOList.add(toUserGroupVO(userSecurityProfileVO, userGroup));
		}
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

	private static List<UserRole> toUserRoles(UserSecurityProfile profile,
			Collection<UserRoleVO> roleVOList) throws IllegalAccessException,
			InvocationTargetException
	{
		List<UserRole> userRoles = null;
		if (roleVOList != null)
		{
			userRoles = new ArrayList<UserRole>(roleVOList.size());
			UserRole userRole = null;
			for (UserRoleVO userRoleVO : roleVOList)
			{
				userRole = new UserRole();
				userRole.setId(userRoleVO.getId());
				userRole.setRole(toRole(userRoleVO.getRole()));
				userRole.setUserProfile(profile);
				userRoles.add(userRole);
			}
		}
		return userRoles;
	}

	private static UserRoleVO toUserRoleVO(UserSecurityProfileVO userProfile,
			UserRole userRole) throws
			IllegalAccessException, IllegalAccessException,
			InvocationTargetException
	{
		UserRoleVO userRoleVO = null;
		if (null != userRole)
		{
			userRoleVO = new UserRoleVOBuilder()
					.setId(userRole.getId())
					.setRole(toRoleVO(userRole.getRole()))
					.setUserProfile(userProfile)
					.createUserRoleVO();
		}
		return userRoleVO;
	}

	public static SecurityGroupVO toSecurityGroupVO(SecurityGroup securityGroup)
			throws IllegalAccessException, InvocationTargetException
	{
		List<SecurityGroupRoleVO> securityGroupRoleVOs = toSecurityGroupRoleVOs(securityGroup.getGroupRoles());
		SecurityGroupVO securityGroupVO = new SecurityGroupVOBuilder()
				.setId(securityGroup.getId())
				.setName(securityGroup.getName())
				.setSecurityGroupRoles(securityGroupRoleVOs)
				.createSecurityGroupVO();
		return securityGroupVO;
	}

	public static SecurityGroup toSecurityGroup(SecurityGroupVO securityGroupVO)
			throws IllegalAccessException, InvocationTargetException
	{
		SecurityGroup securityGroup = new SecurityGroup();
		securityGroup.setId(securityGroupVO.getId());
		securityGroup.setName(securityGroupVO.getName());
		securityGroup.setGroupRoles(toSecurityGroupRoles(securityGroup, securityGroupVO.getGroupRoleVOs()));
		return securityGroup;
	}

	public static List<SecurityGroupVO> toSecurityGroupVOList(
			List<SecurityGroup> securityGroupList) throws IllegalAccessException,
			InvocationTargetException
	{
		List<SecurityGroupVO> securityGroupVOList = new ArrayList<SecurityGroupVO>(securityGroupList.size());
		for (SecurityGroup securityGroup : securityGroupList)
		{
			securityGroupVOList.add(toSecurityGroupVO(securityGroup));
		}
		return securityGroupVOList;
	}

	private static Collection<SecurityGroupRole> toSecurityGroupRoles(
			SecurityGroup parentGroup,
			List<SecurityGroupRoleVO> roles) throws IllegalAccessException,
			InvocationTargetException
	{
		List<SecurityGroupRole> securityGroupRoles = new ArrayList<SecurityGroupRole>(roles.size());
		for (SecurityGroupRoleVO securityGroupRoleVO : roles)
		{
			SecurityGroupRole securityGroupRole = toSecurityGroupRole(securityGroupRoleVO);
			securityGroupRole.setSecurityGroup(parentGroup);
			securityGroupRoles.add(securityGroupRole);
		}
		return securityGroupRoles;
	}

	private static SecurityGroupRole toSecurityGroupRole(
			SecurityGroupRoleVO securityGroupRoleVO) throws
			IllegalAccessException, InvocationTargetException
	{
		SecurityGroupRole securityGroupRole = new SecurityGroupRole();
		securityGroupRole.setId(securityGroupRoleVO.getId());
		securityGroupRole.setRole(toRole(securityGroupRoleVO.getRoleVO()));

		return securityGroupRole;
	}

	private static List<SecurityGroupRoleVO> toSecurityGroupRoleVOs(
			Collection<SecurityGroupRole> groupRoles) throws
			IllegalAccessException, InvocationTargetException
	{
		List<SecurityGroupRoleVO> securityGroupRoleVOs = new ArrayList<SecurityGroupRoleVO>(groupRoles.size());
		for (SecurityGroupRole securityGroupRole : groupRoles)
		{
			securityGroupRoleVOs.add(toSecurityGroupRoleVO(securityGroupRole));
		}

		return securityGroupRoleVOs;
	}

	private static SecurityGroupRoleVO toSecurityGroupRoleVO(
			SecurityGroupRole securityGroupRole) throws IllegalAccessException,
			InvocationTargetException
	{
		SecurityGroupRoleVO securityGroupRoleVO = new SecurityGroupRoleVOBuilder()
				.setId(securityGroupRole.getId())
				.setRole(toRoleVO(securityGroupRole.getRole()))
				.createSecurityGroupRoleVO();
		return securityGroupRoleVO;
	}

	private static List<UserGroup> toUserGroups(
			UserSecurityProfile userSecurityProfile,
			Collection<UserGroupVO> userGroupVOs) throws IllegalAccessException,
			InvocationTargetException
	{
		List<UserGroup> userGroups = null;
		if (null != userGroupVOs)
		{
			userGroups = new ArrayList<UserGroup>(userGroupVOs.size());
			for (UserGroupVO userGroupVO : userGroupVOs)
			{
				UserGroup userGroup = new UserGroup();
				userGroup.setGroup(toSecurityGroup(userGroupVO.getGroup()));
				userGroup.setId(userGroupVO.getId());
				userGroup.setUserProfile(userSecurityProfile);
				userGroups.add(userGroup);
			}
		}
		return userGroups;
	}

	private static UserGroupVO toUserGroupVO(
			UserSecurityProfileVO userSecurityProfileVO, UserGroup userGroup)
			throws IllegalAccessException, InvocationTargetException
	{
		return new UserGroupVOBuilder()
				.setGroup(toSecurityGroupVO(userGroup.getGroup()))
				.setId(userGroup.getId())
				.setUserProfile(userSecurityProfileVO)
				.createUserGroupVO();
	}

	public static ApplicationVO toApplicationVO(Application application)
	{
		ApplicationVOBuilder applicationVOBuilder = new ApplicationVOBuilder()
				.setId(application.getId())
				.setName(application.getName())
				.setToken(application.getToken());
		if (application.getHosts() != null && !application.getHosts().isEmpty())
		{
			for (ApplicationHost applicationHost : application.getHosts())
			{
				applicationVOBuilder.addHost(toApplicationHostVO(applicationHost));
			}
		}
		return applicationVOBuilder.createApplicationVO();
	}

	public static Application toApplication(ApplicationVO applicationVO)
	{
		Application application = new Application();
		application.setId(applicationVO.getId());
		application.setName(applicationVO.getName());
		application.setToken(applicationVO.getToken());
		if (applicationVO.getHosts() != null && !applicationVO.getHosts().isEmpty())
		{
			application.setHosts(new ArrayList<ApplicationHost>(applicationVO.getHosts().size()));
			for (ApplicationHostVO applicationHostVO : applicationVO.getHosts())
			{
				application.getHosts().add(toApplicationHost(applicationHostVO, application));
			}
		}
		return application;
	}

	private static ApplicationHostVO toApplicationHostVO(
			ApplicationHost applicationHost)
	{
		ApplicationHostVO applicationHostVO = new ApplicationHostVO();
		applicationHostVO.setId(applicationHost.getId());
		applicationHostVO.setHostNameOrIpAddress(applicationHost.getHostNameOrIpAddress());
		return applicationHostVO;
	}

	private static ApplicationHost toApplicationHost(
			ApplicationHostVO applicationHostVO, Application application)
	{
		ApplicationHost applicationHost = new ApplicationHost();
		applicationHost.setId(applicationHostVO.getId());
		applicationHost.setHostNameOrIpAddress(applicationHostVO.getHostNameOrIpAddress());
		applicationHost.setApplication(application);
		return applicationHost;
	}
}
