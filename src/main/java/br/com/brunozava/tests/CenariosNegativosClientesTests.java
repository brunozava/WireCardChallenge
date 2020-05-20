package br.com.brunozava.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import org.junit.Test;


public class CenariosNegativosClientesTests extends BaseTest {
	
	@Test
	public void naoDevePermitirCadastroUsuarioSemToken(){
		given()
			.log().all()
			.body("{\"ownId\":\"001\","
					+ "\"fullname\":\"Zava test\","
					+ "\"email\":\"zava@zava.com\","
					+ "\"birthDate\":\"1990-10-22\","
					+ "\"taxDocument\":{\"type\":\"CPF\",\"number\":\"41902632060\"},"
					+ "\"phone\":{\"countryCode\":\"55\","
					+ "\"areaCode\":\"11\",\"number\":\"55552266\"},"
					+ "\"shippingAddress\":{\"city\":\"S\u00e3o Paulo\","
					+ "\"complement\":\"10\",\"district\":\"Itaim Bibi\","
					+ "\"street\":\"Avenida Faria Lima\","
					+ "\"streetNumber\":\"500\",\"zipCode\":\"01451000\","
					+ "\"state\":\"SP\",\"country\":\"BRA\"}}")
		.when()
			.post("/v2/customers/")
		.then()
			.log().all()
			.statusCode(401)
		;
	}
	@Test
	public void naoDevePermitirCadastroUsuarioIDDuplicado(){
		given()
			.log().all()
			.header("Authorization", "Basic " + ACCESS_TOKEN)
			.body("{\"ownId\":\"001\","
					+ "\"fullname\":\"Zava test\","
					+ "\"email\":\"zava@zava.com\","
					+ "\"birthDate\":\"1990-10-22\","
					+ "\"taxDocument\":{\"type\":\"CPF\",\"number\":\"41902632060\"},"
					+ "\"phone\":{\"countryCode\":\"55\","
					+ "\"areaCode\":\"11\",\"number\":\"55552266\"},"
					+ "\"shippingAddress\":{\"city\":\"S\u00e3o Paulo\","
					+ "\"complement\":\"10\",\"district\":\"Itaim Bibi\","
					+ "\"street\":\"Avenida Faria Lima\","
					+ "\"streetNumber\":\"500\",\"zipCode\":\"01451000\","
					+ "\"state\":\"SP\",\"country\":\"BRA\"}}")
		.when()
			.post("/v2/customers/")
		.then()
			.log().all()
			.statusCode(400)
			.body("errors[0].code", is("CUS-008"))
			.body("errors[0].path", is("customer.ownId"))
			.body("errors[0].description", is("O identificador prßprio deve ser único, j¹ existe um customer com o identificador informado"))
		;
	}
	
	@Test
	public void naoDevePermitirCadastroUsuarioSemId(){
		given()
			.log().all()
			.header("Authorization", "Basic " + ACCESS_TOKEN)
			.body("{\"ownId\":\"\","
					+ "\"fullname\":\"Zava test\","
					+ "\"email\":\"zava@zava.com\","
					+ "\"birthDate\":\"1990-10-22\","
					+ "\"taxDocument\":{\"type\":\"CPF\",\"number\":\"41902632060\"},"
					+ "\"phone\":{\"countryCode\":\"55\","
					+ "\"areaCode\":\"11\",\"number\":\"55552266\"},"
					+ "\"shippingAddress\":{\"city\":\"S\u00e3o Paulo\","
					+ "\"complement\":\"10\",\"district\":\"Itaim Bibi\","
					+ "\"street\":\"Avenida Faria Lima\","
					+ "\"streetNumber\":\"500\",\"zipCode\":\"01451000\","
					+ "\"state\":\"SP\",\"country\":\"BRA\"}}")
		.when()
			.post("/v2/customers/")
		.then()
			.log().all()
			.statusCode(400)
			.body("errors[0].code", is("CUS-001"))
			.body("errors[0].path", is("ownIdNotBlank"))
			.body("errors[0].description", is("O identificador próprio não foi informado"))
		;
	}
	
