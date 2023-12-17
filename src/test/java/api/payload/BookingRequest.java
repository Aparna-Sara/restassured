package api.payload;
import java.util.Optional;

public class BookingRequest {
	private Optional<String> firstname;
	private Optional<String> lastname;
	private Optional<Integer> totalprice;
	private Optional<Boolean> depositpaid;
	private Optional<BookingDates> bookingdates;
	private Optional<String> additionalneeds;
	
	public BookingRequest() {
		
		firstname = Optional.empty();
		lastname = Optional.empty();
		totalprice = Optional.empty();
		depositpaid = Optional.empty();
		bookingdates = Optional.empty();
	    additionalneeds = Optional.empty();
	}

	public Optional<String> getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = Optional.ofNullable(firstname);
	}

	public Optional<String> getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = Optional.ofNullable(lastname);
	}

	public Optional<Integer> getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(int totalprice) {
		this.totalprice = Optional.ofNullable(totalprice);
	}

	public Optional<Boolean> getDepositpaid() {
		return depositpaid;
	}

	public void setDepositpaid(boolean depositpaid) {
		this.depositpaid = Optional.ofNullable(depositpaid);
	}

	public Optional<BookingDates> getBookingdates() {
		return bookingdates;
	}

	public void setBookingdates(BookingDates bookingdates) {
		this.bookingdates = Optional.ofNullable(bookingdates);
	}

	public Optional<String> getAdditionalneeds() {
		return additionalneeds;
	}

	public void setAdditionalneeds(String additionalneeds) {
		this.additionalneeds = Optional.ofNullable(additionalneeds);
	}

	

	
	

}
