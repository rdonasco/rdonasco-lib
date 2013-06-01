/*
 * Copyright 2013 Roy F. Donasco.
 * File Created on: 18-May-2013
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

import com.rdonasco.security.role.controllers.*;
import com.rdonasco.common.exceptions.WidgetException;
import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.controller.ApplicationExceptionPopupProvider;
import com.rdonasco.common.vaadin.controller.ApplicationPopupProvider;
import com.rdonasco.common.vaadin.controller.ViewController;
import com.rdonasco.datamanager.controller.DataManagerContainer;
import com.rdonasco.security.app.themes.SecurityDefaultTheme;
import com.rdonasco.security.authentication.services.SessionSecurityChecker;
import com.rdonasco.security.capability.utils.IconHelper;
import com.rdonasco.security.capability.vo.CapabilityItemVO;
import com.rdonasco.security.common.utils.ActionConstants;
import com.rdonasco.security.common.views.ListItemIconCellStyleGenerator;
import com.rdonasco.security.group.utils.GroupConstants;
import com.rdonasco.security.group.views.GroupEditorView;
import com.rdonasco.security.group.vo.GroupItemVO;
import com.rdonasco.security.group.vo.GroupRoleItemVO;
import com.rdonasco.security.group.vo.GroupRoleItemVOBuilder;
import com.rdonasco.security.role.vo.RoleItemVO;
import com.rdonasco.security.vo.RoleVO;
import com.rdonasco.security.vo.SecurityGroupRoleVO;
import com.rdonasco.security.vo.SecurityGroupRoleVOBuilder;
import com.vaadin.data.Buffered;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.DataBoundTransferable;
import com.vaadin.event.MouseEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 *
 * @author Roy F. Donasco
 */
