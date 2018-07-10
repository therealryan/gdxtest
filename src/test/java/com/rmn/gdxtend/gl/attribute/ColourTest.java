package com.rmn.gdxtend.gl.attribute;

import static com.badlogic.gdx.graphics.VertexAttribute.ColorPacked;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.NumberUtils;
import com.rmn.gdxtend.geom.Shape;

/**
 * Because the conversion from {@link Color} to packed float was not obvious to
 * me
 */
public class ColourTest {

	private Shape s = new Shape( 1, 0, ColorPacked() );

	/**
	 * Demonstrates the effect of setting a vertex colour from a {@link Color}
	 * object
	 */
	@Test
	public void fromColour() {
		s.col.set( new Color( 0, 0, 0, 0 ) );
		check( 0b0000_0000_0000_0000_0000_0000_0000_0000 );
		s.col.set( Color.BLACK );
		check( 0b1111_1110_0000_0000_0000_0000_0000_0000 );
		s.col.set( Color.RED );
		check( 0b1111_1110_0000_0000_0000_0000_1111_1111 );
		s.col.set( Color.GREEN );
		check( 0b1111_1110_0000_0000_1111_1111_0000_0000 );
		s.col.set( Color.BLUE );
		check( 0b1111_1110_1111_1111_0000_0000_0000_0000 );
		s.col.set( Color.WHITE );
		check( 0b1111_1110_1111_1111_1111_1111_1111_1111 );
	}

	/**
	 * Demonstrates the effect of setting a vertex colour via components
	 */
	@Test
	public void fromComponents() {
		s.col.rgba( 0, 0, 0, 0 );
		check( 0b0000_0000_0000_0000_0000_0000_0000_0000 );
		s.col.rgba( 0, 0, 0, 1 );
		check( 0b1111_1110_0000_0000_0000_0000_0000_0000 );
		s.col.rgba( 0.5f, 0, 0, 1 );
		check( 0b1111_1110_0000_0000_0000_0000_0111_1111 );
		s.col.rgba( 1, 0, 0, 1 );
		check( 0b1111_1110_0000_0000_0000_0000_1111_1111 );
		s.col.rgba( 0, 1, 0, 1 );
		check( 0b1111_1110_0000_0000_1111_1111_0000_0000 );
		s.col.rgba( 0, 0, 1, 1 );
		check( 0b1111_1110_1111_1111_0000_0000_0000_0000 );
		s.col.rgba( 1, 1, 1, 1 );
		check( 0b1111_1110_1111_1111_1111_1111_1111_1111 );
	}

	/**
	 * Sets individual components
	 */
	@Test
	public void individual() {
		s.col.rgba( 0, 0, 0, 0 );
		check( 0b0000_0000_0000_0000_0000_0000_0000_0000 );

		s.col.r( 1 );
		check( 0b0000_0000_0000_0000_0000_0000_1111_1111 );

		s.col.rgba( 0, 0, 0, 0 );
		s.col.g( 1 );
		check( 0b0000_0000_0000_0000_1111_1111_0000_0000 );

		s.col.rgba( 0, 0, 0, 0 );
		s.col.b( 1 );
		check( 0b0000_0000_1111_1111_0000_0000_0000_0000 );

		s.col.rgba( 0, 0, 0, 0 );
		s.col.a( 1 );
		check( 0b1111_1110_0000_0000_0000_0000_0000_0000 );

		s.col.rgba( 0, 0, 0, 0 );
		s.col.r( 0.00625f );
		check( 0b0000_0000_0000_0000_0000_0000_0000_0001 );
		s.col.g( 0.00625f );
		check( 0b0000_0000_0000_0000_0000_0001_0000_0001 );
		s.col.b( 0.00625f );
		check( 0b0000_0000_0000_0001_0000_0001_0000_0001 );
		s.col.a( 0.00625f );
		check( 0b0000_0001_0000_0001_0000_0001_0000_0001 );
	}

	private void check( int bits ) {
		float actual = s.vertexData[ 0 ];
		float expected = NumberUtils.intToFloatColor( bits );

		int ia = Float.floatToIntBits( actual );
		assertThat( actual )
				.as( "Want 0x" + Integer.toHexString( bits )
						+ "\nHave 0x" + Integer.toHexString( ia ) )
				.isEqualTo( expected );
	}

	/**
	 * Tests applying comonent sets over all vertices
	 */
	@Test
	public void all() {
		Shape t = new Shape( 3, 0, ColorPacked() )
				.col.rgba( 1, 0, 0, 0 ).next()
				.col.rgba( 0, 1, 0, 0 ).next()
				.col.rgba( 0, 0, 1, 0 ).next();

		assertThat( t.vertexData ).isEqualTo( new float[] {
				Float.intBitsToFloat( 0b0000_0000_0000_0000_0000_0000_1111_1111 ),
				Float.intBitsToFloat( 0b0000_0000_0000_0000_1111_1111_0000_0000 ),
				Float.intBitsToFloat( 0b0000_0000_1111_1111_0000_0000_0000_0000 ),
		} );

		t.col.all().a( 1 );

		assertThat( t.vertexData ).isEqualTo( new float[] {
				Float.intBitsToFloat( 0b1111_1110_0000_0000_0000_0000_1111_1111 ),
				Float.intBitsToFloat( 0b1111_1110_0000_0000_1111_1111_0000_0000 ),
				Float.intBitsToFloat( 0b1111_1110_1111_1111_0000_0000_0000_0000 ),
		} );
	}

	/**
	 * Conversion between {@link Color} and packed float values
	 */
	@Test
	public void get() {
		checkGet( Color.WHITE );
		checkGet( Color.BLACK );
		checkGet( Color.RED );
		checkGet( Color.GREEN );
		checkGet( Color.BLUE );
	}

	private static void checkGet( Color in ) {
		Color out = Colour.fromPacked( new Color(), in.toFloatBits() );
		assertThat( out.toString() ).isEqualTo(
				// we lose the last bit of the alpha in the conversion, hence the regex
				// to convert a trailing f to a trailing e
				in.toString().replaceAll( "f$", "e" ) );
	}
}
