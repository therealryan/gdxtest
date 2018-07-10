package com.rmn.gdxtend.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link Comparison} utility
 */
public class ComparisonTest {

	private Comparison cmp = Comparison.instance;

	/**
	 * <code>false</code> is less than <code>true</code>
	 */
	@Test
	public void booleanComparison() {
		assertThat( cmp.compare( false, false ).result() ).isEqualTo( 0 );
		assertThat( cmp.compare( false, true ).result() ).isEqualTo( -1 );
		assertThat( cmp.compare( true, true ).result() ).isEqualTo( 0 );
		assertThat( cmp.compare( true, false ).result() ).isEqualTo( 1 );
	}

	/**
	 * Values that match the minimum are less than those that don't
	 */
	@Test
	public void booleanMinimum() {
		assertThat( cmp.compare( false, false, false ).result() ).isEqualTo( 0 );
		assertThat( cmp.compare( false, true, false ).result() ).isEqualTo( -1 );
		assertThat( cmp.compare( true, true, false ).result() ).isEqualTo( 0 );
		assertThat( cmp.compare( true, false, false ).result() ).isEqualTo( 1 );

		assertThat( cmp.compare( false, false, true ).result() ).isEqualTo( 0 );
		assertThat( cmp.compare( false, true, true ).result() ).isEqualTo( 1 );
		assertThat( cmp.compare( true, true, true ).result() ).isEqualTo( 0 );
		assertThat( cmp.compare( true, false, true ).result() ).isEqualTo( -1 );
	}

	/**
	 * Smaller numbers are less than larger numbers, doy!
	 */
	@Test
	public void floatComparison() {
		assertThat( cmp.compare( 0, 0 ).result() ).isEqualTo( 0 );
		assertThat( cmp.compare( 0, 1 ).result() ).isEqualTo( -1 );
		assertThat( cmp.compare( 1, 0 ).result() ).isEqualTo( 1 );
	}

	/**
	 * Numbers closer to the minimum are less
	 */
	@Test
	public void floatMinimum() {
		assertThat( cmp.compare( 9, 12, 10 ).result() ).isEqualTo( -1 );
		assertThat( cmp.compare( 12, 9, 10 ).result() ).isEqualTo( 1 );
		assertThat( cmp.compare( 9, 9, 10 ).result() ).isEqualTo( 0 );

		// when the two values are equidistant, the smaller is less
		assertThat( cmp.compare( 9, 11, 10 ).result() ).isEqualTo( -1 );
		assertThat( cmp.compare( 11, 9, 10 ).result() ).isEqualTo( 1 );
	}

	private enum Letters {
		A, B, C, D, E
	}

	/**
	 * Smaller ordinals are less
	 */
	@Test
	public void enumComparison() {
		assertThat( cmp.compare( Letters.B, Letters.B ).result() ).isEqualTo( 0 );
		assertThat( cmp.compare( Letters.A, Letters.B ).result() ).isEqualTo( -1 );
		assertThat( cmp.compare( Letters.B, Letters.A ).result() ).isEqualTo( 1 );
	}

	/**
	 * Ordinals closer to the minimum are less
	 */
	@Test
	public void enumMinimum() {

		assertThat( cmp.compare( Letters.B, Letters.E, Letters.C ).result() )
				.isEqualTo( -1 );
		assertThat( cmp.compare( Letters.E, Letters.B, Letters.C ).result() )
				.isEqualTo( 1 );
		assertThat( cmp.compare( Letters.B, Letters.B, Letters.C ).result() )
				.isEqualTo( 0 );

		// when the two values are equidistant, the lower ordinal is less
		assertThat( cmp.compare( Letters.B, Letters.D, Letters.C ).result() )
				.isEqualTo( -1 );
		assertThat( cmp.compare( Letters.D, Letters.B, Letters.C ).result() )
				.isEqualTo( 1 );
	}

	private class TestComparable implements Comparable<TestComparable> {
		private final int value;

		public TestComparable( int value ) {
			this.value = value;
		}

		@Override
		public int compareTo( TestComparable o ) {
			return cmp.compare( value, o.value ).result();
		}
	}

	/**
	 * Shows that it's safe to use one instance for nested comparisons
	 */
	@Test
	public void comparable() {
		TestComparable a = new TestComparable( 0 );
		TestComparable b = new TestComparable( 1 );
		assertEquals( 0, cmp.compare( a, a ).result() );
		assertEquals( -1, cmp.compare( a, b ).result() );
		assertEquals( 1, cmp.compare( b, a ).result() );
	}
}
