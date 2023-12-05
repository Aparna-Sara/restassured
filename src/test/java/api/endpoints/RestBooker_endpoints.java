package api.endpoints;
import static io.restassured.RestAssured.given;

import java.util.Date;

import api.payload.BasicAuth;
import api.payload.BookingRequest;
import io.restassured.http.ContentType;
import io.restassured.matcher.RestAssuredMatchers.*;
import io.restassured.response.Response;

public class RestBooker_endpoints {
	
	public static Response create_authToken(String username, String password) {
		BasicAuth authPayload = new BasicAuth(username, password);
		
		Response response = 
				given()
				.contentType(ContentType.JSON)
				.auth().basic(authPayload.getUsername(), authPayload.getPassword())
				.body(authPayload)
		
				.when()
					.post(Routes.AUTH_URL);
		
		return response;
	}
	
	
	public static Response get_allBookingIds() {
		
		Response allIds_response = 
				given()
				
				.when()
					.get(Routes.GET_ALL_BOOKINGIDS);
		
		return allIds_response;
		
		
	}
public static Response get_FilterByName_BookingIds(String fn, String ln) {
		
		Response allIds_response = 
				given()
					.queryParam("firstname", fn)
					.queryParam("lastname", ln)
				
				.when()
					.get(Routes.GET_ALL_BOOKINGIDS);
		
		return allIds_response;
		
		
	}

public static Response get_FilterByDate_BookingIds(String checkin, String checkout) {
	
	Response ids_response = 
			given()
				.queryParam("checkin", checkin)
				.queryParam("checkout", checkout)
			
			.when()
				.get(Routes.GET_ALL_BOOKINGIDS);
	
	return ids_response;
	
}


public static Response get_booking(String id) {
	
	System.out.println("URL --> "+Routes.GET_BOOKING);
	Response response = 
			given()
				.pathParam("id", id)
				.accept("application/json")
				
			.when()
				.get(Routes.GET_BOOKING);
	
	return response;
	
}


public static Response create_booking(BookingRequest payload) {
	System.out.println("URL --> "+Routes.CREATE_BOOKING);
	Response response =
			given()
				.contentType("application/json")
				.accept("application/json")
				.body(payload)
				
			.when()
				.post(Routes.CREATE_BOOKING);
	return response;
			
}

public static Response update_booking(BookingRequest payload, int id, String token_id) {
	System.out.println("URL -->"+Routes.UPDATE_BOOKING);
	
	Response response = 
			given()
			.contentType("application/json")
			.accept("application/json")
			.header("Cookie","token="+token_id)
			.body(payload)
			
			.when()
				.put(Routes.UPDATE_BOOKING,id);
	return response;
	
}

}
