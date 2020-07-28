package com.mb.action;

import com.intellij.execution.RunManager;
import com.intellij.execution.application.ApplicationConfiguration;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import com.mb.utils.ZipUtils;
import icons.AddJvmParamsIcon;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.net.URL;


public class AddJvmParamsAction extends AnAction {
    private boolean pressed = false;

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        URL url = ZipUtils.class.getResource("/jvm/demo.zip");
        String path = url.getPath();
        int index = path.lastIndexOf("/jvm/demo.zip");
        path = path.substring(6, index - 1);
        index = path.lastIndexOf("/");
        String destPath = path.substring(0, index + 1);
//        JOptionPane.showMessageDialog(null, path);
        try {
            ZipUtils.unZipFiles(path, destPath);
            ZipUtils.unZipFiles(destPath + "jvm/demo.zip", destPath + "jvm/");
            String jvmPath = destPath + "jvm/doom-jar";
            Project project = anActionEvent.getProject();
            for (RunConfiguration runConfiguration : RunManager.getInstance(project).getAllConfigurationsList()) {
                if (runConfiguration instanceof ApplicationConfiguration) {
                    ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) runConfiguration;
                    Presentation presentation = anActionEvent.getPresentation();
                    if (!pressed) {
                        applicationConfiguration.setVMParameters("-javaagent:" + jvmPath + "/doom-agent.jar=DoomPlugins=acceleratepigeon.jar:RunMode=mock");
                        presentation.setIcon(AddJvmParamsIcon.NotPressedIcon);
//                JOptionPane.showMessageDialog(null,"pressed!"+jvmPath);
                        pressed = true;
                    } else {
                        applicationConfiguration.setVMParameters("");
                        presentation.setIcon(AddJvmParamsIcon.PressedIcon);
//                JOptionPane.showMessageDialog(null,"unPressed!"+"...");
                        pressed = false;
                    }
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}
