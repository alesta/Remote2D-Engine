package com.remote.remote2d.gui.editor.browser;

import java.io.File;
import java.util.ArrayList;

public class Folder {
	
	private String folderPath;
	
	ArrayList<File> files;
	ArrayList<Folder> folders;
	
	
	public Folder(String path)
	{
		files = new ArrayList<File>();
		folders = new ArrayList<Folder>();
		setPath(path);
	}
	
	public String getPath()
	{
		return folderPath;
	}
	
	public void setPath(String path)
	{
		folderPath = path;
		
		files.clear();
		folders.clear();
		
		File[] subFiles = new File(path).listFiles();
		for(int x=0;x<subFiles.length;x++)
		{
			if(subFiles[x].isFile())
				files.add(subFiles[x]);
			else if(subFiles[x].isDirectory())
				folders.add(new Folder(subFiles[x].getPath()));
		}
	}
	
	public Folder getParent()
	{
		return new Folder(new File(folderPath).getParent());
	}
	
}
