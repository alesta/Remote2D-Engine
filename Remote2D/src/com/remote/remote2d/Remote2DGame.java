package com.remote.remote2d;

public abstract class Remote2DGame {
	
	/**
	 * This is called when the Remote2D instance starts up.  Put any Remote2D-based initialization code here.
	 */
	public abstract void initGame();
	
	/**
	 * Supplies the local path to any icons.  These are shown on the window and in the taskbar in windows/linux, and in the dock on mac.
	 * 
	 * For full support, you need at least:
	 * One 16x16 and one 32x32 icon for full windows support
	 * One 32x32 icon for full Linux (and similar platforms) support
	 * One 128x128 icon for full Mac support
	 * @return A string array with icons to use, or null for the default LWJGL icon.
	 */
	public String[] getIconPath()
	{
		return null;
	}
	
}
