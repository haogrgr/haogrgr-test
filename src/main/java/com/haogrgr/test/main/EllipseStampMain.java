package com.haogrgr.test.main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class EllipseStampMain {

	static int width = 4000, height = 4000; //图片长宽

	public static void main(String[] args) throws Exception {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);

		Graphics2D graphics = image.createGraphics();
		graphics.translate(width / 2, height / 2);// 把绘制起点挪到圆中心点

		int ew = 3000, eh = 2000;//红圈椭圆长短轴

		//外围红框椭圆
		Ellipse2D ellipse = new Ellipse2D.Double(-ew / 2, -eh / 2, ew, eh);// 椭圆
		graphics.setStroke(new BasicStroke(80.0f));// 线条粗细
		graphics.setColor(Color.RED); // 线条颜色
		graphics.draw(ellipse);// 绘制

		drawFirm(graphics, "美国阿帕奇科技有限公司", ew, eh, 600);

		ImageIO.write(image, "png", new File("D:/test2.png"));
		image.flush();
	}

	/**
	 * 沿着椭圆画字符
	 * @param graphics
	 * @param firm 字符
	 * @param ew 外围椭圆长轴
	 * @param eh 外围椭圆短轴
	 * @param space 红圈和字符所在的椭圆直接的间距
	 */
	public static void drawFirm(Graphics2D graphics, String firm, int ew, int eh, int space) {
		graphics.setColor(Color.RED);
		graphics.setFont(new Font("宋体", Font.BOLD, 200));

		//space 为字符所在的椭圆和外面的红圈椭圆的差距
		int a = (ew - space) / 2, b = (eh - space) / 2;

		//算出椭圆半周长
		double halfLenCount = getHalfEllipseLenth(a, b);
		System.out.println(String.format("椭圆半周长: %s", halfLenCount));

		//第一个字符和最后一个字符先画
		drawFirmChar(graphics, firm.charAt(0), -a, 0, b);//画第一个字符
		drawFirmChar(graphics, firm.charAt(firm.length() - 1), a, 0, b);//画第最好一个字符

		//误差调整
		double[] adjustment = { 0, 0, 420, 200, 100, 70, 45, 30, 23, 13, 9, 8 };

		//根据椭圆周长等分, 然后循环椭圆坐标, 当长度达到等分时, 写下字符
		double x1 = -a, y1 = 0, step = halfLenCount / (firm.length()) + adjustment[firm.length() - 1], len = 0;
		for (double x = -a, i = 1; x <= a; x++) {
			double y = getEllipseY(x, a, b);
			len += getLenth(x1, y1, x, y);
			if (len >= step & i < firm.length() - 1) {
				System.out.println(String.format("%s, %s, %s, len", x, firm.charAt((int) i), i));
				drawFirmChar(graphics, firm.charAt((int) i++), x, y, b);
				len = 0;
			}

			x1 = x;
			y1 = y;
		}

	}

	/**
	 * 椭圆标准方程, 根据x坐标, 求y坐标
	 * @param x x坐标
	 * @param a 椭圆长轴
	 * @param b 椭圆短轴
	 * @return y坐标
	 */
	public static double getEllipseY(double x, int a, int b) {
		double a_a = Math.pow(a, 2), b_b = Math.pow(b, 2);
		double y = Math.sqrt((1 - (x * x) / a_a) * b_b);
		return y;
	}

	/**
	 * 画字符
	 * @param graphics
	 * @param c 字符
	 * @param x 小椭圆x坐标
	 * @param y 小椭圆y坐标
	 * @param theta 字符旋转弧度
	 */
	public static void drawFirmChar(Graphics2D graphics, char c, double x, double y, int b) {
		double theta = Math.atan(((b * b) / y - y) / x); //椭圆上点(x, y)的切线的弧度

		//反转角度, 比如到了90度了
		if (Double.isNaN(theta)) {
			theta = Math.toRadians(0);
		}

		//最后一个字符, 因为其实角度为0度, 字符会靠下, 所以要偏移y轴为字符宽度
		if (theta == 1.5707963267948966d) {
			y = y + graphics.getFontMetrics().stringWidth(c + "");
		}

		//转换坐标 -> 旋转坐标 -> 画字符 -> 还原坐标
		graphics.translate(x, -y);
		graphics.rotate(theta);
		graphics.drawString(c + "", 0, 0);
		graphics.rotate(-theta);
		graphics.translate(-x, y);
	}

	/**
	 * http://zhidao.baidu.com/link?url=SHMm_XY72VHIqBiqZErGVDAVnDX5vQl2gYXJ2-3tffC-pElzfT0atmQ3FxNeIrMgE8-1Gmuru1-jQIOWslJhH_
	 * 
	 * 根据角度, 求该角度所表示的直线(y = kx + 0)与椭圆的交点x坐标
	 **/
	public static double getFriends(double degrees, int a, int b) {
		double k = Math.tan(Math.toRadians(degrees));

		double top = a * b * Math.sqrt(b * b + k * k * a * a);
		double button = b * b + a * a * k * k;

		double ret = top / button;
		return ret;
	}

	/**
	 * 椭圆近视半周长
	 * @param a 椭圆长轴
	 * @param b 椭圆短轴
	 * @return 周长
	 */
	public static double getHalfEllipseLenth(int a, int b) {
		//先近视计算椭圆周长(lenCount)(椭圆上一个点和下一个点的直线距离累加)
		double halfLenCount = 0;

		double x1 = -a, y1 = 0;
		for (double x = -a; x <= a; x++) {
			double y = getEllipseY(x, a, b);
			halfLenCount += getLenth(x1, y1, x, y);//三角函数求斜边长, 就是两个坐标点的距离
			x1 = x;
			y1 = y;
		}

		return halfLenCount;
	}

	/**
	 * 获取坐标系中, 两个点(x1, y1), (x2, y2)的直线距离
	 */
	public static double getLenth(double x1, double y1, double x2, double y2) {
		double ret = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
		return ret;
	}

}
