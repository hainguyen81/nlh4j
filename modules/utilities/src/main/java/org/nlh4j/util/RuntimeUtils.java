/*
 * @(#)RuntimeUtils.java
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.springframework.util.Assert;

/**
 * {@link Runtime} and {@link RuntimeMXBean} Utilities
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class RuntimeUtils implements Serializable {

    /** */
    private static final long serialVersionUID = 1L;
    private static transient RuntimeMXBean runtimeMx;
    private static transient Runtime runtime;

    /**
     * Get {@link RuntimeMXBean} for parsing information
     * @return {@link RuntimeMXBean} or null
     */
    private static RuntimeMXBean getRuntimeMx() {
        if (runtimeMx == null) {
            runtimeMx = ManagementFactory.getRuntimeMXBean();
        }
        if (runtimeMx == null) {
            return null;
        }
        synchronized (runtimeMx) {
            return runtimeMx;
        }
    }
    /**
     * Get {@link Runtime} for parsing information
     * @return {@link Runtime} or null
     */
    private static Runtime getRuntime() {
        if (runtime == null) {
            runtime = Runtime.getRuntime();
        }
        if (runtime == null) {
            return null;
        }
        synchronized (runtime) {
            return runtime;
        }
    }

    /**
     * Returns the name representing the running Java virtual machine.
     * The returned name string can be any arbitrary string and
     * a Java virtual machine implementation can choose
     * to embed platform-specific useful information in the
     * returned name string.  Each running virtual machine could have
     * a different name.
     *
     * @return the name representing the running Java virtual machine.
     */
    public static String getName() {
        RuntimeMXBean intRuntime = getRuntimeMx();
        return (intRuntime == null ? null : intRuntime.getName());
    }

    /**
     * Returns the Java virtual machine implementation name.
     * This method is equivalent to {@link System#getProperty
     * System.getProperty("java.vm.name")}.
     *
     * @return the Java virtual machine implementation name.
     */
    public static String getVmName() {
        RuntimeMXBean intRuntime = getRuntimeMx();
        return (intRuntime == null ? null : intRuntime.getVmName());
    }

    /**
     * Returns the Java virtual machine implementation vendor.
     * This method is equivalent to {@link System#getProperty
     * System.getProperty("java.vm.vendor")}.
     *
     * @return the Java virtual machine implementation vendor.
     */
    public static String getVmVendor() {
        RuntimeMXBean intRuntime = getRuntimeMx();
        return (intRuntime == null ? null : intRuntime.getVmVendor());
    }

    /**
     * Returns the Java virtual machine implementation version.
     * This method is equivalent to {@link System#getProperty
     * System.getProperty("java.vm.version")}.
     *
     * @return the Java virtual machine implementation version.
     */
    public static String getVmVersion() {
        RuntimeMXBean intRuntime = getRuntimeMx();
        return (intRuntime == null ? null : intRuntime.getVmVersion());
    }

    /**
     * Returns the Java virtual machine specification name.
     * This method is equivalent to {@link System#getProperty
     * System.getProperty("java.vm.specification.name")}.
     *
     * @return the Java virtual machine specification name.
     */
    public static String getSpecName() {
        RuntimeMXBean intRuntime = getRuntimeMx();
        return (intRuntime == null ? null : intRuntime.getSpecName());
    }

    /**
     * Returns the Java virtual machine specification vendor.
     * This method is equivalent to {@link System#getProperty
     * System.getProperty("java.vm.specification.vendor")}.
     *
     * @return the Java virtual machine specification vendor.
     */
    public static String getSpecVendor() {
        RuntimeMXBean intRuntime = getRuntimeMx();
        return (intRuntime == null ? null : intRuntime.getSpecVendor());
    }

    /**
     * Returns the Java virtual machine specification version.
     * This method is equivalent to {@link System#getProperty
     * System.getProperty("java.vm.specification.version")}.
     *
     * @return the Java virtual machine specification version.
     */
    public static String getSpecVersion() {
        RuntimeMXBean intRuntime = getRuntimeMx();
        return (intRuntime == null ? null : intRuntime.getSpecVersion());
    }

    /**
     * Returns the version of the specification for the management interface
     * implemented by the running Java virtual machine.
     *
     * @return the version of the specification for the management interface
     * implemented by the running Java virtual machine.
     */
    public static String getManagementSpecVersion() {
        RuntimeMXBean intRuntime = getRuntimeMx();
        return (intRuntime == null ? null : intRuntime.getManagementSpecVersion());
    }

    /**
     * Returns the Java class path that is used by the system class loader
     * to search for class files.
     * This method is equivalent to {@link System#getProperty
     * System.getProperty("java.class.path")}.
     *
     * <p> Multiple paths in the Java class path are separated by the
     * path separator character of the platform of the Java virtual machine
     * being monitored.
     *
     * @return the Java class path.
     */
    public static String getClassPath() {
        RuntimeMXBean intRuntime = getRuntimeMx();
        return (intRuntime == null ? null : intRuntime.getClassPath());
    }

    /**
     * Returns the Java library path.
     * This method is equivalent to {@link System#getProperty
     * System.getProperty("java.library.path")}.
     *
     * <p> Multiple paths in the Java library path are separated by the
     * path separator character of the platform of the Java virtual machine
     * being monitored.
     *
     * @return the Java library path.
     */
    public static String getLibraryPath() {
        RuntimeMXBean intRuntime = getRuntimeMx();
        return (intRuntime == null ? null : intRuntime.getLibraryPath());
    }

    /**
     * Tests if the Java virtual machine supports the boot class path
     * mechanism used by the bootstrap class loader to search for class
     * files.
     *
     * @return <b>true</b> if the Java virtual machine supports the
     * class path mechanism; <b>false</b> otherwise.
     */
    public static boolean isBootClassPathSupported() {
        RuntimeMXBean intRuntime = getRuntimeMx();
        return (intRuntime == null ? Boolean.FALSE : intRuntime.isBootClassPathSupported());
    }

    /**
     * Returns the boot class path that is used by the bootstrap class loader
     * to search for class files.
     *
     * <p> Multiple paths in the boot class path are separated by the
     * path separator character of the platform on which the Java
     * virtual machine is running.
     *
     * <p>A Java virtual machine implementation may not support
     * the boot class path mechanism for the bootstrap class loader
     * to search for class files.
     * The {@link #isBootClassPathSupported} method can be used
     * to determine if the Java virtual machine supports this method.
     *
     * @return the boot class path.
     */
    public static String getBootClassPath() {
        RuntimeMXBean intRuntime = getRuntimeMx();
        return (intRuntime == null ? null : intRuntime.getBootClassPath());
    }

    /**
     * Returns the input arguments passed to the Java virtual machine
     * which does not include the arguments to the <b>main</b> method.
     * This method returns an empty list if there is no input argument
     * to the Java virtual machine.
     * <p>
     * Some Java virtual machine implementations may take input arguments
     * from multiple different sources: for examples, arguments passed from
     * the application that launches the Java virtual machine such as
     * the 'java' command, environment variables, configuration files, etc.
     * <p>
     * Typically, not all command-line options to the 'java' command
     * are passed to the Java virtual machine.
     * Thus, the returned input arguments may not
     * include all command-line options.
     *
     * <p>
     * <b>MBeanServer access</b>:<br>
     * The mapped type of <b>List&lt;String&gt;</b> is <b>String[]</b>.
     *
     * @return a list of <b>String</b> objects; each element
     * is an argument passed to the Java virtual machine.
     */
    public static List<String> getInputArguments() {
        RuntimeMXBean intRuntime = getRuntimeMx();
        return (intRuntime == null ? null : intRuntime.getInputArguments());
    }

    /**
     * Returns the uptime of the Java virtual machine in milliseconds.
     *
     * @return uptime of the Java virtual machine in milliseconds.
     */
    public static long getUptime() {
        RuntimeMXBean intRuntime = getRuntimeMx();
        return (intRuntime == null ? 0 : intRuntime.getUptime());
    }

    /**
     * Returns the start time of the Java virtual machine in milliseconds.
     * This method returns the approximate time when the Java virtual
     * machine started.
     *
     * @return start time of the Java virtual machine in milliseconds.
     *
     */
    public long getStartTime() {
        RuntimeMXBean intRuntime = getRuntimeMx();
        return (intRuntime == null ? 0 : intRuntime.getStartTime());
    }

    /**
     * Returns a map of names and values of all system properties.
     * This method calls {@link System#getProperties} to get all
     * system properties.  Properties whose name or value is not
     * a <b>String</b> are omitted.
     *
     * <p>
     * <b>MBeanServer access</b>:<br>
     * The mapped type of <b>Map&lt;String,String&gt;</b> is
     * {@link javax.management.openmbean.TabularData TabularData}
     * with two items in each row as follows:
     * <blockquote>
     * <table>
     * <caption>System Properties</caption>
     * <tr>
     *   <th>Item Name</th>
     *   <th>Item Type</th>
     *   </tr>
     * <tr>
     *   <td><b>key</b></td>
     *   <td><b>String</b></td>
     *   </tr>
     * <tr>
     *   <td><b>value</b></td>
     *   <td><b>String</b></td>
     *   </tr>
     * </table>
     * </blockquote>
     *
     * @return a map of names and values of all system properties.
     */
    public Map<String, String> getSystemProperties() {
        RuntimeMXBean intRuntime = getRuntimeMx();
        return (intRuntime == null ? null : intRuntime.getSystemProperties());
    }

    /**
     * Returns the number of processors available to the Java virtual machine.
     *
     * <p> This value may change during a particular invocation of the virtual
     * machine.  Applications that are sensitive to the number of available
     * processors should therefore occasionally poll this property and adjust
     * their resource usage appropriately. </p>
     *
     * @return  the maximum number of processors available to the virtual
     *          machine; never smaller than one; 0 if fail
     * @since 1.4
     */
    public static int getAvailableProcessors() {
        Runtime intRuntime = getRuntime();
        return (intRuntime == null ? 0 : intRuntime.availableProcessors());
    }

    /**
     * Returns the amount of free memory in the Java Virtual Machine.
     * Calling the
     * <code>gc</code> method may result in increasing the value returned
     * by <code>freeMemory.</code>
     *
     * @return  an approximation to the total amount of memory currently
     *          available for future allocated objects, measured in bytes.
     *          0 if fail
     */
    public static long getFreeMemory() {
        Runtime intRuntime = getRuntime();
        return (intRuntime == null ? 0 : intRuntime.freeMemory());
    }

    /**
     * Returns the total amount of memory in the Java virtual machine.
     * The value returned by this method may vary over time, depending on
     * the host environment.
     * <p>
     * Note that the amount of memory required to hold an object of any
     * given type may be implementation-dependent.
     *
     * @return  the total amount of memory currently available for current
     *          and future objects, measured in bytes. 0 if fail
     */
    public static long getTotalMemory() {
        Runtime intRuntime = getRuntime();
        return (intRuntime == null ? 0 : intRuntime.totalMemory());
    }

    /**
     * Returns the maximum amount of memory that the Java virtual machine will
     * attempt to use.  If there is no inherent limit then the value {@link
     * java.lang.Long#MAX_VALUE} will be returned.
     *
     * @return  the maximum amount of memory that the virtual machine will
     *          attempt to use, measured in bytes. 0 if fail
     * @since 1.4
     */
    public static long getMaxMemory() {
        Runtime intRuntime = getRuntime();
        return (intRuntime == null ? 0 : intRuntime.maxMemory());
    }

    /**
     * Loads the specified filename as a dynamic library. The filename
     * argument must be a complete path name,
     * (for example
     * <code>Runtime.getRuntime().load("/home/avh/lib/libX11.so");</code>).
     * <p>
     * First, if there is a security manager, its <code>checkLink</code>
     * method is called with the <code>filename</code> as its argument.
     * This may result in a security exception.
     * <p>
     * This is similar to the method {@link #loadLibrary(String)}, but it
     * accepts a general file name as an argument rather than just a library
     * name, allowing any file of native code to be loaded.
     * <p>
     * The method {@link System#load(String)} is the conventional and
     * convenient means of invoking this method.
     *
     * @param      filename   the file to load.
     */
    public void load(String filename) {
        Runtime intRuntime = getRuntime();
        if (intRuntime != null) intRuntime.load(filename);
    }

    /**
     * Loads the dynamic library with the specified library name.
     * A file containing native code is loaded from the local file system
     * from a place where library files are conventionally obtained. The
     * details of this process are implementation-dependent. The
     * mapping from a library name to a specific filename is done in a
     * system-specific manner.
     * <p>
     * First, if there is a security manager, its <code>checkLink</code>
     * method is called with the <code>libname</code> as its argument.
     * This may result in a security exception.
     * <p>
     * The method {@link System#loadLibrary(String)} is the conventional
     * and convenient means of invoking this method. If native
     * methods are to be used in the implementation of a class, a standard
     * strategy is to put the native code in a library file (call it
     * <code>LibFile</code>) and then to put a static initializer:
     * <blockquote><pre>
     * static { System.loadLibrary("LibFile"); }
     * </pre></blockquote>
     * within the class declaration. When the class is loaded and
     * initialized, the necessary native code implementation for the native
     * methods will then be loaded as well.
     * <p>
     * If this method is called more than once with the same library
     * name, the second and subsequent calls are ignored.
     *
     * @param      libname   the name of the library.
     */
    public void loadLibrary(String libname) {
        Runtime intRuntime = getRuntime();
        if (intRuntime != null) intRuntime.loadLibrary(libname);
    }

    /**
     * Executes the specified string command in a separate process.
     *
     * <p>This is a convenience method.  An invocation of the form
     * <b>exec(command)</b>
     * behaves in exactly the same way as the invocation
     * <b>{@link #exec(String, String[], File, StringBuilder, StringBuilder)}</b>.
     *
     * @param   command   a specified system command.
     *
     * @param   output    the output of command line or null if fail
     *
     * @param   error     the error output of command line or null if fail
     */
    public static void exec(String command, StringBuilder output, StringBuilder error) {
        exec(command, null, null, output, error);
    }

    /**
     * Executes the specified string command in a separate process with the
     * specified environment.
     *
     * <p>This is a convenience method.  An invocation of the form
     * <b>exec(command, envp)</b>
     * behaves in exactly the same way as the invocation
     * <b>{@link #exec(String, String[], File, StringBuilder, StringBuilder)}</b>.
     *
     * @param   command   a specified system command.
     *
     * @param   envp      array of strings, each element of which
     *                    has environment variable settings in the format
     *                    <i>name</i>=<i>value</i>, or
     *                    <b>null</b> if the subprocess should inherit
     *                    the environment of the current process.
     *
     * @param   output    the output of command line or null if fail
     *
     * @param   error     the error output of command line or null if fail
     */
    public static void exec(
            String command, String[] envp,
            StringBuilder output, StringBuilder error) {
        exec(command, envp, null, output, error);
    }

    /**
     * Executes the specified string command in a separate process with the
     * specified environment and working directory.
     *
     * <p>This is a convenience method.  An invocation of the form
     * <b>exec(command, envp, dir)</b>
     * behaves in exactly the same way as the invocation
     * <b>{@link #exec(String[], String[], File, StringBuilder, StringBuilder)}</b>,
     * where <code>cmdarray</code> is an array of all the tokens in
     * <code>command</code>.
     *
     * <p>More precisely, the <code>command</code> string is broken
     * into tokens using a {@link StringTokenizer} created by the call
     * <code>new {@link StringTokenizer}(command)</code> with no
     * further modification of the character categories.  The tokens
     * produced by the tokenizer are then placed in the new string
     * array <code>cmdarray</code>, in the same order.
     *
     * @param   command   a specified system command.
     *
     * @param   envp      array of strings, each element of which
     *                    has environment variable settings in the format
     *                    <i>name</i>=<i>value</i>, or
     *                    <b>null</b> if the subprocess should inherit
     *                    the environment of the current process.
     *
     * @param   dir       the working directory of the subprocess, or
     *                    <b>null</b> if the subprocess should inherit
     *                    the working directory of the current process.
     *
     * @param   output    the output of command line or null if fail
     *
     * @param   error     the error output of command line or null if fail
     */
    public static void exec(
            String command, String[] envp, File dir,
            StringBuilder output, StringBuilder error) {
        Assert.hasLength(command, "Empty command");
        StringTokenizer st = new StringTokenizer(command);
        String[] cmdarray = new String[st.countTokens()];
        for (int i = 0; st.hasMoreTokens(); i++)
            cmdarray[i] = st.nextToken();
        exec(cmdarray, envp, dir, output, error);
    }

    /**
     * Executes the specified command and arguments in a separate process.
     *
     * <p>This is a convenience method.  An invocation of the form
     * <b>exec(cmdarray)</b>
     * behaves in exactly the same way as the invocation
     * <b>{@link #exec(String[], String[], File, StringBuilder, StringBuilder)}</b>.
     *
     * @param   cmdarray  array containing the command to call and
     *                    its arguments.
     *
     * @param   output    the output of command line or null if fail
     *
     * @param   error     the error output of command line or null if fail
     */
    public static void exec(String[] cmdarray, StringBuilder output, StringBuilder error) {
        exec(cmdarray, null, null, output, error);
    }

    /**
     * Executes the specified command and arguments in a separate process
     * with the specified environment.
     *
     * <p>This is a convenience method.  An invocation of the form
     * <b>exec(cmdarray, envp)</b>
     * behaves in exactly the same way as the invocation
     * <b>{@link #exec(String[], String[], File, StringBuilder, StringBuilder)}</b>.
     *
     * @param   cmdarray  array containing the command to call and
     *                    its arguments.
     *
     * @param   envp      array of strings, each element of which
     *                    has environment variable settings in the format
     *                    <i>name</i>=<i>value</i>, or
     *                    <b>null</b> if the subprocess should inherit
     *                    the environment of the current process.
     *
     * @param   output    the output of command line or null if fail
     *
     * @param   error     the error output of command line or null if fail
     */
    public static void exec(
            String[] cmdarray, String[] envp,
            StringBuilder output, StringBuilder error) {
        exec(cmdarray, envp, null, output, error);
    }

    /**
     * Executes the specified command and arguments in a separate process with
     * the specified environment and working directory.
     *
     * <p>Given an array of strings <code>cmdarray</code>, representing the
     * tokens of a command line, and an array of strings <code>envp</code>,
     * representing "environment" variable settings, this method creates
     * a new process in which to execute the specified command.
     *
     * <p>This method checks that <code>cmdarray</code> is a valid operating
     * system command.  Which commands are valid is system-dependent,
     * but at the very least the command must be a non-empty list of
     * non-null strings.
     *
     * <p>If <b>envp</b> is <b>null</b>, the subprocess inherits the
     * environment settings of the current process.
     *
     * <p>A minimal set of system dependent environment variables may
     * be required to start a process on some operating systems.
     * As a result, the subprocess may inherit additional environment variable
     * settings beyond those in the specified environment.
     *
     * <p>{@link ProcessBuilder#start()} is now the preferred way to
     * start a process with a modified environment.
     *
     * <p>The working directory of the new subprocess is specified by <b>dir</b>.
     * If <b>dir</b> is <b>null</b>, the subprocess inherits the
     * current working directory of the current process.
     *
     * <p>If a security manager exists, its
     * {@link SecurityManager#checkExec checkExec}
     * method is invoked with the first component of the array
     * <code>cmdarray</code> as its argument. This may result in a
     * {@link SecurityException} being thrown.
     *
     * <p>Starting an operating system process is highly system-dependent.
     * Among the many things that can go wrong are:
     * <ul>
     * <li>The operating system program file was not found.
     * <li>Access to the program file was denied.
     * <li>The working directory does not exist.
     * </ul>
     *
     * <p>In such cases an exception will be thrown.  The exact nature
     * of the exception is system-dependent, but it will always be a
     * subclass of {@link IOException}.
     *
     *
     * @param   cmdarray  array containing the command to call and
     *                    its arguments.
     *
     * @param   envp      array of strings, each element of which
     *                    has environment variable settings in the format
     *                    <i>name</i>=<i>value</i>, or
     *                    <b>null</b> if the subprocess should inherit
     *                    the environment of the current process.
     *
     * @param   dir       the working directory of the subprocess, or
     *                    <b>null</b> if the subprocess should inherit
     *                    the working directory of the current process.
     *
     * @param   output    the output of command line or null if fail
     *
     * @param   error     the error output of command line or null if fail
     */
    public static void exec(
            String[] cmdarray, String[] envp, File dir,
            StringBuilder output, StringBuilder error) {
        Runtime intRuntime = getRuntime();
        Process proc = null;
        InputStream outIs = null;
        InputStreamReader outIsr = null;
        BufferedReader outBr = null;
        InputStream errIs = null;
        InputStreamReader errIsr = null;
        BufferedReader errBr = null;
        try {
            // execute command
            proc = intRuntime.exec(cmdarray, envp, dir);
            // parse output/error stream
            outIs = proc.getInputStream();
            errIs = proc.getErrorStream();
            outIsr = new InputStreamReader(outIs);
            errIsr = new InputStreamReader(errIs);
            outBr = new BufferedReader(outIsr);
            errBr = new BufferedReader(errIsr);
            // read output
            String line = null;
            output = (output == null ? new StringBuilder() : output);
            while((line = outBr.readLine()) != null) {
                if (output.length() > 0) {
                    output.append(System.lineSeparator());
                }
                output.append(line);
            }
            // read error if necessary
            line = null;
            error = (error == null ? new StringBuilder() : error);
            while((line = errBr.readLine()) != null) {
                if (error.length() > 0) {
                    error.append(System.lineSeparator());
                }
                error.append(line);
            }
            // wait process for finishing response
            proc.waitFor();
        } catch(Exception e) {
            LogUtils.logError(RuntimeUtils.class, e.getMessage(), e);
            if (proc != null) {
                proc.destroy();
            }
            output = null;
            error = null;
        } finally {
            StreamUtils.closeQuitely(outIs);
            StreamUtils.closeQuitely(outIsr);
            StreamUtils.closeQuitely(outBr);
            StreamUtils.closeQuitely(errIs);
            StreamUtils.closeQuitely(errIsr);
            StreamUtils.closeQuitely(errBr);
        }
    }
}
