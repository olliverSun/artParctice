package ioc;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
 
public class ComponentScanner {
 
    public static HashMap<String, String> getComponentClassName(
            String packageName) {
    	
    	//���������������
        List<String> classes = getClassName(packageName);
        HashMap<String, String> components = new HashMap<String, String>();
        
        try {
            //ͨ�������ȡ���ж�������ϵ�Componentע��
            for (String cl : classes) {
                Component comp = Class.forName(cl).getAnnotation(Component.class);
               //����Ϊ�գ�����ע���idֵ����·����
                if (comp != null) {
                    components.put(comp.id(), cl);
                }
            }
        
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return components;
    }
    //���ҵ���
    public static List<String> getClassName(String packageName) {
       // /F:/space1/ioc/target/classes/ioc
    	String filePath = ClassLoader.getSystemResource("").getPath() 
                + packageName.replace(".", "\\");  
        
        List<String> fileNames = getClassName(filePath, null);
        return fileNames;
    }
    //�ٵ������������࣬�����������������
    private static List<String> getClassName(String filePath
            , List<String> className) {  
        List<String> myClassName = new ArrayList<String>();  
        File file = new File(filePath);  
        //���·���������ļ���·��
        File[] childFiles = file.listFiles();  
        for (File childFile : childFiles) {
        	//�����ļ����ļ��У�������������ļ�������ļ�������������
            if (childFile.isDirectory()) {  
                myClassName.addAll(getClassName(childFile.getPath()
                        , myClassName));  
            } else {  
            //�������ļ��У�������·��������classes��ǰ��Ŀ¼��������׺
                String childFilePath = childFile.getPath();  
                childFilePath = childFilePath.substring(childFilePath
                        .indexOf("\\classes") + 9, childFilePath.lastIndexOf("."));  
                childFilePath = childFilePath.replace("\\", ".");  
                myClassName.add(childFilePath);  
            }  
        }  
  
        return myClassName;  
    }
    
    public static void main(String[] args) {
        getComponentClassName("com.oolong.javase.annotation");
    }

}