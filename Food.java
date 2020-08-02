package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

class Food extends Circle {
	public final double foot=20;
	public final double leftBorder=10,rightBorder=610,upBorder=10,downBorder=450;
	public final int width=(int)((rightBorder-leftBorder)/foot-1),height=(int)((downBorder-upBorder)/foot-1);
	public int X,Y;
	
	public Food(int initialX,int initialY) {
		this.setRadius(foot/2.0);
		this.setFill(Color.ORANGE);
		this.setStroke(Color.BLACK);
		moveTo(initialX,initialY);
	}
	
	void moveTo(int toX,int toY) {
		X=toX;
		Y=toY;
		this.setCenterX(leftBorder+toX*foot+foot/2.0);
		this.setCenterY(upBorder+toY*foot+foot/2.0);
	}
}