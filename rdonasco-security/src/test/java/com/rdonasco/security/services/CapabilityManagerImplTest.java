/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.services;

import com.rdonasco.common.exceptions.NonExistentEntityException;
import com.rdonasco.security.dao.ActionDAO;
import com.rdonasco.security.dao.CapabilityDAO;
import com.rdonasco.security.dao.ResourceDAO;
import com.rdonasco.security.exceptions.NotSecuredResourceException;
import com.rdonasco.security.model.Action;
import com.rdonasco.security.model.Capability;
import com.rdonasco.security.model.Resource;
import com.rdonasco.security.utils.SecurityEntityValueObjectConverter;
import com.rdonasco.security.utils.SecurityEntityValueObjectDataUtility;
import com.rdonasco.security.vo.ActionVO;
import com.rdonasco.security.vo.ResourceVO;
import com.rdonasco.security.vo.ResourceVOBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 *
 * @author Roy F. Donasco
 */
public class CapabilityManagerImplTest
{
	private static final Logger LOG = Logger.getLogger(CapabilityManagerImplTest.class.getName());

	private static CapabilityDAO capabilityDAOMock;// =mock(CapabilityDAO.class);
	private static ResourceDAO resourceDAOMock;
	private static ActionDAO actionDAOMock;

	public CapabilityManagerImplTest()
	{
	}

	@BeforeClass
	public static void setUpClass()
	{
		capabilityDAOMock = mock(CapabilityDAO.class);
		resourceDAOMock = mock(ResourceDAO.class);
		actionDAOMock = mock(ActionDAO.class);
	}

	@AfterClass
	public static void tearDownClass()
	{
	}

	@Before
	public void setUp()
	{
		reset(capabilityDAOMock);
		reset(resourceDAOMock);
		reset(actionDAOMock);
	}

	@After
	public void tearDown()
	{
	}

	/**
	 * Test of removeResource method, of class CapabilityManagerImpl.
	 */
	@Test
	public void testRemoveResource() throws Exception
	{
		System.out.println("removeResource");
		ResourceVO resourceVO = createTestDataResourceVO();
		CapabilityManagerImpl instance = prepareCapabilityManagerInstanceToTest();
		instance.removeResource(resourceVO);
		verify(resourceDAOMock, times(1)).delete(Resource.class, resourceVO.getId());
	}

	/**
	 * Test of createNewAction method, of class CapabilityManagerImpl.
	 */
	@Test
	public void testCreateNewAction() throws Exception
	{
		System.out.println("createNewAction");
		ActionVO action = ActionVO.createWithNameAndDescription("testName", "testDescription");
		CapabilityManagerImpl instance = prepareCapabilityManagerInstanceToTest();
		doAnswer(new Answer()
		{
			public Object answer(InvocationOnMock invocation)
			{
				Object returnValue = null;
				try
				{
					Object[] args = invocation.getArguments();
					Action action = (Action) args[0];
					action.setId(100L);
					returnValue = action;
				}
				catch (Exception ex)
				{
					LOG.log(Level.SEVERE, ex.getMessage(), ex);
				}
				return returnValue;
			}
		})
		.when(actionDAOMock).create(SecurityEntityValueObjectConverter.toAction(action));
		
		ActionVO result = instance.createNewAction(action);
		assertNotNull(result.getId());
		assertEquals(100,result.getId().longValue());
		assertEquals(action.getName(), result.getName());
		assertEquals(action.getDescription(), result.getDescription());
	}

	@Test
	public void testSuccessfulFindResourceNamed() throws Exception
	{
		System.out.println("successfulFindResourceNamed");
		CapabilityManagerImpl instance = prepareCapabilityManagerInstanceToTest();
		Resource returnedResource = new Resource();
		returnedResource.setId(Long.MIN_VALUE);
		returnedResource.setName("anyResource");
		when(resourceDAOMock.findUniqueDataUsingNamedQuery(anyString(), anyMapOf(String.class, Object.class)))
				.thenReturn(returnedResource);
		ResourceVO foundResource = instance.findResourceNamedAs(returnedResource.getName());
		assertNotNull(foundResource);
	}

