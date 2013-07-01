package com.remote.remote2d.gui.editor.inspector;

import org.lwjgl.input.Keyboard;

import com.remote.remote2d.Remote2D;
import com.remote.remote2d.art.Fonts;
import com.remote.remote2d.art.Texture;
import com.remote.remote2d.gui.GuiTextField;
import com.remote.remote2d.gui.TextLimiter;
import com.remote.remote2d.logic.Vector2D;

public class GuiEditorInspectorSectionTexture extends GuiEditorInspectorSection {
	
	GuiTextField textField;

	public GuiEditorInspectorSectionTexture(String name, Vector2D pos, int width) {
		super(name, pos, width);
		textField = new GuiTextField(pos.add(new Vector2D(10,20)), new Vector2D(width-20,20), 20);
	}

	@Override
	public int getHeight() {
		return 40;
	}

	@Override
	public Object getData() {
		return Remote2D.getInstance().artLoader.getTexture(textField.text);
	}

	@Override
	public void initSection() {
		
	}

	@Override
	public void tick(int i, int j, int k, double delta) {
		textField.tick(i, j, k, delta);
	}

	@Override
	public void render() {
		Fonts.get("Arial").drawString(name, pos.x, pos.y, 20, isComplete() ? 0xffffff : 0xff7777);
		textField.render();
	}
	
	@Override
	public void setData(Object o) {
		if(o instanceof Texture)
		{
			textField.text = ((Texture)o).textureLocation;
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
		return Remote2D.getInstance().artLoader.textureExists(textField.text);
	}
	
	@Override
	public boolean hasFieldBeenChanged() {
		return textField.isSelected() && isComplete() && Remote2D.getInstance().getIntegerKeyboardList().contains(Keyboard.KEY_RETURN);
	}

}
