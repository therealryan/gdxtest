package com.rmn.gdxtend.geom;

import static com.badlogic.gdx.graphics.VertexAttribute.ColorPacked;
import static com.badlogic.gdx.graphics.VertexAttribute.Position;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

/**
 * Tests for {@link Shape} manipulation
 */
public class ShapeTest {

	/**
	 * Joining two shapes together
	 */
	@Test
	public void meld() {
		Shape s = new Shape( 3, 1, Position() );
		Arrays.fill( s.vertexData, 1 );
		Arrays.fill( s.indices, (short) 2 );

		Shape t = new Shape( 3, 1, Position() );
		Arrays.fill( t.vertexData, 3 );
		Arrays.fill( t.indices, (short) 4 );

		assertThat( s.vertexData ).isEqualTo( new float[] {
				1, 1, 1, 1, 1, 1, 1, 1, 1,
		} );
		assertThat( s.indices ).isEqualTo( new short[] {
				2, 2, 2,
		} );
		assertThat( t.vertexData ).isEqualTo( new float[] {
				3, 3, 3, 3, 3, 3, 3, 3, 3,
		} );
		assertThat( t.indices ).isEqualTo( new short[] {
				4, 4, 4,
		} );

		Shape u = new Shape( s, t );
		assertThat( u.vertexData ).isEqualTo( new float[] {
				1, 1, 1, 1, 1, 1, 1, 1, 1,
				3, 3, 3, 3, 3, 3, 3, 3, 3,
		} );
		assertThat( u.indices ).isEqualTo( new short[] {
				2, 2, 2,
				7, 7, 7,
		} );
	}

	/**
	 * Vertex addressing
	 */
	@Test
	public void vertexIndex() {
		Shape s = new Shape( 3, 0, Position() );

		s.pos.xyz( 1, 2, 3 ).next();

		assertThat( s.vertexData ).isEqualTo( new float[] {
				1, 2, 3,
				0, 0, 0,
				0, 0, 0 } );

		s.index( 2 ).pos.x( 4 );

		assertThat( s.vertexData ).isEqualTo( new float[] {
				1, 2, 3,
				0, 0, 0,
				4, 0, 0 } );

		s.index( 0 ).pos.z( 5 );

		assertThat( s.vertexData ).isEqualTo( new float[] {
				1, 2, 5,
				0, 0, 0,
				4, 0, 0 } );
	}

	/**
	 * Position setters
	 */
	@Test
	public void position() {
		Shape s = new Shape( 5, 0, Position() )
				.pos.xyz( 1, 2, 3 ).next()
				.pos.x( 4 ).done().next()
				.pos.y( 5 ).done().next()
				.pos.z( 6 ).done().next()
				.pos.set( new Vector3( 7, 8, 9 ) );

		assertThat( s.vertexData ).isEqualTo( new float[] {
				1, 2, 3,
				4, 0, 0,
				0, 5, 0,
				0, 0, 6,
				7, 8, 9 } );
	}

	/**
	 * Setting all positions in one fell swoop
	 */
	@Test
	public void allPosition() {

		Shape s = new Shape( 3, 0, Position() );

		s.pos.all().xyz( 1, 2, 3 );
		assertThat( s.vertexData ).isEqualTo( new float[] {
				1, 2, 3,
				1, 2, 3,
				1, 2, 3, } );

		s.pos.xyz( 4, 5, 6 );
		assertThat( s.vertexData ).isEqualTo( new float[] {
				4, 5, 6,
				1, 2, 3,
				1, 2, 3, } );

		s.pos.all().x( 7 );
		assertThat( s.vertexData ).isEqualTo( new float[] {
				7, 5, 6,
				7, 2, 3,
				7, 2, 3, } );

		s.pos.all().y( 8 );
		assertThat( s.vertexData ).isEqualTo( new float[] {
				7, 8, 6,
				7, 8, 3,
				7, 8, 3, } );

		s.pos.all().z( 9 );
		assertThat( s.vertexData ).isEqualTo( new float[] {
				7, 8, 9,
				7, 8, 9,
				7, 8, 9, } );
	}

