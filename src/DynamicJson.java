import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;



public class DynamicJson {
	
	@Test(dataProvider = "BooksData")
	public void addBook(String isbn, String aisle ) {
		RestAssured.baseURI="http://216.10.245.166";
		
//		String response = given().header("Content-Type","application/json")
//		.body(payload.addBook())
//		.post("Library/Addbook.php")
//		.then().log().all().assertThat().statusCode(200).extract().response().asString();
//		JsonPath js = ReUsableMethods.rawToJson(response);
//		String ID = js.get("ID");
//		System.out.println(ID);
//		
//		
//		System.out.println("**************************************");
		//Update Author
		
//		String newAuthor = "Jatt";
//		
//		String UpdateResponse = given().header("Content-Type","application/json")
//		.body("{\r\n"
//				+ "\"name\":\"Learn Appium Automation with Java\",\r\n"
//				+ "\"isbn\":\"bcd\",\r\n"
//				+ "\"aisle\":\"227\",\r\n"
//				+ "\"author\":\""+newAuthor+"\"\r\n"
//				+ "}")
//		.when().put("Library/Addbook.php")
//		.then().assertThat().log().all().statusCode(200).extract().response().asString();
//		
//		JsonPath js2 = new JsonPath(UpdateResponse);
//		String updatedAuthor = js2.getString("author");
//		System.out.println(updatedAuthor);
//		Assert.assertEquals(updatedAuthor, newAuthor);
		

		
		 String response = given().header("Content-Type","application/json")
		.body(payload.addBook(isbn,aisle))
		.post("Library/Addbook.php")
		.then().log().all().assertThat().statusCode(200).extract().response().asString();
		JsonPath js1 = ReUsableMethods.rawToJson(response);
		String id = js1.get("ID");
		System.out.println(id);
		
		
		System.out.println("**************************************"); 
		
		//DeleteBook
		
		String deleteResponse = given().header("Content-Type","application/json").body(payload.addBook(isbn, aisle)).delete("Library/Addbook.php").then().log().all()
		.assertThat().statusCode(404).extract().response().asString();
		String deleteMessage = js1.get("msg");
		System.out.println(deleteMessage);
	
	}
	
	@DataProvider(name="BooksData")
	public Object[][] getData() {
		return new Object[][] {{"12","Paa"},{"22","Raa"},{"39","Taa"}};
	}
}
