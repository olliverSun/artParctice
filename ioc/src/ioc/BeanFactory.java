package ioc;
import java.lang.reflect.Field; 
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
 
public class BeanFactory {
 
    private HashMap<String, Object> beanPool;
    private HashMap<String, String> components;
    
    public BeanFactory(String packageName) {
        beanPool = new HashMap<>();
        
        scanComponents(packageName);
    }
    
    private void scanComponents(String packageName) {
        components = ComponentScanner
                .getComponentClassName(packageName);
    }
    
    public Object getBean(String id) throws ClassNotFoundException, 
            InstantiationException, IllegalAccessException, 
            NoSuchMethodException, SecurityException, 
            IllegalArgumentException, InvocationTargetException {
        //�����������ھ�ֱ�ӷ��أ��������ھ�ʵ����һ������
        if (beanPool.containsKey(id)) {
            return beanPool.get(id);
        }
        
        if (components.containsKey(id)) {
        	//������ַ�����componets����ǡ����ӳ�䣬�ͽ�ʵ��������һ��OBject��ָ��
            Object bean = Class.forName(components.get(id))
                    .newInstance();
            //�����ö������Ƿ���Ҫע���
            bean = assemblyMember(bean);
            
            beanPool.put(id, bean);
            
            return getBean(id);
        }
        
        throw new ClassNotFoundException();
    }
    
    private Object assemblyMember(Object obj) throws 
            ClassNotFoundException, InstantiationException, 
            IllegalAccessException, NoSuchMethodException, 
            SecurityException, IllegalArgumentException, 
            InvocationTargetException {
        
        Class cl = obj.getClass();
        //���÷����ȡ�����ֶβ��������ж�ÿ���ֶ����Ƿ���Autowireע��
        for (Field f : cl.getDeclaredFields()) {
            Autowire at = f.getAnnotation(Autowire.class);
            
            if (at != null) {
            	//�����ֶ���ע�⣬���ȡ���ֶζ�Ӧ��set����
                Method setMethod = cl.getMethod("set" 
                        + captureName(f.getName()), f.getType());
                   //��ִ�и÷���     
                setMethod.invoke(obj, getBean(at.id()));
            }
        }
        return obj;
    }
    
    public static String captureName(String name) {
        char[] cs=name.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);
    }

}