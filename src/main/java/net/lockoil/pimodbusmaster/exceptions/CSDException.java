package net.lockoil.pimodbusmaster.exceptions;

/*
 * Ошибка чтения, записи, соединения по CSD
 */
public class CSDException extends Exception {
	
	private static final long serialVersionUID = -4755152068763212915L;

	public CSDException(String s) {
        super(s);
    }
}
