/*
 * @(#)MultiThreading.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.util;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

/**
 * Multi-threading support class
 *
 * @author Hai Nguyen
 *
 */
public abstract class MultiThreading extends Thread implements Serializable {

    /** */
    private static final long serialVersionUID = 1L;
    /** child tasks list */
    private final transient List<Thread> tasks = new LinkedList<Thread>();
    /** accessible tasks map */
    private final transient Map<Thread, Object> sharedData = new LinkedHashMap<Thread, Object>();

    /**
     * Initialize a new instance of {@link MultiThreading}
     */
    public MultiThreading() {}
    /**
     * Initialize a new instance of {@link MultiThreading}
     * @param tasks {@link Thread} list
     */
    public MultiThreading(Thread...tasks) {
        Assert.notEmpty(tasks, "tasks");
        this.getTasks().addAll(Arrays.asList(tasks));
    }

    /**
     * Get the child {@link Thread} tasks list
     * @return the child {@link Thread} tasks list
     */
    protected final synchronized List<Thread> getTasks() {
        return this.tasks;
    }
    /**
     * Get the shared {@link Thread} task data holder
     * @return the shared {@link Thread} task data holder
     */
    private final synchronized Map<Thread, Object> getSharedData() {
        return this.sharedData;
    }
    /**
     * Get the child {@link Thread} task by name
     * @param taskName to find
     * @return the child {@link Thread} task by name
     */
    protected final Thread getTask(String taskName) {
        Assert.hasText(taskName, "Task Name or Id!");
        List<Thread> tasksList = this.getTasks();
        for(final Thread child : tasksList) {
            if (taskName.equalsIgnoreCase(child.getName())
                    || taskName.equalsIgnoreCase(String.valueOf(child.getId()))) {
                return child;
            }
        }
        return null;
    }
    /**
     * Get the child {@link Thread} task by name
     * @param taskName to find
     * @return the child {@link Thread} task by name
     */
    protected final int getTaskIndex(String taskName) {
        Assert.hasText(taskName, "Task Name or Id!");
        List<Thread> tasksList = this.getTasks();
        int i = -1;
        for(final Thread child : tasksList) {
            i++;
            if (taskName.equalsIgnoreCase(child.getName())
                    || taskName.equalsIgnoreCase(String.valueOf(child.getId()))) {
                return i;
            }
        }
        return -1;
    }
    /**
     * Get the child {@link Thread} task by its instance
     * @param task to find
     * @return the child {@link Thread} task or null
     */
    protected final Thread getTask(Thread task) {
        Assert.notNull(task, "task");
        String taskName = (StringUtils.hasText(task.getName())
                ? task.getName() : String.valueOf(task.getId()));
        return this.getTask(taskName);
    }
    /**
     * Get the shared data between tasks
     * @param task accessed {@link Thread} task
     * @return the shared data between tasks
     */
    protected final Object getSharedData(Thread task) {
        Object data = (task != null && this.getSharedData().containsKey(task)
                ? this.getSharedData().get(task) : null);
        if (data == null) return data;
        synchronized (data) { return data; }
    }
    /**
     * Get the shared data between tasks
     * @param taskName accessed {@link Thread} task name
     * @return the shared data between tasks
     */
    protected final Object getSharedData(String taskName) {
        final Thread task = this.getTask(taskName);
        return (task == null ? null : this.getSharedData(task));
    }
    /**
     * Set shared data between tasks
     * @param task child accessed {@link Thread} task
     * @param data shared synchronized data
     * @return true for successfully; else false
     */
    public final boolean setSharedData(Thread task, Object data) {
        Assert.notNull(task, "invalid child task!");
        String taskName = (StringUtils.hasText(task.getName())
                ? task.getName() : String.valueOf(task.getId()));
        return this.setSharedData(taskName, data);
    }
    /**
     * Set shared data between tasks
     * @param taskName child accessed {@link Thread} task name
     * @param data shared synchronized data
     * @return true for successfully; else false
     */
    public final boolean setSharedData(String taskName, Object data) {
        Assert.hasText(taskName, "taskName");
        boolean ok = false;
        final Thread task = this.getTask(taskName);
        if (task != null) {
            this.getSharedData().put(task, data);
            if (!task.isInterrupted() && task.isAlive()) {
                try { this.performSharing(task); }
                catch (Exception e) {
                    LogUtils.logError(this.getClass(), e);
                }
            }
            ok = true;
        }
        return ok;
    }

    /**
     * Perform action after sharing data
     * TODO This method will be called right after sharing data
     * from {@link #setSharedData(Thread, Object)} or {@link #setSharedData(String, Object)} method(s)
     * if shared {@link Thread} has been alived
     * @param task shared {@link Thread} task
     */
    protected abstract void performSharing(Thread task);

