package edu.whu.context;

import edu.whu.context.annotation.Autowired;
import edu.whu.context.annotation.Component;
import edu.whu.context.annotation.Qualifier;
import edu.whu.context.annotation.Value;
import edu.whu.context.utils.ScannerUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author myAdministrator
 */
public class MiniApplicationContext {
    private Map<String, Object> beans;

    public Object getBean(String beanName){
        return beans.get(beanName);
    }

    private List<String> beanNames = new ArrayList<>();

    public String[] getBeanDefinitionNames(){
        return beanNames.toArray(new String[0]);
    }

    public Integer getBeanDefinitionCount(){
        return beanNames.size();
    }

    public void autowireObject(Set<BeanDefinition> beanDefinitions){
        Iterator<BeanDefinition> iterator = beanDefinitions.iterator();
        while (iterator.hasNext()) {
            BeanDefinition beanDefinition = iterator.next();
            Class clazz = beanDefinition.getBeanClass();
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                Autowired annotation = declaredField.getAnnotation(Autowired.class);
                if(annotation!=null){
                    Qualifier qualifier = declaredField.getAnnotation(Qualifier.class);
                    if(qualifier!=null){
                        //byName
                        try {
                            String beanName = qualifier.value();
                            Object bean = getBean(beanName);
                            String fieldName = declaredField.getName();
                            String methodName = "set"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);
                            Method method = clazz.getMethod(methodName, declaredField.getType());
                            Object object = getBean(beanDefinition.getBeanName());
                            method.invoke(object, bean);
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }else{
                        //byType
                    }
                }
            }
        }
    }

    //????????????????????????
    public void createObject(Set<BeanDefinition> beanDefinitions) throws Exception {
        Iterator<BeanDefinition> iterator = beanDefinitions.iterator();
        while (iterator.hasNext()) {
            BeanDefinition beanDefinition = iterator.next();
            Class clazz = beanDefinition.getBeanClass();
            String beanName = beanDefinition.getBeanName();
            if (clazz.isAnnotationPresent(Component.class)) {
                Object obj = clazz.newInstance();
                //Value????????????
                for (Field field : clazz.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Value.class)) {
                        String value = field.getAnnotation(Value.class).value();
                        String methodName = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                        Method method = clazz.getDeclaredMethod(methodName, field.getType());
                        //???????????????????????? ???int string float?????? ????????????
                        Object val = null;
                        switch (field.getType().getName()) {
                            case "java.lang.Integer":
                                val = Integer.parseInt(value);
                                break;
                            case "java.lang.String":
                                val = value;
                                break;
                            case "java.lang.Float":
                                val = Float.parseFloat(value);
                                break;
                        }
                        method.invoke(obj, val);
                    }
                }
                beans.put(beanName, obj);
            }
        }
    }

    public Set<BeanDefinition> findBeanDefinitions(String pack){
        //1???????????????????????????
        Set<Class<?>> classes = ScannerUtil.getAllClass(pack);
        Iterator<Class<?>> iterator = classes.iterator();
        Set<BeanDefinition> beanDefinitions = new HashSet<>();
        while (iterator.hasNext()) {
            //2????????????????????????????????????????????????
            Class<?> clazz = iterator.next();
            Component componentAnnotation = clazz.getAnnotation(Component.class);
            if(componentAnnotation!=null){
                //??????Component????????????
                String beanName = componentAnnotation.value();
                if("".equals(beanName)){
                    //???????????????????????????
                    String className = clazz.getName().replaceAll(clazz.getPackage().getName() + ".", "");
                    beanName = className.substring(0, 1).toLowerCase()+className.substring(1);
                }
                //3????????????????????????BeanDefinition?????????????????????
                beanDefinitions.add(new BeanDefinition(beanName, clazz));
                beanNames.add(beanName);
            }
        }
        return beanDefinitions;
    }
}
