package com.remote.remote2d.gui.editor;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import com.esotericsoftware.minlog.Log;

public class KeyShortcut {
	
	public boolean useControl = false;
	public boolean useMeta = false;
	public boolean useShift = false;
	public int[] shortcuts;
	
	private boolean pressed = false;
	
	public KeyShortcut(int[] shortcuts)
	{
		this.shortcuts = shortcuts;
		setMetaOrControl(true);
	}
	
	@Override
	public String toString()
	{
		String result = "";
		if(useShift)
			result+='\u21e7';
		if(useControl)
			result+="^";
		if(useMeta)
			result += "\u2318";
		for(int x : shortcuts)
		{
			if(x == Keyboard.KEY_DELETE)
				result += "\u2326";
			else if(x == Keyboard.KEY_BACK)
				result += "\u232b";
			else if(x == Keyboard.KEY_LEFT)
				result += "\u21e0";
			else if(x == Keyboard.KEY_UP)
				result += "\u21e1";
			else if(x == Keyboard.KEY_RIGHT)
				result += "\u21e2";
			else if(x == Keyboard.KEY_DOWN)
				result += "\u21e3";
			else
				result += Keyboard.getKeyName(x);
		}
		return result;
	}
	
	public boolean getShortcutActivated()
	{
		boolean oldPressed = pressed;
		pressed = true;
		boolean control = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
		if(useControl && !control)
			pressed = false;
		boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
		if(useShift && !shift)
			pressed = false;
		boolean meta = Keyboard.isKeyDown(Keyboard.KEY_LMETA) || Keyboard.isKeyDown(Keyboard.KEY_RMETA);
		if(useMeta && !meta)
			pressed = false;
		for(int x : shortcuts)
		{
			if(!Keyboard.isKeyDown(x))
				pressed = false;
		}
		
		if(!oldPressed && pressed)
			return true;
		return false;
	}
	
	public KeyShortcut setUseControl(boolean useControl)
	{
		this.useControl = useControl;
		return this;
	}
	
	public KeyShortcut setUseShift(boolean useShift)
	{
		this.useShift = useShift;
		return this;
	}
	
	public KeyShortcut setUseMeta(boolean useMeta)
	{
		this.useMeta = useMeta;
		return this;
	}
	
	public KeyShortcut setMetaOrControl(boolean use)
	{
		useMeta = false;
		useControl = false;
		if(use)
		{
			if(System.getProperty("os.name").startsWith("Mac"))
				useMeta = true;
			else
				useControl = true;
		}
			
		return this;
	}
}
