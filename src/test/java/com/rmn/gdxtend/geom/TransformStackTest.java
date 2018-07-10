package com.rmn.gdxtend.geom;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.rmn.gdxtend.GdxTestRunner;

/**
 * Tests for {@link TransformStack}
 */
@RunWith( GdxTestRunner.class )
public class TransformStackTest {

	private static SoftAssertions softly = new SoftAssertions();

	/**
	 * The stack has limited depth
	 */
	@Test( expected = ArrayIndexOutOfBoundsException.class )
	public void overPush() {
		TransformStack ts = new TransformStack();

		for( int i = 0; i < 10; i++ ) {
			ts.push();
		}
	}

	/**
	 * Don't pop what you haven't pushed
	 */
	@Test( expected = ArrayIndexOutOfBoundsException.class )
	public void overPop() {
		TransformStack ts = new TransformStack();
		ts.pop();
		ts.transform( 0, 0, 0 );
	}

	/**
	 * Shows that we avoid doing that transform if we know there's no point
	 */
	@Test
	public void identity() {
		Vector3 v = mock( Vector3.class );

		TransformStack ts = new TransformStack();

		ts.transform( v );

		verify( v, never() ).mul( isA( Matrix4.class ) );

		ts.translate( 0, 0, 0 ).transform( v );

		verify( v ).mul( isA( Matrix4.class ) );
	}

	/**
	 * Stacked translations
	 */
	@Test
	public void translate() {
		TransformStack ts = new TransformStack();
		check( ts.transform( new Vector3() ), 0, 0, 0 );

		ts.translate( 1, 2, 3 );
		check( ts.transform( 0, 0, 0 ), 1, 2, 3 );

		ts.push().translate( 10, 20, 30 );
		check( ts.transform( 0, 0, 0 ), 11, 22, 33 );

		ts.pop();
		check( ts.transform( 0, 0, 0 ), 1, 2, 3 );
	}

	/**
	 * Stacked scales
	 */
	@Test
	public void scale() {
		TransformStack ts = new TransformStack();

		ts.scale( 2, 3, 4 );

		check( ts.transform( 0, 0, 0 ), 0, 0, 0 );
		check( ts.transform( 1, 1, 1 ), 2, 3, 4 );
		check( ts.transform( 2, 2, 2 ), 4, 6, 8 );

		ts.push();

		ts.scale( 10, 20, 30 );

		check( ts.transform( 0, 0, 0 ), 0, 0, 0 );
		check( ts.transform( 1, 1, 1 ), 20, 60, 120 );
		check( ts.transform( 2, 2, 2 ), 40, 120, 240 );

		ts.pop();

		check( ts.transform( 0, 0, 0 ), 0, 0, 0 );
		check( ts.transform( 1, 1, 1 ), 2, 3, 4 );
		check( ts.transform( 2, 2, 2 ), 4, 6, 8 );
	}

	/**
	 * Transform composition - operations are applied in last-to-first order
	 */
	@Test
	public void compose() {
		{
			TransformStack ts = new TransformStack()
					.scale( 5, 5, 5 )
					.translate( -1, -1, -1 );

			check( ts.transform( 0, 0, 0 ), -5, -5, -5 );
			check( ts.transform( 2, 2, 2 ), 5, 5, 5 );
		}
		{
			TransformStack ts = new TransformStack()
					.translate( -1, -1, -1 )
					.scale( 5, 5, 5 );

			check( ts.transform( 0, 0, 0 ), -1, -1, -1 );
			check( ts.transform( 2, 2, 2 ), 9, 9, 9 );
		}
	}

	private static void check( Vector3 v, float x, float y, float z ) {
		softly.assertThat( v.x ).isEqualTo( x );
		softly.assertThat( v.y ).isEqualTo( y );
		softly.assertThat( v.z ).isEqualTo( z );
		softly.assertAll();
	}
}
