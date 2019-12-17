package com.xnx3.j2ee.func;

import java.io.BufferedReader;  
import java.io.BufferedWriter;  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileWriter;  
import java.io.InputStreamReader; 

/**
 * 将mysql的sql文件，转化为 Sqlite可用的sql文件
 * @author 管雷鸣
 * @deprecated
 */
public class MysqlToSqlite {
	 public static void main(String[] args) {  
         
	        try {  
	            read("H:/wangmarket_maven/wangmarket.sql","H:/wangmarket_maven/wangmarket_sqlite.sql");  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	    }  
	  
	    public static void read(String sourcePath,String destionPath) throws Exception {  
	        InputStreamReader read = new InputStreamReader (new FileInputStream(new File(sourcePath)), "UTF-8");   
	        FileWriter fw = new FileWriter(new File(destionPath));  
	        BufferedReader reader = new BufferedReader(read);  
	        BufferedWriter writer = new BufferedWriter(fw);  
	          
	        String s = null;  
	        StringBuilder sb = new StringBuilder();  
	        while((s = reader.readLine()) != null) {  
	            String replace = null;  
	            String result = null;  
	            if(s.contains("SET FOREIGN_KEY_CHECKS=0;")) {  
	                replace = "SET FOREIGN_KEY_CHECKS=0;";  
	                result = "\r\n";  
	            } else if(s.contains("NOT NULL AUTO_INCREMENT")) {  
	                replace = "NOT NULL AUTO_INCREMENT";  
	                result = "PRIMARY KEY NOT NULL";  
	            } else if(s.contains("PRIMARY KEY (`id`)")) {  
	                replace = "";  
	            } else if(s.contains("ENGINE=InnoDB AUTO_INCREMENT") || s.contains(") ENGINE=InnoDB DEFAULT CHARSET=utf8")) {  
	                s = ");";  
	            }   
	              
	            if(s.equals(");")) {  
	                String resultStr = sb.toString();  
	                sb = new StringBuilder();  
	                sb.append(resultStr.substring(0, resultStr.lastIndexOf(",")));  
	            }  
	              
	            if(replace != null && result != null)  
	                s = s.replace(replace, result);  
	            if(!"".equals(replace)) {  
	                sb.append(s).append("\r\n");  
	            }  
	              
	        }  
	        System.out.println(sb.toString());  
	        writer.write(sb.toString());  
	        reader.close();  
	        writer.close();  
	          
	    }  
}
