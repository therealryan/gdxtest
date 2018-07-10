package com.rmn.gdxtend.gl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.rmn.gdxtend.GdxTest;
import com.rmn.gdxtend.gl.enums.MagFilter;
import com.rmn.gdxtend.gl.shader.None;

/**
 * Tests for {@link State} comparison and compilation
 */
public class StateTest extends GdxTest {

	private State<None> base;
	private State<?>[] altered = new State[ 6 ];

	/**
	 * Builds a typical state
	 */
	@Before
	public void build() {
		base = State.build( None.instance );

		for( int i = 0; i < altered.length; i++ ) {
			altered[ i ] = State.build( None.instance );
		}

		altered[ 0 ].blend.r( 1 );
		altered[ 1 ].clear.r( 1 );
		altered[ 2 ].depthTest.enabled( true );
		altered[ 3 ].polyOffset.enabled( true );
		altered[ 4 ].stencil.enabled( true );
		altered[ 5 ].texture.mag( MagFilter.NEAREST );
	}

	/**
	 * State comparison
	 */
	@Test
	public void comparison() {
		{ // same
			State<None> s = State.build( None.instance );
			assertThat( s ).isEqualTo( base );
			assertThat( s.compareTo( base ) == 0 );
		}

		// check altered ordering
		for( int i = 0; i < altered.length; i++ ) {
			State<?> a = altered[ i ];

			assertThat( base ).as( "index " + i ).isNotEqualTo( a );
			assertThat( base.compareTo( a ) ).as( "index " + i ).isEqualTo( -1 );
			assertThat( a.compareTo( base ) ).as( "index " + i ).isEqualTo( 1 );
		}
	}

	/**
	 * Compile two identical states - they end up with the same index
	 */
	@Test
	public void compilationSame() {
		State<None> s = State.build( None.instance );
		State<None> t = State.build( None.instance );

		int distinct = State.compile( s, t );

		assertThat( distinct ).isEqualTo( 1 );

		assertThat( s.getCompiledIndex() ).isEqualTo( 0 );
		assertThat( t.getCompiledIndex() ).isEqualTo( 0 );
	}

	/**
	 * Compile two different states, the get different indices
	 */
	@Test
	public void compilationDifferent() {
		State<None> s = State.build( None.instance );
		State<None> t = State.build( None.instance );
		t.blend.r( 0.5f );

		int distinct = State.compile( s, t );

		assertThat( distinct ).isEqualTo( 2 );

		assertThat( s.getCompiledIndex() ).isEqualTo( 0 );
		assertThat( t.getCompiledIndex() ).isEqualTo( 1 );
	}

	/**
	 * Compile two states, they get the same compilation batch id
	 */
	@Test
	public void compilationBatch() {
		State<None> s = State.build( None.instance );

		State.compile( s );

		int batch = s.getCompilationBatch();

		State.compile( s );

		assertThat( s.getCompilationBatch() ).isEqualTo( batch + 1 );
	}

	/**
	 * Comparison of compiled states is on the basis of index rather than their
	 * actual values
	 */
	@Test
	public void compiledComparison() {
		State<None> s = State.build( None.instance );
		State<None> t = State.build( None.instance );

		State.compile( s, t );

		// alter a facet
		t.blend.r( 1 );

		// we're now comparing by index, so the change is not noticed
		assertThat( s ).isEqualTo( t );
	}
}
