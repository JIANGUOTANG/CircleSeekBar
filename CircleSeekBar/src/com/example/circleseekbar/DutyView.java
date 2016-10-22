package com.example.circleseekbar;

import org.w3c.dom.ls.LSException;

import android.R.integer;
import android.R.xml;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Style;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;

public class DutyView extends ViewGroup implements OnTouchListener{
	
	
	int width;
	int height;	

	/**
	 * duty最大值
	 */
	public static final int MAX_DUTY = 100;
	
	public int getMaxDuty()
	{
		return MAX_DUTY;
	}
	/**
	 * duty当前值
	 */
	private int duty;
	
	public int getDuty() {
		return duty;
	}
	
	public void setDuty(int value)
	{
		if(value >= 0 && value <= MAX_DUTY)
		{
			duty = value;
			postInvalidate();
		}
	}
	
	/**
	 * 文本大小
	 */
	private float textSize;	
	
	public float getTextSize() {
		return textSize;
	}

	public void setTextSize(float textSize) {
		this.textSize = textSize;
	}

	/**
	 * 文本颜色
	 */
	public static final int DEFAULT_TEXT_COLOR = 0xff326ee9;
	
	private int textColor = DEFAULT_TEXT_COLOR;
	
	public void setTextColor(int color)
	{
		this.textColor = color;
		postInvalidate();
	}
	
	public int getTextColor()
	{
		return this.textColor;
	}
	
	
	/**
	 * 圆弧颜色
	 */
	public static final int DEFAULT_PROGRESS_BG_COLOR = 0xffffffff;
	
	private int progressBgColor = DEFAULT_PROGRESS_BG_COLOR;
	
	public int getProgressBgColor() {
		return progressBgColor;
	}

	public void setProgressBgColo(int arcColor) {
		this.progressBgColor = arcColor;
		postInvalidate();
	}
	
	public static final int DEFAULT_PROGRESS_COLOR = 0xffff4e00;
	
	private int ProgressColor = DEFAULT_PROGRESS_COLOR;
	
	public int getProgressColor()
	{
		return ProgressColor;
	}
	
	public void setProgressColor(int value)
	{
		this.ProgressColor = value;
		postInvalidate();
	}
	
	/**
	 * 圆弧尺寸
	 */
	private float arcWidth;	

	public float getArcWidth() {
		return arcWidth;
	}

	public void setArcWidth(float arcWidth) {
		this.arcWidth = arcWidth;
	}
			

