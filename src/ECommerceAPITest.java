import static  io.restassured.RestAssured.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojo.LoginRequest;
import pojo.LoginResponse;
import pojo.OrderDetail;
import pojo.Orders;
public class ECommerceAPITest {

	public static void main(String[] args) {
		
		// Login
		
		RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").setContentType(ContentType.JSON).build();
		
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUserEmail("manpreetgill1001@yahoo.com");
		loginRequest.setUserPassword("Password1");
		
		RequestSpecification reqLogin = given().relaxedHTTPSValidation().log().all().spec(req).body(loginRequest);
		
		LoginResponse loginResponse = reqLogin.when().post("/api/ecom/auth/login").then().log().all().extract().response().as(LoginResponse.class);
		
		System.out.println(loginResponse.getToken());
		String token = loginResponse.getToken();
		System.out.println(loginResponse.getMessage());
		String userId = loginResponse.getUserId();
		System.out.println(loginResponse.getUserId());
		
		//Add Product
		
		RequestSpecification addProductBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addHeader("Authorization", token).build();
		
		RequestSpecification reqAddProduct = given().log().all().spec(addProductBaseReq).param("productName", "leafs").param("productAddedBy", userId).param("productCategory", "fashion")
		.param("productSubCategory","shirts").param("productPrice", "11500").param("productDescription","Addias Originals").param("productFor", "women")
		.multiPart("productImage",new File ("C:\\Users\\manpr\\Postman\\files\\Untitled.png"));
		
		String addProductResponse = reqAddProduct.when().post("/api/ecom/product/add-product").then().log().all().extract().response().asString();
		JsonPath js = new JsonPath(addProductResponse);
		String productId = js.get("productId");
		
		//Create Order
		
		RequestSpecification createOrderBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addHeader("Authorization", token)
				.setContentType(ContentType.JSON).build();
		
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setCountry("Canada");
		orderDetail.setProductOrderedId(productId);
		
		List <OrderDetail> orderDetailList = new ArrayList <OrderDetail> ();
		orderDetailList.add(orderDetail);
		
		Orders o = new Orders();
		o.setOrders(orderDetailList);
		
		RequestSpecification createOrderReq =given().log().all().spec(createOrderBaseReq).body(o);	
		String responseAddOrder = createOrderReq.when().post("/api/ecom/order/create-order").then().log().all().extract().response().asString();
		System.out.println(responseAddOrder);
		JsonPath js2 = new JsonPath(responseAddOrder);
		
	
		// View Order
		
//	   RequestSpecification viewOrderBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addHeader("Authorization",token).addQueryParam("id", orderId).build();
//	   String viewOrderResponse = given().log().all().spec(viewOrderBaseReq).when().get("/api/ecom/order/get-orders-details")
//			   					.then().log().all().extract().response().asString();
//	   JsonPath js1 = new JsonPath(viewOrderResponse);
//	   String message = js1.get("message");
//	   System.out.println(message);
	   
	   // Delete Product
	   
	   RequestSpecification deleteProdBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addHeader("Authorization", token)
				.setContentType(ContentType.JSON).build();
	   
	   RequestSpecification deleteProdReq = given().log().all().spec(deleteProdBaseReq).pathParam("productId", productId);
	   String deleteProductResponse = deleteProdReq.when().delete("/api/ecom/product/delete-product/{productId}").then().log().all().extract().response().asString();
	   JsonPath js3 = new JsonPath(deleteProductResponse);
	   String deleteMessage = js3.get("message");
	   Assert.assertEquals(deleteMessage, "Product Deleted Successfully");
	}	
}
