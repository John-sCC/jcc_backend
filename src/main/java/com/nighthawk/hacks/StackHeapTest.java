package com.nighthawk.hacks;

public class StackHeapTest {
    public int n = 5; //primitive data type on heap
    public String tester = "hello";
    public double decimal = 5.0;
    public static void changeInt(int nValue, int nRefN, StackHeapTest nRef, String testerval, double decimalRef)
    {
        System.out.println("\nBefore1:");
        System.out.println("\nn:\nvalue = " + nValue + "\n" + "hash = " + System.identityHashCode(nValue));
        System.out.println("\nnRefN:\nvalue = " + nRefN + "\n" + "hash = " + System.identityHashCode(nRefN));
        System.out.println("\nnRef.n:\nvalue = " + nRef.n + "\n" + "hash = " + System.identityHashCode(nRef.n));
        System.out.println("\nStringValue\nvalue = " + testerval + "\n" + "hash = " + System.identityHashCode(testerval));
        System.out.println("\nReferenceStringValue\nvalue = " + nRef.tester + "\n" + "hash = " + System.identityHashCode(nRef.tester));
        System.out.println("\nDoubleValue\nvalue = " + decimalRef + "\n" + "hash = " + System.identityHashCode(decimalRef));
        nValue += 10;
        nRefN += 10;
        nRef.n += 10;
        nRef.tester += " world";
        testerval += " world";
        decimalRef += 10.0;
        System.out.println("\nAfter1:");
        System.out.println("\nn:\nvalue = " + nValue + "\n" + "hash = " + System.identityHashCode(nValue));
        System.out.println("\nnRefN:\nvalue = " + nRefN + "\n" + "hash = " + System.identityHashCode(nRefN));
        System.out.println("\nnRef.n:\nvalue = " + nRef.n + "\n" + "hash = " + System.identityHashCode(nRef.n));
        System.out.println("\nStringValue\nvalue = " + testerval + "\n" + "hash = " + System.identityHashCode(testerval));
        System.out.println("\nReferenceStringValue\nvalue = " + nRef.tester + "\n" + "hash = " + System.identityHashCode(nRef.tester));
        System.out.println("\nDoubleValue\nvalue = " + decimalRef + "\n" + "hash = " + System.identityHashCode(decimalRef));

    }

    public static void main(String[] args)
    {
        int n = 5; //primitive data type on stack
        String tester = "hello";
        double decimalRef = 5.0;

        StackHeapTest nRef = new StackHeapTest();
        System.out.println("\nBefore2: ");
        System.out.println("\nn:\nvalue = " + n + "\n" + "hash = " + System.identityHashCode(n));
        System.out.println("\nnRefN:\nvalue = " + nRef.n + "\n" + "hash = " + System.identityHashCode(nRef.n));
        System.out.println("\nnRef.n:\nvalue = " + nRef + "\n" + "hash = " + System.identityHashCode(nRef));
        System.out.println("\nStringValue\nvalue = " + tester + "\n" + "hash = " + System.identityHashCode(tester));
        System.out.println("\nReferenceStringValue\nvalue = " + nRef.tester + "\n" + "hash = " + System.identityHashCode(nRef.tester));
        System.out.println("\nDoubleValue\nvalue = " + decimalRef + "\n" + "hash = " + System.identityHashCode(decimalRef));

        changeInt(n, nRef.n, nRef, tester, decimalRef); //stack by value, heap by value, heap by reference

        System.out.println("\nAfter2: ");
        System.out.println("\nn:\nvalue = " + n + "\n" + "hash = " + System.identityHashCode(n));
        System.out.println("\nnRef.n:\nvalue = " + nRef.n + "\n" + "hash = " + System.identityHashCode(nRef.n));
        System.out.println("\nnRefN:\nvalue = " + nRef + "\n" + "hash = " + System.identityHashCode(nRef));
        System.out.println("\nStringValue\nvalue = " + tester + "\n" + "hash = " + System.identityHashCode(tester));
        System.out.println("\nReferenceStringInMethod\nvalue = " + nRef.tester + "\n" + "hash = " + System.identityHashCode(nRef.tester));
        System.out.println("\nDoubleValue\nvalue = " + decimalRef + "\n" + "hash = " + System.identityHashCode(decimalRef));

    }
}
