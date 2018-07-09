package com.rmn.gdxtend.expect;

import java.awt.Color;

import org.jfree.graphics2d.svg.SVGGraphics2D;

import com.rmn.gdxtend.geom.Shape;

/**
 * Results in a sketch of the triangle structure of a shape
 */
public class ShapeExpect extends AbstractSketchExpect<ShapeExpect> {

	private float scale = 1;

	/**
	 * @param s
	 *          scaling factor for the sketch
	 * @return this
	 */
	public ShapeExpect scale( float s ) {
		this.scale = s;
		return this;
	}

	/**
	 * Produces a result file containing a sketch of a shape
	 * 
	 * @param shape
	 *          the shape to draw
	 */
	public void check( Shape shape ) {
		shape = new Shape( shape ).pos.all().scale( scale, scale, scale ).apply();

		float minX = Float.MAX_VALUE, maxX = -Float.MAX_VALUE;
		float minY = Float.MAX_VALUE, maxY = -Float.MAX_VALUE;

		// find bounds
		for( int i = 0; i < shape.vertices(); i++ ) {
			shape.index( i );
			minX = Math.min( minX, shape.pos.x() );
			minY = Math.min( minY, shape.pos.y() );
			maxX = Math.max( maxX, shape.pos.x() );
			maxY = Math.max( maxY, shape.pos.y() );
		}

		int width = (int) ( maxX - minX );
		int hMargin = (int) ( width * 0.125f );
		int height = (int) ( maxY - minY );
		int vMargin = (int) ( height * 0.125f );

		SVGGraphics2D g = new SVGGraphics2D( width + 2 * hMargin,
				height + 2 * vMargin );

		g.setColor( Color.LIGHT_GRAY );
		g.drawRect( hMargin, vMargin, width, height );
		g.drawString( String.valueOf( ( maxX - minX ) / scale ),
				hMargin + 0.75f * width,
				vMargin + height + 15 );
		g.drawString( String.valueOf( ( maxY - minY ) / scale ),
				hMargin + width + 3,
				vMargin + 0.75f * height );

		int xfs = (int) ( minX - hMargin );
		int yfs = (int) ( minY - vMargin );

		g.setColor( Color.black );
		for( int i = 0; i < shape.indices.length; i += 3 ) {
			shape.index( shape.indices[ i ] );
			int ax = (int) ( shape.pos.x() - xfs );
			int ay = (int) ( shape.pos.y() - yfs );
			shape.index( shape.indices[ i + 1 ] );
			int bx = (int) ( shape.pos.x() - xfs );
			int by = (int) ( shape.pos.y() - yfs );
			shape.index( shape.indices[ i + 2 ] );
			int cx = (int) ( shape.pos.x() - xfs );
			int cy = (int) ( shape.pos.y() - yfs );

			g.drawLine( ax, ay, bx, by );
			g.drawLine( bx, by, cx, cy );
			g.drawLine( cx, cy, ax, ay );
		}

		check( g );
	}
}
