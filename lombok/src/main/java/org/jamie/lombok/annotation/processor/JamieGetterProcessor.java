package org.jamie.lombok.annotation.processor;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;

/**
 * @author EchoJamie
 * @version 1.0.0
 * @description 编译时注解处理器 需要依赖 $JAVA_HOME/lib/tools.jar
 * @date 2023/5/1 01:58
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"org.jamie.lombok.annotation.JamieGetter"})
public class JamieGetterProcessor extends AbstractProcessor {

    /**
     * 获取已编译的语法树
     */
    private JavacTrees javacTrees;
    /**
     * 构建语法树元素
     */
    private TreeMaker treeMaker;
    /**
     *
     */
    private Names names;
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        String init = "JamieGetterProcessor initialized.";
        // maven 控制台输出
        System.out.println(init);
        // idea 窗口输出
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, init);

        // 初始化 处理环境
        // 用于获取语法树
        javacTrees = JavacTrees.instance(processingEnv);
        // 获取编译上下文
        Context context = ((JavacProcessingEnvironment)processingEnv).getContext();
        // 获取已编译的元素
        this.treeMaker = TreeMaker.instance(context);
        super.init(processingEnv);
        this.names = Names.instance(context);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        annotations.stream()
                //
                .flatMap(typeElement -> roundEnv.getElementsAnnotatedWith(typeElement).stream())
                .forEach(element -> {
                    JCTree tree = javacTrees.getTree(element);
                    // 基于访问者设计模式  修改方法
                    tree.accept(new TreeTranslator() {
                        // 访问方法
                        @Override
                        public void visitMethodDef(JCTree.JCMethodDecl jcMethodDecl) {
                            JCTree.JCStatement getterMethods = treeMaker.Exec(
                                    treeMaker.Apply(
                                            List.nil(),
                                            select("System.out.println"),
                                            List.of(treeMaker.Literal("getter Methods"))
                                    )
                            );
                            // 覆盖原有语句块
                            jcMethodDecl.body.stats = jcMethodDecl.body.stats.append(getterMethods);
                            super.visitMethodDef(jcMethodDecl);
                        }
                    });
                });

        return false;
    }

    private JCTree.JCFieldAccess select(String expression) {
        String[] exps = expression.split("\\.");
        JCTree.JCFieldAccess access = treeMaker.Select(ident(exps[0]), names.fromString(exps[1]));
        int index = 2;
        while (index < exps.length) {
            access = treeMaker.Select(access, names.fromString(exps[index++]));
        }
        return access;
    }

    private JCTree.JCIdent ident(String name) {
        return treeMaker.Ident(names.fromString(name));
    }
}
