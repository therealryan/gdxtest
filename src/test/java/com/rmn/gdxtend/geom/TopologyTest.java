package com.rmn.gdxtend.geom;

import static com.badlogic.gdx.graphics.VertexAttribute.Position;
import static com.rmn.gdxtend.geom.Topology.arrow;
import static com.rmn.gdxtend.geom.Topology.fan;
import static com.rmn.gdxtend.geom.Topology.loop;
import static com.rmn.gdxtend.geom.Topology.quads;
import static com.rmn.gdxtend.geom.Topology.radial;
import static com.rmn.gdxtend.geom.Topology.strip;
import static com.rmn.gdxtend.geom.Topology.triangles;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileWriter;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

/**
 * Tests for triangle structures created by {@link Topology} classes
 */
public class TopologyTest {

	/**
	 * Used to name the generated visualisations
	 */
	@Rule
	public TestName name = new TestName();

	private Shape s;

	/**
	 * <pre>
	 *   2
	 *  / \
	 * 0---1
	 * </pre>
	 */
	@Test
	public void oneTriangle() {
		s = new Shape( 1, triangles, Position() );
		assertThat( s.vertices() ).isEqualTo( 3 );
		assertThat( s.indices ).isEqualTo( new short[] {
				0, 1, 2 } );
	}

	/**
	 * <pre>
	 *   2     5
	 *  / \   / \
	 * 0---1 3---4
	 * </pre>
	 */
	@Test
	public void twoTriangles() {
		s = new Shape( 2, triangles, Position() );
		assertThat( s.vertices() ).isEqualTo( 6 );
		assertThat( s.indices ).isEqualTo( new short[] {
				0, 1, 2,
				3, 4, 5 } );
	}

	/**
	 * <pre>
	 *  1-3
	 *  |\|
	 *  0-2
	 * </pre>
	 */
	@Test
	public void oneQuad() {
		s = new Shape( 1, quads, Position() );
		assertThat( s.vertices() ).isEqualTo( 4 );
		assertThat( s.indices ).isEqualTo( new short[] {
				0, 2, 1,
				2, 3, 1 } );
	}

	/**
	 * <pre>
	 *  1-3 5-7
	 *  |\| |\|
	 *  0-2 4-6
	 * </pre>
	 */
	@Test
	public void twoQuads() {
		s = new Shape( 2, quads, Position() );
		assertThat( s.vertices() ).isEqualTo( 8 );
		assertThat( s.indices ).isEqualTo( new short[] {
				0, 2, 1,
				2, 3, 1,
				4, 6, 5,
				6, 7, 5 } );
	}

	/**
	 * <pre>
	 *   1
	 *  / \
	 * 0---2
	 * </pre>
	 */
	@Test
	public void oneStrip() {
		s = new Shape( 1, strip, Position() );
		assertThat( s.vertices() ).isEqualTo( 3 );
		assertThat( s.indices ).isEqualTo( new short[] {
				0, 2, 1 } );
	}

	/**
	 * <pre>
	 *   1---3
	 *  / \ /
	 * 0---2
	 * </pre>
	 */
	@Test
	public void twoStrip() {
		s = new Shape( 2, strip, Position() );
		assertThat( s.vertices() ).isEqualTo( 4 );
		assertThat( s.indices ).isEqualTo( new short[] {
				0, 2, 1,
				1, 2, 3 } );
	}

	/**
	 * <pre>
	 *   1---3
	 *  / \ / \
	 * 0---2---4
	 * </pre>
	 */
	@Test
	public void threeStrip() {
		s = new Shape( 3, strip, Position() );
		assertThat( s.vertices() ).isEqualTo( 5 );
		assertThat( s.indices ).isEqualTo( new short[] {
				0, 2, 1,
				1, 2, 3,
				2, 4, 3 } );
	}

	/**
	 * <pre>
	 *   1---3---1
	 *  / \ / \ /
	 * 0---2---0
	 * </pre>
	 */
	@Test
	public void fourLoop() {
		s = new Shape( 4, loop, Position() );
		assertThat( s.vertices() ).isEqualTo( 4 );
		assertThat( s.indices ).isEqualTo( new short[] {
				0, 2, 1,
				1, 2, 3,
				2, 0, 3,
				3, 0, 1 } );
	}

	/**
	 * <pre>
	 *   1---3---5---1
	 *  / \ / \ / \ /
	 * 0---2---4---0
	 * </pre>
	 */
	@Test
	public void sixLoop() {
		s = new Shape( 6, loop, Position() );
		assertThat( s.vertices() ).isEqualTo( 6 );
		assertThat( s.indices ).isEqualTo( new short[] {
				0, 2, 1,
				1, 2, 3,
				2, 4, 3,
				3, 4, 5,
				4, 0, 5,
				5, 0, 1 } );
	}

	/**
	 * <pre>
	 *   2
	 *  / \
	 * 0---1
	 * </pre>
	 */
	@Test
	public void threeFan() {
		s = new Shape( 3, fan, Position() );
		assertThat( s.vertices() ).isEqualTo( 3 );
		assertThat( s.indices ).isEqualTo( new short[] {
				0, 1, 2 } );
	}

	/**
	 * <pre>
	 *  3-2
	 *  |/|
	 *  0-1
	 * </pre>
	 */
	@Test
	public void fourFan() {
		s = new Shape( 4, fan, Position() );
		assertThat( s.vertices() ).isEqualTo( 4 );
		assertThat( s.indices ).isEqualTo( new short[] {
				0, 1, 2,
				0, 2, 3 } );
	}

