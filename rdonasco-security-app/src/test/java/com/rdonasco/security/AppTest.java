package com.rdonasco.security;

import com.rdonasco.security.dao.ActionDAO;
import com.rdonasco.security.model.Action;
import com.rdonasco.security.services.CapabilityManagerRemote;
import com.rdonasco.security.services.SystemSecurityManagerRemote;
import com.rdonasco.security.vo.ActionVO;
import javax.ejb.EJB;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import test.util.ArchiveCreator;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
/**
 * Unit test for simple App.
 */
@RunWith(Arquillian.class)
public class AppTest 
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
}
