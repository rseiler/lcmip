package at.rseiler.lcmip.demo;

import org.mockito.Mockito;

import static org.mockito.Mockito.verify;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        new MainInnerClass().usedMethod();
        new UsedClass().used();
        new UsedClass().used();
        new UsedClass().used();
        new UsedClass("used").used();
        System.out.println("hello world".replaceAll("world", "lcmip"));

        // Mockito generates proxy classes which should not be logged.
        UsedClass mock = Mockito.spy(new UsedClass());
        mock.used();
        verify(mock).used();

        Thread.sleep(60_000);
        new UsedClass().used();
    }

    public static class MainInnerClass {

        public void usedMethod() {
            System.out.println("not important");
        }

        public void unusedMethod() {
            System.out.println("not important");
        }

    }

}
