/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.services;

import com.rdonasco.security.exceptions.CapabilityManagerException;
import com.rdonasco.security.vo.ActionVO;
import javax.ejb.Remote;

/**
 *
 * @author Roy F. Donasco
 */
@Remote
public interface CapabilityManagerRemote extends CapabilityManager
{	
}
