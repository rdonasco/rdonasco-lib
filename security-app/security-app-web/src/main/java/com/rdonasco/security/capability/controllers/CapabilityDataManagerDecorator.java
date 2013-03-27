/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.capability.controllers;

import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.datamanager.services.DataManager;
import com.rdonasco.security.app.themes.SecurityDefaultTheme;
import com.rdonasco.security.capability.vo.CapabilityItemVO;
import com.rdonasco.security.capability.vo.CapabilityItemVOBuilder;
import com.rdonasco.security.exceptions.CapabilityManagerException;
import com.rdonasco.security.services.CapabilityManagerLocal;
import com.rdonasco.security.vo.CapabilityVO;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Embedded;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Roy F. Donasco
 */
public class CapabilityDataManagerDecorator implements DataManager<CapabilityItemVO>
{

	//@EJB
	private CapabilityManagerLocal capabilityManager = new CapabilityManagerLocalDummy();
	private ClickListenerProvider clickListenerProvider;

	public CapabilityManagerLocal getCapabilityManager()
	{
		return capabilityManager;
	}

	public void setCapabilityManager(CapabilityManagerLocal capabilityManager)
	{
		this.capabilityManager = capabilityManager;
	}

	@Override
	public void deleteData(CapabilityItemVO data) throws DataAccessException
	{
		try
		{
			capabilityManager.removeCapability(data.getCapabilityVO());
		}
		catch (CapabilityManagerException ex)
		{
			throw new DataAccessException(ex);
		}
	}

	@Override
	public CapabilityItemVO loadData(CapabilityItemVO data) throws
			DataAccessException
	{
		CapabilityItemVO itemVO = null;
		try
		{
			CapabilityVO capability = capabilityManager.findCapabilityWithId(data.getId());
			itemVO = convertToCapabilityItemVOandAddToContainer(capability);
		}
		catch (CapabilityManagerException ex)
		{
			throw new DataAccessException(ex);
		}
		return itemVO;

	}

	@Override
	public List<CapabilityItemVO> retrieveAllData() throws DataAccessException
	{
		List<CapabilityItemVO> capabilityItems = null;
		try
		{
			List<CapabilityVO> capabilities = capabilityManager.findAllCapabilities();
			capabilityItems = new ArrayList<CapabilityItemVO>(capabilities.size());
			for (CapabilityVO capability : capabilities)
			{
				capabilityItems.add(convertToCapabilityItemVOandAddToContainer(capability));
			}
		}
		catch (CapabilityManagerException ex)
		{
			throw new DataAccessException(ex);
		}
		return capabilityItems;
	}

	@Override
	public CapabilityItemVO saveData(CapabilityItemVO data) throws
			DataAccessException
	{
		CapabilityItemVO itemVO;
		try
		{
			CapabilityVO createdCapability = capabilityManager.createNewCapability(data.getCapabilityVO());
			itemVO = convertToCapabilityItemVOandAddToContainer(createdCapability);
		}
		catch (CapabilityManagerException ex)
		{
			throw new DataAccessException(ex);
		}
		return itemVO;
	}

	@Override
	public void updateData(CapabilityItemVO data) throws DataAccessException
	{
		try
		{
			capabilityManager.updateCapability(data.getCapabilityVO());
		}
		catch (CapabilityManagerException ex)
		{
			throw new DataAccessException(ex);
		}
	}

	public ClickListenerProvider getClickListenerProvider()
	{
		return clickListenerProvider;
	}

	public void setClickListenerProvider(
			ClickListenerProvider clickListenerProvider)
	{
		this.clickListenerProvider = clickListenerProvider;
	}

	private CapabilityItemVO convertToCapabilityItemVOandAddToContainer(
			CapabilityVO capability)
	{
		CapabilityItemVO itemVO;
		Embedded icon = new Embedded(null, new ThemeResource(SecurityDefaultTheme.ICONS_16x16_DELETE));
		icon.setDescription(I18NResource.localize("Delete"));
		final CapabilityItemVO capabilityItem = new CapabilityItemVOBuilder()
				.setCapabilityVO(capability)
				.setEmbeddedIcon(icon)
				.createCapabilityItemVO();
		itemVO = capabilityItem;
		if (getClickListenerProvider() != null)
		{
			icon.addListener(getClickListenerProvider().provideClickListenerFor(capabilityItem));
		}
		return itemVO;
	}

}
