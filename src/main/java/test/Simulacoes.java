package test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import model._Simulacao;
import support.Suporte;
import type.CPF;
import type.Mensagem;

public class Simulacoes extends Suporte{
	
	_Simulacao simulacao;
	List<_Simulacao> simulacoesBaseline;
	
	@BeforeClass
	public void init() {
		RestAssured.baseURI = "http://localhost:8080/";
		simulacoesBaseline = new ArrayList<>();
		simulacoesBaseline.add(new _Simulacao("Fulano", "66414919004", "fulano@gmail.com", "11000", 3, true));
		simulacoesBaseline.add(new _Simulacao("Deltrano", "17822386034", "deltrano@gmail.com", "20000", 5, false));
	}
	
	@Test
	public void CT01_consultarSimulacoes() {
		given()
			.log().all()
			.contentType(ContentType.JSON)
		.when()
			.get("api/v1/simulacoes")
		.then()
			.log().all()
			.statusCode(HttpStatus.SC_OK)
			.body("nome[0]", is(simulacoesBaseline.get(0).nome))
			.body("cpf[0]", is(simulacoesBaseline.get(0).cpf))
			.body("email[0]", is(simulacoesBaseline.get(0).email))
			.body("valor[0]", is(Float.parseFloat(simulacoesBaseline.get(0).valor)))
			.body("parcelas[0]", is(simulacoesBaseline.get(0).parcelas))
			.body("seguro[0]", is(simulacoesBaseline.get(0).seguro))
			.body("nome[1]", is(simulacoesBaseline.get(1).nome))
			.body("cpf[1]", is(simulacoesBaseline.get(1).cpf))
			.body("email[1]", is(simulacoesBaseline.get(1).email))
			.body("valor[1]", is(Float.parseFloat(simulacoesBaseline.get(1).valor)))
			.body("parcelas[1]", is(simulacoesBaseline.get(1).parcelas))
			.body("seguro[1]", is(simulacoesBaseline.get(1).seguro))
			
		;
	}
	
	@Test
	public void CT02_consultarSimulacaoPorCPF() {
		_Simulacao simulacaoBaseline = simulacoesBaseline.get(0);
		
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.pathParam("cpf", simulacaoBaseline.cpf)
		.when()
			.get("api/v1/simulacoes/{cpf}")
		.then()
			.log().all()
			.statusCode(HttpStatus.SC_OK)
			.body("nome", is(simulacaoBaseline.nome))
			.body("cpf", is(simulacaoBaseline.cpf))
			.body("email", is(simulacaoBaseline.email))
			.body("valor", is(Float.parseFloat(simulacaoBaseline.valor)))
			.body("parcelas", is(simulacaoBaseline.parcelas))
			.body("seguro", is(simulacaoBaseline.seguro))
			;
	}
	
	@Test
	public void CT03_consultarSimulacaoPorCPFNaoEncontrado() {
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.pathParam("cpf", CPF.CPF_NAO_ENCONTRADO.getCPF())
		.when()
			.get("api/v1/simulacoes/{cpf}")
		.then()
			.log().all()
			.statusCode(HttpStatus.SC_NOT_FOUND)
			.body("mensagem", is(equalsMessage(Mensagem.MSG_CPF_NAO_ENCONTRADO)))
			;
	}
	
	@Test
	public void CT04_deletarSimulacao() {
		Integer id = consultaSimulacao();
		
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.pathParam("id", id)
		.when()
			.delete("/api/v1/simulacoes/{id}")
		.then()
			.log().all()
			.statusCode(HttpStatus.SC_OK)
		;
	}
	
	@Test
	public void CT05_deletarSimulacaoInexistente() {
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.pathParam("id", 99)
		.when()
			.delete("/api/v1/simulacoes/{id}")
		.then()
			.log().all()
			.statusCode(HttpStatus.SC_NOT_FOUND)
			.body("mensagem", is(equalsMessage(Mensagem.MSG_SIMULACAO_NAO_ENCONTRADA)))
		;
	}
	
