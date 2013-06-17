/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 27-Apr-2013
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
package com.rdonasco.common.utils;

import com.rdonasco.common.exceptions.CollectionMergeException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author Roy F. Donasco
 */
public class CollectionsUtility
{

	public static <T extends Collection> T updateCollection(
			final T updatedCollection,
			final T currentCollection) throws
			CollectionMergeException
	{
		return updateCollection(updatedCollection, currentCollection, null, null);
	}

	public static <T extends Collection> T updateCollection(
			final T updatedCollection,
			final T currentCollection,
			CollectionItemDeleteStrategy deleteStrategy) throws
			CollectionMergeException
	{
		return updateCollection(updatedCollection, currentCollection, deleteStrategy, null);
	}

	public static <T extends Collection> T updateCollection(
			final T updatedCollection,
			final T currentCollection,
			CollectionItemUpdateStrategy updateStrategy) throws
			CollectionMergeException
	{
		return updateCollection(updatedCollection, currentCollection, null, updateStrategy);
	}

	public static <T extends Collection> T updateCollection(
			final T updatedCollection,
			final T existingCollection,
			final CollectionItemDeleteStrategy deleteStrategy,
			final CollectionItemUpdateStrategy updateStrategy) throws
			CollectionMergeException
	{
		try
		{
			T mergedCollection = (T) updatedCollection.getClass().newInstance();
			mergedCollection.addAll(existingCollection);
			// find actionst to add
			List objectsToAdd = new ArrayList();
			for (Object objectToAdd : updatedCollection)
			{
				if (!mergedCollection.contains(objectToAdd))
				{
					objectsToAdd.add(objectToAdd);
				}
			}
			List objectsToRemove = new ArrayList();
			// find actions to delete
			for (Object objectToRemove : mergedCollection)
			{
				if (!updatedCollection.contains(objectToRemove))
				{
					objectsToRemove.add(objectToRemove);
				}
			}

			// collect objects to update
			Map objectsToUpdate = new HashMap();
			for (Object objectToUpdate : mergedCollection)
			{
				if (updatedCollection.contains(objectToUpdate))
				{
					objectsToUpdate.put(objectToUpdate, objectToUpdate);
				}
			}
			for (Object updatedObject : updatedCollection)
			{
				Object objectToUpdate = objectsToUpdate.get(updatedObject);
				if (null != objectToUpdate && null == updateStrategy)
				{
					BeanUtils.copyProperties(objectToUpdate, updatedObject);
				}
				else if (null != objectToUpdate && null != updateStrategy)
				{
					updateStrategy.update(objectToUpdate, updatedObject);
				}

			}

			// remove items
			for (Object objectToRemove : objectsToRemove)
			{
				mergedCollection.remove(objectToRemove);
				if (null != deleteStrategy)
				{
					deleteStrategy.delete(objectToRemove);
				}
			}

			// add items
			for (Object objectToAdd : objectsToAdd)
			{
				mergedCollection.add(objectToAdd);
			}
			return mergedCollection;
		}
		catch (Exception ex)
		{
			throw new CollectionMergeException(ex);
		}
	}

	public interface CollectionItemDeleteStrategy<T>
	{

		void delete(T itemToDelete) throws CollectionMergeException;
	}

	public interface CollectionItemUpdateStrategy<T>
	{

		void update(T itemToUpdate, T itemToCopy) throws
				CollectionMergeException;
	}
}
