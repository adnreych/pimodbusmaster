package net.lockoil.pimodbusmaster.csd;

import java.util.Arrays;

import com.google.common.collect.ObjectArrays;

/**
 * Класс для сборки готовой к отправке CSD-команды
 */
public class CSDCommand {
	
	private Byte[] CMD_COMMAND_PART = new Byte[] {'C', 'M', 'D'};
	private Byte[] CSDcommandPayload;
	private Byte CRC;
    private final Byte[] END_PART = {'E', 'N', 'D', 0x0D};
       
    
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
	
	/**
	 * Получить готовую для отправки команду
	 * @return Команда в виде массива
	 */
	public byte[] getCommand() {
		Byte[] swap;
		swap = ObjectArrays.concat(CMD_COMMAND_PART, CSDcommandPayload , Byte.class);
		swap = ObjectArrays.concat(swap, CRC);
		swap = ObjectArrays.concat(swap, END_PART, Byte.class);	
		byte[] command = Utils.toBytePrimitive(swap);

		return command;
	}

	/**
	 * Получить полезную нагрузку команды
	 * @return Полезная нагрузка команды
	 */
	public Byte[] getCSDcommandPayload() {
		return CSDcommandPayload;
	}

	/**
	 * Установить полезную нагрузку команды
	 */
	public void setCSDcommandPayload(Byte[] cSDcommandPayload) {
		CSDcommandPayload = cSDcommandPayload;
	}


	@Override
	public String toString() {
		return "CSDCommand [CMD_COMMAND_PART=" + Arrays.toString(CMD_COMMAND_PART) + ", CSDcommandPayload="
				+ Arrays.toString(CSDcommandPayload) + ", CRC=" + CRC + ", END_PART=" + Arrays.toString(END_PART) + "]";
	}
			
    
}