	@Test
	public void CT06_criarSimulacao() {
		simulacao = new _Simulacao("Fulano de Tal".concat(obterData("mmss")), "97093236014", "email@email.com", "1200", 3, true);
		
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.body(simulacao)
		.when()
			.post("api/v1/simulacoes/")
		.then()
			.log().all()
			.statusCode(HttpStatus.SC_CREATED)
			.body("nome", is(notNullValue()))
			.body("nome", is(simulacao.nome))
			.body("cpf", is(simulacao.cpf))
			.body("email", is(simulacao.email))
			.body("valor", is(Integer.parseInt(simulacao.valor)))
			.body("parcelas", is(simulacao.parcelas))
			.body("seguro", is(simulacao.seguro))
		;
	}
	
	@Test
	public void CT07_criarSimulacaoFormatoCPFInvalido() {
		simulacao = new _Simulacao("Fulano de Tal".concat(obterData("mmss")), "848.097.660-80", "teste@email.com", "3200", 3, true);
		
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.body(simulacao)
		.when()
			.post("/api/v1/simulacoes/")
		.then()
			.log().all()
			.statusCode(HttpStatus.SC_CREATED)
			.body("nome", is(notNullValue()))
			.body("nome", is(simulacao.nome))
			.body("cpf", is(simulacao.cpf))
			.body("email", is(simulacao.email))
			.body("valor", is(Integer.parseInt(simulacao.valor)))
			.body("parcelas", is(simulacao.parcelas))
			.body("seguro", is(simulacao.seguro))
		;
	}
	
	@Test
	public void CT08_criarSimulacaoComEmailInvalido() {
		simulacao = new _Simulacao("Fulano de Tal".concat(obterData("mmss")), "62648716050", "321654", "3200", 3, true);
		
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.body(simulacao)
		.when()
			.post("/api/v1/simulacoes/")
		.then()
			.log().all()
			.statusCode(HttpStatus.SC_BAD_REQUEST)
			.body("erros.email", is(equalsMessage(Mensagem.MSG_EMAIL_INVALIDO)))
		;
	}
	
	@Test
	public void CT09_criarSimulacaoComValorAbaixoMinimo() {
		simulacao = new _Simulacao("Fulano de Tal".concat(obterData("mmss")), "26276298085", "teste@email.com", "1", 3, true);
		
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.body(simulacao)
		.when()
			.post("/api/v1/simulacoes/")
		.then()
			.log().all()
			.statusCode(HttpStatus.SC_BAD_REQUEST)
			.body("erros.valor", is(equalsMessage(Mensagem.MSG_VALOR_ABAIXO_MINIMO)))
		;
	}
	
	@Test
	public void CT10_criarSimulacaoComValorAbaixoMinimo() {
		simulacao = new _Simulacao("Fulano de Tal".concat(obterData("mmss")), "62648716050", "teste@email.com", "800000", 3, true);
		
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.body(simulacao)
		.when()
			.post("/api/v1/simulacoes/")
		.then()
			.log().all()
			.statusCode(HttpStatus.SC_BAD_REQUEST)
			.body("erros.valor", is(equalsMessage(Mensagem.MSG_VALOR_ACIMA_MAXIMO)))
		;
	}
	
	@Test
	public void CT11_criarSimulacaoComNumeroDeParcelasAbaixoMinimo() {
		simulacao = new _Simulacao("Fulano de Tal".concat(obterData("mmss")), "01317496094", "teste@email.com", "5000", 1, true);
		
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.body(simulacao)
		.when()
			.post("/api/v1/simulacoes/")
		.then()
			.log().all()
			.statusCode(HttpStatus.SC_BAD_REQUEST)
			.body("erros.parcelas", is(equalsMessage(Mensagem.MSG_PARCELAS_ABAIXO_MINIMO)))
		;
	}
	
	@Test
	public void CT12_criarSimulacaoComNumeroDeParcelasAcimaMaximo() {
		simulacao = new _Simulacao("Fulano de Tal".concat(obterData("mmss")), "55856777050", "teste@email.com", "5000", 60, true);
		
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.body(simulacao)
		.when()
			.post("/api/v1/simulacoes/")
		.then()
			.log().all()
			.statusCode(HttpStatus.SC_BAD_REQUEST)
			.body("erros.parcelas", is(equalsMessage(Mensagem.MSG_PARCELAS_ACIMA_MAXIMO)))
		;
	}
	
