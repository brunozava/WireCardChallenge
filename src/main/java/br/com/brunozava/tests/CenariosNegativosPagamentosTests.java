package br.com.brunozava.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;




public class CenariosNegativosPagamentosTests extends BaseTest {
	
	@Test
	public void NaoDeveCriarPagtoSemToken(){
		given()
			.log().all()
			.body("{\"statementDescriptor\":\"Minha Loja\","
					+ "\"fundingInstrument\":{\"method\":\"BOLETO\","
					+ "\"boleto\":{\"expirationDate\":\"2020-06-20\","
					+ "\"instructionLines\":{\"first\":\"Atenção,"
					+ "\",\"second\":\"fique atento à data de vencimento do boleto.\","
					+ "\"third\":\"Pague em qualquer casa lotérica.\"},"
					+ "\"logoUri\":\"http://www.lojaexemplo.com.br/logo.jpg\"}}}")
			.pathParam("order_id", "1234567")
		.when()
			.post("/v2/orders/{order_id}/payments")
		.then()
			.log().all()
			.statusCode(401)
		;
	}
	
	@Test
	public void NaoDeveCriarPagtoComCodPedidoErrado(){
		given()
			.log().all()
			.header("Authorization", "Basic " + ACCESS_TOKEN)
			.body("{\"statementDescriptor\":\"Minha Loja\","
					+ "\"fundingInstrument\":{\"method\":\"BOLETO\","
					+ "\"boleto\":{\"expirationDate\":\"2020-06-20\","
					+ "\"instructionLines\":{\"first\":\"Atenção,"
					+ "\",\"second\":\"fique atento à data de vencimento do boleto.\","
					+ "\"third\":\"Pague em qualquer casa lotérica.\"},"
					+ "\"logoUri\":\"http://www.lojaexemplo.com.br/logo.jpg\"}}}")
			.pathParam("order_id", "1234567")
		.when()
			.post("/v2/orders/{order_id}/payments")
		.then()
			.log().all()
			.statusCode(404)
			.body("error", is("resource not found"))
		;
	}
	
	@Test
	public void naoDeveRealizarPagamentoComCodigoPagtoErrado(){
		given()
			.log().all()
			.header("Authorization", "Basic " + ACCESS_TOKEN)
			.pathParam("payment_id", "123456")
			.pathParam("valor", "150000")
		.when()
				.get("/simulador/authorize?payment_id={payment_id}&amount={valor}")
			
		.then()
			.log().all()
			.statusCode(404);
			assertThat("payment_id not found: 123456", is("payment_id not found: 123456"));
		;
	}
	
	@Test
	public void simularErroDescriptografarCC(){
		given()
			.log().all()
			.header("Authorization", "Basic " + ACCESS_TOKEN)
			.body("{\"installmentCount\":6,"
					+ "\"statementDescriptor\":\"Minha Loja\","
					+ "\"fundingInstrument\":{\"method\":\"CREDIT_CARD\","
					+ "\"creditCard\":{\"hash\":\"HhL0kbhfid+jwgj5l6Kt9EPdetDxQN8s7uKUHDYxDC/XoULjzik44rSda3EcWuOcL17Eb8JjWc1JI7gsuwg9P0rJv1mJQx+d3Dv1puQYz1iRjEWWhnB1bw0gTvnnC/05KbWN5M8oTiugmhVK02Rt2gpbcTtpS7VWyacfgesBJFavYYMljYg8p2YGHXkXrMuQiOCeemKLk420d0OTMBba27jDVVJ663HZDrObnjFXJH/4B5irkj+HO5genV+V4PYoLcOESG4nrI3oFAsMGsLLcdJo0NNvkEmJpn0e9GzureKKFYisYU+BEd9EMr/odS0VMvOYRV65HbPTspIkjl2+3Q==\","
					+ "\"store\":true,\"holder\":{\"fullname\":\"Roberto Oliveira\","
					+ "\"birthdate\":\"1988-12-30\","
					+ "\"taxDocument\":{\"type\":\"CPF\",\"number\":\"78994193600\"},"
					+ "\"phone\":{\"countryCode\":\"55\",\"areaCode\":\"11\",\"number\":\"22849560\"},"
					+ "\"billingAddress\":{\"city\":\"Belo Horizonte\","
					+ "\"district\":\"Savassi\",\"street\":\"Avenida Contorno\","
					+ "\"streetNumber\":\"400\",\"zipCode\":\"76932800\","
					+ "\"state\":\"MG\",\"country\":\"BRA\"}}}},\"device\":{\"ip\":\"127.0.0.1\","
					+ "\"geolocation\":{\"latitude\":-33.867,\"longitude\":151.206},"
					+ "\"userAgent\":\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36\","
					+ "\"fingerprint\":\"QAZXswedCVGrtgBNHyujMKIkolpQAZXswedCVGrtgBNHyujMKIkolpQAZXswedCVGrtgBNHyujMKIkolpQAZXswedCVGrtgBNHyujMKIkolp\"}}")
			.pathParam("order_id", "ORD4444444")
		.when()
			.post("/v2/orders/{order_id}/payments")
		.then()
			.log().all()
		;
		
	}
	
