package com.remote.remote2d.gui.editor;

import org.lwjgl.opengl.GL11;

import com.remote.remote2d.gui.GuiTextField;
import com.remote.remote2d.gui.GuiWindow;
import com.remote.remote2d.gui.WindowHolder;
import com.remote.remote2d.logic.ColliderBox;
import com.remote.remote2d.logic.Vector2D;

public class GuiWindowTest extends GuiWindow {
	
	GuiTextField field;

	public GuiWindowTest(WindowHolder holder, Vector2D pos, Vector2D dim, ColliderBox allowedBounds, String title) {
		super(holder, pos, dim, allowedBounds, title);
		field = new GuiTextField(new Vector2D(30, 30), new Vector2D(200,40), 20);
	}

	@Override
	public void renderContents() {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor3f(1, 1, 1);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f(0,  0);
			GL11.glVertex2f(20, 0);
			GL11.glVertex2f(20, 20);
			GL11.glVertex2f(0,  20);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		field.render();
	}
	
	@Override
	public void tick(int i, int j, int k, double delta)
	{
		super.tick(i, j, k, delta);
		Vector2D mouseInWindow = getMouseInWindow(i,j);
		field.tick(mouseInWindow.x, mouseInWindow.y, k, delta);
	}

}