	/**
	 * <pre>
	 * 4-3-2
	 *  \|/|
	 *   0-1
	 * </pre>
	 */
	@Test
	public void fiveFan() {
		s = new Shape( 5, fan, Position() );
		assertThat( s.vertices() ).isEqualTo( 5 );
		assertThat( s.indices ).isEqualTo( new short[] {
				0, 1, 2,
				0, 2, 3,
				0, 3, 4 } );
	}

	/**
	 * <pre>
	 *   1
	 *  /|\
	 * | 0 |
	 *  \|/
	 *   2
	 * </pre>
	 */
	@Test
	public void twoRadial() {
		s = new Shape( 2, radial, Position() );
		assertThat( s.vertices() ).isEqualTo( 3 );
		assertThat( s.indices ).isEqualTo( new short[] {
				0, 1, 2,
				0, 2, 1 } );
	}

	/**
	 * <pre>
	 *   2
	 *  /|\
	 * | 0 |
	 * |/ \|
	 * 3---1
	 * </pre>
	 */
	@Test
	public void threeRadial() {
		s = new Shape( 3, radial, Position() );
		assertThat( s.vertices() ).isEqualTo( 4 );
		assertThat( s.indices ).isEqualTo( new short[] {
				0, 1, 2,
				0, 2, 3,
				0, 3, 1 } );
	}

	/**
	 * <pre>
	 * 2---1
	 * |\ /|
	 * | 0 |
	 * |/ \|
	 * 3---4
	 * </pre>
	 */
	@Test
	public void fourRadial() {
		s = new Shape( 4, radial, Position() );
		assertThat( s.vertices() ).isEqualTo( 5 );
		assertThat( s.indices ).isEqualTo( new short[] {
				0, 1, 2,
				0, 2, 3,
				0, 3, 4,
				0, 4, 1 } );
	}

	/**
	 * <pre>
	 * 1
	 * |\
	 * 3_\
	 * |  0
	 * 4‾/
	 * |/
	 * 2
	 * </pre>
	 */
	@Test
	public void stemlessArrow() {
		s = new Shape( 0, arrow, Position() );
		assertThat( s.vertices() ).isEqualTo( 5 );
		assertThat( s.indices ).isEqualTo( new short[] {
				0, 1, 3,
				0, 3, 4,
				0, 4, 2
		} );
	}

	/**
	 * <pre>
	 *   1
	 *   |\
	 * 5-3_\
	 * |\|  0
	 * 6-4‾/
	 *   |/
	 *   2
	 * </pre>
	 */
	@Test
	public void singleSegArrow() {
		s = new Shape( 1, arrow, Position() );
		assertThat( s.vertices() ).isEqualTo( 7 );
		assertThat( s.indices ).isEqualTo( new short[] {
				0, 1, 3,
				0, 3, 4,
				0, 4, 2,
				3, 5, 4,
				4, 5, 6
		} );
	}

	/**
	 * <pre>
	 *        1
	 *        |\
	 *  9-7-5-3_\
	 *  |\|\|\|  0
	 * 10-8-6-4‾/
	 *        |/
	 *        2
	 * </pre>
	 */
	@Test
	public void tripleSegArrow() {
		s = new Shape( 3, arrow, Position() );
		assertThat( s.vertices() ).isEqualTo( 11 );
		assertThat( s.indices ).isEqualTo( new short[] {
				0, 1, 3,
				0, 3, 4,
				0, 4, 2,
				3, 5, 4,
				4, 5, 6,
				5, 7, 6,
				6, 7, 8,
				7, 9, 8,
				8, 9, 10
		} );
	}

	/**
	 * Creates a visualisation of the topolgies
	 */
	@After
	public void visualise() {
		try {
			File dir = new File( "generated" );
			dir.mkdirs();

			FileWriter out =
					new FileWriter( "generated/" + name.getMethodName() + ".html" );
			out.write( "<html>\n\t<head>\n\t\t<title>" );
			out.write( name.getMethodName() );
			out.write( "</title>\n" );
			out.write( "\t\t<script type=\"text/javascript\" src=\"../src/test/resources/vis.min.js\"></script>\n" );
			out.write( "\t\t<link href=\"../src/test/resources/vis.min.css\" rel=\"stylesheet\" type=\"text/css\" />\n" );
			out.write( "\t</head>\n\t<body>\n\t\t<div id=\"mynetwork\"></div>\n" );
			out.write( "\t\t<script type=\"text/javascript\">\n" );
			out.write( "var container = document.getElementById('mynetwork');\n" );
			out.write( "var data = { dot: 'digraph {" );
			String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLIMNOPQRSTUVXYZ";
			for( int i = 0; i < s.indices.length; i += 3 ) {
				char label = alphabet.charAt( ( i / 3 ) % alphabet.length() );
				out.write( s.indices[ i ] + " -> " + s.indices[ i + 1 ]
						+ "[label=" + label + "];" );
				out.write( s.indices[ i + 1 ] + " -> " + s.indices[ i + 2 ]
						+ "[label=" + label + "];" );
				out.write( s.indices[ i + 2 ] + " -> " + s.indices[ i ]
						+ "[label=" + label + "];" );
			}
			out.write( "}' };\n" );
			out.write( "var network = new vis.Network(container, data);\n" );
			out.write( "\t\t</script>\n\t</body>\n</html>" );

			out.close();
		}
		catch( Exception e ) {
			e.printStackTrace();
		}
	}
}
