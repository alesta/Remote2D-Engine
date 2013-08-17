package com.remote.remote2d.gui.editor.operation;

import com.remote.remote2d.entity.Entity;
import com.remote.remote2d.gui.editor.GuiEditor;

public class OperationNewEntity extends Operation{
	
	Entity entity;
		
	public OperationNewEntity(GuiEditor editor)
	{
		super(editor);
	}

	@Override
	public void execute() {
		editor.insertEntity();
	}

	@Override
	public void undo() {
		editor.getMap().getEntityList().removeEntityFromList(entity);
	}

	@Override
	public String name() {
		return "Insert Entity";
	}

	@Override
	public boolean canBeUndone() {
		return true;
	}
	
}
