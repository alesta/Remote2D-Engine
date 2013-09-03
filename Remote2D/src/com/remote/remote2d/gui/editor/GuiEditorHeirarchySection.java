package com.remote.remote2d.gui.editor;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.Remote2D;
import com.remote.remote2d.art.Fonts;
import com.remote.remote2d.art.Renderer;
import com.remote.remote2d.logic.Interpolator;
import com.remote.remote2d.logic.Vector2;

public class GuiEditorHeirarchySection {
	private Vector2 oldAnimPos;
	private Vector2 newAnimPos;
	private long animStartTime;
	private long animLength;
	private Vector2 oldPos;
	private boolean dragging = false;
	private long lastDragTime = -1;
	private Vector2 dragPos = new Vector2(-1,-1);
	public Vector2 pos;
	public Vector2 dim;
	
	public String content;
	public boolean selected = false;
	public GuiEditorHeirarchy heirarchy;
	
	
	public GuiEditorHeirarchySection(GuiEditorHeirarchy heirarchy, String content, Vector2 pos, Vector2 dim)
	{
		this.heirarchy = heirarchy;
		this.content = content;
		this.pos = pos;
		this.dim = dim;
	}
	

	public void tick(int i, int j, int k) {
		oldPos = pos.copy();
		if(oldAnimPos != null && newAnimPos != null && animStartTime != -1 && animLength != -1)
		{
			float currentTime = System.currentTimeMillis()-animStartTime;
			currentTime /= animLength;
			pos = Interpolator.linearInterpolate(oldAnimPos, newAnimPos, currentTime);
		}
				
	
		if(Remote2D.getInstance().hasMouseBeenPressed() && !dragging)
		{
			if(pos.getColliderWithDim(dim).isPointInside(new Vector2(i,j)))
			{
				heirarchy.setAllUnselected();
				selected = true;
				heirarchy.updateSelected();
				lastDragTime = System.currentTimeMillis();
			}
		} else if(k == 1 && !dragging)
		{
			if(System.currentTimeMillis()-lastDragTime > 100 && lastDragTime != -1 && pos.getColliderWithDim(dim).isPointInside(new Vector2(i,j)))
			{
				dragging = true;
				dragPos = new Vector2(i-pos.x,j-pos.y);
			}
		} else if(k== 1)
		{
			pos = new Vector2(pos.x,j-dragPos.y);
		}
		
		if(k == 0 && dragging)
		{
			lastDragTime = -1;
			dragging = false;
			heirarchy.animateSections();
		}
	}

	public void render(float interpolation) {
		Vector2 truePos = Interpolator.linearInterpolate(oldPos, pos, interpolation);
		if(selected)
			Renderer.drawRect(truePos, dim, 0xffffff, 0.5f);
		Fonts.get("Arial").drawString(content, truePos.x, truePos.y, 20, 0xffffff);
	}
	
	public boolean isDragging()
	{
		return dragging;
	}
	
	public boolean isAnimating()
	{
		return System.currentTimeMillis() - animStartTime <= animLength;
	}
	
	public void setKeyframe(Vector2 newPos, long length)
	{
		oldAnimPos = this.pos.copy();
		newAnimPos = newPos;
		animStartTime = System.currentTimeMillis();
		animLength = length;
	}

}
