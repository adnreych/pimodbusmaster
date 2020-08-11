package net.lockoil.pimodbusmaster.csd;

import java.util.ArrayList;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;
import net.lockoil.pimodbusmaster.model.AtConnectionRequest;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ATConnect {

    private SerialPort serialPort;
    static CSDResponsePayloadParser csdResponsePayloadParser;
    volatile private String status = "";
    volatile private boolean isCSDEventArrived = false;
    volatile private byte[] byteData;
    
    
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
        	if (!status.equals("")) return status;
        }
    }
    
    public byte[] CSDReadRequest(AtConnectionRequest atConnectionRequest, byte[] command) {
    	if (serialPort.isOpened()) {
    		try {
				serialPort.writeBytes(command);
				
			} catch (SerialPortException e) {
				e.printStackTrace();
			}
    	}
		while (true) {
        	if (isCSDEventArrived) {
        		isCSDEventArrived = false;
        		System.out.println("CSDEventArrived:" + isCSDEventArrived);
        		return byteData;
        	}
        }		
	}
    
    public void CSDWriteRequest(AtConnectionRequest atConnectionRequest, byte[] command) {
    	if (serialPort.isOpened()) {
    		try {
				serialPort.writeBytes(command);
				
			} catch (SerialPortException e) {
				e.printStackTrace();
			}
    	}	
	}
    
    public boolean closePort(AtConnectionRequest atConnectionRequest) throws SerialPortException {
    	System.out.println("close port " + atConnectionRequest.getPort());
    	if (serialPort.isOpened()) {
    		return serialPort.closePort();
    	} else {
    		return true;
    	}	
    }
    
    public void refreshConnectionStatus(AtConnectionRequest atConnectionRequest) {
    	System.out.println("refreshConnectionStatus");
    	try {
    		System.out.println("refreshConnectionStatus2");
			if (serialPort.isOpened()) {
				System.out.println("refreshConnectionStatus3");
				if (serialPort.removeEventListener()) {   
					System.out.println("refreshConnectionStatus4");
					serialPort.closePort();
					getAtConnection(atConnectionRequest);
				}
			} else {
				System.out.println("refreshConnectionStatus5");
				status = "";
		    	serialPort.openPort();
		        serialPort.setParams(SerialPort.BAUDRATE_19200,
		                             SerialPort.DATABITS_8,
		                             SerialPort.STOPBITS_1,
		                             SerialPort.PARITY_NONE);
		        serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | 
		                                      SerialPort.FLOWCONTROL_RTSCTS_OUT);
		        serialPort.addEventListener(new ATPortReader(atConnectionRequest.getPhone()), SerialPort.MASK_RXCHAR);
		        serialPort.writeString("AT" + "\r");
			}
		} catch (SerialPortException e) {
			e.printStackTrace();
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
						e1.printStackTrace();
					}
				}
            }
		} 	
    }
    
    private class CSDPortReader implements SerialPortEventListener {
    	
		public void serialEvent(SerialPortEvent event) {
            if(event.isRXCHAR() && event.getEventValue() > 0)	{
            	System.out.println("Begin CSDEventArrived:" + isCSDEventArrived);
                try {
                	
                	byteData = serialPort.readBytes(event.getEventValue());
                	
                	for(byte b : byteData) {
                		System.out.println(b);
                	}
					isCSDEventArrived = true;
					System.out.println("CSDEventArrived:" + isCSDEventArrived);
        	
                }
                catch (SerialPortException ex) {
                    System.out.println(ex);
                }
                catch (Exception e) {
					try {
						serialPort.closePort();
					} catch (SerialPortException e1) {
						e1.printStackTrace();
					}
				}
            }
		} 	
    }
    
    public String[] getPorts() {
    	return SerialPortList.getPortNames();   	
    }
}