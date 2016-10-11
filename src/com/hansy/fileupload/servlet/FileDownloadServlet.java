package com.hansy.fileupload.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 下载文件
 * @author Furious
 *
 */
public class FileDownloadServlet extends HttpServlet{
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//要下载文件的绝对路径
		String filePath="d:/test/520ca3bc33804336983ec14a93b1900e-test.rar";
		//下载文件的文件名
		String fileName=filePath.substring(filePath.indexOf("-")+1);
		
		//设置响应类型为二进制文件流
		resp.setContentType("application/octet-stream");
		
		//设置下载文件的显示名称
		resp.setHeader("Content-disposition","attachment;filename=\""+fileName+"\"");
		
		File file=new File(filePath);
		if(!file.exists()) throw new RuntimeException("没有找到对应的文件");
		
		FileInputStream in=new FileInputStream(file);
		
		OutputStream out=resp.getOutputStream();
		
		int len = 0;
		byte[] buf=new byte[1024];
		
		while((len=in.read(buf))!=-1){
			out.write(buf, 0, len);
		}
		out.flush();
		
		if(out != null)
			out.close();
		if(in != null)
			in.close();
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
