package at.rseiler.lcmip.demo;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        new MainInnerClass().usedMethod();
        new UsedClass().used();
        new UsedClass().used();
        new UsedClass().used();
        new UsedClass("used").used();
        System.out.println("hello world".replaceAll("world", "lcmip"));

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
