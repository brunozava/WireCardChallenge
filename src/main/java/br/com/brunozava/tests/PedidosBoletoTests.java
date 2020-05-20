package br.com.brunozava.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;


import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PedidosBoletoTests extends BaseTest{
	
	private static String ORDER_COD = "ORDER " + System.nanoTime();
	private static String ORDER_ID;
	private static String PAYMENT_ID;
	
	@Test
	public void CEN01_criarPedido(){
		ORDER_ID = given()
				.log().all()
				.header("Authorization", "Basic " + ACCESS_TOKEN)
				.body("{\"ownId\":\""+ORDER_COD+"\","
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
				.statusCode(201)
				.body("status", is("CREATED"))
				.body("ownId", is(ORDER_COD))
				.body("customer.id", is("CUS-PPJBI6MO5J1D"))
				.body("amount.paid", is(0))
				.body("amount.total", is(101500))
				.body("amount.fees", is(0))
				.body("amount.refunds", is(0))
				.body("amount.liquid", is(0))
				.body("amount.currency", is("BRL"))
				.body("amount.subtotals.shipping", is(1500))
				.body("amount.subtotals.addition", is(0))
				.body("amount.subtotals.discount", is(0))
				.body("items[0].product", is("Camisa"))
				.body("items[0].quantity", is(1))
				.body("items[0].category", is("CLOTHING"))
				.extract().path("id");
	}
	
	@Test
	public void CEN02_criarPagtoBoleto(){
		PAYMENT_ID = given()
			.log().all()
			.header("Authorization", "Basic " + ACCESS_TOKEN)
			.body("{\"statementDescriptor\":\"Minha Loja\","
					+ "\"fundingInstrument\":{\"method\":\"BOLETO\","
					+ "\"boleto\":{\"expirationDate\":\"2020-06-20\","
					+ "\"instructionLines\":{\"first\":\"Atenção,"
					+ "\",\"second\":\"fique atento à data de vencimento do boleto.\","
					+ "\"third\":\"Pague em qualquer casa lotérica.\"},"
					+ "\"logoUri\":\"http://www.lojaexemplo.com.br/logo.jpg\"}}}")
			.pathParam("order_id", ORDER_ID)
		.when()
			.post("/v2/orders/{order_id}/payments")
		.then()
			.log().all()
			.statusCode(201)
			.body("status", is("WAITING"))
			.body("amount.total", is(101500))
			.body("amount.gross", is(101500))
			.body("amount.fees", is(0))
			.body("amount.refunds", is(0))
			.body("amount.liquid", is(101500))
			.body("amount.currency", is("BRL"))
			.body("fundingInstrument.method", is("BOLETO"))
			.extract().path("id")

		;
	}
	
		@Test
		public void CEN03_consultarPagamentoAntesdePago(){
			given()
				.log().all()
				.header("Authorization", "Basic " + ACCESS_TOKEN)
				.pathParam("payment_id", PAYMENT_ID)
			.when()
				.get("/v2/payments/{payment_id}")
			.then()
				.log().all()
				.statusCode(200)
				.body("status", is("WAITING"))
				.body("amount.total", is(101500))
				.body("amount.gross", is(101500))
				.body("amount.fees", is(0))
				.body("amount.refunds", is(0))
				.body("amount.liquid", is(101500))
				.body("amount.currency", is("BRL"))
				.body("fundingInstrument.method", is("BOLETO"))
			;
		}
	
		@Test
		public void CEN04_simularPagamento(){
			System.out.println(ORDER_COD);
			given()
				.log().all()
				.header("Authorization", "Basic " + ACCESS_TOKEN)
				.pathParam("payment_id", PAYMENT_ID)
				.pathParam("valor", "150000")
			.when()
					.get("/simulador/authorize?payment_id={payment_id}&amount={valor}")
				
			.then()
				.log().all()
				.statusCode(200)
			;
		}
	
		@Test
		public void CEN05_consultaPagamentoDepoisDePago(){
			given()
				.log().all()
				.header("Authorization", "Basic " + ACCESS_TOKEN)
				.pathParam("payment_id", PAYMENT_ID)
			.when()
				.get("/v2/payments/{payment_id}")
			.then()
				.log().all()
				.statusCode(200)
				.body("status", is("AUTHORIZED"))
				.body("amount.total", is(150000))
				.body("amount.gross", is(150000))
				.body("amount.fees", is(0))
				.body("amount.refunds", is(0))
				.body("amount.liquid", is(101500))
				.body("amount.currency", is("BRL"))
				.body("fundingInstrument.method", is("BOLETO"))
			;
		}

}
