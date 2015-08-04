package com.android.supermario;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Scroller;

/**
 * �Զ���View��ģ��android ����Luncher
 */
public class BigDragableLuncher extends ViewGroup {

	// ��ť����ɫ
	int choseColor, defaultColor;
	// �ײ���ť����
	ImageButton[] bottomBar;
	// ����õ��������ԵĶ���
	private Scroller mScroller;
	//


	private int mScrollX = 0;
	// Ĭ����ʾ�ڼ���
	private int mCurrentScreen = 0;

	public int mTouchSlop = 0;

	public BigDragableLuncher(Context context) {
		super(context);
		mScroller = new Scroller(context);
		// �õ�״̬λ
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
		//���ò��ֲ���
		this.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.FILL_PARENT));
	}

	public BigDragableLuncher(Context context, AttributeSet attrs) {
		super(context, attrs);
		mScroller = new Scroller(context);

		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
		//���ò��ֲ���
		this.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.FILL_PARENT));

		TypedArray a = getContext().obtainStyledAttributes(attrs,
				R.styleable.DragableLuncher);
		mCurrentScreen = a.getInteger(
				R.styleable.DragableLuncher_default_screen, 0);
	}
	//���õײ���ť��ɫ
	public void setBottomBarBg(ImageButton[] ib, int choseColor,
			int defaultColor) {
		this.bottomBar = ib;
		this.choseColor = choseColor;
		this.defaultColor = defaultColor;
	}
	//��Ļ����
	public void snapToDestination() {
		final int screenWidth = getWidth();
		//�������볬��1/2��Ļʱ��������һ������
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
		mCurrentScreen = whichScreen;
		final int newX = whichScreen * getWidth();
		mScroller.startScroll(newX, 0, 0, 0, 10);
		invalidate();
	}
	//ȡ�õ�ǰ��Ļλ��
	public int getCurrentScreen() {
		return mCurrentScreen;
	}
	//�ı䲼��ʱ����
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childLeft = 0;

		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			//����������������Ԫ��
			if (child.getVisibility() != View.GONE) {
				final int childWidth = child.getMeasuredWidth();
				child.layout(childLeft, 0, childLeft + childWidth,
						child.getMeasuredHeight());
				childLeft += childWidth;
			}
		}

	}
	//���ݸ߿���Ϣ
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//ȡ�ÿ��ֵ
		final int width = MeasureSpec.getSize(widthMeasureSpec);
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		if (widthMode != MeasureSpec.EXACTLY) {
			throw new IllegalStateException("error mode.");
		}
		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		if (heightMode != MeasureSpec.EXACTLY) {
			throw new IllegalStateException("error mode.");
		}
		//���߿���Ϣ���ݸ���Ԫ��
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}
		scrollTo(mCurrentScreen * width, 0);
	}
	//�����������
	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			mScrollX = mScroller.getCurrX();
			scrollTo(mScrollX, 0);
			postInvalidate();
		}
	}
}
