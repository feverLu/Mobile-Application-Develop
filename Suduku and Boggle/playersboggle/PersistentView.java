package edu.neu.madcourse.binbinlu.playersboggle;
import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.binbinlu.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class PersistentView extends View {
	
	private static final String TAG = "TouchTest";
	private float width;
	private float height;
	private float startGridHeight;
	private int selX;
	private int selY;
	private List<Point> path = new ArrayList<Point>();
	private StringBuilder slideWord = new StringBuilder();
	
	private final PersistentGame persistentGame;
	private final Rect selRect=new Rect();
	private Paint startCircle;
	private Paint pathCircle;
	private Paint endCircle;
	

	@SuppressWarnings("rawtypes")
	public PersistentView (Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		this.persistentGame=(PersistentGame) context;
		setFocusable(true);
		setFocusableInTouchMode(true);
		// TODO Auto-generated constructor stub
		
		
		startCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
		pathCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
		endCircle = new Paint(Paint.ANTI_ALIAS_FLAG);

		startCircle.setColor(getResources().getColor(R.color.red));
		pathCircle.setColor(getResources().getColor(R.color.yellow));
		endCircle.setColor(getResources().getColor(R.color.green));
		
		startCircle.setStyle(Paint.Style.STROKE);
		pathCircle.setStyle(Paint.Style.STROKE);
		endCircle.setStyle(Paint.Style.STROKE);
		
		startCircle.setStrokeWidth(3);
		pathCircle.setStrokeWidth(3);
		endCircle.setStrokeWidth(3);
		
		
		
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		width = w/4f;
		height = h/4f;
		startGridHeight = h/2f;
		getRect(selX, selY, selRect);
		super.onSizeChanged(w, h, oldw, oldh);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		//draw the background
		Paint background = new Paint();
		
		//do not know why name defined in color.xml can not show
		background.setColor(getResources().getColor(
				R.color.boggle_black));
		canvas.drawRect(0, 0, getWidth(), getHeight(), background);
		
		//draw the board
//		Paint dark = new Paint();
//		dark.setColor(getResources().getColor(R.color.boggle_black));
		
		Paint light = new Paint();
		light.setColor(getResources().getColor(R.color.boggle_white));
		
		//draw the minor grid lines
		for (int i=0; i<5; i++) {
			canvas.drawLine(0, i*height, getWidth(), i*height, light);
			canvas.drawLine(i*width, 0, i*width, getHeight(), light);
		}
//		canvas.drawLine(0, getHeight()/16, getWidth(), getHeight()/16, light);
//		canvas.drawLine(0, getHeight()/8, getWidth(), getHeight()/8, light);
//		canvas.drawLine(getWidth()/2, getHeight()/16, getWidth()/2, getHeight()/2, light);
		
		//draw the characters
		//define color and style for characters
		Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
		foreground.setColor(getResources().getColor(
				R.color.boggle_white));
		foreground.setStyle(Style.FILL);
		foreground.setTextSize(height*0.75f);
		foreground.setTextScaleX(width/height);
		foreground.setTextAlign(Paint.Align.CENTER);
		
		//draw the number in the center of the tile
		FontMetrics fm = foreground.getFontMetrics();
		//centering in X
		float x = width/2;
		//centering in Y
		float y = height/2-(fm.ascent + fm.descent)/2;
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++) {
				canvas.drawText(this.persistentGame.getTileString(i,j)+"", 
						i * width + x, j * height + y, foreground);
			}
		
		
		
		if (!path.isEmpty()) {
			for (int i = 0; i < path.size(); i++) {
				if (i == 0) {
					canvas.drawCircle(((float) path.get(i).x + 0.5f) * width,
							((float) path.get(i).y + 0.5f) * height,
							(width * 0.35f), startCircle);
				} else if (i == (path.size() - 1)) {
					canvas.drawCircle(((float) path.get(i).x + 0.5f) * width,
							((float) path.get(i).y + 0.5f) * height,
							(width * 0.35f), endCircle);
					drawArrow(canvas, path.get(i), path.get(i-1));
				} else {
					canvas.drawCircle(((float) path.get(i).x + 0.5f) * width,
							((float) path.get(i).y + 0.5f) * height,
							(width * 0.2f), pathCircle);
					drawArrow(canvas, path.get(i), path.get(i-1));
				}
			}
		}
	}
	

	public void drawArrow (Canvas canvas, Point cur, Point pre) {
		float fromX, fromY, toX, toY;
		
		Paint arrow = new Paint();
		arrow.setColor(getResources().getColor(R.color.blue));
		Path path = new Path(); 
		
		if ((cur.x == pre.x)&&(cur.y != pre.y)) {
//			fromX = (float) (pre.x + 0.6f)*width;
//			fromY = (float) (pre.y + 0.5f)*height;
//			toX = (float) (cur.x + 0.4f)*width;
//			toY = (float) (cur.y + 0.5f)*width;
//			canvas.drawLine(fromX, fromY, toX, toY, arrow);
			fromY = (cur.y > pre.y ? ((float) (cur.y+0.1f)*height) : ((float) (cur.y+0.9f)*height));
			toY = (cur.y > pre.y ? ((float) (pre.y+0.9f)*height) : ((float) (pre.y+0.1f)*height));
	        path.moveTo(((float) (cur.x+0.5f)*width), fromY);// start point of the triangle 
	        path.lineTo(((float) (pre.x+0.45f)*width), toY);  
	        path.lineTo(((float) (pre.x+0.55f)*width), toY);  
	        path.close(); // form a triangle
	        canvas.drawPath(path, arrow);  
		} else if ((cur.x != pre.x)&&(cur.y == pre.y)) {
//			fromX = (float) (pre.x + 0.5f)*width;
//			fromY = (float) (pre.y + 0.6f)*height;
//			toX = (float) (cur.x + 0.5f)*width;
//			toY = (float) (cur.y + 0.4f)*width;
//			canvas.drawLine(fromX, fromY, toX, toY, arrow);
			fromX = (cur.x > pre.x ? ((float) (cur.x+0.1f)*width) : ((float) (cur.x+0.9f)*width));
			toX = (cur.x > pre.x ? ((float) (pre.x+0.9f)*width) : ((float) (pre.x+0.1f)*width));
			path.moveTo(fromX, ((float) (cur.y+0.5f)*height));// start point of the triangle 
	        path.lineTo(toX, ((float) (pre.y+0.45f)*height));  
	        path.lineTo(toX, ((float) (pre.y+0.55f)*height));  
	        path.close(); // form a triangle
	        canvas.drawPath(path, arrow);  
		} else {
//			fromX = (float) (pre.x + 0.5f)*width;
//			fromY = (float) (pre.y + 0.6f)*height;
//			toX = (float) (cur.x + 0.5f)*width;
//			toY = (float) (cur.y + 0.4f)*width;
//			canvas.drawLine(fromX, fromY, toX, toY, arrow);
		}
	}
	
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			path.clear();
				select((int) (event.getX() / width),
						(int) ((event.getY()) / height));
			Log.i(TAG, "MotionEvent.ACTION_DOWN~~"); 
			Log.i(TAG, selX+" "+selY); 
			break;
		case MotionEvent.ACTION_MOVE:
			Log.i(TAG, selX+" "+selY); 
				if (isNextGrid(event.getX(), event.getY())) {
					select((int) (event.getX() / width), (int) (event.getY() / height));
					Log.i(TAG, "MotionEvent.ACTION_MOVE~~");   
					 
//					postInvalidate();
				} else {
//					Toast.makeText(persistentGame.getApplicationContext(), 
//							"Slide slower!", Toast.LENGTH_SHORT).show();
				}
			break;
			
		case MotionEvent.ACTION_UP:
				slideFinish();
			break;
		}
		return true;	
