package br.com.brunozava.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import io.restassured.http.ContentType;


public class CenariosNegativosPedidosTests extends BaseTest{
	
	
	@Test
	public void naoDeveCriarPedidoSemToken(){
		given()
				.log().all()
			.contentType(ContentType.JSON)
				.body("{\"ownId\":\"0022233\","
						+ "\"amount\":{\"currency\":\"BRL\","
						+ "\"subtotals\":{\"shipping\":1500}},"
						+ "\"items\":[{"
						+ "\"product\":\"Camisa\","
						+ "\"category\":\"CLOTHING\","
						+ "\"quantity\":1,"
						+ "\"detail\":\"Camisa Palmeiras, modelo 2020, cor verde\","
						+ "\"price\":100000}],"
						+ "\"customer\":{"
						+ "\"id\":\"CUS-PPJBI6MO5J1D\""
						+ "}}")
			.when()
				.post("/v2/orders/")
			.then()
				.log().all()
				.statusCode(401)
				;
	}
	
	@Test
	public void naoDeveCriarPedidoSemInformarIdentificador (){
		given()
			.log().all()
			.log().all()
			.header("Authorization", "Basic " + ACCESS_TOKEN)
			.body("{\"ownId\":\"\","
					+ "\"amount\":{\"currency\":\"BRL\","
					+ "\"subtotals\":{\"shipping\":1500}},"
					+ "\"items\":[{"
					+ "\"product\":\"C�mera fotogr�fica\","
					+ "\"category\":\"CAMERAS\","
					+ "\"quantity\":1,"
					+ "\"detail\":\"C�mera fotogr�fica, modelo CM54296, cor preta\","
					+ "\"price\":100000}],"
					+ "\"customer\":{"
					+ "\"id\":\"CUS-PPJBI6MO5J1D\""
					+ "}}")
		.when()
			.post("/v2/orders/")
		.then()
			.log().all()
			.statusCode(400)
			.body("errors[0].code", is("ORD-001"))
			.body("errors[0].path", is("ownId"))
			.body("errors[0].description", is("� necessario informar seu identificador pr�prio"))
		;
			
	}
	
	@Test
	public void naoDeveCriarPedidoSemInformarItem (){
		given()
			.log().all()
			.log().all()
			.header("Authorization", "Basic " + ACCESS_TOKEN)
			.body("{\"ownId\":\"0007\","
					+ "\"amount\":{\"currency\":\"BRL\","
					+ "\"subtotals\":{\"shipping\":1500}},"
					+ "\"items\":[{"
					+ "\"product\":\"\","
					+ "\"category\":\"CAMERAS\","
					+ "\"quantity\":1,"
					+ "\"detail\":\"C�mera fotogr�fica, modelo CM54296, cor preta\","
					+ "\"price\":100000}],"
					+ "\"customer\":{"
					+ "\"id\":\"CUS-PPJBI6MO5J1D\""
					+ "}}")
		.when()
			.post("/v2/orders/")
		.then()
			.log().all()
			.statusCode(400)
			.body("errors[0].code", is("ORD-011"))
			.body("errors[0].path", is("items[0].product"))
			.body("errors[0].description", is("Informe o nome do produto"))
		;
			
	}
	
