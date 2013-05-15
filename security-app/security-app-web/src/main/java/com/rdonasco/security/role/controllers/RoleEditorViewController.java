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

import com.rdonasco.common.exceptions.WidgetException;
import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.controller.ApplicationExceptionPopupProvider;
import com.rdonasco.common.vaadin.controller.ApplicationPopupProvider;
import com.rdonasco.common.vaadin.controller.ViewController;
import com.rdonasco.datamanager.controller.DataManagerContainer;
import com.rdonasco.security.app.themes.SecurityDefaultTheme;
import com.rdonasco.security.capability.utils.IconHelper;
import com.rdonasco.security.capability.vo.CapabilityItemVO;
import com.rdonasco.security.role.views.RoleEditorView;
import com.rdonasco.security.role.vo.RoleCapabilityItemVO;
import com.rdonasco.security.role.vo.RoleCapabilityItemVOBuilder;
import com.rdonasco.security.role.vo.RoleItemVO;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.RoleCapabilityVO;
import com.rdonasco.security.vo.RoleCapabilityVOBuilder;
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
public class RoleEditorViewController implements ViewController<RoleEditorView>
{

	private static final Logger LOG = Logger.getLogger(RoleEditorViewController.class.getName());

	private static final long serialVersionUID = 1L;

	@Inject
	private ApplicationExceptionPopupProvider exceptionPopupProvider;

	@Inject
	private ApplicationPopupProvider popupProvider;

	@Inject
	private RoleEditorView roleEditorView;

	private BeanItem<RoleItemVO> currentItem;

	private DataManagerContainer<RoleItemVO> roleItemTableDataManagerContainer;

	private BeanItemContainer<RoleCapabilityItemVO> roleCapabilitiesContainer = new BeanItemContainer<RoleCapabilityItemVO>(RoleCapabilityItemVO.class);

	private DropHandler roleCapabilitiesDropHandler;

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

	private static final String CAPABILITY_TITLE = "capability.title";

	private static final String[] EDITABLE_COLUMNS = new String[]
	{
		"icon", CAPABILITY_TITLE
	};

	private static final String[] NON_EDITABLE_COLUMNS = new String[]
	{
		CAPABILITY_TITLE
	};

	private final String[] EDITABLE_HEADERS = new String[]
	{
		"", I18NResource.localize("Title")
	};

	private final String[] NON_EDITABLE_HEADERS = new String[]
	{
		I18NResource.localize("Title")
	};

	private Table.CellStyleGenerator CELL_STYLE_GENERATOR = new Table.CellStyleGenerator()
	{
		private static final long serialVersionUID = 1L;

		@Override
		public String getStyle(Object itemId, Object propertyId)
		{
			String style;
			if ("icon".equals(propertyId))
			{
				style = SecurityDefaultTheme.CSS_ICON_IN_A_CELL;
			}
			else
			{
				style = SecurityDefaultTheme.CSS_FULL_WIDTH;
			}
			return style;
		}
	};

	@PostConstruct
	@Override
	public void initializeControlledViewBehavior()
	{
		try
		{
			roleEditorView.initWidget();
			configureRoleCapabilityTable();
			configureButtonListenters();
		}
		catch (WidgetInitalizeException ex)
		{
			exceptionPopupProvider.popUpErrorException(ex);
		}
	}

	public void setRoleItemTableContainer(
			DataManagerContainer<RoleItemVO> roleItemTableContainer)
	{
		this.roleItemTableDataManagerContainer = roleItemTableContainer;
	}

	private void saveChanges()
	{
		try
		{
			getControlledView().getRoleCapabilitiesTable().commit();
			getControlledView().getForm().commit();
			LOG.log(Level.FINE, "roleItemTableContainer == null : {0}", roleItemTableDataManagerContainer == null);
			LOG.log(Level.FINE, "currentItem == null : {0}", currentItem == null);
			Collection<RoleCapabilityVO> editedRoleCapabilities = new ArrayList<RoleCapabilityVO>();
			for (RoleCapabilityItemVO roleCapability : roleCapabilitiesContainer.getItemIds())
			{
				editedRoleCapabilities.add(roleCapability.getRoleCapabilityVO());
			}
			BeanItem<RoleItemVO> roleBean = getCurrentItem();
			roleBean.getItemProperty("roleCapabilities").setValue(editedRoleCapabilities);
			roleItemTableDataManagerContainer.updateItem(roleBean.getBean());
			popupProvider.popUpInfo(I18NResource.localizeWithParameter("Role _ saved", getCurrentItem().getBean().getName()));
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
		getControlledView().getRoleCapabilitiesTable().discard();
		setCurrentItem(currentItem);
		changeViewToViewMode();
	}

	public BeanItem<RoleItemVO> getCurrentItem()
	{
		return currentItem;
	}

	public void setCurrentItem(
			BeanItem<RoleItemVO> currentItem)
	{
		this.currentItem = currentItem;
		roleCapabilitiesContainer.removeAllItems();
		getControlledView().getRoleCapabilitiesTable().setSelectable(true);
		for (RoleCapabilityVO roleCapability : currentItem.getBean().getRoleCapabilities())
		{
			roleCapabilitiesContainer.addItem(createRoleCapabilityItemVO(
					roleCapability.getCapabilityVO()));
		}
		changeViewToViewMode();
	}

	@Override
	public RoleEditorView getControlledView()
	{
		return roleEditorView;
	}

	@Override
	public void refreshView() throws WidgetException
	{
		setItemDataSource(currentItem);
	}

	void setItemDataSource(
			BeanItem<RoleItemVO> beamItem)
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
		getControlledView().getRoleCapabilitiesTable().setVisibleColumns(NON_EDITABLE_COLUMNS);
		getControlledView().getRoleCapabilitiesTable().setColumnHeaders(NON_EDITABLE_HEADERS);
		getControlledView().getRoleCapabilitiesTable().setCellStyleGenerator(CELL_STYLE_GENERATOR);
		getControlledView().getRoleCapabilitiesTable().setDropHandler(null);

	}