//		default:
//			return super.onTouchEvent(event);
//		}
//		return true;
	}
	
	public void slideFinish() {
		invalidate();
		//drawRect = false;
		int tmpScore = 0;
//		if (tooFast) {
//			this.game.alert(null, MOVE_TOO_FAST);
//			selX = selY = -1;
//			path.clear();
//			invalidate();
//			tooFast = false;
//			return;
//		}
		if (slideWord.length() < 3) {
			Toast.makeText(persistentGame.getApplicationContext(), slideWord.length()+", word should be no less than 3!", Toast.LENGTH_SHORT).show();
			slideWord.delete(0, slideWord.length());
		} else if (persistentGame.getFound().contains(slideWord.toString())) {
			//startAnimation(AnimationUtils.loadAnimation(game, R.anim.shake));
			//this.game.alert(currentLetter.toString(), EXIST_WORD);
			//this.game.playMusic(MUSIC_WRONG_WORD);
//			persistentGame.addWord(slideWord.toString());
			persistentGame.playMusic(true);
			Toast.makeText(persistentGame.getApplicationContext(), "Already found!", Toast.LENGTH_SHORT).show();
			slideWord.delete(0, slideWord.length());
			//add it to the word list although it does not exist
			
			
		} else if (persistentGame.checkWord(slideWord.toString())) {
//			this.game.addWord(currentLetter.toString());
//			this.game.addWordNum();
			
			
			//compute the value of this word
			tmpScore = wordScore(slideWord.toString());
			persistentGame.addScore(tmpScore);
			persistentGame.addWord(slideWord.toString());

			Toast.makeText(persistentGame.getApplicationContext(), "Right!", Toast.LENGTH_SHORT).show();
			persistentGame.playMusic(true);
			slideWord.delete(0, slideWord.length());
		} else {
//			startAnimation(AnimationUtils.loadAnimation(game, R.anim.shake));
//			this.game.alert(currentLetter.toString(), WRONG_WORD);
//			tmpScore = -1;
//			this.game.playMusic(MUSIC_WRONG_WORD);
			
			//vibrate to show that the word does not exists
			persistentGame.vibrate(300);
			Toast.makeText(persistentGame.getApplicationContext(), slideWord.toString()+" ", Toast.LENGTH_SHORT).show();
//			Toast.makeText(persistentGame.getApplicationContext(), "Not exists!", Toast.LENGTH_SHORT).show();
			slideWord.delete(0, slideWord.length());
		}
		selX = selY = -1;
		path.clear();
		invalidate();	
	}
	
	//the score of the word
	public int wordScore(String str) {
		int wordLen = str.length();
		if (wordLen<3) {
			return 0;
		} else if (wordLen<5) {
			return 1;
		} else {
			return (wordLen-3);
		}
	}
	
	
	
	
	public boolean isNextGrid(float x, float y) {
		int checkX = (int) (x/width); 
		int checkY = (int) (y/height);
		if (!((checkX == selX) && (checkY == selY)))
			if ((Math.abs(checkX - selX)<2)&&(Math.abs(checkY - selY)<2)) {
				if (inCircle(x, y, checkX, checkY)) {
					return true;
				} 
			} else {
//					Toast.makeText(persistentGame.getApplicationContext(), 
//							"Slide slower!", Toast.LENGTH_SHORT).show();
			}
		return false;		
	}
	
	//check if touch point is in the circle
	public boolean inCircle (float x, float y, int X, int Y) {
		float R = Math.min(0.3f * width, 0.3f * height);
		float pointX = ((X+0.5f) * width) - x;
		float pointY = ((Y+0.5f) * height) - y;
		if (Math.pow(R, 2) > (Math.pow(pointX, 2) + Math.pow(pointY, 2))) {
			return true;
		} else { 
			return false;
		}
	}

	//for left, right, up, down button users
	private void select(int x, int y) {
		invalidate(selRect);
		Log.i(TAG, x+" "+y); 
		selX = (int) x;
		selY = (int) y;
		Log.i(TAG, selX+" "+selY); 
		getRect(selX, selY, selRect);
		invalidate();
		
		String tmp = persistentGame.getTileLetter(selX, selY);
		Point cur = new Point(selX, selY);
		if (path.contains(cur)) {
			path = path.subList(0, path.lastIndexOf(cur) + 1);
			slideWord.delete(path.lastIndexOf(cur) + 1,
					slideWord.length());
		} else {
			path.add(new Point(selX, selY));
			slideWord.append(tmp);
		}
		
//		game.vibrate();
//		this.game.playMusic(MUSIC_SWIPE);
//		this.game.setCurrentSequence(currentLetter.toString());
//		invalidate();
	}
		
	private void getRect(int x, int y, Rect rect) {
		rect.set((int) (x * width), (int) (y * height), (int) (x
				* width + width), (int) (y * height + height));
	}
	
	


}
