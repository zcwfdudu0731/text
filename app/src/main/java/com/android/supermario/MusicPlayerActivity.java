package com.android.supermario;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
/**
 * ���ֲ��Ž�������
 * ǿ��������AndroidManifest������Activity��Screen OrientationΪportrait
 */
public class MusicPlayerActivity extends Activity {
	//������ť��ǰһ�ס����š���һ��
	ImageButton left_ImageButton;
	public static ImageButton play_ImageButton;
	ImageButton right_ImageButton;
	//�����б�
	ListView musicListView;
	//ר���б�
	GridView musicGridView;
	//������
	public static Context context;
	// ��ʼ����ʼ���ֵ
	public int Index = 0;
	// Ϊ��̨����֪ͨ��������
	public static NotificationManager mNotificationManager;
	// ��SeekBar�͸�������TextView
	public static SeekBar seekbar;
	public static TextView time_left;
	public static TextView time_right;
	public static TextView music_Name;
	public static LrcView lrc_view;
	public static TextView music_Album;
	public static TextView music_Artist;
	public static ImageView music_AlbumArt;
	public static TextView music_number;
	//��ද��
	public static RunGif runEql;
	// Ϊ��Ӱ��������
	public RelativeLayout relativeflac;
	public static ImageView reflaction;
	// Ϊ����ʱ��Ͳ���ʱ�䶨�徲̬����
	public static int song_time = 0;
	public static int play_time = 0;
	// Ϊ��Music_infoAdapter������̬����
	public static Music_infoAdapter music_info;
	// ��������ҳ�����
	private BigDragableLuncher bigPage;
	private DragableLuncher smallPage;
	// ������ť����Ӧ��ͼƬ
	private ImageButton[] blind_btn = new ImageButton[3];
	private int[] btn_id = new int[] { R.id.imageButton1, R.id.imageButton2,
			R.id.imageButton3 };
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		context = this;
		bigPage = (BigDragableLuncher) findViewById(R.id.all_space);
		//�м们��ģ��
		smallPage = (DragableLuncher) findViewById(R.id.space);
		// ��GIF��������
		runEql = (RunGif) findViewById(R.id.runGif1);
		// ��Ӱ����
		relativeflac = (RelativeLayout) findViewById(R.id.relativelayout1);
		reflaction = (ImageView) findViewById(R.id.inverted_view);
		// ����������ϵͳ����
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// �󶨸����б����
		musicListView = (ListView) findViewById(R.id.listView1);
		new MusicListView().disPlayList(musicListView, this);
		// ����ȡ�ĸ������Էŵ���ǰ��������
		music_info = new Music_infoAdapter(this);
		// ��ר���б����
		musicGridView = (GridView) findViewById(R.id.gridview1);
		new MusicSpecialView().disPlaySpecial(musicGridView, this);
		// ���������б����¼�
		musicListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				//�򿪲������ַ���
				Intent play_1 = new Intent(MusicPlayerActivity.this,
						ControlPlay.class);
				//�����Ʋ������ݸ����ֲ��ŷ���
				play_1.putExtra("control", "listClick");
				play_1.putExtra("musicId_1", arg2);
				startService(play_1);
				//����󶯻���ת���Ž���
				bigPage.setAnimation(AnimationUtils.loadAnimation(
						MusicPlayerActivity.this, R.anim.alpha_x));
				bigPage.setToScreen(1);
				blind_btn[1]
						.setBackgroundResource(R.drawable.big_button_pressed);
				blind_btn[0].setBackgroundResource(R.drawable.big_button_style);
			}
		});
		// ����ר���б����¼�
		musicGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				//�����ֲ��ŷ���
				Intent play_2 = new Intent(MusicPlayerActivity.this,
						ControlPlay.class);
				//����ר����Ϣ�Ϳ�����Ϣ
				play_2.putExtra("control", "gridClick");
				play_2.putExtra("musicId_2", arg2);
				startService(play_2);
				// ����󶯻���ת���Ž���
				bigPage.setAnimation(AnimationUtils.loadAnimation(
						MusicPlayerActivity.this, R.anim.alpha_x));
				bigPage.setToScreen(1);
				blind_btn[1].setBackgroundResource(R.drawable.big_button_pressed);
				blind_btn[2].setBackgroundResource(R.drawable.big_button_style);
			}
		});
		// Сҳ��������ִ�ж���
		smallPage.isOpenTouchAnima(true);
		// ��ImageButton
		for (int k = 0; k < blind_btn.length; k++) {
			blind_btn[k] = (ImageButton) findViewById(btn_id[k]);
		}
		// ���ð�ť��ѡ����ɫ��Ĭ����ɫ
		bigPage.setBottomBarBg(blind_btn, Color.GREEN, Color.YELLOW);
		// �жϵ�����ĸ���ť��ִ����ת����
		android.view.View.OnClickListener ocl = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				//�����б�
				case R.id.imageButton1:
					bigPage.setAnimation(AnimationUtils.loadAnimation(
							MusicPlayerActivity.this, R.anim.alpha_x));
					bigPage.setToScreen(0);
					//���õ�ǰ��ť��״̬Ϊ����
					v.setPressed(true);
					//����������ť�ı���ͼƬ
					blind_btn[0]
							.setBackgroundResource(R.drawable.big_button_pressed);
					blind_btn[1]
							.setBackgroundResource(R.drawable.big_button_style);
					blind_btn[2]
							.setBackgroundResource(R.drawable.big_button_style);
					break;
				//���ڲ���
				case R.id.imageButton2:
					bigPage.setAnimation(AnimationUtils.loadAnimation(
							MusicPlayerActivity.this, R.anim.alpha_x));
					bigPage.setToScreen(1);
					//���õ�ǰ��ť��״̬Ϊ����
					v.setPressed(true);
					//����������ť�ı���ͼƬ
					blind_btn[1]
							.setBackgroundResource(R.drawable.big_button_pressed);
					blind_btn[0]
							.setBackgroundResource(R.drawable.big_button_style);
					blind_btn[2]
							.setBackgroundResource(R.drawable.big_button_style);
					break;
				case R.id.imageButton3:
					bigPage.setAnimation(AnimationUtils.loadAnimation(
							MusicPlayerActivity.this, R.anim.alpha_x));
					bigPage.setToScreen(2);
					//���õ�ǰ��ť��״̬Ϊ����
					v.setPressed(true);
					//����������ť�ı���ͼƬ
					blind_btn[2]
							.setBackgroundResource(R.drawable.big_button_pressed);
					blind_btn[1]
							.setBackgroundResource(R.drawable.big_button_style);
					blind_btn[0]
							.setBackgroundResource(R.drawable.big_button_style);
				}
			}
		};
		// ��ʼ������
		blind_btn[1].setBackgroundResource(R.drawable.big_button_pressed);
		// ������ť�ֱ�ָ��������
		blind_btn[0].setOnClickListener(ocl);
		blind_btn[1].setOnClickListener(ocl);
		blind_btn[2].setOnClickListener(ocl);
		// ��ʼ����ť
		left_ImageButton = (ImageButton) findViewById(R.id.ib1);
		play_ImageButton = (ImageButton) findViewById(R.id.ib2);
		right_ImageButton = (ImageButton) findViewById(R.id.ib3);
		//��ʼ����������Ԫ��
		time_left = (TextView) findViewById(R.id.time_tv1);
		time_right = (TextView) findViewById(R.id.time_tv2);
		music_Name = (TextView) findViewById(R.id.music_name);
		music_Album = (TextView) findViewById(R.id.music_album);
		music_Artist = (TextView) findViewById(R.id.music_artist);
		seekbar = (SeekBar) findViewById(R.id.player_seekbar);
		lrc_view = (LrcView) findViewById(R.id.LyricShow);
		music_AlbumArt = (ImageView) findViewById(R.id.music_AlbumArt);
		music_number = (TextView) findViewById(R.id.music_number);
		
		// �жϸ�������Ϊ�ղ��Һ�׺Ϊ.mp3
		if (music_info.getCount() > 0
				&& Music_infoAdapter.musicList.get(ControlPlay.playing_id)
						.getMusicName().endsWith(".mp3")) {
			// ��ʾ��ȡ�ĸ���ʱ��
			time_right.setText(Music_infoAdapter
					.toTime(Music_infoAdapter.musicList.get(
							ControlPlay.playing_id).getMusicTime()));
			// ��ȡ.mp3�ַ���
			String a = Music_infoAdapter.musicList.get(ControlPlay.playing_id)
					.getMusicName();
			int b = a.indexOf(".mp3");
			String c = a.substring(0, b);
			// ��ʾ��ȡ�ĸ�����
			music_Name.setText(c);
			music_Name.setAnimation(AnimationUtils.loadAnimation(
					MusicPlayerActivity.this, R.anim.translate_z));

			// ��ʾ���ŵ�ǰ�ڼ��׺͸�������
			int x = ControlPlay.playing_id + 1;
			music_number.setText("" + x + "/"
					+ Music_infoAdapter.musicList.size());

			// ��ʾ��ȡ��������
			music_Artist.setText(Music_infoAdapter.musicList.get(
					ControlPlay.playing_id).getMusicSinger());
			// ��ȡר��ͼƬ·��
			String url = MusicPlayerActivity.music_info
					.getAlbumArt(Music_infoAdapter.musicList.get(
							ControlPlay.playing_id).get_id());
			if (url != null) {
				// ��ʾ��ȡ��ר��ͼƬ
				music_AlbumArt.setImageURI(Uri.parse(url));
				music_AlbumArt.setAnimation(AnimationUtils.loadAnimation(
						context, R.anim.alpha_z));
				try {
					/* Ϊ��Ӱ����λͼ */
					Bitmap mBitmap = BitmapFactory.decodeFile(url);
					reflaction.setImageBitmap(createReflectedImage(mBitmap));
					reflaction.setAnimation(AnimationUtils.loadAnimation(
							context, R.anim.alpha_z));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				//������ʾΪĬ��ͼƬ
				music_AlbumArt.setImageResource(R.drawable.album);
				music_AlbumArt.setAnimation(AnimationUtils.loadAnimation(
						context, R.anim.alpha_z));
				try {
					/* Ϊ��Ӱ����λͼ */
					Bitmap mBitmap = ((BitmapDrawable) getResources()
							.getDrawable(R.drawable.album)).getBitmap();
					reflaction.setImageBitmap(createReflectedImage(mBitmap));
					reflaction.setAnimation(AnimationUtils.loadAnimation(
							context, R.anim.alpha_z));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			Toast.makeText(MusicPlayerActivity.this, "�ֻ���ľ���ҵ�����Ŷ��",
					Toast.LENGTH_LONG).show();
		}
		/**
		 * �����϶�SeekBar�¼�
		 */
		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				// �ж��û��Ƿ���SeekBar���Ҳ�Ϊ�ղ�ִ��
				if (fromUser && ControlPlay.myMediaPlayer != null) {
					ControlPlay.myMediaPlayer.seekTo(progress);
				}
				time_left.setText(Music_infoAdapter.toTime(progress));
			}
		});
		/**
		 * ��������һ�ס���ʵ�ֹ���
		 */
		left_ImageButton.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//�����ֲ��ŷ���
				Intent play_left = new Intent(MusicPlayerActivity.this,
						ControlPlay.class);
				play_left.putExtra("control", "front");
				startService(play_left);
			}
		});
		/**
		 * ���������š���ʵ�ֹ���
		 */
		play_ImageButton.setOnClickListener(new ImageButton.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//�����ֲ��ŷ���
				Intent play_center = new Intent(MusicPlayerActivity.this,
						ControlPlay.class);
				play_center.putExtra("control", "play");
				startService(play_center);
			}

		});
		/**
		 * ��������һ�ס���ʵ�ֹ���
		 */
		right_ImageButton.setOnClickListener(new ImageButton.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//�����ֲ��ŷ���
				Intent play_right = new Intent(MusicPlayerActivity.this,
						ControlPlay.class);
				play_right.putExtra("control", "next");
				startService(play_right);
			}
		});

	}
	/**
	 * ��Ӱ��ʵ�ַ���
	 * 
	 * @param originalBitmap
	 * @return
	 */
	static Bitmap createReflectedImage(Bitmap originalBitmap) {
		// TODO Auto-generated method stub

		// ͼƬ�뵹Ӱ�������
		final int reflectionGap = 4;
		// ͼƬ�Ŀ��
		int width = originalBitmap.getWidth();
		// ͼƬ�ĸ߶�
		int height = originalBitmap.getHeight();

		Matrix matrix = new Matrix();
		// ͼƬ���ţ�x���Ϊԭ����1����y��Ϊ-1��,ʵ��ͼƬ�ķ�ת
		matrix.preScale(1, -1);
		// ������ת���ͼƬBitmap����ͼƬ����ԭͼ��һ�롣
		Bitmap reflectionBitmap = Bitmap.createBitmap(originalBitmap, 0,
				height / 2, width, height / 2, matrix, false);
		// ������׼��Bitmap���󣬿��ԭͼһ�£�����ԭͼ��1.5����
		Bitmap withReflectionBitmap = Bitmap.createBitmap(width, (height
				+ height / 2 + reflectionGap), Config.ARGB_8888);

		// ���캯������Bitmap����Ϊ����ͼƬ�ϻ�ͼ
		Canvas canvas = new Canvas(withReflectionBitmap);
		// ��ԭʼͼƬ
		canvas.drawBitmap(originalBitmap, 0, 0, null);

		// ���������
		Paint defaultPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);

		// ����ӰͼƬ
		canvas.drawBitmap(reflectionBitmap, 0, height + reflectionGap, null);

		// ʵ�ֵ�ӰЧ��
		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0,
				originalBitmap.getHeight(), 0,
				withReflectionBitmap.getHeight(), 0x70ffffff, 0x00ffffff,
				TileMode.MIRROR);
		paint.setShader(shader);
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));

		// ����Ч��
		canvas.drawRect(0, height, width, withReflectionBitmap.getHeight(),
				paint);

		return withReflectionBitmap;
	}

	/**
	 * ���¡����ؼ�������ʾ�û��Ƿ��˳�����
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			Dialog dialog = new MyDialog(MusicPlayerActivity.this,
					R.style.MyDialog);
			// ���ô����Ի�������ĵط�ȡ���Ի���
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * ��̨��ʾ����֪ͨ�ķ���
	 * ��Ҫ��AndroidManifest��C_MusicPlayerActivity�����android:launchMode=
	 * "singleTop"�ſ�����ȫ�˳�
	 * 
	 * @param tickerText
	 *            ����ĸ�����
	 * @param title
	 * @param content
	 * @param drawable
	 *            ͼƬ·��
	 */
	public static void setNotice(String tickerText, String title,
			String content, int drawable) {
		// ����һ��֪ͨ���󣬴�����Ӧ�Ĳ���
		Notification notification = new Notification(drawable, tickerText,
				System.currentTimeMillis());
		// ����֪ͨ���ܱ����
		notification.flags = Notification.FLAG_NO_CLEAR;
		// ��װ��Intent��ת����ϵͳ������ʲôʱ��������תIntent
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				new Intent(context, MusicPlayerActivity.class), 0);
		// notification.setLatestEventInfo(this, "֪ͨ����","֪ͨ����", intent);
		notification.setLatestEventInfo(context, title, content, contentIntent);
		// ����֪ͨ,����Ӧ�÷ŵ���󣬷���ᱨ��,��ʾ��Notification����һ��Ψһ��ID��1
		mNotificationManager.notify(1, notification);
	}

	/**
	 * �Զ���Ի������
	 */
	class MyDialog extends Dialog {
		Context context;
		//���캯��
		public MyDialog(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			this.context = context;
		}
		//���캯��
		public MyDialog(Context context, int theme) {
			super(context, theme);
			// TODO Auto-generated constructor stub
			this.context = context;
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			this.setContentView(R.layout.dialog);
			//�˳���ť�ͷ��ذ�ť
			Button exit_button, return_button;
			//��ʼ��
			exit_button = (Button) findViewById(R.id.exit_button2);
			return_button = (Button) findViewById(R.id.return_button3);

			// ���������˳�Ӧ�ó���
			exit_button.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//�ص����ֲ��ŷ���
					stopService(new Intent(MusicPlayerActivity.this,
							ControlPlay.class));
					mNotificationManager.cancel(1);
					//�˳�����
					System.exit(0);
				}
			});

			// ����
			return_button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//�رնԻ���
					dismiss();
				}
			});
		}
	}

}