	@Test
	public void naoDevePermitirCadastroUsuarioSemNome(){
		given()
			.log().all()
			.header("Authorization", "Basic " + ACCESS_TOKEN)
			.body("{\"ownId\":\"005\","
					+ "\"fullname\":\"\","
					+ "\"email\":\"zava@zava.com\","
					+ "\"birthDate\":\"1990-10-22\","
					+ "\"taxDocument\":{\"type\":\"CPF\",\"number\":\"41902632060\"},"
					+ "\"phone\":{\"countryCode\":\"55\","
					+ "\"areaCode\":\"11\",\"number\":\"55552266\"},"
					+ "\"shippingAddress\":{\"city\":\"S\u00e3o Paulo\","
					+ "\"complement\":\"10\",\"district\":\"Itaim Bibi\","
					+ "\"street\":\"Avenida Faria Lima\","
					+ "\"streetNumber\":\"500\",\"zipCode\":\"01451000\","
					+ "\"state\":\"SP\",\"country\":\"BRA\"}}")
		.when()
			.post("/v2/customers/")
		.then()
			.log().all()
			.statusCode(400)
			.body("errors[0].code", is("CUS-002"))
			.body("errors[0].path", is("fullnameNotBlank"))
			.body("errors[0].description", is("O nome não foi informado"))
		;
	}
	
	@Test
	public void naoDevePermitirCadastroUsuarioSemEmail(){
		given()
			.log().all()
			.header("Authorization", "Basic " + ACCESS_TOKEN)
			.body("{\"ownId\":\"001\","
					+ "\"fullname\":\"Zava test\","
					+ "\"email\":\"\","
					+ "\"birthDate\":\"1990-10-22\","
					+ "\"taxDocument\":{\"type\":\"CPF\",\"number\":\"41902632060\"},"
					+ "\"phone\":{\"countryCode\":\"55\","
					+ "\"areaCode\":\"11\",\"number\":\"55552266\"},"
					+ "\"shippingAddress\":{\"city\":\"S\u00e3o Paulo\","
					+ "\"complement\":\"10\",\"district\":\"Itaim Bibi\","
					+ "\"street\":\"Avenida Faria Lima\","
					+ "\"streetNumber\":\"500\",\"zipCode\":\"01451000\","
					+ "\"state\":\"SP\",\"country\":\"BRA\"}}")
		.when()
			.post("/v2/customers/")
		.then()
			.log().all()
			.statusCode(400)
			.body("errors[0].code", is("CUS-005"))
			.body("errors[0].path", is("emailNotBlank"))
			.body("errors[0].description", is("O e-mail não foi informado"))
		;
	}
	
	@Test
	public void naoDevePermitirCadastroUsuarioEmailInvalido(){
		given()
			.log().all()
			.header("Authorization", "Basic " + ACCESS_TOKEN)
			.body("{\"ownId\":\"0044\","
					+ "\"fullname\":\"Zava test\","
					+ "\"email\":\"zava&&%%zava\","
					+ "\"birthDate\":\"1990-10-22\","
					+ "\"taxDocument\":{\"type\":\"CPF\",\"number\":\"41902632060\"},"
					+ "\"phone\":{\"countryCode\":\"55\","
					+ "\"areaCode\":\"11\",\"number\":\"55552266\"},"
					+ "\"shippingAddress\":{\"city\":\"S\u00e3o Paulo\","
					+ "\"complement\":\"10\",\"district\":\"Itaim Bibi\","
					+ "\"street\":\"Avenida Faria Lima\","
					+ "\"streetNumber\":\"500\",\"zipCode\":\"01451000\","
					+ "\"state\":\"SP\",\"country\":\"BRA\"}}")
		.when()
			.post("/v2/customers/")
		.then()
			.log().all()
			.statusCode(400)
			.body("errors[0].code", is("CUS-006"))
			.body("errors[0].path", is("emailValid"))
			.body("errors[0].description", is("O e-mail informado é inválido"))
		;
	}
	
