/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 11-May-2013
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
package com.rdonasco.security.role.controllers;

import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.datamanager.services.DataManager;
import com.rdonasco.security.capability.utils.IconHelper;
import com.rdonasco.security.common.controllers.ClickListenerProvider;
import com.rdonasco.security.role.vo.RoleItemVO;
import com.rdonasco.security.role.vo.RoleItemVOBuilder;
import com.rdonasco.security.services.RoleManagerLocal;
import com.rdonasco.security.vo.RoleVO;
import com.vaadin.ui.Embedded;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;

/**
 *
 * @author Roy F. Donasco
 */
public class RoleDataManagerDecorator implements DataManager<RoleItemVO>,
		Serializable
{

	private static final long serialVersionUID = 1L;

	@EJB
	private RoleManagerLocal roleManager;

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


	@Override
	public void deleteData(RoleItemVO roleItemVO) throws DataAccessException
	{
		roleManager.deleteData(roleItemVO.getRoleVO());

	}

	@Override
	public RoleItemVO loadData(RoleItemVO roleItemVO) throws DataAccessException
	{
		RoleVO roleVO = roleManager.loadData(roleItemVO.getRoleVO());
		return convertToRoleItemVOWithListener(roleVO);
	}

	@Override
	public List<RoleItemVO> retrieveAllData() throws DataAccessException
	{

		List<RoleVO> roleVOList = roleManager.retrieveAllData();
		List<RoleItemVO> roleItemVOList = new ArrayList<RoleItemVO>(roleVOList.size());
		for (RoleVO roleVO : roleVOList)
		{
			roleItemVOList.add(convertToRoleItemVOWithListener(roleVO));
		}

		return roleItemVOList;
	}

	@Override
	public RoleItemVO saveData(RoleItemVO roleItemVO) throws DataAccessException
	{
		RoleVO savedRole = roleManager.saveData(roleItemVO.getRoleVO());
		return convertToRoleItemVOWithListener(savedRole);
	}

	@Override
	public void updateData(RoleItemVO roleItemVO) throws DataAccessException
	{
		roleManager.updateData(roleItemVO.getRoleVO());
	}

	private RoleItemVO convertToRoleItemVOWithListener(RoleVO roleVO)
	{
		Embedded icon = IconHelper.createDeleteIcon(I18NResource.localize("Delete"));
		RoleItemVO roleItemVO = new RoleItemVOBuilder()
				.setIcon(icon)
				.setRoleVO(roleVO)
				.createRoleItemVO();
		if (getClickListenerProvider() != null)
		{
			icon.addListener(getClickListenerProvider().provideClickListenerFor(roleItemVO));
		}
		return roleItemVO;
	}
}
