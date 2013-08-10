package com.remote.remote2d.world;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.Remote2D;
import com.remote.remote2d.art.Renderer;
import com.remote.remote2d.gui.Gui;
import com.remote.remote2d.logic.ColliderBox;
import com.remote.remote2d.logic.Interpolator;
import com.remote.remote2d.logic.Vector2;

public class Camera {
	
	public Vector2 pos;
	private Vector2 oldPos;
	public boolean useMultiples = true;
	public boolean blackBars = true;
	public Vector2 targetResolution;
	public float additionalScale = 1.0f;
	
	public Camera(Vector2 pos, Vector2 targetResolution)
	{
		this.pos = pos.copy();
		oldPos = pos.copy();
		this.targetResolution = targetResolution.copy();
	}
	
	public Camera()
	{
		this(new Vector2(0,0),new Vector2(720,480));
	}
	
	public void tick(int i, int j, int k)
	{
		this.oldPos = pos.copy();
	}
	
	public void renderBefore(float interpolation, boolean editor)
	{
		Vector2 pos = Interpolator.linearInterpolate2f(oldPos, this.pos, interpolation);
		ColliderBox renderArea = getScreenRenderArea();
		float scale = (renderArea.dim.x/targetResolution.x)*additionalScale;
		
		GL11.glPushMatrix();
		GL11.glTranslatef(renderArea.pos.x, renderArea.pos.y,0);
		GL11.glScalef(scale, scale, 0);
		GL11.glTranslatef(-pos.x, -pos.y, 0);		
	}
	
	public float getTrueScale()
	{
		ColliderBox renderArea = getScreenRenderArea();
		float scale = targetResolution.x/renderArea.dim.x;
		return scale*additionalScale;
	}
	
	public void renderAfter(float interpolation, boolean editor)
	{
		GL11.glPopMatrix();
		if(editor || !blackBars)
			return;
		ColliderBox renderArea = getScreenRenderArea();
		float width = (Gui.screenWidth()-renderArea.dim.x)/2;
		float height = (Gui.screenHeight()-renderArea.dim.y)/2;
		
		Renderer.drawRect(new Vector2(0,0), new Vector2(width,Gui.screenHeight()), 0, 0, 0, 1f);
		Renderer.drawRect(new Vector2(Gui.screenWidth()-width,0), new Vector2(width,Gui.screenHeight()), 0, 0, 0, 1f);
		Renderer.drawRect(new Vector2(0,0), new Vector2(Gui.screenWidth(),height), 0, 0, 0, 1f);
		Renderer.drawRect(new Vector2(0,Gui.screenHeight()-height), new Vector2(Gui.screenWidth(),height), 0, 0, 0, 1f);
	}
	
	public Vector2 getTruePos(float interpolation)
	{
		return Interpolator.linearInterpolate2f(oldPos, this.pos, interpolation);
	}
	
	private ColliderBox getScreenRenderArea()
	{
		Vector2 dim;
		float screenRatio = (float)(Gui.screenWidth())/(float)(Gui.screenHeight());
		float aspectRatio = targetResolution.x/targetResolution.y;
		
		if(screenRatio > aspectRatio) // Use height
			dim = new Vector2((float)(Gui.screenHeight())*aspectRatio,Gui.screenHeight());
		else if(screenRatio < aspectRatio) // Use width
			dim = new Vector2(Gui.screenWidth(),(float)(Gui.screenWidth())/aspectRatio);
		else
			dim = new Vector2(Gui.screenWidth(),Gui.screenHeight());
		
		if(useMultiples)
		{
			if(targetResolution.x <= Gui.screenWidth() && targetResolution.y <= Gui.screenHeight())
			{
				dim.x -= dim.x%targetResolution.x;
				dim.y -= dim.y%targetResolution.y;
			} 
//			else
//			{
//				dim = targetResolution.copy();
//				while(dim.x > Gui.screenWidth() || dim.y > Gui.screenHeight())
//				{
//					dim = dim.divide(new Vector2(2,2));
//				}
//			}
		}
		
		Vector2 winPos = new Vector2(Gui.screenWidth()/2-dim.x/2,Gui.screenHeight()/2-dim.y/2);
		
		return winPos.getColliderWithDim(dim);
	}
	
	public ColliderBox getMapRenderArea()
	{
		return pos.getColliderWithDim(targetResolution);
	}

	public Camera copy() {
		return new Camera(pos,targetResolution);
	}
	
	public Matrix4f getMatrix()
	{
		Matrix4f matrix = new Matrix4f();

		ColliderBox renderArea = getScreenRenderArea();
		float scale = (renderArea.dim.x/targetResolution.x)*additionalScale;
		
		Vector3f translate1 = new Vector3f(renderArea.pos.x, renderArea.pos.y,0);
		Vector3f scaleV = new Vector3f(scale, scale, 0);
		Vector3f translate2 = new Vector3f(-pos.x, -pos.y, 0);
		
		matrix.translate(translate1);
		matrix.scale(scaleV);
		matrix.translate(translate2);
		
		return matrix;
	}
	
	public Matrix4f getInverseMatrix()
	{
		Matrix4f matrix = new Matrix4f();

		ColliderBox renderArea = getScreenRenderArea();
		float scale = (renderArea.dim.x/targetResolution.x)*additionalScale;
		
		Vector3f translate1 = new Vector3f(-renderArea.pos.x, -renderArea.pos.y,0);
		Vector3f scaleV = new Vector3f(1/scale, 1/scale, 0);
		Vector3f translate2 = new Vector3f(pos.x, pos.y, 0);
		
		matrix.translate(translate2);
		matrix.scale(scaleV);
		matrix.translate(translate1);
		
		return matrix;
	}
}
