package com.rmn.gdxtend.expect;

import org.junit.Rule;

/**
 * A {@link Rule} to allow convenient assertion of expected results held in
 * files
 */
public class Expect extends AbstractExpect<Expect> {

	/**
	 * Constructs a new {@link Expect} {@link Rule}. Results are stored in files
	 * with ".expect" suffix.
	 */
	public Expect() {
		super( ".expect" );
	}
}
