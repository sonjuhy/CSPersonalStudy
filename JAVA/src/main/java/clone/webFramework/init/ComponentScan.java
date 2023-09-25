package clone.webFramework.init;

import annotation.CustomAnnotation;
import clone.webFramework.annotation.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ComponentScan {
    private List<Class<?>> restControllerClassList;
    private List<Method> mappingGetMethodList;
    private List<Method> mappingPostMethodList;
    private List<Method> mappingUpdateMethodList;
    private List<Method> mappingDeleteMethodList;

    public ComponentScan(){
        restControllerClassList = new ArrayList<>();
        mappingGetMethodList = new ArrayList<>();
        mappingPostMethodList = new ArrayList<>();
        mappingUpdateMethodList = new ArrayList<>();
        mappingDeleteMethodList = new ArrayList<>();
    }
    public ComponentScan(List<Class<?>> restControllerClassList, List<Method> mappingGetMethodList, List<Method> mappingPostMethodList, List<Method> mappingUpdateMethodList, List<Method> mappingDeleteMethodList){
        this.restControllerClassList = restControllerClassList;
        this.mappingGetMethodList = mappingGetMethodList;
        this.mappingPostMethodList = mappingPostMethodList;
        this.mappingUpdateMethodList = mappingUpdateMethodList;
        this.mappingDeleteMethodList = mappingDeleteMethodList;
    }

    public void scan(){
        String basePackage = "clone.webFramework";
//        String basePackage = ""; // all package scan
        List<Class<?>> classList = scanning(basePackage);
        for(Class<?> clazz : classList){
            if(clazz.isAnnotationPresent(CustomAnnotation.class)) System.out.println(clazz + ", " +clazz.toString().split(" ")[0]+"\n");
            if(clazz.isAnnotationPresent(RestController.class)){
                restControllerClassList.add(clazz);
                Method[] methods = clazz.getDeclaredMethods();
                for(Method method : methods){
                    if(method.isAnnotationPresent(MappingGet.class)){
                        mappingGetMethodList.add(method);
                    }
                    else if(method.isAnnotationPresent(MappingPost.class)){
                        mappingPostMethodList.add(method);
                    }
                    else if(method.isAnnotationPresent(MappingPut.class)){
                        mappingUpdateMethodList.add(method);
                    }
                    else if(method.isAnnotationPresent(MappingDelete.class)){
                        mappingDeleteMethodList.add(method);
                    }
                    else{
                        System.out.println("hello?");
                    }
                }
            }
        }
    }
    public static List<Class<?>> scanning (String basePackageName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = basePackageName.replace('.', '/');

        List<Class<?>> classes = new ArrayList<>();

        try {
            List<File> files = new ArrayList<>();
            Enumeration<URL> resources = classLoader.getResources(path);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                files.add(new File(resource.getFile()));
            }
            for (File file : files) {
                if (file.isDirectory()) {
                    classes.addAll(findClasses(file, basePackageName));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classes;
    }
    private static List<Class<?>> findClasses(File directory, String packageName) {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }

        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                if(className.toCharArray()[0] == '.') className = className.substring(1);
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                try {
                    classes.add(Class.forName(className, false, classLoader));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        return classes;
    }
}
