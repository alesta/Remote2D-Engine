package com.remote.remote2d.test;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.Remote2D;
import com.remote.remote2d.Remote2DGame;
import com.remote.remote2d.StretchType;
import com.remote.remote2d.art.Animation;
import com.remote.remote2d.io.R2DFileManager;
import com.remote.remote2d.test.entity.ComponentPlayer;
import com.remote.remote2d.test.gui.GuiMainMenu;

public class Remote2DTest extends Remote2DGame {
	
	public static void main(String[] args)
	{
		Remote2D.startRemote2D(new Remote2DTest());
	}

	@Override
	public void initGame() {
		Log.DEBUG();
		Remote2D.getInstance().guiList.push(new GuiMainMenu());
		Remote2D.getInstance().componentList.addInsertableComponent("Player", ComponentPlayer.class);
	}
	
	@Override
	public String[] getIconPath()
	{
		String[] paths = {"/res/gui/icon_16.png","/res/gui/icon_32.png","/res/gui/icon_128.png"};
		return paths;
	}
	
}
