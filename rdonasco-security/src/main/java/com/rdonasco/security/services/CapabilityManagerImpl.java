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
import com.rdonasco.security.model.Resource;
import com.rdonasco.security.utils.SecurityEntityValueObjectConverter;
import com.rdonasco.security.vo.ActionVO;
import com.rdonasco.security.vo.ResourceVO;
import com.rdonasco.security.vo.ResourceVOBuilder;
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
		}
		catch (Exception e)
		{
			throw new CapabilityManagerException(e);
		}
		return resourceVO;
	}

	@Override
	public void updateResource(ResourceVO resource) throws CapabilityManagerException
	{
		try
		{
			Resource resourceToUpdate = SecurityEntityValueObjectConverter.toResource(resource);
			resourceDAO.update(resourceToUpdate);
		}
		catch(Exception e)
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
	public ResourceVO findOrAddSecuredResourceNamedAs(String resourceName)
			throws CapabilityManagerException, NotSecuredResourceException
	{
		Resource securedResource = null;
		ResourceVO securedResourceVO = null;
		try
		{
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(Capability.QUERY_PARAM_RESOURCE, resourceName);
			List<Capability> capabilities = capabilityDAO.findAllDataUsingNamedQuery(Capability.NAMED_QUERY_FIND_BY_RESOURCE_NAME, parameters);
			if (null != capabilities && !capabilities.isEmpty())
			{
				securedResource = capabilities.get(0).getResource();
			}
			else
			{
				try
				{
					findResourceNamedAs(resourceName);
				}
				catch (NonExistentEntityException e)
				{
					ResourceVO resourceToAdd = new ResourceVOBuilder()
							.setName(resourceName)
							.setDescription(resourceName)
							.createResourceVO();
					addResource(resourceToAdd);
				}
			}
			if (null == securedResource)
			{
				throw new NotSecuredResourceException("Not Secured Resource. Can be accessed by anyone.");
			}
			securedResourceVO = SecurityEntityValueObjectConverter.toResourceVO(securedResource);
		}
		catch (NotSecuredResourceException e)
		{
			throw e;
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
			throws DataAccessException, NonExistentEntityException, MultipleEntityFoundException
	{
		Action action;
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(Action.QUERY_PARAM_NAME, name);

		action = actionDAO.findUniqueDataUsingNamedQuery(Action.NAMED_QUERY_FIND_ACTION_BY_NAME, parameters);

		return action;
	}

	@Override
	public ActionVO findActionNamed(String nameOfActionToFind) throws CapabilityManagerException,
			NonExistentEntityException
	{
		ActionVO foundAction = null;
		try
		{
			Action action = findActionEntityNamed(nameOfActionToFind);
			foundAction = SecurityEntityValueObjectConverter.toActionVO(action);
		}
		catch(NonExistentEntityException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new CapabilityManagerException(e);
		}
		return foundAction;
	}
	
	
}
