package com.remote.remote2d.gui.editor.inspector;

import org.lwjgl.opengl.GL11;

import com.remote.remote2d.Remote2D;
import com.remote.remote2d.art.Fonts;
import com.remote.remote2d.gui.GuiTextField;
import com.remote.remote2d.gui.TextLimiter;
import com.remote.remote2d.logic.Vector2D;

public class GuiEditorInspectorSectionBoolean extends GuiEditorInspectorSection {
	
	boolean isTrue = false;

	public GuiEditorInspectorSectionBoolean(String name, Vector2D pos, int width) {
		super(name, pos, width);
	}

	@Override
	public int getHeight() {
		return 20;
	}

	@Override
	public Object getData() {
		return isTrue;
	}

	@Override
	public void initSection() {
		
	}

	@Override
	public void tick(int i, int j, int k, double delta) {
		if(i > pos.x+width-20 && i < pos.x+width && j > pos.y && j < pos.y+20 && Remote2D.getInstance().hasMouseBeenPressed())
			isTrue = !isTrue;
	}

	@Override
	public void render() {
		Fonts.get("Arial").drawString(name, pos.x, pos.y, 20, 0xffffff);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		GL11.glVertex2f(pos.x+width-20, pos.y);
		GL11.glVertex2f(pos.x+width, pos.y);
		GL11.glVertex2f(pos.x+width, pos.y+20);
		GL11.glVertex2f(pos.x+width-20, pos.y+20);
		GL11.glVertex2f(pos.x+width-20, pos.y);
		GL11.glEnd();
		
		if(isTrue)
		{
			GL11.glBegin(GL11.GL_LINE_STRIP);
				GL11.glVertex2f(pos.x+width-20, pos.y);
				GL11.glVertex2f(pos.x+width, pos.y+20);
				
				GL11.glVertex2f(pos.x+width-20, pos.y+20);
				GL11.glVertex2f(pos.x+width, pos.y);
			GL11.glEnd();
		}
	}
	
	@Override
	public void setData(Object o) {
		if(o instanceof Boolean)
		{
			isTrue = ((Boolean)o).booleanValue();
		}
	}

	@Override
	public void deselect() {
		
	}
	
	@Override
	public boolean isSelected() {
		return false;
	}

}
