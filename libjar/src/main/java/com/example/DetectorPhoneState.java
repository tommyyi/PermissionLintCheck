package com.example;

import com.android.tools.lint.client.api.JavaParser;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;

import java.util.ArrayList;
import java.util.List;

import lombok.ast.AstVisitor;
import lombok.ast.MethodInvocation;

/**
 * 定义代码检查规则
 * 这个是针对try和catch中的finally是否包涵close方法进行一个判断
 * 由于要对java代码进行扫描,因此继承的是javascanner的接口
 */
public class DetectorPhoneState extends Detector implements Detector.JavaScanner
{
    public static Issue ISSUE_PHONE_STATE = Issue.create("getDeviceId",
        "getDeviceId should be called after READ_PHONE_STATE permission check",
        "getDeviceId should be called after READ_PHONE_STATE permission check, you should declare READ_PHONE_STATE in manifest at the same time",
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
        new Implementation(DetectorPhoneState.class, Scope.JAVA_FILE_SCOPE));
    
    public static final String[] sSupportSuperType = new String[] {"android.telephony.TelephonyManager"};
    
    /**
     * 只关心名是close的方法
     *
     * @return
     */
    @Override
    public List<String> getApplicableMethodNames()
    {
        List<String> methodList = new ArrayList<>();
        methodList.add("getDeviceId");
        methodList.add("getCellLocation");
        return methodList;
    }
    
    /**
     * 该方法调用时，会传入代表getDeviceId方法被调用的节点MethodInvocation,以及所在java文件的上下文JavaContext，
     * 还有AstVisitor。由于我们没有重写createJavaVisitor方法，所以不用管AstVisitor。
     * MethodInvocation封装了getDeviceId被调用处的代码，而结合JavaContext对象，即可寻找对应的上下文，来帮助我们判断条件。
     *
     * @param context
     * @param visitor
     * @param node
     */
    @Override
    public void visitMethod(JavaContext context, AstVisitor visitor, MethodInvocation node)
    {
        // 判断类型,看下所监测的资源是否是我们定义的相关资源
        // 通过JavaContext的resolve的方法,传入node节点,由于所有的AST树上的节点都继承自NODE,所以可以通过node去找到class
        JavaParser.ResolvedMethod method = (JavaParser.ResolvedMethod)context.resolve(node);
        JavaParser.ResolvedClass clzz = method.getContainingClass();
        boolean isSubClass = false;
        for (String targetClzz : sSupportSuperType)
        {
            if (clzz.isSubclassOf(targetClzz, false))
            {
                isSubClass = true;
                break;
            }
        }

        if (!isSubClass)
            super.visitMethod(context, visitor, node);
        else
            context.report(ISSUE_PHONE_STATE, node, context.getLocation(node), "please check permission READ_PHONE_STATE");
    }
}
