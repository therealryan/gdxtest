package com.rmn.gdxtend.gdx;

import org.junit.Rule;
import org.junit.Test;

import com.badlogic.gdx.math.Interpolation;
import com.rmn.gdxtend.expect.XYPlotExpect;
import com.rmn.gdxtend.math.Function;

/**
 * Doesn't really test anything, just generates graphs of the various
 * interpolators
 */
public class InterpolationTest {

	/**
	 * Produces the plot results
	 */
	@Rule
	public XYPlotExpect plot = new XYPlotExpect();

	/**
	 * These didn't fit in any other plot
	 */
	@Test
	public void misc() {
		graph( "linear", Interpolation.linear );
		graph( "fade", Interpolation.fade );
		plot.check();
	}

	/**
	 * Bounce functions
	 */
	@Test
	public void bounce() {
		graph( Interpolation.bounce,
				Interpolation.bounceIn,
				Interpolation.bounceOut );
	}

	/**
	 * Circle functions
	 */
	@Test
	public void circle() {
		graph( "circle", Interpolation.circle );
		graph( "circleIn", Interpolation.circleIn );
		graph( "circleOut", Interpolation.circleOut );
		plot.check();
	}

	/**
	 * Elastic functions
	 */
	@Test
	public void elastic() {
		graph( Interpolation.elastic,
				Interpolation.elasticIn,
				Interpolation.elasticOut );
	}

	/**
	 * Exponential functions
	 */
	@Test
	public void exponent() {
		graph( "exp10", Interpolation.exp10 );
		graph( "exp10In", Interpolation.exp10In );
		graph( "exp10Out", Interpolation.exp10Out );
		graph( "exp5", Interpolation.exp5 );
		graph( "exp5In", Interpolation.exp5In );
		graph( "exp5Out", Interpolation.exp5Out );
		plot.check();
	}

	/**
	 * Power functions
	 */
	@Test
	public void power() {
		graph( "pow2", Interpolation.pow2 );
		graph( "pow3", Interpolation.pow3 );
		graph( "pow4", Interpolation.pow4 );
		graph( "pow5", Interpolation.pow5 );
		plot.check();
	}

	/**
	 * sine functions
	 */
	@Test
	public void sine() {
		graph( Interpolation.sine,
				Interpolation.sineIn,
				Interpolation.sineOut );
	}

	/**
	 * swing functions
	 */
	@Test
	public void swing() {
		graph( Interpolation.swing,
				Interpolation.swingIn,
				Interpolation.swingOut );
	}

	private void graph( String name, final Interpolation i ) {
		plot.with( name, new Function() {

			@Override
			public float map( float f ) {
				return i.apply( f );
			}
		} );
	}

	private void graph( Interpolation... interpolators ) {

		for( final Interpolation i : interpolators ) {
			graph( i.getClass().getSimpleName(), i );
		}

		plot.check();
	}
}
