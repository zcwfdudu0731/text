package com.android.supermario;
import android.content.Context;
import android.widget.ListView;
//���ֲ����б�
public class MusicListView {
	ListView lv_1;
	public static Music_infoAdapter m_info_1;
	public void disPlayList(ListView lv, Context context) {
		//ȡ�������ļ�����������
		m_info_1 = new Music_infoAdapter(context);
		this.lv_1 = lv;
		//Ϊ�����б�����������
		this.lv_1.setAdapter(m_info_1);
	}
}