	@Test
	public void naoDevePermitirCadastroUsuarioSemDtNasc(){
		given()
			.log().all()
			.header("Authorization", "Basic " + ACCESS_TOKEN)
			.body("{\"ownId\":\"0088\","
					+ "\"fullname\":\"Zava test\","
					+ "\"email\":\"zava@zava.com\","
					+ "\"birthDate\":\"\","
					+ "\"taxDocument\":{\"type\":\"CPF\",\"number\":\"41902632060\"},"
					+ "\"phone\":{\"countryCode\":\"55\","
					+ "\"areaCode\":\"11\",\"number\":\"55552266\"},"
					+ "\"shippingAddress\":{\"city\":\"S\u00e3o Paulo\","
					+ "\"complement\":\"10\",\"district\":\"Itaim Bibi\","
					+ "\"street\":\"Avenida Faria Lima\","
					+ "\"streetNumber\":\"500\",\"zipCode\":\"01451000\","
					+ "\"state\":\"SP\",\"country\":\"BRA\"}}")
		.when()
			.post("/v2/customers/")
		.then()
			.log().all()
			.statusCode(400)
			.body("errors[0].code", is("CUS-007"))
			.body("errors[0].path", is("birthdateMatchesPattern"))
			.body("errors[0].description", is("A data de nascimento informada é inválida"))
		;
	}
	
	@Test
	public void naoDevePermitirCadastroUsuarioDtNascInvalida(){
		given()
			.log().all()
			.header("Authorization", "Basic " + ACCESS_TOKEN)
			.body("{\"ownId\":\"00188\","
					+ "\"fullname\":\"Zava test\","
					+ "\"email\":\"zava@zava.com\","
					+ "\"birthDate\":\"2050-10-22\","
					+ "\"taxDocument\":{\"type\":\"CPF\",\"number\":\"41902632060\"},"
					+ "\"phone\":{\"countryCode\":\"55\","
					+ "\"areaCode\":\"11\",\"number\":\"55552266\"},"
					+ "\"shippingAddress\":{\"city\":\"S\u00e3o Paulo\","
					+ "\"complement\":\"10\",\"district\":\"Itaim Bibi\","
					+ "\"street\":\"Avenida Faria Lima\","
					+ "\"streetNumber\":\"500\",\"zipCode\":\"01451000\","
					+ "\"state\":\"SP\",\"country\":\"BRA\"}}")
		.when()
			.post("/v2/customers/")
		.then()
			.log().all()
			.statusCode(400)
			.body("errors[0].code", is("CUS-007"))
			.body("errors[0].path", is("birthdateMatchesPattern"))
			.body("errors[0].description", is("A data de nascimento informada é inválida"))
		;
	}
	
	//Deveria me retornar o código de erro: CUS-022;
	//Devido a este probema não consigo realizar as outras validações.
	@Test
	public void naoDevePermitirCadastroUsuarioSemInformarRua(){
		given()
			.log().all()
			.header("Authorization", "Basic " + ACCESS_TOKEN)
			.body("{\"ownId\":\"0095\","
					+ "\"fullname\":\"Zava test\","
					+ "\"email\":\"zava@zava.com\","
					+ "\"birthDate\":\"1990-10-22\","
					+ "\"taxDocument\":{\"type\":\"CPF\",\"number\":\"41902632060\"},"
					+ "\"phone\":{\"countryCode\":\"55\","
					+ "\"areaCode\":\"11\",\"number\":\"55552266\"},"
					+ "\"shippingAddress\":{\"city\":\"S\u00e3o Paulo\","
					+ "\"complement\":\"10\",\"district\":\"Itaim Bibi\","
					+ "\"street\":\"\","
					+ "\"streetNumber\":\"500\",\"zipCode\":\"01451000\","
					+ "\"state\":\"SP\",\"country\":\"BRA\"}}")
		.when()
			.post("/v2/customers/")
		.then()
			.log().all()
			.statusCode(400)
			.body("errors[0].code", is("CUS-022"))
		;
	}
	
	//Não deveria me retornar um erro devido a não passagem de CPF?
	//Devido a este probema não consigo realizar as outras validações.
	@Test
	public void naoDevePermitirCadastroUsuarioSemCPF(){
		given()
			.log().all()
			.header("Authorization", "Basic " + ACCESS_TOKEN)
			.body("{\"ownId\":\"0089\","
					+ "\"fullname\":\"Zava test\","
					+ "\"email\":\"zava@zava.com\","
					+ "\"birthDate\":\"1990-10-22\","
					+ "\"taxDocument\":{\"type\":\"CPF\",\"number\":\"\"},"
					+ "\"phone\":{\"countryCode\":\"55\","
					+ "\"areaCode\":\"11\",\"number\":\"55552266\"},"
					+ "\"shippingAddress\":{\"city\":\"S\u00e3o Paulo\","
					+ "\"complement\":\"10\",\"district\":\"Itaim Bibi\","
					+ "\"street\":\"Avenida Faria Lima\","
					+ "\"streetNumber\":\"500\",\"zipCode\":\"01451000\","
					+ "\"state\":\"SP\",\"country\":\"BRA\"}}")
		.when()
			.post("/v2/customers/")
		.then()
			.log().all()
			.statusCode(400)
		;
	}
	
