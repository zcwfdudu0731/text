package com.android.supermario;

import java.util.ArrayList;
import java.util.List;

import com.android.supermario.R;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Music_infoAdapter extends BaseAdapter {
	// �������ContentProvider(�������ݿ�)
	public ContentResolver cr;
	// ����װ��ѯ���������ļ�����
	public Cursor mCursor;
	// �����б���Ϣ
	public static List<MusicInfomation> musicList;
	public MusicInfomation mi;

	public Context context;

	// ������Ϣ��1����������2�����֣�3������ʱ�䣬4��ר��(ר��ͼƬ��ר�����ƣ�ר��ID[������ȡͼƬ])��5��������С
	public Music_infoAdapter(Context context) {
		this.context = context;
		// ȡ�����ݿ����
		cr = context.getContentResolver();
		//��ʼ�������б�����
		musicList = new ArrayList<MusicInfomation>();
		String[] s1 = new String[] {
				// ������
				MediaStore.Audio.Media.DISPLAY_NAME,
				// ר����
				MediaStore.Audio.Media.ALBUM,
				// ������
				MediaStore.Audio.Media.ARTIST,
				// ����ʱ��
				MediaStore.Audio.Media.DURATION,
				// ������С
				MediaStore.Audio.Media.SIZE,
				// ����ID
				MediaStore.Audio.Media._ID,
				// ����·��
				MediaStore.Audio.Media.DATA };

		// ��ѯ����������Ϣ
		mCursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, s1,
				null, null, null);

		if (mCursor != null) {
			// �ƶ�����һ��
			mCursor.moveToFirst();
			// ��ø����ĸ�������
			for (int i = 0; i < mCursor.getCount(); i++) {
				// ����mp3�ļ�
				if (mCursor.getString(0).endsWith(".mp3")) {

					mi = new MusicInfomation();
					mi.setMusicName(mCursor.getString(0));
					mi.setMusicAlbum(mCursor.getString(1));
					mi.setMusicSinger(mCursor.getString(2));
					mi.setMusicTime(mCursor.getInt(3));
					mi.setMusicSize(mCursor.getInt(4));
					mi.set_id(mCursor.getInt(5));
					mi.setMusicPath(mCursor.getString(6));
					// װ�ص��б���
					musicList.add(mi);
				}
				// �ƶ�����һ��
				mCursor.moveToNext();
			}
		}
	}
	//�������
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return musicList.size();
	}
	//���ָ��Ԫ��
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return musicList.get(arg0);
	}
	//���ָ��Ԫ��
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	//�����ͼ
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		//���ͳ���ͼ
		LayoutInflater li = LayoutInflater.from(context);
		View v = li.inflate(R.layout.musiclist_item, null);

		TextView tv1;
		TextView tv2;
		TextView tv3;
		TextView tv4;
		//��ʼ������Ԫ��
		tv1 = (TextView) v.findViewById(R.id.tv1);
		tv2 = (TextView) v.findViewById(R.id.tv2);
		tv3 = (TextView) v.findViewById(R.id.tv3);
		tv4 = (TextView) v.findViewById(R.id.tv4);
		//Ϊ����Ԫ����������
		tv1.setText(musicList.get(arg0).getMusicName());
		tv2.setText(toTime(musicList.get(arg0).getMusicTime()));
		tv3.setText(musicList.get(arg0).getMusicPath());
		tv4.setText(toMB(musicList.get(arg0).getMusicSize()) + "MB");
		return v;
	}

	/**
	 * ʱ��ת������
	 */
	public static String toTime(int time) {
		time /= 1000;
		int minute = time / 60;
		int second = time % 60;
		minute %= 60;
		//��ʽ��ʱ��
		return String.format(" %02d:%02d ", minute, second);
	}

	/**
	 * �ļ���Сת������Bת��ΪMB
	 */
	public String toMB(int size) {
		float a = (float) size / (float) (1024 * 1024);
		String b = Float.toString(a);
		int c = b.indexOf(".");
		String fileSize = "";
		fileSize += b.substring(0, c + 2);
		return fileSize;
	}

	/**
	 * ����ר��ͼƬ��ʾ,����и���ͼƬ���Ż᷵�أ�����Ϊnull��Ҫע���ж�
	 * 
	 * @param trackId�����ֵ�id
	 * @return ����������String ���͵�ͼƬ��ַ��Ҳ����uri
	 */
	public String getAlbumArt(int trackId) {
		//�������ֵ�id���ר����id
		String mUriTrack = "content://media/external/audio/media/#";
		String[] projection = new String[] { "album_id" };
		String selection = "_id = ?";
		String[] selectionArgs = new String[] { Integer.toString(trackId) };
		Cursor mcCursor = context.getContentResolver().query(
				Uri.parse(mUriTrack), projection, selection, selectionArgs,
				null);
		int album_id = 0;
		if (mcCursor.getCount() > 0 && mcCursor.getColumnCount() > 0) {
			mcCursor.moveToNext();
			album_id = mcCursor.getInt(0);
		}
		mcCursor.close();
		mcCursor = null;

		if (album_id < 0) {
			return null;
		}
		//����ר����id���ר��ͼƬ��uri��ַ
		String mUriAlbums = "content://media/external/audio/albums";
		projection = new String[] { "album_art" };
		mcCursor = context.getContentResolver().query(
				Uri.parse(mUriAlbums + "/" + Integer.toString(album_id)),
				projection, null, null, null);
		String album_art = null;
		if (mcCursor.getCount() > 0 && mcCursor.getColumnCount() > 0) {
			mcCursor.moveToNext();
			album_art = mcCursor.getString(0);
		}
		mcCursor.close();
		mcCursor = null;

		return album_art;
	}

	/**
	 * 1�������� 2������ 3������ʱ�� 4��ר��(ר��ͼƬ��ר�����ƣ�ר��ID[������ȡͼƬ]) 5��������С
	 */
	public class MusicInfomation {

		private int _id;
		private String musicName;
		private String musicSinger;
		private int musicTime;
		private String musicAlbum;
		private int musicSize;
		private String musicPath;
		//ȡ��id
		public int get_id() {
			return _id;
		}
		//����id
		public void set_id(int _id) {
			this._id = _id;
		}
		//ȡ�ø�����
		public String getMusicName() {
			return musicName;
		}
		//���ø�����
		public void setMusicName(String musicName) {
			this.musicName = musicName;
		}
		//ȡ�ø�����
		public String getMusicSinger() {
			return musicSinger;
		}
		//���ø�����
		public void setMusicSinger(String musicSinger) {
			this.musicSinger = musicSinger;
		}
		//ȡ�����ֲ�����ʱ��
		public int getMusicTime() {
			return musicTime;
		}
		//�������ֲ�����ʱ��
		public void setMusicTime(int musicTime) {
			this.musicTime = musicTime;
		}
		//ȡ���������
		public String getMusicAlbum() {
			return musicAlbum;
		}
		//�����������
		public void setMusicAlbum(String musicAlbum) {
			this.musicAlbum = musicAlbum;
		}
		//ȡ�����ִ�С
		public int getMusicSize() {
			return musicSize;
		}
		//�������ִ�С
		public void setMusicSize(int musicSize) {
			this.musicSize = musicSize;
		}
		//ȡ������·��
		public String getMusicPath() {
			return musicPath;
		}
		//��������·��
		public void setMusicPath(String musicPath) {
			this.musicPath = musicPath;
		}
	}
}