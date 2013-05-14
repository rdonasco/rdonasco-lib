/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 11-May-2013
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
package com.rdonasco.security.role.controllers;

import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.common.exceptions.WidgetException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.controller.ApplicationExceptionPopupProvider;
import com.rdonasco.common.vaadin.controller.ApplicationPopupProvider;
import com.rdonasco.common.vaadin.controller.ViewController;
import com.rdonasco.datamanager.controller.DataManagerContainer;
import com.rdonasco.datamanager.listeditor.controller.ListEditorAttachStrategy;
import com.rdonasco.datamanager.utils.TableHelper;
import com.rdonasco.security.common.controllers.ClickListenerProvider;
import com.rdonasco.security.common.controllers.DeleteClickListenerBuilder;
import com.rdonasco.security.i18n.MessageKeys;
import com.rdonasco.security.role.utils.RoleConstants;
import com.rdonasco.security.role.views.RoleListPanelView;
import com.rdonasco.security.role.vo.RoleItemVO;
import com.rdonasco.security.role.vo.RoleItemVOBuilder;
import com.rdonasco.security.vo.RoleVO;
import com.rdonasco.security.vo.RoleVOBuilder;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
public class RoleListPanelViewController implements
		ViewController<RoleListPanelView>
{

	private static final Logger logger = Logger.getLogger(RoleListPanelViewController.class.getName());

	private static final long serialVersionUID = 1L;

	@Inject
	private Instance<ApplicationExceptionPopupProvider> exceptionPopupProviderFactory;

	private ApplicationExceptionPopupProvider exceptionPopupProvider;

	@Inject
	private Instance<ApplicationPopupProvider> popupProviderFactory;

	private ApplicationPopupProvider popupProvider;

	@Inject
	private RoleListPanelView roleListPanelView;

	@Inject
	private RoleDataManagerDecorator roleDataManager;

	private DataManagerContainer<RoleItemVO> roleItemTableContainer = new DataManagerContainer(RoleItemVO.class);

	@PostConstruct
	@Override
	public void initializeControlledViewBehavior()
	{
		try
		{
			roleListPanelView.initWidget();
			final Table roleListTable = roleListPanelView.getRoleListTable();
			TableHelper.setupTable(roleListTable);
			roleItemTableContainer.setDataManager(roleDataManager);
			setupDeleteClickListener();
			roleListTable.setContainerDataSource(roleItemTableContainer);
			roleListTable.setVisibleColumns(RoleConstants.TABLE_VISIBLE_COLUMNS);
			roleListTable.setColumnHeaders(RoleConstants.TABLE_VISIBLE_HEADERS);
			roleListPanelView.getAddRoleButton().addListener(new Button.ClickListener()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(Button.ClickEvent event)
				{
					addNewRole();
				}
			});
			roleListPanelView.setAttachStrategy(new ListEditorAttachStrategy()
			{
				@Override
				public void attached(Component component)
				{
					refreshList();
					selectTheFirstRecord();
				}
			});


		}
		catch (Exception ex)
		{
			getExceptionPopupProvider().popUpDebugException(ex);
		}
	}

	public DataManagerContainer<RoleItemVO> getRoleItemTableContainer()
	{
		return roleItemTableContainer;
	}

	private void addNewRole()
	{
		RoleVO newRoleVO = new RoleVOBuilder()
				.setName(MessageKeys.NEW_ROLE)
				.createUserRoleVO();
		try
		{
			RoleItemVO newRoleItemVO = new RoleItemVOBuilder()
					.setRoleVO(newRoleVO)
					.createRoleItemVO();

			BeanItem<RoleItemVO> newItemAdded = roleItemTableContainer.addItem(newRoleItemVO);
			roleListPanelView.getRoleListTable().setCurrentPageFirstItemId(newItemAdded.getBean());
			roleListPanelView.getRoleListTable().select(newItemAdded.getBean());
		}
		catch (Exception e)
		{
			logger.log(Level.SEVERE, e.getMessage(), e);
			getPopupProvider().popUpError(I18NResource.localizeWithParameter(MessageKeys.UNABLE_TO_ADD_NEW_ROLE, newRoleVO.getName()));
		}
	}

	public ApplicationPopupProvider getPopupProvider()
	{
		if (null == popupProvider)
		{
			popupProvider = popupProviderFactory.get();
		}
		return popupProvider;
	}

	public ApplicationExceptionPopupProvider getExceptionPopupProvider()
	{
		if (null == exceptionPopupProvider)
		{
			exceptionPopupProvider = exceptionPopupProviderFactory.get();
		}
		return exceptionPopupProvider;
	}

	@Override
	public RoleListPanelView getControlledView()
	{
		return roleListPanelView;
	}

	@Override
	public void refreshView() throws WidgetException
	{
		try
		{
			roleItemTableContainer.refresh();
		}
		catch (DataAccessException ex)
		{
			getExceptionPopupProvider().popUpErrorException(ex);
		}
	}

	private void setupDeleteClickListener()
	{
		ClickListenerProvider<RoleItemVO> deleteClickBuilder = new DeleteClickListenerBuilder<RoleItemVO>()
				.setComponent(getControlledView())
				.setDeleteListener(new DeleteClickListenerBuilder.DeleteListener<RoleItemVO>()
		{
			@Override
			public void delete(RoleItemVO itemToDelete)
			{
				try
				{
					roleItemTableContainer.removeItem(itemToDelete);
					getPopupProvider().popUpInfo(I18NResource.localize(MessageKeys.ROLE_DELETED));
				}
				catch (Exception e)
				{
					getExceptionPopupProvider().popUpErrorException(e);
				}
			}
		})
				.createClickListenerProvider();
		roleDataManager.setClickListenerProvider(deleteClickBuilder);
	}

	private void refreshList()
	{
		try
		{
			refreshView();
		}
		catch (WidgetException ex)
		{
			exceptionPopupProvider.popUpErrorException(ex);
		}
	}

	private void selectTheFirstRecord()
	{
		final Object firstItemId = getControlledView().getRoleListTable().firstItemId();
		if (null != firstItemId)
		{
			getControlledView().getRoleListTable().select(firstItemId);
		}
	}
}
