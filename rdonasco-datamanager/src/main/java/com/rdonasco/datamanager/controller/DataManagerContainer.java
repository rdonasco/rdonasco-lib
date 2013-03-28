/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 27-Mar-2013
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
package com.rdonasco.datamanager.controller;

import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.datamanager.services.DataManager;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import java.util.List;
import javax.enterprise.context.Dependent;

/**
 *
 * @author Roy F. Donasco
 */
@Dependent
public class DataManagerContainer<T> extends BeanItemContainer<T>
{

	private DataManager<T> dataManager;
	private static final long serialVersionUID = 1L;
	private T dummyAddRecord;

	public DataManagerContainer(Class<? super T> t)
	{
		super(t);
	}
	private DataRetrieveListStrategy<T> dataRetrieveListStrategy = new DataRetrieveListStrategy<T>()
	{
		@Override
		public List<T> retrieve() throws DataAccessException
		{
			checkForNullDataManager();
			return getDataManager().retrieveAllData();
		}
	};
	private DataSaveStrategy<T> dataSaveStrategy = new DataSaveStrategy<T>()
	{
		@Override
		public T save(T dataToSave) throws DataAccessException
		{
			checkForNullDataManager();
			return getDataManager().saveData(dataToSave);
		}
	};
	private DataDeleteStrategy<T> dataDeleteStrategy = new DataDeleteStrategy<T>()
	{
		@Override
		public void delete(T dataToDelete) throws DataAccessException
		{
			checkForNullDataManager();
			getDataManager().deleteData(dataToDelete);
		}
	};
	private DataUpdateStrategy<T> dataUpdateStrategy = new DataUpdateStrategy<T>()
	{
		@Override
		public void update(T dataToUpdate) throws DataAccessException
		{
			checkForNullDataManager();
			getDataManager().updateData(dataToUpdate);
		}
	};

	public T getDummyAddRecord()
	{
		return dummyAddRecord;
	}

	public void setDummyAddRecord(T dummyAddRecord)
	{
		this.dummyAddRecord = dummyAddRecord;
	}

	public DataDeleteStrategy<T> getDataDeleteStrategy()
	{
		return dataDeleteStrategy;
	}

	public void setDataDeleteStrategy(
			DataDeleteStrategy<T> dataDeleteStrategy)
	{
		this.dataDeleteStrategy = dataDeleteStrategy;
	}

	public DataUpdateStrategy<T> getDataUpdateStrategy()
	{
		return dataUpdateStrategy;
	}

	public void setDataUpdateStrategy(
			DataUpdateStrategy<T> dataUpdateStrategy)
	{
		this.dataUpdateStrategy = dataUpdateStrategy;
	}

	public DataRetrieveListStrategy<T> getDataRetrieveListStrategy()
	{
		return dataRetrieveListStrategy;
	}

	public void setDataRetrieveListStrategy(
			DataRetrieveListStrategy<T> dataRetrieveListStrategy)
	{
		this.dataRetrieveListStrategy = dataRetrieveListStrategy;
	}

	public DataSaveStrategy<T> getDataSaveStrategy()
	{
		return dataSaveStrategy;
	}

	public void setDataSaveStrategy(
			DataSaveStrategy<T> dataSaveStrategy)
	{
		this.dataSaveStrategy = dataSaveStrategy;
	}

	public DataManager<T> getDataManager()
	{
		return dataManager;
	}

	public void setDataManager(
			DataManager<T> dataManager)
	{
		this.dataManager = dataManager;
	}

	public void refresh() throws DataAccessException
	{

		List<T> allData = getDataRetrieveListStrategy().retrieve();
		for (T data : allData)
		{
			super.addItem(data);
		}
		if (getDummyAddRecord() != null)
		{
			super.addItem(getDummyAddRecord());
		}
	}

	@Override
	public boolean removeItem(Object itemId)
	{
		try
		{
			BeanItem<T> item = super.getItem(itemId);
			if (null == item)
			{
				throw new IllegalArgumentException(String.format("item ID %s not found in collection.", itemId));
			}
			getDataDeleteStrategy().delete(item.getBean());
			return super.removeItem(itemId);
		}
		catch (DataAccessException ex)
		{
			throw new RuntimeException(ex);
		}
	}

	@Override
	public BeanItem<T> addItemAfter(Object previousItemId, Object newItemId)
			throws IllegalArgumentException
	{
		return super.addItemAfter(previousItemId, saveUsingSaveDataStrategy(newItemId));
	}

	@Override
	public BeanItem<T> addItemAt(int index, Object newItemId) throws
			IllegalArgumentException
	{
		return super.addItemAt(index, saveUsingSaveDataStrategy(newItemId));
	}

	@Override
	public BeanItem<T> addItem(Object itemId)
	{
		BeanItem<T> addedBeanItem;
		if (getDummyAddRecord() != null)
		{
			int referenceIndex = super.indexOfId(getDummyAddRecord());
			addedBeanItem = super.addItemAt(referenceIndex, saveUsingSaveDataStrategy(itemId));
		}
		else
		{
			addedBeanItem = super.addItem(saveUsingSaveDataStrategy(itemId));
		}
		return addedBeanItem;
	}

	private T saveUsingSaveDataStrategy(Object newItemId) throws
			RuntimeException
	{
		T savedData = null;
		try
		{			
			savedData = getDataSaveStrategy().save((T) newItemId);
		}
		catch (DataAccessException ex)
		{
			throw new RuntimeException(ex);
		}
		return savedData;
	}

	private void checkForNullDataManager() throws NullPointerException
	{
		if (getDataManager() == null)
		{
			throw new NullPointerException("DataManager cannot be null for DataManagerContainer to operate properly");
		}
	}
}