public class GroupEditorViewController implements
		ViewController<GroupEditorView>
{

	private static final Logger LOG = Logger.getLogger(RoleEditorViewController.class.getName());

	private static final long serialVersionUID = 1L;

	@Inject
	private ApplicationExceptionPopupProvider exceptionPopupProvider;

	@Inject
	private ApplicationPopupProvider popupProvider;

	@Inject
	private GroupEditorView groupEditorView;

	@Inject
	private SessionSecurityChecker sessionSecurityChecker;

	private BeanItem<GroupItemVO> currentItem;

	private DataManagerContainer<GroupItemVO> groupItemTableDataManagerContainer;

	private BeanItemContainer<GroupRoleItemVO> groupRolesContainer = new BeanItemContainer<GroupRoleItemVO>(GroupRoleItemVO.class);

	private DropHandler groupRolesDropHandler;

	private Button.ClickListener cancelClickListener = new Button.ClickListener()
	{
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(Button.ClickEvent event)
		{
			discardChanges();
		}
	};

	private Button.ClickListener editClickListener = new Button.ClickListener()
	{
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(Button.ClickEvent event)
		{
			changeViewToEditMode();
		}
	};

	private Button.ClickListener saveClickListener = new Button.ClickListener()
	{
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(Button.ClickEvent event)
		{
			LOG.log(Level.FINE, "saveButton clicked");
			saveChanges();

		}
	};

	private int[] keyModifiers = new int[]
	{
		ShortcutAction.ModifierKey.CTRL
	};

	private ShortcutListener controlSListener = new ShortcutListener(null,
			ShortcutAction.KeyCode.S, keyModifiers)
	{
		@Override
		public void handleAction(Object sender, Object target)
		{
			saveChanges();
		}
	};

	private ShortcutListener controlEListener = new ShortcutListener(null,
			ShortcutAction.KeyCode.E, keyModifiers)
	{
		@Override
		public void handleAction(Object sender, Object target)
		{
			changeViewToEditMode();
		}
	};

	private ShortcutListener escListener = new ShortcutListener(null,
			ShortcutAction.KeyCode.ESCAPE, null)
	{
		@Override
		public void handleAction(Object sender, Object target)
		{
			discardChanges();
		}
	};

	private static final String ROLE_NAME = "roleName";

	private static final String[] EDITABLE_COLUMNS = new String[]
	{
		"icon", ROLE_NAME
	};

	private static final String[] NON_EDITABLE_COLUMNS = new String[]
	{
		ROLE_NAME
	};

	private final String[] EDITABLE_HEADERS = new String[]
	{
		"", I18NResource.localize("Name")
	};

	private final String[] NON_EDITABLE_HEADERS = new String[]
	{
		I18NResource.localize("Name")
	};

	private Table.CellStyleGenerator CELL_STYLE_GENERATOR = new ListItemIconCellStyleGenerator();

	@PostConstruct
	@Override
	public void initializeControlledViewBehavior()
	{
		try
		{
			groupEditorView.initWidget();
			configureRoleCapabilityTable();
			configureButtonListenters();
		}
		catch (WidgetInitalizeException ex)
		{
			exceptionPopupProvider.popUpErrorException(ex);
		}
	}

	public void setGroupItemTableContainer(
			DataManagerContainer<GroupItemVO> roleItemTableContainer)
	{
		this.groupItemTableDataManagerContainer = roleItemTableContainer;
	}

	private void saveChanges()
	{
		try
		{
			getControlledView().getGroupRolesTable().commit();
			getControlledView().getForm().commit();
			LOG.log(Level.FINE, "groupRolesContainer == null : {0}", groupItemTableDataManagerContainer == null);
			LOG.log(Level.FINE, "currentItem == null : {0}", currentItem == null);
			Collection<SecurityGroupRoleVO> editedRoleCapabilities = new ArrayList<SecurityGroupRoleVO>();
			for (GroupRoleItemVO roleCapability : groupRolesContainer.getItemIds())
			{
				editedRoleCapabilities.add(roleCapability.getSecurityGroupRoleVO());
			}
			BeanItem<GroupItemVO> roleBean = getCurrentItem();
			roleBean.getItemProperty("groupRoles").setValue(editedRoleCapabilities);
			groupItemTableDataManagerContainer.updateItem(roleBean.getBean());
			popupProvider.popUpInfo(I18NResource.localizeWithParameter("Group _ saved", getCurrentItem().getBean().getName()));
			changeViewToViewMode();
		}
		catch (Exception ex)
		{
			exceptionPopupProvider.popUpErrorException(ex);
		}
	}

	private void discardChanges() throws Buffered.SourceException
	{
		getControlledView().getForm().discard();
		getControlledView().getGroupRolesTable().discard();
		setCurrentItem(currentItem);
		changeViewToViewMode();
	}

	public BeanItem<GroupItemVO> getCurrentItem()
	{
		return currentItem;
	}

	public void setCurrentItem(
			BeanItem<GroupItemVO> currentItem)
	{
		this.currentItem = currentItem;
		groupRolesContainer.removeAllItems();
		getControlledView().getGroupRolesTable().setSelectable(true);
		for (SecurityGroupRoleVO groupRoleVO : currentItem.getBean().getGroupRoles())
		{
			groupRolesContainer.addItem(createGroupRoleItemVO(groupRoleVO.getRoleVO()));
		}
		changeViewToViewMode();
	}

	@Override
	public GroupEditorView getControlledView()
	{
		return groupEditorView;
	}

	@Override
	public void refreshView() throws WidgetException
	{
		setItemDataSource(currentItem);
	}

	void setItemDataSource(
			BeanItem<GroupItemVO> beamItem)
	{
		getControlledView().getForm().setItemDataSource(beamItem);
		setCurrentItem(beamItem);
	}

	void changeViewToViewMode()
	{
		getControlledView().getForm().setReadOnly(true);
		getControlledView().getSaveButton().setVisible(false);
		getControlledView().getCancelButton().setVisible(false);
		getControlledView().getEditButton().setVisible(true);
		getControlledView().getGroupRolesTable().setCellStyleGenerator(CELL_STYLE_GENERATOR);
		getControlledView().getGroupRolesTable().setVisibleColumns(NON_EDITABLE_COLUMNS);
		getControlledView().getGroupRolesTable().setColumnHeaders(NON_EDITABLE_HEADERS);
		getControlledView().getGroupRolesTable().setDropHandler(null);

	}

	private void changeViewToEditMode()
	{
		try
		{
			sessionSecurityChecker.checkCapabilityTo(ActionConstants.EDIT, GroupConstants.RESOURCE_GROUPS);
			getControlledView().getForm().setReadOnly(false);
			getControlledView().getSaveButton().setVisible(true);
			getControlledView().getCancelButton().setVisible(true);
			getControlledView().getEditButton().setVisible(false);
			getControlledView().getGroupRolesTable().setCellStyleGenerator(CELL_STYLE_GENERATOR);
			getControlledView().getGroupRolesTable().setVisibleColumns(EDITABLE_COLUMNS);
			getControlledView().getGroupRolesTable().setColumnHeaders(EDITABLE_HEADERS);
			getControlledView().getGroupRolesTable().setDropHandler(groupRolesDropHandler);
		}
		catch (Exception e)
		{
			exceptionPopupProvider.popUpErrorException(e);
		}
	}

	private void configureButtonListenters()
	{
		getControlledView().getEditButton().addListener(editClickListener);
		getControlledView().getCancelButton().addListener(cancelClickListener);
		getControlledView().getSaveButton().addListener(saveClickListener);
		getControlledView().getEditButton().addShortcutListener(controlEListener);
		getControlledView().getEditButton().setDescription("Edit (ctrl+E)");
		getControlledView().getSaveButton().addShortcutListener(controlSListener);
		getControlledView().getSaveButton().setDescription("Save (ctrl+S)");
		getControlledView().getCancelButton().addShortcutListener(escListener);
		getControlledView().getCancelButton().setDescription("Cancel (Esc)");
	}

	private void configureRoleCapabilityTable()
	{
		Table roleCapabilitiesTable = getControlledView().getGroupRolesTable();
		roleCapabilitiesTable.setSelectable(true);
		roleCapabilitiesTable.setContainerDataSource(groupRolesContainer);
		roleCapabilitiesTable.setVisibleColumns(EDITABLE_COLUMNS);
		roleCapabilitiesTable.setColumnHeaders(EDITABLE_HEADERS);
		roleCapabilitiesTable.setCellStyleGenerator(CELL_STYLE_GENERATOR);
		groupRolesDropHandler = new DropHandler()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void drop(DragAndDropEvent dropEvent)
			{
				try
				{
					final DataBoundTransferable transferredData = (DataBoundTransferable) dropEvent.getTransferable();
					if (null != transferredData && transferredData.getItemId() instanceof RoleItemVO)
					{
						sessionSecurityChecker.checkCapabilityTo(ActionConstants.ADD, GroupConstants.RESOURCE_GROUP_ROLES);
						LOG.log(Level.FINE, "drop allowed at group role panel");
						final RoleItemVO roleItemVO = (RoleItemVO) transferredData.getItemId();

						final GroupRoleItemVO newGroupRoleItemVO = createGroupRoleItemVO(roleItemVO.getRoleVO());
						BeanItem<GroupRoleItemVO> addedItem = groupRolesContainer.addItem(newGroupRoleItemVO);
						LOG.log(Level.FINE, "addedItem = {0}", addedItem);

					}
					else
					{
						LOG.log(Level.FINE, "invalid data dropped in group role panel");
					}
				}
				catch (Exception e)
				{
					exceptionPopupProvider.popUpErrorException(e);
				}
			}

			@Override
			public AcceptCriterion getAcceptCriterion()
			{
				return AcceptAll.get();
			}
		};
		getControlledView().getGroupRolesTable().setDropHandler(groupRolesDropHandler);
	}

	private GroupRoleItemVO createGroupRoleItemVO(
			RoleVO roleVO)
	{
		Embedded icon = IconHelper.createDeleteIcon("Remove Role");
		final GroupRoleItemVO groupRoleItemVO = new GroupRoleItemVOBuilder()
				.setIcon(icon)
				.setSecurityGroupRoleVO(new SecurityGroupRoleVOBuilder()
				.setRole(roleVO)
				.setSecurityGroup(getCurrentItem().getBean().getSecurityGroupVO())
				.createSecurityGroupRoleVO())
				.createGroupRoleItemVO();
		icon.addListener(new MouseEvents.ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void click(MouseEvents.ClickEvent event)
			{
				try
				{
					sessionSecurityChecker.checkCapabilityTo(ActionConstants.DELETE, GroupConstants.RESOURCE_GROUP_ROLES);
					if (!getControlledView().isReadOnly() && !groupRolesContainer.removeItem(groupRoleItemVO))
					{
						popupProvider.popUpError(I18NResource
								.localizeWithParameter("Unable to remove role _",
								groupRoleItemVO.getSecurityGroupRoleVO().getRoleVO().getName()));

					}
				}
				catch (Exception e)
				{
					exceptionPopupProvider.popUpErrorException(e);
				}
			}
		});
		return groupRoleItemVO;
	}
}
