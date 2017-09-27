

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.DriverManager;
import java.sql.ResultSet;

/**
 * Created by yar 09.09.2009
 */
public class Main {

    // JDBC URL, username and password of MySQL server
    private static final String url = "jdbc:mysql://localhost:3306/test";
    private static final String user = "root";
    private static final String password = "";

    // JDBC variables for opening and managing connection
    private static Connection con;
    private static Statement stmt;


    public static void main(String[] args) throws Throwable {
        ServerSocket ss = new ServerSocket(8080);


        con = (Connection) DriverManager.getConnection(url, user, password);

        // getting Statement object to execute query
        stmt = (Statement) con.createStatement();


        Serial serial = new Serial();
        serial.initialize();
        serial.setStatement(stmt);

        // executing SELECT query
       // rs = stmt.executeQuery(query);

        while (true) {
            Socket s = ss.accept();
            System.err.println("Client accepted");
            new Thread(new SocketProcessor(s,serial,stmt)).start();
        }

    }

    private static class SocketProcessor implements Runnable {

        private Socket s;
        private Serial serial;
        private InputStream is;
        private OutputStream os;
        private String path;
        private Statement statement;

        private SocketProcessor(Socket s, Serial serial, Statement statement) throws Throwable {
            this.s = s;
            this.is = s.getInputStream();
            this.os = s.getOutputStream();
            this.serial = serial;
            this.statement = statement;
        }

        public void run() {

            PageProccessor pageProccessor = new PageProccessor();
            pageProccessor.setSerial(serial);
            pageProccessor.setStatement(statement);
            try {
                readInputHeaders();
                if (path.indexOf(".") == -1) {
                    writeResponse(pageProccessor.parse(path));
                }

            }catch (InstantiationException e1) {
            }catch (IllegalAccessException ed){
            } catch (IOException e){
            } catch(PageNotFoundException e){
                try {
                    writeResponse( pageProccessor.get404());
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                /*do nothing*/
            } finally {
                try {
                    s.close();
                } catch (Throwable t) {
                    /*do nothing*/
                }
            }
            System.err.println("Client processing finished");
        }

        private void writeResponse(String s) throws IOException {
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Server: YarServer/2009-09-09\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: " + s.length() + "\r\n" +
                    "Connection: close\r\n\r\n";
            String result = response + s;
            os.write(result.getBytes());
            os.flush();
        }

        private void readInputHeaders() throws IOException{
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            int index = 0;
            while(true) {
                String s = br.readLine();
                if (index==0){
                    path = s.split(" ")[1];
                    index++;
                }

                if(s == null || s.trim().length() == 0) {
                    break;
                }
            }
        }
    }
}