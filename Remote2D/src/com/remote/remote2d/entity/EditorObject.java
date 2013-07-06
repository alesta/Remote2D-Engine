package com.remote.remote2d.entity;

import java.awt.Color;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.remote.remote2d.art.Animation;
import com.remote.remote2d.art.Texture;
import com.remote.remote2d.io.R2DFileSaver;
import com.remote.remote2d.io.R2DTypeCollection;
import com.remote.remote2d.logic.Vector2D;

/**
 * If a class implements this interface, then it is able to be edited within the
 * editor.  What this means is that ANY public variables are able to be edited through
 * the inspector pane in the editor.  Note that methods (getters and setters) are not
 * counted in this distinction.
 * @author Flafla2
 *
 */
public abstract class EditorObject implements R2DFileSaver {
	
	/**
	 * Called when this object is changed in the editor.
	 */
	public abstract void apply();
	
	
	public void saveR2DFile(R2DTypeCollection collection)
	{
		Field[] fields = this.getClass().getFields();
		for(int x=0;x<fields.length;x++)
		{
			if(Modifier.isPublic(fields[x].getModifiers()))
			{
				try {
					Object o = fields[x].get(this);
					if(o instanceof Integer)
					{
						collection.setInteger(fields[x].getName(), (Integer)o);
					} else if(o instanceof String)
					{
						collection.setString(fields[x].getName(), (String)o);
					} else if(o instanceof Float)
					{
						collection.setFloat(fields[x].getName(), (Float)o);
					} else if(o instanceof Vector2D)
					{
						collection.setVector2D(fields[x].getName(), (Vector2D)o);
					} else if(o instanceof Texture)
					{
						collection.setTexture(fields[x].getName(), (Texture)o);
					} else if(o instanceof Boolean)
					{
						collection.setBoolean(fields[x].getName(), (Boolean)o);
					} else if(o instanceof Animation)
					{
						collection.setAnimation(fields[x].getName(), (Animation)o);
					} else if(o instanceof Color)
					{
						collection.setColor(fields[x].getName(), (Color)o);
					}
				} catch (Exception e) {}
				
			}
		}
	}
	public void loadR2DFile(R2DTypeCollection collection)
	{
		Field[] fields = this.getClass().getFields();
		for(int x=0;x<fields.length;x++)
		{
			if(Modifier.isPublic(fields[x].getModifiers()))
			{
				try {
					Field field = fields[x];
					if(field.getType() == int.class)
						field.set(this, collection.getInteger(field.getName()));
					else if(field.getType() == String.class)
						field.set(this, collection.getString(field.getName()));
					else if(field.getType() == float.class)
						field.set(this, collection.getFloat(field.getName()));
					else if(field.getType() == long.class)
						field.set(this, collection.getLong(field.getName()));
					else if(field.getType() == Texture.class)
						field.set(this, collection.getTexture(field.getName()));
					else if(field.getType() == Vector2D.class)
						field.set(this, collection.getVector2D(field.getName()));
					else if(field.getType() == Animation.class)
						field.set(this, collection.getAnimation(field.getName()));
					else if(field.getType() == boolean.class)
						field.set(this, collection.getBoolean(field.getName()));
					else if(field.getType() == Color.class)
						field.set(this, collection.getColor(field.getName()));
					
				} catch (Exception e) {}
			}
		}
	}
	
}
