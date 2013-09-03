package com.remote.remote2d.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.remote.remote2d.Remote2D;
import com.remote.remote2d.art.Fonts;
import com.remote.remote2d.art.TextureLoader;

public class FontRenderer {
	Font font;
	boolean useAntiAliasing;
	public FontRenderer(Font f, boolean useAntiAliasing)
	{
		font = f;
		this.useAntiAliasing = useAntiAliasing;
	}
	
	public BufferedImage createImageFromString(String s, float size, int color)
	{
		if(s.length() == 0)
			return null;
		Font sizedFont = font.deriveFont(size);
		FontRenderContext frc = new FontRenderContext(null,useAntiAliasing,false);
		int width = (int) sizedFont.getStringBounds(s, frc).getWidth();
		int height = (int) sizedFont.getStringBounds(s, frc).getHeight();
		BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D graphics = image.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, useAntiAliasing ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		graphics.setFont(sizedFont);
		graphics.setColor(new Color(color));
		graphics.drawString(s, 0, height-graphics.getFontMetrics(sizedFont).getDescent());//Remember, the y var in drawString is the BASELINE!
		graphics.dispose();
		
		return image;
	}
	
	public int[] getStringDim(String s, float size)
	{
		Font sizedFont = font.deriveFont(size);
		FontRenderContext frc = new FontRenderContext(null,useAntiAliasing,true);
		int width = (int) sizedFont.getStringBounds(s, frc).getWidth();
		int height = (int) sizedFont.getStringBounds(s, frc).getHeight();
		
		int[] r = {width,height};
		return r;
	}
	
	public void drawString(String s, float x, float y, float size, int color)
	{
		BufferedImage image = createImageFromString(s,size,color);
		if(s.equals("") || image == null)
			return;
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		int texID = TextureLoader.loadTexture(image,true,false);
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texID);
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex2f(x, y);
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex2f(x+image.getWidth(), y);
			GL11.glTexCoord2f(1, 1);
			GL11.glVertex2f(x+image.getWidth(), y+image.getHeight());
			GL11.glTexCoord2f(0, 1);
			GL11.glVertex2f(x, y+image.getHeight());
		}
		GL11.glEnd();
		GL11.glDeleteTextures(texID);
		image = null;
	}
	
	public void drawCenteredString(String s, int y, float size, int color)
	{
		int[] stringDim = getStringDim(s,size);
		drawString(s,Remote2D.getInstance().displayHandler.getDimensions().x/2-stringDim[0]/2,y,size,color);
	}
	
	public ArrayList<String> getStringSet(String s, float size, float width)
	{
		ArrayList<String> trueContents = new ArrayList<String>();
		String current = "";
		String[] tokens = s.split(" ");
		
		for(int x=0;x<tokens.length;x++)
		{
			if(getStringDim(current+" "+tokens[x], 20)[0] > width-20)
			{
				trueContents.add(current);
				current = "";
			}
			if(!current.equals(""))
				current += " ";
			current += tokens[x];
		}
		if(!current.trim().equals(""))
			trueContents.add(current);
		return trueContents;
	}
}
