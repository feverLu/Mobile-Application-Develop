package edu.neu.madcourse.binbinlu.finalproject;

import java.util.Random;

import edu.neu.madcourse.binbinlu.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.EmbossMaskFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

public class StepOnDrumGameView extends View {
	private static final String TAG = "StepOnDrumGameView";
 
	private static final int ID = 100;

	/*public Thread t = null;
	public SurfaceHolder holder;
	public boolean isOk = false;
	public int uCounter;
	public int bgCounter =-1;
	public int oldNext = -1;*/	
	private int portion = 10;
	private float width; // width of the rect
	private float height; // height of rect
	/*private Random random = new Random(System.nanoTime());
	private long timeGap;*/
	private double exp ;
	private boolean expIn = false; 
	
	private int screenW;
	private int screenH;
	
	private int bmpW;
	private int bmpH;
	private int lineH;
	private boolean bmpIn =false;
	float gravity1, gravity2;
	
	
	private final StepOnDrumGame game;
	/*private long initialTime = System.currentTimeMillis();*/

	public StepOnDrumGameView(Context context) {
		super(context);
		this.game = (StepOnDrumGame) context;
		setFocusable(true);
		/*holder = getHolder();
		uCounter = counter;*/
		
		setFocusableInTouchMode(true);
		/*t = new Thread(this);
		initialTime = System.currentTimeMillis();*/
		setId(ID);
	}

	public StepOnDrumGameView(Context context, AttributeSet attr) {
		super(context, attr,0);
		this.game = (StepOnDrumGame)context;
		/*holder = getHolder();
		uCounter = counter;*/
		setFocusable(true);
		
		/*t = new Thread(this);*/
		setFocusableInTouchMode(true);
		/*initialTime = System.currentTimeMillis();*/
		setId(ID);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		width = w / (float)(portion);
		height =  h /(float)(portion);
		Log.d(TAG, "onSizeChanged: width " + width + ", height " + height);
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas){
		Log.d(TAG, "onDraw:");
		// Draw the background...
		if(!expIn){
			screenW = getWidth();
			screenH = getHeight();
			exp= 10E-2;
			expIn = true;
		}
		/*bgCounter ++;*/
		Paint background = new Paint();
		background.setColor(getResources().getColor(R.color.orange));
		canvas.drawRect(0, 0, screenW, screenH, background);
		drawDetectedZone(canvas);
		
		Paint paint = new Paint();
			Bitmap bmpRaw = BitmapFactory.decodeResource(getResources(), R.drawable.back);
			canvas.drawLine(0, (float)(0.25*getHeight()), getWidth(),(float)(0.25*getHeight()), paint);
			canvas.drawLine(0, (float)(0.235*getHeight()), getWidth(),(float)(0.235*getHeight()), paint);
//			Bitmap line = BitmapFactory.decodeResource(getResources(), R.drawable.line);
			
			//Bitmap bmp = Bitmap.createBitmap(bmpRaw);
			if(!bmpIn){
				bmpW = bmpRaw.getWidth();
				bmpH = bmpRaw.getHeight();
//				lineH=line.getHeight();
				bmpIn = true;
			}
			canvas.drawBitmap(bmpRaw,(screenW-bmpW),(screenH-bmpH), paint);
		
//			canvas.drawBitmap(line,0,((float)(0.25*screenH)-lineH+3), paint);
			
			drawContext(String.valueOf(this.game.score), 2f, 1f,
					(float)0.5*screenW, (float)0.5*screenH, canvas);
			drawContext("Miss: "+String.valueOf(this.game.miss), 0.5f, 1f,
					(float)0.5*screenW, (float)0.6*screenH, canvas);
		if(this.game.arrowList!= null&&!this.game.arrowList.isEmpty())
			paint.setTextSize(30);
			canvas.drawText(game.cur[0]+"", getWidth()/5, getHeight()/5, paint);
			canvas.drawText(game.getFrontEnd() + "", 2*getWidth()/3, getHeight()/5, paint);
			drawArrows(canvas);
		
		}
	
