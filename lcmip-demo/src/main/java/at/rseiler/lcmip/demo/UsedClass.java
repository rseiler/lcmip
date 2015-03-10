package at.rseiler.lcmip.demo;

public class UsedClass {

    private static final String NOT_IMPORTANT = "not important";

    private String something;
    private String unused;

    public UsedClass() {
    }

    public UsedClass(String something) {
        this.something = something;
    }

    public UsedClass(String something, String unused) {
        this.something = something;
        this.unused = unused;
    }

    public void used() {
        System.out.println(NOT_IMPORTANT);
    }

    public void unused() {
        System.out.println(NOT_IMPORTANT);
    }

}
