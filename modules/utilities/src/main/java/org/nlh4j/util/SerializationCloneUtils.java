/*
 * @(#)SerializationCloneUtils.java 1.0 Jun 1, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Clones a object using serialization
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
@SuppressWarnings("unchecked")
public final class SerializationCloneUtils implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

	/**
	 * Clones an object
	 *
	 * @param <T>
	 * 		The object class type
	 * @param x
	 * 		The object to clone
	 *
	 * @return
	 * 		The cloned object
	 *
	 * @throws IllegalArgumentException thrown if could not serializing object or the class type of object is not defined
	 */
	public static <T> T clone(T x) {
		try {
			return cloneX(x);
		}
		catch (IOException e) {
		    e.printStackTrace();
		    throw new IllegalArgumentException(e.getMessage(), e);
	    }
		catch (ClassNotFoundException e) {
		    e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage(), e);
        }
	}

	/**
	 * Clones an object
	 *
	 * @param <T>
	 * 		The object class type
	 * @param x
	 * 		The object to clone
	 *
	 * @return
	 * 		The cloned object
	 *
	 * @throws IOException thrown if could not serializing object
	 * @throws ClassNotFoundException thrown if the class type of object is not defined
	 */
	private static <T> T cloneX(T x) throws IOException, ClassNotFoundException {
		// writes object
		ByteArrayOutputStream bout = null;
		CloneOutput cout = null;
		ByteArrayInputStream bin = null;
        CloneInput cin = null;
        T clone = null;
        IOException e1 = null;
        ClassNotFoundException e2 = null;
		boolean valid = false;
		byte[] bytes = null;
		try {
			// creates output streams
			bout = new ByteArrayOutputStream();
			cout = new CloneOutput(bout);
			// writes object
			cout.writeObject(x);
			bout.flush();
			cout.flush();
			// gets binaries array of object
			bytes = bout.toByteArray();
			valid = true;
		}
		catch (IOException ioe) {
		    e1 = ioe;
		    valid = false;
		}

		// if valid
		if (valid) {
    		// reads object
    		try {
    			// creates input streams
    			bin = new ByteArrayInputStream(bytes);
    			cin = new CloneInput(bin, cout);

    			// reads object
    			clone = (T) cin.readObject();
    			valid = true;
    		}
    		catch (IOException ioe) {
    		    e1 = ioe;
    		    valid = false;
    		}
    		catch (ClassNotFoundException cnfe) {
    		    e2 = cnfe;
    		    valid = false;
    		}
		}

		// release
		StreamUtils.closeQuitely(bout);
		StreamUtils.closeQuitely(bin);
		StreamUtils.closeQuitely(cout);
		StreamUtils.closeQuitely(cin);

		// thrown exception if failed
		if (!valid) {
		    if (e1 != null) throw e1;
		    if (e2 != null) throw e2;
		}

		// returns cloned object
		return clone;
	}

	/**
	 * The stream to serialize an object
	 */
	private static class CloneOutput extends ObjectOutputStream {

		/**
		 * The class queue to serialize
		 */
		private Queue<Class<?>> classQueue = new LinkedList<Class<?>>();

		/**
		 * Initializes a new instance of the {@link CloneOutput} class.
		 *
		 * @param out
		 * 			The real output stream to write object
		 *
		 * @throws IOException
		 * 			Throwed if could not write object
		 */
		CloneOutput(OutputStream out) throws IOException {
			super(out);
		}

		/**
		 * (non-Javadoc)
		 * @see java.io.ObjectOutputStream#annotateClass(java.lang.Class)
		 */
		@Override
		protected void annotateClass(Class<?> c) {
			classQueue.add(c);
		}

		/**
		 * (non-Javadoc)
		 * @see java.io.ObjectOutputStream#annotateProxyClass(java.lang.Class)
		 */
		@Override
		protected void annotateProxyClass(Class<?> c) {
			classQueue.add(c);
		}
	}

	/**
	 * The stream to de-serialize an object
	 */
	private static class CloneInput extends ObjectInputStream {

		/**
		 * The
		 */
		private final CloneOutput output;

		/**
		 * Initializes a new instance of the {@link CloneInput} class.
		 *
		 * @param in
		 * 			The input stream to deserialize
		 * @param output
		 * 			The output stream that contains object
		 *
		 * @throws IOException
		 * 			Throwed if could not de-serialize object
		 */
		CloneInput(InputStream in, CloneOutput output) throws IOException {
			super(in);
			this.output = output;
		}

		/**
		 * (non-Javadoc)
		 * @see java.io.ObjectInputStream#resolveClass(java.io.ObjectStreamClass)
		 */
		@Override
		protected Class<?> resolveClass(ObjectStreamClass osc) throws IOException, ClassNotFoundException {
			Class<?> c = output.classQueue.poll();
			String expected = osc.getName();
			String found = (c == null) ? null : c.getName();
			if (!expected.equals(found))
				throw new InvalidClassException("Classes desynchronized: "
						+ "found " + found + " when expecting " + expected);
			return c;
		}

		/**
		 * (non-Javadoc)
		 * @see java.io.ObjectInputStream#resolveProxyClass(java.lang.String[])
		 */
		@Override
		protected Class<?> resolveProxyClass(String[] interfaceNames) throws IOException, ClassNotFoundException {
			return output.classQueue.poll();
		}
	}
}
