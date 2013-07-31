package com.remote.remote2d.gui.editor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.Remote2D;
import com.remote.remote2d.art.Renderer;
import com.remote.remote2d.entity.Entity;
import com.remote.remote2d.entity.component.Component;
import com.remote.remote2d.gui.Gui;
import com.remote.remote2d.gui.GuiInGame;
import com.remote.remote2d.gui.KeyShortcut;
import com.remote.remote2d.io.R2DFileManager;
import com.remote.remote2d.logic.Vector2;
import com.remote.remote2d.world.Map;

public class GuiEditorTopMenu extends Gui {
	
	public ArrayList<GuiEditorTopMenuSection> sections;
	public GuiEditor editor;
	
	private int height = 20;
	
	public GuiEditorTopMenu(GuiEditor editor)
	{		
		this.editor = editor;
		
		sections = new ArrayList<GuiEditorTopMenuSection>();
				
		int currentX = 0;
				
		String[] fileContents = {"New Map","Open Map","Save Map"};
		GuiEditorTopMenuSection file = new GuiEditorTopMenuSection(currentX, 0, height, fileContents, "File", this);
		
		file.keyCombos[0] = new KeyShortcut(new int[]{Keyboard.KEY_N});
		file.keyCombos[1] = new KeyShortcut(new int[]{Keyboard.KEY_O});
		file.keyCombos[2] = new KeyShortcut(new int[]{Keyboard.KEY_S});
		
		file.reloadSubWidth();
		
		if(file.getEnabled())
			currentX += file.width;
		
		String[] editContents = {"Create Animated Sprite", "Optimize Spritesheet"};
		GuiEditorTopMenuSection edit = new GuiEditorTopMenuSection(currentX, 0, height, editContents, "Edit", this);
		if(edit.getEnabled())
			currentX += edit.width;
		
		edit.keyCombos[0] = new KeyShortcut(new int[]{Keyboard.KEY_S}).setUseShift(true);
		edit.keyCombos[1] = new KeyShortcut(new int[]{Keyboard.KEY_O}).setUseShift(true);
		
		edit.reloadSubWidth();
		
		String[] worldContents = {"Insert Entity", "Delete Entity", "Toggle Grid", "Increase Grid Size", "Decrease Grid Size", "Zoom In", "Zoom Out", "Revert Zoom", "Run Map"};
		GuiEditorTopMenuSection world = new GuiEditorTopMenuSection(currentX, 0, height, worldContents, "World", this);
		if(world.getEnabled())
			currentX += world.width;
		
		world.keyCombos[0] = new KeyShortcut(new int[]{Keyboard.KEY_E});
		world.keyCombos[1] = new KeyShortcut(new int[]{Keyboard.KEY_DELETE}).setMetaOrControl(false);
		world.keyCombos[2] = new KeyShortcut(new int[]{Keyboard.KEY_G}).setMetaOrControl(false);
		world.keyCombos[3] = new KeyShortcut(new int[]{Keyboard.KEY_RBRACKET}).setMetaOrControl(false);
		world.keyCombos[4] = new KeyShortcut(new int[]{Keyboard.KEY_LBRACKET}).setMetaOrControl(false);
		world.keyCombos[5] = new KeyShortcut(new int[]{Keyboard.KEY_EQUALS}).setMetaOrControl(false);
		world.keyCombos[6] = new KeyShortcut(new int[]{Keyboard.KEY_MINUS}).setMetaOrControl(false);
		world.keyCombos[7] = new KeyShortcut(new int[]{Keyboard.KEY_0}).setMetaOrControl(false);
		world.keyCombos[8] = new KeyShortcut(new int[]{Keyboard.KEY_R});
		
		world.reloadSubWidth();
		
		Iterator<Entry<String,Component>> iterator = Remote2D.getInstance().componentList.getIterator();
		ArrayList<String> contents = new ArrayList<String>();
		while(iterator.hasNext())
			contents.add(iterator.next().getKey());
		
		String[] componentContents = new String[contents.size()];
		componentContents = contents.toArray(componentContents);
		GuiEditorTopMenuSection component = new GuiEditorTopMenuSection(currentX, 0, height, componentContents, "Component", this);
		if(component.getEnabled())
			currentX += component.width;
		
		String[] windowContents = {"Toggle Fullscreen","Exit"};
		GuiEditorTopMenuSection window = new GuiEditorTopMenuSection(currentX, 0, height, windowContents, "Window", this);
		if(window.getEnabled())
			currentX += window.width;
		window.keyCombos[0] = new KeyShortcut(new int[]{Keyboard.KEY_F});
		
		window.reloadSubWidth();
		
		String[] devContents = {"Reinitialize Editor", "View Art Asset", "Fancypants Collider Test","Normal Collider Test", "1D Perlin Noise", "2D Perlin Noise"};
		GuiEditorTopMenuSection dev = new GuiEditorTopMenuSection(currentX, 0, height, devContents, "Developer", this);
		if(dev.getEnabled())
			currentX += dev.width;
		
		sections.add(file);
		sections.add(world);
		sections.add(edit);
		sections.add(component);
		sections.add(window);
		sections.add(dev);
	}
	
