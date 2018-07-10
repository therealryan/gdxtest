package com.rmn.gdxtend.gl.facets;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Assert;

import com.rmn.gdxtend.GdxTest;

abstract class FacetTest extends GdxTest {

	/**
	 * Asserts the facets are order correctly, more-default state to before
	 * less-default
	 * 
	 * @param control
	 *          the more-default state
	 * @param altered
	 *          the less-default state
	 */
	protected <T extends Facet<T>> void comparisonOrder( T control, T altered ) {
		Assert.assertEquals( -1, control.compareTo( altered ) );
		Assert.assertEquals( 1, altered.compareTo( control ) );

		assertThat( control.compareTo( altered ) ).isEqualTo( -1 );
		assertThat( altered.compareTo( control ) ).isEqualTo( 1 );
	}
}
