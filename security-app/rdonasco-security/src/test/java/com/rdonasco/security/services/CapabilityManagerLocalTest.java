package com.rdonasco.security.services;

import com.rdonasco.common.exceptions.CollectionMergeException;
import com.rdonasco.common.exceptions.NonExistentEntityException;
import com.rdonasco.common.utils.CollectionsUtility;
import com.rdonasco.config.dao.ConfigElementDAO;
import com.rdonasco.config.data.ConfigElement;
import com.rdonasco.config.parsers.ValueParser;
import com.rdonasco.config.services.ConfigDataManagerLocal;
import com.rdonasco.config.util.ConfigDataValueObjectConverter;
import com.rdonasco.config.vo.ConfigAttributeVO;
import com.rdonasco.security.dao.ActionDAO;
import com.rdonasco.security.model.Action;
import com.rdonasco.security.services.validators.CapabilityValidator;
import com.rdonasco.security.utils.CapabilityTestUtility;
import com.rdonasco.security.vo.ActionVO;
import com.rdonasco.security.vo.CapabilityActionVO;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.CapabilityVOBuilder;
import com.rdonasco.security.vo.ResourceVO;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.rdonasco.security.utils.ArchiveCreator;
import com.rdonasco.security.utils.SecurityConstants;
import com.rdonasco.security.vo.ApplicationVO;
import com.rdonasco.security.vo.ApplicationVOBuilder;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 * Unit test for simple App.
 */
@RunWith(Arquillian.class)
public class CapabilityManagerLocalTest
{

	private static final Logger LOG = Logger.getLogger(CapabilityManagerLocalTest.class.getName());
	private static int KEY = 0;
	
	@EJB
	private CapabilityManagerLocal capabilityManager;
	@EJB
	private ApplicationManagerLocal applicationManager;
	private CapabilityTestUtility testUtility;

	@Before
	public void setUp()
	{
		testUtility = new CapabilityTestUtility(capabilityManager, applicationManager);
	}

	@Deployment
	public static JavaArchive createTestArchive()
	{
		JavaArchive archive = ArchiveCreator.createCommonArchive()
				.addPackage(ActionDAO.class.getPackage())
				.addPackage(SystemSecurityManagerRemote.class.getPackage())
				.addPackage(ActionVO.class.getPackage())
				.addPackage(Action.class.getPackage())
				.addPackage(CapabilityValidator.class.getPackage())
				.addPackage(ConfigElementDAO.class.getPackage())
				.addPackage(ValueParser.class.getPackage())
				.addPackage(ConfigElement.class.getPackage())
				.addPackage(ConfigDataManagerLocal.class.getPackage())
				.addPackage(ConfigAttributeVO.class.getPackage())
				.addPackage(ConfigDataValueObjectConverter.class.getPackage())				
				.addClass(CollectionsUtility.class)
				.addClass(SecurityConstants.class)
				.addClass(CollectionsUtility.CollectionItemDeleteStrategy.class)
				.addClass(CollectionMergeException.class);
		
		return archive;
	}

	@Test
	public void testAddAction() throws Exception
	{
		ActionVO action = new ActionVO();
		action.setName("test");
		ActionVO savedAction = capabilityManager.createNewAction(action);
		assertNotNull(savedAction.getId());

	}

	@Test
	public void testEditAction() throws Exception
	{
		System.out.println("editAction");
		final String actionName = "edit";
		ActionVO savedAction = testUtility.createTestDataActionNamed(actionName);
		ActionVO foundAction = capabilityManager.findActionNamed(actionName);
		assertNotNull(foundAction);
		assertEquals(savedAction.getId(), foundAction.getId());
		capabilityManager.updateAction(foundAction);
		ActionVO updatedAction = capabilityManager.findActionNamed(actionName);
		assertNotNull(foundAction);
		assertEquals(foundAction.getId(), updatedAction.getId());
	}

