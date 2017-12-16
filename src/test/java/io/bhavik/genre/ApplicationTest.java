package io.bhavik.genre;

import static org.junit.Assert.assertEquals;

import java.util.TimeZone;

import org.junit.Test;

public class ApplicationTest {

	@Test
	public void testTimeZoneIsUtc() {
		String timezone = "Pacific/Auckland";
		TimeZone.setDefault(TimeZone.getTimeZone(timezone));
		assertEquals(timezone, TimeZone.getDefault().getID());

		Application app = new Application();
		app.setTimezone();
		assertEquals("UTC", TimeZone.getDefault().getID());
	}
}