	@Test
	public void CT13_criarSimulacaoSemCamposObrigatorios() {
		simulacao = new _Simulacao();
		
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.body(simulacao)
		.when()
			.post("/api/v1/simulacoes/")
		.then()
			.log().all()
			.statusCode(HttpStatus.SC_BAD_REQUEST)
			.body("erros.parcelas", is(equalsMessage(Mensagem.MSG_PARCELA_OBRIGATORIA)))
			.body("erros.cpf", is(equalsMessage(Mensagem.MSG_CPF_OBRIGATORIO)))
			.body("erros.valor", is(equalsMessage(Mensagem.MSG_VALOR_OBRIGATORIO)))
			.body("erros.nome", is(equalsMessage(Mensagem.MSG_NOME_OBRIGATORIO)))
			.body("erros.email", is(equalsMessage(Mensagem.MSG_EMAIL_OBRIGATORIO)))
		;
	}
	
	@Test
	public void CT14_criarSimulacaoCPFJaExistente() {
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.body(simulacao)
		.when()
			.post("/api/v1/simulacoes/")
		.then()
			.log().all()
			.statusCode(HttpStatus.SC_BAD_REQUEST)
			.body("mensagem", is(equalsMessage(Mensagem.MSG_CPF_DUPLICADO)))
		;
	}
	
	@Test
	public void CT15_alterarDadosSimulacao() {
		_Simulacao simulacaoAlterada = new _Simulacao("Nome Alterado".concat(obterData("mmss")), "97093236014", "emailAlterado@email.com", "35000", 24, false);
		
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.pathParam("cpf", simulacao.cpf)
			.body(simulacaoAlterada)
		.when()
			.put("/api/v1/simulacoes/{cpf}")
		.then()
			.log().all()
			.statusCode(HttpStatus.SC_OK)
			.body("nome", is(simulacaoAlterada.nome))
			.body("cpf", is(simulacaoAlterada.cpf))
			.body("email", is(simulacaoAlterada.email))
			.body("valor", is(Integer.parseInt(simulacaoAlterada.valor)))
			.body("parcelas", is(simulacaoAlterada.parcelas))
			.body("seguro", is(simulacaoAlterada.seguro))
		;
	}
	
	@Test
	public void CT16_alterarSimulacaoCPFNaoEncontrado() {
		_Simulacao simulacaoAlterada = new _Simulacao(10);
		
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.pathParam("cpf", CPF.CPF_NAO_ENCONTRADO.getCPF())
			.body(simulacaoAlterada)
		.when()
			.put("/api/v1/simulacoes/{cpf}")
		.then()
			.log().all()
			.statusCode(HttpStatus.SC_NOT_FOUND)
			.body("mensagem", is(equalsMessage(Mensagem.MSG_CPF_NAO_ENCONTRADO)))
		;
	}
	
	@Test
	public void CT17_alterarDadosSimulacaoSemCamposObrigatorios() {
		_Simulacao simulacaoSemDados = new _Simulacao();
		
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.pathParam("cpf", simulacao.cpf)
			.body(simulacaoSemDados)
		.when()
			.put("/api/v1/simulacoes/{cpf}")
		.then()
			.log().all()
			.statusCode(HttpStatus.SC_BAD_REQUEST)
			.body("erros.parcelas", is(equalsMessage(Mensagem.MSG_PARCELA_OBRIGATORIA)))
			.body("erros.cpf", is(equalsMessage(Mensagem.MSG_CPF_OBRIGATORIO)))
			.body("erros.valor", is(equalsMessage(Mensagem.MSG_VALOR_OBRIGATORIO)))
			.body("erros.nome", is(equalsMessage(Mensagem.MSG_NOME_OBRIGATORIO)))
			.body("erros.email", is(equalsMessage(Mensagem.MSG_EMAIL_OBRIGATORIO)))
		;
	}
	
	private Integer consultaSimulacao() {
		_Simulacao simulacaoBaseline = simulacoesBaseline.get(0);
		
		Integer id =
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.pathParam("cpf", simulacaoBaseline.cpf)
		.when()
			.get("api/v1/simulacoes/{cpf}")
		.then()
			.log().all()
			.statusCode(HttpStatus.SC_OK)
			.body("nome", is(simulacaoBaseline.nome))
			.body("cpf", is(simulacaoBaseline.cpf))
			.body("email", is(simulacaoBaseline.email))
			.body("valor", is(Float.parseFloat(simulacaoBaseline.valor)))
			.body("parcelas", is(simulacaoBaseline.parcelas))
			.body("seguro", is(simulacaoBaseline.seguro))
			.extract().path("id")
			;
		
		return id;
	}
}