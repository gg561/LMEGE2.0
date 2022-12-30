package gui;

import org.joml.Vector2f;

public interface GUIElement {
	
	public Vector2f getScale();
	public Vector2f getPosition();
	public void setScale(Vector2f scale);
	public void setPosition(Vector2f position);
	public GUI getGUI();
	public void setGUI(GUI gui);

}
