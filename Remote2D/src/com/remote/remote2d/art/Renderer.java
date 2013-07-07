package com.remote.remote2d.art;

import org.lwjgl.opengl.GL11;

import com.remote.remote2d.logic.Vector2;

public class Renderer {
	
	public static void drawPoly(Vector2[] vectors, Vector2[] uv, Texture tex, float red, float green, float blue, float alpha)
	{
		tex.bind();
		GL11.glColor4f(red, green, blue, alpha);
		GL11.glBegin(GL11.GL_POLYGON);
		
		for(int x=0;x<vectors.length;x++)
		{
			GL11.glTexCoord2f(uv[x].x,uv[x].y);
			GL11.glVertex2f(vectors[x].x, vectors[x].y);
		}
		
		GL11.glEnd();
		
		GL11.glColor3f(1, 1, 1);
	}
	
	public static void drawPoly(Vector2[] vectors, float red, float green, float blue, float alpha)
	{
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(red, green, blue, alpha);
		GL11.glBegin(GL11.GL_POLYGON);
		
		for(int x=0;x<vectors.length;x++)
			GL11.glVertex2f(vectors[x].x, vectors[x].y);
		
		GL11.glEnd();
		
		GL11.glColor3f(1, 1, 1);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	
	public static void drawRect(Vector2 pos, Vector2 dim, Vector2 uvPos, Vector2 uvDim, Texture tex, float red, float green, float blue, float alpha)
	{
		tex.bind();
		GL11.glColor4f(red, green, blue, alpha);
		GL11.glBegin(GL11.GL_QUADS);
		
		GL11.glTexCoord2f(uvPos.x, uvPos.y);
		GL11.glVertex2f(pos.x, pos.y);
		GL11.glTexCoord2f(uvPos.x+uvDim.x, uvPos.y);
		GL11.glVertex2f(pos.x+dim.x, pos.y);
		GL11.glTexCoord2f(uvPos.x+uvDim.x, uvPos.y+uvDim.y);
		GL11.glVertex2f(pos.x+dim.x, pos.y+dim.y);
		GL11.glTexCoord2f(uvPos.x, uvPos.y+uvDim.y);
		GL11.glVertex2f(pos.x, pos.y+dim.y);
		
		GL11.glEnd();
		
		GL11.glColor3f(1, 1, 1);
	}
	
	public static void drawRect(Vector2 pos, Vector2 dim, float red, float green, float blue, float alpha)
	{
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(red, green, blue, alpha);
		GL11.glBegin(GL11.GL_QUADS);
		
		GL11.glVertex2f(pos.x, pos.y);
		GL11.glVertex2f(pos.x+dim.x, pos.y);
		GL11.glVertex2f(pos.x+dim.x, pos.y+dim.y);
		GL11.glVertex2f(pos.x, pos.y+dim.y);
		
		GL11.glEnd();
		
		GL11.glColor3f(1, 1, 1);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
}
