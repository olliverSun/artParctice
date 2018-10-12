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
        //单例：若存在就直接返回，若不存在就实例化一个存入
        if (beanPool.containsKey(id)) {
            return beanPool.get(id);
        }
        
        if (components.containsKey(id)) {
        	//若这个字符串在componets集中恰好有映射，就将实例化并用一个OBject来指向
            Object bean = Class.forName(components.get(id))
                    .newInstance();
            //分析该对象中是否有要注入的
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
        //利用反射获取所有字段并迭代，判断每个字段上是否有Autowire注解
        for (Field f : cl.getDeclaredFields()) {
            Autowire at = f.getAnnotation(Autowire.class);
            
            if (at != null) {
            	//若该字段有注解，则获取该字段对应的set方法
                Method setMethod = cl.getMethod("set" 
                        + captureName(f.getName()), f.getType());
                   //并执行该方法     
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