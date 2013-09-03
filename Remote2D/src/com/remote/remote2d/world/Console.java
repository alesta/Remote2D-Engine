package com.remote.remote2d.world;

import java.util.Stack;

public class Console {
	
	private static Stack<Message> messageStack = new Stack<Message>();
	private static int maxMessages = 100;
	
	public static void pushError(String message)
	{
		pushMessage("ERROR",message,0xff0000);
	}
	
	public static void pushMessage(String message)
	{
		pushMessage("INFO",message,0xffffff);
	}
	
	public static void pushMessage(String prefix, String message, int color)
	{
		pushMessage(new Message(prefix,message,color));
	}
	
	public static void pushMessage(Message message)
	{
		messageStack.push(message);
		updateMessageCount();
		message.print();
	}
	
	public static void pushMessage(Message message, int index)
	{
		messageStack.add(index, message);
		updateMessageCount();
		message.print();
	}
	
	public static Message popMessage()
	{
		return messageStack.pop();
	}
	
	public static Message peekMessage()
	{
		return messageStack.peek();
	}
	
	public static Message getMessage(int index)
	{
		return messageStack.get(index);
	}
	
	public static void setMaxMessages(int maxMessages)
	{
		Console.maxMessages = maxMessages;
		updateMessageCount();
	}
	
	public static int size()
	{
		return messageStack.size();
	}
	
	private static void updateMessageCount()
	{
		while(messageStack.size() > maxMessages)
			messageStack.get(messageStack.size()-1);
	}

}
