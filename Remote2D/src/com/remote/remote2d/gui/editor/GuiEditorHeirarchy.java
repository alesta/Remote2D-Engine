package com.remote.remote2d.gui.editor;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.Remote2D;
import com.remote.remote2d.art.Fonts;
import com.remote.remote2d.art.Renderer;
import com.remote.remote2d.entity.Entity;
import com.remote.remote2d.gui.Gui;
import com.remote.remote2d.gui.GuiMenu;
import com.remote.remote2d.logic.Vector2;
import com.remote.remote2d.logic.Vector2;

public class GuiEditorHeirarchy extends GuiMenu {
	
	public Vector2 pos = new Vector2(0,20);
	public Vector2 dim;
	private ArrayList<GuiEditorHeirarchySection> sections;
	private GuiEditor editor;
	
	public GuiEditorHeirarchy(Vector2 pos, Vector2 dim, GuiEditor editor)
	{
		this.editor = editor;
		this.pos = pos;
		this.dim = dim;
		
		sections = new ArrayList<GuiEditorHeirarchySection>();
		reloadSections();
	}
	
	@Override
	public void initGui()
	{
		
	}

	@Override
	public void tick(int i, int j, int k) {
		if(editor.getMap() == null)
			return;
		
		if(!isSecDragging() && !Mouse.isButtonDown(0) && !isSecAnimating())
		{
			reloadSections();
		}
		
		for(int x=0;x < sections.size();x++)
			sections.get(x).tick(i,j,k);

	}

	@Override
	public void render(float interpolation) {
		Renderer.drawRect(pos, dim, 0x000000, 0.5f);
		
		if(editor.getMap() == null)
			return;
		
		for(int x=0;x < sections.size();x++)
			sections.get(x).render(interpolation);		
	}
	
	public void updateSelected()
	{
		for(int x=0;x<sections.size();x++)
			if(sections.get(x).selected)
				editor.setSelectedEntity(x);
	}
	
	public void animateSections()
	{
		int selectedIndex = getSelected();
		GuiEditorHeirarchySection sec = null;
		if(selectedIndex != -1)
			sec = sections.get(selectedIndex);
		int newSelectedIndex = -1;
		if(sec != null && pos.getColliderWithDim(dim).isPointInside(new Vector2(Remote2D.getInstance().getMouseCoords())))
			newSelectedIndex = (int)(Remote2D.getInstance().getMouseCoords()[1]-pos.y)/20;
		else if(sec != null)
			newSelectedIndex = selectedIndex;
		
		if(newSelectedIndex < 0)
			newSelectedIndex = 0;
		if(newSelectedIndex >= editor.getMap().getEntityList().size()-1)
			newSelectedIndex = editor.getMap().getEntityList().size()-1;
		
		Entity e = editor.getMap().getEntityList().get(selectedIndex);
		editor.getMap().getEntityList().removeEntityFromList(e);
		editor.getMap().getEntityList().addEntityToList(e,newSelectedIndex);
		
		sections.remove(sec);
		sections.add(newSelectedIndex, sec);
		
		float currentYPos = pos.y;
		for(int x=0;x<sections.size();x++)
		{
			sections.get(x).setKeyframe(new Vector2(pos.x,currentYPos), 100);
			currentYPos += 20;
		}
	}
	
	public void setAllUnselected()
	{
		for(int x=0;x<sections.size();x++)
			sections.get(x).selected = false;
	}
	
	private boolean isSecDragging()
	{
		for(int x=0;x<sections.size();x++)
		{
			if(sections.get(x).isDragging())
				return true;
		}
		return false;
	}
	
	private boolean isSecAnimating()
	{
		for(int x=0;x<sections.size();x++)
		{
			if(sections.get(x).isAnimating())
				return true;
		}
		return false;
	}
	
	private int getSelected()
	{
		for(int x=0;x<sections.size();x++)
		{
			if(sections.get(x).selected)
				return x;
		}
		return -1;
	}
	
	private void reloadSections()
	{
		sections.clear();
		if(editor.getMap() == null)
			return;
		
		float currentYPos = pos.y;
		int selected = editor.getMap().getEntityList().indexOf(editor.getSelectedEntity());
		for(int x=0;x<editor.getMap().getEntityList().size();x++)
		{
			String name = editor.getMap().getEntityList().get(x).name;
			if(name.equals(""))
				name = "Untitled";
			GuiEditorHeirarchySection sec = new GuiEditorHeirarchySection(this,name, new Vector2(pos.x,currentYPos),new Vector2(dim.x,20));
			if(selected == x)
				sec.selected = true;
			sections.add(sec);
			
			currentYPos += 20;
		}
		
	}
	
}
