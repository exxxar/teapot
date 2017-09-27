import java.sql.ResultSet;
import java.sql.Statement;

public class PageProccessor extends PageAnotationProcessor {

    private static ResultSet rs;

    public PageProccessor(){
        super.setClass(PageProccessor.class);
    }

    @Page(path = "/")
    public String getMainPage(){
        return "main page";
    }

    public String getValue(Statement statement, String param){
        StringBuffer buf = new StringBuffer();
        String query = "SELECT * FROM `settings` WHERE `param`='"+param+"'";
        try {
            rs = statement.executeQuery(query);

            while (rs.next()) {
                buf.append(rs.getString("value"));

            }
        } catch (Exception e){

        }
        return buf.toString();
    }
    @Page(path = "/start", SerialData = true)
    public String getStart(Statement statement,Serial s){
        s.sendSingleByte((byte)1);
        return getValue(statement,"status");
    }

    @Page(path = "/stop", SerialData = true)
    public String getStop(Statement statement,Serial s){
        s.sendSingleByte((byte)0);
        return getValue(statement,"status");
    }

    @Page(path = "/status", SerialData = true)
    public String getStatus(Statement statement,Serial s){
        return getValue(statement,"status");
    }

    @Page(path = "/temperature", SerialData = true)
    public String getTemp(Statement statement,Serial s){
        s.sendSingleByte((byte)2);
        return getValue(statement,"temperature");
    }

    @Page(path = "/pressure", SerialData = true)
    public String getPressure(Statement statement,Serial s){
        s.sendSingleByte((byte)3);
        //тут нужно обращаться к бд и считывать последнее значение температуры с бд
        //в ивент-хендлере порта будет находиться запись значений в бд от ардуино
        //значения должны приходить после этого запроса, но приходить они будут асинхронно
        //для более быстрого обращения к бд будут отдельные методы
        return getValue(statement,"pressure");
    }

    @Page(path = "/waterlvl", SerialData = true)
    public String getWaterLvl(Statement statement,Serial s){
        s.sendSingleByte((byte)4);
        return getValue(statement,"waterlvl");
    }




    @Page(path = "/about")
    public String getAboutPage(Statement statement){
        return "about page";
    }


    public String get404(){
        return "404";
    }




}
