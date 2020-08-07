package net.lockoil.pimodbusmaster.csd;

import org.springframework.stereotype.Component;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import net.lockoil.pimodbusmaster.exceptions.CSDException;
import net.lockoil.pimodbusmaster.model.AtConnectionRequest;

@Component
public class ATConnect {

    private SerialPort serialPort;
    static CSDResponsePayloadParser csdResponsePayloadParser;
    volatile private String status = "";
    
    
    public String getAtConnection(AtConnectionRequest atConnectionRequest) throws SerialPortException {
    	status = "";
    	serialPort = new SerialPort(atConnectionRequest.getPort());
    	serialPort.openPort();
        serialPort.setParams(SerialPort.BAUDRATE_19200,
                             SerialPort.DATABITS_8,
                             SerialPort.STOPBITS_1,
                             SerialPort.PARITY_NONE);
        serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | 
                                      SerialPort.FLOWCONTROL_RTSCTS_OUT);
        serialPort.addEventListener(new ATPortReader(atConnectionRequest.getPhone()), SerialPort.MASK_RXCHAR);
        serialPort.writeString("AT" + "\r");
        
        while (true) {
        	System.out.println("STATUS:" + status);
        	if (!status.equals("")) return status;
        }
    }
    
    public boolean closePort(AtConnectionRequest atConnectionRequest) throws SerialPortException {
    	serialPort = new SerialPort(atConnectionRequest.getPort());
    	System.out.println("close port " + atConnectionRequest.getPort());
    	if (serialPort.isOpened()) {
    		return serialPort.closePort();
    	} else {
    		return true;
    	}	
    }
    
    

    private class ATPortReader implements SerialPortEventListener {
    	
    	public ATPortReader(String phone) {
    		this.phone = phone;
    	}
    	
    	String phone;
    	
		public void serialEvent(SerialPortEvent event) {
            if(event.isRXCHAR() && event.getEventValue() > 0)	{
                try {
                	
                    String data = serialPort.readString(event.getEventValue());
                    System.out.println("Data" + data);
                   
                    if (data.length() > 4 && data.substring(2, 4).equals("OK")) {
                    	serialPort.writeString("ATD"+ phone + "\r");
					}
                    
                    if (data.length() > 12 && data.substring(2, 12).equals("NO CARRIER")) {
                    	status = "NO CARRIER";
                    	try {
    						serialPort.closePort();
    					} catch (SerialPortException e1) {
    						// TODO Auto-generated catch block
    						e1.printStackTrace();
    					}
					}
                    
                    if (data.length() > 14 && data.substring(2, 14).equals("CONNECT 9600")) {  
                    	status = "CONNECT";
                    	if (serialPort.removeEventListener()) {                		
                    		serialPort.addEventListener(new CSDPortReader(), SerialPort.MASK_RXCHAR);    
                    		
                    	}
                    }
                }
                catch (SerialPortException ex) {
                    System.out.println(ex);
                }
                catch (Exception e) {
					try {
						serialPort.closePort();
					} catch (SerialPortException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
            }
		} 	
    }
    
    private class CSDPortReader implements SerialPortEventListener {
    	
		public void serialEvent(SerialPortEvent event) {
			
            if(event.isRXCHAR() && event.getEventValue() > 0)	{
                try {
                	String data = serialPort.readString(event.getEventValue());
                	if (data.length() > 12 && data.substring(2, 12).equals("NO CARRIER")) {
                    	status = "NO CARRIER";
                    	try {
    						serialPort.closePort();
    					} catch (SerialPortException e1) {
    						// TODO Auto-generated catch block
    						e1.printStackTrace();
    					}
					} else {
						data = serialPort.readHexString(event.getEventValue());
	                	csdResponsePayloadParser.setResponse(data);
	                	System.out.println("DataFromCSD" + data);
	                	for (int i : csdResponsePayloadParser.parsePayload()) {
	                		System.out.println("i:" + i);
						}
					}
                	
                	
                }
                catch (SerialPortException | CSDException ex) {
                    System.out.println(ex);
                }
                catch (Exception e) {
					try {
						serialPort.closePort();
					} catch (SerialPortException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
            }
		} 	
    }
}