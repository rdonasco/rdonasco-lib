/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.capability.controllers;

import com.rdonasco.common.exceptions.DataAccessException;
import com.rdonasco.common.exceptions.WidgetException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.controller.ViewController;
import com.rdonasco.common.vaadin.view.ButtonUtil;
import com.rdonasco.datamanager.controller.DataManagerContainer;
import com.rdonasco.datamanager.controller.DataRetrieveListStrategy;
import com.rdonasco.common.vaadin.controller.ApplicationExceptionPopupProvider;
import com.rdonasco.common.vaadin.controller.ApplicationPopupProvider;
import com.rdonasco.security.app.themes.SecurityDefaultTheme;
import com.rdonasco.security.capability.utils.IconHelper;
import com.rdonasco.security.capability.views.CapabilityEditorView;
import com.rdonasco.security.capability.vo.ActionItemVO;
import com.rdonasco.security.capability.vo.ActionItemVOBuilder;
import com.rdonasco.security.capability.vo.CapabilityItemVO;
import com.rdonasco.security.capability.vo.ResourceItemVO;
import com.rdonasco.security.exceptions.CapabilityManagerException;
import com.rdonasco.security.vo.ActionVO;
import com.rdonasco.security.vo.ResourceVO;
import com.vaadin.Application;
import com.vaadin.data.Buffered;
import com.vaadin.data.Container;
import com.vaadin.data.Validator;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.DataBoundTransferable;
import com.vaadin.event.MouseEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.SourceIs;
import com.vaadin.event.dd.acceptcriteria.And;
import com.vaadin.event.dd.acceptcriteria.ClientSideCriterion;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.vaadin.addon.formbinder.ViewBoundForm;

/**
 *
 * @author Roy F. Donasco
 */
