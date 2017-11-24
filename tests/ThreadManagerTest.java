import logic.ThreadManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ThreadManagerTest {

    private ThreadManager threadManager;

    @BeforeEach
    void setUp() {
        threadManager = new ThreadManager();
    }

    @Test
    void threadSearchTest(){
        String threadSearch = "Finalizer";
        Thread result = threadManager.searchThread(threadSearch);
        assertTrue(result.getName().equals(threadSearch));
    }

    @Test
    void threadGroupSearchTest(){
        String threadGroupSearch = "system";
        ThreadGroup result = threadManager.searchThreadGroup(threadGroupSearch);
        assertTrue(result.getName().equals(threadGroupSearch));

    }

    @Test
    void threadGroupFilterTest(){
        ThreadGroup filter = threadManager.getRootThreadGroup();
        ArrayList<Thread> threads = threadManager.filterByGroup(filter);
        boolean check = checkThreads(threads, filter);
        assertTrue(check);
    }

    private boolean checkThreads(ArrayList<Thread> threads, ThreadGroup threadGroup) {
        for (Thread thread :
             threads) {
            if (!(thread.getThreadGroup().equals(threadGroup))){
                return false;
            }
        }
        return true;
    }

}