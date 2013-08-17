/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.services;

import com.rdonasco.security.vo.AccessRightsVO;
import javax.ejb.Remote;

/**
 *
 * @author Roy F. Donasco
 */
@Remote
public interface SystemSecurityManagerRemote extends SystemSecurityManager
{

		void checkAccessRightsOnSecuritySystem(AccessRightsVO accessRights);

}
