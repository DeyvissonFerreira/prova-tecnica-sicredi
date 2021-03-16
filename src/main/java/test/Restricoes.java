package test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import type.CPF;

public class Restricoes {
	
	@BeforeClass
	public void init() {
		RestAssured.baseURI = "http://localhost:8080/";
	}
	
	@Test
	public void CT01_consultarCPFComRestricao() {
		
		given()
			.log().all()
			.contentType(ContentType.JSON)
		.when()
			.get("/api/v1/restricoes/".concat(CPF.CPF_RESTRICAO.getCPF()))
		.then()
			.log().all()
			.statusCode(200)
			.body("mensagem", is("O CPF ".concat(CPF.CPF_RESTRICAO.getCPF()).concat(" tem problema")))
		;
	}
}
