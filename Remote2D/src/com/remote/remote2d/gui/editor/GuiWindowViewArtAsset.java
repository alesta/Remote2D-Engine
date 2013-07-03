package com.remote.remote2d.gui.editor;

import org.lwjgl.opengl.GL11;

import com.remote.remote2d.Remote2D;
import com.remote.remote2d.gui.GuiButton;
import com.remote.remote2d.gui.GuiTextField;
import com.remote.remote2d.gui.GuiWindow;
import com.remote.remote2d.gui.WindowHolder;
import com.remote.remote2d.logic.ColliderBox;
import com.remote.remote2d.logic.Vector2D;

public class GuiWindowViewArtAsset extends GuiWindow {
	
	GuiTextField field;
	GuiButton doneButton;

	public GuiWindowViewArtAsset(WindowHolder holder, Vector2D pos, ColliderBox allowedBounds) {
		super(holder, pos, new Vector2D(400,450), allowedBounds, "Art Viewer/Selecter");
		field = new GuiTextField(new Vector2D(10,10), new Vector2D(380,40), 20);
	}
	
	public void initGui()
	{
		buttonList.clear();
		doneButton = new GuiButton(0, new Vector2D(20,400), new Vector2D(360,40), "Done");
		buttonList.add(doneButton);
	}

	@Override
	public void renderContents(float interpolation) {
		field.render(interpolation);

		if(Remote2D.getInstance().artLoader.textureExists(field.text))
		{
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			Remote2D.getInstance().artLoader.getTexture(field.text).bind();
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(0, 0);
				GL11.glVertex2f(10,60);
				GL11.glTexCoord2f(1, 0);
				GL11.glVertex2f(390,60);
				GL11.glTexCoord2f(1, 1);
				GL11.glVertex2f(390,390);
				GL11.glTexCoord2f(0, 1);
				GL11.glVertex2f(10,390);
			GL11.glEnd();
		}
	}
	
	@Override
	public void tick(int i, int j, int k)
	{
		super.tick(i, j, k);
		field.tick(getMouseInWindow(i, j).x, getMouseInWindow(i, j).y, k);
	}
	
	@Override
	public void actionPerformed(GuiButton button)
	{
		if(button.id == 0)
		{
			holder.closeWindow(this);
		}
	}
	
	
	
}
