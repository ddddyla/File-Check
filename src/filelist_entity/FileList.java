package filelist_entity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileList {

	private List<String> fileList;

	public List<String> getFileList() {
		return fileList;
	}

	public void setFileList(List<String> fileList) {
		this.fileList = fileList;
	}

//	判断文件类型
	public boolean legal(String fileName){
		int index=fileName.lastIndexOf(".");
		if(index<0)
			return false;
		String type=fileName.substring(index);
		if(type.equals(".doc")){
			return true;
		}else if(type.equals(".docx")){
			return true;
		}else if(type.equals(".ppt")){
			return true;
		}else if(type.equals(".pptx")){
			return true;
		}else if(type.equals(".xlsx")){
			return true;
		}else if(type.equals(".xls")){
			return true;
		}else if(type.equals(".pdf")){
			return true;
		}else if(type.equals(".txt")){
			return true;
		}else {
			return false;
		}
	}
	
//	DFS扫描文件
	public void scan(File f){
		if(f!=null){
			if(f.isDirectory()){
				File[] fileArray=f.listFiles();
				if(fileArray!=null){
					for(File tmp:fileArray){
						scan(tmp);
					}
				}
			}else{
				if(legal(f.getName())){
					fileList.add(f.getAbsolutePath());
				}
			}
		}
	}
	
	public FileList(String path) {
		super();
		File file=new File(path);
		this.fileList = new ArrayList<String>();
		scan(file);
	}
	
	public static void main(String[] args) {
		FileList fl=new FileList("e:/");
		List<String> list=fl.getFileList();
		System.out.println(list.size());
		for(String s:list){
			System.out.println(s);
		}
	}
}
