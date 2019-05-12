package Main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import filelist_entity.FileList;
import operate.Cnt;
import operate.File_Op;
import operate_ipml.file_Op_impl;
import thread.MyThread;

public class Main {

	
	
	public static void main(String[] args) {
		
//		Cnt cnt=new Cnt();
//		File_Op fo=new file_Op_impl();
//		String res=fo.readDoc("D:\\java-mars\\workspace\\file_exam",cnt);
//		System.out.println(res);
		
		long startTime=System.currentTimeMillis();
		List<MyThread> threadList=new ArrayList<MyThread>();
		Lock lock= new ReentrantLock();
		int allCnt=0;
		FileList fl=new FileList("D:\\java-mars\\workspace\\file_exam");
		List<String> list=fl.getFileList();
		for(String s:list){
			//System.out.println("*** "+s);
			MyThread mt=new MyThread();
			mt.setFileName(s);
			mt.start();
			threadList.add(mt);
			
		}
		for(MyThread mt:threadList){
			try {
				mt.join();
				lock.lock();
				allCnt+=mt.getCnt().getCn();
				lock.unlock();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		long endTime=System.currentTimeMillis();
		System.out.println("检查结束\n总计:"+allCnt+"处\n用时:"+(endTime-startTime)+"ms");
	}

}
