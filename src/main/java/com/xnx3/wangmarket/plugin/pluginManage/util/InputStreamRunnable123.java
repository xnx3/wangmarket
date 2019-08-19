package com.xnx3.wangmarket.plugin.pluginManage.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InputStreamRunnable implements Runnable{
	 private BufferedReader bReader = null;

	    InputStreamRunnable(InputStream is, String type) {
	        try {
	            bReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(is)));
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }

	    public void run() {
	        String line;
	        int num = 1;
	        try {
	            while ((line = bReader.readLine()) != null) {
	                System.out.println("---->"+String.format("%02d",num++)+" "+line);
	            }
	            bReader.close();
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }
}