package com.remote.remote2d.gui.editor.operation;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.entity.Entity;
import com.remote.remote2d.gui.editor.GuiEditor;

public class OperationEditEntity extends Operation {
	
	Entity before;
	Entity after;

	public OperationEditEntity(GuiEditor editor, Entity before, Entity after) {
		super(editor);
		this.before = before;
		this.after = after;
	}

	@Override
	public void execute() {
		int position = editor.getMap().getEntityList().indexOf(before);
		after.updatePos();
		editor.getMap().getEntityList().set(position,after);
		editor.setSelectedEntity(position);
	}

	@Override
	public void undo() {
		int position = editor.getMap().getEntityList().indexOf(after);
		before.updatePos();
		editor.getMap().getEntityList().set(position,before);
		editor.setSelectedEntity(position);
	}

	@Override
	public String name() {
		return "Edit Entity";
	}

	@Override
	public boolean canBeUndone() {
		return true;
	}

}
