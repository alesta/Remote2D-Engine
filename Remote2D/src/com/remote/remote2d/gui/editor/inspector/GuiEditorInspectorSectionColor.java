package com.remote.remote2d.gui.editor.inspector;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.Remote2D;
import com.remote.remote2d.art.Fonts;
import com.remote.remote2d.gui.GuiTextField;
import com.remote.remote2d.gui.TextLimiter;
import com.remote.remote2d.logic.Vector2D;

public class GuiEditorInspectorSectionColor extends GuiEditorInspectorSection {
	
	GuiTextField textField;

	public GuiEditorInspectorSectionColor(String name, Vector2D pos, int width) {
		super(name, pos, width);
		textField = new GuiTextField(pos.add(new Vector2D(10,20)), new Vector2D(width-20,20), 20);
		textField.limitToDigits = TextLimiter.LIMIT_TO_HEX;
		textField.prefix = "0x";
		textField.maxLength = 6;
	}

	@Override
	public int getHeight() {
		return 40;
	}

	@Override
	public Object getData() {
		return new Color(Integer.parseInt(textField.text, 16));
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
		if(o instanceof Color)
		{
			
			textField.text = Integer.toHexString(((Color)o).getRGB()).substring(2);
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
		return textField.text.length() == 6;
	}
	
	@Override
	public boolean hasFieldBeenChanged() {
		return textField.isSelected() && isComplete() && Remote2D.getInstance().getIntegerKeyboardList().contains(Keyboard.KEY_RETURN);
	}

}
