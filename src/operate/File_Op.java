package operate;

import java.io.File;
import java.util.List;

public interface File_Op {
	//�õ�fileList�������ļ���type
	public void getType(List<String> fileList);
	//��ȡdoc�ļ�
	public String readDoc(String filePath,Cnt cnt);
	//��ȡdocx�ļ�
	public String readDocx(String filePath,Cnt cnt);
	//��ȡppt�ļ�
	public String readPPT(String filePath,Cnt cnt);
	
	public String readPPT2007(String filePath,Cnt cnt);
	
	public String readExcel(String filePath,Cnt cnt);
	//�����ļ�
	public String deal(String content,String type,Cnt cnt);
	
	public String deal1(String content,String type,Cnt cnt);
	
	public String readPDF(String filePath,Cnt cnt);
	
	public String readTXT(String filePath,Cnt cnt);
	
}
