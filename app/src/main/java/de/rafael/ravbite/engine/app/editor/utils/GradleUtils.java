/*
 * Copyright (c) 2022. All rights reserved.
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

package de.rafael.ravbite.engine.app.editor.utils;

//------------------------------
//
// This class was developed by Rafael K.
// On 05/27/2022 at 2:03 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.app.editor.Editor;
import de.rafael.ravbite.engine.app.editor.project.SimpleProject;
import de.rafael.ravbite.engine.app.editor.project.settings.GradleSettings;
import de.rafael.ravbite.engine.app.editor.task.EditorTask;
import de.rafael.ravbite.engine.app.editor.task.types.download.DownloadEditorTask;
import de.rafael.ravbite.engine.app.editor.task.types.zip.ZipEditorTask;
import de.rafael.ravbite.utils.asset.AssetLocation;
import de.rafael.ravbite.utils.system.OperationSystem;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class GradleUtils {

    public static final String GRADLE_VERSION = "7.4.2";
    public static final String GRADLE_DOWNLOAD = "https://services.gradle.org/distributions/gradle-" + GRADLE_VERSION + "-bin.zip";

    public static final String[] dependencies = new String[] {
            "files(\"libs/engine.jar\", \"libs/utilities.jar\")",
            "platform(\"org.lwjgl:lwjgl-bom:3.3.1\")",
            "\"org.lwjgl:lwjgl\"",
            "\"org.lwjgl:lwjgl-assimp\"",
            "\"org.lwjgl:lwjgl-glfw\"",
            "\"org.lwjgl:lwjgl-openal\"",
            "\"org.lwjgl:lwjgl-opengl\"",
            "\"org.lwjgl:lwjgl-stb\"",
            "\"org.joml:joml:1.10.2\"",
            "\"com.github.stephengold:jbullet:1.0.2\"",
            "\"org.apache.commons:commons-lang3:3.5\"",
            "\"org.apache.commons:commons-io:1.3.2\"",
            "\"com.google.code.gson:gson:2.7\""
    };

    private static final File binariesDirectory = new File("binaries/");
    private static final File gradleDirectory = new File(binariesDirectory, "gradle-" + GRADLE_VERSION + "/");
    private static final File gradleBinDirectory = new File(gradleDirectory, "bin/");

    private static final File tempGradleDownloadFile = new File(binariesDirectory, "gradle.zip");

    public static boolean isInstalled() {
        return gradleDirectory.exists() && gradleBinDirectory.exists();
    }

    public static EditorTask installGradle() {
        return new EditorTask("Installing gradle...")
                .add(new EditorTask("Creating directory...", binariesDirectory::mkdirs))
                .add(new DownloadEditorTask("Downloading gradle from %url%...", GRADLE_DOWNLOAD, watchedInputStream -> Files.copy(watchedInputStream, tempGradleDownloadFile.toPath(), StandardCopyOption.REPLACE_EXISTING)))
                .add(new ZipEditorTask("Unpacking the zip...", 0, tempGradleDownloadFile, binariesDirectory))
                .add(new EditorTask("Cleaning up...", tempGradleDownloadFile::delete));
    }

    public static Process executeGradle(File directory, String... arguments) {
        try {
            OperationSystem.OSType osType = OperationSystem.getOperatingSystemType();

            Process process;
            if(osType == OperationSystem.OSType.Windows) {
                String[] processData = new String[] {new File(gradleBinDirectory, "gradle.bat").getAbsolutePath()};
                processData = ArrayUtils.addAll(processData, arguments);
                ProcessBuilder processBuilder = new ProcessBuilder(processData);
                processBuilder.directory(directory);
                process = processBuilder.start();
            } else {
                String[] processData = new String[] {"sh " + new File(gradleBinDirectory, "gradle").getAbsolutePath()};
                processData = ArrayUtils.addAll(processData, arguments);
                ProcessBuilder processBuilder = new ProcessBuilder(processData);
                processBuilder.directory(directory);
                process = processBuilder.start();
            }

            return process;
        } catch (Exception exception) {
            Editor.getInstance().handleError(exception);
            return null;
        }
    }

    public static void stopGradleDaemon() {
        executeGradle(new File("."), "--stop");
    }

    public static void createGradleProject(File srcDirectory, SimpleProject simpleProject, GradleSettings gradleSettings) {
        List<String> arguments = new ArrayList<>();
        arguments.add("init");
        arguments.add("--project-name");
        arguments.add(simpleProject.getName());
        arguments.add("--package");
        arguments.add(gradleSettings.getPackageName());

        arguments.add("--type");
        int language = gradleSettings.getLanguage();
        String lang = "java";
        if(language == 1) {
            lang = "kotlin";
        } else if(language == 2) {
            lang = "groovy";
        }
        arguments.add(lang + "-library");

        //arguments.add("--dsl");
        //arguments.add("groovy");

        //arguments.add("--test-framework");
        //arguments.add("junit-jupiter");

        arguments.add("--incubating");

        System.out.println(Arrays.toString(arguments.toArray(new String[0])));
        Process process = GradleUtils.executeGradle(srcDirectory, arguments.toArray(new String[0]));
        if(process != null) {
            try {
                process.waitFor();
            } catch (InterruptedException exception) {
                Editor.getInstance().handleError(exception);
            }
        }
    }

    public static void prepareGradleProject(File directory) {
        try {
            File libDirectory = new File(directory, "lib/");
            File libsDirectory = new File(libDirectory, "libs/");

            if (libDirectory.exists()) {
                if (!libsDirectory.exists()) {
                    libsDirectory.mkdirs();
                }

                AssetLocation engineFile = AssetLocation.create("/files/engine.jar", AssetLocation.INTERNAL);
                AssetLocation utilitiesFile = AssetLocation.create("/files/utilities.jar", AssetLocation.INTERNAL);

                try {
                    Files.copy(engineFile.inputStream(), new File(libsDirectory, "engine.jar").toPath(), StandardCopyOption.REPLACE_EXISTING);
                    Files.copy(utilitiesFile.inputStream(), new File(libsDirectory, "utilities.jar").toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception exception) {
                    Editor.getInstance().handleError(exception);
                }

                File gradleBuildFileGroovy = new File(libDirectory, "build.gradle");
                File gradleBuildFileKotlin = new File(libDirectory, "build.gradle.kts");
                File file;

                if (gradleBuildFileGroovy.exists()) {
                    file = gradleBuildFileGroovy;
                } else {
                    file = gradleBuildFileKotlin;
                }

                List<String> lines = new ArrayList<>();
                try (Stream<String> stream = Files.lines(file.toPath())) {
                    AtomicReference<String> previousLine = new AtomicReference<>();
                    stream.forEach(line -> {
                        if(previousLine.get() != null) {
                            if(previousLine.get().contains("dependencies {")) {
                                lines.add(" ");
                                lines.add("    // Dependencies needed for the development of the game");
                                if(gradleBuildFileGroovy.exists()) {
                                    for (String dependency : dependencies) {
                                        lines.add("    api " + dependency);
                                    }
                                } else {
                                    for (String dependency : dependencies) {
                                        lines.add("    api(" + dependency + ")");
                                    }
                                }
                                lines.add(" ");
                            }
                        }
                        lines.add(line);
                        previousLine.set(line);
                    });
                }

                FileWriter fileWriter = new FileWriter(file);
                for (String line : lines) {
                    fileWriter.write(line + System.lineSeparator());
                }
                fileWriter.flush();
                fileWriter.close();

            }
        } catch (Exception exception) {
            Editor.getInstance().handleError(exception);
        }
    }

}
