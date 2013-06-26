package com.remote.remote2d.io;

import java.awt.Color;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class R2DTypeColor extends R2DType {
	
	public Color data;

	public R2DTypeColor(String id) {
		super(id);
	}
	
	public R2DTypeColor(String id, Color data) {
		this(id);
		this.data = data;
	}

	@Override
	public void read(DataInput d) throws IOException {
		data = new Color(d.readInt());
	}

	@Override
	public void write(DataOutput d) throws IOException {
		d.writeInt(data.getRGB());
	}

	@Override
	public byte getId() {
		return 14;
	}
	
	@Override
	public String toString()
	{
		return "0x"+data.getRGB();
	}

}
