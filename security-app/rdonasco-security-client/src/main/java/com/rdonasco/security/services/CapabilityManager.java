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
import java.util.List;

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
	ResourceVO findOrAddResourceNamedAs(String resourceName) throws CapabilityManagerException;
	ActionVO findOrAddActionNamedAs(String name) throws CapabilityManagerException;
	CapabilityVO createNewCapability(CapabilityVO capabilityToCreate) throws CapabilityManagerException;
	void addActionsToCapability(List<ActionVO> actions,
			CapabilityVO capabilityVO) throws CapabilityManagerException;

	List<CapabilityVO> findAllCapabilities() throws CapabilityManagerException;
	CapabilityVO findCapabilityWithId(Long id) throws CapabilityManagerException;
	CapabilityVO findCapabilityWithTitle(String capabilityTitle) throws CapabilityManagerException, NonExistentEntityException;
	void updateCapability(CapabilityVO capabilityToUpdate) throws CapabilityManagerException;
	void removeCapability(CapabilityVO capabilityToRemove) throws CapabilityManagerException;
	List<CapabilityVO> findCapabilitiesWithResourceName(String resourceName)
			throws CapabilityManagerException;

	List<ResourceVO> findAllResources() throws CapabilityManagerException;

	List<ActionVO> findAllActions() throws CapabilityManagerException;
}
