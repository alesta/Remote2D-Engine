package com.remote.remote2d.gui.editor.inspector;

import java.awt.Color;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.Remote2DException;
import com.remote.remote2d.art.Animation;
import com.remote.remote2d.art.Fonts;
import com.remote.remote2d.art.Texture;
import com.remote.remote2d.entity.EditorObject;
import com.remote.remote2d.entity.component.Component;
import com.remote.remote2d.gui.Gui;
import com.remote.remote2d.logic.Vector2;

public class EditorObjectWizard {
	
	private EditorObject component;
	private Vector2 pos;
	private int width;
	public ArrayList<GuiEditorInspectorSection> sections;
	
	public EditorObjectWizard(EditorObject c, Vector2 pos, int width)
	{
		component = c;
		this.pos = pos.copy();
		this.width = width;
		
		Field[] fields = component.getClass().getFields();
		sections = new ArrayList<GuiEditorInspectorSection>();
		Vector2 currentPos = pos.add(new Vector2(0,20));
		for(int x=0;x<fields.length;x++)
		{
			if(Modifier.isPublic(fields[x].getModifiers()))
			{
				try {
					Object o = fields[x].get(component);
					if(o instanceof Integer)
					{
						GuiEditorInspectorSectionInt sec = new GuiEditorInspectorSectionInt(fields[x].getName(),currentPos,width);
						if(o != null)
							sec.textField.text = (Integer)o+"";
						sections.add(sec);
						currentPos.y += sec.getHeight();
					} else if(o instanceof String)
					{
						GuiEditorInspectorSectionString sec = new GuiEditorInspectorSectionString(fields[x].getName(),currentPos,width);
						if(o != null)
							sec.textField.text = (String)o+"";
						sections.add(sec);
						currentPos.y += sec.getHeight();
					} else if(o instanceof Float)
					{
						GuiEditorInspectorSectionFloat sec = new GuiEditorInspectorSectionFloat(fields[x].getName(),currentPos,width);
						if(o != null)
							sec.textField.text = (Float)o+"";
						sections.add(sec);
						currentPos.y += sec.getHeight();
					} else if(o instanceof Vector2)
					{
						GuiEditorInspectorSectionVec2D sec = new GuiEditorInspectorSectionVec2D(fields[x].getName(),currentPos,width);
						if(o != null)
						{
							sec.textField1.text = ((Vector2)o).x+"";
							sec.textField2.text = ((Vector2)o).y+"";
						}
						sections.add(sec);
						currentPos.y += sec.getHeight();
					} else if(o instanceof Texture)
					{
						GuiEditorInspectorSectionTexture sec = new GuiEditorInspectorSectionTexture(fields[x].getName(),currentPos,width);
						if(o != null)
							sec.textField.text = ((Texture)o).textureLocation;
						sections.add(sec);
						currentPos.y += sec.getHeight();
					} else if(o instanceof Boolean)
					{
						GuiEditorInspectorSectionBoolean sec = new GuiEditorInspectorSectionBoolean(fields[x].getName(),currentPos,width);
						sec.setData((Boolean)o);
						sections.add(sec);
						currentPos.y += sec.getHeight();
					} else if(o instanceof Animation)
					{
						GuiEditorInspectorSectionAnimation sec = new GuiEditorInspectorSectionAnimation(fields[x].getName(),currentPos,width);
						if(o != null)
							sec.textField.text = ((Animation)o).getPath();
						sections.add(sec);
						currentPos.y += sec.getHeight();
					} else if(o instanceof Color)
					{
						GuiEditorInspectorSectionColor sec = new GuiEditorInspectorSectionColor(fields[x].getName(),currentPos,width);
						sec.setData((Color)o);
						sections.add(sec);
						currentPos.y += sec.getHeight();
					} 
				} catch (Exception e) {}
				
			}
		}
	}
	
	public void setComponentFields()
	{
		for(int x=0;x<sections.size();x++)
		{
			setComponentField(x);
		}
		
		component.apply();
	}
	
	public void setComponentField(int x)
	{
		try {
			Field field = component.getClass().getField(sections.get(x).name);
			field.set(component, sections.get(x).getData());
		} catch (NoSuchFieldException e) {
			Log.error("Field doesn't exist: "+sections.get(x).name);
		} catch (Exception e) {
			throw new Remote2DException(e);
		}
		component.apply();
	}
	
	public void deselectFields()
	{
		for(int x=0;x<sections.size();x++)
			sections.get(x).deselect();
	}
	
	public boolean isFieldSelected()
	{
		for(int x=0;x<sections.size();x++)
			if(sections.get(x).isSelected())
				return true;
		return false;
	}
	
	public int getHeight()
	{
		int height = 20;
		for(int x=0;x<sections.size();x++)
			height += sections.get(x).getHeight();
		return height;
	}
	
	public int hasFieldBeenChanged()
	{
		for(int x=0;x<sections.size();x++)
			if(sections.get(x).hasFieldBeenChanged())
				return x;
		return -1;
	}
	
	public void tick(int i, int j, int k)
	{
		for(int x=0;x<sections.size();x++)
			sections.get(x).tick(i, j, k);
	}
	
	public void render(float interpolation)
	{
		for(int x=0;x<sections.size();x++)
			sections.get(x).render(interpolation);
		
		Gui.bindRGB(0xffffff);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2f(pos.x, pos.y+20);
		GL11.glVertex2f(pos.x+width, pos.y+20);
		
		GL11.glVertex2f(pos.x, pos.y+getHeight());
		GL11.glVertex2f(pos.x+width, pos.y+getHeight());
		GL11.glEnd();
		
		Fonts.get("Arial").drawString(component.getClass().getSimpleName(), pos.x, pos.y, 20, 0xffffff);
	}
	
}
