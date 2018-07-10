package com.rmn.gdxtend.geom.imp;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;

/**
 * tests for {@link LinearPath}
 */
public class LinearPathTest {

	/**
	 * Simple clamped and unclamped path test
	 */
	@Test
	public void diagonal() {
		LinearPath clamped = new LinearPath()
				.by( Interpolation.linear );
		clamped.now( 0 ).over( 1 );
		clamped.start.set( 0, 0, 0 );
		clamped.end.set( 1, 1, 1 );

		LinearPath unclamped = new LinearPath()
				.from( clamped )
				.clamp( false );

		Vector3 p = new Vector3();
		float t = -1;
		while( t < 2 ) {
			unclamped.now( t ).get( p );
			assertThat( p.x ).isEqualTo( t );
			assertThat( p.y ).isEqualTo( t );
			assertThat( p.z ).isEqualTo( t );

			clamped.now( t ).get( p );
			assertThat( p.x ).isEqualTo( t < 0 ? 0 : t > 1 ? 1 : t );
			assertThat( p.y ).isEqualTo( t < 0 ? 0 : t > 1 ? 1 : t );
			assertThat( p.z ).isEqualTo( t < 0 ? 0 : t > 1 ? 1 : t );

			t += 0.125f;
		}
	}
}
