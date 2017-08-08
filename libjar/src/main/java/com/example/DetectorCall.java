package com.example;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.tools.lint.client.api.JavaParser;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;

import java.util.Collections;
import java.util.List;

import lombok.ast.AstVisitor;
import lombok.ast.ConstructorInvocation;

/**
 * 定义代码检查规则
 * 由于要对java代码进行扫描,因此继承的是javascanner的接口
 */
public class DetectorCall extends Detector implements Detector.JavaScanner
{
    public static Issue ISSUE_CALL = Issue.create("拨打电话操作",
        "call operation should be done after CALL_PHONE permission check",
        "call operation should be done after CALL_PHONE permission check, you should declare CALL_PHONE in manifest at the same time",
        /**
         * 这个主要是用于对问题的分类，不同的问题就可以集中在一起显示。
         * 注意，相同的category最好是单实例的，否则会导致导出的html对该category处理无法归类。
         */
        Category.CORRECTNESS,
        6,
        /**
         * 定义查找问题的严重级别
         */
        Severity.ERROR,
        /**
         * 是用于提供处理该问题的Detector和该Detector所关心的资源范围。当系统生成了抽象语法树（Abstract syntax tree，简称AST），
         * 或者遍历xml资源时，就会调用对应Issue的处理器Detector。这个后面会提到。
         */
        new Implementation(DetectorCall.class, Scope.JAVA_FILE_SCOPE));

    @Override
    public List<String> getApplicableConstructorTypes() {
        return Collections.singletonList("android.content.Intent");
    }

    @Override
    public void visitConstructor(@NonNull JavaContext context, @Nullable AstVisitor visitor, @NonNull ConstructorInvocation node, @NonNull JavaParser.ResolvedMethod constructor)
    {
        int count = constructor.getArgumentCount();
        if(count==1)
        {
            if(node.toString().contains("(Intent.ACTION_CALL)"))
            {
                context.report(ISSUE_CALL, node, context.getLocation(node), "请在操作前先检查权限CALL_PHONE授予情况");
            }
        }

        super.visitConstructor(context, visitor, node, constructor);
    }
}
