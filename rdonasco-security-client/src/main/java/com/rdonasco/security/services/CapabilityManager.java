/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.services;

import com.rdonasco.common.exceptions.NonExistentEntityException;
import com.rdonasco.security.exceptions.CapabilityManagerException;
import com.rdonasco.security.exceptions.NotSecuredResourceException;
import com.rdonasco.security.vo.ActionVO;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.ResourceVO;

/**
 *
 * @author Roy F. Donasco
 */
public interface CapabilityManager
{

	ActionVO createNewAction(ActionVO action) throws CapabilityManagerException;	
	void updateAction(ActionVO actionToUpdate) throws CapabilityManagerException;
	void removeAction(ActionVO actionToRemove) throws CapabilityManagerException;
	ActionVO findActionNamed(String nameOfActionToFind) throws CapabilityManagerException, NonExistentEntityException;
	ResourceVO addResource(ResourceVO resource) throws CapabilityManagerException;
	void updateResource(ResourceVO resource) throws CapabilityManagerException;
	void removeResource(ResourceVO resource) throws CapabilityManagerException;
	ResourceVO findResourceNamedAs(String resourceName) throws CapabilityManagerException, NonExistentEntityException;
	ResourceVO findOrAddSecuredResourceNamedAs(String resourceName) throws CapabilityManagerException, NotSecuredResourceException;
	ActionVO findOrAddActionNamedAs(String name) throws CapabilityManagerException;
	CapabilityVO createNewCapability(CapabilityVO capabilityToCreate) throws CapabilityManagerException;
	void updateCapability(CapabilityVO capabilityToUpdate) throws CapabilityManagerException;
	void removeCapability(CapabilityVO capabilityToRemove) throws CapabilityManagerException;
}
