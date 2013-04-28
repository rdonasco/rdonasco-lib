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

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import junit.framework.TestCase;

/**
 *
 * @author Roy F. Donasco
 */
public class CollectionsUtilityTest extends TestCase
{

	private static final Logger LOG = Logger.getLogger(CollectionsUtilityTest.class.getName());

	public CollectionsUtilityTest(String testName)
	{
		super(testName);
	}

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	/**
	 * Test of updateCollection method, of class CollectionsUtility.
	 */
	public void testAddItemInCollection() throws Exception
	{
		System.out.println("addItemInCollection");
		Collection currentCollection = prepareCurrentCollection();
		DataElement elementToAdd = new DataElement(150, "Brian");
		ArrayList updatedCollection = new ArrayList(currentCollection);
		updatedCollection.add(elementToAdd);
		Collection result = CollectionsUtility.updateCollection(updatedCollection, currentCollection);
		assertFalse("failed to update collection", result.isEmpty());
		assertTrue("failed to add " + elementToAdd, result.contains(elementToAdd));
		assertEquals("invalid size", 4, result.size());
		printCollectionContents(result);
	}

	public void testRemoveItemFromCollection() throws Exception
	{
		System.out.println("removeItemFromCollecction");
		Collection currentCollection = prepareCurrentCollection();
		DataElement elementToRemove = new DataElement(200, "Heidi");
		ArrayList updatedCollection = new ArrayList(currentCollection);
		updatedCollection.remove(elementToRemove);
		Collection result = CollectionsUtility.updateCollection(updatedCollection, currentCollection);
		assertFalse("failed to update collection", result.isEmpty());
		assertFalse("failed to remove " + elementToRemove, result.contains(elementToRemove));
		assertEquals("failed to remove", 2, result.size());
		printCollectionContents(result);
	}

	public void testUpdateItemFromCollection() throws Exception
	{
		System.out.println("updateItemFromCollecction");
		Collection currentCollection = prepareCurrentCollection();
		DataElement elementToUpdate = new DataElement(200, "Heidi");
		ArrayList updatedCollection = new ArrayList(currentCollection);
		updatedCollection.remove(elementToUpdate);
		elementToUpdate.setName("Larissa");
		updatedCollection.add(elementToUpdate);
		Collection<DataElement> result = CollectionsUtility.updateCollection(updatedCollection, currentCollection);
		assertFalse("failed to update collection", result.isEmpty());
		assertTrue("failed to find " + elementToUpdate, result.contains(elementToUpdate));
		assertEquals("invalid size", 3, result.size());
		printCollectionContents(result);
//		for (DataElement data : result)
//		{
//			if (elementToUpdate.equals(data))
//			{
//				assertEquals("failed to update to " + elementToUpdate.getName(), elementToUpdate.getName(), data.getName());
//			}
//		}
	}

	private Collection prepareCurrentCollection()
	{
		ArrayList currentCollection = new ArrayList();
		currentCollection.add(new DataElement(100, "Roy"));
		currentCollection.add(new DataElement(200, "Heidi"));
		currentCollection.add(new DataElement(300, "Denise"));
		return currentCollection;
	}

	private void printCollectionContents(Collection result)
	{
		for (Object object : result)
		{
			LOG.log(Level.INFO, "object = {0}", object);
		}
	}

	public class DataElement
	{

		private int id;
		private String name;

		DataElement(int id, String name)
		{
			this.id = id;
			this.name = name;
		}

		public int getId()
		{
			return id;
		}

		public void setId(int id)
		{
			this.id = id;
		}

		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}

		@Override
		public int hashCode()
		{
			int hash = 5;
			hash = 97 * hash + this.id;
			return hash;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (obj == null)
			{
				return false;
			}
			if (getClass() != obj.getClass())
			{
				return false;
			}
			final DataElement other = (DataElement) obj;
			if (this.id != other.id)
			{
				return false;
			}
			return true;
		}

		@Override
		public String toString()
		{
			return "DataElement{" + "id=" + id + ", name=" + name + '}';
		}
	};
}
