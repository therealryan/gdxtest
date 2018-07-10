package com.rmn.gdxtend.math;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Rule;
import org.junit.Test;

import com.rmn.gdxtend.expect.XYPlotExpect;

/**
 * Tests for {@link Range}
 */
public class RangeTest {

	/**
	 * Plots the results
	 */
	@Rule
	public XYPlotExpect plot = new XYPlotExpect();

	/**
	 * Range construction
	 */
	@Test
	public void construction() {
		Range r = new Range();

		assertThat( r.from ).isEqualTo( 0 );
		assertThat( r.to ).isEqualTo( Float.MIN_VALUE );

		r.from( 4 ).to( 7 );

		assertThat( r.from ).isEqualTo( 4 );
		assertThat( r.to ).isEqualTo( 7 );

		Range s = new Range().from( r );

		assertThat( s.from ).isEqualTo( 4 );
		assertThat( s.to ).isEqualTo( 7 );

		Range t = new Range().from( 2 ).lasting( 7 );

		assertThat( t.from ).isEqualTo( 2 );
		assertThat( t.to ).isEqualTo( 9 );
	}

	/**
	 * Linear interpolation
	 */
	@Test
	public void lerp() {
		Range r = new Range().from( 25 ).to( 75 );

		assertThat( r.lerp( 0 ) ).isEqualTo( 25 );
		assertThat( r.lerp( 1 ) ).isEqualTo( 75 );

		assertThat( r.lerp( 0.25f ) ).isEqualTo( 37.5f );
		assertThat( r.lerp( 0.5f ) ).isEqualTo( 50 );
		assertThat( r.lerp( 0.75f ) ).isEqualTo( 62.5f );

		assertThat( r.lerp( -1 ) ).isEqualTo( -25 );
		assertThat( r.lerp( 2 ) ).isEqualTo( 125 );
	}

	/**
	 * Inverse interpolation
	 */
	@Test
	public void unlerp() {
		Range r = new Range().from( 25 ).to( 75 );

		assertThat( r.unlerp( 25 ) ).isEqualTo( 0 );
		assertThat( r.unlerp( 75 ) ).isEqualTo( 1 );

		assertThat( r.unlerp( 37.5f ) ).isEqualTo( 0.25f );
		assertThat( r.unlerp( 50 ) ).isEqualTo( 0.5f );
		assertThat( r.unlerp( 62.5f ) ).isEqualTo( 0.75f );

		assertThat( r.unlerp( -25 ) ).isEqualTo( -1 );
		assertThat( r.unlerp( 125 ) ).isEqualTo( 2 );
	}

	/**
	 * Contains checks
	 */
	@Test
	public void contains() {
		Range r = new Range().from( 25 ).to( 75 );

		assertThat( r.contains( 24.9999f ) ).isFalse();
		assertThat( r.contains( 25 ) ).isTrue();
		assertThat( r.contains( 50 ) ).isTrue();
		assertThat( r.contains( 75 ) ).isTrue();
		assertThat( r.contains( 75.0001f ) ).isFalse();
	}

	/**
	 * Before checks
	 */
	@Test
	public void before() {
		Range r = new Range().from( 25 ).to( 75 );
		assertThat( r.before( 24 ) ).isTrue();
		assertThat( r.before( 25 ) ).isFalse();

		Range s = new Range().from( 75 ).to( 25 );
		assertThat( s.before( 76 ) ).isTrue();
		assertThat( s.before( 75 ) ).isFalse();
	}

	/**
	 * After checks
	 */
	@Test
	public void after() {
		Range r = new Range().from( 25 ).to( 75 );
		assertThat( r.after( 76 ) ).isTrue();
		assertThat( r.after( 75 ) ).isFalse();

		Range s = new Range().from( 75 ).to( 25 );
		assertThat( s.after( 24 ) ).isTrue();
		assertThat( s.after( 25 ) ).isFalse();
	}

	/**
	 * Range clamping
	 */
	@Test
	public void clamp() {
		Range r = new Range().from( 25 ).to( 75 );

		assertThat( r.clamp( 24 ) ).isEqualTo( 25 );
		assertThat( r.clamp( 25 ) ).isEqualTo( 25 );
		assertThat( r.clamp( 50 ) ).isEqualTo( 50 );
		assertThat( r.clamp( 75 ) ).isEqualTo( 75 );
		assertThat( r.clamp( 76 ) ).isEqualTo( 75 );
	}

	/**
	 * Normalisation
	 */
	@Test
	public void normalise() {
		final Range r = new Range().from( 0 ).to( 1 );

		assertThat( r.normalise( 0.125f ) ).isEqualTo( 0.125f );
		assertThat( r.normalise( 0.5f ) ).isEqualTo( 0.5f );
		assertThat( r.normalise( 0.875f ) ).isEqualTo( 0.875f );
		assertThat( r.normalise( 1.5f ) ).isEqualTo( 0.5f );
		assertThat( r.normalise( 100.875f ) ).isEqualTo( 0.875f );
		assertThat( r.normalise( -0.5f ) ).isEqualTo( 0.5f );
		assertThat( r.normalise( -0.125f ) ).isEqualTo( 0.875f );
		assertThat( r.normalise( -0.875f ) ).isEqualTo( 0.125f );

		r.from( 1 ).to( 2.0f );
		assertThat( r.normalise( 2.5f ) ).isEqualTo( 1.5f );
		assertThat( r.normalise( -0.5f ) ).isEqualTo( 1.5f );

		r.from( 25 ).to( 75 );
		assertThat( r.normalise( 24 ) ).isEqualTo( 74 );
		assertThat( r.normalise( 50 ) ).isEqualTo( 50 );
		assertThat( r.normalise( 76 ) ).isEqualTo( 26 );

		plot.range( 0, 100 ).with( "25-75", new Function() {

			@Override
			public float map( float f ) {
				return r.normalise( f );
			}
		} ).check();
	}

	/**
	 * Ranges do not have to be increasing
	 */
	@Test
	public void negative() {
		Range r = new Range().from( 75 ).to( 25 );

		assertThat( r.lerp( 0 ) ).isEqualTo( 75 );
		assertThat( r.lerp( 0.5f ) ).isEqualTo( 50 );
		assertThat( r.lerp( 1 ) ).isEqualTo( 25 );

		assertThat( r.unlerp( 25 ) ).isEqualTo( 1 );
		assertThat( r.unlerp( 50 ) ).isEqualTo( 0.5f );
		assertThat( r.unlerp( 75 ) + 0 ).isEqualTo( 0 ); // add zero to avoid -0.0f

		assertThat( r.contains( 24.9999f ) ).isFalse();
		assertThat( r.contains( 25 ) ).isTrue();
		assertThat( r.contains( 50 ) ).isTrue();
		assertThat( r.contains( 75 ) ).isTrue();
		assertThat( r.contains( 75.0001f ) ).isFalse();

		assertThat( r.clamp( 24 ) ).isEqualTo( 25 );
		assertThat( r.clamp( 25 ) ).isEqualTo( 25 );
		assertThat( r.clamp( 50 ) ).isEqualTo( 50 );
		assertThat( r.clamp( 75 ) ).isEqualTo( 75 );
		assertThat( r.clamp( 76 ) ).isEqualTo( 75 );

		assertThat( r.normalise( 24 ) ).isEqualTo( 74 );
		assertThat( r.normalise( 50 ) ).isEqualTo( 50 );
		assertThat( r.normalise( 76 ) ).isEqualTo( 26 );
	}
}
