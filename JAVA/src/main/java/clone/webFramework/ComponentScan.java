package clone.webFramework;

import annotation.CustomAnnotation;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ComponentScan {
    public void scan(){
//        String basePackage = "clone.webFramework";
        String basePackage = "";
        List<Class<?>> classList = scanning(basePackage);
        for(Class<?> clazz : classList){
            if(clazz.isAnnotationPresent(CustomAnnotation.class))
            System.out.println(clazz + ", " +clazz.toString().split(" ")[0]+"\n");
            Method[] methodArr = clazz.getDeclaredMethods();
            for(Method method : methodArr){
                if(method.isAnnotationPresent(CustomAnnotation.class)) System.out.println("method name : "+method.getName());
            }

//            Annotation[] annotations = clazz.getDeclaredAnnotations();
//            for(Annotation annotation : annotations){
//                System.out.println("Annotation name : " + annotation.toString());
//            }
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
