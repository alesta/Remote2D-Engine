package com.remote.remote2d.gui.editor.inspector;

import com.remote.remote2d.gui.Gui;
import com.remote.remote2d.gui.editor.DraggableObject;
import com.remote.remote2d.io.R2DType;
import com.remote.remote2d.logic.Vector2;

public abstract class GuiEditorInspectorSection extends Gui {
	
	public Vector2 pos;
	protected String name;
	public String renderName;
	protected int width;
	
	public GuiEditorInspectorSection(String name, Vector2 pos, int width)
	{
		this.pos = pos.copy();
		this.name = name;
		this.width = width;
		
		renderName = splitCamelCase(name);
		if(Character.isLowerCase(renderName.charAt(0)))
			renderName = Character.toUpperCase(name.charAt(0))+name.substring(1);
	}
	
	public abstract int sectionHeight();
	public abstract Object getData();
	public abstract void initSection();
	public abstract void setData(Object o);
	public abstract void deselect();
	public abstract boolean isSelected();
	public abstract boolean isComplete();
	public abstract boolean hasFieldBeenChanged();
	
	public boolean acceptsDraggableObject(DraggableObject object)
	{
		return false;
	}
	
	public void acceptDraggableObject(DraggableObject object)
	{
		
	}
	
	public static String splitCamelCase(String s) {
	   return s.replaceAll(
	      String.format("%s|%s|%s",
	         "(?<=[A-Z])(?=[A-Z][a-z])",
	         "(?<=[^A-Z])(?=[A-Z])",
	         "(?<=[A-Za-z])(?=[^A-Za-z])"
	      ),
	      " "
	   );
	}
}
