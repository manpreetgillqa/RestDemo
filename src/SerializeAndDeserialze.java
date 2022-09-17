import io.restassured.RestAssured;
import io.restassured.response.Response;
import pojo.AddPlace;
import pojo.Location;
import pojo.responsePlace;

import static  io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.List;

public class SerializeAndDeserialze {
	
	
	// Need Gson , Jckson (databind,core and annotations - all 3 same version) 
	
	public static void main(String[] args) {
		
		AddPlace p = new AddPlace();
		p.setAccuracy(50);
		p.setAddress("29, side layout, cohen 09");
		p.setLanguage("French-IN");
		p.setPhone_number("(+91) 983 893 3937");
		p.setWebsite("https://rahulshettyacademy.com");
		p.setName("Frontline house");
		List<String> myList = new ArrayList<String>();
		myList.add("shoe park");
		myList.add("shop");
		p.setTypes(myList);
		
		Location l = new Location();
		l.setLat(-38.383494d);
		l.setLng(33.427362d);
		
		p.setLocation(l);
		
		
		RestAssured.baseURI="https://rahulshettyacademy.com";
		
		responsePlace res = given().log().all().queryParam("key", "qaclick123")
				.body(p)
				.when().post("/maps/api/place/add/json").as(responsePlace.class);
		
		System.out.println(res.getId());
		System.out.println(res.getStatus());
		System.out.println(res.getPlace_id());
		System.out.println(res.getScope());
		System.out.println(res.getReference());
	}

}
