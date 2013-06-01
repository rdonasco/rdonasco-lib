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
package com.rdonasco.security.group.controllers;

import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.common.exceptions.WidgetException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.controller.ApplicationExceptionPopupProvider;
import com.rdonasco.common.vaadin.controller.ApplicationPopupProvider;
import com.rdonasco.common.vaadin.controller.ViewController;
import com.rdonasco.datamanager.controller.DataManagerContainer;
import com.rdonasco.datamanager.listeditor.controller.ListEditorAttachStrategy;
import com.rdonasco.datamanager.utils.TableHelper;
import com.rdonasco.security.authentication.services.SessionSecurityChecker;
import com.rdonasco.security.common.controllers.ClickListenerProvider;
import com.rdonasco.security.common.controllers.DeleteClickListenerBuilder;
import com.rdonasco.security.common.utils.ActionConstants;
import com.rdonasco.security.group.utils.GroupConstants;
import com.rdonasco.security.group.views.GroupListPanelView;
import com.rdonasco.security.group.vo.GroupItemVO;
import com.rdonasco.security.group.vo.GroupItemVOBuilder;
import com.rdonasco.security.i18n.MessageKeys;
import com.rdonasco.security.vo.SecurityGroupVO;
import com.rdonasco.security.vo.SecurityGroupVOBuilder;
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
public class GroupListPanelViewController implements
		ViewController<GroupListPanelView>
{

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLogger(GroupListPanelViewController.class.getName());

	@Inject
	private Instance<ApplicationExceptionPopupProvider> exceptionPopupProviderFactory;

	private ApplicationExceptionPopupProvider exceptionPopupProvider;

	@Inject
	private Instance<ApplicationPopupProvider> popupProviderFactory;

	private ApplicationPopupProvider popupProvider;

	@Inject
	private GroupListPanelView groupListPanelView;

	@Inject
	private GroupDataManagerDecorator dataManager;

	@Inject
	private SessionSecurityChecker sessionSecurityChecker;

	private DataManagerContainer<GroupItemVO> groupItemDataContainer = new DataManagerContainer(GroupItemVO.class);

	@PostConstruct
	@Override
	public void initializeControlledViewBehavior()
	{
		try
		{
			groupListPanelView.initWidget();
			final Table groupListTable = groupListPanelView.getGroupListTable();
			TableHelper.setupTable(groupListTable);
			groupItemDataContainer.setDataManager(dataManager);
			setupDeleteClickListener();
			groupListTable.setContainerDataSource(groupItemDataContainer);
			groupListTable.setVisibleColumns(GroupConstants.TABLE_VISIBLE_COLUMNS);
			groupListTable.setColumnHeaders(GroupConstants.TABLE_VISIBLE_HEADERS);
			groupListPanelView.getAddGroupButton().addListener(new Button.ClickListener()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(Button.ClickEvent event)
				{
					addNewGroup();
				}
			});
			groupListPanelView.getRefreshButton().addListener(new Button.ClickListener()
			{
				@Override
				public void buttonClick(Button.ClickEvent event)
				{
					LOG.log(Level.FINE, "refresh button clicked");
					refreshList();
				}
			});
			groupListPanelView.setAttachStrategy(new ListEditorAttachStrategy()
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

	public DataManagerContainer<GroupItemVO> getGroupItemTableContainer()
	{
		return groupItemDataContainer;
	}

	private void addNewGroup()
	{
		try
		{
			sessionSecurityChecker.checkCapabilityTo(ActionConstants.ADD, GroupConstants.RESOURCE_GROUPS);
			SecurityGroupVO newGroupVO = new SecurityGroupVOBuilder()
					.setName(MessageKeys.NEW_GROUP)
					.createSecurityGroupVO();
			try
			{
				GroupItemVO newGroupItemVO = new GroupItemVOBuilder()
						.setRoleVO(newGroupVO)
						.createGroupItemVO();

				BeanItem<GroupItemVO> newItemAdded = groupItemDataContainer.addItem(newGroupItemVO);
				groupListPanelView.getGroupListTable().setCurrentPageFirstItemId(newItemAdded.getBean());
				groupListPanelView.getGroupListTable().select(newItemAdded.getBean());
			}
			catch (Exception e)
			{
				LOG.log(Level.SEVERE, e.getMessage(), e);
				getPopupProvider().popUpError(I18NResource.localizeWithParameter(MessageKeys.UNABLE_TO_ADD_NEW_GROUP, newGroupVO.getName()));
			}
		}
		catch (Exception e)
		{
			getExceptionPopupProvider().popUpErrorException(e);
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
	public GroupListPanelView getControlledView()
	{
		return groupListPanelView;
	}

	@Override
	public void refreshView() throws WidgetException
	{
		try
		{
			groupItemDataContainer.refresh();
		}
		catch (DataAccessException ex)
		{
			getExceptionPopupProvider().popUpErrorException(ex);
		}
	}

	private void setupDeleteClickListener()
	{
		ClickListenerProvider<GroupItemVO> deleteClickListenerProvider = new DeleteClickListenerBuilder<GroupItemVO>()
				.setComponent(getControlledView())
				.setDeletePromptMessage(I18NResource.localize("deleting group will also remove its assignment to users"))
				.setDeleteListener(new DeleteClickListenerBuilder.DeleteListener<GroupItemVO>()
		{
			@Override
			public void delete(GroupItemVO itemToDelete)
			{
				try
				{
					sessionSecurityChecker.checkCapabilityTo(ActionConstants.DELETE, GroupConstants.RESOURCE_GROUPS);
					LOG.log(Level.FINE, "delete clicked");
					GroupItemVO nextItem = groupItemDataContainer.nextItemId(itemToDelete);
					groupItemDataContainer.removeItem(itemToDelete);
					if (nextItem != null)
					{
						getControlledView().getGroupListTable().select(nextItem);
					}

					getPopupProvider().popUpInfo(I18NResource.localize(MessageKeys.GROUP_DELETED));
				}
				catch (Exception e)
				{
					getExceptionPopupProvider().popUpErrorException(e);
				}
			}
		}).createClickListenerProvider();
		dataManager.setClickListenerProvider(deleteClickListenerProvider);
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
		final Object firstItemId = getControlledView().getGroupListTable().firstItemId();
		if (null != firstItemId)
		{
			getControlledView().getGroupListTable().select(firstItemId);
		}
	}
}
