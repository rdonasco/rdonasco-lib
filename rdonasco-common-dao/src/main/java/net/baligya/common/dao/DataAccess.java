/*
 * Copyright 2011 Roy F. Donasco.
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
package net.baligya.common.dao;

import java.util.List;
import java.util.Map;
import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.common.exceptions.IllegalOrphanException;
import com.rdonasco.common.exceptions.NonexistentEntityException;
import com.rdonasco.common.exceptions.PreexistingEntityException;

/**
 *
 * @author Roy F. Donasco
 */
public interface DataAccess<T>
{

    void create(T data) throws PreexistingEntityException, Exception;

    void delete(Class<T> objectClass, Long id) throws IllegalOrphanException,
            NonexistentEntityException;

    void update(T data) throws IllegalOrphanException,
            NonexistentEntityException, Exception;

    T findData(Class<T> objectClass, Long id);
    
    T findFreshData(Class<T> objectClass, Long id);

    List<T> findAllData();

    List<T> findAllData(int maxResults, int firstResult);

    Class<T> getDataClass();

    List<T> findAllDataUsingNamedQuery(String namedQuery, Map<String, Object> parameters)
            throws DataAccessException;
    T findUniqueDataUsingNamedQuery(String namedQuery, Map<String, Object> parameters)
            throws DataAccessException;
}
