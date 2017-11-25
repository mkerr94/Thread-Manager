package logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class ThreadManager {

    private ThreadRunnable runnable;

    /**
     * @modifies this.allThreads, this.allGroups
     */
    public ThreadManager(){
        runnable = new ThreadRunnable();
    }

    /**
     * @requires returnable of getRootThreadGroup != null
     * @return an array of all thread-groups running in the jvm.
     */
    public ThreadGroup[] getAllThreadGroups() {
        assert getRootThreadGroup() != null
                : "getRootThreadGroup() is returning null";
        final ThreadGroup root = getRootThreadGroup();
        int arrayAlloc = root.activeGroupCount(), i;
        ThreadGroup[] groups;
        do {
            arrayAlloc *= 2;
            groups = new ThreadGroup[arrayAlloc];
            i = root.enumerate( groups, true );
        } while (i == arrayAlloc);

        ThreadGroup[] returnable = new ThreadGroup[i + 1];
        returnable[0] = root;
        System.arraycopy(groups, 0, returnable, 1, i);
        return returnable;
    }

    /**
     * @requires returnable of getRootThreadGroup != null
     * @return an array of all the running threads
     */
    public Thread[] getAllThreads(){
        assert getRootThreadGroup() != null
                : "getRootThreadGroup() is returning null";
        ThreadGroup rootGroup = getRootThreadGroup();
        int size = rootGroup.activeCount();
        Thread[] returnable = new Thread[size];
        rootGroup.enumerate(returnable);
        return returnable;
    }

    /**
     * @return The root thread-group (which should always be system)
     */
    public ThreadGroup getRootThreadGroup(){
        ThreadGroup rootGroup = Thread.currentThread().getThreadGroup();
        ThreadGroup parentGroup;
        while ((parentGroup = rootGroup.getParent()) != null) {
            rootGroup = parentGroup;
        }
        assert (rootGroup.getName().equals("system"))
                : "root is not system, root is: " + rootGroup.getName();
        return rootGroup;
    }

    /**
     * mostly for testing purposes
     * @requires allThreads != null
     * @effects displays all the currently running threads in the JVM to standard out
     */
    public void displayAllThreads() {
        assert getAllThreads() != null
                : "allThreads is null";
        System.out.println("No of threads: " + getAllThreads().length);
        for (Thread thread :
                getAllThreads()) {
            System.out.println("Thread group: " + thread.getThreadGroup().getName() + "    Thread name: " + thread.getName());
        }
    }

    /**
     * mostly for testing purposes
     * @requires allGroups != null
     * @effects Displays all the thread groups in the JVM to standard out
     */
    public void displayAllThreadGroups(){
        assert getAllThreadGroups() != null
                : "allGroups is null";
        System.out.println("No of thread-groups: " + getAllThreadGroups().length);
        for (ThreadGroup threadgroup :
                getAllThreadGroups()) {
            System.out.println("ThreadGroup Name: " + threadgroup.getName());
        }
    }

    /**
     * @requires allThreads != null && search != null
     * @param search the thread the user wants to search for
     * @return the matching thread. returns null if no match is found.
     */
    public Thread searchThread(String search){
        search = search.toLowerCase(Locale.ENGLISH);
        for (Thread thread :
                getAllThreads()) {
            String curThreadName = thread.getName().toLowerCase(Locale.ENGLISH);
            if(curThreadName.equals(search)) {
                return thread;
            }
        }
        return null;
    }

    /**
     * @requires allGroups != null && search != null
     * @param search The thread group the user wants to search for
     * @return The thread matching the string. Will return null if no match is found
     */
    public ThreadGroup searchThreadGroup(String search){
        search = search.toLowerCase(Locale.ENGLISH);
        for (ThreadGroup threadGroup :
                getAllThreadGroups()) {
            String curGroupName = threadGroup.getName().toLowerCase(Locale.ENGLISH);
            if (curGroupName.equals(search)) {
                return threadGroup;
            }
        }
        return null;
    }

    /**
     * @requires filter != null && allThreads != null
     * @param filter the thread-group the user wishes to filter by
     * @return an arraylist of threads belonging to the filter thread-group
     */
    public ArrayList<Thread> filterByGroup(ThreadGroup filter){
        ArrayList<Thread> returnable = new ArrayList<>();
        for (Thread thread :
                getAllThreads()) {
            ThreadGroup threadGroup = thread.getThreadGroup();
            if (threadGroup.equals(filter)) {
                returnable.add(thread);
            }
        }
        return returnable;
    }

    public Thread createThread(String name) {
        ThreadGroup threadGroup = new ThreadGroup("MyGroup");
        return new Thread(threadGroup, runnable, name);
    }

    /**
     * @param id the id of the thread to interrupt
     * @requires id != null
     * @modifies this.dataModel
     * @effects thread with id is interrupted
     */
     public void killThread(Long id) {
        Thread[] allThreads = getAllThreads();
        Thread killThread = null;
        for (Thread t :
                allThreads) {
            if (t.getId() == id) {
                killThread = t;
            }
        }
        if (killThread != null) killThread.interrupt();
    }
}
