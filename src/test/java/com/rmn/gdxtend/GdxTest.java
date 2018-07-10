package com.rmn.gdxtend;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

/**
 * Allows test that require opengl
 */
@RunWith( GdxTestRunner.class )
public abstract class GdxTest {

	/**
	 * Mock gl context
	 */
	@Mock
	public GL20 gl;

	/**
	 * Initialises the mock and injects it into {@link Gdx}
	 */
	@Before
	public void before() {

		MockitoAnnotations.initMocks( this );
		Gdx.gl20 = gl;
		Gdx.gl = gl;
	}
}
