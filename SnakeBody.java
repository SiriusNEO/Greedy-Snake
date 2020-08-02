package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SnakeBody extends Rectangle {
	public final double foot=20;
	public final double leftBorder=10,rightBorder=610,upBorder=10,downBorder=450;
	public final int width=(int)((rightBorder-leftBorder)/foot-1),height=(int)((downBorder-upBorder)/foot-1);
	public int X,Y;
	
	public SnakeBody(int initialX,int initialY,Color initialColor) {
		this.setHeight(foot);
		this.setWidth(foot);
		this.setFill(initialColor);
		this.setStroke(Color.BLACK);
		moveTo(initialX,initialY);
	}
	
	void moveTo(int toX,int toY) {
		X = toX = (toX + width+1) % (width+1);
		Y = toY = (toY + height+1) % (height+1);
		this.setLayoutX(leftBorder+toX*foot);
		this.setLayoutY(upBorder+toY*foot);
	}
}
