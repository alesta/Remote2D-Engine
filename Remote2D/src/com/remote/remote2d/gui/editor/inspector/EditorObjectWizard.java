package com.remote.remote2d.gui.editor.inspector;

import java.awt.Color;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.Remote2D;
import com.remote.remote2d.Remote2DException;
import com.remote.remote2d.art.Animation;
import com.remote.remote2d.art.Fonts;
import com.remote.remote2d.art.Renderer;
import com.remote.remote2d.art.Texture;
import com.remote.remote2d.entity.EditorObject;
import com.remote.remote2d.entity.Entity;
import com.remote.remote2d.entity.component.Component;
import com.remote.remote2d.gui.Gui;
import com.remote.remote2d.gui.editor.DraggableObject;
import com.remote.remote2d.gui.editor.GuiEditor;
import com.remote.remote2d.logic.Vector2;

public class EditorObjectWizard {
	
	private EditorObject object;
	private GuiEditor editor;
	private Vector2 pos;
	private int width;
	public ArrayList<GuiEditorInspectorSection> sections;
	
	public EditorObjectWizard(GuiEditor editor, EditorObject c, Vector2 pos, int width)
	{
		object = c;
		this.editor = editor;
		this.pos = pos.copy();
		this.width = width;
		
		Field[] fields = object.getClass().getFields();
		sections = new ArrayList<GuiEditorInspectorSection>();
		Vector2 currentPos = pos.add(new Vector2(0,20));
		for(int x=0;x<fields.length;x++)
		{
			if(Modifier.isPublic(fields[x].getModifiers()))
			{
				try {
					Object o = fields[x].get(object);
					Class type = fields[x].getType();
					
					String name = splitCamelCase(fields[x].getName());
					if(Character.isLowerCase(name.charAt(0)))
						name = Character.toUpperCase(name.charAt(0))+name.substring(1);
					if(type == int.class)
					{
						GuiEditorInspectorSectionInt sec = new GuiEditorInspectorSectionInt(name,currentPos,width);
						if(o != null)
							sec.textField.text = (Integer)o+"";
						sections.add(sec);
						currentPos.y += sec.sectionHeight();
					} else if(type == String.class)
					{
						GuiEditorInspectorSectionString sec = new GuiEditorInspectorSectionString(name,currentPos,width);
						if(o != null)
							sec.textField.text = (String)o+"";
						sections.add(sec);
						currentPos.y += sec.sectionHeight();
					} else if(type == float.class)
					{
						GuiEditorInspectorSectionFloat sec = new GuiEditorInspectorSectionFloat(name,currentPos,width);
						if(o != null)
							sec.textField.text = (Float)o+"";
						sections.add(sec);
						currentPos.y += sec.sectionHeight();
					} else if(type == Vector2.class)
					{
						GuiEditorInspectorSectionVec2D sec = new GuiEditorInspectorSectionVec2D(name,currentPos,width);
						if(o != null)
						{
							sec.textField1.text = ((Vector2)o).x+"";
							sec.textField2.text = ((Vector2)o).y+"";
						}
						sections.add(sec);
						currentPos.y += sec.sectionHeight();
					} else if(type == Texture.class)
					{
						GuiEditorInspectorSectionTexture sec = new GuiEditorInspectorSectionTexture(name,currentPos,width);
						if(o != null)
							sec.textField.text = ((Texture)o).textureLocation;
						sections.add(sec);
						currentPos.y += sec.sectionHeight();
					} else if(type == boolean.class)
					{
						GuiEditorInspectorSectionBoolean sec = new GuiEditorInspectorSectionBoolean(name,currentPos,width);
						sec.setData((Boolean)o);
						sections.add(sec);
						currentPos.y += sec.sectionHeight();
					} else if(type == Animation.class)
					{
						GuiEditorInspectorSectionAnimation sec = new GuiEditorInspectorSectionAnimation(name,currentPos,width);
						if(o != null)
							sec.textField.text = ((Animation)o).getPath();
						sections.add(sec);
						currentPos.y += sec.sectionHeight();
					} else if(type == Color.class)
					{
						GuiEditorInspectorSectionColor sec = new GuiEditorInspectorSectionColor(name,currentPos,width);
						sec.setData((Color)o);
						sections.add(sec);
						currentPos.y += sec.sectionHeight();
					}
				} catch (Exception e) {}
				
			}
		}
	}
	
	public static String splitCamelCase(String s) {
		   return s.replaceAll(
		      String.format("%s|%s|%s",
		         "(?<=[A-Z])(?=[A-Z][a-z])",
		         "(?<=[^A-Z])(?=[A-Z])",
		         "(?<=[A-Za-z])(?=[^A-Za-z])"
		      ),
		      " "
		   );
		}
	
	public void setComponentFields()
	{
		for(int x=0;x<sections.size();x++)
		{
			setComponentField(x);
		}
		
		object.apply();
	}
	
	public void setComponentField(int x)
	{
		try {
			Field field = object.getClass().getField(sections.get(x).name);
			field.set(object, sections.get(x).getData());
		} catch (NoSuchFieldException e) {
			Log.error("Field doesn't exist: "+sections.get(x).name);
		} catch (Exception e) {
			throw new Remote2DException(e);
		}
		object.apply();
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
			height += sections.get(x).sectionHeight();
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
	
	public boolean recieveDraggableObject(DraggableObject drag)
	{
		int[] mouse = Remote2D.getInstance().getMouseCoords();
		Vector2 mouseVec = new Vector2(mouse[0],mouse[1]);
		for(int x=0;x<sections.size();x++)
		{
			Vector2 secDim = new Vector2(width,sections.get(x).sectionHeight());
			boolean inside = sections.get(x).pos.add(new Vector2(0,editor.getInspector().getScrollOffset(0))).getColliderWithDim(secDim).isPointInside(mouseVec);
			if(sections.get(x).acceptsDraggableObject(drag) && inside)
			{
				sections.get(x).acceptDraggableObject(drag);
				return true;
			}
		}
		return false;
	}
	
	public void render(float interpolation)
	{
		for(int x=0;x<sections.size();x++)
		{
			
			Vector2 sizeVec = new Vector2(width,sections.get(x).sectionHeight());
			boolean highlight = sections.get(x).pos.add(new Vector2(0,editor.getInspector().getScrollOffset(0))).getColliderWithDim(sizeVec).isPointInside(new Vector2(Remote2D.getInstance().getMouseCoords()));
			if(editor.dragObject != null && sections.get(x).acceptsDraggableObject(editor.dragObject) && highlight)
			{
				Renderer.drawRect(sections.get(x).pos, new Vector2(width,sections.get(x).sectionHeight()), 0xffffff, 0.5f);
			}
			sections.get(x).render(interpolation);
		}
		
		Renderer.drawLine(new Vector2(pos.x,pos.y+20), new Vector2(pos.x+width,pos.y+20), 0xffffff, 1.0f);
		Renderer.drawLine(new Vector2(pos.x,pos.y+getHeight()), new Vector2(pos.x+width,pos.y+getHeight()), 0xffffff, 1.0f);
		
		Fonts.get("Arial").drawString(object.getClass().getSimpleName(), pos.x, pos.y, 20, 0xffffff);
	}
	
}