	/**
	 * Colour setters
	 */
	@Test
	public void colour() {
		Shape s =
				new Shape( 3, 0, ColorPacked() )
				.col.set( Color.RED ).next()
				.col.rgba( 0.5f, 0.25f, 0.125f, 0.0625f ).next()
				.col.r( 0.03125f )
						.g( 0.015625f )
						.b( 0.0078125f )
						.a( 0.5f ).done();

		Color c = new Color( 0.0f, 1.0f, 0.0f, 0.0f );
		assertThat( Integer.toHexString( c.toIntBits() ) ).isEqualTo( "ff00" );
		assertThat( c.toIntBits() ).isEqualTo(
				0b0000_0000_0000_0000_1111_1111_0000_0000 );

		assertThat( s.vertexData ).isEqualTo( new float[] {
				Float.intBitsToFloat( 0b1111_1110_0000_0000_0000_0000_1111_1111 ),
				Float.intBitsToFloat( 0b0000_1110_0001_1111_0011_1111_0111_1111 ),
				Float.intBitsToFloat( 0b0111_1110_0000_0001_0000_0011_0000_0111 ),
		} );
	}

	/**
	 * Setting all vertex colours in one fell swoop
	 */
	@Test
	public void allColour() {
		Shape s =
				new Shape( 3, 0, ColorPacked() )
				.col.all().set( Color.RED );

		assertThat( s.vertexData ).isEqualTo( new float[] {
				Color.RED.toFloatBits(),
				Color.RED.toFloatBits(),
				Color.RED.toFloatBits(),
		} );
	}

	/**
	 * Setting position and colour
	 */
	@Test
	public void colourAndPosition() {
		Shape s = new Shape( 3, 0, Position(), ColorPacked() )
				.pos.xyz( 1, 2, 3 )
				.col.set( Color.RED )
						.next()
				.pos.xyz( 4, 5, 6 )
				.col.rgba( 0.5f, 0.25f, 0.125f, 0.0625f )
						.next()
				.pos.xyz( 7, 8, 9 ).col
						.r( 0.03125f )
						.g( 0.015625f )
						.b( 0.0078125f )
						.a( 0.5f ).done();

		assertThat( s.vertexData ).isEqualTo( new float[] {
				1, 2, 3,
				Float.intBitsToFloat( 0b1111_1110_0000_0000_0000_0000_1111_1111 ),
				4, 5, 6,
				Float.intBitsToFloat( 0b0000_1110_0001_1111_0011_1111_0111_1111 ),
				7, 8, 9,
				Float.intBitsToFloat( 0b0111_1110_0000_0001_0000_0011_0000_0111 ),
		} );

		// check that global setters only affect their own components
		s.pos.all().x( 8 );

		assertThat( s.vertexData ).isEqualTo( new float[] {
				8, 2, 3,
				Float.intBitsToFloat( 0b1111_1110_0000_0000_0000_0000_1111_1111 ),
				8, 5, 6,
				Float.intBitsToFloat( 0b0000_1110_0001_1111_0011_1111_0111_1111 ),
				8, 8, 9,
				Float.intBitsToFloat( 0b0111_1110_0000_0001_0000_0011_0000_0111 ),
		} );
	}

	/**
	 * Position transform
	 */
	@Test
	public void transform() {
		Shape s = new Shape( 3, 0, Position() )
				.pos.xyz( 1, 2, 3 ).next()
				.pos.xyz( 4, 5, 6 ).next()
				.pos.xyz( 7, 8, 9 ).next();

		assertThat( s.vertexData ).isEqualTo( new float[] {
				1, 2, 3, 4, 5, 6, 7, 8, 9,
		} );

		s.index( 0 );

		s.pos.scale( 2, 2, 1 ).apply().next();
		s.pos.scale( 3, 1, 3 ).apply().next();
		s.pos.scale( 1, 4, 4 ).apply().next();

		assertThat( s.vertexData ).isEqualTo( new float[] {
				2, 4, 3, 12, 5, 18, 7, 32, 36,
		} );

		s.pos.all().scale( 2, 2, 2 ).apply();

		assertThat( s.vertexData ).isEqualTo( new float[] {
				4, 8, 6, 24, 10, 36, 14, 64, 72,
		} );
	}

	/**
	 * Attempting to use an attribute that the shape does not have
	 */
	@Test( expected = NullPointerException.class )
	public void missing() {
		Shape s = new Shape( 3, 1, Position() );

		// we don't have the colour attribute
		s.col.set( Color.RED );
	}
}