	@Test
	public void simularErroSemNomeCartao(){
		given()
			.log().all()
			.header("Authorization", "Basic " + ACCESS_TOKEN)
			.body("{\"installmentCount\":1,"
					+ "\"statementDescriptor\":\"Minha Loja\","
					+ "\"fundingInstrument\":{\"method\":\"CREDIT_CARD\","
					+ "\"creditCard\":{\"hash\":\"FOPjSDXT5By0KgG3JOAzpOVBd6lO/i2DxC5kB843HnD5oZbr5WeB0NIWPK+HKpQH8S3yqTKhde6Kldwv7eLQl8Kj6AxHeUi4GRkQCitKIDJ6QBrrarybke5XmCwQDVyz+8990BnT0tlxpTp/CgmLychEccvRBv7cHWgHQ9V05AzLpTskPDl6USWTc5k7gy74tbfgpSCyLqEtP0DvCiUd8604exzK9sLAnR4Yxn9exs1eJbwi86tEHuN+R+/1IwVgsaM9l4rEpVC0AjryT0ZBbSmcya2x4SrIXIspAKsrJnruLWG5h5ql9jNLWF4t7/TtA+yHmDTDy63btJ6quGjKHg==\","
					+ "\"store\":true,\"holder\":{\"fullname\":\"\","
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
		.when()
			.post("/v2/orders/ORD34444/payments")
		.then()
			.log().all()
			.statusCode(400)
			.body("errors.code", hasItems("CUS-004", "PAY-634", "PAY-635"))
			.body("errors.path", hasItem("fundingInstrument.creditCard.holder.fullname"))
			.body("errors.description", hasItems("O nome informado não pode conter só números", "O nome do portador do cartão não foi informado", "O nome do portador do cartão Deve ter no mínimo 3 caracteres e no máximo 65"))
		;
	}
	
	@Test
	public void simularErroSemDtNasc(){
		given()
		.log().all()
		.header("Authorization", "Basic " + ACCESS_TOKEN)
		.body("{\"installmentCount\":1,"
				+ "\"statementDescriptor\":\"Minha Loja\","
				+ "\"fundingInstrument\":{\"method\":\"CREDIT_CARD\","
				+ "\"creditCard\":{\"hash\":\"FOPjSDXT5By0KgG3JOAzpOVBd6lO/i2DxC5kB843HnD5oZbr5WeB0NIWPK+HKpQH8S3yqTKhde6Kldwv7eLQl8Kj6AxHeUi4GRkQCitKIDJ6QBrrarybke5XmCwQDVyz+8990BnT0tlxpTp/CgmLychEccvRBv7cHWgHQ9V05AzLpTskPDl6USWTc5k7gy74tbfgpSCyLqEtP0DvCiUd8604exzK9sLAnR4Yxn9exs1eJbwi86tEHuN+R+/1IwVgsaM9l4rEpVC0AjryT0ZBbSmcya2x4SrIXIspAKsrJnruLWG5h5ql9jNLWF4t7/TtA+yHmDTDy63btJ6quGjKHg==\","
				+ "\"store\":true,\"holder\":{\"fullname\":\"Bruno Zavanella\","
				//+ "\"birthdate\":\"1987-08-13\","
				+ "\"birthdate\":\"\","
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
	.when()
		.post("/v2/orders/ORD34444/payments")
	.then()
		.log().all()
		.statusCode(400)
		.body("errors.code", hasItem("PAY-033"))
		.body("errors.path", hasItem("fundingInstrument.creditCard.holder.validBirthdate"))
		.body("errors.description", hasItem("A data de nascimento do portador do cartão é inválido"))
	;
	}
	
	@Test
	public void simularErroCPFInvalido(){
		given()
		.log().all()
		.header("Authorization", "Basic " + ACCESS_TOKEN)
		.body("{\"installmentCount\":1,"
				+ "\"statementDescriptor\":\"Minha Loja\","
				+ "\"fundingInstrument\":{\"method\":\"CREDIT_CARD\","
				+ "\"creditCard\":{\"hash\":\"FOPjSDXT5By0KgG3JOAzpOVBd6lO/i2DxC5kB843HnD5oZbr5WeB0NIWPK+HKpQH8S3yqTKhde6Kldwv7eLQl8Kj6AxHeUi4GRkQCitKIDJ6QBrrarybke5XmCwQDVyz+8990BnT0tlxpTp/CgmLychEccvRBv7cHWgHQ9V05AzLpTskPDl6USWTc5k7gy74tbfgpSCyLqEtP0DvCiUd8604exzK9sLAnR4Yxn9exs1eJbwi86tEHuN+R+/1IwVgsaM9l4rEpVC0AjryT0ZBbSmcya2x4SrIXIspAKsrJnruLWG5h5ql9jNLWF4t7/TtA+yHmDTDy63btJ6quGjKHg==\","
				+ "\"store\":true,\"holder\":{\"fullname\":\"Bruno Zavanella\","
				+ "\"birthdate\":\"1987-08-13\","
				+ "\"taxDocument\":{\"type\":\"CPF\",\"number\":\"789941936005555\"},"
				+ "\"phone\":{\"countryCode\":\"55\",\"areaCode\":\"11\",\"number\":\"92222888\"},"
				+ "\"billingAddress\":{\"city\":\"Santo Andre\","
				+ "\"district\":\"Savassi\",\"street\":\"Avenida Contorno\","
				+ "\"streetNumber\":\"55\",\"zipCode\":\"09111220\","
				+ "\"state\":\"SP\",\"country\":\"BRA\"}}}},"
				+ "\"device\":{\"ip\":\"127.0.0.1\","
				+ "\"geolocation\":{\"latitude\":-33.867,\"longitude\":151.206},"
				+ "\"userAgent\":\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36\","
				+ "\"fingerprint\":\"QAZXswedCVGrtgBNHyujMKIkolpQAZXswedCVGrtgBNHyujMKIkolpQAZXswedCVGrtgBNHyujMKIkolpQAZXswedCVGrtgBNHyujMKIkolp\"}}")
	.when()
		.post("/v2/orders/ORD34444/payments")
	.then()
		.log().all()
		.statusCode(400)
		.body("errors.code", hasItem("PAY-036"))
		.body("errors.path", hasItem("fundingInstrument.creditCard.holder.taxDocument.validCpf"))
		.body("errors.description", hasItem("O CPF informado deve ter 11 números"))
	;
	}
	
	//Está me retornando um erro 500 ao invés de PAY-600
	@Test
	public void simularErroQtsParcelasInvalidas(){
		given()
		.log().all()
		.header("Authorization", "Basic " + ACCESS_TOKEN)
		.body("{\"installmentCount\":xx,"
				+ "\"statementDescriptor\":\"Minha Loja\","
				+ "\"fundingInstrument\":{\"method\":\"CREDIT_CARD\","
				+ "\"creditCard\":{\"hash\":\"FOPjSDXT5By0KgG3JOAzpOVBd6lO/i2DxC5kB843HnD5oZbr5WeB0NIWPK+HKpQH8S3yqTKhde6Kldwv7eLQl8Kj6AxHeUi4GRkQCitKIDJ6QBrrarybke5XmCwQDVyz+8990BnT0tlxpTp/CgmLychEccvRBv7cHWgHQ9V05AzLpTskPDl6USWTc5k7gy74tbfgpSCyLqEtP0DvCiUd8604exzK9sLAnR4Yxn9exs1eJbwi86tEHuN+R+/1IwVgsaM9l4rEpVC0AjryT0ZBbSmcya2x4SrIXIspAKsrJnruLWG5h5ql9jNLWF4t7/TtA+yHmDTDy63btJ6quGjKHg==\","
				+ "\"store\":true,\"holder\":{\"fullname\":\"Bruno Zavanella\","
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
	.when()
		.post("/v2/orders/ORD34444/payments")
	.then()
		.log().all()
		.statusCode(400)
		.body("errors.code", hasItem("PAY-600"))
	;
	}

}
