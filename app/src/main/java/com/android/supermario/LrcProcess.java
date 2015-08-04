package com.android.supermario;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * �������ļ�����
 */
public class LrcProcess {
	//������б�
	private List<LrcContent> LrcList;
	//���ڴ��ÿһ����
	private LrcContent mLrcContent;
	//��ʼ������
	public LrcProcess() {
		//ʵ������
		mLrcContent = new LrcContent();
		LrcList = new ArrayList<LrcContent>();
	}
	/**
	 * ��ȡ����ļ�������
	 */
	public String readLRC(String song_path) {
		StringBuilder stringBuilder = new StringBuilder();
		//����ļ���mp3�ļ���ͬһĿ¼����������Ҫ��ͬ
		File f = new File(song_path.replace(".mp3", ".lrc"));
		try {
			FileInputStream fis = new FileInputStream(f);
			//��utf-8��ʽ��������
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String s = "";
			while ((s = br.readLine()) != null) {
				// �滻�ַ�
				s = s.replace("[", "");
				s = s.replace("]", "@");
				// ����"@"�ַ�
				String splitLrc_data[] = s.split("@");
				if (splitLrc_data.length > 1) {
					mLrcContent.setLrc(splitLrc_data[1]);
					// ������ȡ�ø���ʱ��
					int LrcTime = TimeStr(splitLrc_data[0]);
					//���ø��ʱ��
					mLrcContent.setLrc_time(LrcTime);
					// ��ӽ��б�����
					LrcList.add(mLrcContent);
					// ��������
					mLrcContent = new LrcContent();
				}
			}
			br.close();
			isr.close();
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			stringBuilder.append("δ�ҵ�����ļ�");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			stringBuilder.append("ľ�ж�ȡ����ʰ���");
		}
		return stringBuilder.toString();
	}
	/**
	 * ��������ʱ�䴦����
	 */
	public int TimeStr(String timeStr) {
		timeStr = timeStr.replace(":", ".");
		timeStr = timeStr.replace(".", "@");
		//ȡ��ʱ����Ϣ��ŵ�������
		String timeData[] = timeStr.split("@");

		// ������֡��벢ת��Ϊ����
		int minute = Integer.parseInt(timeData[0]);
		int second = Integer.parseInt(timeData[1]);
		int millisecond = Integer.parseInt(timeData[2]);

		// ������һ������һ�е�ʱ��ת��Ϊ������
		int currentTime = (minute * 60 + second) * 1000 + millisecond * 10;
		return currentTime;
	}
	public List<LrcContent> getLrcContent() {
		return LrcList;
	}
	/**
	 * ��ø�ʺ�ʱ�䲢���ص���
	 */
	public class LrcContent {
		private String Lrc;
		private int Lrc_time;
		//��ø��
		public String getLrc() {
			return Lrc;
		}
		//���ø��
		public void setLrc(String lrc) {
			Lrc = lrc;
		}
		//��ø�ʶ�Ӧ��ʱ��
		public int getLrc_time() {
			return Lrc_time;
		}
		//���ø�ʶ�Ӧ��ʱ��
		public void setLrc_time(int lrc_time) {
			Lrc_time = lrc_time;
		}
	}
}