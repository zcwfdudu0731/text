
package com.android.supermario;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Scroller;

/**
 * �Զ���View��ģ��android ����Luncher
 */
public class DragableLuncher extends ViewGroup {
	// ��ť����ɫ
	int choseColor, defaultColor;
	// �ײ���ť����
	ImageButton[] bottomBar;
	// ����õ��������ԵĶ���
	private Scroller mScroller;
	// �������Ĺ�����
	private VelocityTracker mVelocityTracker;
	// ��������ʼX����
	private int mScrollX = 0;
	// Ĭ����ʾ�ڼ���
	private int mCurrentScreen = 0;
	// ��������X����
	private float mLastMotionX;

	private static final int SNAP_VELOCITY = 1000;

	private final static int TOUCH_STATE_REST = 0;
	private final static int TOUCH_STATE_SCROLLING = 1;

	private int mTouchState = TOUCH_STATE_REST;
	//�û������ľ�����Сֵ
	private int mTouchSlop = 0;

	public DragableLuncher(Context context) {
		super(context);
		mScroller = new Scroller(context);
		//�õ������ж��û��Ƿ񻬶��ľ�����ٽ�ֵ
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

		this.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.FILL_PARENT));
	}

	public DragableLuncher(Context context, AttributeSet attrs) {
		super(context, attrs);
		mScroller = new Scroller(context);

		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

		this.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,

				ViewGroup.LayoutParams.FILL_PARENT));

		/* ���xml�����õ�����ֵ��������ָ��Ĭ����ʾ�ڼ���Ļ */
		// mCurrentScreen =
		// attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/com.luncher.demo",
		// "default_screen", 0);
		TypedArray a = getContext().obtainStyledAttributes(attrs,
				R.styleable.DragableLuncher);
		mCurrentScreen = a.getInteger(
				R.styleable.DragableLuncher_default_screen, 0);
	}
	//����touch�¼�������true����ִ��ontouch�ص�����
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		final int action = ev.getAction();
		if ((action == MotionEvent.ACTION_MOVE)
				&& (mTouchState != TOUCH_STATE_REST)) {
			return true;
		}
		//ȡ�õ�ǰ��x����
		final float x = ev.getX();

		switch (action) {
			case MotionEvent.ACTION_MOVE:
				// ȡ����ֵ
				final int xDiff = (int) Math.abs(x - mLastMotionX);
				//���ƶ��ľ���С����С�������ƶ��ı�־λ��Ϊtrue��������Ϊfalse
				boolean xMoved = xDiff > mTouchSlop;
				if (xMoved) {
					//����û�����x�Ử���㹻�ľ���͹���
					mTouchState = TOUCH_STATE_SCROLLING;
				}
				break;
			//������touch˲���¼��x����
			case MotionEvent.ACTION_DOWN:
				// ��¼�����ĳ�ʼλ��
				mLastMotionX = x;
				//���ֹͣ�϶�
				mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST
						: TOUCH_STATE_SCROLLING;
				break;

			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				// ֹͣ�϶�
				mTouchState = TOUCH_STATE_REST;
				break;
		}
		return mTouchState != TOUCH_STATE_REST;
	}

	/**
	 * �����Ƿ�򿪴�������
	 */
	public boolean isOpenTouchAnima(boolean b) {
		isOpen = b;
		return isOpen;
	}
	//Ĭ�ϴ���������
	public boolean isOpen = true;
	//��Ӧ����ʱ��
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isOpen) {
			if (mVelocityTracker == null) {
				//�������̽����
				mVelocityTracker = VelocityTracker.obtain();
			}
			//��touch�¼���ӽ�̽������
			mVelocityTracker.addMovement(event);
			//ȡ��touch�¼�������
			final int action = event.getAction();
			//ȡ��x����
			final float x = event.getX();
			//�������touch�¼�
			switch (action) {
				case MotionEvent.ACTION_DOWN:
					if (!mScroller.isFinished()) {
						mScroller.abortAnimation();
					}
					// ��¼�³�ʼλ��
					mLastMotionX = x;
					break;
				case MotionEvent.ACTION_MOVE:
					final int deltaX = (int) (mLastMotionX - x);
					mLastMotionX = x;
					if (deltaX < 0) {
						if (mScrollX > 0) {
							scrollBy(Math.max(-mScrollX, deltaX), 0);
						}
					} else if (deltaX > 0) {
						//ȡ�ÿɹ�����������
						final int availableToScroll = getChildAt(
								getChildCount() - 1).getRight()
								- mScrollX - getWidth();
						if (availableToScroll > 0) {
							scrollBy(Math.min(availableToScroll, deltaX), 0);
						}
					}
					break;
				case MotionEvent.ACTION_UP:
					//���㵱ǰ����
					final VelocityTracker velocityTracker = mVelocityTracker;
					velocityTracker.computeCurrentVelocity(1000);
					int velocityX = (int) velocityTracker.getXVelocity();

					if (velocityX > SNAP_VELOCITY && mCurrentScreen > 0) {
						// ��������ߵĽ���
						snapToScreen(mCurrentScreen - 1);
					} else if (velocityX < -SNAP_VELOCITY
							&& mCurrentScreen < getChildCount() - 1) {
						// �������ұߵĽ���
						snapToScreen(mCurrentScreen + 1);
					} else {
						//�������ж��Ľ���
						snapToDestination();
					}

					if (mVelocityTracker != null) {
						mVelocityTracker.recycle();
						mVelocityTracker = null;
					}
					mTouchState = TOUCH_STATE_REST;
					break;
				case MotionEvent.ACTION_CANCEL:
					mTouchState = TOUCH_STATE_REST;
			}
			mScrollX = this.getScrollX();
		} else {
			return false;
		}
		if (bottomBar != null) {
			for (int k = 0; k < bottomBar.length; k++) {
				if (k == mCurrentScreen) {
					bottomBar[k].setBackgroundColor(choseColor);
				} else {
					bottomBar[k].setBackgroundColor(defaultColor);
				}
			}
		}

		return true;
	}

	public void setBottomBarBg(ImageButton[] ib, int choseColor,
							   int defaultColor) {
		this.bottomBar = ib;
		this.choseColor = choseColor;
		this.defaultColor = defaultColor;
	}
	//�������ж��Ľ���
	private void snapToDestination() {
		final int screenWidth = getWidth();
		final int whichScreen = (mScrollX + (screenWidth / 2)) / screenWidth;
		snapToScreen(whichScreen);
	}

	/**
	 * ������Ч����ʾ����
	 */
	public void snapToScreen(int whichScreen) {
		mCurrentScreen = whichScreen;
		final int newX = whichScreen * getWidth();
		final int delta = newX - mScrollX;
		mScroller.startScroll(mScrollX, 0, delta, 0, Math.abs(delta) * 2);
		invalidate();
	}

	/**
	 * ��������Ч����ʾ����
	 */
	public void setToScreen(int whichScreen) {
		// Log.i(LOG_TAG, "set To Screen " + whichScreen);
		mCurrentScreen = whichScreen;
		final int newX = whichScreen * getWidth();
		mScroller.startScroll(newX, 0, 0, 0, 10);
		invalidate();
	}
	//��õ�ǰ��Ļ�ǵڼ���
	public int getCurrentScreen() {
		return mCurrentScreen;
	}
	//�������沼�ָı�ʱ����
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childLeft = 0;
		//�����Ԫ�صĸ���
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() != View.GONE) {
				final int childWidth = child.getMeasuredWidth();
				child.layout(childLeft, 0, childLeft + childWidth,
						child.getMeasuredHeight());
				childLeft += childWidth;
			}
		}
	}
	//ȡ�ò����õ��ĸ߿�
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//��ȡ�����
		final int width = MeasureSpec.getSize(widthMeasureSpec);
		//��ȡ����ȵ�ģʽ
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		if (widthMode != MeasureSpec.EXACTLY) {
			throw new IllegalStateException("error mode.");
		}
		//��ȡ�߶ȵ�ģʽ
		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		if (heightMode != MeasureSpec.EXACTLY) {
			throw new IllegalStateException("error mode.");
		}

		// ��Ԫ�ؽ��������ͬ���ĸߺͿ�
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}
		//������ָ������Ļ
		scrollTo(mCurrentScreen * width, 0);
	}
	//�������������
	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			mScrollX = mScroller.getCurrX();
			scrollTo(mScrollX, 0);
			postInvalidate();
		}
	}
}