	//Deveria me retornar o código de erro: CUS-018;
	//Devido a este probema não consigo realizar as outras validações.
	@Test
	public void naoDevePermitirCadastroUsuarioSemTelefone(){
		given()
			.log().all()
			.header("Authorization", "Basic " + ACCESS_TOKEN)
			.body("{\"ownId\":\"004\","
					+ "\"fullname\":\"Zava test\","
					+ "\"email\":\"zava@zava.com\","
					+ "\"birthDate\":\"1990-10-22\","
					+ "\"taxDocument\":{\"type\":\"CPF\",\"number\":\"41902632060\"},"
					+ "\"phone\":{\"countryCode\":\"55\","
					+ "\"areaCode\":\"11\",\"number\":\"\"},"
					+ "\"shippingAddress\":{\"city\":\"S\u00e3o Paulo\","
					+ "\"complement\":\"10\",\"district\":\"Itaim Bibi\","
					+ "\"street\":\"Avenida Faria Lima\","
					+ "\"streetNumber\":\"500\",\"zipCode\":\"01451000\","
					+ "\"state\":\"SP\",\"country\":\"BRA\"}}")
		.when()
			.post("/v2/customers/")
		.then()
			.log().all()
			.statusCode(400)
			.body("errors[0].code", is("CUS-0018"))
		;
	}
	
	//Deveria me retornar o código de erro: CUS-032;
	//Devido a este probema não consigo realizar as outras validações.
	@Test
	public void naoDevePermitirCadastroUsuarioSemInformarPais(){
		given()
			.log().all()
			.header("Authorization", "Basic " + ACCESS_TOKEN)
			.body("{\"ownId\":\"0088\","
					+ "\"fullname\":\"Zava test\","
					+ "\"email\":\"zava@zava.com\","
					+ "\"birthDate\":\"1990-10-22\","
					+ "\"taxDocument\":{\"type\":\"CPF\",\"number\":\"41902632060\"},"
					+ "\"phone\":{\"countryCode\":\"\","
					+ "\"areaCode\":\"11\",\"number\":\"55552266\"},"
					+ "\"shippingAddress\":{\"city\":\"S\u00e3o Paulo\","
					+ "\"complement\":\"10\",\"district\":\"Itaim Bibi\","
					+ "\"street\":\"Avenida Faria Lima\","
					+ "\"streetNumber\":\"500\",\"zipCode\":\"01451000\","
					+ "\"state\":\"SP\",\"country\":\"BRA\"}}")
		.when()
			.post("/v2/customers/")
		.then()
			.log().all()
			.statusCode(400)
			.body("errors[0].code", is("CUS-032"))
			;
	}
	
	//Deveria me retornar o código de erro: CUS-030;
	//Devido a este probema não consigo realizar as outras validações.
	@Test
	public void naoDevePermitirCadastroUsuarioSemInformarEstado(){
		given()
			.log().all()
			.header("Authorization", "Basic " + ACCESS_TOKEN)
			.body("{\"ownId\":\"0076\","
					+ "\"fullname\":\"Zava test\","
					+ "\"email\":\"zava@zava.com\","
					+ "\"birthDate\":\"1990-10-22\","
					+ "\"taxDocument\":{\"type\":\"CPF\",\"number\":\"41902632060\"},"
					+ "\"phone\":{\"countryCode\":\"55\","
					+ "\"areaCode\":\"11\",\"number\":\"55552266\"},"
					+ "\"shippingAddress\":{\"city\":\"S\u00e3o Paulo\","
					+ "\"complement\":\"10\",\"district\":\"Itaim Bibi\","
					+ "\"street\":\"Avenida Faria Lima\","
					+ "\"streetNumber\":\"500\",\"zipCode\":\"01451000\","
					+ "\"state\":\"\",\"country\":\"BRA\"}}")
		.when()
			.post("/v2/customers/")
		.then()
			.log().all()
			.statusCode(400)
			.body("errors[0].code", is("CUS-030"))
		;
	}
	
}
