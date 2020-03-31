package stepdefs.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

import java.util.HashMap;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import utils.TestBase;
import utils.api.CmnApiMethods;

public class PutRequestStepDefs extends TestBase {
	
	Scenario scn;
	String email = GetRandomString(10) + "@gmail.com";
	
	@Before
	public void SetUp(Scenario s) {
		this.scn = s;
	}
	
	@Given("I have a new user created in the system")
	public void i_have_a_new_user_created_in_the_system() {

		CmnApiMethods cmnApiMethod  = new CmnApiMethods();
		newUserID = cmnApiMethod.CreateNewUserInGoRestAPI();
		scn.write("New user created with id as: " + newUserID);
		
	}

	@When("I hit the api with put request to update the existing user details")
	public void i_hit_the_api_with_put_request_to_update_the_existing_user_details() {

		HashMap<String,String> hm_header = new HashMap<String,String>();
		hm_header.put("Content-Type", "application/json");

		String body_string = "{\n" + 
				"	\"email\":\""+ email +"\"\n" + 
				"}";
		scn.write("body sent as: " +  body_string);
		
		req_spec.headers(hm_header).body(body_string);	
		
		scn.write("End Point: " + "/public-api/users/" + newUserID);
		resp = req_spec.when().put("/public-api/users/" + newUserID);
		scn.write("Response Put Request: " + resp.asString());
	}

	@Then("API should update the user")
	public void api_should_update_the_user() {
		resp.then()
		.assertThat()
		.body("_meta.success", equalTo(true))
		.body("_meta.code", equalTo(200))
		.body("_meta.message", equalTo("OK. Everything worked as expected."))
		.body("result.email", equalTo(email));
	}

	@Then("get request to the user should return updated information")
	public void get_request_to_the_user_should_return_updated_information() {
		Response resp_get = given()
				.baseUri(server)
				.auth().oauth2(accessToken)
				.when()
				.get("/public-api/users/" + newUserID);
		scn.write("get reponse after put: " + resp_get.asString());
		
		resp_get.then()
				.assertThat()
				.statusCode(200)
				.body("_meta.success", equalTo(true))
				.body("_meta.code", equalTo(200))
				.body("_meta.message",equalTo("OK. Everything worked as expected."))
				.body("result", not(emptyArray()))
				.body("result.email", equalTo(email));

	}

	@When("I hit the api with put request and setting wrong email")
	public void i_hit_the_api_with_put_request_and_setting_wrong_email() {
		HashMap<String,String> hm_header = new HashMap<String,String>();
		hm_header.put("Content-Type", "application/json");

		String body_string = "{\n" + 
				"	\"email\":\""+ "wrongemail" +"\"\n" + 
				"}";
		scn.write("body sent as: " +  body_string);
		
		req_spec.headers(hm_header).body(body_string);	
		
		scn.write("End Point: " + "/public-api/users/" + newUserID);
		resp = req_spec.when().put("/public-api/users/" + newUserID);
		scn.write("Response Put Request: " + resp.asString());
	}

	@When("I hit the api with put request to update the incorrect user")
	public void i_hit_the_api_with_put_request_to_update_the_incorrect_user() {
		HashMap<String,String> hm_header = new HashMap<String,String>();
		hm_header.put("Content-Type", "application/json");

		String body_string = "{\n" + 
				"	\"email\":\""+ email +"\"\n" + 
				"}";
		scn.write("body sent as: " +  body_string);
		
		req_spec.headers(hm_header).body(body_string);	
		
		scn.write("End Point: " + "/public-api/users/" + "3535535353553645334543564534");
		resp = req_spec.when().put("/public-api/users/" + "3535535353553645334543564534");
		scn.write("Response Put Request: " + resp.asString());
	}


}