	@Test(expected = NonExistentEntityException.class)
	public void testDeleteAction() throws Exception
	{
		System.out.println("deleteAction");
		ActionVO actionToDelete = testUtility.createTestDataActionNamed("actionToDelete");
		capabilityManager.removeAction(actionToDelete);
		capabilityManager.findActionNamed(actionToDelete.getName());

	}

	@Test
	public void testAddResource() throws Exception
	{
		System.out.println("addResource");
		ResourceVO resourceToAdd = testUtility.createTestDataResourceNamed("resourceToAdd");
		assertNotNull(resourceToAdd);
		assertNotNull(resourceToAdd.getId());
	}

	@Test
	public void testEditResource() throws Exception
	{
		System.out.println("editResource");
		ResourceVO resourceToEdit = testUtility.createTestDataResourceNamed("resourceToEdit");
		capabilityManager.updateResource(resourceToEdit);
		ResourceVO updatedResource = capabilityManager.findResourceNamedAs(resourceToEdit.getName());
		assertEquals(resourceToEdit, updatedResource);
	}

	@Test(expected = NonExistentEntityException.class)
	public void testRemoveResource() throws Exception
	{
		System.out.println("removeResource");
		try
		{
			ResourceVO resourceToRemove = testUtility.createTestDataResourceNamed("resourceToRemove");
			capabilityManager.removeResource(resourceToRemove);
			capabilityManager.findResourceNamedAs(resourceToRemove.getName());
		}
		catch (Exception e)
		{
			LOG.log(Level.SEVERE, e.getMessage(), e);
			throw e;
		}
	}

	@Test
	public void testAddCapability() throws Exception
	{
		System.out.println("addCapability");
		final String actionName = "add";
		final String resourceName = "employee";
		final String applicationName = "hrSystem" + (KEY++);
		CapabilityVO savedCapabilityVO = testUtility.createTestDataCapabilityWithActionAndResourceNameOnSystem(actionName, resourceName, applicationName);
		assertNotNull(savedCapabilityVO);
		assertNotNull(savedCapabilityVO.getId());
	}
	
	@Test
	public void testAddCapabilityWithApplication() throws Exception
	{
		System.out.println("addCapabilityWithApplication");
				
		final String actionName = "add"+ (KEY++);
		final String resourceName = "hr.employee"+ (KEY++);
		final String applicationName = "hrjSystem" + (KEY++);
		CapabilityVO savedCapabilityVO = testUtility.createTestDataCapabilityWithActionAndResourceNameOnSystem(actionName, resourceName, applicationName);
		assertNotNull(savedCapabilityVO);
		assertNotNull(savedCapabilityVO.getId());		
		assertNotNull("application was not saved",savedCapabilityVO.getApplicationVO());
	}

	@Test
	public void testUpdateCapabilityDetails() throws Exception
	{
		System.out.println("updateCapabilityDetails");
		final String actionName = "add" + (KEY++);
		final String resourceName = "employee" + (KEY++);
		CapabilityVO capabilityVOtoUpdate = testUtility.createTestDataCapabilityWithActionAndResourceNameOnSystem(actionName, resourceName,"HR System"+ (KEY++));
		capabilityVOtoUpdate.setDescription("updated description");
		capabilityVOtoUpdate.setTitle("updated title");
		ApplicationVO applicationToTest = testUtility.createTestApplicationData("updated application for capability");
		capabilityVOtoUpdate.setApplicationVO(applicationToTest);
		capabilityManager.updateCapability(capabilityVOtoUpdate);		
		CapabilityVO updatedCapabilityVO = capabilityManager.findCapabilityWithId(capabilityVOtoUpdate.getId());
		assertEquals(capabilityVOtoUpdate.getId(), updatedCapabilityVO.getId());
		assertEquals(capabilityVOtoUpdate.getDescription(), updatedCapabilityVO.getDescription());
		assertEquals(capabilityVOtoUpdate.getTitle(), updatedCapabilityVO.getTitle());
		assertEquals(capabilityVOtoUpdate.getActions().size(), updatedCapabilityVO.getActions().size());
		assertEquals(applicationToTest,updatedCapabilityVO.getApplicationVO());
	}