    /**
     * Add a new child {@link Thread} task
     * @param task to add
     * @param started specify whether start the specified {@link Thread} task
     */
    public final void addTask(Thread task, Boolean started) {
        Assert.notNull(task, "task");
        this.getTasks().add(task);
        if (Boolean.TRUE.equals(started)) {
            try { task.start(); }
            catch (Exception e) {
                LogUtils.logWarn(this.getClass(), e.getMessage());
            }
        }
    }
    /**
     * Add a new child {@link Thread} task
     * @param task to add
     */
    public final void addTask(Thread task) {
        this.addTask(task, Boolean.FALSE);
    }

    /**
     * Remove the child {@link Thread} task, also removing shared data for this task
     * @param task to remove
     * @return the removed {@link Thread}
     */
    public final int removeTask(Thread task) {
        return this.removeTask(task, Boolean.TRUE);
    }
    /**
     * Remove the child {@link Thread} task, also removing shared data for this task
     * @param task to remove
     * @param interrupted specify interrupting this task if necessary
     * @return the removed {@link Thread}
     */
    public final int removeTask(Thread task, Boolean interrupted) {
        Assert.notNull(task, "task");
        String taskName = (StringUtils.hasText(task.getName())
                ? task.getName() : String.valueOf(task.getId()));
        final List<Thread> tasksList = this.getTasks();
        int size = tasksList.size();
        int idx = this.getTaskIndex(taskName);
        if (0 <= idx && idx < size) {
            // parse child task
            final Thread child = tasksList.get(idx);
            // interrupt child task if necessary
            if (Boolean.TRUE.equals(interrupted)) {
                try { child.interrupt(); }
                catch (Exception e) {
                    LogUtils.logWarn(this.getClass(), e.getMessage());
                }
            }
            // clear shared data
            this.getSharedData().remove(child);
            // remove child task
            tasksList.remove(child);
        } else idx = -1;
        return idx;
    }
    /**
     * Remove the child {@link Thread} task, also removing shared data for this task
     * @param taskName to remove
     * @return the removed {@link Thread}
     */
    public final Thread removeTask(String taskName) {
        return this.removeTask(taskName, Boolean.TRUE);
    }
    /**
     * Remove the child {@link Thread} task, also removing shared data for this task
     * @param taskName to remove
     * @param interrupted specify interrupting this task if necessary
     * @return the removed {@link Thread}
     */
    public final Thread removeTask(String taskName, Boolean interrupted) {
        Assert.hasText(taskName, "taskName");
        final List<Thread> tasksList = this.getTasks();
        int size = tasksList.size();
        int idx = this.getTaskIndex(taskName);
        final Thread child;
        if (0 <= idx && idx < size) {
            // parse child task
            child = tasksList.get(idx);
            // interrupt child task if necessary
            if (Boolean.TRUE.equals(interrupted)) {
                try { child.interrupt(); }
                catch (Exception e) {
                    LogUtils.logWarn(this.getClass(), e.getMessage());
                }
            }
            // clear shared data
            this.getSharedData().remove(child);
            // remove child task
            tasksList.remove(child);
        } else child = null;
        return child;
    }

    /**
     * Process un-caught exception
     * TODO Children classes maybe override this method for processing un-caught exception
     * @param t {@link Thread}
     * @param e {@link Throwable}
     */
    protected void uncaughtException(Thread t, Throwable e) {
        String threadName = MessageFormat.format("{0}.{1}",
                (t != null && StringUtils.hasText(t.getName()) ? t.getName() : "THREAD"),
                (t != null ? String.valueOf(t.getId()) : "0"));
        String exMsg = (e == null ? "UNKNOWN EXCEPTION" : e.getMessage());
        LogUtils.logWarn(this.getClass(), "EXCEPTION FROM TASK {" + threadName + "} - " + exMsg);
        LogUtils.logError(this.getClass(), e);
    }

