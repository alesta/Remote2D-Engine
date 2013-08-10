package com.remote.remote2d.gui.editor;

import java.util.ArrayList;
import java.util.Stack;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.Remote2D;
import com.remote.remote2d.art.Fonts;
import com.remote.remote2d.art.Renderer;
import com.remote.remote2d.entity.Entity;
import com.remote.remote2d.gui.GuiMenu;
import com.remote.remote2d.gui.GuiWindow;
import com.remote.remote2d.gui.KeyShortcut;
import com.remote.remote2d.gui.WindowHolder;
import com.remote.remote2d.gui.editor.browser.GuiEditorBrowser;
import com.remote.remote2d.gui.editor.inspector.GuiEditorInspector;
import com.remote.remote2d.gui.editor.operation.GuiWindowConfirmOperation;
import com.remote.remote2d.gui.editor.operation.Operation;
import com.remote.remote2d.logic.ColliderBox;
import com.remote.remote2d.logic.Vector2;
import com.remote.remote2d.world.Camera;
import com.remote.remote2d.world.Map;

public class GuiEditor extends GuiMenu implements WindowHolder {
	
	public Vector2 posOffset = new Vector2(0,0);
	public boolean grid = false;
	
	private GuiEditorTopMenu menu;
	private GuiEditorInspector inspector;
	private GuiEditorPreview preview;
	private GuiEditorBrowser browser;
	private GuiEditorHeirarchy heirarchy;
	private Stack<GuiWindow> windowStack;
	private Stack<Operation> undoList;
	private ArrayList<Operation> redoList;
	
	private Map map;
	private Entity stampEntity;
	private Entity selectedEntity = null;
	private boolean allowEntityPlace = true;
	
	private Vector2 dragPoint = null;
	
	public GuiEditor()
	{
		super();
		backgroundColor = 0xaa0000;
		undoList = new Stack<Operation>();
		redoList = new ArrayList<Operation>();
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
			map.camera.targetResolution = screenDim();
		
		int previewHeight = Math.min(320, (screenHeight()-20)/3);
		if(inspector == null)
		{
			inspector = new GuiEditorInspector(
					new Vector2(0,20),
					new Vector2(300,screenHeight()-previewHeight-20), this);
		} else
			inspector.dim = new Vector2(300,screenHeight()-previewHeight-20);
		
		if(preview == null)
		{
			preview = new GuiEditorPreview(inspector,new Vector2(0,screenHeight()-previewHeight),new Vector2(300,previewHeight));
		} else
		{
			preview.pos.y = screenHeight()-previewHeight;
			preview.dim.y = previewHeight;
		}
		
		inspector.initGui();
		
		if(heirarchy == null)
		{
			heirarchy = new GuiEditorHeirarchy(new Vector2(screenWidth()-300,20),
					new Vector2(300,300), this);
		} else
			heirarchy.pos = new Vector2(screenWidth()-300,20);
		
		heirarchy.initGui();
		
		if(browser == null)
			browser = new GuiEditorBrowser(this,new Vector2(screenWidth()-300,320),new Vector2(300,screenHeight()-320));
		else
		{
			browser.pos.x = screenWidth()-300;
			browser.dim.y = screenHeight()-320;
		}
		browser.resetSections();
		
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
			Fonts.get("Pixel_Arial").drawString("Remote2D Editor", screenWidth()/2-size[0]/2, screenHeight()/2-size[1]/2+10, 40, 0xff9999);
		}
		
		if(map != null)
		{
			if(grid)
				map.drawGrid(interpolation);
			
			map.render(true,interpolation);
		}
		
		if(map != null)
		{
			for(int x = 0;x<map.getEntityList().size();x++)
			{
				Entity entity = map.getEntityList().get(x);
				if(entity.pos.getColliderWithDim(entity.getDim()).isPointInside(getMapMousePos()))
				{
					int fontsize = 10;
					String name = entity.name;
					if(Keyboard.isKeyDown(Keyboard.KEY_U))
						name = entity.getUUID();
					int fontdim[] = Fonts.get("Arial").getStringDim(name, fontsize);
					Vector2 dim = new Vector2(fontdim[0]+20,fontdim[1]);
					Vector2 pos = new Vector2(entity.pos.x+entity.dim.x/2,entity.pos.y+entity.dim.y);
					pos = map.worldToScreenCoords(pos);
					pos.x -= fontdim[0]/2+10;
					Renderer.drawRect(pos, dim, 0x000000, 0.5f);
					Fonts.get("Arial").drawString(name, pos.x+10, pos.y, fontsize, 0xffffff);
				}
			}
		}
		