	public void drawDetectedZone(Canvas canvas) {
//Paint arrowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//		
//		Shader mLinearGradient = null;
//		mLinearGradient = new LinearGradient(0,0,100,100,   
//                new int[]{Color.CYAN,Color.GREEN,Color.BLUE,Color.WHITE},   
//                null,Shader.TileMode.REPEAT);   
//		
//		Bitmap mBitmap = null;
//		if (!arrow.detected) {
//			mBitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.arrowfancy)).getBitmap();
//		} else {
//			mBitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.arrowalreadydetected)).getBitmap();
//		}
//		Shader mBitmapShader = null;
//		mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.REPEAT, Shader.TileMode.MIRROR);
//		
//		float[] direction = new float[]{1, 1, 1};
//		float light = 0.2f;
//		float specular = 6;
//		float blur = 1.5f;
//		EmbossMaskFilter emboss = new EmbossMaskFilter(direction, light, specular, blur);
//		
////		Rasterizer rasterizer = new Rasterizer();
////		arrowPaint.setRasterizer(rasterizer);
//		
//		arrowPaint.setShader(mLinearGradient);
////		arrowPaint.setDither(true);  
//		arrowPaint.setStrokeJoin(Paint.Join.MITER);  
//		arrowPaint.setStrokeCap(Paint.Cap.ROUND);
//		arrowPaint.setPathEffect(new CornerPathEffect(4));
//		
//
//		
//		arrowPaint.setMaskFilter(emboss);
//		arrowPaint.setShader(mBitmapShader);
//		arrowPaint.setColor(getResources().getColor(R.color.green));
//		arrowPaint.setStyle(Paint.Style.FILL_AND_STROKE);
//		arrowPaint.setStrokeCap(Paint.Cap.ROUND);
//		
//		Path mPath = new Path();
//		//arrow heading left
//		if(arrow.direction==0){
//			mPath.moveTo(arrow.left, (arrow.top+0.06f*screenW));
//			mPath.lineTo((arrow.left+0.06f*screenW), arrow.top);
//			mPath.lineTo((arrow.left+0.06f*screenW), (arrow.top+0.03f*screenW));
//			mPath.lineTo((arrow.left+0.12f*screenW), (arrow.top+0.03f*screenW));
//			mPath.lineTo((arrow.left+0.12f*screenW), (arrow.top+0.09f*screenW));
//			mPath.lineTo((arrow.left+0.06f*screenW), (arrow.top+0.09f*screenW));
//			mPath.lineTo((arrow.left+0.06f*screenW), (arrow.top+0.12f*screenW));
//			mPath.lineTo(arrow.left, (arrow.top+0.06f*screenW));
//		}
//		
////		//arrow heading right
//		if(arrow.direction==1){
//			mPath.moveTo(arrow.left, (arrow.top+0.03f*screenW));
//			mPath.lineTo((arrow.left+0.06f*screenW), (arrow.top+0.03f*screenW));
//     		mPath.lineTo((arrow.left+0.06f*screenW), arrow.top);
//			mPath.lineTo((arrow.left+0.12f*screenW), (arrow.top+0.06f*screenW));
//			mPath.lineTo((arrow.left+0.06f*screenW), (arrow.top+0.12f*screenW));
//			mPath.lineTo((arrow.left+0.06f*screenW), (arrow.top+0.09f*screenW));
//			mPath.lineTo(arrow.left, (arrow.top+0.09f*screenW));
//			mPath.lineTo(arrow.left, (arrow.top+0.03f*screenW));
//		}
//		
////		//arrow heading up
//		if(arrow.direction==2){
//			mPath.moveTo(arrow.left, (arrow.top+0.06f*screenW));
//			mPath.lineTo((arrow.left+0.06f*screenW), arrow.top);
//			mPath.lineTo((arrow.left+0.12f*screenW), (arrow.top+0.06f*screenW));
//			mPath.lineTo((arrow.left+0.09f*screenW), (arrow.top+0.06f*screenW));
//			mPath.lineTo((arrow.left+0.09f*screenW), (arrow.top+0.12f*screenW));
//			mPath.lineTo((arrow.left+0.03f*screenW), (arrow.top+0.12f*screenW));
//			mPath.lineTo((arrow.left+0.03f*screenW), (arrow.top+0.06f*screenW));
//			mPath.lineTo(arrow.left, (arrow.top+0.06f*screenW));
//		}
//		
//		//arrow heading down
//		if(arrow.direction==3){
//			mPath.moveTo(arrow.left, (arrow.top+0.06f*screenW));
//			mPath.lineTo((arrow.left+0.06f*screenW), (arrow.top+0.12f*screenW));
//			mPath.lineTo((arrow.left+0.12f*screenW), (arrow.top+0.06f*screenW));
//			mPath.lineTo((arrow.left+0.09f*screenW), (arrow.top+0.06f*screenW));
//			mPath.lineTo((arrow.left+0.09f*screenW), arrow.top);
//			mPath.lineTo((arrow.left+0.03f*screenW), arrow.top);
//			mPath.lineTo((arrow.left+0.03f*screenW), (arrow.top+0.06f*screenW));
//			mPath.lineTo(arrow.left, (arrow.top+0.06f*screenW));
//		}
//		
//		mPath.close();
//		canvas.drawPath(mPath, arrowPaint);
		
	}
		
