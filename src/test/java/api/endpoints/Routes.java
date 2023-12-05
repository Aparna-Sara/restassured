package api.endpoints;
/*create paths or routes for all operations
 * C - create user
 * R
 * U
 * D
 * */

public class Routes {
	
	public static String BASE_URL ="https://jsonplaceholder.typicode.com";
	
	public static String GET_URL = "https://jsonplaceholder.typicode.com/posts";
	public static String GET_URL_param = "https://jsonplaceholder.typicode.com/posts/1";
	public static String POST_URL = "https://jsonplaceholder.typicode.com/posts";
	public static String PUT_URL = "https://jsonplaceholder.typicode.com/posts/1";
	public static String DELETE_URL = "https://jsonplaceholder.typicode.com/posts/1";

	
	
	//REST BOOKER APP
	
	public static String BASE_URL_BOOKER = "https://restful-booker.herokuapp.com";
	public static String AUTH_URL = BASE_URL_BOOKER + "/auth";
	public static String GET_ALL_BOOKINGIDS = BASE_URL_BOOKER + "/booking";
	public static String GET_BOOKING = BASE_URL_BOOKER + "/booking/{id}";
	public static String CREATE_BOOKING = BASE_URL_BOOKER +"/booking";
	public static String UPDATE_BOOKING = BASE_URL_BOOKER + "/booking/{id}";
	
	
}