		if(stampEntity != null)
		{
			map.camera.renderBefore(interpolation, true);
			stampEntity.render(true,interpolation);
			stampEntity.getGeneralCollider().drawCollider(0xffffff);
			map.camera.renderAfter(interpolation, true);
		}
		
		inspector.render(interpolation);
		preview.render(interpolation);
		heirarchy.render(interpolation);
		browser.render(interpolation);
		
		for(int x=0;x<windowStack.size();x++)
		{
			windowStack.get(x).render(interpolation);
		}
		
		menu.render(interpolation);
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
			if(!isWidgetHovered(i,j) || dragPoint != null)
			{
				if(Mouse.isButtonDown(2) || (Mouse.isButtonDown(0) && (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))))
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
				Vector2 mousePos = new Vector2(i,j).divide(new Vector2(map.camera.additionalScale));
				if(deltaWheel > 0 && map.camera.additionalScale < 16)//zoom in, up
				{
					map.camera.additionalScale *= 2;
					map.camera.pos = map.camera.pos.add(new Vector2(i,j).divide(new Vector2(map.camera.additionalScale)));
				} else if(deltaWheel < 0 && map.camera.additionalScale > 0.25)//zoom out, down
				{
					map.camera.pos = map.camera.pos.subtract(new Vector2(i,j).divide(new Vector2(map.camera.additionalScale)));
					map.camera.additionalScale /= 2;
				}
			}
			
			map.tick(i, j, k, true);
			backgroundColor = map.backgroundColor;
		}
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
		KeyShortcut.CAN_EXECUTE = !inspector.isTyping();
		menu.tick(i,j,k);
		if(stampEntity != null)
		{
			stampEntity.updatePos();
			stampEntity.pos = getMapMousePos();
			if(grid)
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
			} else if(map != null && !isWidgetHovered(i,j))
			{
				selectedEntity = map.getTopEntityAtPoint(getMapMousePos());
				inspector.setCurrentEntity(selectedEntity);
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
	
	public Vector2 getMapMousePos()
	{
		Vector2 mouse = new Vector2(Remote2D.getInstance().getMouseCoords());
		return map.screenToWorldCoords(mouse);
	}

	@Override
	public GuiWindow getTopWindow() {
		return windowStack.peek();
	}
	
	private boolean isWidgetHovered(int i, int j)
	{
		return inspector.pos.getColliderWithDim(inspector.dim).isPointInside(new Vector2(i,j))
				|| heirarchy.pos.getColliderWithDim(heirarchy.dim).isPointInside(new Vector2(i,j))
				|| browser.pos.getColliderWithDim(browser.dim).isPointInside(new Vector2(i,j))
				|| preview.pos.getColliderWithDim(preview.dim).isPointInside(new Vector2(i,j));
	}
	
	public ColliderBox getWindowBounds()
	{
		return new ColliderBox(new Vector2(0,20),new Vector2(screenWidth(),screenHeight()-20));
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
		map.camera.targetResolution = screenDim();
		windowStack.clear();
		inspector.setCurrentEntity(null);
	}

	public Entity getSelectedEntity() {
		return selectedEntity;
	}
	
	public void executeOperation(Operation o)
	{
		o.execute();
		redoList.clear();
		if(o.canBeUndone())
			undoList.add(o);
		else
			undoList.clear();
		
		menu.updateUndo();
	}
	
	public void confirmOperation(Operation o)
	{
		attemptToPutWindowOnTop(new GuiWindowConfirmOperation(this, new Vector2(40,40), getWindowBounds(), o));
	}
	
	public void undo()
	{
		if(undoList.size() > 0)
		{
			redoList.add(undoList.peek());
			undoList.pop().undo();
		}
		
		menu.updateUndo();
	}
	
	public void redo()
	{
		if(redoList.size() > 0)
		{
			undoList.push(redoList.get(0));
			redoList.get(0).execute();
			redoList.remove(0);
		}
		
		menu.updateUndo();
	}
	
	public Operation peekUndoStack()
	{
		if(undoList.size() > 0)
			return undoList.peek();
		else
			return null;
	}
	
	public Operation peekRedoStack()
	{
		if(redoList.size() > 0)
			return redoList.get(0);
		else
			return null;
	}
	
}