	private void drawContext(String text, float textSize, float textScale,
			float xPosition, float yPosition, Canvas canvas) {
		Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
		foreground.setColor(getResources().getColor(R.color.red));
		foreground.setStyle(Style.FILL);
		foreground.setTextSize(height * textSize);
		foreground.setTextScaleX(textScale * width / height);
		foreground.setTextAlign(Paint.Align.CENTER);

		// Draw the number in the center of the tile
		FontMetrics fm = foreground.getFontMetrics();
		// Centering in X: use alignment (and X at midpoint)
		float x = width / 2;
		// Centering in Y: measure ascent/descent first
		float y = height / 2 - (fm.ascent + fm.descent) / 2;
		canvas.drawText(text, xPosition - x/2, yPosition + y, foreground);
	}
	
	private void drawArrows(Canvas canvas){
		
		for(int i = 0; i< this.game.arrowList.size();i++){
			StepOnDrumArrow arrow = this.game.arrowList.get(i);
			arrow.top-=arrow.speed;
			
			if((Math.abs(arrow.top - (0.25*screenH))<= exp)){
				this.game.openSensorData=true;
			}
			if((Math.abs(arrow.top - (0.235*screenH))<= exp)){
				this.game.openSensorData= false;
				gravity1 = game.cur[0];
				gravity2 = game.getFrontEnd();
				
				if(!arrow.checked){
					arrow.detected = this.game.ifDetected(arrow);
					arrow.checked = true;
				}
				
			}
			if(arrow.top<=0){
				this.game.arrowList.remove(i);
			}
		}
		for(int i = 0; i< this.game.arrowList.size();i++){
			StepOnDrumArrow arrow = this.game.arrowList.get(i);
			drawOneArrow(arrow,canvas);
			/*Paint paint = new Paint();
			Bitmap bmpRaw = this.game.getBitmap(arrow);
			//Bitmap bmp = Bitmap.createBitmap(bmpRaw);
			canvas.drawBitmap(bmpRaw,arrow.left,arrow.top, paint);*/
			
			}
	}
	
	private void drawOneArrow(StepOnDrumArrow arrow, Canvas canvas){
		Paint arrowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		Shader mLinearGradient = null;
		mLinearGradient = new LinearGradient(0,0,100,100,   
                new int[]{Color.CYAN,Color.GREEN,Color.BLUE,Color.WHITE},   
                null,Shader.TileMode.REPEAT);   
		
		Bitmap mBitmap = null;
		if (!arrow.detected) {
			mBitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.arrowfancy)).getBitmap();
		} else {
			mBitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.arrowalreadydetected)).getBitmap();
		}
		Shader mBitmapShader = null;
		mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.REPEAT, Shader.TileMode.MIRROR);
		
		float[] direction = new float[]{1, 1, 1};
		float light = 0.2f;
		float specular = 6;
		float blur = 1.5f;
		EmbossMaskFilter emboss = new EmbossMaskFilter(direction, light, specular, blur);
		
//		Rasterizer rasterizer = new Rasterizer();
//		arrowPaint.setRasterizer(rasterizer);
		
		arrowPaint.setShader(mLinearGradient);
//		arrowPaint.setDither(true);  
		arrowPaint.setStrokeJoin(Paint.Join.MITER);  
		arrowPaint.setStrokeCap(Paint.Cap.ROUND);
		arrowPaint.setPathEffect(new CornerPathEffect(4));
		

		
		arrowPaint.setMaskFilter(emboss);
		arrowPaint.setShader(mBitmapShader);
		arrowPaint.setColor(getResources().getColor(R.color.green));
		arrowPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		arrowPaint.setStrokeCap(Paint.Cap.ROUND);
		
		Path mPath = new Path();
		//arrow heading left
		if(arrow.direction==0){
			mPath.moveTo((arrow.left+0.08f*screenW), arrow.top);
			mPath.lineTo((arrow.left+0.08f*screenW), (arrow.top+0.04f*screenW));
			mPath.lineTo((arrow.left+0.16f*screenW), (arrow.top+0.04f*screenW));
			mPath.lineTo((arrow.left+0.16f*screenW), (arrow.top+0.12f*screenW));
			mPath.lineTo((arrow.left+0.08f*screenW), (arrow.top+0.12f*screenW));
			mPath.lineTo((arrow.left+0.08f*screenW), (arrow.top+0.16f*screenW));
			mPath.lineTo(arrow.left, (arrow.top+0.08f*screenW));
			mPath.lineTo((arrow.left+0.08f*screenW), arrow.top);
		}
		
