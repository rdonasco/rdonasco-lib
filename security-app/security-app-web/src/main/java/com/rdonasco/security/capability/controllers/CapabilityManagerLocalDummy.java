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
import com.rdonasco.security.vo.CapabilityActionVO;
import com.rdonasco.security.vo.CapabilityActionVOBuilder;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.CapabilityVOBuilder;
import com.rdonasco.security.vo.ResourceVO;
import com.rdonasco.security.vo.ResourceVOBuilder;
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
	private static Long resourceID = 1L;
	private static List<ResourceVO> resources;
	private static List<ActionVO> actions;
	private static long idSeed = 1L;

	@Override
	public ActionVO createNewAction(ActionVO action) throws
			CapabilityManagerException
	{
		action.setId(idSeed++);
		actions.add(action);
		return action;
	}

	@Override
	public void updateAction(ActionVO actionToUpdate) throws
			CapabilityManagerException
	{
		ActionVO actionUpdated = actions.get(actions.indexOf(actionToUpdate));
		actionUpdated.setName(actionToUpdate.getName());
		actionUpdated.setDescription(actionToUpdate.getDescription());
	}

	@Override
	public void removeAction(ActionVO actionToRemove) throws
			CapabilityManagerException
	{
		actions.remove(actionToRemove);
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
		resource.setId(resourceID++);
		resources.add(resource);
		return resource;
	}

	@Override
	public void updateResource(ResourceVO resource) throws
			CapabilityManagerException
	{
		int index = resources.indexOf(resource);
		ResourceVO resourceToUpdate = resources.get(index);
		resourceToUpdate.setName(resource.getName());
		resourceToUpdate.setDescription(resource.getDescription());
	}

	@Override
	public void removeResource(ResourceVO resource) throws
			CapabilityManagerException
	{
		resources.remove(resource);
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
	public List<ResourceVO> findAllResources() throws
			CapabilityManagerException
	{
		if (null == resources)
		{
			final int size = 6;
			resources = new ArrayList<ResourceVO>();
			for (long i = 0; i < size; i++)
			{
				final ResourceVO resource = new ResourceVOBuilder()
						.setId(resourceID++)
						.setName("Resource-" + resourceID)
						.setDescription("Description-" + resourceID)
						.createResourceVO();
				resources.add(resource);
			}
		}
		return resources;
	}

	@Override
	public List<CapabilityVO> findAllCapabilities() throws
			CapabilityManagerException
	{
		final int size = 30;
		List<CapabilityVO> capabilities = new ArrayList<CapabilityVO>();
		for (long i = 0; i < size; i++)
		{
			final CapabilityVO capabilityVO = createTestDataCapabilityVO();
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
	private static Long capabilityActionKey = 1L;
	private static Long actionKey = 1L;

	private CapabilityVO createTestDataCapabilityVO() throws
			CapabilityManagerException
	{
		List<CapabilityActionVO> actions = new ArrayList<CapabilityActionVO>();
		CapabilityVO capability = new CapabilityVOBuilder()
				.setTitle("Capability to do " + idSeed)
				.setDescription("Capability to do " + idSeed)
				.setResource(findAllResources().get(0))
				.setActions(actions)
				.createCapabilityVO();
		int size = 5;
		for (int i = 0; i < size; i++)
		{
			ActionVO action = ActionVO.createWithIdNameAndDescription(actionKey++, "Action " + actionKey, "Action " + actionKey);
			CapabilityActionVO capabilityAction = new CapabilityActionVOBuilder()
					.setActionVO(action)
					.setId(capabilityActionKey++)
					.createCapabilityActionVO();
			actions.add(capabilityAction);
			capabilityAction.setCapabilityVO(capability);
		}

		return capability;
	}

	@Override
	public List<ActionVO> findAllActions() throws CapabilityManagerException
	{
		if (actions == null)
		{
			actions = new ArrayList<ActionVO>();
			actions.add(ActionVO.createWithIdNameAndDescription(idSeed++, "View", "View"));
			actions.add(ActionVO.createWithIdNameAndDescription(idSeed++, "Create", "Create"));
			actions.add(ActionVO.createWithIdNameAndDescription(idSeed++, "Edit", "Edit"));
			actions.add(ActionVO.createWithIdNameAndDescription(idSeed++, "Delete", "Delete"));
			actions.add(ActionVO.createWithIdNameAndDescription(idSeed++, "Update", "Update"));
		}
		return actions;
	}
}
