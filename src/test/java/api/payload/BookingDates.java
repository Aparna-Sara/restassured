package api.payload;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public class BookingDates {

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate checkin;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate checkout;
	
	public BookingDates() {
		
	}

	public LocalDate getCheckin() {
		return checkin;
	}

	public void setCheckin(LocalDate checkin) {
		this.checkin = checkin;
	}

	public LocalDate getCheckout() {
		return checkout;
	}

	public void setCheckout(LocalDate checkout) {
		this.checkout = checkout;
	}

	
	
}
