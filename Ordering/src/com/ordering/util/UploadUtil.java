package com.ordering.util;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;

public class UploadUtil {
	
	public static void delDir(File f) {
		// �ж��Ƿ���һ��Ŀ¼, ���ǵĻ�����, ֱ��ɾ��; �����һ��Ŀ¼, �Ƚ����������.
		if (f.isDirectory()) {
			// ��ȡ���ļ�/Ŀ¼
			File[] subFiles = f.listFiles();
			// ������Ŀ¼
			for (File subFile : subFiles) {
				// �ݹ����ɾ�����ļ�: �������һ����Ŀ¼���ļ�, һ�εݹ�Ϳ�ɾ��. �������һ���ǿ�Ŀ¼, ���
				// �ݹ���������ݺ���ɾ��
				delDir(subFile);
			}
		}
		// ɾ����Ŀ¼���ļ�
		f.delete();
	}
	
}
