package br.com.brunozava.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PedidosCartaoCancelledTests extends BaseTest{
	
	private static String ORDER_COD = "ORDER " + System.nanoTime();
	private static String ORDER_ID;
	
	@Test
	public void CEN_01_criarPedidoNovoTeste(){
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
	public void CEN_02_simularPagtoCancelled(){
		given()
			.log().all()
			.header("Authorization", "Basic " + ACCESS_TOKEN)
			.body("{\"installmentCount\":1,"
					+ "\"statementDescriptor\":\"Minha Loja\","
					+ "\"fundingInstrument\":{\"method\":\"CREDIT_CARD\","
					+ "\"creditCard\":{\"hash\":\"FOPjSDXT5By0KgG3JOAzpOVBd6lO/i2DxC5kB843HnD5oZbr5WeB0NIWPK+HKpQH8S3yqTKhde6Kldwv7eLQl8Kj6AxHeUi4GRkQCitKIDJ6QBrrarybke5XmCwQDVyz+8990BnT0tlxpTp/CgmLychEccvRBv7cHWgHQ9V05AzLpTskPDl6USWTc5k7gy74tbfgpSCyLqEtP0DvCiUd8604exzK9sLAnR4Yxn9exs1eJbwi86tEHuN+R+/1IwVgsaM9l4rEpVC0AjryT0ZBbSmcya2x4SrIXIspAKsrJnruLWG5h5ql9jNLWF4t7/TtA+yHmDTDy63btJ6quGjKHg==\","
					+ "\"store\":true,\"holder\":{\"fullname\":\"REJECT\","
					+ "\"birthdate\":\"1987-08-13\","
					+ "\"taxDocument\":{\"type\":\"CPF\",\"number\":\"78994193600\"},"
					+ "\"phone\":{\"countryCode\":\"55\",\"areaCode\":\"11\",\"number\":\"92222888\"},"
					+ "\"billingAddress\":{\"city\":\"Santo Andre\","
					+ "\"district\":\"Savassi\",\"street\":\"Avenida Contorno\","
					+ "\"streetNumber\":\"55\",\"zipCode\":\"09111220\","
					+ "\"state\":\"SP\",\"country\":\"BRA\"}}}},"
					+ "\"device\":{\"ip\":\"127.0.0.1\","
					+ "\"geolocation\":{\"latitude\":-33.867,\"longitude\":151.206},"
					+ "\"userAgent\":\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36\","
					+ "\"fingerprint\":\"QAZXswedCVGrtgBNHyujMKIkolpQAZXswedCVGrtgBNHyujMKIkolpQAZXswedCVGrtgBNHyujMKIkolpQAZXswedCVGrtgBNHyujMKIkolp\"}}")
			.pathParam("order_id", ORDER_ID)
		.when()
			.post("/v2/orders/{order_id}/payments")
		.then()
			.log().all()
			.statusCode(201)
			.body("fundingInstrument.method", is("CREDIT_CARD"))
			.body("status", is("IN_ANALYSIS"))
		;
	}
}