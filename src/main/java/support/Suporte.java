package support;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import type.Mensagem;

public class Suporte {
	
	public String obterData(String format) {
		DateFormat formato = new SimpleDateFormat(format);
		Date data = new Date();
		return formato.format(data).toString();
	}
	
	public String equalsMessage(Mensagem mensagem) {
		return mensagem.getMensagem();
	}

}
