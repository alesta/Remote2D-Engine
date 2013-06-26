package com.remote.remote2d.art;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.Remote2D;
import com.remote.remote2d.io.R2DFileManager;
import com.remote.remote2d.io.R2DFileSaver;
import com.remote.remote2d.io.R2DType;
import com.remote.remote2d.io.R2DTypeCollection;
import com.remote.remote2d.io.R2DTypeString;

/**
 * Loads textures for you, and also caches them in case a texture is used a lot.
 * @author Flafla2
 *
 */
public class ArtLoader {
	
	private ArrayList<Texture> artList;
	private HashMap<String,Animation> animList;
	
	public ArtLoader()
	{
		artList = new ArrayList<Texture>();
		animList = new HashMap<String,Animation>();
	}
	
	public Texture getTexture(String s)
	{
		return getTexture(s,false,false);
	}
	
	public Texture getTexture(String s, boolean linearScaling, boolean repeat)
	{
		if(!textureExists(s))
			return null;
		
		for(int x=0;x<artList.size();x++)
		{
			if(artList.get(x).textureLocation.equals(s) && artList.get(x).repeat == repeat && artList.get(x).linearScaling == linearScaling)
			{
				return artList.get(x);
			}
		}
		
		Log.debug("New texture added to list: "+s);
		artList.add(new Texture(s,linearScaling,repeat));
		return artList.get(artList.size()-1);
	}
	
	public Animation getAnimation(String s)
	{
		if(!R2DExists(s))
			return null;
		if(!animList.containsKey(s))
		{
			Log.debug("New animation added to list: "+s);
			Animation animation = new Animation(s);
			animList.put(s, animation);
		}
		return animList.get(s);
	}
	
	public void reloadArt()
	{
		for(int x=0;x<artList.size();x++)
			artList.get(x).removeTexture();
		artList.clear();
		
		Iterator<Entry<String,Animation>> animIterator = animList.entrySet().iterator();
		while(animIterator.hasNext())
		{
			Entry<String,Animation> entry = animIterator.next();
			animList.get(entry.getKey()).reload();
		}
	}
	
	public boolean textureExists(String s)
	{
		String x = Remote2D.getJarPath().getPath().toString()+s;
		File f = new File(x);

		if(f.exists() && f.isFile() && f.getName().endsWith(".png"))
			return true;
		else
			return false;
	}
	
	public boolean R2DExists(String s)
	{
		String x = Remote2D.getJarPath().getPath().toString()+s;
		File f = new File(x);

		if(f.exists() && f.isFile() && f.getName().endsWith(".r2d"))
			return true;
		else
			return false;
	}
	
}
