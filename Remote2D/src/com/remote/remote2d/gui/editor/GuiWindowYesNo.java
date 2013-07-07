package com.remote.remote2d.gui.editor;

import java.util.ArrayList;

import com.remote.remote2d.art.Fonts;
import com.remote.remote2d.gui.GuiButton;
import com.remote.remote2d.gui.GuiWindow;
import com.remote.remote2d.gui.WindowHolder;
import com.remote.remote2d.logic.ColliderBox;
import com.remote.remote2d.logic.Vector2;

public class GuiWindowYesNo extends GuiWindow {
	
	private String[] contents;
	private Answer answer = Answer.UNDEFINED;

	public GuiWindowYesNo(WindowHolder holder, Vector2 pos, ColliderBox allowedBounds, String title, String contents) {
		super(holder, pos, new Vector2(300,100), allowedBounds, title);
		
		ArrayList<String> trueContents = new ArrayList<String>();
		String current = "";
		String[] tokens = contents.split(" ");
		
		for(int x=0;x<tokens.length;x++)
		{
			if(Fonts.get("Arial").getStringDim(current+" "+tokens[x], 20)[0] > dim.x-20)
			{
				trueContents.add(current);
				current = "";
			}
			if(!current.equals(""))
				current += " ";
			current += tokens[x];
		}
		
		this.contents = new String[trueContents.size()];
		this.contents = trueContents.toArray(this.contents);
		
		dim.y = this.contents.length*25+60;
	}
	
	@Override
	public void initGui()
	{
		buttonList.clear();
		buttonList.add(new GuiButton(0,new Vector2(10,dim.y-40),new Vector2(135,40),"Yes"));
		buttonList.add(new GuiButton(1,new Vector2(155,dim.y-40),new Vector2(135,40),"No"));
	}
	
	@Override
	public void actionPerformed(GuiButton button)
	{
		if(button.id == 0)
			answer = Answer.YES;
		else if(button.id == 1)
			answer = Answer.NO;
	}
	
	public Answer getAnswer()
	{
		return answer;
	}

	@Override
	public void renderContents(float interpolation) {
		for(int x=0;x<contents.length;x++)
			Fonts.get("Arial").drawString(contents[x], 10, 10+x*25, 20, 0xffffff);
	}
	
	enum Answer
	{
		YES, NO, UNDEFINED;
	}
}
