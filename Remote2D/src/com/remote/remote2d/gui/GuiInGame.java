package com.remote.remote2d.gui;

import org.lwjgl.input.Keyboard;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.Remote2D;

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
	public void render()
	{
		Remote2D.getInstance().map.render(false);
	}
	
	@Override
	public void tick(int i, int j, int k, double delta)
	{
		Remote2D.getInstance().map.tick(i,j,k,delta);
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
			Remote2D.getInstance().guiList.pop();
	}
	
}
