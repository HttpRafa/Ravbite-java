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
import de.rafael.ravbite.engine.graphics.components.mesh.MeshComponent;
import de.rafael.ravbite.engine.graphics.components.transform.Transform;
import de.rafael.ravbite.engine.graphics.object.game.GameObject;
import de.rafael.ravbite.engine.graphics.object.game.material.standard.Material;
import de.rafael.ravbite.engine.graphics.object.game.mesh.Mesh;
import de.rafael.ravbite.engine.graphics.window.EngineWindow;
import de.rafael.ravbite.utils.asset.AssetLocation;
import org.lwjgl.glfw.GLFW;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class DebugWindow {

    private final EngineWindow engineWindow;

    private final HashMap<GameObject, GameObjectDebugWindow> debugGameObjects = new HashMap<>();

    private final JFrame engineTestFrame;

    /**
     * Used to test the engine
     * @param engineWindow EngineWindow
     */
    public DebugWindow(EngineWindow engineWindow) {
        this.engineWindow = engineWindow;

        this.engineTestFrame = new JFrame("Ravbite Debug / Engine");
        this.engineTestFrame.setSize(350, 80);
        this.engineTestFrame.setResizable(false);

        JPanel jPanel = new JPanel();

        JTextField textureIdLabel = new JTextField("null");
        textureIdLabel.setEditable(false);
        jPanel.add(textureIdLabel);
        JButton loadTextureButton = new JButton("Load Texture");
        loadTextureButton.addActionListener(actionEvent -> {
            JFileChooser jFileChooser = new JFileChooser(".");
            jFileChooser.setDialogTitle("Select texture file");
            jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG image", "png");
            jFileChooser.setFileFilter(filter);
            int returnValue = jFileChooser.showOpenDialog(jPanel);

            if(returnValue == JFileChooser.APPROVE_OPTION) {
                File file = jFileChooser.getSelectedFile();

                engineWindow.getThreadExecutor().addTask(() -> {
                    try {
                        int textureId = engineWindow.getGLUtils().rbLoadTexture(AssetLocation.create(file.getAbsolutePath(), AssetLocation.EXTERNAL));
                        textureIdLabel.setText(textureId + "");
                    } catch (IOException exception) {
                        exception.printStackTrace();
                        System.exit(0);
                    }
                });
            }
        });
        jPanel.add(loadTextureButton);

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(actionEvent -> {
            engineWindow.getThreadExecutor().addTask(() -> {
                GLFW.glfwSetWindowShouldClose(engineWindow.getWindow(), true);
            });
        });
        jPanel.add(exitButton);

        this.engineTestFrame.add(jPanel);
    }

    /**
     * Adds a gameObject to debug
     * @param gameObject GameObject
     */
    public void addGameObject(GameObject gameObject) {
        JFrame gameObjectFrame = new JFrame("Ravbite Debug / " + gameObject.getName());
        gameObjectFrame.setSize(600, 400);
        gameObjectFrame.setIconImage(engineTestFrame.getIconImage());

        JPanel jPanel = new JPanel();

        JLabel transformXValueLocal = new JLabel("000");
        jPanel.add(new JLabel("Local XYZ ("));
        jPanel.add(transformXValueLocal);
        JLabel transformYValueLocal = new JLabel("000");
        jPanel.add(transformYValueLocal);
        JLabel transformZValueLocal = new JLabel("000");
        jPanel.add(transformZValueLocal);

        JButton changeTransformLocal = new JButton("Change XYZ");
        changeTransformLocal.addActionListener(actionEvent -> {
            JTextField xValue = new JTextField(transformXValueLocal.getText());
            JTextField yValue = new JTextField(transformYValueLocal.getText());
            JTextField zValue = new JTextField(transformZValueLocal.getText());
            final JComponent[] inputs = new JComponent[] {
                    new JLabel("X"),
                    xValue,
                    new JLabel("Y"),
                    yValue,
                    new JLabel("Z"),
                    zValue
            };
            int result = JOptionPane.showConfirmDialog(gameObjectFrame, inputs, "Change local XYZ", JOptionPane.DEFAULT_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                gameObject.getTransform().getPosition().set(new float[] {Float.parseFloat(xValue.getText()), Float.parseFloat(yValue.getText()), Float.parseFloat(zValue.getText())});
            }
        });
        jPanel.add(changeTransformLocal);
        jPanel.add(new JLabel(") /"));

        JLabel transformXValue = new JLabel("000");
        jPanel.add(new JLabel("World XYZ ("));
        jPanel.add(transformXValue);
        JLabel transformYValue = new JLabel("000");
        jPanel.add(transformYValue);
        JLabel transformZValue = new JLabel("000");
        jPanel.add(transformZValue);
        jPanel.add(new JLabel(")"));

        Optional<Component> component = gameObject.hasComponent(MeshComponent.class);
        if(component.isPresent()) {
            MeshComponent meshComponent = (MeshComponent) component.get();

            String[][] data = new String[meshComponent.getMesh().collectMeshes().length][6];
            Mesh[] meshes = meshComponent.getMesh().collectMeshes();
            for (int i = 0; i < meshes.length; i++) {
                Mesh mesh = meshes[i];
                data[i][0] = mesh.getName();
                data[i][1] = mesh.getVertices().length + "";
                data[i][2] = mesh.getNormals().length + "";
                data[i][3] = mesh.getTangents().length + "";
                data[i][4] = mesh.getTextureCoords().length + "";
                data[i][5] = mesh.getIndices().length + "";
            }
            String[] column = new String[]{"Name", "Positions", "Normals", "Tangents", "TextureCoords", "Indices"};

            JTable meshesTable = new JTable(data, column);
            meshesTable.setBounds(40,40,500,300);
            JScrollPane jScrollPane = new JScrollPane(meshesTable);
            jPanel.add(jScrollPane);
        }

        /*JButton changeTextureId = new JButton("Change");
        changeTextureId.addActionListener(actionEvent -> {
            String input = JOptionPane.showInputDialog(gameObjectFrame, "Change textureId");
            Optional<Component> component = gameObject.hasComponent(MaterialComponent.class);
            if(component.isPresent()) {
                MaterialComponent materialComponent = (MaterialComponent) component.get();
                if(materialComponent.getMaterial() instanceof Material material) {
                    material.getAlbedo().texture(Integer.parseInt(input));
                }
            }
        });
        jPanel.add(changeTextureId);

         */

        gameObjectFrame.add(jPanel);
        gameObjectFrame.setVisible(true);
        this.debugGameObjects.put(gameObject, new GameObjectDebugWindow(gameObjectFrame, transformXValueLocal, transformYValueLocal, transformZValueLocal, transformXValue, transformYValue, transformZValue));
    }

    /**
     * Updates all values in windows
     */
    public void updateGameObjects() {
        for (GameObject gameObject : debugGameObjects.keySet()) {
            GameObjectDebugWindow window = debugGameObjects.get(gameObject);

            /*Optional<Component> component = gameObject.hasComponent(MaterialComponent.class);
            if(component.isPresent()) {
                MaterialComponent materialComponent = (MaterialComponent) component.get();
                if(materialComponent.getMaterial() instanceof Material material) {
                    window.materialAlbedoTextureId.setText(material.getAlbedo().getTextureId() + "");
                }
            }

             */

            Transform worldTransform = gameObject.getSpecialTransform(Transform.WORLD_SPACE);
            window.transformXValue.setText(cutNumber(worldTransform.getPosition().x));
            window.transformYValue.setText(cutNumber(worldTransform.getPosition().y));
            window.transformZValue.setText(cutNumber(worldTransform.getPosition().z));

            window.transformXValueLocal.setText(cutNumber(gameObject.getTransform().getPosition().x));
            window.transformYValueLocal.setText(cutNumber(gameObject.getTransform().getPosition().y));
            window.transformZValueLocal.setText(cutNumber(gameObject.getTransform().getPosition().z));

            window.jFrame.repaint();
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

    /**
     * @return Engine debugWindow
     */
    public JFrame getFrame() {
        return engineTestFrame;
    }

    public record GameObjectDebugWindow(JFrame jFrame, JLabel transformXValueLocal,
                                        JLabel transformYValueLocal, JLabel transformZValueLocal, JLabel transformXValue,
                                        JLabel transformYValue, JLabel transformZValue) {}

}
