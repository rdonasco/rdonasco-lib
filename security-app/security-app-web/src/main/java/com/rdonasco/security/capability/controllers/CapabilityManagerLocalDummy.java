/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.capability.controllers;

import com.rdonasco.common.exceptions.NonExistentEntityException;
import com.rdonasco.security.exceptions.CapabilityManagerException;
import com.rdonasco.security.exceptions.NotSecuredResourceException;
import com.rdonasco.security.services.CapabilityManagerLocal;
import com.rdonasco.security.vo.ActionVO;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.CapabilityVOBuilder;
import com.rdonasco.security.vo.ResourceVO;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Roy F. Donasco
 */
public class CapabilityManagerLocalDummy implements CapabilityManagerLocal
{
	private static final Logger LOG = Logger.getLogger(CapabilityManagerLocalDummy.class.getName());

	@Override
	public ActionVO createNewAction(ActionVO action) throws
			CapabilityManagerException
	{
		// To change body of generated methods, choose Tools | Templates.
		// TODO: Complete code for method createNewAction
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void updateAction(ActionVO actionToUpdate) throws
			CapabilityManagerException
	{
		// To change body of generated methods, choose Tools | Templates.
		// TODO: Complete code for method updateAction
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void removeAction(ActionVO actionToRemove) throws
			CapabilityManagerException
	{
		// To change body of generated methods, choose Tools | Templates.
		// TODO: Complete code for method removeAction
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public ActionVO findActionNamed(String nameOfActionToFind) throws
			CapabilityManagerException, NonExistentEntityException
	{
		// To change body of generated methods, choose Tools | Templates.
		// TODO: Complete code for method findActionNamed
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public ResourceVO addResource(ResourceVO resource) throws
			CapabilityManagerException
	{
		// To change body of generated methods, choose Tools | Templates.
		// TODO: Complete code for method addResource
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void updateResource(ResourceVO resource) throws
			CapabilityManagerException
	{
		// To change body of generated methods, choose Tools | Templates.
		// TODO: Complete code for method updateResource
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void removeResource(ResourceVO resource) throws
			CapabilityManagerException
	{
		// To change body of generated methods, choose Tools | Templates.
		// TODO: Complete code for method removeResource
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public ResourceVO findResourceNamedAs(String resourceName) throws
			CapabilityManagerException, NonExistentEntityException
	{
		// To change body of generated methods, choose Tools | Templates.
		// TODO: Complete code for method findResourceNamedAs
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public ResourceVO findOrAddSecuredResourceNamedAs(String resourceName)
			throws CapabilityManagerException, NotSecuredResourceException
	{
		// To change body of generated methods, choose Tools | Templates.
		// TODO: Complete code for method findOrAddSecuredResourceNamedAs
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public ResourceVO findOrAddResourceNamedAs(String resourceName) throws
			CapabilityManagerException
	{
		// To change body of generated methods, choose Tools | Templates.
		// TODO: Complete code for method findOrAddResourceNamedAs
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public ActionVO findOrAddActionNamedAs(String name) throws
			CapabilityManagerException
	{
		// To change body of generated methods, choose Tools | Templates.
		// TODO: Complete code for method findOrAddActionNamedAs
		throw new UnsupportedOperationException("Not supported yet.");
	}
	private static long idSeed = 1L;
	@Override
	public CapabilityVO createNewCapability(CapabilityVO capabilityToCreate)
			throws CapabilityManagerException
	{
		capabilityToCreate.setId(idSeed++);
		return capabilityToCreate;
	}

	@Override
	public void addActionsToCapability(
			List<ActionVO> actions, CapabilityVO capabilityVO) throws
			CapabilityManagerException
	{
		// To change body of generated methods, choose Tools | Templates.
		// TODO: Complete code for method addActionsToCapability
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<CapabilityVO> findAllCapabilities() throws
			CapabilityManagerException
	{
		final int size = 30;
		List<CapabilityVO> capabilities = new ArrayList<CapabilityVO>();
		for (long i = 0; i < size; i++)
		{
			final CapabilityVO capabilityVO = new CapabilityVOBuilder()
					.setTitle("Capability to do " + (i + 1))
					.createCapabilityVO();
			capabilities.add(createNewCapability(capabilityVO));
		}
		return capabilities;
	}

	@Override
	public CapabilityVO findCapabilityWithId(Long id) throws
			CapabilityManagerException
	{
		// To change body of generated methods, choose Tools | Templates.
		// TODO: Complete code for method findCapabilityWithId
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public CapabilityVO findCapabilityWithTitle(String capabilityTitle) throws
			CapabilityManagerException, NonExistentEntityException
	{
		// To change body of generated methods, choose Tools | Templates.
		// TODO: Complete code for method findCapabilityWithTitle
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void updateCapability(CapabilityVO capabilityToUpdate) throws
			CapabilityManagerException
	{
		// To change body of generated methods, choose Tools | Templates.
		// TODO: Complete code for method updateCapability
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void removeCapability(CapabilityVO capabilityToRemove) throws
			CapabilityManagerException
	{
		LOG.info("simulated removal of capability");
	}

	@Override
	public List<CapabilityVO> findCapabilitiesWithResourceName(
			String resourceName) throws CapabilityManagerException
	{
		// To change body of generated methods, choose Tools | Templates.
		// TODO: Complete code for method findCapabilitiesWithResourceName
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
