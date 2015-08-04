package com.android.supermario;

import java.util.ArrayList;
import java.util.List;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import com.android.supermario.LrcProcess.LrcContent;
//���ֲ��ŷ���
public class ControlPlay extends Service {
	//ý�岥����
	public static MediaPlayer myMediaPlayer;
	public MusicPlayerActivity c_ma = new MusicPlayerActivity();
	//��ʿ�����
	public LrcProcess mLrcProcess;
	public LrcView mLrcView;
	public static int playing_id = 0; 

	// ��ʼ����ʼ���ֵ
	private int index = 0;
	// ��ʼ����������ʱ��ı���
	private int CurrentTime = 0;
	// ��ʼ��������ʱ��ı���
	private int CountTime = 0;
	// ��������
	private List<LrcContent> lrcList = new ArrayList<LrcContent>();
	//������Ϣ
	public static Music_infoAdapter m_in;
	Handler handler = new Handler();
	public boolean playFlag = true;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	//���񴴽�ʱ����
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		initMediaSource(initMusicUri(0));
	}
	//��������ʱ����
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

		super.onDestroy();
		if (myMediaPlayer != null) {
			//�ͷ���Դ
			myMediaPlayer.stop();
			myMediaPlayer.release();
			myMediaPlayer = null;
		}
	}
	//��������ʱ����
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		//��ÿ��Ʊ�־��
		String playFlag = intent.getExtras().getString("control");
		//����/��ͣ
		if ("play".equals(playFlag)) {
			playMusic();
		} else if ("next".equals(playFlag)) {
			//������һ��
			playNext();
		} else if ("front".equals(playFlag)) {
			//����ǰһ��
			playFront();
		} else if ("listClick".equals(playFlag)) {
			//����ָ������
			playing_id = intent.getExtras().getInt("musicId_1");
			initMediaSource(initMusicUri(playing_id));
			playMusic();
		} else if ("gridClick".equals(playFlag)) {
			//����ָ������
			playing_id = intent.getExtras().getInt("musicId_2");
			initMediaSource(initMusicUri(playing_id));
			playMusic();
		}
	}
	/**
	 * ��ʼ��ý�����
	 * 
	 * @param mp3Path
	 *            mp3·��
	 */
	public void initMediaSource(String mp3Path) {
		//��������uri��ַ
		Uri mp3Uri = Uri.parse(mp3Path);
		if (myMediaPlayer != null) {
			myMediaPlayer.stop();
			myMediaPlayer.reset();
			myMediaPlayer = null;
		}
		// ΪmyMediaPlayer��������
		myMediaPlayer = MediaPlayer.create(this, mp3Uri);
	}

	/**
	 * �����б�ڼ��еĸ���·��
	 * 
	 * @param _id
	 *            ��ʾ������ţ���0��ʼ
	 */
	public String initMusicUri(int _id) {
		playing_id = _id;
		String s;
		// �ж��б���б��Ȳ�Ϊ��ʱ�Ż�ȡ
		if (Music_infoAdapter.musicList != null
				&& Music_infoAdapter.musicList.size() != 0) {
			s = Music_infoAdapter.musicList.get(_id).getMusicPath();
			return s;
		} else {
			// ���򷵻ؿ��ַ���
			return "";
		}
	}

	/**
	 * ���ֲ��ŷ��������Ҵ�����ͣ����
	 */
	public void playMusic() {

		// �жϸ�������Ϊ��

		if (myMediaPlayer != null && Music_infoAdapter.musicList.size() != 0) {
			if (myMediaPlayer.isPlaying()) {
				// ������ͣ
				myMediaPlayer.pause();
				// ��ͣ����GIF����
				MusicPlayerActivity.runEql.setFlag(false);
				MusicPlayerActivity.runEql.invalidate();
				// �������Ű�ť����
				MusicPlayerActivity.play_ImageButton
						.setBackgroundResource(R.drawable.play_button);
				// ȡ��֪ͨ
				MusicPlayerActivity.mNotificationManager.cancel(1);
			} else {
				myMediaPlayer.start();

				// ��ʼ���������
				mLrcProcess = new LrcProcess();
				// ��ȡ����ļ�
				mLrcProcess.readLRC(Music_infoAdapter.musicList.get(playing_id)
						.getMusicPath());
				// ���ش����ĸ���ļ�
				lrcList = mLrcProcess.getLrcContent();
				MusicPlayerActivity.lrc_view.setSentenceEntities(lrcList);
				// �л���������ʾ���
				MusicPlayerActivity.lrc_view.setAnimation(AnimationUtils
						.loadAnimation(ControlPlay.this, R.anim.alpha_z));
				// �����߳�
				mHandler.post(mRunnable);

				// ��������
				MusicPlayerActivity.play_ImageButton
						.setBackgroundResource(R.drawable.pause_button);
				// �����̸߳���SeekBar
				startSeekBarUpdate();
				// ��������GIF����
				MusicPlayerActivity.runEql.setFlag(true);
				MusicPlayerActivity.runEql.invalidate();

				// ���¸������ŵڼ���
				int x = playing_id + 1;
				MusicPlayerActivity.music_number.setText("" + x + "/"
						+ Music_infoAdapter.musicList.size());

				// ��ȡ.mp3�ַ���
				String a = Music_infoAdapter.musicList.get(playing_id)
						.getMusicName();
				int b = a.indexOf(".mp3");
				String c = a.substring(0, b);
				// �л����������¸�����
				MusicPlayerActivity.music_Name.setText(c);
				MusicPlayerActivity.music_Name.setAnimation(AnimationUtils
						.loadAnimation(ControlPlay.this, R.anim.translate_z));

				// ����������ר����
				MusicPlayerActivity.music_Album
						.setText(Music_infoAdapter.musicList.get(playing_id)
								.getMusicAlbum());
				MusicPlayerActivity.music_Album.setAnimation(AnimationUtils
						.loadAnimation(ControlPlay.this, R.anim.alpha_y));

				// ���¸�����
				MusicPlayerActivity.music_Artist
						.setText(Music_infoAdapter.musicList.get(playing_id)
								.getMusicSinger());

				// ���¸���ʱ��
				MusicPlayerActivity.time_right.setText(Music_infoAdapter
						.toTime(Music_infoAdapter.musicList.get(playing_id)
								.getMusicTime()));

				// ��ȡר��ͼƬ·��
				String url = MusicPlayerActivity.music_info
						.getAlbumArt(Music_infoAdapter.musicList.get(
								ControlPlay.playing_id).get_id());
				if (url != null) {

					// ��ʾ��ȡ��ר��ͼƬ
					MusicPlayerActivity.music_AlbumArt.setImageURI(Uri
							.parse(url));
					MusicPlayerActivity.music_AlbumArt
							.setAnimation(AnimationUtils.loadAnimation(
									MusicPlayerActivity.context,
									R.anim.alpha_z));

					// ����֪ͨ,����������������Ҽ�֪ͨͼ��
					MusicPlayerActivity.setNotice(c, c,
							Music_infoAdapter.musicList.get(playing_id)
									.getMusicSinger(), R.drawable.notice_icon);

					try {
						/* Ϊ��Ӱ����λͼ */
						Bitmap mBitmap = BitmapFactory.decodeFile(url);
						MusicPlayerActivity.reflaction
								.setImageBitmap(MusicPlayerActivity
										.createReflectedImage(mBitmap));
						MusicPlayerActivity.reflaction
								.setAnimation(AnimationUtils.loadAnimation(
										MusicPlayerActivity.context,
										R.anim.alpha_z));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					MusicPlayerActivity.music_AlbumArt
							.setImageResource(R.drawable.album);
					MusicPlayerActivity.music_AlbumArt
							.setAnimation(AnimationUtils.loadAnimation(
									MusicPlayerActivity.context,
									R.anim.alpha_y));

					// ����֪ͨ,����������������Ҽ�֪ͨͼ��
					MusicPlayerActivity.setNotice(c, c,
							Music_infoAdapter.musicList.get(playing_id)
									.getMusicSinger(), R.drawable.notice_icon);

					try {
						/* Ϊ��Ӱ����λͼ */
						Bitmap mBitmap = ((BitmapDrawable) getResources()
								.getDrawable(R.drawable.album)).getBitmap();
						MusicPlayerActivity.reflaction
								.setImageBitmap(MusicPlayerActivity
										.createReflectedImage(mBitmap));
						MusicPlayerActivity.reflaction
								.setAnimation(AnimationUtils.loadAnimation(
										MusicPlayerActivity.context,
										R.anim.alpha_z));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			/**
			 * ���������Ƿ����
			 */
			myMediaPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub

					// �����굱ǰ�������Զ�������һ��
					playNext();
				}
			});

		} else {
			Toast.makeText(ControlPlay.this, "û�����ֻ����ҵ�����...",
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * ������һ��
	 */
	public void playNext() {

		// �жϸ�������Ϊ��
		if (Music_infoAdapter.musicList.size() != 0) {
			// ����������һ����һֱ�������һ��
			if (playing_id == Music_infoAdapter.musicList.size() - 1) {
				playing_id = Music_infoAdapter.musicList.size() - 1;
				Toast.makeText(ControlPlay.this, "�Ѿ������һ������",
						Toast.LENGTH_SHORT).show();

				MusicPlayerActivity.play_ImageButton
						.setBackgroundResource(R.drawable.play_button);
				MusicPlayerActivity.mNotificationManager.cancel(1);

			} else {
				initMediaSource(initMusicUri(++playing_id));
				playMusic();
			}
		} else {
			Toast.makeText(ControlPlay.this, "û���ҵ�������", Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * ������һ��
	 */
	public void playFront() {

		// �жϸ�������Ϊ��
		if (Music_infoAdapter.musicList.size() != 0) {
			// ������˵�һ����һֱ���ŵ�һ��
			if (playing_id == 0) {
				playing_id = 0;
				Toast.makeText(ControlPlay.this, "���ھ��ǵ�һ��Ŷ��",
						Toast.LENGTH_SHORT).show();
			} else {
				initMediaSource(initMusicUri(--playing_id));
				playMusic();
			}
		} else {
			Toast.makeText(ControlPlay.this, "ľ���ҵ���������", Toast.LENGTH_SHORT)
					.show();
		}
	}

	public void startSeekBarUpdate() {
		// TODO Auto-generated method stub
		handler.post(start);
	}

	Runnable start = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			handler.post(updatesb);
		}

	};

	Runnable updatesb = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			// ��ȡSeekBar�߶����ǵ�ʱ��
			MusicPlayerActivity.play_time = myMediaPlayer
					.getCurrentPosition();

			// ������䵱ǰ��ȡ�Ľ���
			MusicPlayerActivity.seekbar
					.setProgress(MusicPlayerActivity.play_time);
			// SeekBar�����ֵ������ʱ��
			MusicPlayerActivity.seekbar.setMax(Music_infoAdapter.musicList
					.get(playing_id).getMusicTime());

			// �߳��ӳ�1000��������
			handler.postDelayed(updatesb, 1000);
		}
	};

	Handler mHandler = new Handler();
	// ��ʹ����߳�
	Runnable mRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			MusicPlayerActivity.lrc_view.SetIndex(LrcIndex());
			MusicPlayerActivity.lrc_view.invalidate();
			mHandler.postDelayed(mRunnable, 100);
		}
	};

	/**
	 * ���ͬ��������
	 */
	public int LrcIndex() {
		if (myMediaPlayer.isPlaying()) {
			// ��ø����������ĵ�ʱ��
			CurrentTime = myMediaPlayer.getCurrentPosition();
			// ��ø�����ʱ�䳤��
			CountTime = myMediaPlayer.getDuration();
		}
		if (CurrentTime < CountTime) {

			for (int i = 0; i < lrcList.size(); i++) {
				if (i < lrcList.size() - 1) {
					if (CurrentTime < lrcList.get(i).getLrc_time() && i == 0) {
						index = i;
					}
					if (CurrentTime > lrcList.get(i).getLrc_time()
							&& CurrentTime < lrcList.get(i + 1).getLrc_time()) {
						index = i;
					}
				}
				if (i == lrcList.size() - 1
						&& CurrentTime > lrcList.get(i).getLrc_time()) {
					index = i;
				}
			}
		}
		return index;
	}

}