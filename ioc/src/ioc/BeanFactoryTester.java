package ioc;

import java.lang.reflect.InvocationTargetException;

public class BeanFactoryTester {
 
    public static void main(String[] args) throws Exception{
        BeanFactory beanFactory = new BeanFactory("ioc");
        
        BusinessObject obj = (BusinessObject) beanFactory.getBean("businessObject");
        obj.print();
            
    }

}