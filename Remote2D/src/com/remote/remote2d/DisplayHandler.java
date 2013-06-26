package com.remote.remote2d;

import java.awt.Window;
import java.lang.reflect.Method;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.art.CursorLoader;
import com.remote.remote2d.art.Texture;
import com.remote.remote2d.logic.Vector2D;

public class DisplayHandler {
	
	public int width;
	public int height;
	public boolean fullscreen;
	public boolean borderless;
	
	public DisplayHandler(int width, int height, boolean fullscreen, boolean borderless)
	{
		this.width = borderless ? Display.getDesktopDisplayMode().getWidth() : width;
		this.height = borderless ? Display.getDesktopDisplayMode().getHeight() : height;
		this.fullscreen = fullscreen;
		this.borderless = borderless;
		
		try {
			Display.setDisplayMode(new DisplayMode(width,height));
			Display.setTitle("Remote2D");
			Display.setResizable(Remote2D.RESIZING_ENABLED);
			Display.setFullscreen(fullscreen && !borderless);
			
			Display.create();
		} catch (LWJGLException e) {
			throw new Remote2DException(e,"Failed to create LWJGL Display");
		}
		
		initGL();
	}
	
	public void checkDisplayResolution()
	{
		if(Display.getWidth() != width || Display.getHeight() != height)
		{
			width = Display.getWidth();
			height = Display.getHeight();
			initGL();
			
			for(int x=0;x<Remote2D.getInstance().guiList.size();x++)
				Remote2D.getInstance().guiList.get(x).initGui();
		}
	}
	
	public void initGL()
	{
		Log.info("Initializing OpenGL");
		GL11.glEnable(GL11.GL_TEXTURE_2D);               
        
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);          
        
    	// enable alpha blending
    	GL11.glEnable(GL11.GL_BLEND);
    	GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    
    	GL11.glViewport(0,0,width,height);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, height, 0, 1, -1);//Note, the GL coordinates are flipped!
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		if(Remote2D.getInstance().artLoader != null)
		{
			Remote2D.getInstance().artLoader.reloadArt();
		}
		
		//CursorLoader.setCursor(new Texture("/res/gui/mouse.png"), new Vector2D(22,22));
	}
	
	/**
	 * Set the display mode to be used 
	 * 
	 * @param width The width of the display required
	 * @param height The height of the display required
	 * @param fullscreen True if we want fullscreen mode
	 */
	public void setDisplayMode(int width, int height, boolean fullscreen, boolean borderless) {
		int posX = Display.getX();
		int posY = Display.getY();
		
		if(borderless && fullscreen)
		{
			posX = 0;
			posY = 0;
			width = Display.getDesktopDisplayMode().getWidth();
			height = Display.getDesktopDisplayMode().getWidth();
			fullscreen = false;
		}

	    // return if requested DisplayMode is already set
	    if ((Display.getDisplayMode().getWidth() == width) && 
	        (Display.getDisplayMode().getHeight() == height) && 
	        (Display.isFullscreen() == fullscreen) &&
	        this.borderless == borderless)
	    {
		    return;
	    }

	    try {
	        DisplayMode targetDisplayMode = null;
			
		if (fullscreen) {
		DisplayMode[] modes = Display.getAvailableDisplayModes();
		int freq = 0;
		
		for (int i=0;i<modes.length;i++) {
			DisplayMode current = modes[i];
						
			if ((current.getWidth() == width) && (current.getHeight() == height)) {
				if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
					if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
						targetDisplayMode = current;
						freq = targetDisplayMode.getFrequency();
                }
            }

			    // if we've found a match for bpp and frequence against the 
			    // original display mode then it's probably best to go for this one
			    // since it's most likely compatible with the monitor
			    if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) &&
	                        (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
	                            targetDisplayMode = current;
	                            break;
	                    }
	                }
	            }
	        } else {
	            targetDisplayMode = new DisplayMode(width,height);
	        }

	        if (targetDisplayMode == null) {
	            Log.warn("Failed to find value mode: "+width+"x"+height+" fs="+fullscreen);
	            return;
	        }
	        	        
	        this.width = targetDisplayMode.getWidth();
	        this.height = targetDisplayMode.getHeight();
	        this.fullscreen = fullscreen;
	        	        
	        Display.destroy();
	        System.setProperty("org.lwjgl.opengl.Window.undecorated", borderless ? "true" : "false");
	        Display.setDisplayMode(targetDisplayMode);
	        Display.setFullscreen(fullscreen);
	        Display.setLocation(posX, posY);
	        Display.setVSyncEnabled(fullscreen);
	        Display.create();
	        
	        initGL();
				
	    } catch (LWJGLException e) {
	        Log.warn("Unable to setup mode "+width+"x"+height+" fullscreen="+fullscreen + e);
	    }
	    
	    for(int x=0;x<Remote2D.getInstance().guiList.size();x++)
			Remote2D.getInstance().guiList.get(x).initGui();
	}

}
