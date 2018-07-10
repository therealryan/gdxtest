package com.rmn.gdxtend.gl.facets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.Mock;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.rmn.gdxtend.gl.enums.MagFilter;
import com.rmn.gdxtend.gl.enums.MinFilter;

/**
 * Tests for {@link TextureState}
 */
public class TextureStateTest extends FacetTest {

	private TextureState tex = new TextureState();
	private TextureState control = new TextureState();

	@Mock
	private Texture imageA;

	@Mock
	private Texture imageB;

	/**
	 * Fields are set correctly
	 */
	@Test
	public void construction() {
		tex.with( imageA );
		tex.min( MinFilter.LINEAR_MIPMAP_LINEAR ).mag( MagFilter.NEAREST );
		tex.s( TextureWrap.MirroredRepeat ).t( TextureWrap.ClampToEdge );

		assertThat( tex.texture ).isEqualTo( imageA );
		assertThat( tex.min ).isEqualTo( MinFilter.LINEAR_MIPMAP_LINEAR );
		assertThat( tex.mag ).isEqualTo( MagFilter.NEAREST );
		assertThat( tex.s ).isEqualTo( TextureWrap.MirroredRepeat );
		assertThat( tex.t ).isEqualTo( TextureWrap.ClampToEdge );
	}

	/**
	 * Comparison of identical states
	 */
	@Test
	public void same() {
		assertThat( tex ).isEqualTo( control );
	}

	/**
	 * Fields are copied correctly
	 */
	@Test
	public void copy() {
		tex.with( imageA );
		tex.min( MinFilter.LINEAR_MIPMAP_LINEAR ).mag( MagFilter.NEAREST );
		tex.s( TextureWrap.MirroredRepeat ).t( TextureWrap.ClampToEdge );

		TextureState copy = new TextureState();

		assertThat( copy ).isNotEqualTo( tex );

		copy.from( tex );

		assertThat( copy ).isEqualTo( tex );
	}

	/**
	 * Altered texture id sorts to after default
	 */
	@Test
	public void idComparison() {
		comparisonOrder( control, tex.with( imageA ) );
	}

	/**
	 * Altered minification filter sorts to after default
	 */
	@Test
	public void minComparison() {
		comparisonOrder( control, tex.min( MinFilter.LINEAR_MIPMAP_LINEAR ) );
	}

	/**
	 * Altered magnification sorts to after default
	 */
	@Test
	public void magComparison() {
		comparisonOrder( control, tex.mag( MagFilter.NEAREST ) );
	}

	/**
	 * Altered horizontal texture wrap sorts to after default
	 */
	@Test
	public void sComparison() {
		comparisonOrder( control, tex.s( TextureWrap.MirroredRepeat ) );
	}

	/**
	 * Altered vertical texture wrap sorts to after default
	 */
	@Test
	public void tComparison() {
		comparisonOrder( control, tex.t( TextureWrap.MirroredRepeat ) );
	}

	/**
	 * Transition between identical states results in no GL calls
	 */
	@Test
	public void noopTransition() {
		tex.transition( control );

		verify( Gdx.gl, never() ).glEnable( anyInt() );
		verify( Gdx.gl, never() ).glDisable( anyInt() );
		verify( Gdx.gl, never() ).glTexParameterf( anyInt(), anyInt(), anyFloat() );
	}

	/**
	 * Transition to enabled states causes all parameters to be set
	 */
	@Test
	public void enableTransition() {
		tex.with( imageA )
				.min( MinFilter.LINEAR_MIPMAP_LINEAR )
				.mag( MagFilter.NEAREST )
				.s( TextureWrap.MirroredRepeat )
				.t( TextureWrap.ClampToEdge );

		tex.transition( control );

		verify( imageA )
				.bind();
		verify( imageA )
				.unsafeSetFilter( TextureFilter.MipMapLinearLinear,
						TextureFilter.Nearest );
		verify( imageA )
				.unsafeSetWrap( TextureWrap.MirroredRepeat, TextureWrap.ClampToEdge );
	}

	/**
	 * Parameters are set correctly on transition
	 */
	@Test
	public void paramTransition() {
		tex.with( imageA );
		tex.min( MinFilter.LINEAR_MIPMAP_LINEAR ).mag( MagFilter.NEAREST );
		tex.s( TextureWrap.MirroredRepeat ).t( TextureWrap.ClampToEdge );

		TextureState other = new TextureState().from( tex );
		other.min( MinFilter.LINEAR ).mag( MagFilter.LINEAR )
				.s( TextureWrap.ClampToEdge ).t( TextureWrap.Repeat );

		other.transition( tex );

		verify( imageA, never() )
				.bind();
		verify( imageA )
				.unsafeSetFilter( TextureFilter.Linear, TextureFilter.Linear );
		verify( imageA )
				.unsafeSetWrap( TextureWrap.ClampToEdge, TextureWrap.Repeat );
	}

	/**
	 * Parameters are set correctly on texture disable
	 */
	@Test
	public void disableTransition() {

		tex.with( imageA );

		control.transition( tex );

		verify( Gdx.gl )
				.glBindTexture( GL20.GL_TEXTURE_2D, 0 );
		verify( imageA, never() )
				.unsafeSetFilter( isA( TextureFilter.class ), isA( TextureFilter.class ) );
		verify( imageA, never() )
				.unsafeSetWrap( isA( TextureWrap.class ), isA( TextureWrap.class ) );
	}
}
