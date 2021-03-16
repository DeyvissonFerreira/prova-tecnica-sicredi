package model;

public class _Simulacao {
	
	public String nome, cpf, email;
	public Integer parcelas;
	public String valor;
	public boolean seguro;
	
	public _Simulacao() {
	}
	
	public _Simulacao(String nome, String cpf, String email, String valor, Integer parcelas, boolean seguro) {
		this.nome = nome;
		this.cpf = cpf;
		this.email = email;
		this.valor = valor;
		this.parcelas = parcelas;
		this.seguro = seguro;
	}
	
	public _Simulacao(Integer parcelas) {
		this.parcelas = parcelas;
	}
	
	public _Simulacao(String valor) {
		this.valor = valor;
	}

}
