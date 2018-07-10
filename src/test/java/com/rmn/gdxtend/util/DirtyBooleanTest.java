package com.rmn.gdxtend.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * Tests for {@link DirtyBoolean}
 */
public class DirtyBooleanTest {

	/**
	 * Flags are initially false and dirty
	 */
	@Test
	public void initial() {
		assertThat( new DirtyBoolean().dirty() ).isTrue();
		assertThat( new DirtyBoolean().get() ).isFalse();
	}

	/**
	 * Flag value returned correctly
	 */
	@Test
	public void value() {
		DirtyBoolean db = new DirtyBoolean();

		db.set( true );
		assertThat( db.get() ).isTrue();
		db.set( false );
		assertThat( db.get() ).isFalse();
	}

	/**
	 * Flag value changes are detected
	 */
	@Test
	public void dirty() {
		DirtyBoolean db = new DirtyBoolean().clear();

		db.set( true );

		assertThat( db.dirty() ).isTrue();
		assertThat( db.dirty() ).isFalse();

		db.set( false );

		assertThat( db.dirty() ).isTrue();
		assertThat( db.dirty() ).isFalse();
	}

	/**
	 * Dirty status can be cleared
	 */
	@Test
	public void clear() {
		assertThat( new DirtyBoolean().clear().dirty() ).isFalse();
	}

	/**
	 * Dirty status can be forced
	 */
	@Test
	public void force() {
		assertThat( new DirtyBoolean().clear().force().dirty() ).isTrue();
	}
}
