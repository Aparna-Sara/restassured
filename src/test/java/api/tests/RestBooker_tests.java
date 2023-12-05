package api.tests;


import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.config.ObjectMapperConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import static org.hamcrest.Matchers.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import api.endpoints.RestBooker_endpoints;
import api.endpoints.Routes;
import api.payload.BasicAuth;
import api.payload.BookingDates;
import api.payload.BookingRequest;
import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
public class RestBooker_tests {
	
	BasicAuth authPayload;
	private String token_id;

	
	@Test(priority = 1)
	public void test_basicAuth(ITestContext context) {
		System.out.println("url--->" +Routes.AUTH_URL);
		
		Response response = 
				RestBooker_endpoints.create_authToken("admin", "password123");
		
		response.then().log().all();
		System.out.println("response:"+response.asPrettyString());
		String token_id = response.getBody().jsonPath().get("token");
		System.out.println("token-->"+token_id);
		
		response.then().statusCode(200);
		response.
			then().
			log().all()
			.statusCode(200)
			.body("$", hasKey("token"));
		
		context.setAttribute("token_id", token_id);
		
	}
	
	//@Test(priority = 2, dependsOnMethods = "test_basicAuth")
	public void test_getAllBookings(ITestContext context) {
		Response allBookings_resp = 
				RestBooker_endpoints.get_allBookingIds();
		//allBookings_resp.then().log().all();
		
	List<Integer> bookingIds = 
		allBookings_resp.jsonPath().getList("bookingid");
		System.out.println("size-->"+bookingIds.size());
		
		
		
	for(Integer bookingid : bookingIds) {
			String bookingidString = String.valueOf(bookingid);
			 int occurrences = Collections.frequency(bookingIds, bookingidString);
			// System.out.println("bookingid:" +bookingid +" occurrances:"+occurrences);
		}
	
		System.out.println("token_id"+context.getAttribute("token_id"));
	}
	
	
	//@Test
	public void test_getBookingId_filterByName() {
		Response response = 
				RestBooker_endpoints.get_FilterByName_BookingIds("sally", "brown");
		System.out.println(response.asPrettyString());
		response.then().statusCode(200);
		
	}
	
	//@Test
	public void test_getBookingId_filterByDate() throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date checkin1 = dateFormat.parse("2014-03-13");
		Date checkout1 = dateFormat.parse("2014-05-21");
		String checkin = dateFormat.format(checkin1);
		String checkout = dateFormat.format(checkout1);
		
		System.out.println("checkin::"+checkin +"checkout::"+checkout );
		
		Response response = 
				RestBooker_endpoints.get_FilterByDate_BookingIds(checkin,checkout);
		System.out.println(response.asPrettyString());
		response.then().statusCode(200);
		
	}
	
	//@Test
	public void test_getBookin() {
			Response resp = RestBooker_endpoints.get_booking("223");
			resp.then().log().all();
			//System.out.println("Resp:"+resp.asPrettyString());
			resp.then().statusCode(200);
			
			resp.then().assertThat().body(matchesJsonSchemaInClasspath("api/schemas/BookingId_response_Schema.json"));
			String actual_FN = resp.jsonPath().getString("firstname");
			Assert.assertEquals(actual_FN, "Josh");
	
	}
	
	
	//@Test
	public void test_create_booking(ITestContext context) throws ParseException, JsonProcessingException {
		BookingRequest reqPayload = new BookingRequest();
		
		reqPayload.setFirstname("Aparna");
		reqPayload.setLastname("Ashwin");
		reqPayload.setDepositpaid(true);
		reqPayload.setTotalprice(209);
		
		BookingDates bookingDates = new BookingDates();
		bookingDates.setCheckin(LocalDate.parse("2023-12-01"));
		bookingDates.setCheckout(LocalDate.parse("2023-12-02"));
		
		
		reqPayload.setBookingdates(bookingDates);
		reqPayload.setAdditionalneeds("coca cola");
		
		//serialization
		

		ObjectMapper objMapper = new ObjectMapper();
		objMapper.registerModule(new JavaTimeModule());
		
		String jsonpayload = objMapper.writeValueAsString(reqPayload);
		
		System.out.println("request payload:" +jsonpayload);
		
		Response response = RestBooker_endpoints.create_booking(reqPayload);
		System.out.println("response-->"+response.asPrettyString());
		 System.out.println("Response Headers:");
		    response.headers().asList().forEach(header -> System.out.println(header.getName() + ": " + header.getValue()));

		    response.then().assertThat().statusCode(200);
		    System.out.println("fn>>>"+response.jsonPath().getString("booking.firstname"));
		 Assert.assertEquals((response.jsonPath().getString("booking.firstname")),"Aparna");
		 System.out.println("tp>>>"+response.body().jsonPath().getInt("booking.totalprice"));
		 Assert.assertEquals(response.body().jsonPath().getInt("booking.totalprice"),209);
		 
		int booking_id = response.jsonPath().getInt("bookingid");
		System.out.println("Booking_id>>"+booking_id);
		
		context.setAttribute("booking_id", booking_id);
		
		
		
		//response.then().log().all();
	}

	
	@Test(dependsOnMethods = "test_basicAuth")
	public void test_updateBooking(ITestContext context) throws JsonProcessingException {
		
		String token_id = (String) context.getAttribute("token_id");
		System.out.println("token_id::::"+token_id);
		BookingRequest reqPayload = new BookingRequest();
		
		reqPayload.setFirstname("Aparna");
		reqPayload.setLastname("Advait");
		reqPayload.setDepositpaid(true);
		reqPayload.setTotalprice(209);
		
		BookingDates bookingDates = new BookingDates();
		bookingDates.setCheckin(LocalDate.parse("2023-12-01"));
		bookingDates.setCheckout(LocalDate.parse("2023-12-02"));
		
		
		reqPayload.setBookingdates(bookingDates);
		reqPayload.setAdditionalneeds("coca cola with french fries");
		
		ObjectMapper objMapper = new ObjectMapper();
		objMapper.registerModule(new JavaTimeModule());
		String jsonpayload = objMapper.writeValueAsString(reqPayload);
		
		System.out.println("reqpayload--->"+jsonpayload);
		
		Response response = RestBooker_endpoints.update_booking(reqPayload, 1, token_id);
		response.then().log().all();
		
		
		
		
	}
}
