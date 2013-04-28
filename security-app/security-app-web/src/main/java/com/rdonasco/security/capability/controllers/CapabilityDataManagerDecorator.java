/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.capability.controllers;

import com.rdonasco.security.common.controllers.ClickListenerProvider;
import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.datamanager.services.DataManager;
import com.rdonasco.security.capability.utils.IconHelper;
import com.rdonasco.security.capability.vo.CapabilityItemVO;
import com.rdonasco.security.capability.vo.CapabilityItemVOBuilder;
import com.rdonasco.security.exceptions.CapabilityManagerException;
import com.rdonasco.security.services.CapabilityManagerLocal;
import com.rdonasco.security.vo.ActionVO;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.ResourceVO;
import com.vaadin.ui.Embedded;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;

/**
 *
 * @author Roy F. Donasco
 */
public class CapabilityDataManagerDecorator implements
		DataManager<CapabilityItemVO>, Serializable
{

	private static final long serialVersionUID = 1L;
	@EJB
	private CapabilityManagerLocal capabilityManager;
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
			itemVO = convertToCapabilityItemVOWithListener(capability);
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
				capabilityItems.add(convertToCapabilityItemVOWithListener(capability));
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
			itemVO = convertToCapabilityItemVOWithListener(createdCapability);
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

	public List<ResourceVO> findAllResources() throws
			CapabilityManagerException
	{
		return capabilityManager.findAllResources();
	}

	private CapabilityItemVO convertToCapabilityItemVOWithListener(
			CapabilityVO capability)
	{

		Embedded icon = IconHelper.createDeleteIcon(I18NResource.localize("Delete"));
		CapabilityItemVO itemVO = new CapabilityItemVOBuilder()
				.setCapabilityVO(capability)
				.setIcon(icon)
				.createCapabilityItemVO();
		if (getClickListenerProvider() != null)
		{
			icon.addListener(getClickListenerProvider().provideClickListenerFor(itemVO));
		}
		return itemVO;
	}

	public ResourceVO addResource(ResourceVO resource) throws
			CapabilityManagerException
	{
		return capabilityManager.addResource(resource);
	}

	public void updateResource(ResourceVO resource) throws
			CapabilityManagerException
	{
		capabilityManager.updateResource(resource);
	}

	public void removeResource(ResourceVO resource) throws
			CapabilityManagerException
	{
		capabilityManager.removeResource(resource);
	}

	public List<ActionVO> findAllActions() throws CapabilityManagerException
	{
		return capabilityManager.findAllActions();
	}

	public ActionVO createNewAction(ActionVO action) throws
			CapabilityManagerException
	{
		return capabilityManager.createNewAction(action);
	}

	public void updateAction(ActionVO actionToUpdate) throws
			CapabilityManagerException
	{
		capabilityManager.updateAction(actionToUpdate);
	}

	public void removeAction(ActionVO actionToRemove) throws
			CapabilityManagerException
	{
		capabilityManager.removeAction(actionToRemove);
	}
}
