package com.rmn.gdxtend.expect;

import static org.assertj.core.api.Assertions.fail;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.nio.file.Files;

import org.assertj.core.api.Fail;
import org.assertj.core.api.SoftAssertions;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * A {@link Rule} to allow convenient assertion of expected results held in
 * files
 *
 * @param <T>
 *          self type
 */
public abstract class AbstractExpect<T extends AbstractExpect<T>> extends
		TestWatcher {

	/**
	 * Run the test with this property set to some value to regenerate results
	 */
	private static final String REGEN_PROPERTY = "regenerate";

	private static enum Dir {
		/**
		 * Test expectations
		 */
		EXPECT( "src/test/resources/expect" ),
		/**
		 * Actual results
		 */
		ACTUAL( "src/test/resources/actual" );

		public final String path;

		private Dir( String path ) {
			this.path = path;
		}
	};

	private final String suffix;
	private String testClass, testMethod;
	private boolean regenerate = false;

	private String name = "";

	private SoftAssertions softly = new SoftAssertions();

	protected AbstractExpect() {
		this( ".expect" );
	}

	protected AbstractExpect( String suffix ) {
		this.suffix = suffix;
	}

	protected T reset() {
		name = "";
		return self();
	}

	@Override
	protected void starting( Description description ) {
		testClass = description.getClassName();
		testMethod = description.getMethodName();
		regenerate = false;

		if( getTestDir( Dir.ACTUAL ).exists() ) {
			// clear out any actual results for this test that may be hanging around
			// from the last run
			for( File f : getTestDir( Dir.ACTUAL ).listFiles( new FilenameFilter() {
				@Override
				public boolean accept( File dir, String name ) {
					return name.startsWith( testMethod );
				}
			} ) ) {
				f.delete();
			}
		}

		try {
			Class<?> c = Class.forName( description.getClassName() );

			// this'll go to bollocks if test methods ever accept argument.
			// we'll just ignore that for now.
			Method m = c.getMethod( description.getMethodName() );

			regenerate = m.isAnnotationPresent( Regenerate.class )
					|| "true".equals( System.getProperty( REGEN_PROPERTY ) )
					|| c.isAnnotationPresent( Regenerate.class )
					|| c.getPackage().isAnnotationPresent( Regenerate.class );
		}
		catch( Exception e ) {
			e.printStackTrace();
		}
	}

	@Override
	protected void finished( Description description ) {

		// clean up empty dirs in the actual tree
		deleteEmptyDirs( new File( Dir.ACTUAL.path ) );

		if( regenerate ) {
			// don't let tests pass if they're just regenerating the results
			Fail.fail( "Result regenerated" );
		}

		softly.assertAll();
	}

	/**
	 * @param name
	 *          An optional name for the result. Useful when one test checks more
	 *          than one result.
	 * @return this
	 */
	public T named( String name ) {
		this.name = "_" + name;
		return self();
	}

	/**
	 * Compares an actual result against an expected result in a file. If the
	 * expect file does not exist or if the regenerate property is set or the
	 * {@link Regenerate} annotation is present, then the expected result file
	 * will be overwritten with the actual result
	 *
	 * @param actual
	 *          the result
	 */
	public void check( String actual ) {

		File expectFile = getFile( Dir.EXPECT );

		if( regenerate ) {
			// write the file
			expectFile.getParentFile().mkdirs();
			try {
				Files.write( expectFile.toPath(), actual.getBytes() );
			}
			catch( IOException ioe ) {
				fail( "Could not write " + expectFile, ioe );
			}
		}
		else {
			// compare the file
			try {
				String expected =
						new String( Files.readAllBytes( expectFile.toPath() ) );

				if( !expected.equals( actual ) ) {
					// an expectation has been broken - save the actual result for manual
					// analysis
					getFile( Dir.ACTUAL ).getParentFile().mkdirs();
					Files.write( getFile( Dir.ACTUAL ).toPath(), actual.getBytes() );

					// signal failure. SoftAssertions doesn't have a fail method
					softly.assertThat(
							"diff " + expectFile + " " + getFile( Dir.ACTUAL ) )
							.isEqualTo( "Files matched" );

					if( !expected.contains( "\n" ) && !actual.contains( "\n" ) ) {
						// if there are no newlines then there's a chance that the diff is
						// useful in the failure report
						softly.assertThat( actual ).isEqualTo( expected );
					}
				}
			}
			catch( IOException ioe ) {
				fail( "error checking result", ioe );
			}
		}

		reset();
	}

	private File getTestDir( Dir type ) {
		StringBuilder path = new StringBuilder( type.path );

		assert testClass != null : "You've failed to @Rule the Expect object";
		for( String s : testClass.split( "\\." ) ) {
			path.append( "/" ).append( s );
		}

		return new File( path.toString() );
	}

	private File getFile( Dir type ) {
		return new File( getTestDir( type ), resultName() + suffix );
	}

	/**
	 * @return the name of the current result
	 */
	protected String resultName() {
		return testMethod + name;
	}

	@SuppressWarnings( "unchecked" )
	protected T self() {
		return (T) this;
	}

	private static boolean deleteEmptyDirs( File f ) {
		if( f.isDirectory() ) {
			boolean success = true;
			for( File g : f.listFiles() ) {
				success &= deleteEmptyDirs( g );
			}
			return success && f.delete();
		}
		else {
			return false;
		}
	}

	/**
	 * Apply this to cause results to be regenerated.
	 */
	@Retention( RetentionPolicy.RUNTIME )
	@Target( { ElementType.PACKAGE, ElementType.TYPE, ElementType.METHOD } )
	public static @interface Regenerate {
	}
}