    /* (非 Javadoc)
     * @see java.lang.Thread#run
     */
    @Override
    public final void run() {
        // check listeners
        Assert.notEmpty(this.getTasks(), "tasks");
        // create thread for every listener
        for(Thread task : this.getTasks()) {
            // exception handler
            UncaughtExceptionHandler exHandler = task.getUncaughtExceptionHandler();
            if (exHandler == null) {
                exHandler = new UncaughtExceptionHandler() {

                    /* (非 Javadoc)
                     * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
                     */
                    @Override
                    public void uncaughtException(Thread t, Throwable e) {
                        // process un-caught exception
                        MultiThreading.this.uncaughtException(t, e);
                    }
                };
            } else {
                final UncaughtExceptionHandler orginalExHandler = exHandler;
                exHandler = new UncaughtExceptionHandler() {

                    /* (非 Javadoc)
                     * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
                     */
                    @Override
                    public void uncaughtException(Thread t, Throwable e) {
                        // process un-caught exception
                        MultiThreading.this.uncaughtException(t, e);
                        orginalExHandler.uncaughtException(t, e);
                    }
                };
            }
            task.setUncaughtExceptionHandler(exHandler);
            // start child task if necessary
            try { task.start(); }
            catch (Exception e) { LogUtils.logWarn(this.getClass(), e.getMessage()); }
        }
        // wait for child tasks
        while(Boolean.TRUE.equals(this.stillAlive(Boolean.FALSE))) {
            LogUtils.logDebug(this.getClass(), "Sleep to wait child tasks!");
            try { Thread.sleep(3000); }
            catch (Exception e) { LogUtils.logWarn(this.getClass(), e.getMessage()); }
        }
        // perform finishing
        this.performFinish();
    }

    /**
     * Perform action after all task(s) has been finished
     * TODO This method will be called right after all task(s) has been finished from {@link #run()} method
     */
    protected abstract void performFinish();

    /**
     * Check whether existed at least one alive child task
     * @param removeDeathTasks specify whether removing the interrupted tasks and shared data of these tasks
     * @return true for alive; else false
     */
    protected final Boolean stillAlive(Boolean removeDeathTasks) {
        List<Thread> tasksList = this.getTasks();
        Boolean isAlive = !CollectionUtils.isEmpty(tasksList);
        if (Boolean.TRUE.equals(isAlive)) {
            isAlive = Boolean.FALSE;
            List<Thread> deathTasks = new LinkedList<Thread>();
            for(Thread task : tasksList) {
                Boolean taskDeath = (task == null || task.isInterrupted() || !task.isAlive());
                if (Boolean.TRUE.equals(taskDeath)) {
                    String threadName = MessageFormat.format("{0}.{1}",
                            (task != null && StringUtils.hasText(task.getName()) ? task.getName() : "THREAD"),
                            (task != null ? String.valueOf(task.getId()) : "0"));
                    LogUtils.logDebug(this.getClass(), "Task {" + threadName + "} has been interrupted!");
                    // remove child task if it has been interuppted
                    deathTasks.add(task);
                    // remove shared data
                    if (Boolean.TRUE.equals(removeDeathTasks) && this.getSharedData().containsKey(task)) {
                        this.getSharedData().remove(task);
                    }
                }
                isAlive = (Boolean.TRUE.equals(isAlive) || !Boolean.TRUE.equals(taskDeath));
                if (Boolean.TRUE.equals(isAlive)) break;
            }
            // remove all death tasks
            if (Boolean.TRUE.equals(removeDeathTasks) && !CollectionUtils.isEmpty(deathTasks)) {
                tasksList.removeAll(deathTasks);
                deathTasks.clear();
            }
        }
        return Boolean.TRUE.equals(isAlive);
    }

    /* (非 Javadoc)
     * @see java.lang.Thread#isInterrupted
     */
    @Override
    public final boolean isInterrupted() {
        return !Boolean.TRUE.equals(this.stillAlive(Boolean.FALSE));
    }

    /* (非 Javadoc)
     * @see java.lang.Thread#interrupt
     */
    @Override
    public final void interrupt() {
        // interupt child threads
        LogUtils.logDebug(this.getClass(), "Main Thread has been interuptted. So interupting child threads!");
        List<Thread> tasksList = this.getTasks();
        if (!CollectionUtils.isEmpty(tasksList)) {
            for(final Thread task : tasksList) {
                if (task.isAlive() && !task.isInterrupted()) {
                    LogUtils.logDebug(this.getClass(), "--> Interupting Thread {" + task.getName() + "} by main thread!");
                    try { task.interrupt(); }
                    catch (Exception e) {}
                    finally {
                        // remove shared data
                        if (this.getSharedData().containsKey(task)) {
                            this.getSharedData().remove(task);
                        }
                    }
                }
            }
        }
        tasksList.clear();
        this.getSharedData().clear();
        // interupt by super
        super.interrupt();
    }

    /* (非 Javadoc)
     * @see java.lang.Thread#finalize
     */
    @Override
    protected final void finalize() throws Throwable {
        // interrupt child tasks
        try { this.interrupt(); }
        catch (Exception e) {}
        // release
        this.release();
        // finalize by super
        super.finalize();
    }

    /**
     * TODO Children class maybe override this method for relasing managed resource(s) if necessary
     */
    protected void release() {}
}
