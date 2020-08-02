package application;
	
import java.util.*;
import java.math.*;

import javafx.application.Platform;
import javafx.application.Application;

import javafx.event.*;
import javafx.stage.*;

import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.image.*;
import javafx.scene.effect.*;
import javafx.scene.text.*;

public class Main extends Application {
	SnakeBody[] snake = new SnakeBody[1002];
	public final double foot=20;
	public final int sceneWidth=800,sceneHeight=500,everyScore=10,tick=80;
	public final double snakeBodySize=18,leftBorder=10,rightBorder=610,upBorder=10,downBorder=450;
	public final int width=(int)((rightBorder-leftBorder)/foot-1),height=(int)((downBorder-upBorder)/foot-1);
	public final String[] stateText = {"Common\nQAQ","Fever\n!!!","GoldBonus\n$$$","Ghost\n~~~"};
	public final Color[] stateColor = {Color.BLUE,Color.RED,Color.YELLOW,Color.WHITE};
	public final int[] bigTimeData = {0,30,40,30},stateTimeData = {0,130,70,160};
	
	public int score=0,initialLen=12,nowLen=initialLen,flagX=1,flagY=0,flagPause=1;
	public int eatCnt=0,bigState=0,state=0;
	public int fever=0,goldBonus=0,ghost=0;
	public double bigTime,stateTime;
	public int[][] map=new int[52][52];
	
	Line line1 = new Line(leftBorder,upBorder,rightBorder,upBorder);
	Line line2 = new Line(leftBorder,upBorder,leftBorder,downBorder);
	Line line3 = new Line(leftBorder,downBorder,rightBorder,downBorder);
	Line line4 = new Line(rightBorder,upBorder,rightBorder,downBorder);
	
	Label scoreBoard = new Label();
	Label scoreBoard1 = new Label();
	Label stateBoard = new Label();
	Label stateBoard1 = new Label();
	Label spBoard = new Label();
	
	Rectangle timeBar = new Rectangle();
	Rectangle timeBar1 = new Rectangle();
	Rectangle stimeBar = new Rectangle();
	Rectangle stimeBar1 = new Rectangle();

	Food food = new Food(15,10);
	BigFood bigFood = new BigFood();
	
