package operate_ipml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.hslf.usermodel.HSLFSlideShowImpl;
import org.apache.poi.hslf.usermodel.HSLFTextParagraph;
import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xslf.extractor.XSLFPowerPointExtractor;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideShow;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.xmlbeans.XmlException;

import operate.Cnt;
import operate.File_Op;

public class file_Op_impl implements File_Op {

	private List<String> fileList;
	
	@Override
	public void getType(List<String> fileList){
		Set<String> set=new HashSet<String>();
		for(String s:fileList){
			int loc=s.lastIndexOf(".");
			String type=s.substring(loc+1);
			if(!set.contains(type)&&type.length()<=5){
				set.add(type);
			}
		}
		System.out.println("type-size:"+set.size());
		for(String s:set){
			System.out.println(s);
		}
	}


	@Override
	public String readDoc(String filePath,Cnt cnt){
		String res="";
		File file=new File(filePath);
		String content=file.getName()+'\n';
		WordExtractor we=null;
		try {
			FileInputStream is=new FileInputStream(file);
			we = new WordExtractor(is);
			content=we.getText();
			res=deal1(content,".doc",cnt);
			is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	@Override
	public String readDocx(String filePath,Cnt cnt) {
		File file = new File(filePath);
		String content=file.getName()+'\n';
		String res="";
        try {
            FileInputStream fis = new FileInputStream(file);
            XWPFDocument xdoc = new XWPFDocument(fis);
            XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
            content += extractor.getText();
            //System.out.println(content);
            res=deal1(content,".docx", cnt);
            //System.out.println(doc1);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return res;
	}
	
	@Override
	public String readPPT(String filePath,Cnt cnt) {
		/** 加载PPT **/
		HSLFSlideShow ppt;
		String res="";
		try {
			ppt = new HSLFSlideShow(new HSLFSlideShowImpl(filePath));
			 /** 可以 理解为PPT里的每一页 **/
			List<HSLFSlide> slide = ppt.getSlides();
			/** PPT里的每一页的内容  **/
			//List<List<HSLFTextParagraph>> textPara;
			//String text = null;
			for(int i=0;i<slide.size();i++){
	            //读取一张幻灯片的标题
	            //String title=slide.get(i).getTitle();
	            //读取一张幻灯片的内容(包括标题)
	             
	             /* You can then call getTextParagraphs() on these, 
	             * to get their blocks of text. (A list of HSLFTextParagraph
	             * normally holds all the text in a given area of the page,
	             * eg in the title bar, or in a box). 
	             * From the HSLFTextParagraph, you can extract the text,
	             * and check what type of text it is (eg Body, Title).
	             * You can also call getTextRuns(), which will 
	             * return the HSLFTextRuns that make up the TextParagraph.
	             * A HSLFTextRun is a text fragment, having the same character formatting.*/
	             
				//简单粗暴地用了toString()，发现这样居然也可以=.=  
	            StringBuilder sb = new StringBuilder(slide.get(i).getTextParagraphs().toString()); 
	            while(true){
	            	int tmp=sb.indexOf("[");
	            	if(tmp<0)
	            		break;
	            	else 
	            		sb.deleteCharAt(tmp);
	            }
	            while(true){
	            	int tmp=sb.indexOf("]");
	            	if(tmp<0)
	            		break;
	            	else 
	            		sb.deleteCharAt(tmp);
	            }
	            while(true){
	            	int tmp=sb.indexOf(",");
	            	if(tmp<0)
	            		break;
	            	else 
	            		sb.deleteCharAt(tmp);
	            }
	            String content=sb.toString();
	            Pattern pkb = Pattern.compile("[\\s|\t|\n]*"); //为了去除制表符
	            Matcher m = pkb.matcher(content);
	            content = m.replaceAll("");
	            content.trim();
	            //System.out.println(content);
	            
	            String tmp=deal1(content,".ppt",cnt);
	            if(tmp.length()>0)
	            	res+="第"+(i+1)+"页:\n"+tmp;
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public String readPPT2007(String filePath,Cnt cnt)  {
		String res="";
		XMLSlideShow ppt;
		try {
			ppt = new XMLSlideShow(new FileInputStream(filePath));
			int cntPage=0;
			for(XSLFSlide slide : ppt.getSlides()){
				String slidetext ="";
				for (XSLFShape sh : slide.getShapes()) {
		            // name of the shape
		            String name = sh.getShapeName();
		            if (sh instanceof XSLFTextShape) {
		                XSLFTextShape shape = (XSLFTextShape) sh;
		                List<XSLFTextParagraph> textPara = ((XSLFTextShape) sh).getTextParagraphs();
		                
		                for(XSLFTextParagraph tx : textPara){
		                	slidetext +=tx.getText();
		                }
		            }	            
				}
				cntPage++;
				Pattern pkb = Pattern.compile("[\\s|\t|\n]*"); //为了去除制表符
	            Matcher m = pkb.matcher(slidetext);
	            slidetext = m.replaceAll("");
	            slidetext.trim();
				//System.out.println(slidetext);
				String tmp=deal1(slidetext,".pptx",cnt);
				if(tmp.length()>0)
					res+="第"+cntPage+"页:\n"+tmp;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return res;
		//String text = new XSLFPowerPointExtractor(POIXMLDocument.openPackage(filePath)).getText();
		//contain(filePath,text);
	}

	public String deal1(String content,String type,Cnt cnt) {
		String res="";
		
		Map<String,Integer> map= new HashMap<String,Integer >();
		map.put("绝密",0);
		map.put("机密",1);
		map.put("秘密",2);
		map.put("保密",3);
		map.put("涉密",4);
		String[] rows=content.split("\n");
		int cntAll=0;
		for(int r=0;r<rows.length;r++){
			
			if(r>=2){
				String lrow=rows[r-1];
				String nrow=rows[r];
				Pattern pkb = Pattern.compile("[\\s|\t|\r]*"); //为了去除制表符
	            Matcher m = pkb.matcher(lrow);
	            lrow = m.replaceAll("");
	            m=pkb.matcher(nrow);
	            nrow=m.replaceAll("");
	            lrow.trim();
	            nrow.trim();
	            if(nrow.indexOf('密')==0){
	            	if(lrow.lastIndexOf("绝")==lrow.length()-1){
	            		res+="第"+(r-1)+"行: 第"+lrow.length()+"个字处: 绝密"+'\n';
	            		cntAll++;
	            	}else if(lrow.lastIndexOf("机")==lrow.length()-1){
	            		res+="第"+(r-1)+"行: 第"+lrow.length()+"个字处: 机密"+'\n';
	            		cntAll++;
	            	}else if(lrow.lastIndexOf("秘")==lrow.length()-1){
	            		res+="第"+(r-1)+"行: 第"+lrow.length()+"个字处: 秘密"+'\n';
	            		cntAll++;
	            	}else if(lrow.lastIndexOf("保")==lrow.length()-1){
	            		res+="第"+(r-1)+"行: 第"+lrow.length()+"个字处: 保密"+'\n';
	            		cntAll++;
	            	}else if(lrow.lastIndexOf("涉")==lrow.length()-1){
	            		res+="第"+(r-1)+"行: 第"+lrow.length()+"个字处: 涉密"+'\n';
	            		cntAll++;
	            	}
	            }
			}
			int index=0;
			//System.out.println(rows[r]);
			int[] loc=new int[5];
			loc[0]=rows[r].indexOf("绝密",index);
			loc[1]=rows[r].indexOf("机密",index);
			loc[2]=rows[r].indexOf("秘密",index);
			loc[3]=rows[r].indexOf("保密",index);
			loc[4]=rows[r].indexOf("涉密",index);
//			System.out.println("***loc "+loc[0]);
//			System.out.println("***loc "+loc[1]);
//			System.out.println("***loc "+loc[2]);
//			System.out.println("***loc "+loc[3]);
//			System.out.println("***loc "+loc[4]);
			while(true){
				Arrays.sort(loc);
				int froLoc=-1;
				for(int i=0;i<5;i++){
					if(loc[i]>=0){
						froLoc=loc[i];
						loc[i]=-1;
						break;
					}
				}
				if(froLoc<0)
					break;
				String tmp=rows[r].substring(froLoc,froLoc+2);
				//System.out.println(tmp);
				if(type.equals(".ppt")||type.equals(".pptx")){
					if(r==0&&!(type.equals(".ppt")||type.equals(".pptx")))
						res+="文件名中第"+(froLoc+1)+"个字处: "+tmp+'\n';
					else 
						res+="第"+(froLoc+1)+"个字处: "+tmp+'\n';
				}else {
					if(r==0&&!(type.equals(".ppt")||type.equals(".pptx")))
						res+="文件名中第"+(froLoc+1)+"个字处: "+tmp+'\n';
					else
						res+="第"+(r)+"行: 第"+(froLoc+1)+"个字处: "+tmp+'\n';
				}
				cntAll++;
				index=froLoc+1;
				int next=map.get(tmp);
				loc[next]=rows[r].indexOf(tmp,index);
			}
		}
		if(cntAll>0)
			res+="总计: "+cntAll+"处\n";
		cnt.add(cntAll);
		return res;
	}
	
	@Override
	public String deal(String content,String type,Cnt cnt) {
		//System.out.println(content);
		String res="";
		String[] rows=content.split("\n");
		int cntAll=0;
		int lastrow=-1;
		for(int i=0;i<rows.length;i++){
			Pattern pkb = Pattern.compile("\t*"); //为了去除制表符
            Matcher m = pkb.matcher(rows[i]);
            rows[i] = m.replaceAll("");
			rows[i].trim();
			int index=0;
			if(rows[i].indexOf("密")==0){
				if(lastrow==0){
					if(type.equals(".doc"))
						res+="第"+(i)+"行,"+"第"+(rows[i-1].length()-1)+"个字处:绝密\n";
					else
						res+="第"+(i)+"行,"+"第"+(rows[i-1].length())+"个字处:绝密\n";
					cntAll++;
				}
				else if(lastrow==1){
					if(type.equals(".doc"))
						res+="第"+(i)+"行,"+"第"+(rows[i-1].length()-1)+"个字处:机密\n";
					else 
						res+="第"+(i)+"行,"+"第"+(rows[i-1].length())+"个字处:机密\n";
					cntAll++;
				}
				else if(lastrow==2){
					if(type.equals(".doc"))
						res+="第"+(i)+"行,"+"第"+(rows[i-1].length()-1)+"个字处:秘密\n";
					else
						res+="第"+(i)+"行,"+"第"+(rows[i-1].length())+"个字处:秘密\n";
					cntAll++;
				}
				lastrow=-1;
			}
			//res+='\n';
			int lastIndex=rows[i].length()-1;
			if(type.equals(".doc"))
				lastIndex=rows[i].length()-2;
			//System.out.println("*** "+rows[i].lastIndexOf("绝"));
			if(rows[i].lastIndexOf("绝")==lastIndex)
				lastrow=0;
			else if(rows[i].lastIndexOf("机")==lastIndex)
				lastrow=1;
			else if(rows[i].lastIndexOf("秘")==lastIndex)
				lastrow=2;
			
			while(true){
				int[] loc=new int[3];
				loc[0]=rows[i].indexOf("绝密",index);
				loc[1]=rows[i].indexOf("机密",index);
				loc[2]=rows[i].indexOf("秘密",index);
				int flag=-1;
				
				if(loc[0]<loc[1]){   //得出loc0和loc1之间小的
					if(loc[0]<0)
						flag=1;
					else
						flag=0;
				}else if(loc[0]>loc[1]){
					if(loc[1]<0)
						flag=0;
					else
						flag=1;
				}
				
				if(flag<0){   //以上比的结果和loc2比 得出最靠前的一个关键字
					if(loc[2]>=0)
						flag=2;
				}else {
					if(loc[flag]>loc[2]&&loc[2]>=0){
						flag=2;
					}
				}
				
				if(flag>=0){
					index=loc[flag]+1;
					cntAll++;
					if(type.equals(".ppt")||type.equals(".pptx"))
						res+="第"+(loc[flag]+1)+"个字处:";
					else
						res+="第"+(i+1)+"行,"+"第"+(loc[flag]+1)+"个字处:";
					if(flag==0)
						res+="绝密\n";
					else if(flag==1)
						res+="机密\n";
					else 
						res+="秘密\n";
				}else {
					break;
				}
			}
		}
		if(cntAll>0)
			res+="总计"+cntAll+"处\n";
		cnt.add(cntAll);
		return res;
	}

	public String readExcel(String filePath,Cnt cnt){
		String res="";
		FileInputStream inp;
		File file=new File(filePath);
		String text=file.getName()+'\n';
		try {
			inp = new FileInputStream(filePath);
			HSSFWorkbook wb = new HSSFWorkbook(new POIFSFileSystem(inp));
		    ExcelExtractor extractor = new ExcelExtractor(wb);
		    extractor.setFormulasNotResults(true);
		    extractor.setIncludeSheetNames(false);
		    text += extractor.getText();
		    res=deal1(text,"xls",cnt);
		    //contain(filePath,text);
		    //System.out.println("*** "+text);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    return res;
	}

	public String readPDF(String filePath,Cnt cnt){
		File file = new File(filePath);
		String res="";
		PDDocument document = new PDDocument();
		String text=file.getName()+'\n';
        try
        {
        	try {
				document = PDDocument.load(file);
				PDFTextStripper stripper = new PDFTextStripper();  
	            stripper.setSortByPosition( true );
	            stripper.setStartPage(1);stripper.setEndPage(document.getNumberOfPages());
	            text += stripper.getText(document);
	            //System.out.println(text);
	            res=deal1(text,".pdf",cnt);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	return res;
        }
          
        	 
//        	for (int i = 1; i <= document.getNumberOfPages();i++){
//            	//PDPageContentStream contents = new PDPageContentStream(document, page);
//        		stripper.setStartPage(i);stripper.setEndPage(i+1);
//        		String text = stripper.getText(document);
//            }
        
        finally{
            if( document != null )
            {
                try {
					document.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }
	}

	public String readTXT(String filePath,Cnt cnt){
		String encoding="GBK";
		String res="";
        File file=new File(filePath);
        String text ="";
        text += file.getName()+'\n';
        if(file.isFile() && file.exists()){ //判断文件是否存在
            InputStreamReader read;
			try {
				read = new InputStreamReader(
				new FileInputStream(file),encoding);
				//考虑到编码格式
	            BufferedReader bufferedReader = new BufferedReader(read);
	            String lineTxt = null;
	            while((lineTxt = bufferedReader.readLine()) != null){
	            	text += lineTxt;
	            }
	            read.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
        }
        res=deal1(text,".txt",cnt);
        return res;
	}
}
