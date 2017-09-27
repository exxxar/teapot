
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Statement;
import java.util.Enumeration;
public class Serial implements SerialPortEventListener{
    SerialPort serialPort;
    private Statement statement;

    private InputStream input;
    private OutputStream output;
    private static final int TIME_OUT = 2000;
    private static final int DATA_RATE = 9600;
    private static final String PORT_NAMES[] = {
            "/dev/tty.usbserial-A9007UX1", // Mac OS X
            "/dev/ttyUSB0", // Linux
            "COM4", // Windows
    };


    public void setStatement(Statement stetement){
        this.statement = stetement;
    }
    public void initialize() {
        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            for (String portName : PORT_NAMES) {
                if (currPortId.getName().equals(portName)) {
                    portId = currPortId;
                    break;
                }
            }
        }
        if (portId == null) {
            System.out.println("Could not find COM port.");
            return;
        }else{
            System.out.println("Found your Port");
        }
        try {
            // open serial port, and use class name for the appName.
            serialPort = (SerialPort) portId.open(this.getClass().getName(),TIME_OUT);
            // set port parameters
            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            // open the streams
            input = serialPort.getInputStream();
            output = serialPort.getOutputStream();
            // add event listeners
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }


    public synchronized void close() {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }


    public void sendSingleByte(byte myByte){
        try {

            output.write(myByte);
            output.flush();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }


    public synchronized void serialEvent (SerialPortEvent oEvent) {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                int myByte=input.read();
                int value = myByte & 0xff;//byte to int conversion:0...127,-127...0 -> 0...255

                if(value>=0 && value<256){
                    String param = "";
                    switch (value){
                        case 31: param = "status";  break;
                        case 32: param = "status";  break;
                        case 33: param = "temperature";  break;
                        case 34: param = "pressure";  break;
                        case 35: param = "waterlvl";  break;
                    }

                    myByte=input.read();
                    value = myByte & 0xff;//
                    statement.execute("UPDATE `settings` SET `value`='"+value+"' WHERE `param`='"+param+"'");
                    System.out.print(value);

                    }
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }
    }
}