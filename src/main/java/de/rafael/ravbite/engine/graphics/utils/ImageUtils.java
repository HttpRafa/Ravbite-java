package de.rafael.ravbite.engine.graphics.utils;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/29/2022 at 9:22 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.asset.AssetLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
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
        return ImageIO.read(assetLocation.asInputStream());
    }

    public static GLFWImage[] bufferedImageArrayToGLFWImageArray(BufferedImage[] bufferedImageArray) {
        GLFWImage[] array = new GLFWImage[bufferedImageArray.length];
        for (int i = 0; i < bufferedImageArray.length; i++) {
            array[i] = bufferedImageToGLFWImage(bufferedImageArray[i]);
        }
        return array;
    }

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

        final ByteBuffer buffer = BufferUtils.createByteBuffer(bufferedImage.getWidth() * bufferedImage.getHeight() * 4);

        for (int i = 0; i < bufferedImage.getHeight(); i++) {
            for (int j = 0; j < bufferedImage.getWidth(); j++) {
                int colorSpace = bufferedImage.getRGB(j, i);
                buffer.put((byte) ((colorSpace << 8) >> 24));
                buffer.put((byte) ((colorSpace << 16) >> 24));
                buffer.put((byte) ((colorSpace << 24) >> 24));
                buffer.put((byte) (colorSpace >> 24));
            }
        }

        buffer.flip();

        final GLFWImage result = GLFWImage.create();
        result.set(bufferedImage.getWidth(), bufferedImage.getHeight(), buffer);

        return result;
    }

}