	public DutyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DutyView);
		
		duty = array.getInteger(R.styleable.DutyView_initDuty, 0);				
		
		textColor = array.getColor(R.styleable.DutyView_textColor, DEFAULT_TEXT_COLOR);
		textSize = array.getDimension(R.styleable.DutyView_textSize, DensityUtils.sp2px(context, 18));
		
		progressBgColor = array.getColor(R.styleable.DutyView_progressBgColor, DEFAULT_PROGRESS_BG_COLOR);
		ProgressColor = array.getColor(R.styleable.DutyView_progressColor, DEFAULT_PROGRESS_COLOR);
		
		arcWidth = array.getDimension(R.styleable.DutyView_arcWidth, DensityUtils.dp2px(context, 5));
									
		array.recycle();
		
		arcPaint = new Paint();
		arcPaint.setColor(progressBgColor);
		arcPaint.setAntiAlias(true);
		arcPaint.setStyle(Style.STROKE);
		arcPaint.setStrokeWidth(arcWidth);
		
		textPaint = new Paint();
		textPaint.setColor(textColor);
		textPaint.setTextSize(textSize);
		textPaint.setAntiAlias(true);		
		
		setOnTouchListener(this);		
		
		this.setBackgroundColor(0x00ffffff);
	}
	
	
	
	Paint arcPaint = null;
	RectF arcRectF = new RectF();
	
	private void drawProgressBar(Canvas canvas) {
		// TODO Auto-generated method stub

		arcPaint.setColor(progressBgColor);
		canvas.drawArc(arcRectF, 135, 270, false, arcPaint);
		
		arcPaint.setColor(ProgressColor);
		canvas.drawArc(arcRectF, 135, 270*duty/MAX_DUTY, false, arcPaint);
				
	}
	
	Paint textPaint = null;
	
	private void drawText(Canvas canvas)
	{
		String text = String.valueOf((int)(duty*100/MAX_DUTY))+"%";
		
		float length = textPaint.measureText(text);
		float x = width/2-length/2;
		float y = height/2;
		
		canvas.drawText(text, x, y, textPaint);
	}
		
	//DutyViewProgress progressView;
	DutyViewThumb thumbView;
	int thumbWidth;
	int thumbHeight;
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		drawProgressBar(canvas);
		
		drawText(canvas);
		
		super.onDraw(canvas);		
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		
		measureChildren(widthMeasureSpec, heightMeasureSpec);
		
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);
		
		setMeasuredDimension(width, height);
		
		if(thumbView == null)
		{
			thumbView = (DutyViewThumb) getChildAt(0);
			thumbWidth = thumbView.getMeasuredWidth()/2;
			thumbHeight = thumbView.getMeasuredHeight()/2;
		}
						
		arcRectF.set(thumbWidth, thumbWidth, width-thumbWidth,height-thumbWidth);
				
		invaliArea.set(width/3, height/2, width*2/3, height);
	}

	RectF thumbRect = new RectF();
	
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
				
		try {
			
			if(thumbView == null)
			{
				thumbView = (DutyViewThumb) getChildAt(0);
				thumbWidth = thumbView.getMeasuredWidth()/2;
				thumbHeight = thumbView.getMeasuredHeight()/2;
			}
						
			calculateThumbPosition();
			
			thumbView.layout((int)thumbRect.left,
										(int)thumbRect.top, 
										(int)thumbRect.right, 
										(int)thumbRect.bottom);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}				
	}
	
	/**
	 * 输入坐标，求出对应象限
	 * @param x
	 * @param y
	 * @return
	 */
	private static int getQuadrant(double x, double y) {
		if (x >= 0) {
			return y >= 0 ? 1 : 4;
		} else {
			return y >= 0 ? 2 : 3;
		}
	}
	
	/**
	 * 获取指定坐标对应的角度
	 * @param xTouch
	 * @param yTouch
	 * @return
	 */
	private double getAngle(double xTouch, double yTouch) {
		double x = xTouch - (width / 2d);
		double y = height - yTouch - (height / 2d);

		switch (getQuadrant(x, y)) {
		case 1:
			return Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;

		case 2:
		case 3:
			return 180 - (Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);

		case 4:
			return 360 + Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;

		default:
			// ignore, does not happen
			return 0;
		}
	}
	
	private final float angleRate = MAX_DUTY/270f;
	/**
	 * 角度对应的百分比
	 * @param angle
	 * @return
	 */
	private int angle2pesent(double angle)
	{
		//无效角度
		if(angle < 300 && angle > 240)
			return -1;
		
		if(angle > 225)
			angle-=360;
		
		return (int) (Math.abs(angle-225)*angleRate);
	}
	
	/**
	 * 根据当前的duty值计算thumb的位置
	 */
	private void calculateThumbPosition()
	{
		double angle = duty/angleRate;		
		angle += 135;
		
		if(angle > 360)
			angle-=360;
		
		float xPoint = (float) (width/2+(width/2-thumbWidth)*Math.cos(Math.toRadians(angle)));
		float yPoint = (float) (height/2+(height/2-thumbHeight)*Math.sin(Math.toRadians(angle)));
		
		thumbRect.left = xPoint-thumbWidth;
		thumbRect.top = yPoint-thumbHeight;
		thumbRect.right = xPoint+thumbWidth;
		thumbRect.bottom = yPoint+thumbHeight;		
		
	}
	
	//触摸的无效区域
	RectF invaliArea = new RectF();
		
	float last_x;
	float last_y;
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		
		float x = event.getX();
		float y = event.getY();	
		
		//触摸无效区域禁止横向滑动
		if(invaliArea.contains(x, y))
		{			
			if(thumbView.isSelected())
			{
				thumbView.setSelected(false);
				if(listener != null)
					listener.onDutyChanged(duty);
			}			
			Log.i("userLog", "无效区域");
			return true;
		}
		
		switch (event.getAction()) 
		{

		case MotionEvent.ACTION_DOWN:
			Log.i("userLog", "action down");
			if(thumbView.isSelected())
			{
				/*thumbView.setSelected(false);
				if(listener != null)
				{
					listener.onDutyChanged(duty);
				}	*/			
			}
			else {
				//是否点击滑动块
				if(thumbRect.contains((int)x, (int)y))
				{
					thumbView.setSelected(true);
					last_x = x;
					last_y = y;
					Log.i("userLog", "点击滑块");
				}
				
			}
			break;
							
		case MotionEvent.ACTION_MOVE:					

			if(thumbView.isSelected())
			{
				Log.i("userLog", "滑动");
				//触摸不连续，防止多点触控
				if(Math.abs(x-last_x) > 150 || Math.abs(y-last_y) > 150)
				{
					Log.i("userLog", "滑动不连续");
					thumbView.setSelected(false);
					if(listener != null)
					{
						listener.onDutyChanged(duty);
					}
					return true;
				}
				
				//计算角度和百分比
				double angle = getAngle(x, y);				
				int _duty = angle2pesent(angle);
				
				if(_duty != -1)
				{
					setDuty(_duty);					
				}
				else {
					if(duty < 10)
					{
						setDuty(0);
					}
					else if(duty > 90)
					{
						setDuty(100);
					}
					
					
					thumbView.setSelected(false);
					if(listener != null)
					{
						listener.onDutyChanged(duty);
					}
				}
				
				last_x = x;
				last_y = y;
				
				calculateThumbPosition();
				
				thumbView.layout((int)thumbRect.left,
											(int)thumbRect.top, 
											(int)thumbRect.right, 
											(int)thumbRect.bottom);
			}
			
			break;
			
		case MotionEvent.ACTION_UP:	
			Log.i("userLog", "action up");
			if(thumbView.isSelected())
			{
				thumbView.setSelected(false);	
				if(listener != null)
				{
					listener.onDutyChanged(duty);
				}
			}
			break;

		default:
			break;
		}
		return true;
	}
	
	
	
	
	public interface onDutyChangedListener
	{
		void onDutyChanged(int currentDuty);		
	}
	
	onDutyChangedListener listener = null;
	
	public void setOnDutyChangedListener(onDutyChangedListener listener)
	{
		this.listener = listener;
	}
	

}
