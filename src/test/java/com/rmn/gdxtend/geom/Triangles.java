package com.rmn.gdxtend.geom;

import static com.badlogic.gdx.graphics.VertexAttribute.ColorPacked;
import static com.badlogic.gdx.graphics.VertexAttribute.Position;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.badlogic.gdx.graphics.Color;

/**
 * Triangle construction tests
 */
public class Triangles {

	/**
	 * Base - just one triangle
	 */
	@Test
	public void one() {

		Color c = Color.BLUE;

		Shape t =
				new Shape( 1, Topology.triangles,
						Position(),
						ColorPacked() )
				.pos.xyz( 1, 2, 3 ).col.set( c ).next()
				.pos.xyz( 4, 5, 6 ).col.set( c ).next()
				.pos.xyz( 7, 8, 9 ).col.set( c ).next();

		assertThat( t.vertexData ).isEqualTo( new float[] {
				1, 2, 3, c.toFloatBits(),
				4, 5, 6, c.toFloatBits(),
				7, 8, 9, c.toFloatBits(),
		} );
	}

	/**
	 * Two triangles
	 */
	@Test
	public void two() {

		Color c = Color.BLUE;

		Shape t =
				new Shape( 2, Topology.triangles,
						Position(),
						ColorPacked() );
		for( int i = 0; i < 2; i++ ) {

			t.pos.xyz( 1, 2, 3 ).col.set( c ).next();
			t.pos.xyz( 4, 5, 6 ).col.set( c ).next();
			t.pos.xyz( 7, 8, 9 ).col.set( c ).next();
		}

		assertThat( t.vertexData ).isEqualTo( new float[] {
				1, 2, 3, c.toFloatBits(),
				4, 5, 6, c.toFloatBits(),
				7, 8, 9, c.toFloatBits(),
				1, 2, 3, c.toFloatBits(),
				4, 5, 6, c.toFloatBits(),
				7, 8, 9, c.toFloatBits(),
		} );
	}

	/**
	 * Melding two triangles into one shape
	 */
	public void join() {

		Color c = Color.BLUE;
		Shape one =
				new Shape( 1, Topology.triangles,
						Position(),
						ColorPacked() )
				.pos.xyz( 1, 2, 3 ).col.set( c ).next()
				.pos.xyz( 4, 5, 6 ).col.set( c ).next()
				.pos.xyz( 7, 8, 9 ).col.set( c ).next();
		Shape two =
				new Shape( 2, Topology.triangles,
						Position(),
						ColorPacked() )
				.pos.xyz( 1, 2, 3 ).col.set( c ).next()
				.pos.xyz( 4, 5, 6 ).col.set( c ).next()
				.pos.xyz( 7, 8, 9 ).col.set( c ).next()
				.pos.xyz( 1, 2, 3 ).col.set( c ).next()
				.pos.xyz( 4, 5, 6 ).col.set( c ).next()
				.pos.xyz( 7, 8, 9 ).col.set( c ).next();

		Shape joined = new Shape( one, one );

		assertThat( joined.vertexData ).isEqualTo( two.vertexData );
		assertThat( joined.indices ).isEqualTo( two.indices );
	}
}
