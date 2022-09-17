package JIRA;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.io.File;

public class JiraTest {

	public static void main(String[] args) {
		
		RestAssured.baseURI = "http://localhost:8080";
		
		String expectedMessage = "Hi, How are you?";
		
		//Login Scenario
		
		SessionFilter session = new SessionFilter();  // Another way and more efficient way than JsonPath
		
		
		String response = given().relaxedHTTPSValidation().log().all().header("Content-Type","application/json").body("{ \"username\": \"manpreetgillqa\", \"password\": \"Manpreet4455\" }")
		.filter(session)  // Session Filter variable is passed here to store session in this variable
		.when().post("/rest/auth/1/session")
		.then().log().all().extract().response().asString();
		
		
		// Add comment
		
		String addCommentResponse = given().pathParam("key", "10003").log().all().header("Content-Type","application/json"). body("{\r\n"
				+ "    \"body\": \""+expectedMessage+"\",\r\n"
				+ "    \"visibility\": {\r\n"
				+ "        \"type\": \"role\",\r\n"
				+ "        \"value\": \"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}")
		.filter(session)
		.when().post("/rest/api/2/issue/{key}/comment")
		.then().log().all().assertThat().statusCode(201).extract().response().asString();
		
		JsonPath js1 = new JsonPath(addCommentResponse);
		String commentID = js1.getString("id");
		
		System.out.println("**********************************************");
		
		//Add attachment
		
//		given().header("X-Atlassian-Token","no-check").filter(session).pathParam("key", "10003")
//		.header("Content-Type","multipart/form-data")
//		.multiPart("file",new File("jira.txt")).when()
//		.post("rest/api/2/issue/{key}/attachments").then().log().all().assertThat().statusCode(200);
		
		
		//Get Issue
		
		//First one is with path parameters
		
//		String issueDetails = given().filter(session).pathParam("key", "10003").log().all().when().get("/rest/api/2/issue/{key}").then().log().all().extract().response().asString();
//		System.out.println(issueDetails);
		
		//Second one with query parameters
		
		String issueDetails = given().filter(session).pathParam("key", "10003").queryParam("fields", "comment").log().all().when().get("/rest/api/2/issue/{key}")
		.then().log().all().extract().response().asString();
		System.out.println(issueDetails);
		
		JsonPath js = new JsonPath(issueDetails);
		int commentCount = js.getInt("fields.comment.comments.size()");
		for(int i = 0 ; i < commentCount ; i++) {
			String commentIdIssue = js.get("fields.comment.comments["+i+"].id").toString();
			if(commentIdIssue.equalsIgnoreCase(commentID)) {
				String message = js.get("fields.comment.comments["+i+"].body").toString();
				System.out.println(message);
				Assert.assertEquals(message, expectedMessage);
			}
		}
		
	}

}
