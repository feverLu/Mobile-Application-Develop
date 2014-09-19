package edu.neu.madcourse.binbinlu.finalproject;
import edu.neu.madcourse.binbinlu.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class ScoreView extends View {
	private static final String TAG = "ScoreView";
 
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
	private int exp ;
	private boolean expIn = false; 
	
	private int screenW;
	private int screenH;
	
	private int bmpW;
	private int bmpH;
	private int lineH;
	private boolean bmpIn =false;
	
	
	private final StepOnDrumScore score;
	/*private long initialTime = System.currentTimeMillis();*/

	public ScoreView(Context context) {
		super(context);
		this.score = (StepOnDrumScore) context;
		setFocusable(true);
		/*holder = getHolder();
		uCounter = counter;*/
		
		setFocusableInTouchMode(true);
		/*t = new Thread(this);
		initialTime = System.currentTimeMillis();*/
		setId(ID);
	}

	public ScoreView(Context context, AttributeSet attr) {
		super(context, attr,0);
		this.score = (StepOnDrumScore)context;
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
		Log.i(TAG,"exp is ~~~~~~~~~~~~~~~`"+getHeight());
		Log.i(TAG,"exp is ~~~~~~~~~~~~~~~`"+getHeight());
		Log.i(TAG,"exp is ~~~~~~~~~~~~~~~`"+getHeight());
		Log.i(TAG,"exp is ~~~~~~~~~~~~~~~`"+getHeight());
		Log.i(TAG,"exp is ~~~~~~~~~~~~~~~`"+getHeight());
		if(!expIn){
			screenW = getWidth();
			screenH = getHeight();
			exp=(int) (screenH*0.005);
			expIn = true;
		}
		Log.i(TAG,"exp is ~~~~~~~~~~~~~~~`"+exp);
		Log.i(TAG,"exp is ~~~~~~~~~~~~~~~`"+exp);
		Log.i(TAG,"exp is ~~~~~~~~~~~~~~~`"+exp);
		Log.i(TAG,"exp is ~~~~~~~~~~~~~~~`"+exp);
		Log.i(TAG,"exp is ~~~~~~~~~~~~~~~`"+exp);
		Log.d(TAG, "Draw Background:");
		
		/*bgCounter ++;*/
		Paint background = new Paint();
		background.setColor(getResources().getColor(R.color.orange));
		canvas.drawRect(0, 0, screenW, screenH, background);
		
		/*Paint paint = new Paint();
			Bitmap bmpRaw = BitmapFactory.decodeResource(getResources(), R.drawable.back);
			Bitmap line = BitmapFactory.decodeResource(getResources(), R.drawable.line);
			//Bitmap bmp = Bitmap.createBitmap(bmpRaw);
			if(!bmpIn){
				bmpW = bmpRaw.getWidth();
				bmpH = bmpRaw.getHeight();
				lineH=line.getHeight();
				bmpIn = true;
			}
			canvas.drawBitmap(bmpRaw,(screenW-bmpW),(screenH-bmpH), paint);
		
			canvas.drawBitmap(line,0,((float)(0.25*screenH)-lineH+3), paint);*/
			
			drawContext(String.valueOf(this.score.scoreContent), 0.5f, 1f,
					(float)0.5*screenW, (float)0.5*screenH, canvas);
			/*drawContext("Miss: "+String.valueOf(this.game.miss), 0.5f, 1f,
					(float)0.5*screenW, (float)0.6*screenH, canvas);*/
		
	
			
		
		
		
		
		
		
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
	
	
	
	
	
	}




