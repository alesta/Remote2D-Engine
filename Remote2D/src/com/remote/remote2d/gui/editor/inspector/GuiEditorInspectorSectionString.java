package com.remote.remote2d.gui.editor.inspector;

import org.lwjgl.input.Keyboard;

import com.remote.remote2d.Remote2D;
import com.remote.remote2d.art.Fonts;
import com.remote.remote2d.gui.GuiTextField;
import com.remote.remote2d.gui.TextLimiter;
import com.remote.remote2d.logic.Vector2;

public class GuiEditorInspectorSectionString extends GuiEditorInspectorSection {
	
	GuiTextField textField;

	public GuiEditorInspectorSectionString(String name, Vector2 pos, int width) {
		super(name, pos, width);
		textField = new GuiTextField(pos.add(new Vector2(10,20)), new Vector2(width-20,20), 20);
	}

	@Override
	public int getHeight() {
		return 40;
	}

	@Override
	public Object getData() {
		return textField.text;
	}

	@Override
	public void initSection() {
		
	}

	@Override
	public void tick(int i, int j, int k) {
		textField.tick(i, j, k);
	}

	@Override
	public void render(float interpolation) {
		Fonts.get("Arial").drawString(name, pos.x, pos.y, 20, isComplete() ? 0xffffff : 0xff7777);
		textField.render(interpolation);
	}
	
	@Override
	public void setData(Object o) {
		if(o instanceof String)
		{
			textField.text = (String)o;
		}
	}
	
	@Override
	public void deselect() {
		textField.deselect();
	}
	
	@Override
	public boolean isSelected() {
		return textField.isSelected();
	}

	@Override
	public boolean isComplete() {
		return true;
	}
	
	@Override
	public boolean hasFieldBeenChanged() {
		return textField.isSelected() && isComplete() && Remote2D.getInstance().getIntegerKeyboardList().contains(Keyboard.KEY_RETURN);
	}

}
