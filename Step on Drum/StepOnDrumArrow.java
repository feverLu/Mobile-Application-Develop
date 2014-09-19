package edu.neu.madcourse.binbinlu.finalproject;



public class StepOnDrumArrow {

	public float speed;
	public float left;
	public float top;
	/*public int width;
	public int height;*/
	public boolean detected;
	public int direction;
	public boolean checked;
	
	public StepOnDrumArrow (float speed, int dir, StepOnDrumGameView gameView ){
		this.speed = speed;
		
		this.left = ((2*dir+1)*gameView.getWidth()/8)-16;//(int)(0.2*gameView.getWidth()/2);
		this.top = gameView.getHeight()-32;//(int)(0.04*gameView.getHeight());
				
		/*this.width= 32;//(int)(0.04*gameView.getWidth());
		this.height = 32;//(int)(0.04*gameView.getHeight());
*/		this.detected = false;
		this.checked = false;
		this.direction =dir;
	}
	
	
}