	@Override
	public void start(Stage primaryStage) {
		Pane root = new Pane();
		 for (int i=0;i<=width;++i)
			for (int j=0;j<=height;++j)
				map[i][j] = 0;
		 for (int i=0;i<initialLen;++i) {
			int tmpX=15-i,tmpY=height-3;
			snake[i] = new SnakeBody(tmpX,tmpY, stateColor[state]);
			if(i==1) map[tmpX][tmpY] = 2;
			else map[tmpX][tmpY] = 1;
			root.getChildren().add(snake[i]);
		}
		map[15][10] = 3;
		
		scoreBoard.setLayoutX(rightBorder+foot);
		scoreBoard1.setLayoutX(rightBorder+foot);
		scoreBoard.setLayoutY(upBorder+foot);
		scoreBoard1.setLayoutY(upBorder+4*foot);
		scoreBoard.setFont(Font.font("Ink Free", 40));
		scoreBoard1.setTextFill(Color.ORANGE);
		scoreBoard1.setFont(Font.font("Comic Sans MS", 35));
		scoreBoard.setText("Score");
		scoreBoard1.setText(String.valueOf(score));
		stateBoard.setLayoutX(rightBorder+foot);
		stateBoard.setLayoutY(upBorder+9*foot);
		stateBoard.setFont(Font.font("Ink Free", 40));
		stateBoard.setText("State");
		stateBoard1.setLayoutX(rightBorder+foot);
		stateBoard1.setLayoutY(upBorder+12*foot);
		stateBoard1.setFont(Font.font("Comic Sans MS", 35));
		stateBoard1.setTextFill(stateColor[state]);
		stateBoard1.setText(stateText[state]);
		spBoard.setLayoutX(rightBorder+foot);
		spBoard.setLayoutY(upBorder+5.5*foot);
		spBoard.setFont(Font.font("Comic Sans MS", 30));
		
		timeBar.setLayoutX(leftBorder);
		timeBar.setLayoutY(downBorder+foot);
		timeBar1.setLayoutX(leftBorder);
		timeBar1.setLayoutY(downBorder+foot);
		timeBar.setWidth(rightBorder-leftBorder);
		timeBar.setHeight(foot/2);
		timeBar1.setWidth(rightBorder-leftBorder);
		timeBar1.setHeight(foot/2);
		timeBar1.setFill(Color.LIGHTBLUE);
		timeBar.setFill(Color.LIGHTGREEN);
		timeBar.setStroke(Color.BLACK);
		timeBar1.setStroke(Color.BLACK);
		stimeBar.setLayoutX(leftBorder);
		stimeBar.setLayoutY(downBorder+foot*1.5);
		stimeBar1.setLayoutX(leftBorder);
		stimeBar1.setLayoutY(downBorder+foot*1.5);
		stimeBar.setWidth(rightBorder-leftBorder);
		stimeBar.setHeight(foot/2);
		stimeBar1.setWidth(rightBorder-leftBorder);
		stimeBar1.setHeight(foot/2);
		stimeBar1.setFill(Color.LIGHTBLUE);
		stimeBar.setStroke(Color.BLACK);
		stimeBar1.setStroke(Color.BLACK);
		
		root.getChildren().add(scoreBoard);
		root.getChildren().add(scoreBoard1);
		root.getChildren().add(stateBoard);
		root.getChildren().add(stateBoard1);
		root.getChildren().add(food);
		root.getChildren().add(line1);
		root.getChildren().add(line2);
		root.getChildren().add(line3);
		root.getChildren().add(line4);
		
		Scene scene = new Scene(root, sceneWidth, sceneHeight);
		scene.setOnKeyPressed(event -> {
		      int tmpFX=flagX,tmpFY=flagY;
			  switch (event.getCode()) {
			      case W:flagY=-1;flagX=0;break;
			      case S:flagY=1;flagX=0;break;
			      case A:flagX=-1;flagY=0;break;
			      case D:flagX=1;flagY=0;break;
			      case SPACE: {
			    	  flagPause*=-1;
			    	  if(flagPause < 0) {
			    		  stateBoard1.setText("Pause ...");
			    		  stateBoard1.setTextFill(Color.PURPLE);
			    	  }
			    	  else {
			    		  stateBoard1.setTextFill(stateColor[state]);
			    		  stateBoard1.setText(stateText[state]);
			    	  }
			    	  break;
			      }
			  }
			  int tmpX1=snake[0].X+flagX,tmpY1=snake[0].Y+flagY;
		      tmpX1 = (tmpX1 + width+1) % (width+1);
			  tmpY1 = (tmpY1 + height+1) % (height+1);
			  if(map[tmpX1][tmpY1]==2) {
				  flagX=tmpFX;
				  flagY=tmpFY;
			  }
		});
		Timer timer = new Timer();
		TimerTask timerTask = new TimerTask() {
			    @Override
			    public void run() {
					    Platform.runLater(new Runnable() {
					    @Override
					    public void run() {
					    	if(flagPause > 0) {
					    		for (int times=1;times<=fever+1;++times) {
					    			if(state >= 1) {
					    				if(stateTime <= 0) {
					    					fever=0;goldBonus=0;ghost=0;
					    					root.setStyle("-fx-background-color:lightblue");
					    					food.setFill(Color.ORANGE);
					    					root.getChildren().remove(spBoard);
					    					root.getChildren().remove(stimeBar);
					    					root.getChildren().remove(stimeBar1);
					    					state=0;
					    					for (int i=0;i<nowLen;++i) snake[i].setFill(stateColor[state]);
						    				stateBoard1.setTextFill(stateColor[state]);
								    		stateBoard1.setText(stateText[state]);
					    				}
					    				stateTime --;
					    				if(stateTime%7 == 0) {
					    					score += goldBonus;
					    					if(score>0) score -= ghost;
					    				}
					    				scoreBoard1.setText(String.valueOf(score));
					    				stimeBar.setWidth(stateTime/stateTimeData[state]*(rightBorder-leftBorder));
					    			}
					    			if(bigState >= 1) {
					    				if(bigTime <= 0) {
					    					root.getChildren().remove(bigFood);
					    					map[bigFood.lX][bigFood.lY]=map[bigFood.lX][bigFood.rY]=map[bigFood.rX][bigFood.lY]=map[bigFood.rX][bigFood.rY]=0;
						    				bigState = 0;
						    				++eatCnt;
						    				root.getChildren().remove(timeBar);
						    				root.getChildren().remove(timeBar1);
					    				}
					    				bigTime --;
					    				timeBar.setWidth(bigTime/bigTimeData[bigState]*(rightBorder-leftBorder));
					    			}
					    			else if(bigState == 0 && eatCnt%6 == 0 && eatCnt > 0) {
					    				int rndX=(int)Math.floor((Math.random()*(width-1))),rndY=(int)Math.floor((Math.random()*(height-1))),
					    				rndState=(int)Math.floor((Math.random()*3)+1);
									    while(map[rndX][rndY]!=0||map[rndX+1][rndY]!=0||map[rndX][rndY+1]!=0||map[rndX+1][rndY+1]!=0) {
									    	rndX=(int)Math.floor((Math.random()*(width-1)));
									    	rndY=(int)Math.floor((Math.random()*(height-1)));
									    }
									    bigState=rndState;
									    bigTime=bigTimeData[bigState];
									    bigFood.setFill(stateColor[bigState]);
					    				bigFood.moveTo(rndX, rndY);
					    				map[rndX][rndY]=map[rndX+1][rndY]=map[rndX][rndY+1]=map[rndX+1][rndY+1]=4;
					    				root.getChildren().add(bigFood);
					    				root.getChildren().add(timeBar1);
					    				root.getChildren().add(timeBar);
					    			}
					    			int tmpX=snake[nowLen-1].X,tmpY=snake[nowLen-1].Y,
									tmpX1=snake[0].X+flagX,tmpY1=snake[0].Y+flagY;
									tmpX1 = (tmpX1 + width+1) % (width+1);
									tmpY1 = (tmpY1 + height+1) % (height+1);
									map[tmpX][tmpY] = 0;
									for (int i=nowLen-1;i>0;--i)
										snake[i].moveTo(snake[i-1].X, snake[i-1].Y);
									map[snake[2].X][snake[2].Y] = 1;
									map[snake[1].X][snake[1].Y] = 2;
									if(map[tmpX1][tmpY1]==1&&ghost==0) {
										for (int i=0;i<nowLen;++i) snake[i].setFill(Color.GRAY);
										stateBoard1.setText("DIED");
										stateBoard1.setTextFill(Color.GRAY);
										timer.cancel();
									}
									else {
										snake[0].moveTo(tmpX1, tmpY1);
										if(map[tmpX1][tmpY1]==4) {
											root.getChildren().remove(bigFood);
											root.getChildren().remove(timeBar);
											root.getChildren().remove(timeBar1);
						    				map[bigFood.lX][bigFood.lY]=map[bigFood.lX][bigFood.rY]=map[bigFood.rX][bigFood.lY]=map[bigFood.rX][bigFood.rY]=0;
						    				map[tmpX1][tmpY1] = 1;
						    				++nowLen;
						    				++eatCnt;
						    				snake[nowLen-1] = new SnakeBody(tmpX,tmpY, stateColor[state]);
										    map[tmpX][tmpY] = 1;
										    root.getChildren().add(snake[nowLen-1]);
										    if(state!=0) {
										    	fever=0;goldBonus=0;ghost=0;
						    					root.setStyle("-fx-background-color:lightblue");
						    					food.setFill(Color.ORANGE);
						    					root.getChildren().remove(spBoard);
						    					root.getChildren().remove(stimeBar);
						    					root.getChildren().remove(stimeBar1);
										    }
						    				state=bigState;
					    					root.getChildren().add(stimeBar1);
										    root.getChildren().add(stimeBar);
					    					stimeBar.setFill(stateColor[state]);
						    				switch (state) {
						    					case 1:{
						    						fever=1;
						    						spBoard.setTextFill(stateColor[state]);
						    						spBoard.setText("x40");
						    						food.setFill(stateColor[state]);
						    						root.getChildren().add(spBoard);
						    						root.setStyle("-fx-background-color:pink");
						    						break;
						    					}
						    					case 2:{
						    						goldBonus=(int)bigTime;
						    						spBoard.setTextFill(stateColor[state]);
						    						spBoard.setText("+"+String.valueOf(goldBonus));
						    						root.getChildren().add(spBoard);
						    						break;
						    					}
						    					case 3:{
						    						ghost=1;
						    						spBoard.setTextFill(stateColor[state]);
						    						spBoard.setText("-1");
						    						root.getChildren().add(spBoard);
						    						food.setFill(Color.LIGHTBLUE);
						    						break;
						    					}
						    				}
						    				stateTime=stateTimeData[state];
						    				stimeBar.setFill(stateColor[state]);
											for (int i=0;i<nowLen;++i) snake[i].setFill(stateColor[state]);
						    				stateBoard1.setTextFill(stateColor[state]);
								    		stateBoard1.setText(stateText[state]);
						    				bigState = 0;
										}
										if(map[tmpX1][tmpY1]==3) {
										    int rndX=(int)Math.floor((Math.random()*width)),rndY=(int)Math.floor((Math.random()*height));
										    while(map[rndX][rndY]>=1) {
										    	rndX=(int)Math.floor((Math.random()*width));
										    	rndY=(int)Math.floor((Math.random()*height));
										    }
										    	food.moveTo(rndX, rndY);
											    map[rndX][rndY] = 3;
											    if(fever==0)score+=everyScore;
											    else score+=everyScore*40;
											    ++nowLen;
											    ++eatCnt;
											    snake[nowLen-1] = new SnakeBody(tmpX,tmpY, stateColor[state]);
											    map[tmpX][tmpY] = 1;
											    root.getChildren().add(snake[nowLen-1]);
												scoreBoard1.setText(String.valueOf(score));
									    }
									}
					    		}
									
					    	}
					    }
				    });
		        }
		};
		root.setStyle("-fx-background-color:lightblue");
		timer.schedule(timerTask, 0, tick);
		primaryStage.setTitle("Greedy Snake");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}