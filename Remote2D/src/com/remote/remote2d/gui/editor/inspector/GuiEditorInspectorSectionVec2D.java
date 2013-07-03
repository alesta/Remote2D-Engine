package com.remote.remote2d.gui.editor.inspector;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.Remote2D;
import com.remote.remote2d.art.Fonts;
import com.remote.remote2d.gui.GuiTextField;
import com.remote.remote2d.gui.TextLimiter;
import com.remote.remote2d.logic.Vector2D;

public class GuiEditorInspectorSectionVec2D extends GuiEditorInspectorSection {
	
	GuiTextField textField1;
	GuiTextField textField2;
	
	boolean link = false;
	float old1 = 0.0f;
	float old2 = 0.0f;
	

	public GuiEditorInspectorSectionVec2D(String name, Vector2D pos, int width) {
		super(name, pos, width);
		textField1 = new GuiTextField(pos.add(new Vector2D(10,20)), new Vector2D(width/2-10,20), 20);
		textField1.limitToDigits = TextLimiter.LIMIT_TO_INTEGER;
		
		textField2 = new GuiTextField(pos.add(new Vector2D(width/2+10,20)), new Vector2D(width/2-20,20), 20);
		textField2.limitToDigits = TextLimiter.LIMIT_TO_INTEGER;
	}

	@Override
	public int getHeight() {
		return 40;
	}

	@Override
	public Object getData() {
		return new Vector2D(Integer.parseInt(textField1.text),Integer.parseInt(textField2.text));
	}

	@Override
	public void initSection() {
		
	}

	@Override
	public void tick(int i, int j, int k) {
		textField1.tick(i, j, k);
		textField2.tick(i, j, k);
		
		if(i > pos.x+width-20 && i < pos.x+width && j > pos.y && j < pos.y+20 && Remote2D.getInstance().hasMouseBeenPressed())
			link = !link;
		
		if(link)
		{
			if(textField1.hasTyped() && textField1.hasText() && textField2.hasText())
			{
				float new1 = Integer.parseInt(textField1.text);
				int x = (int)((new1*old2)/old1);
				textField2.text = ""+x;
				// old1 = new1
				// old2 = x
				// old1*x = new1*old2
				// x = (new1*old2)/old1
			}
			if(textField2.hasTyped() && textField1.hasText() && textField2.hasText())
			{
				float new2 = Integer.parseInt(textField2.text);
				int x = (int)((new2*old1)/old2);
				textField1.text = ""+x;
				// old1 = x
				// old2 = new2
				// old2*x = new2*old1
				// x = (new2*old1)/old2
			}
		}else
		{
			if(textField1.hasText())
				old1 = Integer.parseInt(textField1.text);
			if(textField2.hasText())
				old2 = Integer.parseInt(textField2.text);
		}
	}

	@Override
	public void render(float interpolation) {
		Fonts.get("Arial").drawString(name, pos.x, pos.y, 20, isComplete() ? 0xffffff : 0xff7777);
		textField1.render(interpolation);
		textField2.render(interpolation);
		
		GL11.glBegin(GL11.GL_LINE_STRIP);
		GL11.glVertex2f(pos.x+width-20, pos.y);
		GL11.glVertex2f(pos.x+width, pos.y);
		GL11.glVertex2f(pos.x+width, pos.y+20);
		GL11.glVertex2f(pos.x+width-20, pos.y+20);
		GL11.glVertex2f(pos.x+width-20, pos.y);
		GL11.glEnd();
		
		if(link)
		{
			GL11.glBegin(GL11.GL_LINE_STRIP);
				GL11.glVertex2f(pos.x+width-20, pos.y);
				GL11.glVertex2f(pos.x+width, pos.y+20);
				
				GL11.glVertex2f(pos.x+width-20, pos.y+20);
				GL11.glVertex2f(pos.x+width, pos.y);
			GL11.glEnd();
		}
	}
	
	@Override
	public void setData(Object o) {
		if(o instanceof Vector2D)
		{
			textField1.text = ((Vector2D)o).x+"";
			textField2.text = ((Vector2D)o).y+"";
		}
	}
	
	@Override
	public void deselect() {
		textField1.deselect();
		textField2.deselect();
	}
	
	@Override
	public boolean isSelected() {
		return textField1.isSelected() || textField2.isSelected();
	}

	@Override
	public boolean isComplete() {
		return !textField1.text.trim().equals("") && !textField2.text.trim().equals("");
	}
	
	@Override
	public boolean hasFieldBeenChanged() {
		return (textField1.isSelected() || textField2.isSelected()) && isComplete() && Remote2D.getInstance().getIntegerKeyboardList().contains(Keyboard.KEY_RETURN);
	}

}
