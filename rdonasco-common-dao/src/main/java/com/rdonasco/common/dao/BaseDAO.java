/*
 *  Copyright 2010 Roy F. Donasco.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package com.rdonasco.common.dao;

import com.rdonasco.common.dao.utils.DAOCommonConstants;
import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.common.exceptions.IllegalOrphanException;
import com.rdonasco.common.exceptions.MultipleEntityFoundException;
import com.rdonasco.common.exceptions.NonExistentEntityException;
import com.rdonasco.common.exceptions.PreexistingEntityException;
import com.rdonasco.common.i18.I18NResource;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author Roy F. Donasco
 */
public abstract class BaseDAO<T> implements DataAccess<T>
{

	public BaseDAO()
	{
	}

	public abstract EntityManager getEntityManager();

	@Override
	public void create(T data) throws PreexistingEntityException, Exception
	{
		try
		{
			getEntityManager().persist(data);
		}
		catch (Exception ex)
		{
			try
			{
				checkIfAlreadyExist(data, ex);
			}
			catch (PreexistingEntityException e)
			{
				throw e;
			}
			catch (Exception e)
			{
				throw ex;
			}
		}
	}

	protected void checkIfAlreadyExist(T data, Exception ex) throws
			IllegalAccessException, IllegalArgumentException,
			PreexistingEntityException
	{
		Object id = extractIdentifier(data);
		String canonicalName = data.getClass().getCanonicalName();
		if (findData(id) != null)
		{
			throw new PreexistingEntityException(I18NResource.
					localizeWithParameter(canonicalName + " _ already exists.",
					data), ex);
		}
	}

	@Override
	public void delete(Object id) throws
			IllegalOrphanException, NonExistentEntityException
	{
		EntityManager em = getEntityManager();
		T data;
		try
		{
			data = em.find(getDataClass(), id);
		}
		catch (EntityNotFoundException enfe)
		{
			throw new NonExistentEntityException(I18NResource
					.localizeWithParameter(DAOCommonConstants.ID_NO_LONGER_EXIST, getDataClass().getCanonicalName(), id), enfe);
		}
		em.remove(data);

	}

	@Override
	public void update(T data) throws IllegalOrphanException,
			NonExistentEntityException, Exception
	{
		try
		{
			EntityManager em = getEntityManager();
			em.merge(data);
		}
		catch (Exception ex)
		{
			String msg = ex.getLocalizedMessage();
			if (msg == null || msg.length() == 0)
			{
				Long id = (Long) extractIdentifier(data);
				if (findData(id) == null)
				{
					throw new NonExistentEntityException(I18NResource
							.localizeWithParameter(DAOCommonConstants.ID_NO_LONGER_EXIST, data.getClass().getCanonicalName(), id), ex);
				}
			}
			throw ex;
		}
	}

	private Object extractIdentifier(T data) throws IllegalArgumentException,
			IllegalAccessException
	{
		Object id = null;
		for (Field field : data.getClass().getFields())
		{
			Id idField = field.getAnnotation(Id.class);
			if (idField != null)
			{
				id = field.get(data);
				break;
			}
		}

		return id;
	}

	@Override
	public List<T> findAllData()
	{
		return findAllData(true, -1, -1);
	}

	@Override
	public List<T> findAllData(int maxResults, int firstResult)
	{
		return findAllData(false, maxResults, firstResult);
	}

	private List<T> findAllData(boolean all, int maxResults, int firstResult)
	{
		EntityManager em = getEntityManager();
		CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
		cq.select(cq.from(getDataClass()));
		Query q = em.createQuery(cq);
		if (!all)
		{
			q.setMaxResults(maxResults);
			q.setFirstResult(firstResult);
		}
		return q.getResultList();
	}

	@Override
	public T findData(Object id)
	{
		EntityManager em = getEntityManager();
		T data = em.find(getDataClass(), id);
		return data;
	}

	@Override
	public T findFreshData(Object id)
	{
		EntityManager em = getEntityManager();
		T data = em.find(getDataClass(), id);
		if (null != data)
		{
			em.refresh(data);
		}

		return data;
	}

	@Override
	public List<T> findAllDataUsingNamedQuery(String namedQuery,
			Map<String, Object> parameters)
			throws DataAccessException
	{
		List<T> dataList = null;
		try
		{
			EntityManager em = getEntityManager();
			TypedQuery<T> query = em.createNamedQuery(namedQuery, getDataClass());
			if (parameters != null)
			{
				for (String paramName : parameters.keySet())
				{
					query.setParameter(paramName, parameters.get(paramName));
				}
			}

			dataList = query.getResultList();
		}
		catch (Exception e)
		{
			throw new DataAccessException(e);
		}
		return dataList;
	}

	@Override
	public T findUniqueDataUsingNamedQuery(String namedQuery,
			Map<String, Object> parameters)
			throws DataAccessException, NonExistentEntityException,
			MultipleEntityFoundException
	{
		T foundData = null;
		try
		{
			EntityManager em = getEntityManager();
			TypedQuery<T> query = em.createNamedQuery(namedQuery, getDataClass());
			if (parameters != null)
			{
				for (String paramName : parameters.keySet())
				{
					query.setParameter(paramName, parameters.get(paramName));
				}
			}

			foundData = query.getSingleResult();
		}
		catch (NoResultException e)
		{
			throw new NonExistentEntityException(e);
		}
		catch (Exception e)
		{
			throw new DataAccessException(e);
		}
		return foundData;
	}

	@Override
	public int executeUpdateUsingNamedQuery(String namedQuery,
			Map<String, Object> parameters) throws DataAccessException
	{
		// To change body of generated methods, choose Tools | Templates.
		// TODO: Complete code for method executeUpdateUsingNamedQuery
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
