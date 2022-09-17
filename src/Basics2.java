import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static  io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.Assert;

public class Basics2 {

	public static void main(String[] args) throws IOException {
		
		//validate if add place API is working as expected
		
		// Add place -> Update place with new Address -> Get place to validate if new address is present in response
		
		
		// 3 Principles
		
		// GIVEN - all input details
		// WHEN - Submit the API -  RESOURCE AND HTTP goes here
		// THEN - Validate the response
		// Content of the file to String -> content of file can covert into Byte -> Byte data to String -- Access data from a json file from an external source and apply in test
		
		
		
		RestAssured.baseURI="https://rahulshettyacademy.com";
		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body(new String(Files.readAllBytes(Paths.get("C:\\Users\\manpr\\OneDrive\\Desktop\\api.json"))))
		.when().post("/maps/api/place/add/json")                             // data coming from payload class getdata method
		.then().assertThat().statusCode(200).body("scope",equalTo("APP"))
		.header("server", "Apache/2.4.41 (Ubuntu)").extract().response().asString();
		
		System.out.println(response);
		JsonPath js = new JsonPath(response );  //For parsing Json
		String placeID = js.getString("place_id");
		System.out.println(placeID);
		
		// Update place
		String newAddress = "70 winter walk, Canada";
		
		given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body("{\r\n"
				+ "\"place_id\":\""+placeID+"\",\r\n"
				+ "\"address\":\""+newAddress+"\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}\r\n"
				+ "")
		.when().put("maps/api/place/update/json")
		.then().assertThat().log().all().statusCode(200).body("msg",equalTo("Address successfully updated"));
		
		//Get place to verify
		
		 String getPlaceResponse = given().log().all().queryParam("key", "qaclick123")
		.queryParam("place_id", placeID)
		.when().get("maps/api/place/get/json")
		.then().assertThat().log().all().statusCode(200).extract().response().asString();
		 
		 JsonPath js1 =  ReUsableMethods.rawToJson(getPlaceResponse);
		 String actualAddress = js1.getString("address");
		 System.out.println(actualAddress);
		 
		 Assert.assertEquals(actualAddress, newAddress);
	}

}
