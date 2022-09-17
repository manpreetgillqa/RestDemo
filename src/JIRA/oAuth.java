package JIRA;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

public class oAuth {

	public static void main(String[] args) throws InterruptedException {
		
		//Mendatory fields for GetAuthorization Code Request
		
//		System.setProperty("webdriver.chrome.driver", "C:/Dev_Tools/chromedriver_win32/chromedriver.exe");
//		
//		WebDriver driver = new ChromeDriver();
//		driver.get("https://accounts.google.com/o/oauth2/v2/auth/identifier?scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email&auth_url=https%3A%2F%2Faccounts.google.com%2Fo%2Foauth2%2Fv2%2Fauth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https%3A%2F%2Frahulshettyacademy.com%2FgetCourse.php&flowName=GeneralOAuthFlow");
//		driver.findElement(By.cssSelector("input[type='email']")).sendKeys("manpreetgill4455@gmail.com");
//		driver.findElement(By.cssSelector("input[type='email']")).sendKeys(Keys.ENTER);
//		Thread.sleep(3000);
//		
//		driver.findElement(By.cssSelector("input[type='password']")).sendKeys("GILL0009");
//		driver.findElement(By.cssSelector("input[type='email']")).sendKeys(Keys.ENTER);
//		Thread.sleep(4000);
//		String url =  driver.getCurrentUrl();
		
		//from 2020 , you can not automate GetAuthorization Code Request , we have to do it manually then copy the url from the browser to extract the code
		// request the developer to increase the lifetime scope of the url
		
		String url = "https://rahulshettyacademy.com/getCourse.php?code=4%2F0AdQt8qj0yzTmmtY4BiLCXS-omvTRUDBYUtt5FTt5nJB4IyfVpB-XYMFdFhGHMZY9aUrxhA&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=consent";
		String partialCode = url.split("code=")[1];
		String code = partialCode.split("&scope")[0];
		
		System.out.println("***************************************");
		System.out.println(code);
		
		//Mandatory fields to get GetAccessToken Request
		
		String accessTokenResponse = given().urlEncodingEnabled(false)
		.queryParams("code",code)
		.queryParams("client_id",	"692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
		.queryParams("client_secret","erZOWM9g3UtwNRj340YYaK_W")
		.queryParams("redirect_uri","https://rahulshettyacademy.com/getCourse.php")
		.queryParams("grant_type","authorization_code")
		.when().log().all()
		.post("https://www.googleapis.com/oauth2/v4/token").asString();
		
		JsonPath js = new JsonPath(accessTokenResponse);
		String accessToken = js.getString("access_token");
		System.out.println("***************************************");
		System.out.println(accessToken);
		
		
		//Actual Request
		
		String response = given().queryParam("access_token", accessToken)
		.when().log().all()
		.get("https://rahulshettyacademy.com/getCourse.php").asString();
		System.out.println("***************************************");
		System.out.println(response);
	}

}
