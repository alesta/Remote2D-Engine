package com.remote.remote2d.engine;

import java.util.List;
import java.util.ArrayList;

import com.remote.remote2d.engine.io.R2DFileSaver;
import com.remote.remote2d.engine.io.R2DTypeCollection;


/**
 * Used to log the performance of engine functions, in milliseconds.
 * @author Adrian
 *
 */
public class PerformanceLogger implements R2DFileSaver {

	private static ArrayList<EventNode> data = new ArrayList<EventNode>();
	private static EventNode current = null;
	
	public static int logLimit = 100;
	
	/**
	 * Signals the beginning of an "Event" - in other words adds another layer
	 * of accuracy to the profiler.
	 * @param name Name of the process.
	 */
	public void pushEvent(String name)
	{
		EventNode node = new EventNode(current);
		current = node;
		current.begin();
	}
	
	/**
	 * Signals the end of the most recently pushed Event.  This logs the amount
	 * of time the event took for the profiler.  If the event has no parent event
	 * (in other words it is the first event put onto the stack) then the current
	 * event tree is saved and reset.
	 */
	public void popEvent()
	{
		current.end();
		if(current.parent == null)
		{
			data.add(current);
			if(data.size() > logLimit)
				data.remove(0);
		}
		current = current.parent;
	}
	
	@Override
	public void saveR2DFile(R2DTypeCollection collection) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void loadR2DFile(R2DTypeCollection collection) {
		// TODO Auto-generated method stub
		
	}
	
	private static class EventNode
	{
		public String name = "";
		private long startTime = -1;
		private long endTime = -1;
		
		public List<EventNode> nodes;
		public EventNode parent;
		
		public EventNode(EventNode parent)
		{
			this.parent = parent;
			nodes = new ArrayList<EventNode>();
		}
		
		public void begin()
		{
			if(startTime == -1)
				startTime = System.currentTimeMillis();
		}
		
		public void end()
		{
			if(endTime == -1)
				endTime = System.currentTimeMillis();
		}
		
		public long getStartTime()
		{
			return startTime;
		}
		
		public long getEndTime()
		{
			return endTime;
		}
	}
}
