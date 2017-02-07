package com.haogrgr.test.main;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.google.common.base.Charsets;
import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.LiteralNode;
import jdk.nashorn.internal.ir.Statement;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.parser.Parser;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.ErrorManager;
import jdk.nashorn.internal.runtime.Source;
import jdk.nashorn.internal.runtime.options.Options;

@SuppressWarnings("restriction")
public class JavaScriptAst {

    public static void main(String[] args) throws IOException {
        Options options = new Options("nashorn");
        options.set("anon.functions", true);
        options.set("parse.only", true);
        options.set("scripting", true);

        ErrorManager errors = new ErrorManager();
        Context context = new Context(options, errors, Thread.currentThread().getContextClassLoader());
        Source source = Source.sourceFor("test",
            new File("/Users/tudesheng/projects/static/bms-fs/src/ibms/wsa/stock/stockIn/stockManage.js"),
            Charsets.UTF_8);
        Parser parser = new Parser(context.getEnv(), source, errors);

        FunctionNode functionNode = parser.parse();
        Block block = functionNode.getBody();
        List<Statement> statements = block.getStatements();
        for (Statement statement : statements) {
            statement.accept(new NodeVisitor<LexicalContext>(new LexicalContext()) {
                @Override
                public boolean enterLiteralNode(LiteralNode<?> literalNode) {
                    if (literalNode.isString())
                        System.out.println(literalNode.getString());
                    return super.enterLiteralNode(literalNode);
                }
            });
        }
    }

}
