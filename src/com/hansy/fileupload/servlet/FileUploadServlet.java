package com.hansy.fileupload.servlet;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * 文件上传
 * @author Furious
 *
 */
public class FileUploadServlet extends HttpServlet{

	/**
	 * 设置文件保存的目录
	 */
	private static final String SAVE_PATH="d:/test/";
	
	/**
	 * 限制上传文件的大小为10M
	 */
	private static final long FILE_SIZE=1024*2024*10;
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		  * 工厂：DiskFileItemFactory
//		  * 解析器：ServletFileUpload
//		  * 表单项：FileItem
		DiskFileItemFactory factory=new DiskFileItemFactory();
		
		ServletFileUpload fileUpload=new ServletFileUpload(factory);
		
		//设置请求编码，处理中文文件名
		fileUpload.setHeaderEncoding("utf-8");
		//设置响应编码
		resp.setContentType("text/html;charset=utf-8");
		
		List<FileItem> fileItemList;
		try {
			//得到表单对象
			fileItemList=fileUpload.parseRequest(req);
			
			if(fileItemList==null || fileItemList.size()==0) return;
			
			for(FileItem fileItem:fileItemList){
				//若为普通表单，不做任何处理
				if (fileItem.isFormField()) continue;
				
				long fileSize=fileItem.getSize();
				if (fileSize>FILE_SIZE) throw new RuntimeException("文件过大！");
				
				//得到文件名，由于浏览器的差异，得到的文件名可能包含上传文件的绝对路径，这里只截取文件名部分
				String uploadFileName=fileItem.getName();
				if(uploadFileName.contains("\\"))
					uploadFileName=uploadFileName.substring(uploadFileName.lastIndexOf("\\")+1);
				
				//设置保存的文件名，采用uuid+"-"+文件名的方式
				String fileName=UUID.randomUUID().toString().replaceAll("-", "")+"-"+uploadFileName;
				
				File file=new File(SAVE_PATH+fileName);
				if(!file.getParentFile().exists()) 
					file.getParentFile().mkdirs();
				file.createNewFile();
				try {
					fileItem.write(file);
					resp.getWriter().print("文件上传成功！");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
		
	}
}
