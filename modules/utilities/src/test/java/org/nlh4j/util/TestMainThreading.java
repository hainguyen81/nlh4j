/**
 *
 */
package org.nlh4j.util;

import java.io.Serializable;

import org.junit.Test;

/**
 * Test class of {@link MultiThreading}
 *
 * @author Hai Nguyen
 *
 */
public final class TestMainThreading implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    private MultiThreading mainThread;

    /**
     * Test {@link MultiThreading}
     */
    @Test
    public void testMultiThread() {
        // child 1
        final Thread task1 = new Thread(new Runnable() {

            /* (非 Javadoc)
             * @see java.lang.Runnable#run()
             */
            @Override
            public void run() {
                int cnt = 0;
                do {
                    cnt++;
                    System.out.println("TASK 1 - {" + String.valueOf(cnt) + "}");
                    try { Thread.sleep(2000); }
                    catch (Exception e) {}
                    if (cnt > 10) {
                        mainThread.removeTask("Task1");
                    } else if (cnt == 10) {
                        mainThread.setSharedData("Task2", cnt);
                    }
                } while(true);
            }
        }, "Task1");
        task1.setContextClassLoader(this.getClass().getClassLoader());
        task1.setDaemon(Boolean.TRUE);
        task1.setPriority(Thread.MIN_PRIORITY);
        // child 2
        final Thread task2 = new Thread(new Runnable() {

            /* (非 Javadoc)
             * @see java.lang.Runnable#run()
             */
            @Override
            public void run() {
                int cnt = 0;
                do {
                    cnt++;
                    System.out.println("TASK 2 - {" + String.valueOf(cnt) + "}");
                    try { Thread.sleep(2000); }
                    catch (Exception e) {}
                    if (cnt > 30) {
                        mainThread.removeTask("Task2");
                    } else if (cnt == 20) {
                        mainThread.setSharedData("Task3", cnt);
                    }
                } while(true);
            }
        }, "Task2");
        task2.setContextClassLoader(this.getClass().getClassLoader());
        task2.setDaemon(Boolean.TRUE);
        task2.setPriority(Thread.MIN_PRIORITY);
        // child 3
        final Thread task3 = new Thread(new Runnable() {

            /* (非 Javadoc)
             * @see java.lang.Runnable#run()
             */
            @Override
            public void run() {
                int cnt = 0;
                do {
                    cnt++;
                    System.out.println("TASK 3 - {" + String.valueOf(cnt) + "}");
                    try { Thread.sleep(2000); }
                    catch (Exception e) {}
                    if (cnt > 40) {
                        mainThread.removeTask("Task3");
                    } else if (cnt == 30) {
                        mainThread.setSharedData("Task1", cnt);
                    }
                } while(true);
            }
        }, "Task3");
        task3.setContextClassLoader(this.getClass().getClassLoader());
        task3.setDaemon(Boolean.TRUE);
        task3.setPriority(Thread.MIN_PRIORITY);
        // main thread
        mainThread = new MultiThreading() {

            /**
             * serialVersionUID
             */
            private static final long serialVersionUID = 1L;

            /* (非 Javadoc)
             * @see org.nlh4j.util.MultiThreading#performSharing(java.lang.Thread)
             */
            @Override
            protected void performSharing(Thread task) {
                Object sharedData = super.getSharedData(task);
                String sharedDataStr = (sharedData == null ? "NULL" : String.valueOf(sharedData));
                if ("Task1".equalsIgnoreCase(task.getName())) {
                    LogUtils.logDebug(this.getClass(), "Shared Task 1! Shared data {" + sharedDataStr + "}");
                } else if ("Task2".equalsIgnoreCase(task.getName())) {
                    LogUtils.logDebug(this.getClass(), "Shared Task 2! Shared data {" + sharedDataStr + "}");
                } else if ("Task3".equalsIgnoreCase(task.getName())) {
                    LogUtils.logDebug(this.getClass(), "Shared Task 3! Shared data {" + sharedDataStr + "}");
                } else {
                    LogUtils.logDebug(this.getClass(), "Shared Unknown Task!");
                }
            }

            /*
             * (Non-Javadoc)
             * @see org.nlh4j.util.MultiThreading#performFinish()
             */
            @Override
            protected void performFinish() {}
        };
        mainThread.setContextClassLoader(this.getClass().getClassLoader());
        mainThread.setDaemon(Boolean.TRUE);
        mainThread.setPriority(Thread.NORM_PRIORITY);
        mainThread.addTask(task1, Boolean.FALSE);
        mainThread.addTask(task2, Boolean.FALSE);
        mainThread.addTask(task3, Boolean.FALSE);
        // start main Thread
        mainThread.start();
    }

    /**
     * Check main thread alive
     * @return true for alive; else false
     */
    public boolean isAlive() {
        return (mainThread != null && !mainThread.isInterrupted() && mainThread.isAlive());
    }

    /**
     * Interrupt main thread
     */
    public void interrupt() {
        if (mainThread != null) { mainThread.interrupt(); }
    }

    /**
     * Test Main
     * @param args arguments
     */
    public static void main(String[] args) {
        TestMainThreading test = new TestMainThreading();
        test.testMultiThread();
        do {
            try { Thread.sleep(3000); }
            catch (Exception e) {}
        } while(test.isAlive());
        test.interrupt();
    }
}
