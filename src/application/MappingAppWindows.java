package application;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import model.MappingModel;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

/**
 * @Author wutailong11062
 * @Date 2022/1/15 4:12 下午
 * @Version 1.0
 */
public class MappingAppWindows extends JPanel {

    private static JEditorPane editorPane;

    private static final String MAPPING_RESULT_MODEL = "@Mapping(target = \"#t#\", source = \"#s#\")";

    private static int selectCount = 0;

    private static final int SOURCE = 0;

    private static final int TARGET = 1;

    /**
     * source
     */
    private static final TreeSelectionListener leftTreeListener = e -> {
        treeSelectEditorHandler(e, SOURCE);
    };

    /**
     * target
     */
    private static final TreeSelectionListener rightTreeListener = e -> {
        treeSelectEditorHandler(e, TARGET);
    };

    private static ActionListener outputListener;

    public static void setOutputListener(ActionListener listener) {
        outputListener = listener;
    }

    public static void startMapping(PsiClass sourceClazz, PsiClass targetClazz) {
        if (sourceClazz == null || targetClazz == null) {
            return;
        }
        createAndShowGUI(sourceClazz, targetClazz);
    }

    private static void treeSelectEditorHandler(TreeSelectionEvent e, int flag) {
        String replaceFlag = SOURCE == flag ? "#s#" : "#t#";
        TreePath path = e.getPath();
        DefaultMutableTreeNode lastPathComponent = (DefaultMutableTreeNode) path.getLastPathComponent();
        MappingModel model = (MappingModel) lastPathComponent.getUserObject();

        String fieldName = StringUtils.isBlank(model.getParentFieldName()) ? model.getFieldName() : model.getParentFieldName().concat(".").concat(model.getFieldName());

        if (StringUtils.isNotBlank(editorPane.getText())
                && !editorPane.getText().contains(replaceFlag)
                && editorPane.getText().contains(SOURCE == flag ? "#t#" : "#s#")) {
            cancelText();
            editorPane.setText(editorPane.getText() + MAPPING_RESULT_MODEL.replace(replaceFlag, fieldName) + "\n");
            return;
        }

        selectCount++;
        if (StringUtils.isBlank(editorPane.getText())) {
            editorPane.setText(MAPPING_RESULT_MODEL.replace(replaceFlag, fieldName) + "\n");
            return;
        }

        if (selectCount % 2 == 1) {
            editorPane.setText(editorPane.getText() + MAPPING_RESULT_MODEL.replace(replaceFlag, fieldName) + "\n");
            return;
        }
        editorPane.setText(editorPane.getText().replace(replaceFlag, fieldName));
    }


    private static void createAndShowGUI(PsiClass sourceClazz, PsiClass targetClazz) {
//        try {
//            UIManager.setLookAndFeel(
//                    UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception e) {
//            System.err.println("Couldn't use system look and feel.");
//        }

        //Create and set up the window.
        JFrame frame = buildFrame();
        JPanel demo = new JPanel(new BorderLayout());

        //Add the scroll panes to a split pane.
        //Create the scroll pane and add the tree to it.
        JScrollPane sourceTreeView = new JScrollPane(createTree(sourceClazz, leftTreeListener));

        // Create target tree
        JScrollPane targetTreeView = new JScrollPane(createTree(targetClazz, rightTreeListener));

        Dimension minimumSize = new Dimension(50, 300);
        sourceTreeView.setMinimumSize(minimumSize);
        targetTreeView.setMinimumSize(minimumSize);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sourceTreeView, targetTreeView);
        splitPane.setResizeWeight(0.5);
        splitPane.setOneTouchExpandable(true);
        splitPane.setContinuousLayout(true);
        splitPane.setDividerLocation(450);
        demo.add(splitPane, BorderLayout.CENTER);