	@Test
	public void testUpdateCapabilityAndAddActions() throws Exception
	{
		System.out.println("updateCapabilityAndAddActions");
		final String actionName = "add" + (KEY++);
		final String resourceName = "employee" + (KEY++);
		CapabilityVO capabilityVOtoUpdate = testUtility.createTestDataCapabilityWithActionAndResourceNameOnSystem(actionName, resourceName, "HR system "+ (KEY++));
		CapabilityActionVO capabilityActionToAdd = testUtility.createTestDataCapabilityActionNamed("edit" + (KEY++), capabilityVOtoUpdate);
		capabilityVOtoUpdate.getActions().add(capabilityActionToAdd);
		capabilityManager.updateCapability(capabilityVOtoUpdate);
		CapabilityVO updatedCapabilityVO = capabilityManager.findCapabilityWithId(capabilityVOtoUpdate.getId());
		assertEquals(2, updatedCapabilityVO.getActions().size());
	}

	@Test
	public void testUpdateCapabilityAndRemoveActions() throws Exception
	{
		System.out.println("updateCapabilityAndAddActions");
		final String actionName = "add" + (KEY++);
		final String resourceName = "employee" + (KEY++);
		CapabilityVO capabilityVOtoUpdate = testUtility.createTestDataCapabilityWithActionAndResourceNameOnSystem(actionName, resourceName, "HR system" + (KEY++));
		CapabilityActionVO capabilityActionToAdd = testUtility.createTestDataCapabilityActionNamed("edit" + (KEY++), capabilityVOtoUpdate);
		capabilityVOtoUpdate.getActions().add(capabilityActionToAdd);
		capabilityManager.updateCapability(capabilityVOtoUpdate);
		CapabilityVO updatedCapabilityVO = capabilityManager.findCapabilityWithId(capabilityVOtoUpdate.getId());
		Iterator<CapabilityActionVO> actionsToRemove = updatedCapabilityVO.getActions().iterator();
		while (actionsToRemove.hasNext())
		{
			actionsToRemove.next();
			actionsToRemove.remove();
			break;
		}
		capabilityManager.updateCapability(updatedCapabilityVO);
		CapabilityVO updatedCapabilityRemovedActionsVO = capabilityManager.findCapabilityWithId(capabilityVOtoUpdate.getId());
		assertEquals(1, updatedCapabilityRemovedActionsVO.getActions().size());
	}

	@Test
	public void testUpdateCapabilityWithRemovedAndAddNewActions() throws
			Exception
	{
		System.out.println("updateCapabilityWithRemovedAndAddNewActions");
		final String actionName = "add" + (KEY++);
		final String resourceName = "employee" + (KEY++);
		CapabilityVO capabilityVOtoUpdate = testUtility.createTestDataCapabilityWithActionAndResourceNameOnSystem(actionName, resourceName," HR System 4"+ (KEY++));
		CapabilityActionVO capabilityActionToAdd = testUtility.createTestDataCapabilityActionNamed("edit" + (KEY++), capabilityVOtoUpdate);
		capabilityVOtoUpdate.getActions().add(capabilityActionToAdd);
		capabilityManager.updateCapability(capabilityVOtoUpdate);
		CapabilityVO updatedCapabilityVO = capabilityManager.findCapabilityWithId(capabilityVOtoUpdate.getId());
		Iterator<CapabilityActionVO> actionsToRemove = updatedCapabilityVO.getActions().iterator();
		while (actionsToRemove.hasNext())
		{
			actionsToRemove.next();
			actionsToRemove.remove();
			break;
		}
		capabilityActionToAdd = testUtility.createTestDataCapabilityActionNamed("delete" + (KEY++), capabilityVOtoUpdate);
		updatedCapabilityVO.getActions().add(capabilityActionToAdd);
		capabilityManager.updateCapability(updatedCapabilityVO);
		CapabilityVO updatedCapabilityRemovedActionsVO = capabilityManager.findCapabilityWithId(capabilityVOtoUpdate.getId());
		assertEquals(2, updatedCapabilityRemovedActionsVO.getActions().size());
		assertNotNull(updatedCapabilityRemovedActionsVO.findActionNamed(capabilityActionToAdd.getActionVO().getName()));
	}

