package type;

public enum CPF {
	
	CPF_RESTRICAO("97093236014"),
	CPF_NAO_ENCONTRADO("99999999999");
	
	private String value;
	
	private CPF(String value) {
		this.value = value;
	}
	
	public String getCPF() {
		return this.value;
	}

}
