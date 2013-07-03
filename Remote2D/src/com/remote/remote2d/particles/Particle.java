package com.remote.remote2d.particles;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.gui.Gui;
import com.remote.remote2d.logic.Interpolator;
import com.remote.remote2d.logic.Vector2D;
import com.remote.remote2d.world.Map;

public class Particle {
	
	private Vector2D pos;
	private int dim;
	private Color color;
	private float alpha;
	private long startTime;
	
	public Vector2D velocity;
	public Vector2D environment;
	public Color startColor;
	public Color endColor;
	public int startSize;
	public int endSize;
	public float startAlpha;
	public float endAlpha;
	public long life;
	public long lifeLength;
	
	public Particle(Vector2D startPos, Vector2D velocity, Vector2D environment, Color startColor, Color endColor, int startSize, int endSize, float startAlpha, float endAlpha, long lifeLength)
	{
		this.pos = startPos.copy();
		this.velocity = velocity.copy();
		this.environment = environment.copy();
		this.startColor = new Color(startColor.getRGB());
		this.endColor = new Color(endColor.getRGB());
		this.startSize = startSize;
		this.endSize = endSize;
		this.startAlpha = startAlpha;
		this.endAlpha = endAlpha;
		this.life = 0;
		this.lifeLength = lifeLength;
		
		startTime = System.currentTimeMillis();
	}
	
	public boolean tick(Map map)
	{
		life = System.currentTimeMillis()-startTime;
		
		if(life >= lifeLength)
			return false;
			
		
		velocity = velocity.add(environment);
		Vector2D correction = new Vector2D(0,0);
		if(map != null)
			correction = map.getCorrection(pos.subtract(new Vector2D(dim/2,dim/2)).getColliderWithDim(new Vector2D(dim,dim)), velocity);
		pos = pos.add(velocity.add(correction));
		
		if(!correction.equals(new Vector2D(0,0)))
		{
			velocity = velocity.add(velocity.multiply(correction.normalize()).multiply(new Vector2D(2,2)));
		}
		
		float percentage = ((float)life)/((float)lifeLength);
		float red = (float) Interpolator.linearInterpolate(startColor.getRed(), endColor.getRed(), percentage)/255f;
		float green = (float) Interpolator.linearInterpolate(startColor.getGreen(), endColor.getGreen(), percentage)/255f;
		float blue = (float) Interpolator.linearInterpolate(startColor.getBlue(), endColor.getBlue(), percentage)/255f;
		color = new Color(red,green,blue);
		
		dim = (int) Interpolator.linearInterpolate(startSize, endSize, percentage);
		alpha = (float) Interpolator.linearInterpolate(startAlpha, endAlpha, percentage);
		
		return true;
	}
	
	public void render()
	{
		float[] color = Gui.getRGB(this.color.getRGB());
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(color[0],color[1],color[2],alpha);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f(pos.x-dim/2, pos.y-dim/2);
			GL11.glVertex2f(pos.x+dim/2, pos.y-dim/2);
			GL11.glVertex2f(pos.x+dim/2, pos.y+dim/2);
			GL11.glVertex2f(pos.x-dim/2, pos.y+dim/2);
		GL11.glEnd();
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
}