	@Test
	public void naoDeveCriarPedidoComvalorIgual0 (){
		given()
			.log().all()
			.log().all()
			.header("Authorization", "Basic " + ACCESS_TOKEN)
			.body("{\"ownId\":\"0007\","
					+ "\"amount\":{\"currency\":\"BRL\","
					+ "\"subtotals\":{\"shipping\":1500}},"
					+ "\"items\":[{"
					+ "\"product\":\"C�mera fotogr�fica\","
					+ "\"category\":\"CAMERAS\","
					+ "\"quantity\":1,"
					+ "\"detail\":\"C�mera fotogr�fica, modelo CM54296, cor preta\","
					+ "\"price\":0}],"
					+ "\"customer\":{"
					+ "\"id\":\"CUS-PPJBI6MO5J1D\""
					+ "}}")
		.when()
			.post("/v2/orders/")
		.then()
			.log().all()
			.statusCode(400)
			.body("errors[0].code", is("ORD-009"))
			.body("errors[0].path", is("items[0].price"))
			.body("errors[0].description", is("Todos os valores devem ser maiores que zero"))
		;
			
	}
	//Deveria me retornar o c�digo de erro: ORD-014;
	//Devido a este probema n�o consigo realizar as outras valida��es.
	@Test
	public void naoDeveCriarPedidoSemValor (){
		given()
			.log().all()
			.header("Authorization", "Basic " + ACCESS_TOKEN)
			.body("{\"ownId\":\"0007\","
					+ "\"amount\":{\"currency\":\"BRL\","
					+ "\"subtotals\":{\"shipping\":1500}},"
					+ "\"items\":[{"
					+ "\"product\":\"C�mera fotogr�fica\","
					+ "\"category\":\"CAMERAS\","
					+ "\"quantity\":1,"
					+ "\"detail\":\"C�mera fotogr�fica, modelo CM54296, cor preta\","
					+ "\"price\":}],"
					+ "\"customer\":{"
					+ "\"id\":\"CUS-PPJBI6MO5J1D\""
					+ "}}")
		.when()
			.post("/v2/orders/")
		.then()
			.log().all()
			.statusCode(400)
			.body("errors[0].code", is("ORD-014"))
		;
	}
	
	// Estou recebendo o c�digo e descri��o do erro diferente da documenta��o: "code": "-"
	// Deveria me retornar o c�digo de erro: ORD-004
	// Devido a este probema n�o consigo realizar as outras valida��es.
	@Test
	public void naoDeveCriarPedidoSemInformarMoeda (){
		given()
			.log().all()
			.log().all()
			.header("Authorization", "Basic " + ACCESS_TOKEN)
			.body("{\"ownId\":\"0007\","
					+ "\"amount\":{\"currency\":\"EUR\","
					+ "\"subtotals\":{\"shipping\":1500}},"
					+ "\"items\":[{"
					+ "\"product\":\"C�mera fotogr�fica\","
					+ "\"category\":\"CAMERAS\","
					+ "\"quantity\":1,"
					+ "\"detail\":\"C�mera fotogr�fica, modelo CM54296, cor preta\","
					+ "\"price\":100000}],"
					+ "\"customer\":{"
					+ "\"id\":\"CUS-PPJBI6MO5J1D\""
					+ "}}")
		.when()
			.post("/v2/orders/")
		.then()
			.log().all()
			.statusCode(400)
			.body("errors[0].code", is("ORD-004"))
			.body("errors[0].path", is("amount.currency"))
			.body("errors[0].description", is("Valor inv�lido para o campo, consulte a documenta��o"))
		;
			
	}
	// Deveria me retornar o c�digo de erro: ORD-005
	// Devido a este probema n�o consigo realizar as outras valida��es.
	@Test
	public void naoDeveCriarPedidoComFrete0 (){
		given()
			.log().all()
			.log().all()
			.header("Authorization", "Basic " + ACCESS_TOKEN)
			.body("{\"ownId\":\"0007\","
					+ "\"amount\":{\"currency\":\"BRL\","
					+ "\"subtotals\":{\"shipping\":0}},"
					+ "\"items\":[{"
					+ "\"product\":\"C�mera fotogr�fica\","
					+ "\"category\":\"CAMERAS\","
					+ "\"quantity\":1,"
					+ "\"detail\":\"C�mera fotogr�fica, modelo CM54296, cor preta\","
					+ "\"price\":100000}],"
					+ "\"customer\":{"
					+ "\"id\":\"CUS-PPJBI6MO5J1D\""
					+ "}}")
		.when()
			.post("/v2/orders/")
		.then()
			.log().all()
			.statusCode(400)
			.body("errors[0].code", is("ORD-005"))
		;
			
	}

}
