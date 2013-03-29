/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rdonasco.security.capability.controllers;

import com.rdonasco.common.exceptions.WidgetException;
import com.rdonasco.common.exceptions.WidgetInitalizeException;
import com.rdonasco.common.vaadin.controller.ViewController;
import com.rdonasco.security.app.controllers.ApplicationExceptionPopupProvider;
import com.rdonasco.security.capability.views.CapabilityEditorView;
import com.rdonasco.security.vo.CapabilityVO;
import com.rdonasco.security.vo.CapabilityVOBuilder;
import com.vaadin.data.util.BeanItem;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.vaadin.addon.formbinder.ViewBoundForm;

/**
 *
 * @author Roy F. Donasco
 */
public class CapabilityEditorViewController implements
		ViewController<CapabilityEditorView>, Serializable
{
	private static final long serialVersionUID = 1L;
	@Inject
	private CapabilityEditorView editorView;
	@Inject
	private Instance<ApplicationExceptionPopupProvider> exceptionPopupInstances;

	@PostConstruct
	@Override
	public void initializeControlledViewBehavior()
	{
		ViewBoundForm form;
		try
		{
			editorView.initWidget();
			CapabilityVO capability = createTestDataCapabilityVO();
			form = new ViewBoundForm(editorView);
			editorView.setEditorForm(form);
			form.setWriteThrough(false);
			form.setItemDataSource(new BeanItem<CapabilityVO>(capability));
		}
		catch (WidgetInitalizeException ex)
		{
			exceptionPopupInstances.get().popUpErrorException(ex);
		}
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

	private CapabilityVO createTestDataCapabilityVO()
	{
		return new CapabilityVOBuilder()
				.setId(1L)
				.setTitle("test title")
				.setDescription("test description")
				.createCapabilityVO();
	}
}
