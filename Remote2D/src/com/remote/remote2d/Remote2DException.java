package com.remote.remote2d;

import com.esotericsoftware.minlog.Log;

public class Remote2DException extends RuntimeException
{
	
	public Remote2DException(Exception e, String s)
	{
		Log.error("REMOTE2D HANDLED ERROR: "+s);
		Log.error("Here is the stack trace:\n");
		if(e != null)
			e.printStackTrace();
		else
			System.err.println("-----No stack trace provided-----");
	}
	
	public Remote2DException(Exception e)
	{
		this(e, "No description provided");
	}

}
