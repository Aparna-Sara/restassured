package api.endpoints;

import static io.restassured.RestAssured.given;

import io.restassured.http.ContentType;
import io.restassured.matcher.RestAssuredMatchers.*;
import io.restassured.response.Response;

import static org.testng.Assert.assertEquals;

import org.hamcrest.Matchers.*;

import api.payload.User;

//created to perform CRUD operations like create, reade, update and delete

public class UserEndPoints {

	 private static final String BASE_URI = "https://jsonplaceholder.typicode.com"; 
	 // Using JSONPlaceholder for demo
	 
	
	 public static Response createUser(User payload) {
		
		 Response response =
				 
		 given()
		 .contentType(ContentType.JSON)
		 .accept(ContentType.JSON)
		 .body(payload)
		 
		 .when()
		 .post(Routes.POST_URL);
		 
		 return response;
	 }
	 
	 
	 
	 
	 public static Response GetUser() {
		 
		 Response response = 
		  given()
		 .when()
		 .get(Routes.GET_URL);
		 
		 return response;
	 }
	 
	 public static Response GetUser_param() {
		 
		 Response response = 
		  given()
		  
		  
		 .when()
		 .get(Routes.GET_URL_param);
		 
		 return response;
	 }
	 
	 
	 public static Response UpdateUser(User payload) {
		 
		Response response =
				
		given()
		 .contentType(ContentType.JSON)
		 .accept(ContentType.JSON)
		 .body(payload)
		 
		 .when()
		 .put(Routes.PUT_URL);
		 
		 return response;
		 	
		 
	 }
	 
	 
}
