package com.rmn.gdxtend.math;

import org.junit.Rule;
import org.junit.Test;

import com.rmn.gdxtend.expect.XYPlotExpect;
import com.rmn.gdxtend.expect.XYPlotExpect.CallBack;

/**
 * Exercises {@link FloatVariable}
 */
public class FloatVariableTest {

	/**
	 * Output value plot
	 */
	@Rule
	public XYPlotExpect plot = new XYPlotExpect().range( 0, 1 );

	/**
	 *
	 */
	@Test
	public void retarget() {

		final FloatVariable v = new FloatVariable().now( 0 ).set( 0, 1 ).over( 1 );

		plot.at( 0.5f, new CallBack() {

			@Override
			public void trigger( float f ) {
				v.now( f ).to( 0 );
			}
		} )
				.with( "linear", new Function() {

					@Override
					public float map( float f ) {
						return v.now( f ).get();
					}
				} );

		plot.check();
	}
}
