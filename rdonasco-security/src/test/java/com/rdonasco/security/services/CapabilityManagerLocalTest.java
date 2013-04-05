package com.rdonasco.security.services;

import com.rdonasco.common.exceptions.NonExistentEntityException;
import com.rdonasco.security.dao.ActionDAO;
import com.rdonasco.security.exceptions.CapabilityManagerException;
import com.rdonasco.security.model.Action;
import com.rdonasco.security.vo.ActionVO;
import com.rdonasco.security.vo.CapabilityActionVO;
import com.rdonasco.security.vo.CapabilityActionVOBuilder;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.CapabilityVOBuilder;
import com.rdonasco.security.vo.ResourceVO;
import com.rdonasco.security.vo.ResourceVOBuilder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import test.util.ArchiveCreator;
import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
@RunWith(Arquillian.class)
public class CapabilityManagerLocalTest
{

	@EJB
	private CapabilityManagerRemote capabilityManager;

	@Deployment
	public static JavaArchive createTestArchive()
	{
		JavaArchive archive = ArchiveCreator.createCommonArchive()
				.addPackage(ActionDAO.class.getPackage())
				.addPackage(SystemSecurityManagerRemote.class.getPackage())
				.addPackage(ActionVO.class.getPackage())
				.addPackage(Action.class.getPackage());

		return archive;
	}

	@Test
	public void testAddAction() throws Exception
	{
		ActionVO action = new ActionVO();
		action.setDescription("test");
		action.setName("test");
		ActionVO savedAction = capabilityManager.createNewAction(action);
		assertNotNull(savedAction.getId());

	}

	@Test
	public void testEditAction() throws Exception
	{
		System.out.println("editAction");
		final String actionName = "edit";
		ActionVO savedAction = createTestDataActionNamed(actionName);
		ActionVO foundAction = capabilityManager.findActionNamed(actionName);
		assertNotNull(foundAction);
		assertEquals(savedAction.getId(), foundAction.getId());
		assertEquals(savedAction.getDescription(), foundAction.getDescription());
		foundAction.setDescription("edited action");
		capabilityManager.updateAction(foundAction);
		ActionVO updatedAction = capabilityManager.findActionNamed(actionName);
		assertNotNull(foundAction);
		assertEquals(foundAction.getId(), updatedAction.getId());
		assertEquals(foundAction.getDescription(), updatedAction.getDescription());
	}

	@Test(expected = NonExistentEntityException.class)
	public void testDeleteAction() throws Exception
	{
		System.out.println("deleteAction");
		ActionVO actionToDelete = createTestDataActionNamed("actionToDelete");
		capabilityManager.removeAction(actionToDelete);
		capabilityManager.findActionNamed(actionToDelete.getName());

	}

	@Test
	public void testAddResource() throws Exception
	{
		System.out.println("addResource");
		ResourceVO resourceToAdd = createTestDataResourceNamed("resourceToAdd");
		assertNotNull(resourceToAdd);
		assertNotNull(resourceToAdd.getId());
	}

	@Test
	public void testEditResource() throws Exception
	{
		System.out.println("editResource");
		ResourceVO resourceToEdit = createTestDataResourceNamed("resourceToEdit");
		final String editedDescription = "editedResource";
		resourceToEdit.setDescription(editedDescription);
		capabilityManager.updateResource(resourceToEdit);
		ResourceVO updatedResource = capabilityManager.findResourceNamedAs(resourceToEdit.getName());
		assertEquals(resourceToEdit, updatedResource);
		assertEquals(resourceToEdit.getDescription(), updatedResource.getDescription());
	}

	@Test(expected = NonExistentEntityException.class)
	public void testRemoveResource() throws Exception
	{
		System.out.println("removeResource");
		ResourceVO resourceToRemove = createTestDataResourceNamed("resourceToRemove");
		capabilityManager.removeResource(resourceToRemove);
		capabilityManager.findResourceNamedAs(resourceToRemove.getName());
	}

	@Test
	public void testAddCapability() throws Exception
	{
		System.out.println("addCapability");
		final String actionName = "add";
		final String resourceName = "employee";
		CapabilityVO savedCapabilityVO = createTestDataCapabilityWithActionAndResourceName(actionName, resourceName);
		assertNotNull(savedCapabilityVO);
		assertNotNull(savedCapabilityVO.getId());
	}
	private static int KEY = 0;

	@Test
	public void testUpdateCapabilityDetails() throws Exception
	{
		System.out.println("updateCapabilityDetails");
		final String actionName = "add" + (KEY++);
		final String resourceName = "employee" + (KEY++);
		CapabilityVO capabilityVOtoUpdate = createTestDataCapabilityWithActionAndResourceName(actionName, resourceName);
		capabilityVOtoUpdate.setDescription("updated description");
		capabilityVOtoUpdate.setTitle("updated title");
		capabilityManager.updateCapability(capabilityVOtoUpdate);
		CapabilityVO updatedCapabilityVO = capabilityManager.findCapabilityWithId(capabilityVOtoUpdate.getId());
		assertEquals(capabilityVOtoUpdate.getId(), updatedCapabilityVO.getId());
		assertEquals(capabilityVOtoUpdate.getDescription(), updatedCapabilityVO.getDescription());
		assertEquals(capabilityVOtoUpdate.getTitle(), updatedCapabilityVO.getTitle());
		assertEquals(capabilityVOtoUpdate.getActions().size(), updatedCapabilityVO.getActions().size());
	}

