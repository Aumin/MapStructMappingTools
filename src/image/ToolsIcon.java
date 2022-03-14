package image;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

public interface ToolsIcon {
    Icon DemoAction = IconLoader.getIcon("/image/icon.svg", ToolsIcon.class);

    String convert(Integer integer);
}
