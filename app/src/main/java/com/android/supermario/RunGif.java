package com.android.supermario;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.view.View;
//���Ŷ�����ͼ��
public class RunGif extends View {
	public Movie mMovie;
	public long mMovieStart;
	//�������ŵı�־
	public static boolean flag = false;
	//���캯�����ڳ�ʼ������
	public RunGif(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mMovie = Movie.decodeStream(getResources().openRawResource(
				R.raw.mediaview_gif1));
	}
	//�Ƿ񲥷Ŷ����ı�־
	public boolean setFlag(boolean b) {
		flag = b;
		return flag;
	}
	public RunGif(Context context, AttributeSet as) {
		super(context, as);
		// TODO Auto-generated constructor stub
		//�����ԵĹ��캯��
		mMovie = Movie.decodeStream(getResources().openRawResource(
				R.raw.mediaview_gif1));
	}
	//���ƶ���
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		long now = android.os.SystemClock.uptimeMillis();
		if (flag == true) {
			if (mMovieStart == 0) {
				// ���ŵ�һ֡
				mMovieStart = now;
			}
			if (mMovie != null) {
				//ȡ��������ʱ��
				int dur = mMovie.duration();
				if (dur == 0) {
					dur = 15000;
				}
				//�����Ҫ��ʾ�ڼ�֡
				int relTime = (int) ((now - mMovieStart) % dur);
				//����Ҫ��ʾ��֡
				mMovie.setTime(relTime);
				//��ʾ
				mMovie.draw(canvas, 90, 30);
				// ������ˢ�µ�ǰView
				invalidate();
			}
		}
	}
}