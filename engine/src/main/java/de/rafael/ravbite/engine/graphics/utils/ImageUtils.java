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
package de.rafael.ravbite.engine.graphics.utils;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/29/2022 at 9:22 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.utils.asset.AssetLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ImageUtils {

    /**
     * Loads an image from a path
     * @param assetLocation Path to the image
     * @return Image as BufferedImage
     * @throws IOException If the file doesn't exist or an error occurred while reading the data
     */
    public static BufferedImage loadImage(AssetLocation assetLocation) throws IOException {
        return ImageIO.read(assetLocation.inputStream());
    }

    /**
     * Converts an array of BufferedImages to an array of GLFWImages
     * @param bufferedImageArray Array of BufferedImages
     * @return Array of GLFWImages
     */
    public static GLFWImage[] bufferedImageArrayToGLFWImageArray(BufferedImage[] bufferedImageArray) {
        GLFWImage[] array = new GLFWImage[bufferedImageArray.length];
        for (int i = 0; i < bufferedImageArray.length; i++) {
            array[i] = bufferedImageToGLFWImage(bufferedImageArray[i]);
        }
        return array;
    }

    /**
     * Converts an image of BufferedImage to an GLFWImage
     * @param bufferedImage BufferedImage
     * @return GLFWImage
     */
    public static GLFWImage bufferedImageToGLFWImage(BufferedImage bufferedImage) {
        if (bufferedImage.getType() != BufferedImage.TYPE_INT_ARGB_PRE) {
            BufferedImage convertedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
            Graphics2D graphics = convertedImage.createGraphics();

            int targetWidth = bufferedImage.getWidth();
            int targetHeight = bufferedImage.getHeight();

            graphics.drawImage(bufferedImage, 0, 0, targetWidth, targetHeight, null);
            graphics.dispose();

            bufferedImage = convertedImage;
        }

        ByteBuffer buffer = BufferUtils.createByteBuffer(bufferedImage.getWidth() * bufferedImage.getHeight() * 4);

        for (int h = 0; h < bufferedImage.getHeight(); h++) {
            for (int w = 0; w < bufferedImage.getWidth(); w++) {
                int colorSpace = bufferedImage.getRGB(w, h);
                buffer.put((byte) ((colorSpace << 8) >> 24));
                buffer.put((byte) ((colorSpace << 16) >> 24));
                buffer.put((byte) ((colorSpace << 24) >> 24));
                buffer.put((byte) (colorSpace >> 24));
            }
        }

        buffer.flip();

        GLFWImage result = GLFWImage.create();
        result.set(bufferedImage.getWidth(), bufferedImage.getHeight(), buffer);

        return result;
    }

}
