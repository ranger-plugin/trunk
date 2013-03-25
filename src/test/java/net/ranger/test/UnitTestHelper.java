package net.ranger.test;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;

/**
 * 
 * Helper class for unit tests.
 * 
 * @author Emerson Loureiro
 * 
 */
public abstract class UnitTestHelper extends TestCase {

	private JUnit4Mockery mockery;

	private Map<Class<?>, Integer> mockNamingTable;

	public UnitTestHelper() {
		this.mockNamingTable = new HashMap<Class<?>, Integer>();
	}

	@Override
	public final void setUp() {
		this.mockery = new JUnit4Mockery();
		this.setUpImpl();
	}

	@Override
	public final void tearDown() {
		this.getMockery().assertIsSatisfied();
	}

	/**
	 * Returns the mockery for creating mock tests for this test case.
	 * 
	 * @return {@link Mockery}
	 */
	public Mockery getMockery() {
		return this.mockery;
	}

	/**
	 * Creates and returns a mock instance of the provided class - has to be an
	 * interface.
	 * 
	 * @param <T>
	 *            Type of the interface being mocked.
	 * @param classToBeMocked
	 *            Class of the interface of be mocked.
	 * @return An object of type <T>, being a mock of the interface given.
	 */
	public <T> T mock(Class<T> classToBeMocked) {
		Integer count = this.mockNamingTable.get(classToBeMocked);

		if (count == null) {
			count = 1;
		} else {
			count = count + 1;
		}

		this.mockNamingTable.put(classToBeMocked, count);

		return this.getMockery().mock(classToBeMocked, classToBeMocked.toString() + "_" + count);
	}

	/**
	 * Subclasses-specific implementation of the start up method. Override to
	 * define each test specific initialization steps.
	 */
	public void setUpImpl() {
	}

	/**
	 * Subclasses-specific implementation of the tear down method. Override to
	 * define each test specific final steps for the test.
	 */
	public void tearDownImpl() {
	}
}
