package com.remote.remote2d.gui.editor.inspector;

import com.remote.remote2d.art.Fonts;
import com.remote.remote2d.gui.editor.GuiEditor;
import com.remote.remote2d.logic.Vector2;

public class GuiEditorInspectorSectionSet extends GuiEditorInspectorSection {
	
	GuiEditorInspectorSection[] set;
	
	
	public GuiEditorInspectorSectionSet(String name, GuiEditor inspector, Vector2 pos, int width) {
		super(name, inspector, pos, width);
		
	}

	@Override
	public int sectionHeight() {
		// TODO Auto-generated method stub
		return 120;
	}

	@Override
	public Object getData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initSection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setData(Object o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deselect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isSelected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasFieldBeenChanged() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void tick(int i, int j, int k) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float interpolation) {
		Fonts.get("Arial").drawString(renderName, pos.x, pos.y, 20, isComplete() ? 0xffffff : 0xff7777);
		
	}

}