	private void changeViewToEditMode()
	{
		getControlledView().getForm().setReadOnly(false);
		getControlledView().getSaveButton().setVisible(true);
		getControlledView().getCancelButton().setVisible(true);
		getControlledView().getEditButton().setVisible(false);
		getControlledView().getRoleCapabilitiesTable().setVisibleColumns(EDITABLE_COLUMNS);
		getControlledView().getRoleCapabilitiesTable().setColumnHeaders(EDITABLE_HEADERS);
		getControlledView().getRoleCapabilitiesTable().setCellStyleGenerator(CELL_STYLE_GENERATOR);
		getControlledView().getRoleCapabilitiesTable().setDropHandler(roleCapabilitiesDropHandler);
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
		Table roleCapabilitiesTable = getControlledView().getRoleCapabilitiesTable();
		roleCapabilitiesTable.setSelectable(true);
		roleCapabilitiesTable.setContainerDataSource(roleCapabilitiesContainer);
		roleCapabilitiesContainer.addNestedContainerProperty(CAPABILITY_TITLE);
		roleCapabilitiesTable.setVisibleColumns(EDITABLE_COLUMNS);
		roleCapabilitiesTable.setColumnHeaders(EDITABLE_HEADERS);
		roleCapabilitiesTable.setCellStyleGenerator(CELL_STYLE_GENERATOR);
		roleCapabilitiesDropHandler = new DropHandler()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void drop(DragAndDropEvent dropEvent)
			{
				final DataBoundTransferable transferredData = (DataBoundTransferable) dropEvent.getTransferable();
				if (null != transferredData && transferredData.getItemId() instanceof CapabilityItemVO)
				{
					LOG.log(Level.FINE, "drop allowed at role capability panel");
					final CapabilityItemVO droppedCapabilityItemVO = (CapabilityItemVO) transferredData.getItemId();

					final RoleCapabilityItemVO newRoleCapability = createRoleCapabilityItemVO(droppedCapabilityItemVO.getCapabilityVO());
					BeanItem<RoleCapabilityItemVO> addedItem = roleCapabilitiesContainer.addItem(newRoleCapability);
					LOG.log(Level.FINE, "addedItem = {0}", addedItem);

				}
				else
				{
					LOG.log(Level.FINE, "invalid data dropped in role capability panel");
				}
			}

			@Override
			public AcceptCriterion getAcceptCriterion()
			{
				return AcceptAll.get();
			}
		};
		getControlledView().getRoleCapabilitiesTable().setDropHandler(roleCapabilitiesDropHandler);
	}

	private RoleCapabilityItemVO createRoleCapabilityItemVO(
			final CapabilityVO capabilityVO)
	{
		Embedded icon = IconHelper.createDeleteIcon("Remove Capability");
		final RoleCapabilityItemVO newRoleCapability = new RoleCapabilityItemVOBuilder()
				.setIcon(icon)
				.setRoleCapabilityVO(new RoleCapabilityVOBuilder()
				.setCapabilityVO(capabilityVO)
				.setRoleVO(getCurrentItem().getBean().getRoleVO())
				.createRoleCapabilityVO())
				.createRoleCapabilityItemVO();
		icon.addListener(new MouseEvents.ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void click(MouseEvents.ClickEvent event)
			{
				if (!getControlledView().isReadOnly() && !roleCapabilitiesContainer.removeItem(newRoleCapability))
				{
					popupProvider.popUpError(I18NResource
							.localizeWithParameter("Unable to remove capability _",
							newRoleCapability.getCapability().getTitle()));

				}
			}
		});
		return newRoleCapability;
	}
}
