package com.rmn.gdxtend.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

/**
 * Tests for {@link ArrayUtil}
 */
public class ArrayUtilTest {

	/**
	 * Tests for parsing string to array
	 */
	@Test
	public void bytesFromString() {
		byte[] input = new byte[ 1000 ];
		new Random().nextBytes( input );

		String serialised = Arrays.toString( input );

		{ // null dest
			byte[] output = ArrayUtil.fromString( serialised, (byte[]) null );
			assertThat( output ).isEqualTo( input );
		}

		{ // dest too small
			byte[] dest = new byte[ input.length - 1 ];
			byte[] output = ArrayUtil.fromString( serialised, dest );
			assertThat( output ).isEqualTo( input );
			assertThat( output ).isNotSameAs( dest );
		}

		{ // dest fits
			byte[] dest = new byte[ input.length ];
			byte[] output = ArrayUtil.fromString( serialised, dest );
			assertThat( output ).isEqualTo( input );
			assertThat( output ).isSameAs( dest );
		}
	}

	/**
	 * Tests for printing multidimensional object arrays
	 */
	@Test
	public void objectToString() {
		assertThat( ArrayUtil.toString( new String[] { "a", "b", "c" } ) )
				.isEqualTo( "[a, b, c]" );
		assertThat(
				ArrayUtil.toString( new String[][] { { "a" }, { "b" }, { "c" } } ) )
				.isEqualTo( "[[a], [b], [c]]" );

		assertThat(
				ArrayUtil.toString( new Object[] { null,
						new String[] { "a", "b", null }, "c" } ) )
				.isEqualTo( "[null, [a, b, null], c]" );
	}

	/**
	 * Tests for printing multidimensional primitive arrays
	 */
	@Test
	public void primitiveToString() {
		assertThat( ArrayUtil.toString(
				new Object[] { new int[] { 1 }, new int[] { 2 }, new int[] { 3 } } ) )
				.isEqualTo( "[[1], [2], [3]]" );

		assertThat(
				ArrayUtil.toString( new Object[] {
						new boolean[] { true },
						new byte[] { 1 },
						new short[] { 2 },
						new char[] { 'a' },
						new int[] { 3 },
						new float[] { 4.5f },
						new long[] { 6 },
						new double[] { 7.8 },
				} ) )
				.isEqualTo( "[[true], [1], [2], [a], [3], [4.5], [6], [7.8]]" );
	}
}
