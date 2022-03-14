
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import model.Const;
import application.MappingAppWindows;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;


public class MapStructMappingToolsAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent actionEvent) {
        DataContext dataContext = actionEvent.getDataContext();
        PsiJavaFile psiFileData = (PsiJavaFile) CommonDataKeys.PSI_FILE.getData(dataContext);
        assert psiFileData != null;

        Editor editor = actionEvent.getRequiredData(CommonDataKeys.EDITOR);
        if (StringUtils.isBlank(editor.getSelectionModel().getSelectedText())) {
            return;
        }

        List<PsiElement> psiElements = Arrays.asList(psiFileData.getChildren());
        Optional<PsiElement> elementOptional = psiElements.stream().filter(element -> element instanceof PsiClass).findFirst();
        assert elementOptional.isPresent();

        PsiClass psiClass = (PsiClass) elementOptional.get();
        if (!checkDisplay(psiClass)) {
            Messages.showErrorDialog(actionEvent.getProject(),
                    Const.ERROR_MSG_1,
                    Const.ERROR_TITLE);
            return;
        }

        Project project = actionEvent.getProject();
        assert project != null;
        Optional<PsiElement> psiKeyWords = Arrays.stream(psiClass.getChildren()).filter(element -> element instanceof PsiKeyword).findFirst();
        assert psiKeyWords.isPresent();

        String source = "";
        String target = "";

        String selectedTexts = editor.getSelectionModel()
                .getSelectedText().trim();
        for (PsiMethod method : psiClass.getMethods()) {
            PsiTypeElement returnTypeElement = Optional.ofNullable(method.getReturnTypeElement()).orElseThrow();
            PsiIdentifier methodNameElement = Optional.ofNullable(method.getNameIdentifier()).orElseThrow();
            String params = method.getParameterList().getText();
            if (!selectedTexts.contains(returnTypeElement.getText())
                    || !selectedTexts.contains(methodNameElement.getText())
                    || !selectedTexts.contains(params)) {
                continue;
            }

            // 处理@MappingTarget
            for (PsiParameter parameter : method.getParameterList().getParameters()) {
                if (returnTypeElement.getType().isAssignableFrom(PsiType.VOID)
                        && parameter.getModifierList() != null
                        && parameter.getModifierList().getText().equals(Const.MAPPER_TARGET)) {

                    if (method.getParameterList().getParameters().length > 2) {
                        Messages.showErrorDialog(actionEvent.getProject(),
                                Const.ERROR_MSG_2,
                                Const.ERROR_TITLE);
                        return;
                    }
                    source = Optional.ofNullable(parameter.getTypeElement())
                            .map(PsiTypeElement::getInnermostComponentReferenceElement)
                            .orElseThrow()
                            .getQualifiedName();
                    continue;
                }

                target = Optional.ofNullable(parameter.getTypeElement())
                        .map(PsiTypeElement::getInnermostComponentReferenceElement)
                        .orElseThrow()
                        .getQualifiedName();
            }

            // 参数中没有@MappingTarget注解
            if (StringUtils.isEmpty(source)) {
                if (method.getParameterList().getParameters().length > 1) {
                    Messages.showErrorDialog(actionEvent.getProject(),
                            Const.ERROR_MSG_2,
                            Const.ERROR_TITLE);
                    return;
                }
                source = Optional.ofNullable(returnTypeElement.getInnermostComponentReferenceElement()).map(PsiJavaCodeReferenceElement::getQualifiedName).orElseThrow();
            }

        }

        if (source.isEmpty() || target.isEmpty()) {
            Messages.showErrorDialog(actionEvent.getProject(),
                    Const.ERROR_MSG_3,
                    Const.ERROR_TITLE);
            return;
        }

        PsiClass sourcePsiClass = JavaPsiFacade.getInstance(project).findClass(source, GlobalSearchScope.allScope(project));
        PsiClass targetPsiClass = JavaPsiFacade.getInstance(project).findClass(target, GlobalSearchScope.allScope(project));

        // 展示窗口
        MappingAppWindows.startMapping(sourcePsiClass, targetPsiClass);
        // 填充映射代码
        MappingAppWindows.setOutputListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputPsiFile(project, editor, psiFileData);
            }
        });
    }

    @Override
    public void update(@NotNull AnActionEvent actionEvent) {

    }

    private boolean checkDisplay(PsiClass psiClass) {
        assert psiClass != null;
        for (PsiElement element : psiClass.getChildren()) {
            if (element instanceof PsiModifierList) {
                // 有@Mapper注解的才能使用
                return element.getText().contains(Const.MAPPER_MODIFIER);
            }
        }
        return false;
    }


    private void outputPsiFile(Project project, Editor editor, PsiJavaFile psiFileData) {
        WriteCommandAction.runWriteCommandAction(project, new Runnable() {
            @Override
            public void run() {
                String editorPaneContent = MappingAppWindows.getEditorPaneContent();
                if (StringUtils.isEmpty(editorPaneContent)) {
                    return;
                }
                int offset = editor.getSelectionModel().getSelectionStart();
                StringJoiner stringJoiner = new StringJoiner("\n", "", "\n");
                String[] mappingCodes = editorPaneContent.split("\n");
                for (String code : mappingCodes) {
                    if (StringUtils.isBlank(code)) {
                        continue;
                    }
                    stringJoiner.add(Const.FOUR_BLACK + code);
                }
                editor.getDocument().insertString(offset, stringJoiner.toString());
                // 格式化代码
//                CodeStyleManager.getInstance(project).reformat(psiFileData);
            }
        });
    }

}
