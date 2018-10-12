package ioc;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
 
public class ComponentScanner {
 
    public static HashMap<String, String> getComponentClassName(
            String packageName) {
    	
    	//查找所有类的名称
        List<String> classes = getClassName(packageName);
        HashMap<String, String> components = new HashMap<String, String>();
        
        try {
            //通过反射获取所有对象的类上的Component注解
            for (String cl : classes) {
                Component comp = Class.forName(cl).getAnnotation(Component.class);
               //若不为空，将该注解的id值与类路径绑定
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
    //先找到包
    public static List<String> getClassName(String packageName) {
       // /F:/space1/ioc/target/classes/ioc
    	String filePath = ClassLoader.getSystemResource("").getPath() 
                + packageName.replace(".", "\\");  
        
        List<String> fileNames = getClassName(filePath, null);
        return fileNames;
    }
    //再到包下找所有类，并获得类名存入数组
    private static List<String> getClassName(String filePath
            , List<String> className) {  
        List<String> myClassName = new ArrayList<String>();  
        File file = new File(filePath);  
        //获得路径下所有文件的路径
        File[] childFiles = file.listFiles();  
        for (File childFile : childFiles) {
        	//若该文件是文件夹，则继续查找子文件夹里的文件，并传入数组
            if (childFile.isDirectory()) {  
                myClassName.addAll(getClassName(childFile.getPath()
                        , myClassName));  
            } else {  
            //若不是文件夹，则获得其路径，砍掉classes以前的目录，砍掉后缀
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