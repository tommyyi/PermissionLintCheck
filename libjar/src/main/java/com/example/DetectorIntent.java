package com.example;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;

import java.util.Collections;
import java.util.List;

import lombok.ast.AlternateConstructorInvocation;
import lombok.ast.Annotation;
import lombok.ast.AnnotationDeclaration;
import lombok.ast.AnnotationElement;
import lombok.ast.AnnotationMethodDeclaration;
import lombok.ast.AnnotationValueArray;
import lombok.ast.ArrayAccess;
import lombok.ast.ArrayCreation;
import lombok.ast.ArrayDimension;
import lombok.ast.ArrayInitializer;
import lombok.ast.Assert;
import lombok.ast.AstVisitor;
import lombok.ast.BinaryExpression;
import lombok.ast.Block;
import lombok.ast.BooleanLiteral;
import lombok.ast.Break;
import lombok.ast.Case;
import lombok.ast.Cast;
import lombok.ast.Catch;
import lombok.ast.CharLiteral;
import lombok.ast.ClassDeclaration;
import lombok.ast.ClassLiteral;
import lombok.ast.Comment;
import lombok.ast.CompilationUnit;
import lombok.ast.ConstructorDeclaration;
import lombok.ast.ConstructorInvocation;
import lombok.ast.Continue;
import lombok.ast.Default;
import lombok.ast.DoWhile;
import lombok.ast.EmptyDeclaration;
import lombok.ast.EmptyStatement;
import lombok.ast.EnumConstant;
import lombok.ast.EnumDeclaration;
import lombok.ast.EnumTypeBody;
import lombok.ast.ExpressionStatement;
import lombok.ast.FloatingPointLiteral;
import lombok.ast.For;
import lombok.ast.ForEach;
import lombok.ast.Identifier;
import lombok.ast.If;
import lombok.ast.ImportDeclaration;
import lombok.ast.InlineIfExpression;
import lombok.ast.InstanceInitializer;
import lombok.ast.InstanceOf;
import lombok.ast.IntegralLiteral;
import lombok.ast.InterfaceDeclaration;
import lombok.ast.KeywordModifier;
import lombok.ast.LabelledStatement;
import lombok.ast.MethodDeclaration;
import lombok.ast.MethodInvocation;
import lombok.ast.Modifiers;
import lombok.ast.Node;
import lombok.ast.NormalTypeBody;
import lombok.ast.NullLiteral;
import lombok.ast.PackageDeclaration;
import lombok.ast.Return;
import lombok.ast.Select;
import lombok.ast.StaticInitializer;
import lombok.ast.StringLiteral;
import lombok.ast.Super;
import lombok.ast.SuperConstructorInvocation;
import lombok.ast.Switch;
import lombok.ast.Synchronized;
import lombok.ast.This;
import lombok.ast.Throw;
import lombok.ast.Try;
import lombok.ast.TypeReference;
import lombok.ast.TypeReferencePart;
import lombok.ast.TypeVariable;
import lombok.ast.UnaryExpression;
import lombok.ast.VariableDeclaration;
import lombok.ast.VariableDefinition;
import lombok.ast.VariableDefinitionEntry;
import lombok.ast.VariableReference;
import lombok.ast.While;

/**
 * 定义代码检查规则
 * 由于要对java代码进行扫描,因此继承的是javascanner的接口
 */
public class DetectorIntent extends Detector implements Detector.JavaScanner
{
    public static Issue ISSUE_Intent = Issue.create("联系人",
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
        new Implementation(DetectorIntent.class, Scope.JAVA_FILE_SCOPE));

    @Override
    public List<Class<? extends Node>> getApplicableNodeTypes() {
        return Collections.singletonList(Select.class);
    }

