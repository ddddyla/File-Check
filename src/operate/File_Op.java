package operate;

import java.io.File;
import java.util.List;

public interface File_Op {
	//得到fileList中所有文件的type
	public void getType(List<String> fileList);
	//读取doc文件
	public String readDoc(String filePath,Cnt cnt);
	//读取docx文件
	public String readDocx(String filePath,Cnt cnt);
	//读取ppt文件
	public String readPPT(String filePath,Cnt cnt);
	
	public String readPPT2007(String filePath,Cnt cnt);
	
	public String readExcel(String filePath,Cnt cnt);
	//处理文件
	public String deal(String content,String type,Cnt cnt);
	
	public String deal1(String content,String type,Cnt cnt);
	
	public String readPDF(String filePath,Cnt cnt);
	
	public String readTXT(String filePath,Cnt cnt);
	
}
