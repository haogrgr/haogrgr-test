//package com.haogrgr.test.main;
//
//import java.io.File;
//import java.nio.charset.Charset;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//import javax.tools.JavaFileObject;
//
//import com.sun.source.tree.LiteralTree;
//import com.sun.source.util.TreeScanner;
//import com.sun.tools.javac.file.JavacFileManager;
//import com.sun.tools.javac.main.JavaCompiler;
//import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
//import com.sun.tools.javac.tree.JCTree.JCLiteral;
//import com.sun.tools.javac.util.Context;
//import com.sun.tools.javac.util.List;
//import com.sun.tools.javac.util.Pair;
//
///**
// * 使用javac来抽取源文件中的字面量，可以用来做老项目国际化改造，提取所有硬编码中文
// * 
// * @author shengde.tds
// * @since 2017-01-23 11:26:51 +0800
// */
//public class CompilerMain {
//
//    public static void main(String[] args) throws Throwable {
//        // 主要是JavaCompiler类的parseFiles方法
//        Context context = new Context();
//        JavacFileManager jfm = new JavacFileManager(context, true, Charset.forName("UTF-8"));
//        JavaCompiler compiler = JavaCompiler.instance(context);
//
//        // 枚举目录，找出.java文件
//        String dir = "/Users/shengde/projects/";
//        Iterable<JavaFileObject> jfiles = listJavaFiles(dir, jfm);
//
//        // 解析java源文件为AST，并获取字面量Node
//        java.util.List<Pair<JCCompilationUnit, JCLiteral>> literals = parseAndListLiterals(jfiles, compiler);
//
//        System.out.println("AppName,English,Key,Simplified Chinese,Description,Group");
//        // 过滤String字面量和Char字面量
//        literals.stream().filter(pair -> {
//            com.sun.source.tree.Tree.Kind kind = pair.snd.getKind();
//            boolean isLStr = kind == com.sun.source.tree.Tree.Kind.STRING_LITERAL;
//            boolean isLChar = kind == com.sun.source.tree.Tree.Kind.CHAR_LITERAL;
//            boolean hasVal = pair.snd.getValue() != null;
//            return (isLStr || isLChar) && hasVal;
//        }).filter(pair -> {
//            if (pair.snd.getValue() instanceof Character) {
//                return isChinese(((Character) pair.snd.getValue()).charValue());
//            }
//            boolean hasChinese = ((CharSequence) pair.snd.getValue()).chars().anyMatch(CompilerMain::isChinese);
//            return hasChinese;
//        }).forEach(pair -> {
//            Object val = pair.snd.getValue();
//            String filename = pair.fst.sourcefile.getName();
//            String shortName = filename.substring(filename.lastIndexOf('/') + 1);
//            int lineNum = pair.fst.lineMap.getLineNumber(pair.snd.pos);
//
//            StringBuilder sb = new StringBuilder();
//            sb.append(val.toString()).append(",");// Simplified Chinese
//            sb.append(shortName + " : " + lineNum).append(",");// desc
//            System.out.println(sb);
//        });
//
//        // 不要黄色警告
//        jfm.close();
//    }
//
//    // 通过Files.find接口，找出dir下所有的后缀为.java的文件
//    protected static Iterable<JavaFileObject> listJavaFiles(String dir, JavacFileManager jfm) throws Exception {
//        Stream<Path> paths = Files.find(Paths.get(dir), 100, (path, attr) -> {
//            return path.toFile().isFile() && path.toString().endsWith(".java");
//        });
//        try {
//            java.util.List<File> collect = paths.map(Path::toFile).collect(Collectors.toList());
//            Iterable<? extends JavaFileObject> jfiles = jfm.getJavaFileObjectsFromFiles(collect);
//            List<JavaFileObject> from = List.from(jfiles);
//            return from;
//        } finally {
//            paths.close();
//        }
//    }
//
//    // 解析java源文件为AST，并获取字面量Node
//    protected static java.util.List<Pair<JCCompilationUnit, JCLiteral>> parseAndListLiterals(
//            Iterable<JavaFileObject> jfiles, JavaCompiler compiler) {
//        List<JCCompilationUnit> parseFiles = compiler.parseFiles(jfiles);
//
//        java.util.List<Pair<JCCompilationUnit, JCLiteral>> nodes = new ArrayList<>();
//        parseFiles.forEach(unit -> {
//            unit.accept(new TreeScanner<Void, Void>() {
//                public Void visitLiteral(LiteralTree node, Void p) {
//                    nodes.add(Pair.of(unit, (JCLiteral) node));
//                    return null;
//                }
//            }, null);
//        });
//
//        return nodes;
//    }
//
//    // 判断是否为汉字或中文标点，网上找的：http://www.cnblogs.com/zztt/p/3427452.html
//    protected static boolean isChinese(int intc) {
//        char c = (char) intc;
//        return isChineseByScript(c) || isChinesePunctuation(c);
//    }
//
//    // 判断是否为汉字
//    protected static boolean isChineseByScript(char c) {
//        Character.UnicodeScript sc = Character.UnicodeScript.of(c);
//        if (sc == Character.UnicodeScript.HAN) {
//            return true;
//        }
//        return false;
//    }
//
//    // 判断是否为中文标点
//    protected static boolean isChinesePunctuation(char c) {
//        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
//        if (ub == Character.UnicodeBlock.GENERAL_PUNCTUATION || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
//                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
//                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS
//                || ub == Character.UnicodeBlock.VERTICAL_FORMS) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//}
