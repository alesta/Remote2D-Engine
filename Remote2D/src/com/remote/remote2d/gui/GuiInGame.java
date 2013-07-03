package com.remote.remote2d.gui;

import org.lwjgl.input.Keyboard;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.Remote2D;
import com.remote.remote2d.art.Fonts;

public class GuiInGame extends GuiMenu {
	
	public GuiInGame()
	{
		Log.info("Loading In-Game!");
		backgroundColor = Remote2D.getInstance().map.backgroundColor;
	}
	
	@Override
	public void initGui()
	{
		Remote2D.getInstance().map.getEntityList().reloadTextures();
	}
	
	@Override
	public void render(float interpolation)
	{
		Remote2D.getInstance().map.render(false,interpolation);
		Fonts.get("Arial").drawString("FPS: "+Remote2D.getInstance().getFPS(), 10, 10, 20, 0x000000);
	}
	
	@Override
	public void tick(int i, int j, int k)
	{
		Remote2D.getInstance().map.tick(i,j,k);
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
			Remote2D.getInstance().guiList.pop();
	}
	
}
