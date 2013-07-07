package com.remote.remote2d.gui.editor.inspector;

import com.remote.remote2d.gui.Gui;
import com.remote.remote2d.io.R2DType;
import com.remote.remote2d.logic.Vector2;

public abstract class GuiEditorInspectorSection extends Gui {
	
	public Vector2 pos;
	protected String name;
	protected int width;
	
	public GuiEditorInspectorSection(String name, Vector2 pos, int width)
	{
		this.pos = pos.copy();
		this.name = name;
		this.width = width;
	}
	
	public abstract int getHeight();
	public abstract Object getData();
	public abstract void initSection();
	public abstract void setData(Object o);
	public abstract void deselect();
	public abstract boolean isSelected();
	public abstract boolean isComplete();
	public abstract boolean hasFieldBeenChanged();
}
