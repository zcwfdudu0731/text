package com.android.supermario;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
import com.android.supermario.LrcProcess.LrcContent;
/**
 * �Զ���滭��ʣ���������Ч��
 */
public class LrcView extends TextView {
	//��ͼ�Ŀ��
	private float width;
	//��ͼ�ĸ߶�
	private float high;
	//��ǰ���Ÿ�ʵĻ���
	private Paint CurrentPaint;
	//�ǵ�ǰ���Ÿ�ʵĻ���
	private Paint NotCurrentPaint;
	//ÿ�����ֵĸ߶�
	private float TextHigh = 25;
	//�ǵ�ǰ���Ÿ�ʵ���ͼ��С
	private float TextSize = 18;
	//�����ʾ��λ��
	private int Index = 0;
	//�������б�
	private List<LrcContent> mSentenceEntities = new ArrayList<LrcContent>();
	//��ʼ���������б�
	public void setSentenceEntities(List<LrcContent> mSentenceEntities) {
		this.mSentenceEntities = mSentenceEntities;
	}
	//���ֲ�ͬ�����ĳ�ʼ��
	public LrcView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}
	public LrcView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}
	public LrcView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}
	//��ʼ������
	private void init() {
		// TODO Auto-generated method stub
		setFocusable(true);
		// ��������
		CurrentPaint = new Paint();
		CurrentPaint.setAntiAlias(true);
		CurrentPaint.setTextAlign(Paint.Align.CENTER);
		// �Ǹ�������
		NotCurrentPaint = new Paint();
		NotCurrentPaint.setAntiAlias(true);
		NotCurrentPaint.setTextAlign(Paint.Align.CENTER);
	}
	//������ͼ
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if (canvas == null) {
			return;
		}
		//���ø���������ɫ
		CurrentPaint.setColor(Color.argb(210, 251, 248, 29));
		NotCurrentPaint.setColor(Color.argb(140, 255, 255, 255));
		//���õ�ǰ��������Ϊ��С
		CurrentPaint.setTextSize(24);
		CurrentPaint.setTypeface(Typeface.SERIF);
		//���÷ǵ�ǰ��������Ϊ��С
		NotCurrentPaint.setTextSize(TextSize);
		NotCurrentPaint.setTypeface(Typeface.DEFAULT);
		try {
			setText("");
			//��ʾ��ǰ���ŵĸ��
			canvas.drawText(mSentenceEntities.get(Index).getLrc(), width / 2,
					high / 2, CurrentPaint);
			float tempY = high / 2;
			// ��������֮ǰ�ľ���
			for (int i = Index - 1; i >= 0; i--) {
				// ��������
				tempY = tempY - TextHigh;
				//��ʾ���
				canvas.drawText(mSentenceEntities.get(i).getLrc(), width / 2,
						tempY, NotCurrentPaint);
			}
			tempY = high / 2;
			// ��������֮��ľ���
			for (int i = Index + 1; i < mSentenceEntities.size(); i++) {
				// ��������
				tempY = tempY + TextHigh;
				//��ʾ���
				canvas.drawText(mSentenceEntities.get(i).getLrc(), width / 2,
						tempY, NotCurrentPaint);
			}
		} catch (Exception e) {
			setText("δ�ҵ�����ļ�");
		}
	}
	//��ͼ��С�ı�ʱ
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		this.width = w;
		this.high = h;
	}
	//���ø����ʾ��λ��
	public void SetIndex(int index) {
		this.Index = index;
	}
}