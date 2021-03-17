package type;

public enum Mensagem {
	
	MSG_CPF_NAO_ENCONTRADO("CPF 99999999999 não encontrado"),
	MSG_VALOR_ABAIXO_MINIMO("Valor deve ser maior ou igual a R$ 1.000"),
	MSG_VALOR_ACIMA_MAXIMO("Valor deve ser menor ou igual a R$ 40.000"),
	MSG_PARCELAS_ABAIXO_MINIMO("Parcelas deve ser igual ou maior que 2"),
	MSG_PARCELAS_ACIMA_MAXIMO("Parcelas deve ser igual ou menor que 48"),
	MSG_PARCELA_OBRIGATORIA("Parcelas não pode ser vazio"),
	MSG_CPF_OBRIGATORIO("CPF não pode ser vazio"),
	MSG_CPF_RESTRICAO("O CPF %s tem problema"),
	MSG_NOME_OBRIGATORIO("Nome não pode ser vazio"),
	MSG_EMAIL_OBRIGATORIO("E-mail não deve ser vazio"),
	MSG_VALOR_OBRIGATORIO("Valor não pode ser vazio"),
	MSG_CPF_DUPLICADO("CPF duplicado"),
	MSG_SIMULACAO_NAO_ENCONTRADA("Simulação não encontrada"),
	MSG_EMAIL_INVALIDO("não é um endereço de e-mail");
	
	private String value;
	
	private Mensagem(String value) {
		this.value = value;
	}
	
	public String getMensagem() {
		return this.value;
	}
}