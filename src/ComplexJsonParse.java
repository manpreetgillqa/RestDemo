import org.testng.Assert;

import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {

	public static void main(String[] args) {
		
		JsonPath js2 = new JsonPath(payload.CoursePrice());
		//Print the number of courses
		
		int count = js2.getInt("courses.size()");
		System.out.println(count);
		
		//Print purchase amount
		
		int totalAmount = js2.getInt("dashboard.purchaseAmount");
		System.out.println(totalAmount);
		
		//Print Title of the first course
		
		String courseTitle1 = js2.getString("courses.title[0]");
		System.out.println(courseTitle1);
		
		// OR
		
		String titleFirstCourse = js2.get("courses[0].title");
		System.out.println(titleFirstCourse);
		
		// Print All course titles and their respective Prices
		
		for(int i = 0 ; i < count ; i++) {  			// count = "courses.size()"
			String courseTitles = js2.get("courses["+i+"].title");
			int coursePrices =  js2.getInt("courses["+i+"].price");
			System.out.println(courseTitles + " " + coursePrices );
		}

		// Print no of copies sold by RPA Course
		
		for(int j = 0 ; j < count ; j++) {
			String courseTitle = js2.get("courses["+j+"].title");
			if( courseTitle.equalsIgnoreCase("RPA")) {
				int countOfCopies = js2.getInt("courses["+j+"].copies");
				System.out.println(countOfCopies);
				break;
			}
		}
	
		// Verify if Sum of all Course prices matches with Purchase Amount
		int sum = 0;
		for(int k = 0 ; k < count ; k++) {
			int price = js2.getInt("courses["+k+"].price");
			int copies = js2.getInt("courses["+k+"].copies");
			int amount = price * copies ;
			System.out.println(amount);
			sum = sum + amount;
			
		}
		System.out.println(sum);
		Assert.assertEquals(sum, totalAmount);  // totalAmount = "dashboard.purchaseAmount"
		}

}
