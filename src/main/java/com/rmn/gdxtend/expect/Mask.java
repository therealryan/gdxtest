package com.rmn.gdxtend.expect;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A set of masking operations
 */
public class Mask {

	private String pendingPattern;
	private int pendingFlags;
	private boolean captures;

	private Deque<Op> operations = new ArrayDeque<>();

	/**
	 * @param pattern
	 *          A pattern for strings to mask
	 * @return this
	 */
	public Mask mask( String pattern ) {
		captures = false;
		pendingPattern = pattern;
		return this;
	}

	/**
	 * @param pattern
	 *          A pattern with capture groups to mask
	 * @return this
	 */
	public Mask maskCaptures( String pattern ) {
		captures = true;
		pendingPattern = pattern;
		return this;
	}

	/**
	 * @param flags
	 *          A set of regex match flags to use for the next mask
	 * @return this
	 */
	public Mask match( int... flags ) {
		pendingFlags = 0;
		for( int f : flags ) {
			pendingFlags |= f;
		}
		return this;
	}

	/**
	 * @param replacement
	 *          the strings to use to replace masked input
	 * @return this
	 */
	public Mask with( String... replacement ) {

		if( captures ) {
			operations.add( new CaptureReplacement( pendingPattern, pendingFlags,
					replacement ) );
		}
		else {
			if( replacement.length != 1 ) {
				throw new IllegalArgumentException(
						"Simple masks must have exactly 1 replacement string" );
			}
			operations.add( new Replacement( pendingPattern, pendingFlags,
					replacement[ 0 ] ) );
		}

		pendingPattern = null;
		pendingFlags = 0;
		return this;
	}

	/**
	 * Applies the masks
	 * 
	 * @param in
	 *          unmasked string
	 * @return masked string
	 */
	public String apply( String in ) {
		String masked = in;
		List<Op> toRun = new ArrayList<>( operations );

		for( Op op : toRun ) {
			masked = op.apply( masked );
		}
		return masked;
	}

	private abstract class Op {
		protected final Pattern pattern;

		protected Op( String pattern, int flags ) {
			this.pattern = Pattern.compile( pattern, flags );
		}

		public abstract String apply( String input );
	}

	private class Replacement extends Op {
		private final String replacement;

		public Replacement( String pattern, int flags, String replacement ) {
			super( pattern, flags );
			this.replacement = replacement;
		}

		@Override
		public String apply( String input ) {
			Matcher m = pattern.matcher( input );
			return m.replaceAll( replacement );
		}
	}

	private class CaptureReplacement extends Op {
		private final String[] replacements;

		public CaptureReplacement( String pattern, int flags,
				String... replacements ) {
			super( pattern, flags );
			this.replacements = replacements;
		}

		@Override
		public String apply( String input ) {

			// build a list of replacements from the captures that we find
			List<Replacement> toRun = new ArrayList<>();
			Matcher m = pattern.matcher( input );
			int count = 0;
			while( m.find() ) {
				count++;
				for( int i = 0; i < replacements.length && i < m.groupCount(); i++ ) {
					// avoid masking stuff we've already masked
					if( !m.group( i + 1 ).matches( replacements[ i ] + "_\\d+" ) ) {
						toRun.add( new Replacement( m.group( i + 1 ), pattern.flags(),
								replacements[ i ] + "_" + count ) );
					}
				}
			}

			// ad them to this mask and run them
			String masked = input;
			for( Replacement r : toRun ) {
				operations.addFirst( r );
				masked = r.apply( masked );
			}

			return masked;
		}
	}
}
