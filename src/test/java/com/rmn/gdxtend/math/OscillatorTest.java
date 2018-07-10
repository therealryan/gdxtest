package com.rmn.gdxtend.math;

import org.junit.Rule;
import org.junit.Test;

import com.badlogic.gdx.math.Interpolation;
import com.rmn.gdxtend.expect.XYPlotExpect;

/**
 * Tests for {@link Oscillator}
 */
public class OscillatorTest {

	/**
	 * Plots
	 */
	@Rule
	public XYPlotExpect plot = new XYPlotExpect();

	/**
	 * Demonstrates frequency variation
	 */
	@Test
	public void frequency() {
		plot.with( "1", new Oscillator().frequency( 1 ) );
		plot.with( "2", new Oscillator().frequency( 2 ) );
		plot.with( "3", new Oscillator().frequency( 3 ) );
		plot.with( "4", new Oscillator().frequency( 4 ) );

		plot.check();
	}

	/**
	 * Demonstrates amplitude variation
	 */
	@Test
	public void amplitude() {
		plot.with( "1", new Oscillator().amplitude( 1 ) );
		plot.with( "2", new Oscillator().amplitude( 2 ) );
		plot.with( "3", new Oscillator().amplitude( 3 ) );
		plot.with( "4", new Oscillator().amplitude( 4 ) );

		plot.check();
	}

	/**
	 * Demonstrates bia variation
	 */
	@Test
	public void bias() {
		plot.with( "1", new Oscillator().bias( 1 ) );
		plot.with( "2", new Oscillator().bias( 2 ) );
		plot.with( "3", new Oscillator().bias( 3 ) );
		plot.with( "4", new Oscillator().bias( 4 ) );

		plot.check();
	}

	/**
	 * Demonstrates offset variation
	 */
	@Test
	public void offset() {
		plot.with( "1/2", new Oscillator().offset( .5f ) );
		plot.with( "1/4", new Oscillator().offset( 0.25f ) );
		plot.with( "1/8", new Oscillator().offset( 0.125f ) );

		plot.check();
	}

	/**
	 * Demonstrates varying level duartions
	 */
	@Test
	public void times() {
		plot.with( "1-2-3-4", new Oscillator().rise( 0.1f ).high( 0.2f )
				.fall( 0.3f ).low( 0.4f ).frequency( 1 ) );

		plot.check();
	}

	/**
	 * Demonstrates interpolators
	 */
	@Test
	public void interpolator() {
		plot.with( "linear", new Oscillator() );
		plot.with( "sine", new Oscillator().with( Interpolation.sine ) );
		plot.with( "bounce", new Oscillator().with( Interpolation.bounce ) );
		plot.with( "swing", new Oscillator().with( Interpolation.swing ) );

		plot.check();
	}
}
