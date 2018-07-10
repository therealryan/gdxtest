package com.rmn.gdxtend.gl.facets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

/**
 * Tests for {@link PolygonOffset}
 */
public class PolygonOffsetTest extends FacetTest {

	PolygonOffset polyOffset = new PolygonOffset();
	PolygonOffset control = new PolygonOffset();

	/**
	 * Fields are set correctly
	 */
	@Test
	public void construction() {
		polyOffset.enabled( true ).factor( 0.5f ).units( 0.25f );

		assertThat( polyOffset.enabled ).isTrue();
		assertThat( polyOffset.factor ).isEqualTo( 0.5f );
		assertThat( polyOffset.units ).isEqualTo( 0.25f );
	}

	/**
	 * Comparison of identical states
	 */
	@Test
	public void same() {
		assertThat( polyOffset ).isEqualTo( control );
	}

	/**
	 * Fields are copied correctly
	 */
	@Test
	public void copy() {
		polyOffset.enabled( true ).factor( 0.5f ).units( 0.25f );

		PolygonOffset copy = new PolygonOffset();

		assertThat( copy ).isNotEqualTo( polyOffset );

		copy.from( polyOffset );

		assertThat( copy ).isEqualTo( polyOffset );
	}

	/**
	 * ENabled state sorts to after disabled
	 */
	@Test
	public void enabledComparison() {
		comparisonOrder( control, polyOffset.enabled( true ) );
	}

	/**
	 * Altered factor sorts to after default
	 */
	@Test
	public void factorComparison() {
		comparisonOrder( control, polyOffset.factor( 0.5f ) );
	}

	/**
	 * Altered unit sorts to after default
	 */
	@Test
	public void unitsComparison() {
		comparisonOrder( control, polyOffset.units( 0.5f ) );
	}

	/**
	 * Transition between identical state results in no GL calls
	 */
	@Test
	public void noopTransition() {
		polyOffset.transition( control );

		verify( Gdx.gl, never() ).glEnable( anyInt() );
		verify( Gdx.gl, never() ).glDisable( anyInt() );
		verify( Gdx.gl, never() ).glPolygonOffset( anyFloat(), anyFloat() );
	}

	/**
	 * Parameters are set correctly in transition
	 */
	@Test
	public void transition() {
		polyOffset.enabled( true ).factor( 0.5f ).units( 0.25f );

		polyOffset.transition( control );

		verify( Gdx.gl ).glEnable( GL20.GL_POLYGON_OFFSET_FILL );
		verify( Gdx.gl ).glPolygonOffset( 0.5f, 0.25f );
	}
}
