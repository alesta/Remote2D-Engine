package com.remote.remote2d.gui;

import org.lwjgl.opengl.GL11;

import com.remote.remote2d.art.Texture;
import com.remote.remote2d.logic.Vector2;

public abstract class Gui {
	
	public Gui()
	{
		
	}
	
	public abstract void tick(int i, int j, int k);
	public abstract void render(float interpolation);
	
	public static float[] getRGB(int rgb)
	{
		float r = ((rgb >> 16) & 0xff)/255f;
		float g = ((rgb >> 8) & 0xff)/255f;
		float b = (rgb & 0xff)/255f;
		float[] returnval = {r,g,b};
		return returnval;
	}
	
	public static void bindRGB(int rgb)
	{
		float[] color = getRGB(rgb);
		GL11.glColor3f(color[0],color[1],color[2]);
	}
	
	public static void renderTextureWithCoords(Texture tex, Vector2 pos, Vector2 dim, Vector2 imgPos, Vector2 imgDim)
	{
		tex.bind();
		Vector2 glImgPos = new Vector2((float)imgPos.x/(float)tex.image.getWidth(),
				(float)imgPos.y/(float)tex.image.getHeight());
		Vector2 glImgDim = new Vector2((float)imgDim.x/(float)tex.image.getWidth(),
				(float)imgDim.y/(float)tex.image.getHeight());
		
		GL11.glColor4f(1,1,1,1);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(glImgPos.x, 				glImgPos.y);
			GL11.glVertex2f(pos.x, 			pos.y);
			
			GL11.glTexCoord2f(glImgPos.x+glImgDim.x, 	glImgPos.y);
			GL11.glVertex2f(pos.x+dim.x, 	pos.y);
			
			GL11.glTexCoord2f(glImgPos.x+glImgDim.x, 	glImgPos.y+glImgDim.y);
			GL11.glVertex2f(pos.x+dim.x, 	pos.y+dim.y);
			
			GL11.glTexCoord2f(glImgPos.x, 				glImgPos.y+glImgDim.y);
			GL11.glVertex2f(pos.x, 			pos.y+dim.y);
		GL11.glEnd();
	}
	
}
