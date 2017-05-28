//========================================================================
//
//File:      $RCSfile: ElementEditingSupport.java,v $
//Version:   $Revision: 1.2 $
//Modified:  $Date: 2013/01/17 03:35:34 $
//
//Copyright 2005-2014 Mentor Graphics Corporation. All rights reserved.
//
//========================================================================
// Licensed under the Apache License, Version 2.0 (the "License"); you may not 
// use this file except in compliance with the License.  You may obtain a copy 
// of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
// WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   See the 
// License for the specific language governing permissions and limitations under
// the License.
//======================================================================== 
//
package org.xtuml.bp.core.editors.editing;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.xtuml.bp.core.CorePlugin;
import org.xtuml.bp.core.Ooaofooa;
import org.xtuml.bp.core.common.ModelElement;
import org.xtuml.bp.core.common.NonRootModelElement;
import org.xtuml.bp.core.common.Transaction;
import org.xtuml.bp.core.common.TransactionException;
import org.xtuml.bp.core.common.TransactionManager;
import org.xtuml.bp.core.editors.focus.viewers.MetamodelTreeViewer;
import org.xtuml.bp.core.inspector.ObjectElement;
import org.xtuml.bp.core.ui.cells.CellModifierProvider;
import org.xtuml.bp.ui.canvas.Ooaofgraphics;

public class ElementEditingSupport extends EditingSupport {

	private MetamodelTreeViewer viewer = null;

	public ElementEditingSupport(MetamodelTreeViewer viewer) {
		super(viewer);
		this.viewer = viewer;
	}

	@Override
	public void setValue(Object element, Object value) {
		if(value == null) {
			return;
		}
		if (element instanceof ObjectElement) {
			ObjectElement objEle = (ObjectElement) element;
			if(objEle.getValue().equals(value)) {
				return;
			}
			// get a transaction from the compare transaction manager
			Transaction transaction = null;
			try {
				transaction = TransactionManager.getSingleton().startTransaction("Modify " + objEle.getName(),
						new ModelElement[] { Ooaofooa.getDefaultInstance(), Ooaofgraphics.getDefaultInstance() });
				NonRootModelElement parent = (NonRootModelElement) objEle.getParent();
				Object attributeOwner = null;
				if(objEle.getAttributeOwner() != null) {
					attributeOwner = objEle.getAttributeOwner();
				}
				if (((NonRootModelElement) objEle.getParent())
						.getModelRoot() instanceof Ooaofooa) {
					CellModifierProvider.setValue(parent, objEle, value, (NonRootModelElement) attributeOwner);
				} else {
					org.xtuml.bp.ui.canvas.cells.CellModifierProvider
							.setValue(parent, objEle, value,
									(NonRootModelElement) attributeOwner);
				}
				if(transaction != null) {
					TransactionManager.getSingleton().endTransaction(transaction);
				}			
			} catch (TransactionException e) {
				CorePlugin.logError("Unable to complete transaction.", e);
			}
		}
	}

	@Override
	public Object getValue(Object element) {
		return ((ITableLabelProvider) viewer.getLabelProvider()).getColumnText(
				element, 1);
	}

	@Override
	public boolean canEdit(Object element) {
		if (element instanceof ObjectElement) {
			ObjectElement object = (ObjectElement) element;
			if (object.getName().equals("Descrip") || object.getName().equals("Action_Semantics")) { //$NON-NLS-1$ //$NON-NLS-2$
				return false;
			}
			if(!object.isUserModifiable()) {
				return false;
			}
			if (((NonRootModelElement) object.getParent())
					.getModelRoot() instanceof Ooaofooa) {
				return CellModifierProvider.supportsEdit(
						(NonRootModelElement) object.getParent(), object, viewer.getTree());
			} else {
				return org.xtuml.bp.ui.canvas.cells.CellModifierProvider.supportsEdit(
						(NonRootModelElement) object.getParent(), object, viewer.getTree());				
			}
		}
		return false;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		if (element instanceof ObjectElement) {
			ObjectElement objEle = (ObjectElement) element;
			CellEditor editor = null;
			if(((NonRootModelElement) objEle.getParent()).getModelRoot() instanceof Ooaofooa) {
				editor = CellModifierProvider.getCellEditor(
						(NonRootModelElement) objEle.getParent(), viewer.getTree(),
						objEle);
			} else {
				editor = org.xtuml.bp.ui.canvas.cells.CellModifierProvider.getCellEditor(
						(NonRootModelElement) objEle.getParent(), viewer.getTree(),
						objEle);				
			}
			final CellEditor finalEditor = editor;
			editor.addListener(new ICellEditorListener() {
				
				@Override
				public void editorValueChanged(boolean oldValidState, boolean newValidState) {
						if(!newValidState) {
							viewer.setErrorMessage(finalEditor.getErrorMessage());
						} else {
							viewer.setErrorMessage("");
						}
				}
				
				@Override
				public void cancelEditor() {
					viewer.setErrorMessage("");
				}
				
				@Override
				public void applyEditorValue() {
					viewer.setErrorMessage("");
				}
			});
			return editor;
		}
		return null;
	}

}
