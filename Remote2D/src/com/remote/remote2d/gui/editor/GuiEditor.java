package com.remote.remote2d.gui.editor;

import java.util.Stack;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.Remote2D;
import com.remote.remote2d.art.Fonts;
import com.remote.remote2d.entity.Entity;
import com.remote.remote2d.gui.GuiMenu;
import com.remote.remote2d.gui.GuiWindow;
import com.remote.remote2d.gui.WindowHolder;
import com.remote.remote2d.gui.editor.browser.GuiEditorBrowser;
import com.remote.remote2d.gui.editor.inspector.GuiEditorInspector;
import com.remote.remote2d.logic.ColliderBox;
import com.remote.remote2d.logic.Vector2;
import com.remote.remote2d.world.Camera;
import com.remote.remote2d.world.Map;

public class GuiEditor extends GuiMenu implements WindowHolder {
	
	private GuiEditorTopMenu menu;
	private GuiEditorInspector inspector;
	private GuiEditorPreview preview;
	private GuiEditorBrowser browser;
	private GuiEditorHeirarchy heirarchy;
	private Stack<GuiWindow> windowStack;
	
	private Map map;
	private Entity stampEntity;
	private Entity selectedEntity = null;
	private boolean allowEntityPlace = true;
	private boolean gridSnap = false;
	private Vector2 dragPoint = null;
	
	public Vector2 posOffset = new Vector2(0,0);
	
	public GuiEditor()
	{
		super();
		backgroundColor = 0xaa0000;
		menu = new GuiEditorTopMenu(this);
	}
	
	@Override
	public void initGui()
	{
		if(windowStack == null)
		{
			windowStack = new Stack<GuiWindow>();
		}
		
		if(map != null)
			map.camera.targetResolution = new Vector2(Remote2D.getInstance().displayHandler.width, Remote2D.getInstance().displayHandler.height);
		
		if(inspector == null)
		{
			inspector = new GuiEditorInspector(
					new Vector2(0,20),
					new Vector2(300,getHeight()-320), this);
		} else
			inspector.dim = new Vector2(300,getHeight()-320);
		
		if(preview == null)
		{
			preview = new GuiEditorPreview(inspector,new Vector2(0,getHeight()-300),new Vector2(300,300));
		} else
			preview.pos.y = getHeight()-300;
		
		inspector.initGui();
		
		if(heirarchy == null)
		{
			heirarchy = new GuiEditorHeirarchy(new Vector2(getWidth()-300,20),
					new Vector2(300,300), this);
		} else
			heirarchy.pos = new Vector2(getWidth()-300,20);
		
		heirarchy.initGui();
		
		if(browser == null)
			browser = new GuiEditorBrowser(this,new Vector2(getWidth()-300,320),new Vector2(300,getHeight()-320));
		else
		{
			browser.pos.x = getWidth()-300;
			browser.dim.y = getHeight()-320;
		}
		
		if(map != null)
			map.getEntityList().reloadTextures();
		
		for(int x=0;x<windowStack.size();x++)
			windowStack.get(x).initGui();
	}
	
	public void disableElementPlace()
	{
		allowEntityPlace = false;
	}
	
	public GuiEditorInspector getInspector()
	{
		return inspector;
	}
	
	public void replaceSelectedEntity(Entity e)
	{
		int index = map.getEntityList().indexOf(selectedEntity);
		map.getEntityList().set(index, e);
		selectedEntity = map.getEntityList().get(index);
	}
	
	@Override
	public void render(float interpolation)
	{
		super.render(interpolation);
		if(map == null)
		{
			int[] size = Fonts.get("Pixel_Arial").getStringDim("Remote2D Editor", 40);
			Fonts.get("Pixel_Arial").drawString("Remote2D Editor", getWidth()/2-size[0]/2, getHeight()/2-size[1]/2+10, 40, 0xff9999);
		}
		
		if(map != null)
		{
			if(gridSnap)
				map.drawGrid(interpolation);
			
			map.render(true,interpolation);
		}
		GL11.glColor4f(1, 1, 1, 0.5f);
		if(stampEntity != null)
		{
			map.camera.renderBefore(interpolation, true);
			stampEntity.render(true,interpolation);
			stampEntity.getGeneralCollider().drawCollider();
			map.camera.renderAfter(interpolation, true);
		}
		GL11.glColor4f(1, 1, 1, 1);
		
		if(selectedEntity != null)
		{
			inspector.render(interpolation);
			preview.render(interpolation);
		}
		if(map != null)
		{
			
			heirarchy.render(interpolation);
		}
		browser.render(interpolation);
		
		for(int x=0;x<windowStack.size();x++)
		{
			windowStack.get(x).render(interpolation);
		}
		
		menu.render(interpolation);
	}
	
