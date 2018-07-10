package com.rmn.gdxtend.gl.facets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.rmn.gdxtend.gl.enums.BlendEquation;
import com.rmn.gdxtend.gl.enums.DestinationFactor;
import com.rmn.gdxtend.gl.enums.SourceFactor;

/**
 * Tests for {@link Blend}
 */
public class BlendTest extends FacetTest {

	private final Blend blend = new Blend();
	private final Blend control = new Blend();

	/**
	 * All fields are set correctly
	 */
	@Test
	public void construction() {
		blend.enabled( true )
				.r( 0.5f )
				.g( 0.25f )
				.b( 0.125f )
				.a( 0.0625f )
				.src( SourceFactor.CONSTANT_COLOR )
				.dst( DestinationFactor.CONSTANT_COLOR )
				.equation( BlendEquation.GL_FUNC_SUBTRACT );

		assertThat( blend.enabled ).isTrue();
		assertThat( blend.color.r ).isEqualTo( 0.5f );
		assertThat( blend.color.g ).isEqualTo( 0.25f );
		assertThat( blend.color.b ).isEqualTo( 0.125f );
		assertThat( blend.color.a ).isEqualTo( 0.0625f );
		assertThat( blend.srcFactor ).isEqualTo( SourceFactor.CONSTANT_COLOR );
		assertThat( blend.destFactor ).isEqualTo( DestinationFactor.CONSTANT_COLOR );
		assertThat( blend.equation ).isEqualTo( BlendEquation.GL_FUNC_SUBTRACT );
	}

	/**
	 * Comparison of identical objects
	 */
	@Test
	public void same() {
		assertThat( blend ).isEqualTo( control );
	}

	/**
	 * All fields are copied correctly
	 */
	@Test
	public void copy() {
		blend.enabled( true )
				.r( 0.5f )
				.g( 0.25f )
				.b( 0.125f )
				.a( 0.0625f )
				.src( SourceFactor.CONSTANT_COLOR )
				.dst( DestinationFactor.CONSTANT_COLOR )
				.equation( BlendEquation.GL_FUNC_SUBTRACT );

		Blend copy = new Blend();

		assertThat( copy ).isNotEqualTo( blend );

		copy.from( blend );

		assertThat( copy ).isEqualTo( blend );
	}

	/**
	 * Enabled states sort to after disabled states
	 */
	@Test
	public void enabledComparison() {
		comparisonOrder( control, blend.enabled( true ) );
	}

	/**
	 * Altered src factor sorts to after default
	 */
	@Test
	public void srcFactorComparison() {
		comparisonOrder( control, blend.src( SourceFactor.ZERO ) );
	}

	/**
	 * Altered dst factor sorts to after default
	 */
	@Test
	public void dstFactorComparison() {
		comparisonOrder( control, blend.dst( DestinationFactor.ONE ) );
	}

	/**
	 * Altered function sorts to after default
	 */
	@Test
	public void equationComparison() {
		comparisonOrder( control, blend.equation( BlendEquation.GL_FUNC_SUBTRACT ) );
	}

	/**
	 * Altered colour sorts to after default
	 */
	@Test
	public void colorComparison() {
		comparisonOrder( control, blend.r( 1 ) );
	}

	/**
	 * Transition between similar {@link Blend} states does not cause any GL calls
	 */
	public void noopTransition() {
		blend.transition( control );

		verify( Gdx.gl, never() ).glEnable( anyInt() );
		verify( Gdx.gl, never() ).glDisable( anyInt() );
		verify( Gdx.gl, never() ).glBlendFunc( anyInt(), anyInt() );
		verify( Gdx.gl, never() ).glBlendEquation( anyInt() );
		verify( Gdx.gl, never() ).glBlendColor( anyFloat(), anyFloat(),
				anyFloat(), anyFloat() );
	}

	/**
	 * Values are applied correctly during transition
	 */
	@Test
	public void transition() {

		blend.enabled( true ).r( 0.5f ).g( 0.25f ).b( 0.125f ).a( 0.0625f )
				.src( SourceFactor.CONSTANT_COLOR ).dst( DestinationFactor.DST_COLOR )
				.equation( BlendEquation.GL_FUNC_SUBTRACT );

		blend.transition( control );

		verify( Gdx.gl ).glEnable( GL20.GL_BLEND );
		verify( Gdx.gl ).glBlendFunc( GL20.GL_CONSTANT_COLOR, GL20.GL_DST_COLOR );
		verify( Gdx.gl ).glBlendEquation( GL20.GL_FUNC_SUBTRACT );
		verify( Gdx.gl ).glBlendColor( 0.5f, 0.25f, 0.125f, 0.0625f );

		control.transition( blend );

		verify( Gdx.gl ).glDisable( GL20.GL_BLEND );
	}
}
