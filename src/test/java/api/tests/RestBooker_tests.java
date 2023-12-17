package api.tests;


import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.config.ObjectMapperConfig;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import static org.hamcrest.Matchers.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import api.endpoints.RestBooker_endpoints;
import api.endpoints.Routes;
import api.payload.BasicAuth;
import api.payload.BookingDates;
import api.payload.BookingRequest;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import utils.ExtentReportsManager;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
public class RestBooker_tests {
	
	BasicAuth authPayload;
	private static ExtentReports extent;
    private static ExtentTest test;
	
	
	@BeforeClass
	public void setUp() {
		
		extent = ExtentReportsManager.createInstance();
        test = ExtentReportsManager.createTest(getClass().getSimpleName());

	}
	
	@AfterClass
	public void tearDown() {
		
		extent.flush(); 
	}
	@AfterMethod
	public void afterMethod(ITestResult result) {
	    // Example of marking the test as passed or failed
	    if (result.getStatus() == ITestResult.SUCCESS) {
	        test.log(Status.PASS, "Test Passed");
	    } else if (result.getStatus() == ITestResult.FAILURE) {
	        test.log(Status.FAIL, "Test Failed");
	        test.log(Status.FAIL, "Failure Details: " + result.getThrowable());
	    }
	}

	
	@Test(priority = 1)
	public void test_basicAuth(ITestContext context) {
		System.out.println("Auth url--->" +Routes.AUTH_URL);
		
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
	
	@Test(priority = 2, dependsOnMethods = "test_basicAuth")
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
	
	
	@Test
	public void test_getBookingId_filterByName() {
		Response response = 
				RestBooker_endpoints.get_FilterByName_BookingIds("sally", "brown");
		System.out.println(response.asPrettyString());
		response.then().statusCode(200);
		test.pass("Test passed"); 
	}
	
	@Test
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
	
	@Test
	public void test_getBookin() {
			Response resp = RestBooker_endpoints.get_booking("112");
			resp.then().log().all();
			//System.out.println("Resp:"+resp.asPrettyString());
			resp.then().statusCode(200);
			
			resp.then().assertThat().body(matchesJsonSchemaInClasspath("api/schemas/BookingId_response_Schema.json"));
			String actual_FN = resp.jsonPath().getString("firstname");
			Assert.assertEquals(actual_FN, "Josh");
	
	}
	
	
	@Test(priority =1)
	public void test_create_booking(ITestContext context) throws ParseException, JsonProcessingException {
		System.out.println("create test-----");
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
		objMapper.registerModule(new Jdk8Module());
		
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

	
	@Test(priority =2,dependsOnMethods = "test_basicAuth")
	public void test_updateBooking(ITestContext context) throws JsonProcessingException {
		System.out.println("update test-----");
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
	 objMapper.registerModule(new Jdk8Module());
		String jsonpayload = objMapper.writeValueAsString(reqPayload);
		
		System.out.println("reqpayload--->"+jsonpayload);
		
		Response response = RestBooker_endpoints.update_booking(reqPayload, 11, token_id);
		response.then().log().all();
		
		
	}
	
	
	@Test(priority=3, dependsOnMethods = "test_basicAuth")
	public void test_partialUpdate_booking(ITestContext context) throws JsonProcessingException {
		
//		Map<String,Object> patchPayload = new HashMap<String,Object>();
//		patchPayload.put("firstname", "Aglio");
//		patchPayload.put("lastname", "Olio");
		
		ObjectNode patchPayload = JsonNodeFactory.instance.objectNode();
		patchPayload.put("firstname", "Ombre");
		patchPayload.put("lastname", "Olio");
//		BookingRequest reqPayload = new BookingRequest();
//			
//		reqPayload.setFirstname("Aglio");
//		reqPayload.setLastname("Advait");
//		reqPayload.setDepositpaid(true);
//		reqPayload.setTotalprice(209);
//		
//		BookingDates bookingDates = new BookingDates();
//		bookingDates.setCheckin(LocalDate.parse("2023-12-01"));
//		bookingDates.setCheckout(LocalDate.parse("2023-12-02"));
//		
//		
//		reqPayload.setBookingdates(bookingDates);
//		reqPayload.setAdditionalneeds("chocolate cake");
//		
			String token_id = (String) context.getAttribute("token_id");
			
			ObjectMapper objMapper = new ObjectMapper();
			objMapper.registerModule(new JavaTimeModule());
			objMapper.registerModule(new Jdk8Module());
			objMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
				String jsonreqPayload = objMapper.writeValueAsString(patchPayload);
				System.out.println("req Payload:::" +jsonreqPayload);
		
		
		
		Response response = RestBooker_endpoints.patch_booking(patchPayload, 11, token_id);
		response.then().log().all();
		response.then().assertThat().statusCode(200);
		
		Assert.assertEquals(response.jsonPath().getInt("totalprice"), 209);
		
		
	}
	
	//private boolean isDeletePerformed = false;
	
	@Test(priority = 4,dependsOnMethods =  "test_basicAuth")
	public void test_delete_booking(ITestContext context) {
		System.out.println("Delete test-----");
		Response getResp = RestBooker_endpoints.get_booking("11");
		getResp.then().log().all();
		int statusCode = getResp.statusCode();
		if(statusCode ==201) {
		
		//if(!isDeletePerformed) {
		String token_id = (String) context.getAttribute("token_id");
		System.out.println("before delete====");
		Response response = RestBooker_endpoints.delete_booking(11,token_id);
		
			response.then().log().all();
			Assert.assertEquals(response.getStatusCode(), 201);
			System.out.println("After delete====");
			
			Response afterResp = RestBooker_endpoints.get_booking("11");
			afterResp.then().log().all();
	}
	else {
		System.out.println("Delete already performed");
	}
		

}
	
	@Test(priority=5)
	public void test_create_url(ITestContext context) throws JsonProcessingException {
		System.out.println("create with url---");
		String reqBody = "firstname=Jim" +
                "&lastname=Brown" +
                "&totalprice=111" +
                "&depositpaid=true" +
                "&bookingdates%5Bcheckin%5D=2018-01-01" +
                "&bookingdates%5Bcheckout%5D=2018-01-02"+
                "&additionalneeds=ok bye";
		
		ObjectMapper objMapper = new ObjectMapper();
			String jsonreq = objMapper.writeValueAsString(reqBody);
			System.out.println("req payload --->"+jsonreq);
		
		Response resp = RestBooker_endpoints.create_booking_url(reqBody);
		resp.then().log().all();
		
		System.out.println("Headers::"+resp.getHeaders());
		System.out.println("Body::"+resp.getBody().asPrettyString());
		
		
	}
	}
