package com.rdonasco.security;

import com.rdonasco.common.exceptions.NonExistentEntityException;
import com.rdonasco.security.dao.ActionDAO;
import com.rdonasco.security.exceptions.CapabilityManagerException;
import com.rdonasco.security.model.Action;
import com.rdonasco.security.services.CapabilityManagerRemote;
import com.rdonasco.security.services.SystemSecurityManagerRemote;
import com.rdonasco.security.vo.ActionVO;
import com.rdonasco.security.vo.ResourceVO;
import com.rdonasco.security.vo.ResourceVOBuilder;
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
public class CapabilityManagerJPATest 
{
	@EJB 
	private CapabilityManagerRemote capabilityManager; 
	
    @Deployment
    public static JavaArchive createTestArchive()
    {
		JavaArchive  archive = ArchiveCreator.createCommonArchive()
				.addPackage(ActionDAO.class.getPackage())
				.addPackage(SystemSecurityManagerRemote.class.getPackage())
				.addPackage(ActionVO.class.getPackage())
				.addPackage(Action.class.getPackage())
				;
		
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
		assertEquals(savedAction.getId(),foundAction.getId());
		assertEquals(savedAction.getDescription(),foundAction.getDescription());
		foundAction.setDescription("edited action");
		capabilityManager.updateAction(foundAction); 
		ActionVO updatedAction = capabilityManager.findActionNamed(actionName);
		assertNotNull(foundAction);
		assertEquals(foundAction.getId(),updatedAction.getId());
		assertEquals(foundAction.getDescription(),updatedAction.getDescription());		
    }	
	
	@Test(expected=NonExistentEntityException.class)
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
		assertEquals(resourceToEdit,updatedResource);
		assertEquals(resourceToEdit.getDescription(),updatedResource.getDescription());
	}
	
	@Test(expected=NonExistentEntityException.class)
	public void testRemoveResource() throws Exception
	{
		System.out.println("removeResource");
		ResourceVO resourceToRemove = createTestDataResourceNamed("resourceToRemove");
		capabilityManager.removeResource(resourceToRemove);
		capabilityManager.findResourceNamedAs(resourceToRemove.getName());		
	}

	private ActionVO createTestDataActionNamed(String name) throws CapabilityManagerException
	{
		ActionVO action = new ActionVO();
		action.setDescription(name + " description");
		action.setName(name);
		ActionVO savedAction = capabilityManager.createNewAction(action);
		return savedAction;
	}

	private ResourceVO createTestDataResourceNamed(String name) throws CapabilityManagerException
	{
		ResourceVO resourceToAdd = new ResourceVOBuilder()
				.setName(name)
				.setDescription(name + "description")
				.createResourceVO();
		
		ResourceVO resourceAdded = capabilityManager.addResource(resourceToAdd);
		return resourceAdded;
	}
}
