package com.rmn.gdxtend.gl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Test;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.rmn.gdxtend.GdxTest;
import com.rmn.gdxtend.geom.Shape;
import com.rmn.gdxtend.geom.Topology;
import com.rmn.gdxtend.gl.shader.None;

/**
 * Tests for {@link Renderer} operation
 */
public class RendererTest extends GdxTest {

	/**
	 * State array grows as needed
	 */
	@Test
	public void grow() {
		Renderer r = new Renderer();

		State<None> state = State.build( None.instance );

		State<?>[] larger = r.grow( state );

		assertThat( larger ).isEqualTo( new State[] { state } );
	}

	/**
	 * Adds a single state to the renderer
	 */
	@Test
	public void withOne() {
		Renderer r = new Renderer();

		assertThat( r.geometry ).hasSize( 0 );

		State<?> s = State.build( None.instance );

		r.with( s );

		assertThat( r.geometry ).hasSize( 1 );
		assertThat( r.geometry[ 0 ].state ).isEqualTo( s );
	}

	/**
	 * Add two identical states to a renderer - end up with one vertex batch
	 */
	@Test
	public void withSame() {

		Renderer r = new Renderer();

		assertThat( r.geometry ).hasSize( 0 );

		State<?> s = State.build( None.instance );
		State<?> t = State.build( None.instance );

		assertThat( s ).isEqualTo( t );

		r.with( s ).with( t );

		assertThat( r.geometry ).hasSize( 1 );
		assertThat( r.geometry[ 0 ].state ).isEqualTo( s );
	}

	/**
	 * Add two different states - end up with two batches
	 */
	@Test
	public void withTwo() {

		Renderer r = new Renderer();

		assertThat( r.geometry ).hasSize( 0 );

		State<None> s = State.build( None.instance );
		State<None> t = State.build( None.instance );
		t.blend.a( 0.5f );

		assertThat( s ).isLessThan( t );

		r.with( s ).with( t );

		assertThat( r.geometry ).hasSize( 2 );
		assertThat( r.geometry[ 0 ].state ).isEqualTo( s );
		assertThat( r.geometry[ 1 ].state ).isEqualTo( t );
	}

	/**
	 * Adds geometry to the renderer
	 */
	@Test
	public void addTriangles() {
		Renderer r = new Renderer().withInitialSize( 5 );
		assertThat( r.geometry ).hasSize( 0 );

		State<None> s = State.build( None.instance );

		float[] v = new float[ 3 ];
		Arrays.fill( v, 1 );
		short[] i = new short[] { 0, 1, 2 };

		r.addTriangles( s, v, i );

		assertThat( r.geometry ).hasSize( 1 );
		assertThat( r.geometry[ 0 ].vertices ).isEqualTo( new float[] {
				1, 1, 1,
				0, 0, 0,
				0, 0, 0,
				0, 0, 0,
				0, 0, 0,
		} );
		assertThat( r.geometry[ 0 ].indices ).isEqualTo( new short[] {
				0, 1, 2, 0, 0,
		} );

		Arrays.fill( v, 3 );

		r.addTriangles( s, v, i );
		assertThat( r.geometry ).hasSize( 1 );
		assertThat( r.geometry[ 0 ].vertices ).isEqualTo( new float[] {
				1, 1, 1,
				3, 3, 3,
				0, 0, 0,
				0, 0, 0,
				0, 0, 0,
		} );
		assertThat( r.geometry[ 0 ].indices ).isEqualTo( new short[] {
				0, 1, 2, 1, 2, 3, 0, 0, 0,
		} );
	}

	/**
	 * Exercises applying a transform as geometry is added
	 */
	@Test
	public void transformedTriangle() {
		Renderer r = new Renderer().withInitialSize( 5 );
		r.transform.translate( 10, 20, 30 );

		Shape triangle =
				new Shape( 1, Topology.triangles, VertexAttribute.Position() )
				.pos.xyz( 1, 2, 3 ).next()
				.pos.xyz( 4, 5, 6 ).next()
				.pos.xyz( 7, 8, 9 );

		r.add( State.build( None.instance ), triangle );

		assertThat( r.geometry ).hasSize( 1 );
		assertThat( r.geometry[ 0 ].vertices ).isEqualTo( new float[] {
				11, 22, 33,
				14, 25, 36,
				17, 28, 39,
				0, 0, 0,
				0, 0, 0,
		} );
	}
}
