package com.rmn.gdxtend.gl.facets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import com.badlogic.gdx.Gdx;

/**
 * Test for {@link Clear}
 */
public class ClearTest extends FacetTest {
	private final Clear clear = new Clear();
	private final Clear control = new Clear();

	/**
	 * Fields are set properly
	 */
	@Test
	public void construction() {
		clear.r( 0.5f )
				.g( 0.25f )
				.b( 0.125f )
				.a( 0.0625f )
				.depth( 2 )
				.stencil( 3 );

		assertThat( clear.color.r ).isEqualTo( 0.5f );
		assertThat( clear.color.g ).isEqualTo( 0.25f );
		assertThat( clear.color.b ).isEqualTo( 0.125f );
		assertThat( clear.color.a ).isEqualTo( 0.0625f );
		assertThat( clear.depth ).isEqualTo( 2 );
		assertThat( clear.stencil ).isEqualTo( 3 );
	}

	/**
	 * Comparison of identical states
	 */
	@Test
	public void same() {
		assertThat( clear ).isEqualTo( control );
	}

	/**
	 * Fields are copied correctly
	 */
	@Test
	public void copy() {
		clear.r( 0.5f )
				.g( 0.25f )
				.b( 0.125f )
				.a( 0.0625f )
				.depth( 2 )
				.stencil( 3 );
		Clear copy = new Clear();

		assertThat( copy ).isNotEqualTo( clear );

		copy.from( clear );

		assertThat( copy ).isEqualTo( clear );
	}

	/**
	 * Altered colour sorts to after default
	 */
	@Test
	public void colorComparison() {
		comparisonOrder( control, clear.r( 1 ) );
	}

	/**
	 * Altered depth sorts to after default
	 */
	@Test
	public void depthComparison() {
		comparisonOrder( control, clear.depth( 1 ) );
	}

	/**
	 * Altered stencil sorts to after default
	 */
	@Test
	public void stencilComparison() {
		comparisonOrder( control, clear.stencil( 1 ) );
	}

	/**
	 * Transition between identical states results in no GL calls
	 */
	@Test
	public void noopTransition() {
		clear.transition( control );

		verify( Gdx.gl, never() ).glClearColor(
				anyFloat(), anyFloat(), anyFloat(), anyFloat() );
		verify( Gdx.gl, never() ).glClearDepthf( anyFloat() );
		verify( Gdx.gl, never() ).glClearStencil( anyInt() );
	}

	/**
	 * Parameters are set correctly on transition
	 */
	@Test
	public void transition() {
		clear.r( 0.5f ).g( 0.25f ).b( 0.125f ).a( 0.0625f ).depth( 2 ).stencil( 3 );

		clear.transition( control );

		verify( Gdx.gl ).glClearColor( 0.5f, 0.25f, 0.125f, 0.0625f );
		verify( Gdx.gl ).glClearDepthf( 2 );
		verify( Gdx.gl ).glClearStencil( 3 );
	}
}
