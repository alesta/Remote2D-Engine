package com.remote.remote2d.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.remote.remote2d.logic.Vector2D;

public class R2DTypeVec2D extends R2DType {
	
	public Vector2D data;

	public R2DTypeVec2D(String id) {
		super(id);
		this.data = new Vector2D(0,0);
	}
	
	public R2DTypeVec2D(String id, Vector2D data) {
		this(id);
		this.data = data;
	}

	@Override
	public void read(DataInput d) throws IOException {
		data.x = d.readInt();
		data.y = d.readInt();
	}

	@Override
	public void write(DataOutput d) throws IOException {
		d.writeInt(data.x);
		d.writeInt(data.y);
	}

	@Override
	public byte getId() {
		return 11;
	}
	
	@Override
	public String toString()
	{
		return data.toString();
	}

}
