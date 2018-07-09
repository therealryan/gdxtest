package com.rmn.gdxtend.expect;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.regex.Pattern;

import org.junit.Test;

/**
 * Exercises the {@link Mask} class
 */
public class MaskTest {

	/**
	 * Simple replacement case
	 */
	@Test
	public void replacement() {
		Mask m = new Mask()
				.mask( "foo" ).with( "bar" );
		assertThat( m.apply( "baz foo bar" ) )
				.isEqualTo( "baz bar bar" );
	}

	/**
	 * Regex match flags can be supplied
	 */
	@Test
	public void flag() {
		Mask m = new Mask().mask( "f.oo" ).with( "bar" );

		assertThat( m.apply( "f\noo fpoo" ) ).isEqualTo( "f\noo bar" );

		m = new Mask().mask( "f.oo" ).match( Pattern.DOTALL ).with( "bar" );

		assertThat( m.apply( "f\noo fpoo" ) ).isEqualTo( "bar bar" );
	}

	/**
	 * Captured groups are masked
	 */
	@Test
	public void capture() {
		Mask m = new Mask()
				.maskCaptures( "<tag>(.*?)</tag>" )
				.with( "foo" );

		String masked = m.apply(
				"123 <tag>123</tag> 123 <tag>456</tag> 456" );
		assertThat( masked ).isEqualTo(
				"foo_1 <tag>foo_1</tag> foo_1 <tag>foo_2</tag> foo_2" );

		String doubleMasked = m.apply( masked );
		assertThat( doubleMasked )
				.as( "idempotence" )
				.isEqualTo( masked );

		assertThat( m.apply( "123 456" ) )
				.as( "adaptive" )
				.isEqualTo( "foo_1 foo_2" );
	}

	/**
	 * Multiple groups in the same regex can be masked
	 */
	@Test
	public void multicapture() {
		Mask m = new Mask()
				.maskCaptures( "<tag attr='(.*?)'>(.*?)</tag>" )
				.with( "bar", "foo" );

		String masked = m.apply( "123 <tag attr='456'>123</tag> 456" );
		assertThat( masked )
				.isEqualTo( "foo_1 <tag attr='bar_1'>foo_1</tag> bar_1" );
	}
}
