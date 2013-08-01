package com.remote.remote2d.gui.editor.operation;

import com.remote.remote2d.entity.Entity;
import com.remote.remote2d.gui.editor.GuiEditor;

public class OperationDeleteEntity extends Operation {
	
	Entity entity;
	int position;

	public OperationDeleteEntity(GuiEditor editor) {
		super(editor);
		this.entity = editor.getSelectedEntity();
	}

	@Override
	public void execute() {
		position = editor.getMap().getEntityList().indexOf(entity);
		editor.getMap().getEntityList().removeEntityFromList(entity);
	}

	@Override
	public void undo() {
		editor.getMap().getEntityList().addEntityToList(entity,position);
	}

	@Override
	public String name() {
		return "Delete Entity";
	}

	@Override
	public boolean canBeUndone() {
		return true;
	}

}
