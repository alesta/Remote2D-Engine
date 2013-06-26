package com.remote.remote2d.art;

import org.lwjgl.opengl.GL11;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.Remote2D;
import com.remote.remote2d.io.R2DFileManager;
import com.remote.remote2d.io.R2DFileSaver;
import com.remote.remote2d.io.R2DTypeCollection;
import com.remote.remote2d.logic.ColliderBox;
import com.remote.remote2d.logic.Vector2D;
import com.remote.remote2d.logic.Vector2DF;

public class Animation implements R2DFileSaver {
	
	private Texture tex;
	private Vector2D startPos;
	private Vector2D spriteDim;
	private Vector2D padding;
	private Vector2D frames;
	private ColliderBox[] framePos;
	private int currentframe = 0;
	private int framelength;
	private long lastFrameTime;
	private String path;
	
	public boolean flippedX = false;
	public boolean flippedY = false;
	
	
	public Animation(Texture tex, Vector2D startPos, Vector2D spriteDim, Vector2D padding, Vector2D frames, int framelength)
	{
		this.tex = tex;
		this.startPos = startPos;
		this.spriteDim = spriteDim;
		this.padding = padding;
		this.frames = frames;
		generateFrames();
		
		this.framelength = framelength;
		lastFrameTime = System.currentTimeMillis();
	}
	
	public Animation(String path)
	{
		new R2DFileManager(path,"Animation",this);
		this.path = path;
	}
	
	@Override
	public void saveR2DFile(R2DTypeCollection collection) {
		collection.setString("texture", tex.textureLocation);
		collection.setInteger("startPos.x", startPos.x);
		collection.setInteger("startPos.y", startPos.y);
		collection.setInteger("spriteDim.x", spriteDim.x);
		collection.setInteger("spriteDim.y", spriteDim.y);
		collection.setInteger("padding.x", padding.x);
		collection.setInteger("padding.y", padding.y);
		collection.setInteger("frames.x", frames.x);
		collection.setInteger("frames.y", frames.y);
		collection.setInteger("framelength", framelength);
	}

	@Override
	public void loadR2DFile(R2DTypeCollection collection) {
		tex = Remote2D.getInstance().artLoader.getTexture(collection.getString("texture"));
		startPos = new Vector2D(collection.getInteger("startPos.x"),collection.getInteger("startPos.y"));
		spriteDim = new Vector2D(collection.getInteger("spriteDim.x"),collection.getInteger("spriteDim.y"));
		padding = new Vector2D(collection.getInteger("padding.x"),collection.getInteger("padding.y"));
		frames = new Vector2D(collection.getInteger("frames.x"),collection.getInteger("frames.y"));
		framelength = collection.getInteger("framelength");
		generateFrames();
	}
	
	public void generateFrames()
	{
		framePos = new ColliderBox[frames.x*frames.y];
		for(int x=0;x<frames.x;x++)
		{
			for(int y=0;y<frames.y;y++)
			{
				Vector2D pos = startPos.add(spriteDim.add(padding).multiply(new Vector2D(x,y)));
				ColliderBox collider = new ColliderBox(pos,spriteDim);
				framePos[frames.x*y+x] = collider;
			}
		}
	}
	
	public String getPath()
	{
		return path;
	}
	
	public void render(Vector2D pos, Vector2D dim)
	{
		if(framePos == null)
		{
			Log.debug("framePos == null!");
			return;
		}
		if(System.currentTimeMillis()-lastFrameTime > framelength)
		{
			currentframe++;
			if(currentframe >= framePos.length)
				currentframe = 0;
			lastFrameTime = System.currentTimeMillis();
		}
		
		ColliderBox collider = framePos[currentframe];
		Vector2DF imgPos = collider.pos.divideSensitive(new Vector2DF(tex.image.getWidth(),tex.image.getHeight()));
		Vector2DF imgDim = collider.dim.divideSensitive(new Vector2DF(tex.image.getWidth(),tex.image.getHeight()));
		
		if(flippedX)
		{
			imgPos.x += imgDim.x;
			imgDim.x *= -1;
		}
		
		if(flippedY)
		{
			imgPos.y += imgDim.y;
			imgDim.y *= -1;
		}
		
		tex.bind();
		GL11.glPushMatrix();
		GL11.glTranslatef(pos.x, pos.y, 0);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(imgPos.x,imgPos.y);
			GL11.glVertex2f(0, 0);
			
			GL11.glTexCoord2f(imgPos.x+imgDim.x,imgPos.y);
			GL11.glVertex2f(dim.x, 0);
			
			GL11.glTexCoord2f(imgPos.x+imgDim.x,imgPos.y+imgDim.y);
			GL11.glVertex2f(dim.x, dim.y);
			
			GL11.glTexCoord2f(imgPos.x,imgPos.y+imgDim.y);
			GL11.glVertex2f(0, dim.y);
		GL11.glEnd();
		GL11.glPopMatrix();
	}
	
	public Vector2D getStartPos() {
		return startPos;
	}

	public void setStartPos(Vector2D startPos) {
		this.startPos = startPos;
		generateFrames();
	}

	public Vector2D getSpriteDim() {
		return spriteDim;
	}

	public void setSpriteDim(Vector2D spriteDim) {
		this.spriteDim = spriteDim;
		generateFrames();
	}

	public Vector2D getPadding() {
		return padding;
	}

	public void setPadding(Vector2D padding) {
		this.padding = padding;
		generateFrames();
	}

	public Vector2D getFrames() {
		return frames;
	}

	public void setFrames(Vector2D frames) {
		this.frames = frames;
		generateFrames();
	}

	public int getFramelength() {
		return framelength;
	}

	public void setFramelength(int framelength) {
		this.framelength = framelength;
		generateFrames();
	}

	public long getLastFrameTime() {
		return lastFrameTime;
	}

	public void setLastFrameTime(long lastFrameTime) {
		this.lastFrameTime = lastFrameTime;
		generateFrames();
	}
	
	public void reload()
	{
		GL11.glDeleteTextures(tex.glId);
		tex = Remote2D.getInstance().artLoader.getTexture(tex.textureLocation);
	}

	public void renderFrames() {
		
		if(framePos == null)
			return;
				
		GL11.glColor3f(0, 1, 0);
		for(int x = 0;x<framePos.length;x++)
		{
			framePos[x].drawCollider();
		}
		GL11.glColor3f(1, 1, 1);
	}
	
}
