package net.lockoil.pimodbusmaster.csd;

import java.util.Arrays;

import com.google.common.collect.ObjectArrays;

import net.lockoil.pimodbusmaster.csd.Utils;

public class CSDCommand {
	
	private Byte[] CSDcommandNumber;
	private Byte[] CSDcommandPayload;
	private Byte[] CMD_COMMAND_PART;
    private final Byte[] END_PART = {'E', 'N', 'D', 0x0D};
    private Byte CRC;
    
    
    
	public CSDCommand(byte[] CSDcommandNumber, Byte[] CSDcommandPayload) {
		this.CSDcommandNumber = Utils.toByteWrap(CSDcommandNumber);
		this.CSDcommandPayload = CSDcommandPayload;
		CMD_COMMAND_PART = ObjectArrays.concat(new Byte[] {'C', 'M', 'D'}, this.CSDcommandNumber, Byte.class);
		this.CRC = getCRC();
	}
	
	
	private Byte getCRC() {
		Byte summ = 0;
		Byte[] arrForCRC = ObjectArrays.concat(CMD_COMMAND_PART, CSDcommandPayload , Byte.class);
		for (Byte b : arrForCRC) {
			//System.out.println("b: " + b);
			summ = (byte) (summ + b);
		}
		//System.out.println("CRC: " + summ);
		return summ;
	}
	
	public byte[] getCommand() {
		Byte[] swap;
		swap = ObjectArrays.concat(CMD_COMMAND_PART, CSDcommandPayload , Byte.class);
		swap = ObjectArrays.concat(swap, CRC);
		swap = ObjectArrays.concat(swap, END_PART, Byte.class);	
		byte[] command = Utils.toBytePrimitive(swap);

		return command;
	}


	public Byte[] getCSDcommandNumber() {
		return CSDcommandNumber;
	}


	public void setCSDcommandNumber(Byte[] cSDcommandNumber) {
		CSDcommandNumber = cSDcommandNumber;
	}


	public Byte[] getCSDcommandPayload() {
		return CSDcommandPayload;
	}


	public void setCSDcommandPayload(Byte[] cSDcommandPayload) {
		CSDcommandPayload = cSDcommandPayload;
	}


	@Override
	public String toString() {
		return "CSDCommand [CSDcommandNumber=" + Arrays.toString(CSDcommandNumber) + ", CSDcommandPayload="
				+ Arrays.toString(CSDcommandPayload) + ", CMD_COMMAND_PART=" + Arrays.toString(CMD_COMMAND_PART)
				+ ", END_PART=" + Arrays.toString(END_PART) + ", CRC=" + CRC + "]";
	}
	
	
	
    
    
}
