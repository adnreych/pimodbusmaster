package net.lockoil.pimodbusmaster.csd;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;
import net.lockoil.pimodbusmaster.model.AtConnectionRequest;

/**
 * Класс для соединения с устройством по протоколу CSD через модемное AT-соединение.
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ATConnect {

    private SerialPort serialPort;
    static CSDResponsePayloadParser csdResponsePayloadParser;
    volatile private String status = "";
    volatile private boolean isCSDEventArrived = false;
    volatile private byte[] byteData;
    
	/**
	 * Соединение через AT
	 * @param atConnectionRequest Параметры соединения
	 * @return Статус текущего AT-соединения
	 */
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
    
    /**
	 * Отправить CSD-запрос на чтение и получить ответ
	 * @param atConnectionRequest Параметры соединения
	 * @param command CSD-команда
	 * @return Сырые данные, представляющие ANSI-символы
	 */
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
        		return byteData;
        	}
        }		
	}
    
    /**
	 * Отправить CSD-запрос на запись
	 * @param atConnectionRequest Параметры соединения
	 * @param command CSD-команда
	 */
    public void CSDWriteRequest(AtConnectionRequest atConnectionRequest, byte[] command) {
    	if (serialPort.isOpened()) {
    		try {
				serialPort.writeBytes(command);
				while (true) {
		        	if (isCSDEventArrived) {
		        		isCSDEventArrived = false;
		        		break;
		        	}
		        }	
			} catch (SerialPortException e) {
				e.printStackTrace();
			}
    	}	
	}
    
    /**
   	 * Закрыть указанный в {@link AtConnectionRequest} COM-port
   	 * @param atConnectionRequest Параметры соединения
   	 */
    public boolean closePort(AtConnectionRequest atConnectionRequest) throws SerialPortException {
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
                try {                	
                	byteData = serialPort.readBytes(event.getEventValue());
                	
                	for(byte b : byteData) {
                		System.out.println(b);
                	}
					isCSDEventArrived = true;	
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
    
    /**
	 * Получить список COM-портов на устройстве
	 * @return Список COM-портов
	 */
    public String[] getPorts() {
    	return SerialPortList.getPortNames();   	
    }
}