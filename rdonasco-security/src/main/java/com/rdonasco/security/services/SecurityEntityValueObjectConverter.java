/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.services;

import com.rdonasco.security.model.Capability;
import com.rdonasco.security.model.Resource;
import com.rdonasco.security.model.UserCapability;
import com.rdonasco.security.model.UserSecurityProfile;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.ResourceVO;
import com.rdonasco.security.vo.UserCapabilityVO;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.BeanUtilsBean2;

/**
 *
 * @author Roy F. Donasco
 */
class SecurityEntityValueObjectConverter
{
	static
	{
		BeanUtilsBean.setInstance(new BeanUtilsBean2());
	}
	static final BeanUtilsBean BEAN_UTILS = BeanUtilsBean.getInstance();

	static UserSecurityProfile toUserProfile(UserSecurityProfileVO userSecurityProfileVO) 
			throws IllegalAccessException, InvocationTargetException
	{
		UserSecurityProfile securityProfile = new UserSecurityProfile();
		BEAN_UTILS.copyProperties(securityProfile, userSecurityProfileVO);
		securityProfile.setCapabilities(new ArrayList<UserCapability>(userSecurityProfileVO.getCapabilityVOList().size()));
		UserCapability userCapability = null;
		for(UserCapabilityVO userCapabilityVO : userSecurityProfileVO.getCapabilityVOList())
		{
			userCapability = new UserCapability();
			BEAN_UTILS.copyProperties(userCapability, userCapabilityVO);
			securityProfile.getCapabilities().add(userCapability);
		}
		return securityProfile;
	}

	static CapabilityVO toCapabilityVO(Capability capability)
			throws IllegalAccessException, InvocationTargetException
	{
		CapabilityVO capabilityVO = new CapabilityVO();
		BEAN_UTILS.copyProperties(capabilityVO, capability);
		
		return capabilityVO;
	}

	static Resource toResource(ResourceVO resource)
	{
		throw new UnsupportedOperationException("Not yet implemented");
	}

	static ResourceVO toResourceVO(Resource resource)
	{
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