	@Test
	public void testUpdateCapabilityClearAndRemoveSameActions() throws
			Exception
	{
		System.out.println("UpdateCapabilityClearAndRemoveSameActions");
		ResourceVO resourceVO = testUtility.createTestDataResourceNamed("station");
		ApplicationVO applicationVO = new ApplicationVOBuilder()
				.setName("new application for capability" + (KEY++))
				.setToken("token")
				.createApplicationVO();
		ApplicationVO createdApplication = applicationManager.createNewApplication(applicationVO);
		CapabilityVO capability = new CapabilityVOBuilder()
				.setTitle("manage station")
				.setDescription("manage Station")
				.setResource(resourceVO)
				.setApplication(createdApplication)
				.createCapabilityVO();
		capability = capabilityManager.createNewCapability(capability);
		List<ActionVO> actions = testUtility.createTestDataActions("add" + (KEY++), "edit" + (KEY++), "delete" + (KEY++));
		testUtility.createAndAssociateActionsToCapability(actions, capability);
		capabilityManager.updateCapability(capability);
		// reassociate it as new
		CapabilityVO updatedCapabilityWithActionsToReassociate = capabilityManager.findCapabilityWithId(capability.getId());
		updatedCapabilityWithActionsToReassociate.getActions().clear();
		testUtility.createAndAssociateActionsToCapability(actions, updatedCapabilityWithActionsToReassociate);
		capabilityManager.updateCapability(updatedCapabilityWithActionsToReassociate);
		CapabilityVO updatedCapabilityWithActions = capabilityManager.findCapabilityWithId(updatedCapabilityWithActionsToReassociate.getId());
		assertEquals(3, updatedCapabilityWithActions.getActions().size());

	}

	@Test
	public void testDeleteCapability() throws Exception
	{
		System.out.println("deleteCapability");
		final String actionName = "delete" + (KEY++);
		final String resourceName = "employee" + (KEY++);
		CapabilityVO capabilityVOtoDelete = testUtility.createTestDataCapabilityWithActionAndResourceNameOnSystem(actionName, resourceName, "HR System 5"+ (KEY++));
		capabilityManager.removeCapability(capabilityVOtoDelete);
		CapabilityVO foundCapabilityVO = capabilityManager.findCapabilityWithId(capabilityVOtoDelete.getId());
		assertNull(foundCapabilityVO);
	}

	@Test
	public void testFindCapabilityWithTitle() throws Exception
	{
		System.out.println("FindCapabilityWithTitle");
		System.out.println("addCapability");
		final String actionName = "logon";
		final String resourceName = "system";
		CapabilityVO savedCapabilityVO = testUtility.createTestDataCapabilityWithActionAndResourceNameOnSystem(actionName, resourceName,"HR SYSTEM 6"+ (KEY++));

		CapabilityVO foundCapabilityVO = capabilityManager.findCapabilityWithTitle(savedCapabilityVO.getTitle());
		assertNotNull(foundCapabilityVO);
		assertEquals(savedCapabilityVO.getTitle(), foundCapabilityVO.getTitle());
	}

	@Test
	public void testAddActionsToCapability() throws Exception
	{
		System.out.println("addActionsToCapability");
		ActionVO actionToAdd = testUtility.createTestDataActionNamed("missingInAction");
		CapabilityVO capabilityToUpdate = testUtility.createTestDataCapabilityWithActionAndResourceNameOnSystem("currentAction", "newResource","hr SYSTEM 7"+ (KEY++));
		List<ActionVO> actionsToAdd = new ArrayList<ActionVO>();
		actionsToAdd.add(actionToAdd);
		capabilityManager.addActionsToCapability(actionsToAdd, capabilityToUpdate);
		CapabilityVO updatedCapability = capabilityManager.findCapabilityWithId(capabilityToUpdate.getId());
		updatedCapability.findActionNamed("missingInAction");

	}
	//--- no more test beyond this point.
}
