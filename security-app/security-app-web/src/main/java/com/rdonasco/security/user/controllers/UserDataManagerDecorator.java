/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 15-Apr-2013
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rdonasco.security.user.controllers;

import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.datamanager.services.DataManager;
import com.rdonasco.security.capability.utils.IconHelper;
import com.rdonasco.security.common.controllers.ClickListenerProvider;
import com.rdonasco.security.exceptions.SecurityManagerException;
import com.rdonasco.security.services.SystemSecurityManager;
import com.rdonasco.security.services.SystemSecurityManagerLocal;
import com.rdonasco.security.user.vo.UserSecurityProfileItemVO;
import com.rdonasco.security.user.vo.UserSecurityProfileItemVOBuilder;
import com.rdonasco.security.utils.EncryptionUtil;
import com.rdonasco.security.vo.UserSecurityProfileVO;
import com.vaadin.ui.Embedded;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;

/**
 *
 * @author Roy F. Donasco
 */
public class UserDataManagerDecorator implements
		DataManager<UserSecurityProfileItemVO>
{
	@EJB
	private SystemSecurityManagerLocal securityManager;
	private ClickListenerProvider clickListenerProvider;

	public ClickListenerProvider getClickListenerProvider()
	{
		return clickListenerProvider;
	}

	public void setClickListenerProvider(
			ClickListenerProvider clickListenerProvider)
	{
		this.clickListenerProvider = clickListenerProvider;
	}

	public SystemSecurityManager getSecurityManager()
	{
		return securityManager;
	}

	@Override
	public UserSecurityProfileItemVO loadData(UserSecurityProfileItemVO data)
			throws DataAccessException
	{
		try
		{
			UserSecurityProfileVO userSecurityProfile = getSecurityManager().findSecurityProfileWithLogonID(data.getLogonId());
			UserSecurityProfileItemVO userSecurityProfileItemVO = convertToUserSecurityProfileItemWithListener(userSecurityProfile);
			return userSecurityProfileItemVO;
		}
		catch (SecurityManagerException ex)
		{
			throw new DataAccessException(ex);
		}
	}

	@Override
	public List<UserSecurityProfileItemVO> retrieveAllData() throws
			DataAccessException
	{
		List<UserSecurityProfileItemVO> profileItems = null;
		try
		{
			List<UserSecurityProfileVO> profileVOList = getSecurityManager().findAllProfiles();
			profileItems = new ArrayList<UserSecurityProfileItemVO>(profileVOList.size());
			for(UserSecurityProfileVO profile : profileVOList)
			{
				profileItems.add(convertToUserSecurityProfileItemWithListener(profile));
			}
		}
		catch (Exception e)
		{
			throw new DataAccessException(e);
		}
		return profileItems;
	}

	@Override
	public UserSecurityProfileItemVO saveData(UserSecurityProfileItemVO data)
			throws DataAccessException
	{
		UserSecurityProfileItemVO savedItemVO = null;
		try
		{
			UserSecurityProfileVO savedProfile = getSecurityManager().createNewSecurityProfile(data.getUserSecurityProfileVO());
			savedItemVO = convertToUserSecurityProfileItemWithListener(savedProfile);
		}
		catch (SecurityManagerException ex)
		{
			Logger.getLogger(UserDataManagerDecorator.class.getName()).log(Level.SEVERE, null, ex);
		}
		return savedItemVO;
	}

	@Override
	public void updateData(UserSecurityProfileItemVO data) throws
			DataAccessException
	{
		try
		{
			String password = data.getUserSecurityProfileVO().getPassword();
			if (password != null && !password.isEmpty())
			{
				data.getUserSecurityProfileVO().setPassword(EncryptionUtil.encryptWithPassword(password, password));
			}
			getSecurityManager().updateSecurityProfile(data.getUserSecurityProfileVO());
		}
		catch (Exception ex)
		{
			throw new DataAccessException(ex);
		}
	}

	private UserSecurityProfileItemVO convertToUserSecurityProfileItemWithListener(
			UserSecurityProfileVO userSecurityProfile)
	{
		Embedded icon = IconHelper.createDeleteIcon(I18NResource.localize("Delete"));
		UserSecurityProfileItemVO userSecurityProfileItemVO
				= new UserSecurityProfileItemVOBuilder()
				.setIcon(icon)
				.setUserSecurityProfileVO(userSecurityProfile)
				.createUserSecurityProfileItemVO();
		userSecurityProfileItemVO.setPassword(null);
		userSecurityProfileItemVO.setRetypedPassword(null);
		if(getClickListenerProvider() != null)
		{
			icon.addListener(getClickListenerProvider().provideClickListenerFor(userSecurityProfileItemVO));
		}
		return userSecurityProfileItemVO;
	}

	@Override
	public void deleteData(UserSecurityProfileItemVO data) throws
			DataAccessException
	{
		try
		{
			getSecurityManager().removeSecurityProfile(data.getUserSecurityProfileVO());
		}
		catch (SecurityManagerException ex)
		{
			throw new DataAccessException(ex);
		}
	}

}