    @Override
    public AstVisitor createJavaVisitor(@NonNull JavaContext context)
    {
        return new AstVisitor()
        {
            @Override
            public boolean visitTypeReference(TypeReference node)
            {
                return false;
            }

            @Override
            public boolean visitTypeReferencePart(TypeReferencePart node)
            {
                return false;
            }

            @Override
            public boolean visitVariableReference(VariableReference node)
            {
                return false;
            }

            @Override
            public boolean visitIdentifier(Identifier node)
            {
                return false;
            }

            @Override
            public boolean visitIntegralLiteral(IntegralLiteral node)
            {
                return false;
            }

            @Override
            public boolean visitFloatingPointLiteral(FloatingPointLiteral node)
            {
                return false;
            }

            @Override
            public boolean visitBooleanLiteral(BooleanLiteral node)
            {
                return false;
            }

            @Override
            public boolean visitCharLiteral(CharLiteral node)
            {
                return false;
            }

            @Override
            public boolean visitStringLiteral(StringLiteral node)
            {
                return false;
            }

            @Override
            public boolean visitNullLiteral(NullLiteral node)
            {
                return false;
            }

            @Override
            public boolean visitBinaryExpression(BinaryExpression node)
            {
                return false;
            }

            @Override
            public boolean visitUnaryExpression(UnaryExpression node)
            {
                return false;
            }

            @Override
            public boolean visitInlineIfExpression(InlineIfExpression node)
            {
                return false;
            }

            @Override
            public boolean visitCast(Cast node)
            {
                return false;
            }

            @Override
            public boolean visitInstanceOf(InstanceOf node)
            {
                return false;
            }

            @Override
            public boolean visitConstructorInvocation(ConstructorInvocation node)
            {
                return false;
            }

            @Override
            public boolean visitMethodInvocation(MethodInvocation node)
            {
                return false;
            }

            @Override
            public boolean visitSelect(Select node)
            {
                String nodeStr = node.toString();
                if(nodeStr.contains("ContactsContract.Contacts"))
                    context.report(ISSUE_Intent, node, context.getLocation(node), "确认联系人权限");
                else if(nodeStr.contains("Calendars.CONTENT_URI"))
                    context.report(ISSUE_Intent, node, context.getLocation(node), "确认日历权限");
                else if(nodeStr.contains("ACTION_IMAGE_CAPTURE"))
                    context.report(ISSUE_Intent, node, context.getLocation(node), "确认相机权限");
                else if(nodeStr.contains("LOCATION_SERVICE"))
                    context.report(ISSUE_Intent, node, context.getLocation(node), "确认位置权限");
                return false;
            }

            @Override
            public boolean visitArrayAccess(ArrayAccess node)
            {
                return false;
            }

            @Override
            public boolean visitArrayCreation(ArrayCreation node)
            {
                return false;
            }

            @Override
            public boolean visitArrayInitializer(ArrayInitializer node)
            {
                return false;
            }

            @Override
            public boolean visitAnnotationValueArray(AnnotationValueArray node)
            {
                return false;
            }

            @Override
            public boolean visitArrayDimension(ArrayDimension node)
            {
                return false;
            }

            @Override
            public boolean visitClassLiteral(ClassLiteral node)
            {
                return false;
            }

            @Override
            public boolean visitSuper(Super node)
            {
                return false;
            }

            @Override
            public boolean visitThis(This node)
            {
                return false;
            }

            @Override
            public boolean visitLabelledStatement(LabelledStatement node)
            {
                return false;
            }

            @Override
            public boolean visitExpressionStatement(ExpressionStatement node)
            {
                return false;
            }

            @Override
            public boolean visitIf(If node)
            {
                return false;
            }

            @Override
            public boolean visitFor(For node)
            {
                return false;
            }

            @Override
            public boolean visitForEach(ForEach node)
            {
                return false;
            }

            @Override
            public boolean visitTry(Try node)
            {
                return false;
            }

            @Override
            public boolean visitCatch(Catch node)
            {
                return false;
            }

            @Override
            public boolean visitWhile(While node)
            {
                return false;
            }

            @Override
            public boolean visitDoWhile(DoWhile node)
            {
                return false;
            }

            @Override
            public boolean visitSynchronized(Synchronized node)
            {
                return false;
            }

            @Override
            public boolean visitBlock(Block node)
            {
                return false;
            }

            @Override
            public boolean visitAssert(Assert node)
            {
                return false;
            }

            @Override
            public boolean visitEmptyStatement(EmptyStatement node)
            {
                return false;
            }

            @Override
            public boolean visitSwitch(Switch node)
            {
                return false;
            }

            @Override
            public boolean visitCase(Case node)
            {
                return false;
            }

            @Override
            public boolean visitDefault(Default node)
            {
                return false;
            }

            @Override
            public boolean visitBreak(Break node)
            {
                return false;
            }

            @Override
            public boolean visitContinue(Continue node)
            {
                return false;
            }

            @Override
            public boolean visitReturn(Return node)
            {
                return false;
            }

            @Override
            public boolean visitThrow(Throw node)
            {
                return false;
            }

            @Override
            public boolean visitVariableDeclaration(VariableDeclaration node)
            {
                return false;
            }

            @Override
            public boolean visitVariableDefinition(VariableDefinition node)
            {
                return false;
            }

            @Override
            public boolean visitVariableDefinitionEntry(VariableDefinitionEntry node)
            {
                return false;
            }

            @Override
            public boolean visitTypeVariable(TypeVariable node)
            {
                return false;
            }

            @Override
            public boolean visitKeywordModifier(KeywordModifier node)
            {
                return false;
            }

            @Override
            public boolean visitModifiers(Modifiers node)
            {
                return false;
            }

            @Override
            public boolean visitAnnotation(Annotation node)
            {
                return false;
            }

            @Override
            public boolean visitAnnotationElement(AnnotationElement node)
            {
                return false;
            }

            @Override
            public boolean visitNormalTypeBody(NormalTypeBody node)
            {
                return false;
            }

            @Override
            public boolean visitEnumTypeBody(EnumTypeBody enumTypeBody)
            {
                return false;
            }

            @Override
            public boolean visitEmptyDeclaration(EmptyDeclaration node)
            {
                return false;
            }

            @Override
            public boolean visitMethodDeclaration(MethodDeclaration node)
            {
                return false;
            }

            @Override
            public boolean visitConstructorDeclaration(ConstructorDeclaration node)
            {
                return false;
            }

            @Override
            public boolean visitSuperConstructorInvocation(SuperConstructorInvocation node)
            {
                return false;
            }

            @Override
            public boolean visitAlternateConstructorInvocation(AlternateConstructorInvocation node)
            {
                return false;
            }

            @Override
            public boolean visitInstanceInitializer(InstanceInitializer node)
            {
                return false;
            }

            @Override
            public boolean visitStaticInitializer(StaticInitializer node)
            {
                return false;
            }

            @Override
            public boolean visitClassDeclaration(ClassDeclaration node)
            {
                return false;
            }

            @Override
            public boolean visitInterfaceDeclaration(InterfaceDeclaration node)
            {
                return false;
            }

            @Override
            public boolean visitEnumDeclaration(EnumDeclaration node)
            {
                return false;
            }

            @Override
            public boolean visitEnumConstant(EnumConstant node)
            {
                return false;
            }

            @Override
            public boolean visitAnnotationDeclaration(AnnotationDeclaration node)
            {
                return false;
            }

            @Override
            public boolean visitAnnotationMethodDeclaration(AnnotationMethodDeclaration node)
            {
                return false;
            }

            @Override
            public boolean visitCompilationUnit(CompilationUnit node)
            {
                return false;
            }

            @Override
            public boolean visitPackageDeclaration(PackageDeclaration node)
            {
                return false;
            }

            @Override
            public boolean visitImportDeclaration(ImportDeclaration node)
            {
                return false;
            }

            @Override
            public boolean visitParseArtefact(Node node)
            {
                return false;
            }

            @Override
            public boolean visitComment(Comment node)
            {
                return false;
            }

            @Override
            public void endVisit(Node node)
            {

            }

            @Override
            public void afterVisitTypeReference(TypeReference node)
            {

            }

            @Override
            public void afterVisitTypeReferencePart(TypeReferencePart node)
            {

            }

            @Override
            public void afterVisitVariableReference(VariableReference node)
            {

            }

            @Override
            public void afterVisitIdentifier(Identifier node)
            {

            }

            @Override
            public void afterVisitIntegralLiteral(IntegralLiteral node)
            {

            }

            @Override
            public void afterVisitFloatingPointLiteral(FloatingPointLiteral node)
            {

            }

            @Override
            public void afterVisitBooleanLiteral(BooleanLiteral node)
            {

            }

            @Override
            public void afterVisitCharLiteral(CharLiteral node)
            {

            }

            @Override
            public void afterVisitStringLiteral(StringLiteral node)
            {

            }

            @Override
            public void afterVisitNullLiteral(NullLiteral node)
            {

            }

            @Override
            public void afterVisitBinaryExpression(BinaryExpression node)
            {

            }

            @Override
            public void afterVisitUnaryExpression(UnaryExpression node)
            {

            }

            @Override
            public void afterVisitInlineIfExpression(InlineIfExpression node)
            {

            }

            @Override
            public void afterVisitCast(Cast node)
            {

            }

            @Override
            public void afterVisitInstanceOf(InstanceOf node)
            {

            }

            @Override
            public void afterVisitConstructorInvocation(ConstructorInvocation node)
            {

            }

            @Override
            public void afterVisitMethodInvocation(MethodInvocation node)
            {

            }

            @Override
            public void afterVisitSelect(Select node)
            {

            }

            @Override
            public void afterVisitArrayAccess(ArrayAccess node)
            {

            }

            @Override
            public void afterVisitArrayCreation(ArrayCreation node)
            {

            }

            @Override
            public void afterVisitArrayInitializer(ArrayInitializer node)
            {

            }

            @Override
            public void afterVisitAnnotationValueArray(AnnotationValueArray node)
            {

            }

            @Override
            public void afterVisitArrayDimension(ArrayDimension node)
            {

            }

            @Override
            public void afterVisitClassLiteral(ClassLiteral node)
            {

            }

            @Override
            public void afterVisitSuper(Super node)
            {

            }

            @Override
            public void afterVisitThis(This node)
            {

            }

            @Override
            public void afterVisitLabelledStatement(LabelledStatement node)
            {

            }

            @Override
            public void afterVisitExpressionStatement(ExpressionStatement node)
            {

            }

            @Override
            public void afterVisitIf(If node)
            {

            }

            @Override
            public void afterVisitFor(For node)
            {

            }

            @Override
            public void afterVisitForEach(ForEach node)
            {

            }

            @Override
            public void afterVisitTry(Try node)
            {

            }

            @Override
            public void afterVisitCatch(Catch node)
            {

            }

            @Override
            public void afterVisitWhile(While node)
            {

            }

            @Override
            public void afterVisitDoWhile(DoWhile node)
            {

            }

            @Override
            public void afterVisitSynchronized(Synchronized node)
            {

            }

            @Override
            public void afterVisitBlock(Block node)
            {

            }

            @Override
            public void afterVisitAssert(Assert node)
            {

            }

            @Override
            public void afterVisitEmptyStatement(EmptyStatement node)
            {

            }

            @Override
            public void afterVisitSwitch(Switch node)
            {

            }

            @Override
            public void afterVisitCase(Case node)
            {

            }

            @Override
            public void afterVisitDefault(Default node)
            {

            }

            @Override
            public void afterVisitBreak(Break node)
            {

            }

            @Override
            public void afterVisitContinue(Continue node)
            {

            }

            @Override
            public void afterVisitReturn(Return node)
            {

            }

            @Override
            public void afterVisitThrow(Throw node)
            {

            }

            @Override
            public void afterVisitVariableDeclaration(VariableDeclaration node)
            {

            }

            @Override
            public void afterVisitVariableDefinition(VariableDefinition node)
            {

            }

            @Override
            public void afterVisitVariableDefinitionEntry(VariableDefinitionEntry node)
            {

            }

            @Override
            public void afterVisitTypeVariable(TypeVariable node)
            {

            }

            @Override
            public void afterVisitKeywordModifier(KeywordModifier node)
            {

            }

            @Override
            public void afterVisitModifiers(Modifiers node)
            {

            }

            @Override
            public void afterVisitAnnotation(Annotation node)
            {

            }

            @Override
            public void afterVisitAnnotationElement(AnnotationElement node)
            {

            }

            @Override
            public void afterVisitNormalTypeBody(NormalTypeBody node)
            {

            }

            @Override
            public void afterVisitEnumTypeBody(EnumTypeBody enumTypeBody)
            {

            }

            @Override
            public void afterVisitEmptyDeclaration(EmptyDeclaration node)
            {

            }

            @Override
            public void afterVisitMethodDeclaration(MethodDeclaration node)
            {

            }

            @Override
            public void afterVisitConstructorDeclaration(ConstructorDeclaration node)
            {

            }

            @Override
            public void afterVisitSuperConstructorInvocation(SuperConstructorInvocation node)
            {

            }

            @Override
            public void afterVisitAlternateConstructorInvocation(AlternateConstructorInvocation node)
            {

            }

            @Override
            public void afterVisitInstanceInitializer(InstanceInitializer node)
            {

            }

            @Override
            public void afterVisitStaticInitializer(StaticInitializer node)
            {

            }

            @Override
            public void afterVisitClassDeclaration(ClassDeclaration node)
            {

            }

            @Override
            public void afterVisitInterfaceDeclaration(InterfaceDeclaration node)
            {

            }

            @Override
            public void afterVisitEnumDeclaration(EnumDeclaration node)
            {

            }

            @Override
            public void afterVisitEnumConstant(EnumConstant node)
            {

            }

            @Override
            public void afterVisitAnnotationDeclaration(AnnotationDeclaration node)
            {

            }

            @Override
            public void afterVisitAnnotationMethodDeclaration(AnnotationMethodDeclaration node)
            {

            }

            @Override
            public void afterVisitCompilationUnit(CompilationUnit node)
            {

            }

            @Override
            public void afterVisitPackageDeclaration(PackageDeclaration node)
            {

            }

            @Override
            public void afterVisitImportDeclaration(ImportDeclaration node)
            {

            }

            @Override
            public void afterVisitParseArtefact(Node node)
            {

            }

            @Override
            public void afterVisitComment(Comment node)
            {

            }
        };
    }
}
