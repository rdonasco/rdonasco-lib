/*
 * Copyright 2012 Roy F. Donasco.
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
package test.util;

import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.dao.BaseDAO;
import com.rdonasco.common.exceptions.DataAccessException;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

/**
 *
 * @author Roy F. Donasco
 */
public class ArchiveCreator
{
	public static JavaArchive createCommonArchive()
	{
		return ShrinkWrap.create(JavaArchive.class, "SecurityEJBTest.jar")
				// add resources
				.addAsManifestResource(new ByteArrayAsset("<beans/>".getBytes()), ArchivePaths.create("beans.xml"))
				.addAsManifestResource("persistence.xml", ArchivePaths.create("persistence.xml"))
				.addPackage(BaseDAO.class.getPackage())
				.addPackage(DataAccessException.class.getPackage())
				.addPackage(I18NResource.class.getPackage())
				;
	}
}