	@Test
	public void testUpdateCapabilityAndAddActions() throws Exception
	{
		System.out.println("updateCapabilityAndAddActions");
		final String actionName = "add" + (KEY++);
		final String resourceName = "employee" + (KEY++);
		CapabilityVO capabilityVOtoUpdate = createTestDataCapabilityWithActionAndResourceName(actionName, resourceName);
		CapabilityActionVO capabilityActionToAdd = createTestDataCapabilityActionNamed("edit" + (KEY++), capabilityVOtoUpdate);
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
		CapabilityVO capabilityVOtoUpdate = createTestDataCapabilityWithActionAndResourceName(actionName, resourceName);
		CapabilityActionVO capabilityActionToAdd = createTestDataCapabilityActionNamed("edit" + (KEY++), capabilityVOtoUpdate);
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
		CapabilityVO capabilityVOtoUpdate = createTestDataCapabilityWithActionAndResourceName(actionName, resourceName);
		CapabilityActionVO capabilityActionToAdd = createTestDataCapabilityActionNamed("edit" + (KEY++), capabilityVOtoUpdate);
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
		capabilityActionToAdd = createTestDataCapabilityActionNamed("delete" + (KEY++), capabilityVOtoUpdate);
		updatedCapabilityVO.getActions().add(capabilityActionToAdd);
		capabilityManager.updateCapability(updatedCapabilityVO);
		CapabilityVO updatedCapabilityRemovedActionsVO = capabilityManager.findCapabilityWithId(capabilityVOtoUpdate.getId());
		assertEquals(2, updatedCapabilityRemovedActionsVO.getActions().size());
		assertNotNull(updatedCapabilityRemovedActionsVO.findActionNamed(capabilityActionToAdd.getActionVO().getName()));
	}

	@Test
	public void testDeleteCapability() throws Exception
	{
		System.out.println("deleteCapability");
		final String actionName = "delete" + (KEY++);
		final String resourceName = "employee" + (KEY++);
		CapabilityVO capabilityVOtoDelete = createTestDataCapabilityWithActionAndResourceName(actionName, resourceName);
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
		CapabilityVO savedCapabilityVO = createTestDataCapabilityWithActionAndResourceName(actionName, resourceName);

		CapabilityVO foundCapabilityVO = capabilityManager.findCapabilityWithTitle(savedCapabilityVO.getTitle());
		assertNotNull(foundCapabilityVO);
		assertEquals(savedCapabilityVO.getTitle(), foundCapabilityVO.getTitle());
	}

	@Test
	public void testAddActionsToCapability() throws Exception
	{
		System.out.println("addActionsToCapability");
		ActionVO actionToAdd = createTestDataActionNamed("missingInAction");
		CapabilityVO capabilityToUpdate = createTestDataCapabilityWithActionAndResourceName("currentAction", "newResource");
		List<ActionVO> actionsToAdd = new ArrayList<ActionVO>();
		actionsToAdd.add(actionToAdd);
		capabilityManager.addActionsToCapability(actionsToAdd, capabilityToUpdate);
		CapabilityVO updatedCapability = capabilityManager.findCapabilityWithId(capabilityToUpdate.getId());
		updatedCapability.findActionNamed("missingInAction");

	}

	//--- no more test beyond this point.
	private ActionVO createTestDataActionNamed(String name) throws
			CapabilityManagerException
	{
		ActionVO action = new ActionVO();
		action.setDescription(name + " description");
		action.setName(name);
		ActionVO savedAction = capabilityManager.createNewAction(action);
		return savedAction;
	}

	private ResourceVO createTestDataResourceNamed(String name) throws
			CapabilityManagerException
	{
		ResourceVO resourceToAdd = new ResourceVOBuilder()
				.setName(name)
				.setDescription(name + "description")
				.createResourceVO();

		ResourceVO resourceAdded = capabilityManager.addResource(resourceToAdd);
		return resourceAdded;
	}

	private CapabilityVO createTestDataCapabilityWithActionAndResourceName(
			final String actionName,
			final String resourceName) throws CapabilityManagerException
	{
		ActionVO action = createTestDataActionNamed(actionName);
		ResourceVO resource = createTestDataResourceNamed(resourceName);
		final String capabilityTitle = "capability to " + actionName + " " + resourceName;
		CapabilityVO capabilityVO = new CapabilityVOBuilder()
				.addAction(action)
				.setResource(resource)
				.setTitle(capabilityTitle)
				.setDescription(capabilityTitle + " description")
				.createCapabilityVO();
		CapabilityVO savedCapabilityVO = capabilityManager.createNewCapability(capabilityVO);
		return savedCapabilityVO;
	}

	private CapabilityActionVO createTestDataCapabilityActionNamed(
			final String actionName, CapabilityVO capabilityVOtoUpdate) throws
			CapabilityManagerException
	{
		ActionVO actionVOtoAdd = createTestDataActionNamed(actionName);
		CapabilityActionVO capabilityActionToAdd = new CapabilityActionVOBuilder()
				.setActionVO(actionVOtoAdd)
				.setCapabilityVO(capabilityVOtoUpdate)
				.createCapabilityActionVO();
		return capabilityActionToAdd;
	}
}
