package net.lockoil.pimodbusmaster.csd;

import java.util.Arrays;

import com.google.common.collect.ObjectArrays;

public class CSDCommand {
	
	private Byte[] CSDcommandPayload;
	private Byte[] CMD_COMMAND_PART = new Byte[] {'C', 'M', 'D'};
    private final Byte[] END_PART = {'E', 'N', 'D', 0x0D};
    private Byte CRC;
    
    
    
	public CSDCommand(Byte[] CSDcommandPayload) {
		this.CSDcommandPayload = CSDcommandPayload;
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


	public Byte[] getCSDcommandPayload() {
		return CSDcommandPayload;
	}


	public void setCSDcommandPayload(Byte[] cSDcommandPayload) {
		CSDcommandPayload = cSDcommandPayload;
	}


	@Override
	public String toString() {
		return "CSDCommand [CSDcommandPayload="
				+ Arrays.toString(CSDcommandPayload) + ", CMD_COMMAND_PART=" + Arrays.toString(CMD_COMMAND_PART)
				+ ", END_PART=" + Arrays.toString(END_PART) + ", CRC=" + CRC + "]";
	}
	
	
	
    
    
}
