/*
 * Copyright (c) 2022.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 *         this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in the
 *         documentation and/or other materials provided with the distribution.
 *     * Neither the name of the developer nor the names of its contributors
 *         may be used to endorse or promote products derived from this software
 *         without specific prior written permission.
 *     * Redistributions in source or binary form must keep the original package
 *         and class name.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.rafael.ravbite.engine.utils.debug;

//------------------------------
//
// This class was developed by Rafael K.
// On 04/08/2022 at 7:28 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.components.Component;
import de.rafael.ravbite.engine.graphics.components.transform.Transform;
import de.rafael.ravbite.engine.graphics.object.game.GameObject;
import de.rafael.ravbite.engine.graphics.object.game.material.IMaterial;
import de.rafael.ravbite.engine.graphics.window.EngineWindow;
import de.rafael.ravbite.engine.utils.debug.windows.EngineDebugOptionsWindow;
import de.rafael.ravbite.engine.utils.debug.windows.GameObjectOptionsWindow;
import de.rafael.ravbite.utils.asset.AssetLocation;
import org.lwjgl.glfw.GLFW;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DebugWindow {

    private final EngineWindow engineWindow;

    private List<GameObjectOptionsWindow> gameObjectOptionsWindowList = new ArrayList<>();

    private final EngineDebugOptionsWindow engineDebugOptionsWindow;

    private String imagePath;

    /**
     * Used to test the engine
     * @param engineWindow EngineWindow
     */
    public DebugWindow(EngineWindow engineWindow) {
        this.engineWindow = engineWindow;

        engineDebugOptionsWindow = new EngineDebugOptionsWindow(this);
        engineDebugOptionsWindow.setVisible(true);
    }

    /**
     * Adds a gameObject to debug
     * @param gameObject GameObject
     */
    public void addGameObject(GameObject gameObject) {
        GameObjectOptionsWindow gameObjectOptionsWindow = new GameObjectOptionsWindow(gameObject);
        gameObjectOptionsWindow.setTitle(gameObject.getName());
        gameObjectOptionsWindow.setVisible(true);
        gameObjectOptionsWindow.componentsList.setModel(new javax.swing.AbstractListModel<String>() {
            public int getSize() {
                return gameObject.getObjectComponents().size();
            }
            public String getElementAt(int i) {
                return gameObject.getObjectComponents().get(i).getClass().getSimpleName().replaceAll("Component", "");
            }
        });

        gameObjectOptionsWindowList.add(gameObjectOptionsWindow);

        updateTransformTable(gameObject.getTransform(), gameObjectOptionsWindow.transformTable);
    }

    public void updateTransformTable(Transform transform, JTable table) {
        table.getModel().setValueAt(cutNumber(transform.getPosition().x), 0, 1);
        table.getModel().setValueAt(cutNumber(transform.getPosition().y), 0, 2);
        table.getModel().setValueAt(cutNumber(transform.getPosition().z), 0, 3);

        table.getModel().setValueAt(cutNumber(transform.getRotation().x), 1, 1);
        table.getModel().setValueAt(cutNumber(transform.getRotation().y), 1, 2);
        table.getModel().setValueAt(cutNumber(transform.getRotation().z), 1, 3);

        table.getModel().setValueAt(cutNumber(transform.getScale().x), 2, 1);
        table.getModel().setValueAt(cutNumber(transform.getScale().y), 2, 2);
        table.getModel().setValueAt(cutNumber(transform.getScale().z), 2, 3);
    }

    /**
     * Updates all values in windows
     */
    public void updateGameObjects() {
        for (GameObjectOptionsWindow gameObjectOptionsWindow : gameObjectOptionsWindowList) {
            updateTransformTable(gameObjectOptionsWindow.gameObject.getTransform(), gameObjectOptionsWindow.transformTable);
        }
    }

    public void loadIntoOpenGL() {
        if(imagePath != null) {
            engineWindow.getThreadExecutor().addTask(() -> {
                try {
                    int textureId = engineWindow.getGLUtils().rbLoadTexture(AssetLocation.create(imagePath, AssetLocation.EXTERNAL));
                    engineDebugOptionsWindow.textureIdLabel.setText(textureId + "");
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            });
        }
    }

    public void openTextureSelection() {
        JFileChooser jFileChooser = new JFileChooser(".");
        jFileChooser.setDialogTitle("Select texture file");
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG image", "png");
        jFileChooser.setFileFilter(filter);
        int returnValue = jFileChooser.showOpenDialog(engineDebugOptionsWindow);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser.getSelectedFile();
            try {
                BufferedImage loadedImage = ImageIO.read(file);
                imagePath = file.getPath();

                engineDebugOptionsWindow.textureLabel.setIcon(new ImageIcon(loadedImage));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
    /**
     * Cuts the number
     * @param number Number to cut
     * @return String to display
     */
    public String cutNumber(float number) {
        String[] numberString = String.valueOf(number).split("\\.");
        return numberString[0] + "." + numberString[1].substring(0, Math.min(2, numberString[1].length()));
    }

    /**
     * @return EngineWindow
     */
    public EngineWindow getEngineWindow() {
        return engineWindow;
    }

}
