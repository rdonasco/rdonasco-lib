/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.capability.controllers;

import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.datamanager.services.DataManager;
import com.rdonasco.security.app.controllers.ApplicationExceptionPopupProvider;
import com.rdonasco.security.app.themes.SecurityDefaultTheme;
import com.rdonasco.security.capability.views.CapabilityListContainer;
import com.rdonasco.security.capability.vo.CapabilityItemVO;
import com.rdonasco.security.capability.vo.CapabilityItemVOBuilder;
import com.rdonasco.security.exceptions.CapabilityManagerException;
import com.rdonasco.security.services.CapabilityManagerLocal;
import com.rdonasco.security.vo.CapabilityVO;
import com.vaadin.event.MouseEvents;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Embedded;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
public class CapabilityDataManager implements DataManager<CapabilityItemVO>
{
	@EJB
	private CapabilityManagerLocal capabilityManager;
	private CapabilityListContainer capabilityListContainer;
	@Inject
	private ApplicationExceptionPopupProvider exceptionPopupProvider;

	public CapabilityManagerLocal getCapabilityManager()
	{
		return capabilityManager;
	}

	public void setCapabilityManager(CapabilityManagerLocal capabilityManager)
	{
		this.capabilityManager = capabilityManager;
	}

	public CapabilityListContainer getCapabilityListContainer()
	{
		return capabilityListContainer;
	}

	public void setCapabilityListContainer(
			CapabilityListContainer capabilityListContainer)
	{
		this.capabilityListContainer = capabilityListContainer;
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
		List<CapabilityItemVO> allCapabilityItems = null;
		try
		{
			List<CapabilityVO> allCapabilities = capabilityManager.findAllCapabilities();
			allCapabilityItems = new ArrayList<CapabilityItemVO>(allCapabilities.size());
			for (CapabilityVO capability : allCapabilities)
			{
				allCapabilityItems.add(convertToCapabilityItemVOandAddToContainer(capability));
			}
		}
		catch (CapabilityManagerException ex)
		{
			throw new DataAccessException(ex);
		}
		return allCapabilityItems;
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

	private CapabilityItemVO convertToCapabilityItemVOandAddToContainer(
			CapabilityVO capability)
	{
		CapabilityItemVO itemVO;
		Embedded icon = new Embedded(null, new ThemeResource(SecurityDefaultTheme.ICONS_16x16_DELETE));
		final CapabilityItemVO capabilityItem = new CapabilityItemVOBuilder()
				.setCapabilityVO(capability)
				.setEmbeddedIcon(icon)
				.createCapabilityItemVO();
		itemVO = capabilityItem;
		icon.addListener(new MouseEvents.ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void click(MouseEvents.ClickEvent event)
			{
				try
				{
					capabilityManager.removeCapability(capabilityItem.getCapabilityVO());
					capabilityListContainer.removeItem(capabilityItem);
				}
				catch (CapabilityManagerException ex)
				{
					exceptionPopupProvider.popUpErrorException(ex);
				}
			}
		});
		return itemVO;
	}
}
