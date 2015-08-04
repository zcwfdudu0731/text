package com.android.supermario;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class MusicSpecialView extends BaseAdapter {
	public Context context;
	GridView gv_1;
	public static MusicSpecialView msv;

	public void disPlaySpecial(GridView gv, Context context) {
		this.context = context;
		//��ʼ����ͼ
		msv = new MusicSpecialView();
		this.gv_1 = gv;
		this.gv_1.setAdapter(msv);

	}
	//��������ļ�����
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Music_infoAdapter.musicList.size();
	}
	//ȡ��ָ�����ļ�
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return Music_infoAdapter.musicList.get(arg0);
	}
	//ȡ��ָ�����ļ�
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	//���ÿһ��Ԫ�ص���ͼ
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//���ͳ���ͼ
		LayoutInflater lif = LayoutInflater.from(MusicPlayerActivity.context);
		View v = lif.inflate(R.layout.gridspecial_item, null);
		//ÿһ��Ԫ�ذ���һ��ͼƬ��ͼ�������ı���ͼ
		ImageView iv;
		TextView tv1;
		TextView tv2;
		//��ʼ������Ԫ��
		iv = (ImageView) v.findViewById(R.id.gridspecial_view1);
		tv1 = (TextView) v.findViewById(R.id.album_xxx);
		tv2 = (TextView) v.findViewById(R.id.artist_xxx);

		// ��ȡר��ͼƬ·��
		String url = MusicPlayerActivity.music_info
				.getAlbumArt(Music_infoAdapter.musicList.get(position).get_id());
		if (url != null) {
			//����ר����Ӧ��ͼƬ
			iv.setImageURI(Uri.parse(url));
		} else {
			//����Ĭ��ͼƬ
			iv.setImageResource(R.drawable.default_album);
		}
		iv.setAnimation(AnimationUtils.loadAnimation(
				MusicPlayerActivity.context, R.anim.alpha_z));

		// ��������ʾר����
		tv1.setText(Music_infoAdapter.musicList.get(position).getMusicAlbum());
		tv1.setAnimation(AnimationUtils.loadAnimation(
				MusicPlayerActivity.context, R.anim.alpha_y));

		// ��������ʾ������
		tv2.setText(Music_infoAdapter.musicList.get(position).getMusicSinger());
		tv2.setAnimation(AnimationUtils.loadAnimation(
				MusicPlayerActivity.context, R.anim.alpha_y));
		return v;
	}
}