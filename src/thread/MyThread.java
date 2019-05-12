package thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import operate.Cnt;
import operate.File_Op;
import operate_ipml.file_Op_impl;

public class MyThread extends Thread {

	private String fileName;
	
	private Cnt cnt=new Cnt();
	
	Lock lock= new ReentrantLock();
	
	public Cnt getCnt() {
		return cnt;
	}

	public void setCnt(Cnt cnt) {
		this.cnt = cnt;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void run(){
		File_Op fo=new file_Op_impl();
		int index=fileName.lastIndexOf(".");
		String type=fileName.substring(index);
		
		if(type.equals(".doc")){
			String res=fo.readDoc(fileName,cnt);
			lock.lock();
			System.out.println("文件路径:"+fileName+'\n');
			System.out.println(res);
			lock.unlock();
		}else if(type.equals(".docx")){
			String res=fo.readDocx(fileName,cnt);
			lock.lock();
			System.out.println("文件路径:"+fileName+'\n');
			System.out.println(res);
			lock.unlock();
		}else if(type.equals(".ppt")){
			String res=fo.readPPT(fileName,cnt);
			lock.lock();
			System.out.println("文件路径:"+fileName+'\n');
			System.out.println(res);
			lock.unlock();
		}else if(type.equals(".pptx")){
			String res=fo.readPPT2007(fileName,cnt);
			lock.lock();
			System.out.println("文件路径:"+fileName+'\n');
			System.out.println(res);
			lock.unlock();
		}else if(type.equals(".xls")){
			String res=fo.readExcel(fileName,cnt);
			lock.lock();
			System.out.println("文件路径:"+fileName+'\n');
			System.out.println(res);
			lock.unlock();
		}else if(type.equals(".pdf")){
			String res=fo.readPDF(fileName, cnt);
			lock.lock();
			System.out.println("文件路径:"+fileName+'\n');
			System.out.println(res);
			lock.unlock();
		}else if(type.equals(".txt")){
			String res=fo.readTXT(fileName, cnt);
			lock.lock();
			System.out.println("文件路径:"+fileName+'\n');
			System.out.println(res);
			lock.unlock();
		}
	}
}
