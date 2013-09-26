package com.remote.remote2d;

import com.esotericsoftware.minlog.Log;
import com.esotericsoftware.minlog.Log.Logger;
import com.remote.remote2d.world.Console;
import com.remote.remote2d.world.Message;

public class ConsoleLogger extends Logger {
	
	public void log(int level, String category, String message, Throwable ex)
	{
		new Logger().log(level, category, message, ex);
		int color = 0xffffff;
		if(level == Log.LEVEL_ERROR)
		{
			color = 0xff0000;
			if(ex != null)
			{
				StackTraceElement[] trace = ex.getStackTrace();
				for(StackTraceElement e : trace)
					message += "\n"+e.toString();
			}
		}
		else if(level == Log.LEVEL_DEBUG)
			color = 0xbbbbbb;
		else if(level == Log.LEVEL_WARN)
			color = 0xffff44;
		Console.pushMessage(new Message(category,message,color));
	}

}
