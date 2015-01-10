package com.haogrgr.test.algorithm;

/**
 * 控制台输出 螺旋矩阵
 * 1  2  3  4
 * 12 13 14 5
 * 11 16 15 6
 * 10 9  8  7
 *
 */
public class PrintSpiralMatrix {

	public static void main(String[] args) throws Exception {
		SpiralMatrix m = new SpiralMatrix(8);
		m.fill().print();
	}

}

class SpiralMatrix {
	int[][] matrix;
	int[] path = {1, 1, 2, 3, 4 };//[0] 为当前方向下标, [1 - 4] 为 → ↓ ← ↑ 
	int x, y, start = 1;

	SpiralMatrix(int len) { this.matrix = new int[len][len]; }
	
	SpiralMatrix fill(){ for(int i = 0, len = matrix.length * matrix.length; i < len; i++) set(); return this; }
	
	private void set() {
		matrix[y][x] = start++;  go(true);  //前进
		if (x < 0 || y < 0 || x == matrix.length || y == matrix.length || matrix[y][x] != 0) {//越界或以赋值就后退->换方向->前进
			go(false);   path[0] = path[0] == 4 ? 1 : path[0] + 1;  go(true);
		}
	}
	
	private void go(boolean f){//前进or后退
		switch (path[path[0]]) {
			case 1: x = f ? x+1 : x-1; break; //→
			case 2: y = f ? y+1 : y-1; break; //↓
			case 3: x = f ? x-1 : x+1;; break; //←
			case 4: y = f ? y-1 : y+1; break; //↑
			default: throw new IllegalStateException(path[path[0]] + "");
		}
	}
	
	void print(){
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				System.out.print(matrix[i][j] + "\t");
			}
			System.out.println();
		}
		System.out.println();
	}
	
}