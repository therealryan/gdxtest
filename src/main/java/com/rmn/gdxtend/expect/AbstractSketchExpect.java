package com.rmn.gdxtend.expect;

import java.awt.Color;

import org.jfree.graphics2d.svg.SVGGraphics2D;

/**
 * Supports tests where the result is a drawing
 *
 * @param <T>
 *          self type
 */
public class AbstractSketchExpect<T extends AbstractSketchExpect<T>> extends
		AbstractExpect<T> {

	protected AbstractSketchExpect() {
		super( ".svg" );
	}

	/**
	 * Creates a new SVG context and sets the paint to black
	 * 
	 * @param width
	 *          width of resultant sketch
	 * @param height
	 *          height of resultant sketch
	 * @return the context
	 */
	public SVGGraphics2D create( int width, int height ) {
		SVGGraphics2D g = new SVGGraphics2D( width, height );
		g.setColor( Color.BLACK );
		g.drawRect( 0, 0, width, height );
		return g;
	}

	/**
	 * @param g
	 *          the sketch
	 */
	public void check( SVGGraphics2D g ) {
		super.check( pretty( g.getSVGDocument() ) );
	}

	/**
	 * Not how it's supposed to be done, but a hell of a lot faster
	 * 
	 * @param xml
	 *          unindented xml
	 * @return indented xml
	 */
	protected static String pretty( String xml ) {
		// strip comments
		xml = xml.replaceAll( "<!--.*-->", "" );
		// add newlines between adjacent tags
		xml = xml.replaceAll( "><", ">\n<" );
		// indent
		StringBuilder sb = new StringBuilder();
		int indent = 0;
		for( String line : xml.split( "\n" ) ) {
			if( line.endsWith( "/>" )
					|| line.startsWith( "<!" ) || line.startsWith( "<?" )
					|| line.matches( "<(.*?)\\s.*?>.*?</\\1>" ) ) {
				// standalone tag, declarations, element
				indent( indent, sb );
				sb.append( line ).append( "\n" );
			}
			else if( line.startsWith( "</" ) ) {
				// block close
				indent--;
				indent( indent, sb );
				sb.append( line ).append( "\n" );
			}
			else {
				// block open
				indent( indent, sb );
				sb.append( line ).append( "\n" );
				indent++;
			}
		}

		return sb.toString();
	}

	private static void indent( int d, StringBuilder sb ) {
		for( int i = 0; i < d; i++ ) {
			sb.append( "  " );
		}
	}
}
