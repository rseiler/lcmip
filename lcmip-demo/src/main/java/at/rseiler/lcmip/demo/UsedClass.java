package at.rseiler.lcmip.demo;

public class UsedClass {

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
        System.out.println("not important");
    }

    public void unused() {
        System.out.println("not important");
    }

}
