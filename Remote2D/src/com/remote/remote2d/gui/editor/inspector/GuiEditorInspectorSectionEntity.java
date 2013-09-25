package com.remote.remote2d.gui.editor.inspector;

import com.remote.remote2d.art.Fonts;
import com.remote.remote2d.art.Renderer;
import com.remote.remote2d.entity.Entity;
import com.remote.remote2d.gui.editor.DraggableObject;
import com.remote.remote2d.gui.editor.DraggableObjectEntity;
import com.remote.remote2d.gui.editor.GuiEditor;
import com.remote.remote2d.logic.Vector2;

public class GuiEditorInspectorSectionEntity extends GuiEditorInspectorSection {

	Entity entity = null;
	private boolean changed = false;
	
	public GuiEditorInspectorSectionEntity(String name, GuiEditor inspector, Vector2 pos, int width) {
		super(name, inspector, pos, width);
	}

	@Override
	public int sectionHeight() {
		return 20;
	}

	@Override
	public Object getData() {
		return entity;
	}

	@Override
	public void initSection() {
		
	}

	@Override
	public void tick(int i, int j, int k) {
		
	}

	@Override
	public void render(float interpolation) {
		Fonts.get("Arial").drawString(renderName, pos.x, pos.y, 20, isComplete() ? 0xffffff : 0xff7777);
		final int outerRad = 18;
		final Vector2 circleCenter = new Vector2(sectionHeight()/2,width-outerRad-10);
		Renderer.drawCircleHollow(pos.add(circleCenter), outerRad, 10, 0xffffff, 1);
		Renderer.drawCircleOpaque(pos.add(circleCenter), outerRad/2, 5, 0xffffff, 1);
	}
	
	@Override
	public void setData(Object o) {
		if(o instanceof Entity)
		{
			entity = (Entity)o;
		}
	}
	
	@Override
	public void deselect() {
		
	}
	
	@Override
	public boolean isSelected() {
		return false;
	}

	@Override
	public boolean isComplete() {
		return true;
	}
	
	@Override
	public boolean hasFieldBeenChanged() {
		boolean c = changed;
		changed = false;
		return c;
	}
	
	public boolean acceptsDraggableObject(DraggableObject object)
	{
		if(object instanceof DraggableObjectEntity)
		{
			DraggableObjectEntity fileobj = ((DraggableObjectEntity)object);
			if(fileobj.uuid != null)
			{
				if(!fileobj.uuid.trim().equals(""))
					return true;
			}
		}
		return false;
	}
	
	public void acceptDraggableObject(DraggableObject object)
	{
		if(object instanceof DraggableObjectEntity)
		{
			DraggableObjectEntity fileobj = ((DraggableObjectEntity)object);
			if(fileobj.uuid != null)
			{
				if(!fileobj.uuid.trim().equals(""))
				{
					setData(inspector.getMap().getEntityList().getEntityWithUUID(fileobj.uuid));
				}
			}
		}
	}

}