        createResetButton(demo);
        createPrintResult(demo);
        createCancelButton(demo);
        createWriteButton(demo);
        frame.add(demo);
    }


    private static void createPrintResult(JPanel demo) {
        JPanel panel1 = new JPanel(new BorderLayout(1, 0));
        editorPane = new JEditorPane();
        editorPane.setEnabled(true);
        panel1.add(editorPane);
        demo.add(panel1, BorderLayout.SOUTH);
    }

    private static void createResetButton(JPanel demo) {
        JPanel panel = new JPanel(new BorderLayout(0, 1));
        JButton reset = new JButton();
        reset.setText("reset");
        panel.setPreferredSize(new Dimension(50, 600));
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorPane.setText("");
                selectCount = 0;
            }
        });
        panel.add(reset);
        demo.add(panel, BorderLayout.WEST);
    }

    private static void createCancelButton(JPanel demo) {
        JPanel panel = new JPanel(new BorderLayout(0, 1));
        JButton cancel = new JButton();
        cancel.setText("revoke");
        panel.setPreferredSize(new Dimension(50, 600));
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelText();
            }
        });
        panel.add(cancel);
        demo.add(panel, BorderLayout.EAST);
    }

    private static void createWriteButton(JPanel demo) {
        JPanel panel = new JPanel(new BorderLayout(0, 1));
        JButton cancel = new JButton();
        cancel.setText("output");
        panel.setPreferredSize(new Dimension(1000, 30));
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (outputListener !=  null) {
                    outputListener.actionPerformed(e);
                }
            }
        });
        panel.add(cancel);
        demo.add(panel, BorderLayout.NORTH);
    }


    private static void cancelText() {
        ArrayList<String> mapText = Lists.newArrayList(editorPane.getText().split("\n"));
        mapText.remove(mapText.size() - 1);
        String join = String.join("\n", mapText);
        if (StringUtils.isBlank(join)) {
            editorPane.setText(join);
            return;
        }
        editorPane.setText(join + "\n");
    }

    private static JTree createTree(PsiClass clazz, TreeSelectionListener listener) {
        MappingModel root = new MappingModel();
        root.setFieldName("root");
        root.setFieldType(clazz.getQualifiedName());
        root.setParentFieldName("");
        //Create the nodes.
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(root);
        createNodes(top, clazz, true);
        //Create a tree that allows one selection at a time.
        JTree tree = new JTree(top);
        tree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);
        //Listen for when the selection changes.
        tree.addTreeSelectionListener(listener);
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) tree.getCellRenderer();

        try {
            Icon LeafIcon = new ImageIcon(new URL("https://m.hellobike.com/resource/helloyun/11062/TGRDH_f9e0f9ec884c1574141ae3c3492c7360%20(1).jpeg?x-oss-process=image/quality,q_80"));
            Icon closeIcon = new ImageIcon(new URL("https://m.hellobike.com/resource/helloyun/11062/19mVQ_src=http___images.669pic.com_element_pic_59_58_39_69_24d50961330b6cf46b9818ae916a97dc.jpg!w700wb&amp;refer=http___images.669pic.jpeg?x-oss-process=image/quality,q_80"));
            Icon openIcon = new ImageIcon(new URL("https://m.hellobike.com/resource/helloyun/11062/k-zqs_src=http___images.669pic.com_element_pic_59_58_39_69_24d50961330b6cf46b9818ae916a97dc.jpg!w700wb&amp;refer=http___images.669pic.jpeg?x-oss-process=image/quality,q_80"));
            // 设置用于表示展开的非叶节点的图标。
            renderer.setOpenIcon(openIcon);
            // 设置用于表示未展开的非叶节点的图标。
            renderer.setClosedIcon(closeIcon);
            // 设置用于表示叶节点的图标。
            renderer.setLeafIcon(LeafIcon);
        } catch (Exception e) {
            // ignore
        }

        return tree;
    }

    private static void createNodes(DefaultMutableTreeNode top, PsiClass psiClass, boolean startRoot) {
        List<PsiField> fieldList = new ArrayList<>(Arrays.asList(psiClass.getFields()));

        // super class fields
        PsiClass superClass = psiClass.getSuperClass();
        while (superClass != null && !Object.class.getCanonicalName().equals(superClass.getQualifiedName())) {
            fieldList.addAll(Arrays.asList(superClass.getFields()));
            superClass = superClass.getSuperClass();
        }

        for (PsiField psiField : fieldList) {
            // final不可修改不展示
            if (psiField.getModifierList() != null
                    && psiField.getModifierList().getText().contains("final")) {
                continue;
            }

            // 枚举常量不展示
            if (psiField instanceof PsiEnumConstant) {
                continue;
            }

            assert psiField.getTypeElement() != null;
            try {
                MappingModel topUserObject = ((MappingModel) top.getUserObject());
                String parentFieldName = "";
                if (!startRoot) {
                    if (StringUtils.isEmpty(topUserObject.getParentFieldName())) {
                        parentFieldName = topUserObject.getFieldName();
                    } else {
                        parentFieldName = topUserObject.getParentFieldName() + "." + topUserObject.getFieldName();
                    }
                }

                if (isNormalType(psiField.getTypeElement())) {
                    MappingModel childModel = new MappingModel(psiField.getName(), psiField.getTypeElement().getText(), parentFieldName);
                    top.add(new DefaultMutableTreeNode(childModel));
                    continue;
                }

                String qualifiedName = Optional.ofNullable(psiField.getTypeElement()).orElseThrow().getType().getCanonicalText();
                PsiClass nodeClass = JavaPsiFacade.getInstance(psiField.getProject()).findClass(qualifiedName, GlobalSearchScope.allScope(psiField.getProject()));

                MappingModel nodeModel = new MappingModel(psiField.getName(), psiField.getTypeElement().getText(), parentFieldName);
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(nodeModel);
                top.add(node);
                assert nodeClass != null;

                createNodes(node, nodeClass, false);

            } catch (Exception e) {
                System.err.println(e.getMessage() + "\n" + psiField.getType() + "\n" + psiField.getTypeElement().getText() + "\n" + psiClass.getQualifiedName());
            }
        }

    }

    /**
     * 是否是一些通用类型
     *
     * @param fieldElement
     * @return
     */
    private static boolean isNormalType(PsiTypeElement fieldElement) {
        PsiType fieldType = fieldElement.getType();
        return fieldType.getCanonicalText().equals(String.class.getCanonicalName())
                || fieldType.getCanonicalText().equals((BigDecimal.class.getCanonicalName()))
                || fieldType.getCanonicalText().equals((Byte.class.getCanonicalName()))
                || fieldType.getCanonicalText().equals((Short.class.getCanonicalName()))
                || fieldType.getCanonicalText().equals((Character.class.getCanonicalName()))
                || fieldType.getCanonicalText().equals((Integer.class.getCanonicalName()))
                || fieldType.getCanonicalText().equals((Float.class.getCanonicalName()))
                || fieldType.getCanonicalText().equals((Long.class.getCanonicalName()))
                || fieldType.getCanonicalText().equals((Double.class.getCanonicalName()))
                || fieldType.getCanonicalText().equals((Boolean.class.getCanonicalName()))
                || fieldType.getCanonicalText().contains((List.class.getCanonicalName()))
                || fieldType.getCanonicalText().contains((Set.class.getCanonicalName()))
                || fieldType.getCanonicalText().contains((Map.class.getCanonicalName()))
                || fieldType.getCanonicalText().equals((LocalDateTime.class.getCanonicalName()))
                || fieldType.getCanonicalText().equals((LocalDate.class.getCanonicalName()))
                || fieldType.getCanonicalText().equals((Time.class.getCanonicalName()))
                || fieldType.getCanonicalText().equals((Timestamp.class.getCanonicalName()))
                || fieldType.getCanonicalText().equals((Date.class.getCanonicalName()))
                || fieldType.getCanonicalText().equals((JSONArray.class.getCanonicalName()))
                || fieldType.getCanonicalText().equals((JSONObject.class.getCanonicalName()))
                || fieldElement.getText().contains("[]")
                || fieldType.isAssignableFrom(PsiType.BYTE)
                || fieldType.isAssignableFrom(PsiType.INT)
                || fieldType.isAssignableFrom(PsiType.CHAR)
                || fieldType.isAssignableFrom(PsiType.DOUBLE)
                || fieldType.isAssignableFrom(PsiType.FLOAT)
                || fieldType.isAssignableFrom(PsiType.LONG)
                || fieldType.isAssignableFrom(PsiType.SHORT)
                || fieldType.isAssignableFrom(PsiType.BOOLEAN)
                ;

    }


    private static JFrame buildFrame() {
        JFrame jFrame = new JFrame();
        //设置窗体的大小
        jFrame.setSize(1000, 600);
        //设置窗体标题
        jFrame.setTitle("Mapping代码生成器");
        //用户单击窗口的关闭按钮时程序执行的操作
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //设置窗体可见
        jFrame.setVisible(true);
        jFrame.setLocationRelativeTo(null);
        return jFrame;
    }

    public static String getEditorPaneContent() {
        return editorPane.getText();
    }
}

