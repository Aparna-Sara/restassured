package api.tests;

import org.testng.annotations.Test;

import api.endpoints.UserEndPoints;
import api.payload.User;
import io.restassured.response.Response;

public class UserTests {

	User userpayload;
	
	//@Test(priority=2)
	public void test_GetUser() {
		Response response = UserEndPoints.GetUser();
		
		response.then().statusCode(200);
		response.then().log().all();
	}
	
	//@Test(priority=3)
	public void test_GetUser_param() {
		Response response = UserEndPoints.GetUser_param();
		response.then().log().all();
	}
	
	
	@Test(priority =1)
	public void test_PostUser() {
		userpayload = new User(5555,"Anusha","supero", 5555);
		
		Response response = UserEndPoints.createUser(userpayload);
		response.then().log().all();
	}
	
	//@Test(priority = 1)
	public void test_CreateAndRetrieveUser() {
	    // Create a user
	    userpayload = new User(5555, "Anusha", "supero", 5555);
	    Response createResponse = UserEndPoints.createUser(userpayload);
	    createResponse.then().log().all();
	    // Retrieve the user
	    Response retrieveResponse = UserEndPoints.GetUser_param();

	    // Assertions or logging for both responses
	    
	    retrieveResponse.then().log().all();
	}
	
	@Test
	public void test_UpdateUser() {
		
		userpayload = new User(5555, "Anusha", "supero", 5555);

		userpayload.setId(101);
		userpayload.setTitle("AnuAparna");
		userpayload.setBody("bodyy");
		userpayload.setUserId(555);
		
	Response update_response = UserEndPoints.UpdateUser(this.userpayload);	
	update_response.then().log().all();
	

	
	
	
		
	}
}
