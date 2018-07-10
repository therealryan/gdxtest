package com.rmn.gdxtend.gl.facets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.rmn.gdxtend.gl.enums.ComparisonFunction;

/**
 * Tests for {@link Depth}
 */
public class DepthTest extends FacetTest {

	private Depth depth = new Depth();
	private Depth control = new Depth();

	/**
	 * Fields are set correctly
	 */
	@Test
	public void construction() {
		depth.enabled( true )
				.function( ComparisonFunction.ALWAYS )
				.mask( false )
				.near( 0.5f )
				.far( 0.25f );

		assertThat( depth.enabled ).isTrue();
		assertThat( depth.func ).isEqualTo( ComparisonFunction.ALWAYS );
		assertThat( depth.mask ).isFalse();
		assertThat( depth.near ).isEqualTo( 0.5f );
		assertThat( depth.far ).isEqualTo( 0.25f );
	}

	/**
	 * Comparison of identical states
	 */
	@Test
	public void same() {
		assertThat( depth ).isEqualTo( control );
	}

	/**
	 * Fields are copied correctly
	 */
	@Test
	public void copy() {
		depth.enabled( true )
				.function( ComparisonFunction.ALWAYS )
				.mask( false )
				.near( 0.5f )
				.far( 0.25f );

		Depth copy = new Depth();

		assertThat( copy ).isNotEqualTo( depth );

		copy.from( depth );

		assertThat( copy ).isEqualTo( depth );
	}

	/**
	 * Enabled state sorts to after disabled
	 */
	@Test
	public void enabledComparison() {
		comparisonOrder( control, depth.enabled( true ) );
	}

	/**
	 * Altered function sorts to after default
	 */
	@Test
	public void functionComparison() {
		comparisonOrder( control, depth.function( ComparisonFunction.ALWAYS ) );
	}

	/**
	 * Altered mask sorts to after default
	 */
	@Test
	public void maskComparison() {
		comparisonOrder( control, depth.mask( false ) );
	}

	/**
	 * Altered near value sorts to after default
	 */
	@Test
	public void nearComparison() {
		comparisonOrder( control, depth.near( 0.5f ) );
	}

	/**
	 * Altered far value sorts to after default
	 */
	@Test
	public void farComparison() {
		comparisonOrder( control, depth.far( 1.5f ) );
	}

	/**
	 * Transition between identical states results in no GL calls
	 */
	@Test
	public void noopTransition() {
		depth.transition( control );

		verify( Gdx.gl, never() ).glEnable( anyInt() );
		verify( Gdx.gl, never() ).glDisable( anyInt() );
		verify( Gdx.gl, never() ).glDepthFunc( anyInt() );
		verify( Gdx.gl, never() ).glDepthMask( anyBoolean() );
		verify( Gdx.gl, never() ).glDepthRangef( anyFloat(), anyFloat() );
	}

	/**
	 * Parameters are set correctly on transition
	 */
	@Test
	public void transition() {
		depth.enabled( true )
				.function( ComparisonFunction.ALWAYS )
				.mask( false )
				.near( 0.5f )
				.far( 0.25f );

		depth.transition( control );

		verify( Gdx.gl ).glEnable( GL20.GL_DEPTH_TEST );
		verify( Gdx.gl ).glDepthFunc( GL20.GL_ALWAYS );
		verify( Gdx.gl ).glDepthMask( false );
		verify( Gdx.gl ).glDepthRangef( 0.5f, 0.25f );

		control.transition( depth );

		verify( Gdx.gl ).glDisable( GL20.GL_DEPTH_TEST );
	}
}
