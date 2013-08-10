/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rdonasco.security.capability.views;

import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.i18.I18NResource;
import com.rdonasco.common.vaadin.view.ControlledView;
import com.rdonasco.security.app.themes.SecurityDefaultTheme;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Roy F. Donasco
 */
public class CapabilityEditorView extends VerticalLayout implements
		ControlledView
{

	private static final long serialVersionUID = 1L;
	private Panel capabilityDetailPanel = new Panel();
	private Panel capabilityActionsPanel = new Panel();
	private HorizontalLayout buttonLayout = new HorizontalLayout();
	private Button saveButton = new Button();
	private Button editButton = new Button();
	private Button cancelButton = new Button();
	private TextField titleField = new TextField(I18NResource.localize("Title"));
	private TextArea descriptionField = new TextArea(I18NResource.localize("Description"));
	private ComboBox resourceField = new ComboBox(I18NResource.localize("Resource"));
	private ComboBox applicationField = new ComboBox(I18NResource.localize("Application"));
	private Table actionsTable = new Table();
	private Form editorForm;
	private DragAndDropWrapper resourceDragAndDropWrapper;

	public Form getEditorForm()
	{
		return editorForm;
	}

	public void setEditorForm(Form editorForm)
	{
		this.editorForm = editorForm;
	}

	@Override
	public void initWidget() throws WidgetInitalizeException
	{
		configureCapabilityFields();
		configureCapabilityActionFields();
		configureEditorButtons();
		addComponent(capabilityDetailPanel);
		addComponent(capabilityActionsPanel);
		addComponent(buttonLayout);
		setExpandRatio(capabilityActionsPanel, 1);

		addStyleName(SecurityDefaultTheme.CSS_CAPABILITY_EDITOR);
		setSpacing(true);
	}

	public DragAndDropWrapper getResourceDragAndDropWrapper()
	{
		return resourceDragAndDropWrapper;
	}

	public Button getSaveButton()
	{
		return saveButton;
	}

	public Button getEditButton()
	{
		return editButton;
	}

	public Button getCancelButton()
	{
		return cancelButton;
	}

	public TextField getTitleField()
	{
		return titleField;
	}

	public ComboBox getApplicationField()
	{
		return applicationField;
	}

	public ComboBox getResourceField()
	{
		return resourceField;
	}

	public Table getActionsTable()
	{
		return actionsTable;
	}

	private void configureCapabilityFields()
	{
		capabilityDetailPanel.setCaption(I18NResource.localize("Capability Editor"));
		capabilityDetailPanel.setStyleName(SecurityDefaultTheme.CSS_PANEL_BUBBLE);
		VerticalLayout fieldLayout = new VerticalLayout();
		titleField.setWidth(300f, UNITS_PIXELS);
		fieldLayout.addComponent(titleField);
		descriptionField.setWidth(300f, UNITS_PIXELS);
		fieldLayout.addComponent(applicationField);
		fieldLayout.addComponent(resourceField);
		fieldLayout.addComponent(descriptionField);
		fieldLayout.setSpacing(true);
		resourceDragAndDropWrapper = new DragAndDropWrapper(fieldLayout);
		capabilityDetailPanel.addComponent(resourceDragAndDropWrapper);
		titleField.setReadOnly(true);
		descriptionField.setReadOnly(true);
		resourceField.setReadOnly(true);
		applicationField.setReadOnly(true);
	}

	private void configureCapabilityActionFields()
	{
		capabilityActionsPanel.setCaption(I18NResource.localize("Allowed Actions"));
		capabilityActionsPanel.setStyleName(SecurityDefaultTheme.CSS_PANEL_BUBBLE);
		capabilityActionsPanel.addComponent(getActionsTable());
		getActionsTable().addStyleName(SecurityDefaultTheme.CSS_TABLE_SMALL_STRIPED);
		getActionsTable().addStyleName(SecurityDefaultTheme.CSS_TABLE_BORDERLESS);
		getActionsTable().setSizeFull();
		((VerticalLayout) capabilityActionsPanel.getContent()).setExpandRatio(getActionsTable(), 1);
	}

	private void configureEditorButtons()
	{
		editButton.setCaption(I18NResource.localize("Edit"));
		editButton.setIcon(new ThemeResource(SecurityDefaultTheme.ICON_16x16_EDIT));
		saveButton.setCaption(I18NResource.localize("Save"));
		saveButton.setIcon(new ThemeResource(SecurityDefaultTheme.ICON_16x16_SAVE));
		cancelButton.setCaption(I18NResource.localize("Cancel"));
		cancelButton.setIcon(new ThemeResource(SecurityDefaultTheme.ICON_16x16_CANCEL));

		saveButton.addStyleName(SecurityDefaultTheme.CSS_BUTTON_DEFAULT);
		buttonLayout.addComponent(editButton);
		buttonLayout.addComponent(saveButton);
		buttonLayout.addComponent(cancelButton);
		buttonLayout.setSpacing(true);
	}
}
