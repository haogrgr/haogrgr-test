package com.haogrgr.test.main;

import java.io.PrintWriter;

import org.eclipse.jdt.core.compiler.CompilationProgress;
import org.eclipse.jdt.core.compiler.batch.BatchCompiler;

public class ECJCompiler {

    public static void main(String[] args) {
        CompilationProgress progress = null;

        boolean ret = BatchCompiler.compile(
                "/Users/tudesheng/projects/haogrgr/haogrgr-test/src/test/java/com/haogrgr/test/main/Temp.java",
                new PrintWriter(System.out), new PrintWriter(System.err), progress);

        System.out.println(ret);
    }

}