	@Test(expected = NonExistentEntityException.class)
	public void testNoRecordFoundInFindResourceNamed() throws Exception
	{
		System.out.println("noRecordFoundInFindResourceNamed");
		CapabilityManagerImpl instance = prepareCapabilityManagerInstanceToTest();
		Resource returnedResource = new Resource();
		returnedResource.setId(Long.MIN_VALUE);
		returnedResource.setName("anyResource");
		when(resourceDAOMock.findUniqueDataUsingNamedQuery(anyString(), anyMapOf(String.class, Object.class)))
				.thenThrow(NonExistentEntityException.class);
		instance.findResourceNamedAs(returnedResource.getName());
	}

	private List<Capability> getCapabilityOnEditingUser()
	{		
		List<Capability> capabilities = new ArrayList<Capability>();
		capabilities.add(SecurityEntityValueObjectDataUtility.createTestDataCapabilityOnResourceAndAction("User", "Edit"));
		return capabilities;
	}

	@Test
	public void testSuccessfulFindSecuredResourceNamed() throws Exception
	{
		System.out.println("successfulFindSecuredResourceNamed");
		CapabilityManagerImpl instance = prepareCapabilityManagerInstanceToTest();
		Resource returnedResource = new Resource();
		returnedResource.setId(Long.MIN_VALUE);
		returnedResource.setName("anyResource");
		when(capabilityDAOMock.findAllDataUsingNamedQuery(anyString(), anyMapOf(String.class, Object.class)))
				.thenReturn(getCapabilityOnEditingUser());
		ResourceVO foundResource = instance.findOrAddSecuredResourceNamedAs(returnedResource.getName());
		assertNotNull(foundResource);
	}

	@Test(expected = NotSecuredResourceException.class)
	public void testNotFoundSecuredResourceNamed() throws Exception
	{
		System.out.println("notFoundSecuredResourceNamed");
		CapabilityManagerImpl instance = prepareCapabilityManagerInstanceToTest();
		Resource returnedResource = new Resource();
		returnedResource.setId(Long.MIN_VALUE);
		returnedResource.setName("anyResource");
		when(capabilityDAOMock.findAllDataUsingNamedQuery(anyString(), anyMapOf(String.class, Object.class)))
				.thenReturn(null);
		when(resourceDAOMock.findUniqueDataUsingNamedQuery(anyString(), anyMapOf(String.class, Object.class)))
				.thenThrow(NonExistentEntityException.class);
		instance.findOrAddSecuredResourceNamedAs(returnedResource.getName());
		verify(resourceDAOMock, times(1)).create(returnedResource);
	}

	@Test
	public void testFindOrAddNonExistingActionNamed() throws Exception
	{
		System.out.println("findOrAddNonExistingActionNamed");
		CapabilityManagerImpl instance = prepareCapabilityManagerInstanceToTest();
		String actionName = "Edit";
		Action action = new Action();
		action.setName(actionName);
		action.setDescription(actionName);
		when(actionDAOMock.findUniqueDataUsingNamedQuery(anyString(), anyMapOf(String.class, Object.class))).thenThrow(NonExistentEntityException.class);
		instance.findOrAddActionNamedAs(actionName);
		verify(actionDAOMock, times(1)).create(action);

	}

	@Test
	public void testFindOrAddAnExistingActionNamed() throws Exception
	{
		System.out.println("findOrAddAnExistingActionNamed");
		CapabilityManagerImpl instance = prepareCapabilityManagerInstanceToTest();
		String actionName = "Edit";
		Action action = new Action();
		action.setId(Long.MIN_VALUE);
		action.setName(actionName);
		action.setDescription(actionName);
		when(actionDAOMock.findUniqueDataUsingNamedQuery(anyString(), anyMapOf(String.class, Object.class))).thenReturn(action);
		ActionVO foundAction = instance.findOrAddActionNamedAs(actionName);
		verify(actionDAOMock, times(1)).findUniqueDataUsingNamedQuery(anyString(), anyMapOf(String.class, Object.class));
		assertEquals("action.id did not match", action.getId(), foundAction.getId());
	}

	private CapabilityManagerImpl prepareCapabilityManagerInstanceToTest()
	{
		CapabilityManagerImpl capabilityManager = new CapabilityManagerImpl();
		capabilityManager.setActionDAO(actionDAOMock);
		capabilityManager.setCapabilityDAO(capabilityDAOMock);
		capabilityManager.setResourceDAO(resourceDAOMock);
		return capabilityManager;
	}

	private ResourceVO createTestDataResourceVO()
	{
		ResourceVO resourceVO = new ResourceVOBuilder()
				.setId(Long.MIN_VALUE)
				.setName("test name")
				.setDescription("test description")
				.createResourceVO();
		return resourceVO;
	}
}
