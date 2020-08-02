package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class BigFood extends Circle {
	public final double foot=20;
	public final double leftBorder=10,rightBorder=610,upBorder=10,downBorder=450;
	public final int width=(int)((rightBorder-leftBorder)/foot-1),height=(int)((downBorder-upBorder)/foot-1);
	public int lX,lY,rX,rY;
	
	public BigFood() {
		this.setRadius(foot);
		this.setStroke(Color.BLACK);
	}
	
	void moveTo(int toX,int toY) {
		lX=toX;rX=toX+1;
		lY=toY;rY=toY+1;
		this.setCenterX(leftBorder+toX*foot+foot);
		this.setCenterY(upBorder+toY*foot+foot);
	}
}