public class CapabilityEditorViewController implements
		ViewController<CapabilityEditorView>, Serializable
{

	private DropHandler resourceDropHander;
	@Inject
	private ApplicationPopupProvider popupProvider;
	@Inject
	private ApplicationExceptionPopupProvider exceptionPopupProvider;

	public enum EditorMode
	{

		EDIT, VIEW
	};
	private EditorMode editorMode = EditorMode.VIEW;
	private static final Logger LOG = Logger.getLogger(CapabilityEditorViewController.class.getName());
	private static final long serialVersionUID = 1L;
	@Inject
	private CapabilityEditorView editorView;
	@Inject
	private CapabilityDataManagerDecorator capabilityDataManager;
	private BeanItemContainer<ActionItemVO> actionsContainer = new BeanItemContainer<ActionItemVO>(ActionItemVO.class);
	private BeanItem<CapabilityItemVO> currentItem;
	private DataManagerContainer<ResourceVO> resourceComboboxDataContainer = new DataManagerContainer<ResourceVO>(ResourceVO.class);
	private Table actionTableSource;
	@Inject
	private Application application;

	@PostConstruct
	@Override
	public void initializeControlledViewBehavior()
	{
		try
		{
			editorView.initWidget();
			configureResourceComboBox();
			configureActionTable();
			configureForm();
			configureButtons();

		}
		catch (Exception ex)
		{
			LOG.log(Level.SEVERE, ex.getMessage(), ex);
		}
	}

	private void addActionVOToContainer(ActionVO action)
	{
		Embedded icon = IconHelper.createDeleteIcon(I18NResource.localize("Remove action"));
		final ActionItemVO actionItemVO = new ActionItemVOBuilder()
				.setAction(action)
				.setIcon(icon)
				.createActionItemVO();
		actionsContainer.addItem(actionItemVO);
		icon.addListener(new MouseEvents.ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void click(MouseEvents.ClickEvent event)
			{
				if (!editorView.getEditorForm().isReadOnly() && !actionsContainer.removeItem(actionItemVO))
				{
					popupProvider.popUpError(I18NResource.localizeWithParameter("Unable to remove action _", actionItemVO));

				}
			}
		});
	}

	void setResourceTableSource(final Table resourceEditorTable)
	{
		final SourceIs sourceIs = new SourceIs(resourceEditorTable);
		resourceDropHander = new DropHandler()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void drop(DragAndDropEvent dropEvent)
			{
				if (getEditorMode() == EditorMode.EDIT)
				{
					final DataBoundTransferable transferredData = (DataBoundTransferable) dropEvent
							.getTransferable();
					final ResourceItemVO sourceItemId = (ResourceItemVO) transferredData.getItemId();
					editorView.getResourceField().setValue(sourceItemId.getResource());
					LOG.info("drop allowed at capabity field panel");
				}
				else
				{
					LOG.info("drop not allowed at capabity field panel");
				}
			}

			@Override
			public AcceptCriterion getAcceptCriterion()
			{
				return sourceIs;
			}
		};
	}

	public Table getActionTableSource()
	{
		return actionTableSource;
	}

	public void setActionTableSource(Table actionTableSource)
	{
		this.actionTableSource = actionTableSource;
	}

	public EditorMode getEditorMode()
	{
		return editorMode;
	}

	public void setEditorMode(EditorMode editorMode)
	{
		this.editorMode = editorMode;
	}

	@Override
	public CapabilityEditorView getControlledView()
	{
		return editorView;
	}

	@Override
	public void refreshView() throws WidgetException
	{
		editorView.removeAllComponents();
		editorView.initWidget();
	}

	private void configureResourceComboBox() throws DataAccessException
	{
		resourceComboboxDataContainer.setDataRetrieveListStrategy(new DataRetrieveListStrategy<ResourceVO>()
		{
			@Override
			public List<ResourceVO> retrieve() throws DataAccessException
			{
				try
				{
					return capabilityDataManager.findAllResources();
				}
				catch (CapabilityManagerException ex)
				{
					throw new DataAccessException(ex);
				}
			}
		});
		resourceComboboxDataContainer.refresh();
		editorView.getResourceField().setContainerDataSource(resourceComboboxDataContainer);
	}

	public void setCurrentItem(BeanItem<CapabilityItemVO> currentItem)
	{
		this.currentItem = currentItem;
		editorView.getEditorForm().setItemDataSource(currentItem);
		actionsContainer.removeAllItems();
		if (null != currentItem)
		{
			for (ActionVO action : currentItem.getBean().getActions())
			{
				addActionVOToContainer(action);
			}
		}
		setViewToReadOnly();
	}

	public void setViewToReadOnly()
	{
		editorView.getEditorForm().setReadOnly(true);
		editorView.getActionsTable().setReadOnly(true);
		editorView.getCancelButton().setVisible(false);
		editorView.getEditButton().setEnabled(true);
		editorView.getEditButton().setVisible(true);
		editorView.getSaveButton().setEnabled(false);
		editorView.getSaveButton().setVisible(false);
		editorView.getCancelButton().setEnabled(false);
		editorView.getCancelButton().setVisible(false);
		editorView.getSaveButton().setComponentError(null);
		editorView.getActionsTable().setDropHandler(null);
		editorView.getResourceDragAndDropWrapper().setDropHandler(null);
		setEditorMode(EditorMode.VIEW);
	}

	public void setViewToEditMode()
	{
		setEditorMode(EditorMode.EDIT);
		try
		{
			resourceComboboxDataContainer.refresh();
		}
		catch (DataAccessException ex)
		{
			exceptionPopupProvider.popUpErrorException(ex);
		}
		editorView.getEditorForm().setReadOnly(false);
		editorView.getActionsTable().setReadOnly(false);
		editorView.getCancelButton().setVisible(true);
		editorView.getCancelButton().setEnabled(true);
		editorView.getEditButton().setEnabled(false);
		editorView.getEditButton().setVisible(false);
		editorView.getSaveButton().setEnabled(true);
		editorView.getSaveButton().setVisible(true);
		editorView.getActionsTable().setDropHandler(actionDropHandler);
		editorView.getResourceDragAndDropWrapper().setDropHandler(resourceDropHander);
		editorView.getTitleField().focus();
	}

	private void configureActionTable()
	{
		Table actionTable = editorView.getActionsTable();
		actionTable.setSelectable(true);
		actionTable.setContainerDataSource(actionsContainer);
		actionTable.setVisibleColumns(new String[]
		{
			"icon", "name"
		});
		editorView.getActionsTable().setColumnHeaders(new String[]
		{
			"", I18NResource.localize("Name")
		});
		actionTable.setCellStyleGenerator(new Table.CellStyleGenerator()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getStyle(Object itemId, Object propertyId)
			{
				String style = null;
				if ("icon".equals(propertyId))
				{
					style = SecurityDefaultTheme.CSS_ICON_IN_A_CELL;
				}
				else if ("name".equals(propertyId))
				{
					style = SecurityDefaultTheme.CSS_FULL_WIDTH;
				}
				return style;
			}
		});
	}

	private void configureForm() throws Buffered.SourceException,
			Validator.InvalidValueException
	{
		ViewBoundForm form;
		form = new ViewBoundForm(editorView);
		editorView.setEditorForm(form);
		form.setWriteThrough(false);
	}

	private void configureButtons()
	{
		ButtonUtil.disableButtons(editorView.getEditButton(), editorView.getSaveButton(), editorView.getCancelButton());
		ButtonUtil.hideButtons(editorView.getEditButton(), editorView.getSaveButton(), editorView.getCancelButton());

		editorView.getEditButton().addListener(new Button.ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(Button.ClickEvent event)
			{
				setViewToEditMode();
			}
		});
		int[] keyModifiers = new int[]
		{
			ShortcutAction.ModifierKey.CTRL
		};
		editorView.getEditButton().addShortcutListener(
				new ShortcutListener("Edit (ctrl+E)",
				ShortcutAction.KeyCode.E, keyModifiers)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target)
			{
				if (editorView.getEditButton().isEnabled())
				{
					setViewToEditMode();
				}
			}
		});
		editorView.getEditButton().setDescription("Edit (ctrl+E)");
		editorView.getSaveButton().addListener(new Button.ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(Button.ClickEvent event)
			{
				saveCapability();
			}
		});
		editorView.getSaveButton().addShortcutListener(new ShortcutListener("Save (ctrl+S)",
				ShortcutAction.KeyCode.S, keyModifiers)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target)
			{
				if (editorView.getSaveButton().isEnabled())
				{
					saveCapability();
				}
			}
		});
		editorView.getSaveButton().setDescription("Save (ctrl+S)");
		editorView.getCancelButton().addListener(new Button.ClickListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(Button.ClickEvent event)
			{
				discardChanges();
			}
		});
		editorView.getCancelButton().setDescription("Cancel (Esc)");
		editorView.getCancelButton().addShortcutListener(new ShortcutListener(null, KeyCode.ESCAPE, null)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target)
			{
				if (editorView.getCancelButton().isEnabled())
				{
					discardChanges();
				}
			}
		});
	}

	private void discardChanges() throws Buffered.SourceException
	{
		editorView.getEditorForm().discard();
		setCurrentItem(currentItem);
		setViewToReadOnly();
	}

	private void saveCapability() throws Buffered.SourceException,
			Validator.InvalidValueException
	{
		try
		{
			editorView.getEditorForm().commit();
			List<ActionVO> actions = new ArrayList<ActionVO>();
			for (ActionItemVO actionItem : actionsContainer.getItemIds())
			{
				actions.add(ActionVO.createWithIdAndName(
						actionItem.getId(),
						actionItem.getName()));
			}
			currentItem.getBean().setActions(actions);
			CapabilityItemVO capabilityItemVO = ((BeanItem<CapabilityItemVO>) editorView.getEditorForm().getItemDataSource()).getBean();
			capabilityDataManager.updateData(capabilityItemVO);
			setViewToReadOnly();
			popupProvider.popUpInfo(I18NResource.localizeWithParameter("Capability _ Saved", capabilityItemVO.getTitle()));
		}
		catch (DataAccessException ex)
		{
			exceptionPopupProvider.popUpErrorException(ex);
		}
	}
	private DropHandler actionDropHandler = new DropHandler()
	{
		private static final long serialVersionUID = 1L;

		@Override
		public void drop(DragAndDropEvent dropEvent)
		{
			// criteria verify that this is safe
			final DataBoundTransferable transferredData = (DataBoundTransferable) dropEvent
					.getTransferable();
			final Container sourceContainer = transferredData.getSourceContainer();
			final Object sourceItemId = transferredData.getItemId();
			addActionVOToContainer(((ActionItemVO) sourceItemId).getAction());
			for (Object object : sourceContainer.getItemIds())
			{
				if (getActionTableSource().isSelected(object))
				{
					addActionVOToContainer(((ActionItemVO) object).getAction());
				}
			}
			LOG.info("drop event allowed");
		}

		@Override
		public AcceptCriterion getAcceptCriterion()
		{
			ClientSideCriterion sourceCriterion = new SourceIs(getActionTableSource());
			return new And(sourceCriterion);
		}
	};
}
