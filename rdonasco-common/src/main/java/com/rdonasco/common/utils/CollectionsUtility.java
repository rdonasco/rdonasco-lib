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
import java.util.List;

/**
 *
 * @author Roy F. Donasco
 */
public class CollectionsUtility
{
	public static <T extends Collection> T updateCollection(T updatedCollection,
			T currentCollection) throws
			CollectionMergeException
	{
		return updateCollection(updatedCollection, currentCollection, null);
	}

	public static <T extends Collection> T updateCollection(
			final T updatedActions,
			final T actionsToUpdate,
			final CollectionItemDeleteStrategy deleteStrategy) throws
			CollectionMergeException
	{
		try
		{
			T currentActions = (T) updatedActions.getClass().newInstance();
			currentActions.addAll(actionsToUpdate);
			// find actionst to add
			List actionsToAdd = new ArrayList();
			for (Object actionToAdd : updatedActions)
			{
				if (!currentActions.contains(actionToAdd))
				{
					actionsToAdd.add(actionToAdd);
				}
			}
			List actionsToRemove = new ArrayList();
			// find actions to delete
			for (Object actionToRemove : currentActions)
			{
				if (!updatedActions.contains(actionToRemove))
				{
					actionsToRemove.add(actionToRemove);
				}
			}

			// remove items
			for (Object actionToRemove : actionsToRemove)
			{
				currentActions.remove(actionToRemove);
				if (null != deleteStrategy)
				{
					deleteStrategy.delete(actionToRemove);
				}
			}

			// add items
			for (Object actionToAdd : actionsToAdd)
			{
				currentActions.add(actionToAdd);
			}
			return currentActions;
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
}
