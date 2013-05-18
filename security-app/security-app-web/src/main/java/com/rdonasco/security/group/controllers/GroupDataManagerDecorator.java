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
package com.rdonasco.security.group.controllers;

import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.datamanager.services.DataManager;
import com.rdonasco.security.capability.utils.IconHelper;
import com.rdonasco.security.common.controllers.ClickListenerProvider;
import com.rdonasco.security.group.vo.GroupItemVO;
import com.rdonasco.security.group.vo.GroupItemVOBuilder;
import com.rdonasco.security.services.SecurityGroupDataManagerLocal;
import com.rdonasco.security.vo.SecurityGroupVO;
import com.vaadin.ui.Embedded;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;

/**
 *
 * @author Roy F. Donasco
 */
public class GroupDataManagerDecorator implements DataManager<GroupItemVO>,
		Serializable
{

	private static final long serialVersionUID = 1L;

	@EJB
	private SecurityGroupDataManagerLocal groupDataManager;

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
	public void deleteData(GroupItemVO groupItemVO) throws DataAccessException
	{
		groupDataManager.deleteData(groupItemVO.getSecurityGroupVO());

	}

	@Override
	public GroupItemVO loadData(GroupItemVO groupItemVO) throws
			DataAccessException
	{
		SecurityGroupVO groupVO = groupDataManager.loadData(groupItemVO.getSecurityGroupVO());
		return convertToGroupItemVOWithListener(groupVO);
	}

	@Override
	public List<GroupItemVO> retrieveAllData() throws DataAccessException
	{

		List<SecurityGroupVO> groupVOList = groupDataManager.retrieveAllData();
		List<GroupItemVO> roleItemVOList = new ArrayList<GroupItemVO>(groupVOList.size());
		for (SecurityGroupVO roleVO : groupVOList)
		{
			roleItemVOList.add(convertToGroupItemVOWithListener(roleVO));
		}

		return roleItemVOList;
	}

	@Override
	public GroupItemVO saveData(GroupItemVO roleItemVO) throws
			DataAccessException
	{
		SecurityGroupVO savedRole = groupDataManager.saveData(roleItemVO.getSecurityGroupVO());
		return convertToGroupItemVOWithListener(savedRole);
	}

	@Override
	public void updateData(GroupItemVO roleItemVO) throws DataAccessException
	{
		groupDataManager.updateData(roleItemVO.getSecurityGroupVO());
	}

	private GroupItemVO convertToGroupItemVOWithListener(SecurityGroupVO groupVO)
	{
		Embedded icon = IconHelper.createDeleteIcon(I18NResource.localize("Delete"));
		GroupItemVO groupItemVO = new GroupItemVOBuilder()
				.setIcon(icon)
				.setRoleVO(groupVO)
				.createGroupItemVO();
		return groupItemVO;
	}
}
