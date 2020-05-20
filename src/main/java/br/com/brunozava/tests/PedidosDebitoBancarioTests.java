package br.com.brunozava.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PedidosDebitoBancarioTests extends BaseTest {
	
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
						+ "\"product\":\"Câmera fotográfica\","
						+ "\"category\":\"CAMERAS\","
						+ "\"quantity\":1,"
						+ "\"detail\":\"Câmera fotográfica, modelo CM54296, cor preta\","
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
				.extract().path("id");
		
		
	}
	
	@Test
	public void CEN02_criarPagtoDebitoOnline(){
		PAYMENT_ID = given()
			.log().all()
			.header("Authorization", "Basic " + ACCESS_TOKEN)
			.body("{\"fundingInstrument\":{\"method\":\"ONLINE_BANK_DEBIT\","
					+ "\"onlineBankDebit\":{\"bankNumber\":341,"
					+ "\"expirationDate\":\"2020-10-22\"}}}")
			.pathParam("order_id", ORDER_ID)
		.when()
			.post("/v2/orders/{order_id}/payments")
		.then()
			.log().all()
			.statusCode(201)
			.body("status", is("WAITING"))
			.body("amount.total", is(101500))
			.body("amount.gross", is(101500))
			.body("amount.fees", is(3611))
			.body("amount.refunds", is(0))
			.body("amount.liquid", is(97889))
			.body("amount.currency", is("BRL"))
			.body("fundingInstrument.method", is("ONLINE_BANK_DEBIT"))
			.extract().path("id")
		;
	}
	
	@Test
	public void CEN03_consultarPagamentoAntesdePago(){
		System.out.println(ORDER_COD);
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
			.body("amount.fees", is(3611))
			.body("amount.refunds", is(0))
			.body("amount.liquid", is(97889))
			.body("amount.currency", is("BRL"))
			.body("fundingInstrument.method", is("ONLINE_BANK_DEBIT"))
		;
	}
	
		@Test
		public void CEN04_simularPagamento(){
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
				.body("id", is(PAYMENT_ID))
				.body("status", is("AUTHORIZED"))
				.body("amount.total", is(101500))
				.body("amount.gross", is(101500))
				.body("amount.fees", is(3611))
				.body("amount.refunds", is(0))
				.body("amount.liquid", is(97889))
				.body("amount.currency", is("BRL"))
				.body("fundingInstrument.method", is("ONLINE_BANK_DEBIT"))
			;
		}
	
	

}
