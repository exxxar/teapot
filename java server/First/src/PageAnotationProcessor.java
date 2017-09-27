
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.Set;

public class PageAnotationProcessor {

    private Serial serial;
    private Class clazz;
    private Statement statement;

    public void setSerial(Serial serial){
        this.serial = serial;
    }

    public void setStatement(Statement statement){
        this.statement = statement;
    }
    public void setClass(Class c){
        this.clazz = c;
    }

    public String parse(String path) throws IllegalAccessException, InstantiationException, PageNotFoundException {

            Method[] methods = (clazz).getMethods();
            Object object = (clazz).newInstance();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Page.class) && method.getAnnotation(Page.class).path().equals(path.trim())) {
                    try {
                        if (!method.getAnnotation(Page.class).SerialData())
                            return (String) method.invoke(object,statement);
                        else
                            return (String) method.invoke(object,statement,serial);

                    } catch (Exception e) {
                        System.err.println(e);
                    }
                }
            }

        throw new PageNotFoundException("Page not found");
    }
}
