package com.xeros.plugins.converters

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaTokenType
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiJavaToken
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset
import io.xeros.config.ItemsReflection

class ItemIdConverter : PsiElementBaseIntentionAction(), IntentionAction {

    override fun getFamilyName(): String = "Xeros plugins"

    override fun getText(): String = "Convert Item Id to named equivalent"

    override fun startInWriteAction(): Boolean = true

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        if (element is PsiJavaToken) {
            if (element.tokenType == JavaTokenType.INTEGER_LITERAL) {
                return ItemsReflection.contains(element.text.toInt())
            }
        }
        return true
    }

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        val document= editor?.document ?: return

        WriteCommandAction.runWriteCommandAction(project) {
            val named = ItemsReflection.getIdsToNames()[element.text.toInt()]
            val full = "Items.$named"
            document.replaceString(element.startOffset, element.endOffset, full)
        }
    }

}