	public void initSections()
	{
		int currentX = 0;
		for(int x=0;x<sections.size();x++)
		{
			if(sections.get(x).getEnabled())
			{
				sections.get(x).x = currentX;
				currentX += sections.get(x).width;
			}
		}
		
	}
	
	public GuiEditorTopMenuSection getSectionWithName(String s)
	{
		for(int x=0;x<sections.size();x++)
		{
			if(sections.get(x).title.equals(s))
				return sections.get(x);
		}
		return null;
	}
	
	public boolean isMenuHovered(int i, int j)
	{
		boolean z = false;
		for(int x=0;x<sections.size();x++)
		{
			if(sections.get(x).isSelected)
				z = true;
		}
		return (i>0 && j>0 && i<Remote2D.getInstance().displayHandler.width && j<height) || z;
	}

	@Override
	public void render(float interpolation) {
		Renderer.drawRect(new Vector2(0,0), new Vector2(Remote2D.getInstance().displayHandler.width,height), 1, 0.2f, 0.2f, 1);
		
		for(int x=0;x<sections.size();x++)
		{
			sections.get(x).render(interpolation);
		}
	}

	@Override
	public void tick(int i, int j, int k) {
		getSectionWithName("World").setEnabled(editor.getMap() != null);
		getSectionWithName("Component").setEnabled(editor.getMap() != null);
		
		if(isMenuHovered(i,j))
			editor.disableElementPlace();
		
		for(int x=0;x<sections.size();x++)
		{
			sections.get(x).tick(i,j,k);
		}
		
		String secTitle = "NONE";
		String secSubTitle = "NONE";
		
		for(int x=0;x<sections.size();x++)
		{
			int selectedSubSec = sections.get(x).popSelectedBox();
			if(selectedSubSec != -1)
			{
				secTitle = sections.get(x).title;
				secSubTitle = sections.get(x).values[selectedSubSec];
				Log.info("Selected box: " + secTitle + ">" + secSubTitle+" ");
			}
		}
		
		if(secTitle.equalsIgnoreCase("File"))
		{
			if(secSubTitle.equalsIgnoreCase("New Map"))
			{
				Log.info("Opening new Document!");
				editor.setMap(new Map());
			} else if(secSubTitle.equalsIgnoreCase("Open Map"))
			{
				Log.info("Opening!");
				Map newMap = new Map();
				R2DFileManager mapManager = new R2DFileManager("/res/maps/map.r2d", newMap);
				mapManager.read();
				editor.setMap(newMap);
			} else if(secSubTitle.equalsIgnoreCase("Save Map"))
			{
				Map map = editor.getMap();
				R2DFileManager mapManager = new R2DFileManager("/res/maps/map.r2d", map);
				mapManager.write();
				editor.setMap(map);
			}
		} else if(secTitle.equalsIgnoreCase("Edit"))
		{
			if(secSubTitle.equalsIgnoreCase("Create Animated Sprite"))
			{
				Remote2D.getInstance().guiList.push(new GuiCreateSpriteSheet());
			} else if(secSubTitle.equalsIgnoreCase("Optimize Spritesheet"))
			{
				Remote2D.getInstance().guiList.push(new GuiOptimizeSpriteSheet());
			}
		} else if(secTitle.equalsIgnoreCase("Window"))
		{
			if(secSubTitle.equalsIgnoreCase("Toggle Fullscreen"))
			{
				Remote2D.getInstance().displayHandler.setDisplayMode(Display.getDesktopDisplayMode().getWidth(),
						Display.getDesktopDisplayMode().getHeight(), !Display.isFullscreen(), 
						false);
			}else if(secSubTitle.equalsIgnoreCase("Exit"))
			{
				Remote2D.getInstance().guiList.pop();
			}
		} else if(secTitle.equalsIgnoreCase("Developer"))
		{
			if(secSubTitle.equals("Reinitialize Editor"))
			{
				Remote2D.getInstance().guiList.pop();
				Remote2D.getInstance().guiList.push(new GuiEditor());
			} else if(secSubTitle.equals("View Art Asset"))
			{
				editor.attemptToPutWindowOnTop(new GuiWindowViewArtAsset(editor, new Vector2(200,200), editor.getWindowBounds()));
			} else if(secSubTitle.equalsIgnoreCase("Fancypants Collider Test"))
			{
				editor.attemptToPutWindowOnTop(new GuiWindowCollisionTest(editor, new Vector2(300,300), editor.getWindowBounds()));
			} else if(secSubTitle.equalsIgnoreCase("Normal Collider Test"))
			{
				editor.attemptToPutWindowOnTop(new GuiWindowGeneralColliderTest(editor, new Vector2(300,300), editor.getWindowBounds()));
			} else if(secSubTitle.equalsIgnoreCase("1D Perlin Noise"))
			{
				editor.attemptToPutWindowOnTop(new GuiWindowPerlin1D(editor, new Vector2(10,30), editor.getWindowBounds()));
			} else if(secSubTitle.equalsIgnoreCase("2D Perlin Noise"))
			{
				editor.attemptToPutWindowOnTop(new GuiWindowPerlin2D(editor, new Vector2(20,30), editor.getWindowBounds()));
			}
		} else if(secTitle.equalsIgnoreCase("World"))
		{
			if(secSubTitle.equalsIgnoreCase("Insert Entity"))
			{
				editor.setActiveEntity(new Entity());
			} else if(secSubTitle.equalsIgnoreCase("Run Map"))
			{
				Remote2D.getInstance().map = editor.getMap().copy();
				Remote2D.getInstance().guiList.push(new GuiInGame());
			} else if(secSubTitle.equalsIgnoreCase("Delete Entity"))
			{
				editor.deleteSelectedEntity();
			} else if(secSubTitle.equalsIgnoreCase("Toggle Grid"))
			{
				editor.grid = !editor.grid;
			} else if(secSubTitle.equalsIgnoreCase("Increase Grid Size"))
			{
				editor.getMap().gridSize *= 2;
			} else if(secSubTitle.equalsIgnoreCase("Decrease Grid Size"))
			{
				if(editor.getMap().gridSize > 1)
					editor.getMap().gridSize /= 2;
			} else if(secSubTitle.equalsIgnoreCase("Zoom In"))
			{
				if(editor.getMap().camera.additionalScale < 16)
					editor.getMap().camera.additionalScale *= 2;
			} else if(secSubTitle.equalsIgnoreCase("Zoom Out"))
			{
				if(editor.getMap().camera.additionalScale > 0.25)
					editor.getMap().camera.additionalScale /= 2;
			} else if(secSubTitle.equalsIgnoreCase("Revert Zoom"))
				editor.getMap().camera.additionalScale = 1;
		} else if(secTitle.equalsIgnoreCase("Component"))
		{
			if(editor.getSelectedEntity() != null)
			{
				editor.getSelectedEntity().addComponent(Remote2D.getInstance().componentList.getComponent(secSubTitle));
				editor.getInspector().setCurrentEntity(editor.getSelectedEntity());
			}
		}
	}
	
}
