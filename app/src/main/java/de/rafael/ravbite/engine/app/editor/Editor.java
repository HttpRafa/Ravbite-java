/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-", "$today.year")2022. All rights reserved.
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

package de.rafael.ravbite.engine.app.editor;

//------------------------------
//
// This class was developed by Rafael K.
// On 5/21/2022 at 8:18 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.app.editor.element.ErrorElement;
import de.rafael.ravbite.engine.app.editor.element.TaskPopup;
import de.rafael.ravbite.engine.app.editor.manager.element.ElementManager;
import de.rafael.ravbite.engine.app.editor.manager.project.ProjectManager;
import de.rafael.ravbite.engine.app.editor.task.TaskExecutor;
import de.rafael.ravbite.engine.app.editor.window.EditorWindow;
import imgui.ImGui;
import org.apache.commons.lang3.ArrayUtils;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

public class Editor {

    public static final String GRADLE_DOWNLOAD_URL = "https://services.gradle.org/distributions/gradle-7.4.2-bin.zip";

    private static Editor instance = null;

    private EditorWindow editorWindow;
    private Thread windowThread;

    private Throwable[] reportedErrors = new Throwable[0];

    private final ThreadPoolExecutor threadPoolExecutor = new ScheduledThreadPoolExecutor(8);
    private final TaskExecutor taskExecutor;

    private final ElementManager elementManager;
    private final ProjectManager projectManager;

    public Editor() {
        instance = this;

        elementManager = new ElementManager();
        projectManager = new ProjectManager(this);
        taskExecutor = new TaskExecutor(this);
    }

    /**
     * Executes the last tasks of the editor
     */
    public void exit() {
        threadPoolExecutor.shutdown();
    }

    /**
     * Updates the editor
     */
    public void update() {
        projectManager.update();
    }

    /**
     * Loads all projects etc.
     */
    public void load() {
        elementManager.startDrawing(new ErrorElement(this));
        elementManager.startDrawing(new TaskPopup(this));
        projectManager.loadProjects();

        update();
    }

    /**
     * Start the editor process
     */
    public void start() {
        windowThread = new Thread(() -> {
            editorWindow = new EditorWindow(this);
            editorWindow.initialize();
            editorWindow.loop();
            editorWindow.destroy();
            exit();
        }, "Window Thread");
        windowThread.start();
        threadPoolExecutor.execute(this::load);
    }

    /**
     * Called if an exception is thrown
     * @param throwable Exception
     */
    public void handleError(Throwable throwable) {
        reportedErrors = ArrayUtils.add(reportedErrors, throwable);

        editorWindow.getThreadExecutor().addTask(() -> {
            ImGui.openPopup("Reported errors");
        });
    }

    /**
     * Removes a reportedError
     * @param index Index of the error
     */
    public void solveError(int index) {
        reportedErrors = ArrayUtils.remove(reportedErrors, index);
    }

    /**
     * Removes all reportedError
     */
    public void solveAllErrors() {
        reportedErrors = new Throwable[0];
    }

    /**
     * @return Window of the editor
     */
    public EditorWindow getEditorWindow() {
        return editorWindow;
    }

    /**
     * @return Thread of the window
     */
    public Thread getWindowThread() {
        return windowThread;
    }

    /**
     * @return Array of reportedErrors
     */
    public Throwable[] getReportedErrors() {
        return reportedErrors;
    }

    /**
     * @return Thread pool for the editor
     */
    public ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    /**
     * @return Running tasks manager
     */
    public TaskExecutor getTaskExecutor() {
        return taskExecutor;
    }

    /**
     * @return Element manager
     */
    public ElementManager getElementManager() {
        return elementManager;
    }

    /**
     * @return Project manager
     */
    public ProjectManager getProjectManager() {
        return projectManager;
    }

    /**
     * @return Instance of the editor
     */
    public static Editor getInstance() {
        return instance;
    }

}