//		//arrow heading right
		if(arrow.direction==1){
			mPath.moveTo(arrow.left, (arrow.top+0.04f*screenW));
			mPath.lineTo((arrow.left+0.08f*screenW), (arrow.top+0.04f*screenW));
     		mPath.lineTo((arrow.left+0.08f*screenW), arrow.top);
			mPath.lineTo((arrow.left+0.16f*screenW), (arrow.top+0.08f*screenW));
			mPath.lineTo((arrow.left+0.08f*screenW), (arrow.top+0.16f*screenW));
			mPath.lineTo((arrow.left+0.08f*screenW), (arrow.top+0.12f*screenW));
			mPath.lineTo(arrow.left, (arrow.top+0.12f*screenW));
			mPath.lineTo(arrow.left, (arrow.top+0.04f*screenW));
		}
		
//		//arrow heading up
		if(arrow.direction==2){
			mPath.moveTo(arrow.left, (arrow.top+0.08f*screenW));
			mPath.lineTo((arrow.left+0.08f*screenW), arrow.top);
			mPath.lineTo((arrow.left+0.16f*screenW), (arrow.top+0.08f*screenW));
			mPath.lineTo((arrow.left+0.12f*screenW), (arrow.top+0.08f*screenW));
			mPath.lineTo((arrow.left+0.12f*screenW), (arrow.top+0.16f*screenW));
			mPath.lineTo((arrow.left+0.04f*screenW), (arrow.top+0.16f*screenW));
			mPath.lineTo((arrow.left+0.04f*screenW), (arrow.top+0.08f*screenW));
			mPath.lineTo(arrow.left, (arrow.top+0.08f*screenW));
		}
		
		//arrow heading down
		if(arrow.direction==3){
			mPath.moveTo(arrow.left, (arrow.top+0.08f*screenW));
			mPath.lineTo((arrow.left+0.08f*screenW), (arrow.top+0.16f*screenW));
			mPath.lineTo((arrow.left+0.16f*screenW), (arrow.top+0.08f*screenW));
			mPath.lineTo((arrow.left+0.12f*screenW), (arrow.top+0.08f*screenW));
			mPath.lineTo((arrow.left+0.12f*screenW), arrow.top);
			mPath.lineTo((arrow.left+0.04f*screenW), arrow.top);
			mPath.lineTo((arrow.left+0.04f*screenW), (arrow.top+0.08f*screenW));
			mPath.lineTo(arrow.left, (arrow.top+0.08f*screenW));
		}
		
		mPath.close();
		canvas.drawPath(mPath, arrowPaint);
	}
	/*
	public void run(){
		while(isOk ==true){
			//perform drawing here
			if(!holder.getSurface().isValid()){
				continue;
			}
			long timeGap=System.currentTimeMillis()-initialTime;
			generateArrow(timeGap);
			
			Canvas canvas = holder.lockCanvas();
			gameDraw(canvas);
			holder.unlockCanvasAndPost(canvas);
			//sleep 50 ms
			try {
		  		 Thread.sleep(30L);    // 0.5 second
		  		  }
		   		 catch (Exception e) {
		   			 e.printStackTrace();
		   		 } 
			
		}
	}*/
	/*
	public void generateArrow(long t){
		if(t <=30000){
			if((t%2000)==0)
				addNewArrow();
		}else{
			if(30000<t&&t<=900000){
				if((t%1000)==0)
					addNewArrow();
			}else{
				if(900000<t){
					if((t%500)==0)
						addNewArrow();
				}
			}
		}
	}
	*/
	/*private void addNewArrow(){
		
		int next = random.nextInt(4);
		while(oldNext ==next){
			next = random.nextInt(4);
		}
		oldNext =next;
		StepOnDrumArrow arrow = new StepOnDrumArrow((float)(getHeight()*0.00375), next, this);
		this.game.arrowList.add(arrow);
	}
*/
	
	/*public void pause(){
		isOk = false;
		while(true){
			try{
				t.join();
			}catch( Exception e){
				e.printStackTrace();
			}
			break;
		}
		t=null;
	}
	
	public void resume(){
		isOk = true;
		
		t.start();
	}
	*/
	
	
	}




