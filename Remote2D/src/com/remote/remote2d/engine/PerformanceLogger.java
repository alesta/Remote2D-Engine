package com.remote.remote2d.engine;

import java.util.List;
import java.util.ArrayList;

import com.remote.remote2d.engine.io.R2DTypeCollection;


/**
 * Used to log the performance of engine functions, in milliseconds.
 * @author Adrian
 */
public class PerformanceLogger {

	private static ArrayList<EventNode> data = new ArrayList<EventNode>();
	private static EventNode current = null;
	
	public static int logLimit = 100;
	
	/**
	 * Signals the beginning of an "Event" - in other words adds another layer
	 * of accuracy to the profiler.
	 * @param name Name of the process.
	 */
	public static void pushEvent(String name)
	{
		EventNode node = new EventNode(current);
		node.name = name;
		current.nodes.add(node);
		current = node;
		current.begin();
	}
	
	/**
	 * Signals the end of the most recently pushed Event.  This logs the amount
	 * of time the event took for the profiler.  If the event has no parent event
	 * (in other words it is the first event put onto the stack) then the current
	 * event tree is saved and reset.
	 */
	public static void popEvent()
	{
		if(current == null)
			return;
		current.end();
		if(current.parent == null)
		{
			data.add(current);
			if(data.size() > logLimit)
				data.remove(0);
		}
		current = current.parent;
	}
	
	/**
	 * Caches the current Event tree into memory.  If there are more trees than {@link #logLimit} then we 
	 */
	public static void endLoop()
	{
		while(current != null && current.parent != null)
		{
			current.end();
			current = current.parent;
		}
		
		data.add(current);
		current = null;
	}
	
	public static void saveR2DFile(R2DTypeCollection collection) {
		collection.setInteger("data.size", data.size());
		collection.setInteger("logLimit", logLimit);
		for(int x=0;x<data.size();x++)
		{
			R2DTypeCollection coll = new R2DTypeCollection("data_"+x);
			data.get(x).saveR2DFile(coll);
			collection.setCollection(coll);
		}
	}
	
	public static void loadR2DFile(R2DTypeCollection collection) {
		logLimit = collection.getInteger("logLimit");
		data.clear();
		for(int x=0;x<collection.getInteger("data.size");x++)
		{
			R2DTypeCollection coll = collection.getCollection("data_"+x);
			EventNode node = new EventNode(null);
			node.loadR2DFile(coll);
			data.add(node);
		}
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
		
		public void saveR2DFile(R2DTypeCollection collection) {
			collection.setString("name", name);
			collection.setLong("startTime", startTime);
			collection.setLong("endTime", endTime);
			collection.setInteger("nodes.size", nodes.size());
			for(int x=0;x<nodes.size();x++)
			{
				R2DTypeCollection coll = new R2DTypeCollection("nodes_"+x);
				nodes.get(x).saveR2DFile(coll);
				collection.setCollection(coll);
			}
		}
		
		public void loadR2DFile(R2DTypeCollection collection) {
			nodes.clear();
			this.name = collection.getString("name");
			this.startTime = collection.getLong("startTime");
			this.endTime = collection.getLong("endTime");
			for(int x=0;x<collection.getInteger("nodes.size");x++)
			{
				R2DTypeCollection coll = collection.getCollection("nodes_"+x);
				EventNode node = new EventNode(this);
				node.loadR2DFile(coll);
				nodes.add(node);
			}
			
		}
	}
}
