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
		//This value would probably be stored elsewhere
		final double GAME_HERTZ = 30.0;
		//Calculate how many ns each frame should take for our target game hertz.
		final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
		//At the very most we will update the game this many times before a new render.
		//If you're worried about visual hitches more than perfect timing, set this to 1.
		final int MAX_UPDATES_BEFORE_RENDER = 5;
		//We will need the last update time.
		double lastUpdateTime = System.nanoTime();
		//Store the last time we rendered.
		double lastRenderTime = System.nanoTime();
		
		//If we are able to get as high as this FPS, don't render again.
		final double TARGET_FPS = 60;
		final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;
		
		//Simple way of finding FPS.
		int lastSecondTime = (int) (lastUpdateTime / 1000000000);
		
		while (running)
		{
			if(Display.isCloseRequested())
				running = false;
			
			double now = System.nanoTime();
			int updateCount = 0;
			
			 //Do as many game updates as we need to, potentially playing catchup.
			while( now - lastUpdateTime > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BEFORE_RENDER )
			{
				int i = getMouseCoords()[0];
				int j = getMouseCoords()[1];
				tick(i, j, getMouseDown());
				lastUpdateTime += TIME_BETWEEN_UPDATES;
				updateCount++;
			}
					   
			//If for some reason an update takes forever, we don't want to do an insane number of catchups.
			//If you were doing some sort of game that needed to keep EXACT time, you would get rid of this.
			if ( now - lastUpdateTime > TIME_BETWEEN_UPDATES)
			{
				lastUpdateTime = now - TIME_BETWEEN_UPDATES;
			}
			   
			//Render. To do so, we need to calculate interpolation for a smooth render.
			float interpolation = Math.min(1.0f, (float) ((now - lastUpdateTime) / TIME_BETWEEN_UPDATES) );
			render(interpolation);
			fpsCounter++;
			Display.update();
			lastRenderTime = now;
			   
			//Update the frames we got.
			int thisSecond = (int) (lastUpdateTime / 1000000000);
			if (thisSecond > lastSecondTime)
			{
				fps = fpsCounter;
				fpsCounter = 0;
				lastSecondTime = thisSecond;
			}
			   
			//Yield until it has been at least the target time between renders. This saves the CPU from hogging.
			while ( now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS && now - lastUpdateTime < TIME_BETWEEN_UPDATES)
			{
				Thread.yield();
				
				//This stops the app from consuming all your CPU. It makes this slightly less accurate, but is worth it.
				//You can remove this line and it will still work (better), your CPU just climbs on certain OSes.
				//FYI on some OS's this can cause pretty bad stuttering. Scroll down and have a look at different peoples' solutions to this.
				try {Thread.sleep(1);} catch(Exception e) {} 
				
				now = System.nanoTime();
			}
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
	
	public void tick(int i, int j, int k)
	{
		updateKeyboardList();
		guiList.peek().tick(i, j, k);
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
	
	public void render(float interpolation)
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
		
		guiList.peek().render(interpolation);
		
		CursorLoader.render(interpolation);
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
