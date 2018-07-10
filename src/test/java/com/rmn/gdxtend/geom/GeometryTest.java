package com.rmn.gdxtend.geom;

import static com.badlogic.gdx.graphics.VertexAttribute.Position;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rmn.gdxtend.GdxTestRunner;
import com.rmn.gdxtend.expect.ShapeExpect;

/**
 * Tests for {@link Geometry} classes
 */
@RunWith( GdxTestRunner.class )
public class GeometryTest {

	/**
	 * Produces the shape sketches
	 */
	@Rule
	public ShapeExpect expect = new ShapeExpect().scale( 100 );

	private static final float TOLERANCE = 0.000001f;

	/**
	 * <pre>
	 *   1
	 * 2   0
	 *   3
	 * </pre>
	 */
	@Test
	public void fourSegmentCirle() {
		Shape s = new Shape( 4, Topology.fan, Position() );
		Geometry.circle.define( s );

		Assert.assertArrayEquals( new float[] {
				1, 0, 0,
				0, 1, 0,
				-1, 0, 0,
				0, -1, 0,
		}, s.vertexData, TOLERANCE );

		expect.check( s );
	}

	/**
	 * <pre>
	 *   2
	 * 3 0 1
	 *   4
	 * </pre>
	 */
	@Test
	public void fourSegmentCenteredCirle() {
		Shape s = new Shape( 4, Topology.radial, Position() );
		Geometry.centeredCircle.define( s );

		Assert.assertArrayEquals( new float[] {
				0, 0, 0,
				1, 0, 0,
				0, 1, 0,
				-1, 0, 0,
				0, -1, 0,
		}, s.vertexData, TOLERANCE );

		expect.check( s );
	}

	/**
	 * <pre>
	 * 3 2 1
	 * 4   0
	 * 5 6 7
	 * </pre>
	 */
	@Test
	public void eightSegmentCirle() {
		Shape shape = new Shape( 8, Topology.fan, Position() );
		Geometry.circle.define( shape );

		float c = (float) Math.cos( Math.toRadians( 45 ) );
		float s = (float) Math.sin( Math.toRadians( 45 ) );

		Assert.assertArrayEquals( new float[] {
				1, 0, 0,
				c, s, 0,
				0, 1, 0,
				-c, s, 0,
				-1, 0, 0,
				-c, -s, 0,
				0, -1, 0,
				c, -s, 0,
		}, shape.vertexData, TOLERANCE );

		expect.check( shape );
	}

	/**
	 * <pre>
	 * 4 3 2
	 * 5 0 1
	 * 6 7 8
	 * </pre>
	 */
	@Test
	public void eightSegmentCenteredCirle() {
		Shape shape = new Shape( 8, Topology.radial, Position() );
		Geometry.centeredCircle.define( shape );

		float c = (float) Math.cos( Math.toRadians( 45 ) );
		float s = (float) Math.sin( Math.toRadians( 45 ) );

		Assert.assertArrayEquals( new float[] {
				0, 0, 0,
				1, 0, 0,
				c, s, 0,
				0, 1, 0,
				-c, s, 0,
				-1, 0, 0,
				-c, -s, 0,
				0, -1, 0,
				c, -s, 0,
		}, shape.vertexData, TOLERANCE );

		expect.check( shape );
	}

	/**
	 * <pre>
	 *     2
	 *     3
	 * 4 5   1 0
	 *     7
	 *     6
	 * </pre>
	 */
	@Test
	public void fourSegmentCircleStrip() {
		Shape shape = new Shape( 8, Topology.loop, Position() );
		Geometry.circleStrip.withWidth( 0.25f ).define( shape );

		float outer = 1.0f;
		float inner = 0.75f;
		Assert.assertArrayEquals( new float[] {
				outer, 0, 0,
				inner, 0, 0,
				0, outer, 0,
				0, inner, 0,
				-outer, 0, 0,
				-inner, 0, 0,
				0, -outer, 0,
				0, -inner, 0,
		}, shape.vertexData, TOLERANCE );

		expect.check( shape );
	}

	/**
	 * Rounded quad
	 */
	@Test
	public void roundedQuad() {
		Shape s = new Shape( 16, Topology.fan, Position() );
		Geometry.roundedQuad
				.withWidth( 5 )
				.withHeight( 5 )
				.withRadius( 1 )
				.define( s );

		expect.check( s );
	}
}
