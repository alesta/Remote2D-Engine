package com.remote.remote2d;

import java.io.File;
import java.util.ArrayList;
import java.util.Stack;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.art.ArtLoader;
import com.remote.remote2d.art.CursorLoader;
import com.remote.remote2d.entity.InsertableComponentList;
import com.remote.remote2d.entity.InsertableEntityList;
import com.remote.remote2d.entity.component.ComponentColliderBox;
import com.remote.remote2d.gui.GuiMenu;
import com.remote.remote2d.world.Map;

public class Remote2D {
	
	/*----------CORE VARIABLES--------------*/
	public DisplayHandler displayHandler;
	private Remote2DGame game;
	private static final Remote2D instance = new Remote2D();
	public static final boolean RESIZING_ENABLED = true;
	
	/*----------GAMELOOP VARIABLES----------*/
	public boolean running = true;
	private int fpsCounter = 0;
	private int fps = 0;
	private int lastFpsTime = 0;
	
	/*----------GAME VARIABLES--------------*/
	public Map map;
	/**
	 * ALL rendering and ticking, besides REALLY basic stuff goes through here.  Even in-game!
	 */
	public Stack<GuiMenu> guiList;
	public ArtLoader artLoader;	
	public InsertableComponentList componentList;
	
	private boolean mousePressed = false;
	private int deltaWheel = 0;
	private ArrayList<Character> charList;
	private ArrayList<Character> charListLimited;
	private ArrayList<Integer> keyboardList;
	private String allowedChars = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()-_=+\\][';:\"/.,?><`~ ";
	
	public static void startRemote2D(Remote2DGame game) {
		
		Thread thread = new Thread("Remote2D Thread") {
			
			@Override
			public void run()
			{
				instance.start();
			}
			
		};
		
		instance.game = game;
		thread.start();
		
	}
	
	public static Remote2D getInstance()
	{
		return instance;
	}
	
	public void start()
	{
		displayHandler = new DisplayHandler(1024,576,false,false);
		
		initGame();
		
		gameLoop();
		
		displayHandler.setDisplayMode(1024,576,false,false);
		Display.destroy();
		System.exit(0);
		
	}
	
	public void initGame()
	{
		map = new Map();
		guiList = new Stack<GuiMenu>();
		charList = new ArrayList<Character>();
		charListLimited = new ArrayList<Character>();
		keyboardList = new ArrayList<Integer>();
		
		componentList = new InsertableComponentList();
		componentList.addInsertableComponent("Box Collider", new ComponentColliderBox(null));
		
		artLoader = new ArtLoader();
		
		game.initGame();
	}
	
	public void gameLoop()
	{
		long lastLoopTime = System.nanoTime();
		final int TARGET_FPS = 60;
		final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;  
		Log.info("Optimal Frame length:"+OPTIMAL_TIME);
		
		while(running)
		{
			if(Display.isCloseRequested())
				running = false;
			
			// work out how long its been since the last update, this
		    // will be used to calculate how far the entities should
		    // move this loop
		    long now = System.nanoTime();
		    long updateLength = now - lastLoopTime;
		    lastLoopTime = now;
		    double delta = updateLength / ((double)OPTIMAL_TIME);
	
		    // update the frame counter
		    lastFpsTime += updateLength;
		    fpsCounter++;
		      
		    // update our FPS counter if a second has passed since
		    // we last recorded
		    if (lastFpsTime >= 1000000000)
		    {
		       //System.out.println("(FPS: "+fps+")");
		       lastFpsTime = 0;
		       fps = fpsCounter;
		       fpsCounter = 0;
		    }
		      
		    // update the game logic
		    int[] mouse = getMouseCoords();
		    int mDown = getMouseDown();
		    tick(mouse[0],mouse[1],mDown,delta);
		     
		    // draw everyting
		    render();
		    
		    // we want each frame to take 10 milliseconds, to do this
		    // we've recorded when we started the frame. We add 10 milliseconds
		    // to this and then factor in the current time to give 
		    // us our final value to wait for
		    // remember this is in ms, whereas our lastLoopTime etc. vars are in ns.
		   /* try
		    {
		    	Thread.sleep( (lastLoopTime-System.nanoTime() + OPTIMAL_TIME)/1000000 );
		    } catch(Exception e)
		    {
		    	//throw new Remote2DException(e,"Failed to sleep on tick!");
		    }*/
		    Display.sync(TARGET_FPS);
		    Display.update();
		}
		
	}
	
	public int[] getMouseCoords()
	{
		int[] r = {Mouse.getX(),displayHandler.height-Mouse.getY()};
		return r;
	}
	
	public int getMouseDown()
	{
		if(Mouse.isButtonDown(0))
			return 1;
		if(Mouse.isButtonDown(1))
			return 2;
		return 0;
	}
	
	public void tick(int i, int j, int k, double delta)
	{
		updateKeyboardList();
		guiList.peek().tick(i, j, k, delta);
	}
	
	public static File getJarPath()
	{
		return new File(Remote2D.class.getProtectionDomain().getCodeSource().getLocation().getPath());
	}
	
	public boolean hasMouseBeenPressed()
	{
		return mousePressed;
	}
	
	private void updateKeyboardList()
	{
		mousePressed = false;
		while(Mouse.next())
		{
			boolean button = Mouse.getEventButtonState();
			int eventButton = Mouse.getEventButton();
			if(button && eventButton == 0)
			{
				mousePressed = true;
			}
		}
		
		deltaWheel = Mouse.getDWheel();
		
		charList.clear();
		charListLimited.clear();
		keyboardList.clear();
		while(Keyboard.next())
		{
			if(Keyboard.getEventKeyState())
			{
				char c = Keyboard.getEventCharacter();
				if(allowedChars.contains((""+c)) || c == '\b')
					charListLimited.add(c);
				charList.add(c);
				keyboardList.add(Keyboard.getEventKey());
			}
		}
	}
	
	public ArrayList<Character> getKeyboardList()
	{
		return charList;
	}
	
	public ArrayList<Character> getLimitedKeyboardList()
	{
		return charListLimited;
	}
	
	public ArrayList<Integer> getIntegerKeyboardList()
	{
		return keyboardList;
	}
	
	public int getDeltaWheel()
	{
		return deltaWheel;
	}
	
	public void render()
	{
		GL11.glLoadIdentity();
				
		int color = guiList.peek().backgroundColor;
		float r = ((color >> 16) & 0xff)/255f;
		float g = ((color >> 8) & 0xff)/255f;
		float b = (color & 0xff)/255f;
		
		GL11.glClearColor(r, g, b, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		if(RESIZING_ENABLED)
			displayHandler.checkDisplayResolution();
		
		guiList.peek().render();
		
		CursorLoader.render();
	}
	
	public Remote2DGame getGame()
	{
		return game;
	}
	
	public int getFPS()
	{
		return fps;
	}
	
	public void shutDown()
	{
		Log.info("Remote2D Engine Shutting Down");
	}

}
