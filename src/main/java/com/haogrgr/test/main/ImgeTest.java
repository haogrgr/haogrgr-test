package com.haogrgr.test.main;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class ImgeTest {

	public static void main(String[] args) throws Exception {
		BufferedImage image = (BufferedImage) ImageIO.read(ImgeTest.class.getResourceAsStream("/img.jpg"));
		int width = image.getWidth(), height = image.getHeight(), r = image.getWidth() / 2 + 20, rx = image.getWidth() / 2, ry = image.getHeight() / 2;

		System.out.println("width : " + width + ", height : " + height);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int temp = (x - rx) * (x - rx) + (y - ry) * (y - ry);
				if (temp > (r * r)) {
					image.setRGB(x, y, 0);
				}
			}
		}
	}

}
