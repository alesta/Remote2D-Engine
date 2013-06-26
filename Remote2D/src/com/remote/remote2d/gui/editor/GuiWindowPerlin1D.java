package com.remote.remote2d.gui.editor;

import org.lwjgl.opengl.GL11;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.gui.GuiButton;
import com.remote.remote2d.gui.GuiWindow;
import com.remote.remote2d.gui.WindowHolder;
import com.remote.remote2d.logic.ColliderBox;
import com.remote.remote2d.logic.Noise1D;
import com.remote.remote2d.logic.Vector2D;

public class GuiWindowPerlin1D extends GuiWindow {
		private float[] values;

	public GuiWindowPerlin1D(WindowHolder holder, Vector2D pos, ColliderBox allowedBounds) {
		super(holder, pos, new Vector2D(300,300), allowedBounds, "1D Perlin Noise");
		generate();
	}
	
	public void generate()
	{
		values = Noise1D.GeneratePerlinNoise(Noise1D.GenerateWhiteNoise(300, (int) System.currentTimeMillis()), 6);
	}
	
	@Override
	public void renderContents() {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		GL11.glColor3f(1, 1, 1);
		for(int x=0;x<values.length;x++)
		{
			GL11.glVertex2f(x, 300-values[x]*300);
		}
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	@Override
	public void initGui()
	{
		buttonList.clear();
		buttonList.add(new GuiButton(0,new Vector2D(30,10),new Vector2D(240,40),"Regenerate"));
	}
	
	@Override
	public void actionPerformed(GuiButton button)
	{
		if(button.id == 0)
			generate();
	}

}
