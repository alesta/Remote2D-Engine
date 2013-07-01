package com.remote.remote2d.gui.editor.inspector;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.Remote2D;
import com.remote.remote2d.entity.Entity;
import com.remote.remote2d.gui.Gui;
import com.remote.remote2d.gui.GuiButton;
import com.remote.remote2d.gui.GuiMenu;
import com.remote.remote2d.gui.editor.GuiEditor;
import com.remote.remote2d.gui.editor.GuiWindowInsertComponent;
import com.remote.remote2d.logic.Vector2D;

public class GuiEditorInspector extends GuiMenu {
		
	public Entity currentEntity;
	private ArrayList<EditorObjectWizard> components;
	private GuiButton button;
	private GuiEditor editor;
	public int offset = 0;
	public Vector2D pos;
	public Vector2D dim;

	public GuiEditorInspector(Vector2D pos, Vector2D dim, GuiEditor editor)
	{
		super();
		this.pos = pos;
		this.dim = dim;
		this.editor = editor;
		components = new ArrayList<EditorObjectWizard>();
		initGui();
	}
	
	@Override
	public void initGui()
	{
		buttonList.clear();
		if(pos != null && dim != null)
		{
			button = new GuiButton(0,new Vector2D(pos.x+dim.x/2-100,pos.y+dim.y-20),new Vector2D(200,20),"Apply");
			button.setDisabled(currentEntity == null);
			buttonList.add(button);
		}
		
		
	}
	
	@Override
	public void tick(int i, int j, int k, double delta) {
		super.tick(i, j, k, delta);
		
		if(pos.getColliderWithDim(dim).isPointInside(new Vector2D(i,j)))
		{
			if(Remote2D.getInstance().getDeltaWheel() < 0)
				offset += 5;
			if(Remote2D.getInstance().getDeltaWheel() > 0)
				offset -= 5;
			
			if(offset > getTotalComponentHeight()-dim.y+20)
				offset = getTotalComponentHeight()-dim.y+20;
			if(offset < 0)
				offset = 0;
		}
		
		for(int x=0;x<components.size();x++)
		{
			components.get(x).tick(i,j+offset,k,delta);
			
			int changed = components.get(x).hasFieldBeenChanged();
			if(changed != -1)
				components.get(x).setComponentField(changed);
		}
	}
	
	@Override
	public void renderBackground()
	{
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(0,0,0,0.5f);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f(pos.x,pos.y);
			GL11.glVertex2f(pos.x+dim.x,pos.y);
			GL11.glVertex2f(pos.x+dim.x,pos.y+dim.y);
			GL11.glVertex2f(pos.x,pos.y+dim.y);
		GL11.glEnd();
		bindRGB(0xffffff);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	@Override
	public void render() {
		super.render();
		
		for(int x=0;x<components.size();x++)
		{
			GL11.glPushMatrix();
			GL11.glTranslatef(0, -offset, 0);
			GL11.glEnable(GL11.GL_SCISSOR_TEST);
			GL11.glScissor(pos.x, pos.y, dim.x, dim.y-20);
			components.get(x).render();
			GL11.glDisable(GL11.GL_SCISSOR_TEST);
			GL11.glPopMatrix();
		}
	}
	
	public void setCurrentEntity(Entity e)
	{
		this.currentEntity = e;
		components.clear();
		button.setDisabled(currentEntity == null);
		if(currentEntity==null)
			return;
		//Log.debug("Setting Inspector Entity:"+e.name);
		Vector2D currentPos = pos.copy();
		EditorObjectWizard ew = new EditorObjectWizard(currentEntity,currentPos,dim.x);
		components.add(ew);
		currentPos.y += ew.getHeight();
		
		for(int x=0;x<e.getComponents().size();x++)
		{
			EditorObjectWizard cw = new EditorObjectWizard(e.getComponents().get(x),currentPos,dim.x);
			components.add(cw);
			currentPos.y += cw.getHeight();
		}
		
		//Log.debug("Component Count: "+components.size());
	}
	
	public void apply()
	{
		for(int x=0;x<components.size();x++)
		{
			components.get(x).setComponentFields();
		}
		editor.replaceSelectedEntity(currentEntity);
	}
	
	public boolean isTyping()
	{
		for(int x=0;x<components.size();x++)
			if(components.get(x).isFieldSelected())
				return true;
		return false;
	}
	
	public int getTotalComponentHeight()
	{
		int height = 0;
		for(int x=0;x<components.size();x++)
		{
			height += components.get(x).getHeight();
		}
		return height;
	}
	
	@Override
	public void actionPerformed(GuiButton button)
	{
		if(button.id == 0)
		{
			apply();
		} else if(button.id == 1)
		{
			for(int x=0;x<components.size();x++)
				components.get(x).deselectFields();
			editor.attemptToPutWindowOnTop(new GuiWindowInsertComponent(editor, new Vector2D(50,50), editor.getWindowBounds(), currentEntity, editor));
		}
	}

}
