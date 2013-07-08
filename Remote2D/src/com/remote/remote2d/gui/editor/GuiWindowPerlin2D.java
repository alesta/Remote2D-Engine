package com.remote.remote2d.gui.editor;

import java.awt.image.BufferedImage;

import org.lwjgl.opengl.GL11;

import com.remote.remote2d.art.Texture;
import com.remote.remote2d.gui.GuiButton;
import com.remote.remote2d.gui.GuiWindow;
import com.remote.remote2d.gui.WindowHolder;
import com.remote.remote2d.logic.ColliderBox;
import com.remote.remote2d.logic.Noise2D;
import com.remote.remote2d.logic.Vector2;

public class GuiWindowPerlin2D extends GuiWindow {
	
	private Noise2D perlin;
	private BufferedImage values;
	private Texture tex;

	public GuiWindowPerlin2D(WindowHolder holder, Vector2 pos, ColliderBox allowedBounds) {
		super(holder, pos, new Vector2(300,300), allowedBounds, "2D Perlin Noise");
		generate();
	}
	
	public void generate()
	{
		if(tex != null)
			tex.removeTexture();
		values = Noise2D.perlinImage(300, 300, 6);
		tex = new Texture(values,true,false);
	}
	
	@Override
	public void renderContents(float interpolation) {
		tex.bind();
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glColor3f(1, 1, 1);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex2f(0,0);
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex2f(300,0);
			GL11.glTexCoord2f(1, 1);
			GL11.glVertex2f(300,300);
			GL11.glTexCoord2f(0, 1);
			GL11.glVertex2f(0,300);
		GL11.glEnd();
	}
	
	@Override
	public void initGui()
	{
		buttonList.clear();
		buttonList.add(new GuiButton(0,new Vector2(30,10),new Vector2(240,40),"Regenerate"));
	}
	
	@Override
	public void actionPerformed(GuiButton button)
	{
		if(button.id == 0)
			generate();
	}

}
