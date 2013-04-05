/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.services;

import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.common.exceptions.MultipleEntityFoundException;
import com.rdonasco.common.exceptions.NonExistentEntityException;
import com.rdonasco.security.dao.ActionDAO;
import com.rdonasco.security.dao.CapabilityDAO;
import com.rdonasco.security.dao.ResourceDAO;
import com.rdonasco.security.exceptions.NotSecuredResourceException;
import com.rdonasco.security.exceptions.CapabilityManagerException;
import com.rdonasco.security.model.Action;
import com.rdonasco.security.model.Capability;
import com.rdonasco.security.model.CapabilityAction;
import com.rdonasco.security.model.Resource;
import com.rdonasco.security.utils.SecurityEntityValueObjectConverter;
import com.rdonasco.security.vo.ActionVO;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.ResourceVO;
import com.rdonasco.security.vo.ResourceVOBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
@Stateless
public class CapabilityManagerImpl implements CapabilityManagerRemote,
		CapabilityManagerLocal
{

	private static final Logger LOG = Logger.getLogger(CapabilityManagerImpl.class.getName());
	@Inject
	private CapabilityDAO capabilityDAO;
	@Inject
	private ResourceDAO resourceDAO;
	@Inject
	private ActionDAO actionDAO;

	public void setActionDAO(ActionDAO actionDAO)
	{
		this.actionDAO = actionDAO;
	}

	public void setCapabilityDAO(CapabilityDAO capabilityDAO)
	{
		this.capabilityDAO = capabilityDAO;
	}

	public void setResourceDAO(ResourceDAO resourceDAO)
	{
		this.resourceDAO = resourceDAO;
	}

	@Override
	public ResourceVO addResource(ResourceVO resourceVO) throws
			CapabilityManagerException
	{
		try
		{
			Resource resource = SecurityEntityValueObjectConverter.toResource(resourceVO);
			if (null == resourceVO.getId())
			{
				resource.setId(null);
			}
			resourceDAO.create(resource);
			resourceVO = SecurityEntityValueObjectConverter.toResourceVO(resource);
			LOG.log(Level.FINE, "resource {0} created", resourceVO);
		}
		catch (Exception e)
		{
			throw new CapabilityManagerException(e);
		}
		return resourceVO;
	}

	@Override
	public void updateResource(ResourceVO resource) throws
			CapabilityManagerException
	{
		try
		{
			Resource resourceToUpdate = SecurityEntityValueObjectConverter.toResource(resource);
			resourceDAO.update(resourceToUpdate);
		}
		catch (Exception e)
		{
			throw new CapabilityManagerException(e);
		}
	}

	@Override
	public void removeResource(ResourceVO resource) throws
			CapabilityManagerException
	{
		try
		{
			resourceDAO.delete(Resource.class, resource.getId());
		}
		catch (Exception e)
		{
			throw new CapabilityManagerException(e);
		}
	}

	@Override
	public ResourceVO findResourceNamedAs(String resourceName) throws
			CapabilityManagerException, NonExistentEntityException
	{
		ResourceVO resourceVO = null;
		try
		{
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(Resource.QUERY_PARAM_RESOURCE_NAME, resourceName);
			Resource resource = resourceDAO.findUniqueDataUsingNamedQuery(Resource.NAMED_QUERY_FIND_RESOURCE_BY_NAME, parameters);
			resourceVO = SecurityEntityValueObjectConverter.toResourceVO(resource);
		}
		catch (NonExistentEntityException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new CapabilityManagerException(e);
		}
		return resourceVO;
	}

	@Override
	public ResourceVO findOrAddResourceNamedAs(String resourceName) throws
			CapabilityManagerException
	{
		ResourceVO resourceVO = null;
		try
		{
			resourceVO = findOrAddSecuredResourceNamedAsAndPotentiallyThrowNotSecuredException(resourceName, false);
		}
		catch (NotSecuredResourceException e)
		{
			LOG.log(Level.SEVERE, "NotSecuredResourceException thrown despite the request not to.", e);
		}
		return resourceVO;
	}

	@Override
	public List<ResourceVO> findAllResources() throws
			CapabilityManagerException
	{
		List<ResourceVO> resourceVoList;
		try
		{
			List<Resource> resources = resourceDAO.findAllData();
			resourceVoList = SecurityEntityValueObjectConverter.toResourceVOList(resources);

		}
		catch (Exception e)
		{
			throw new CapabilityManagerException(e);
		}
		return resourceVoList;
	}

	@Override
	public ResourceVO findOrAddSecuredResourceNamedAs(String resourceName)
			throws CapabilityManagerException, NotSecuredResourceException
	{
		return findOrAddSecuredResourceNamedAsAndPotentiallyThrowNotSecuredException(resourceName, true);
	}

	private ResourceVO findOrAddSecuredResourceNamedAsAndPotentiallyThrowNotSecuredException(
			String resourceName, boolean throwNotSecuredException)
			throws CapabilityManagerException, NotSecuredResourceException
	{
		Resource securedResource = null;
		ResourceVO securedResourceVO = null;
		try
		{
			List<Capability> capabilities = findCapabilityEntitiesWithResourceName(resourceName);
			if (null != capabilities && !capabilities.isEmpty())
			{
				securedResource = capabilities.get(0).getResource();
				securedResourceVO = SecurityEntityValueObjectConverter.toResourceVO(securedResource);
			}
			else
			{
				try
				{
					securedResourceVO = findResourceNamedAs(resourceName);
				}
				catch (NonExistentEntityException e)
				{
					ResourceVO resourceToAdd = new ResourceVOBuilder()
							.setName(resourceName)
							.setDescription(resourceName)
							.createResourceVO();
					securedResourceVO = addResource(resourceToAdd);
				}
			}
			if (null == securedResource)
			{
				throw new NotSecuredResourceException(String.format("Not Secured Resource {%s}. Can be accessed by anyone.", resourceName));
			}
		}
		catch (NotSecuredResourceException e)
		{
			if (throwNotSecuredException)
			{
				throw e;
			}
			else
			{
				LOG.log(Level.FINE, "ignored exception {0}", e.getMessage());
				LOG.log(Level.FINE, e.getMessage(), e);
			}
		}
		catch (Exception e)
		{
			throw new CapabilityManagerException(e);
		}

		return securedResourceVO;
	}

	@Override
	public ActionVO findOrAddActionNamedAs(String name) throws
			CapabilityManagerException
	{
		ActionVO actionVO = null;
		Action action = null;
		try
		{
			action = findActionEntityNamed(name);
		}
		catch (NonExistentEntityException e)
		{
			LOG.log(Level.WARNING, "Action {0} not found. Creating one", name);
			LOG.log(Level.FINE, e.getMessage(), e);
			action = new Action();
			action.setName(name);
			action.setDescription(name);
			try
			{
				actionDAO.create(action);
				LOG.log(Level.FINE, "Action {0} created.", action);
			}
			catch (Exception ex)
			{
				throw new CapabilityManagerException(ex);
			}

		}
		catch (Exception e)
		{
			throw new CapabilityManagerException(e);
		}
		try
		{
			actionVO = SecurityEntityValueObjectConverter.toActionVO(action);
		}
		catch (Exception ex)
		{
			throw new CapabilityManagerException(ex);
		}
		return actionVO;
	}

	@Override
	public ActionVO createNewAction(ActionVO actionVO) throws
			CapabilityManagerException
	{
		ActionVO actionVOToReturn = null;
		try
		{
			Action action = SecurityEntityValueObjectConverter.toAction(actionVO);
			actionDAO.create(action);
			actionVOToReturn = SecurityEntityValueObjectConverter.toActionVO(action);
			LOG.log(Level.FINE, "action {0} created", actionVOToReturn);
		}
		catch (Exception e)
		{
			throw new CapabilityManagerException(e);
		}
		return actionVOToReturn;
	}

	@Override
	public void updateAction(ActionVO actionToUpdate) throws
			CapabilityManagerException
	{
		try
		{
			Action action = SecurityEntityValueObjectConverter.toAction(actionToUpdate);
			actionDAO.update(action);
		}
		catch (Exception e)
		{
			throw new CapabilityManagerException(e);
		}
	}

	@Override
	public void removeAction(ActionVO actionToRemove) throws
			CapabilityManagerException
	{
		try
		{
			actionDAO.delete(Action.class, actionToRemove.getId());
		}
		catch (Exception e)
		{
			throw new CapabilityManagerException(e);
		}
	}

	private Action findActionEntityNamed(String name)
			throws DataAccessException, NonExistentEntityException,
			MultipleEntityFoundException
	{
		Action action;
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(Action.QUERY_PARAM_NAME, name);

		action = actionDAO.findUniqueDataUsingNamedQuery(Action.NAMED_QUERY_FIND_ACTION_BY_NAME, parameters);

		return action;
	}

	@Override
	public ActionVO findActionNamed(String nameOfActionToFind) throws
			CapabilityManagerException,
			NonExistentEntityException
	{
		ActionVO foundAction = null;
		try
		{
			Action action = findActionEntityNamed(nameOfActionToFind);
			foundAction = SecurityEntityValueObjectConverter.toActionVO(action);
		}
		catch (NonExistentEntityException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new CapabilityManagerException(e);
		}
		return foundAction;
	}

	@Override
	public List<ActionVO> findAllActions() throws CapabilityManagerException
	{
		List<Action> actions;
		List<ActionVO> actionVOList;
		try
		{
			actions = actionDAO.findAllData();
			actionVOList = SecurityEntityValueObjectConverter.toActionVOList(actions);
		}
		catch (Exception e)
		{
			throw new CapabilityManagerException(e);
		}
		return actionVOList;
	}

	@Override
	public CapabilityVO createNewCapability(CapabilityVO capabilityToCreate)
			throws CapabilityManagerException
	{
		CapabilityVO createdCapabilityVO = null;
		try
		{
			Capability capability = SecurityEntityValueObjectConverter.toCapability(capabilityToCreate);
			capabilityDAO.create(capability);
			createdCapabilityVO = SecurityEntityValueObjectConverter.toCapabilityVO(capability);
			LOG.log(Level.FINE, "capability {0} created", createdCapabilityVO);
		}
		catch (Exception e)
		{
			throw new CapabilityManagerException(e);
		}
		return createdCapabilityVO;
	}

	@Override
	public List<CapabilityVO> findAllCapabilities() throws
			CapabilityManagerException
	{
		List<Capability> allCapability = capabilityDAO.findAllData();
		List<CapabilityVO> allCapabilityVO = new ArrayList<CapabilityVO>(allCapability.size());
		try
		{
			for (Capability capabilty : allCapability)
			{
				allCapabilityVO.add(SecurityEntityValueObjectConverter.toCapabilityVO(capabilty));
			}
		}
		catch (Exception e)
		{
			throw new CapabilityManagerException(e);
		}
		return allCapabilityVO;
	}

	@Override
	public CapabilityVO findCapabilityWithId(Long id) throws
			CapabilityManagerException
	{
		CapabilityVO foundCapabilityVO = null;
		try
		{
			Capability capability = capabilityDAO.findData(Capability.class, id);
			foundCapabilityVO = SecurityEntityValueObjectConverter.toCapabilityVO(capability);
		}
		catch (Exception e)
		{
			throw new CapabilityManagerException(e);
		}
		return foundCapabilityVO;
	}

	@Override
	public void updateCapability(CapabilityVO capabilityToUpdate) throws
			CapabilityManagerException
	{
		try
		{
			Capability capability = SecurityEntityValueObjectConverter.toCapability(capabilityToUpdate);
			capabilityDAO.update(capability);
		}
		catch (Exception e)
		{
			throw new CapabilityManagerException(e);
		}
	}

	@Override
	public void removeCapability(CapabilityVO capabilityToRemove) throws
			CapabilityManagerException
	{
		try
		{
			capabilityDAO.delete(Capability.class, capabilityToRemove.getId());
		}
		catch (Exception e)
		{
			throw new CapabilityManagerException(e);
		}
	}

	@Override
	public CapabilityVO findCapabilityWithTitle(String capabilityTitle) throws
			CapabilityManagerException, NonExistentEntityException
	{
		CapabilityVO foundCapabilityVO = null;
		try
		{
			Map<String, Object> parameter = new HashMap<String, Object>();
			parameter.put(Capability.QUERY_PARAM_TITLE, capabilityTitle);
			Capability capability = capabilityDAO.findUniqueDataUsingNamedQuery(Capability.NAMED_QUERY_FIND_BY_TITLE, parameter);
			foundCapabilityVO = SecurityEntityValueObjectConverter.toCapabilityVO(capability);
		}
		catch (NonExistentEntityException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new CapabilityManagerException(e);
		}
		return foundCapabilityVO;
	}

	private List<Capability> findCapabilityEntitiesWithResourceName(
			String resourceName) throws CapabilityManagerException
	{
		List<Capability> capabilities = null;
		try
		{
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(Capability.QUERY_PARAM_RESOURCE_NAME, resourceName);
			capabilities = capabilityDAO.findAllDataUsingNamedQuery(Capability.NAMED_QUERY_FIND_BY_RESOURCE_NAME, parameters);
		}
		catch (Exception e)
		{
			throw new CapabilityManagerException(e);
		}
		return capabilities;
	}

	@Override
	public List<CapabilityVO> findCapabilitiesWithResourceName(
			String resourceName) throws CapabilityManagerException
	{
		List<CapabilityVO> capabilityVOList = null;
		try
		{

			List<Capability> capabilities = findCapabilityEntitiesWithResourceName(resourceName);
			for (Capability capability : capabilities)
			{
				capabilityVOList.add(SecurityEntityValueObjectConverter.toCapabilityVO(capability));
			}
		}
		catch (Exception e)
		{
			throw new CapabilityManagerException(e);
		}
		return capabilityVOList;
	}

	@Override
	public void addActionsToCapability(
			List<ActionVO> actions, CapabilityVO capabilityVO) throws
			CapabilityManagerException
	{
		try
		{
			Capability capability = capabilityDAO.findData(Capability.class, capabilityVO.getId());
			List<CapabilityAction> actionsToAdd = new ArrayList<CapabilityAction>();
			Action action;
			CapabilityAction capabilityAction;
			for (ActionVO actionVO : actions)
			{
				action = SecurityEntityValueObjectConverter.toAction(actionVO);
				capabilityAction = new CapabilityAction();
				capabilityAction.setAction(action);
				capabilityAction.setCapability(capability);
				actionsToAdd.add(capabilityAction);
			}
			capability.getActions().addAll(actionsToAdd);
			capabilityDAO.update(capability);
		}
		catch (Exception e)
		{
			throw new CapabilityManagerException(e);
		}
	}
}
