package clone.webFramework.init;

import clone.webFramework.servlet.ServletContainer;
import clone.webFramework.servlet.WebServer;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class Init {
    public Bean bean;
    public Init(){
        bean = new Bean();

        ComponentScan cs = new ComponentScan(bean.getRestControllerClassList(),
                bean.getMappingGetMethodList(),
                bean.getMappingPostMethodList(),
                bean.getMappingUpdateMethodList(),
                bean.getMappingDeleteMethodList());
        cs.scan();
        ServletContainer sc = new ServletContainer();
        sc.setBean(bean);
        try {
            WebServer webServer = new WebServer(new ServletContainer.ServletHandler());
            webServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static class Bean{
        // Beans
        private static List<Class<?>> restControllerClassList = new ArrayList<>();
        private static List<Method> mappingGetMethodList = new ArrayList<>();
        private static List<Method> mappingPostMethodList = new ArrayList<>();
        private static List<Method> mappingUpdateMethodList = new ArrayList<>();
        private static List<Method> mappingDeleteMethodList = new ArrayList<>();

        public List<Class<?>> getRestControllerClassList() {
            return restControllerClassList;
        }

        public void setRestControllerClassList(List<Class<?>> restControllerClassList) {
            Bean.restControllerClassList = restControllerClassList;
        }

        public List<Method> getMappingGetMethodList() {
            return mappingGetMethodList;
        }

        public void setMappingGetMethodList(List<Method> mappingGetMethodList) {
            Bean.mappingGetMethodList = mappingGetMethodList;
        }

        public List<Method> getMappingPostMethodList() {
            return mappingPostMethodList;
        }

        public void setMappingPostMethodList(List<Method> mappingPostMethodList) {
            Bean.mappingPostMethodList = mappingPostMethodList;
        }

        public List<Method> getMappingUpdateMethodList() {
            return mappingUpdateMethodList;
        }

        public void setMappingUpdateMethodList(List<Method> mappingUpdateMethodList) {
            Bean.mappingUpdateMethodList = mappingUpdateMethodList;
        }

        public List<Method> getMappingDeleteMethodList() {
            return mappingDeleteMethodList;
        }

        public void setMappingDeleteMethodList(List<Method> mappingDeleteMethodList) {
            Bean.mappingDeleteMethodList = mappingDeleteMethodList;
        }
    }
}
