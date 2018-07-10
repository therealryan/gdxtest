package com.rmn.gdxtend.geom;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.data.Offset;
import org.junit.Test;

/**
 * Tests for {@link Position}
 */
public class PositionTest {

	private static final Offset<Float> tolerance =
			Offset.offset( new Float( 0.0001f ) );

	/**
	 * Tests position addition
	 */
	@Test
	public void add() {
		Position a = new Position();
		Position b = new Position();
		Position c = new Position();

		assertSame( a.add( b ), c );

		a.position.set( 1, 2, 3 );
		b.position.set( 10, 20, 30 );
		c.position.set( 11, 22, 33 );

		a.rotation.setEulerAngles( 0, 0, 45 );
		b.rotation.setEulerAngles( 0, 0, 90 );
		c.rotation.setEulerAngles( 0, 0, 135 );

		a.scale = 2;
		b.scale = 3;
		c.scale = 6;

		assertSame( a.add( b ), c );
	}

	private static void assertSame( Position a, Position b ) {
		assertThat( a.position.x ).isEqualTo( b.position.x, tolerance );
		assertThat( a.position.y ).isEqualTo( b.position.y, tolerance );
		assertThat( a.position.z ).isEqualTo( b.position.z, tolerance );
		assertThat( a.rotation.getYaw() )
				.isEqualTo( b.rotation.getYaw(), tolerance );
		assertThat( a.rotation.getPitch() )
				.isEqualTo( b.rotation.getPitch(), tolerance );
		assertThat( a.rotation.getRoll() )
				.isEqualTo( b.rotation.getRoll(), tolerance );
		assertThat( a.scale ).isEqualTo( b.scale, tolerance );
	}
}