	private void poll(int i, int j)
	{
		if(Remote2D.getInstance().getKeyboardList().contains('g'))
			gridSnap = !gridSnap;
		if(Remote2D.getInstance().getKeyboardList().contains(']'))
			map.gridSize *= 2;
		if(Remote2D.getInstance().getKeyboardList().contains('[') && map.gridSize>1)
			map.gridSize /= 2;
		if(Remote2D.getInstance().getKeyboardList().contains('+') && map.camera.additionalScale < 16)
		{
			map.camera.additionalScale *= 2;
		}
		if(Remote2D.getInstance().getKeyboardList().contains('-') && map.camera.additionalScale > 0.25)
		{
			map.camera.additionalScale /= 2;
		}
		if(Remote2D.getInstance().getKeyboardList().contains('0'))
		{
			map.camera.additionalScale = 1;
		}
	}
	
	@Override
	public void tick(int i, int j, int k)
	{
		super.tick(i, j, k);
		
		if(getMouseInWindow(i,j))
			disableElementPlace();
		
		for(int x=0;x<windowStack.size();x++)
		{
			windowStack.get(x).tick(i,j,k);
		}
		if(map != null)
		{
			if(Mouse.isButtonDown(2))
			{
				if(dragPoint == null)
					dragPoint = new Vector2(i/map.camera.additionalScale+map.camera.pos.x,j/map.camera.additionalScale+map.camera.pos.y);
				else
				{
					map.camera.pos = dragPoint.subtract(new Vector2(i,j).divide(new Vector2(map.camera.additionalScale)));
				}
			} else
				dragPoint = null;
			
			int deltaWheel = Remote2D.getInstance().getDeltaWheel();
			if(deltaWheel > 0 && map.camera.additionalScale < 16)//zoom in, up
			{
				map.camera.additionalScale *= 2;
				map.camera.pos = map.camera.pos.add(new Vector2(i,j).divide(new Vector2(map.camera.additionalScale)));
			} else if(deltaWheel < 0 && map.camera.additionalScale > 0.25)//zoom out, down
			{
				map.camera.pos = map.camera.pos.subtract(new Vector2(i,j).divide(new Vector2(map.camera.additionalScale)));
				map.camera.additionalScale /= 2;
			}
			
			map.tick(i, j, k, true);
			backgroundColor = map.backgroundColor;
		}
		menu.tick(i,j,k);
		if(windowStack.size() == 0)
		{
			inspector.tick(i, j, k);
			heirarchy.tick(i, j, k);
			browser.tick(i, j, k);
		}
		else if(!windowStack.peek().isSelected())
		{
			inspector.tick(i, j, k);
			heirarchy.tick(i, j, k);
			browser.tick(i, j, k);
		}
		
		if(!inspector.isTyping())
		{
			if(windowStack.size() == 0)
				poll(i,j);
			else if(!windowStack.peek().isSelected())
				poll(i,j);
		}
		
		if(stampEntity != null)
		{
			stampEntity.updatePos();
			stampEntity.pos = new Vector2((i+map.camera.pos.x)/map.camera.additionalScale,(j+map.camera.pos.y)/map.camera.additionalScale);
			if(gridSnap)
			{
				stampEntity.pos.x -= stampEntity.pos.x%map.gridSize;
				stampEntity.pos.y -= stampEntity.pos.y%map.gridSize;
			}
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
		{
			stampEntity = null;
		}
		
		if(Remote2D.getInstance().hasMouseBeenPressed() && allowEntityPlace)
		{
			if(stampEntity != null)
			{
				selectedEntity = stampEntity.clone();
				map.getEntityList().addEntityToList(selectedEntity);
				inspector.setCurrentEntity(selectedEntity);
			} else if(map != null && !((
				   inspector.pos.getColliderWithDim(inspector.dim).isPointInside(new Vector2(i,j))
					|| heirarchy.pos.getColliderWithDim(heirarchy.dim).isPointInside(new Vector2(i,j))
					|| browser.pos.getColliderWithDim(browser.dim).isPointInside(new Vector2(i,j))
					|| preview.pos.getColliderWithDim(preview.dim).isPointInside(new Vector2(i,j)))))
			{
				selectedEntity = map.getTopEntityAtPoint(new Vector2( (int)((i+map.camera.pos.x)/map.camera.additionalScale),(int)((j+map.camera.pos.y)/map.camera.additionalScale)));
				inspector.setCurrentEntity(map.getTopEntityAtPoint(new Vector2( (int)((i+map.camera.pos.x)/map.camera.additionalScale),(int)((j+map.camera.pos.y)/map.camera.additionalScale))));
			}
		}
		
		if(map != null)
		{
			boolean up = Keyboard.isKeyDown(Keyboard.KEY_UP);
			boolean down = Keyboard.isKeyDown(Keyboard.KEY_DOWN);
			boolean left = Keyboard.isKeyDown(Keyboard.KEY_LEFT);
			boolean right = Keyboard.isKeyDown(Keyboard.KEY_RIGHT);
			if(up)
				map.camera.pos.y += 5f;
			if(down)
				map.camera.pos.y -= 5f;
			if(left)
				map.camera.pos.x += 5f;
			if(right)
				map.camera.pos.x -= 5f;
		}
		
		allowEntityPlace = true;
	}

	@Override
	public GuiWindow getTopWindow() {
		return windowStack.peek();
	}
	
	public ColliderBox getWindowBounds()
	{
		return new ColliderBox(new Vector2(0,20),new Vector2(getWidth(),getHeight()-20));
	}

	@Override
	public void attemptToPutWindowOnTop(GuiWindow window) {
		if(!(windowStack.contains(window)))
		{
			if(windowStack.size() != 0)
				windowStack.peek().setSelected(false);
			windowStack.push(window);
		}else if(windowStack.peek().equals(window))
		{
			
		}else if(!(windowStack.peek().pos.getColliderWithDim(windowStack.peek().dim).isPointInside(new Vector2(new Vector2(Remote2D.getInstance().getMouseCoords()).getElements()))))
		{
			int x = windowStack.indexOf(window);
			windowStack.peek().setSelected(false);
			if(x != -1)
			{
				windowStack.remove(x);
			}
			windowStack.push(window);
		}
		windowStack.peek().setSelected(true);
	}
	
	public boolean getMouseInWindow(int i, int j)
	{
		for(int x=0;x<windowStack.size();x++)
		{
			if(getMouseInWindow(i,j,windowStack.get(x)))
					return true;
		}
		return false;
	}
	
	public boolean getMouseInWindow(int i, int j, GuiWindow window)
	{
		return window.pos.getColliderWithDim(window.dim.add(new Vector2(0,20))).isPointInside(new Vector2(i,j));
	}
	
	public void setSelectedEntity(int index)
	{
		if(index == -1)
		{
			selectedEntity = null;
			inspector.setCurrentEntity(null);
			return;
		}
		selectedEntity = map.getEntityList().get(index);
		inspector.setCurrentEntity(map.getEntityList().get(index));
	}

	@Override
	public void closeWindow(GuiWindow window) {
		Log.info("Closing Window");
		if(windowStack.contains(window))
		{
			windowStack.remove(window);
		}
		if(windowStack.size() != 0)
			windowStack.peek().setSelected(true);
	}
	
	public Map getMap()
	{
		return map;
	}
	
	public void setActiveEntity(Entity e)
	{
		if(map == null)
			return;
		this.stampEntity = e;
	}

	public void setMap(Map map) {
		if(map != null && !map.equals(map))
			map.camera.pos.y = -20;
		this.map = map;
		map.camera.targetResolution = new Vector2(Remote2D.getInstance().displayHandler.width, Remote2D.getInstance().displayHandler.height);
		inspector.setCurrentEntity(null);
	}

	public Entity getSelectedEntity() {
		return selectedEntity;
	}
	
}
