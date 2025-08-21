
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

class Minesweeper extends Vis  {

	static class Cell {
		int[] coord = new int[2];
		boolean seen;
		boolean bomb;
		boolean flagged;
		int number; // number of adjacent bombs
		public Cell (int x, int y) {
			this.seen = false;
			this.coord[0] = x;
			this.coord[1] = y;
			this.bomb = false;
			this.flagged = false;
			this.number = 0;
		}
	}

	static void drawGrid(int n, Color Color1, Color Color2, Color Color1Seen, Color Color2Seen, Cell[][] Cells) {
		double interval = 1000.0/n;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (Cells[i][j].seen&&!Cells[i][j].bomb) {

					if ((i+j) % 2 == 1) {
						drawRectangle(i*interval,j*interval, interval*(i+1),interval*(j+1),Color1Seen);
					}
					else {
						drawRectangle(i*interval,j*interval, interval*(i+1),interval*(j+1),Color2Seen);
					}
				}
				else {
					if ((i+j) % 2 == 1) {
						drawRectangle(i*interval,j*interval, interval*(i+1),interval*(j+1),Color1);
					}
					else {
						drawRectangle(i*interval,j*interval, interval*(i+1),interval*(j+1),Color2);
					}
				}

			}
		}
		// Uncomment if you want the black lines in the grid 
		/*
		// Draw Horizontal Lines
		for (int i = 1; i < n; i++) {
			drawLine(0, i * interval, 1000, i*interval,BLACK);
		}
		// Draw vertical Lines
		for (int i = 1; i < n; i++) {
			drawLine (i * interval, 0, i*interval, 1000, BLACK);
		}
		 */

	}

	static void drawBomb(Cell Bomb1, int size, Color Color1) {
		double interval = 1000.0/size;
		int x = Bomb1.coord[0];
		int y = Bomb1.coord[1];
		drawRectangle((x+0.1)*interval,(y+0.1)*interval, interval*(x+0.9),interval*(y+0.9),Color1);
	}

	// n - the integers number of bombs 
	// size - the integer size of the grid
	static void GenerateBombs (int n, int size, Cell[][] Cells){
		Random random = new Random();
		HashMap<Cell, Boolean> result = new HashMap<>();
		HashMap<int[], Integer> used_coord = new HashMap<>();
		int x;
		int y;
		while (result.size() < n)	 {
			x = random.nextInt(size);
			y = random.nextInt(size);
			Cell Bomb1 = Cells[x][y];
			if (Bomb1.seen ==false) {
				Bomb1.bomb = true;
				int[] coord = {x,y};
				if (!used_coord.containsKey(coord)) {
					result.put(Bomb1,Bomb1.seen);
				}
			}
		}
	}


	static int[] getCell(double x, double y,  int size) {
		double interval = 1000/size;
		int i = 0;
		while (i*interval < x) {
			i++;
		}
		int j = 0;
		while (j*interval < y) {
			j++;
		}
		int[] coord =  {i-1, j-1};
		return coord;
	}

	static void Lost(Cell[][] Cells, Cell lastBomb, int size) {
		// Make all Bombs visible
		for (int i = 0; i < size;i++) {
			for (int j =0; j< size; j++) {
				if (Cells[i][j].bomb) {
					Cells[i][j].seen = true;
				}
			}
		}
		drawBomb(lastBomb, size, RED);
	}

	static void AssignNumbers(Cell[][] Cells, int size) {
		for (int i = 0; i < size;i++) {
			for (int j = 0; j<size; j++) {
				if (!Cells[i][j].bomb) {
					int n = 0;
					if (i+1<size && Cells[i+1][j].bomb) {n++;}
					if (i-1>=0 && Cells[i-1][j].bomb) {n++;}
					if (j+1<size && Cells[i][j+1].bomb) {n++;}
					if (j-1>=0 && Cells[i][j-1].bomb) {n++;}
					if (i+1<size && j+1<size && Cells[i+1][j+1].bomb) {n++;}
					if (i+1<size && j-1>=0 && Cells[i+1][j-1].bomb) {n++;}
					if (i-1>=0 && j+1<size && Cells[i-1][j+1].bomb) {n++;}
					if (i-1>=0 && j-1>=0 && Cells[i-1][j-1].bomb) {n++;}
					Cells[i][j].number = n;
				}
			}
		}
	}

	static void drawFlag(Cell Cell1, int size) {
		double interval = 1000/size;
		int x = Cell1.coord[0];
		int y = Cell1.coord[1];
		drawRectangle((x+0.47)*interval, (y+0.05)* interval, (x+0.53)*interval, (y+0.95)*interval, RED);
		drawTriangle((x+0.53)*interval,(y+0.95)*interval,(x+0.53)*interval,(y+0.55)*interval,(x+0.95)*interval,(y+0.75)*interval,RED);
	}

	public static void main(String[] arguments) {
		Cell[][] Cells = null;
		Color niceColor1 = new Color(   227.0 / 255.0,   253.0 / 255.0,   152.0 / 255.0);
		Color niceColor2 = new Color(   217.0 / 255.0,   246.0 / 255.0,   105.0 / 255.0);
		Color niceColor1Seen = new Color(   210.0 / 255.0,   180.0 / 255.0,   140.0 / 255.0);
		Color niceColor2Seen = new Color(   245.0 / 255.0,   245.0 / 255.0,   220.0 / 255.0);
		canvasConfig(0, 0, 1000.0, 1000.0, WHITE, 1000);
		int size = 10; // the board is size*size
		int bombsNumber = 15;
		int seenNumber;
		boolean lost = false;
		boolean win = false;
		Cell BoomBomb = null; // the Bomb that will be highlighted in red 
		int t = 0; // time variable 
		Cell[][] Cellsi = new Cell[size][size];
		for (int i = 0; i < size;i++) {
			for (int j = 0; j< size; j++) {
				Cellsi[i][j] = new Cell(i,j);
			}
		}
		boolean menu = true;
		boolean generated = false;
		boolean inPlay = false;
		boolean backToMenu = false;
		int numberClicked = 0;
		int mode = -1;
		ASSERT(size*size>bombsNumber);

		while (beginFrame()) {

			if (menu) {
				drawGrid(10, niceColor1, niceColor2, niceColor1Seen, niceColor2Seen, Cellsi);
				drawString("Minesweeper" , 100, 700, BLACK, 120 );
				drawRectangle(100,200,400,500,niceColor1Seen);
				drawString("Difficulty" , 130, 535 ,BLACK , 40 );
				drawString("Easy" , 250, 440 , WHITE , 40 );
				drawString("Med" , 250, 340 , WHITE , 40 );
				drawString("Hard" , 250, 240 , WHITE , 40 );
				drawCircle(180,451,15,WHITE);
				drawCircle(180,351,15,WHITE);
				drawCircle(180,251,15,WHITE);

				drawRectangle(600,200,900,500,niceColor2Seen);
				drawString("Size", 700, 535 ,BLACK , 40 );
				drawString("10X10" , 750, 440 , BLACK , 40 );
				drawString("20X20" , 750, 340 , BLACK , 40 );
				drawString("30X30" , 750, 240 , BLACK , 40 );
				drawCircle(680,451,15,BLACK);
				drawCircle(680,351,15,BLACK);
				drawCircle(680,251,15,BLACK);

				drawRectangle(425,125,575,175,BLACK);

				drawString("Play", 465, 143,WHITE , 30 );


				double x = mouseX;
				double y = mouseY;

				HashMap<String, Integer> map = new HashMap<String, Integer>();
				map.put("-110",10);
				map.put("-210",20);
				map.put("-310",30);
				map.put("-120",40);
				map.put("-220",75);
				map.put("-320",110);
				map.put("-130",100);
				map.put("-230",200);
				map.put("-330",300);

				if  (mousePressed) {
					if (x>(180-30)&&x<(180+30)&&y<(451+30)&&y>(451-30)) {
						mode = -1;
					}
					else if ((x>(180-30)&&x<(180+30)&&y<(351+30)&&y>(351-30))) {
						mode = -2;
					}
					else if ((x>(180-30)&&x<(180+30)&&y<(251+30)&&y>(251-30))) {
						mode = -3;
					}

					else if (x>(680-30)&&x<(680+30)&&y<(451+30)&&y>(451-30)) {
						size = 10;
					}
					else if ((x>(680-30)&&x<(680+30)&&y<(351+30)&&y>(351-30))) {
						size = 20;
					}
					else if ((x>(680-30)&&x<(680+30)&&y<(251+30)&&y>(251-30))) {
						size = 30;
					}
					else if ((x>(425)&&x<(575)&&y>(125)&&y<(175))) {
						inPlay = true;
					}

				}
				String gameMode = String.valueOf(mode) + String.valueOf(size);

				if (size == 10) {
					drawCircle(680,451,10,WHITE);
				}
				else if (size == 20) {
					drawCircle(680,351,10,WHITE);
				}
				else if (size == 30) {
					drawCircle(680,251,10,WHITE);
				}
				if (mode == -1) {
					drawCircle(180,451,10,BLACK);
				}
				else if (mode == -2) {
					drawCircle(180,351,10,BLACK);
				}
				else if (mode == -3) {
					drawCircle(180,251,10,BLACK);
				}
				bombsNumber = map.get(gameMode);
				if (inPlay) {
					menu = false;
				}
			}

			else if (inPlay) {
				if  (!generated) {
					Cells = new Cell[size][size];
					for (int i = 0; i < size;i++) {
						for (int j = 0; j< size; j++) {
							Cells[i][j] = new Cell(i,j);
						}
					}
					generated = true;
				}
				if (numberClicked == 1) {
					GenerateBombs(bombsNumber, size, Cells);
					AssignNumbers(Cells,size); // assign numbers of adjacent bombs to the cells
					numberClicked = 2;
				}

				seenNumber = 0;
				int flagNumber = 0;
				int Mx = -1;
				int My = -1;
				drawGrid(size, niceColor1, niceColor2, niceColor1Seen, niceColor2Seen, Cells);
				if (mouseX < 1000) {Mx = getCell(mouseX, mouseY, size)[0];}
				if (mouseY < 1000) {My = getCell(mouseX, mouseY, size)[1];}

				if (mousePressed&&!lost&&!win) {
					numberClicked++;
					if (keyHeld(SHIFT)) {
						Cells[Mx][My].flagged = !Cells[Mx][My].flagged;

					}
					else if (Cells[Mx][My].bomb) {
						BoomBomb = Cells[Mx][My];
						Cells[Mx][My].seen = true;

					}
					// Double click mechanics for opening - open all adjacent cells after double click
					else if (t<40&&mousePressed&&!Cells[Mx][My].bomb&&Cells[Mx][My].seen) {
						if (Mx+1<size &&!Cells[Mx+1][My].flagged) { 
							Cells[Mx+1][My].seen = true;
							if (Cells[Mx+1][My].bomb) {BoomBomb = Cells[Mx+1][My];}
						}
						if (Mx-1>=0 &&!Cells[Mx-1][My].flagged) {
							Cells[Mx-1][My].seen = true;
							if (Cells[Mx-1][My].bomb) {BoomBomb = Cells[Mx-1][My];}
						}
						if (My+1<size &&!Cells[Mx][My+1].flagged) { 
							Cells[Mx][My+1].seen = true;
							if (Cells[Mx][My+1].bomb) {BoomBomb = Cells[Mx][My+1];}
						}
						if (My-1>=0 &&!Cells[Mx][My-1].flagged) {
							Cells[Mx][My-1].seen = true;
							if (Cells[Mx][My-1].bomb) {BoomBomb = Cells[Mx][My-1];}
						}
						if (Mx+1<size && My+1<size &&!Cells[Mx+1][My+1].flagged) { 
							Cells[Mx+1][My+1].seen = true;
							if (Cells[Mx+1][My+1].bomb) {BoomBomb = Cells[Mx+1][My+1];}
						}
						if (Mx+1<size && My-1>=0 &&!Cells[Mx+1][My-1].flagged) { 
							Cells[Mx+1][My-1].seen = true;
							if (Cells[Mx+1][My-1].bomb) {BoomBomb = Cells[Mx+1][My-1];}
						}
						if (Mx-1>=0 && My+1<size &&!Cells[Mx-1][My+1].flagged) { 
							Cells[Mx-1][My+1].seen = true;
							if (Cells[Mx-1][My+1].bomb) {BoomBomb = Cells[Mx-1][My+1];}
						}
						if (Mx-1>=0 && My-1>=0 && !Cells[Mx-1][My-1].flagged) {
							Cells[Mx-1][My-1].seen = true;
							if (Cells[Mx-1][My-1].bomb) {BoomBomb = Cells[Mx-1][My-1];}
						}
					}
					else {
						Cells[Mx][My].seen = true;
						int i = Mx;
						int j = My;
						if (numberClicked == 1) {
							if (i+1<size) { Cells[i+1][j].seen = true;}
							if (i-1>=0) { Cells[i-1][j].seen = true;}
							if (j+1<size) { Cells[i][j+1].seen = true;}
							if (j-1>=0) { Cells[i][j-1].seen = true;}
							if (i+1<size && j+1<size) { Cells[i+1][j+1].seen = true;}
							if (i+1<size && j-1>=0) { Cells[i+1][j-1].seen = true;}
							if (i-1>=0 && j+1<size) { Cells[i-1][j+1].seen = true;}
							if (i-1>=0 && j-1>=0) { Cells[i-1][j-1].seen = true;}
						}
					}
					t=0;
				}
				
				// Draw Seen cells
				for (int i = 0; i < size;i++) {
					for (int j =0; j< size; j++) {
						if (Cells[i][j].seen) {
							seenNumber ++;
						}
						if (Cells[i][j].bomb && Cells[i][j].seen) {
							drawBomb(Cells[i][j], size, BLACK);
							Lost(Cells, BoomBomb, size); 
							lost = true;
						}
						// Draw the number cells
						if (Cells[i][j].number!=0 && Cells[i][j].seen) {
							drawString(Integer.toString(Cells[i][j].number), (i+0.2)*1000/size, (j+0.2)*1000/size, BLACK, 1000/size);
						}
						// Open all the cells that are adjacent to 0s
						if (Cells[i][j].number == 0 && Cells[i][j].seen && !Cells[i][j].bomb && numberClicked >=2) {
							if (i+1<size) { Cells[i+1][j].seen = true;}
							if (i-1>=0) { Cells[i-1][j].seen = true;}
							if (j+1<size) { Cells[i][j+1].seen = true;}
							if (j-1>=0) { Cells[i][j-1].seen = true;}
							if (i+1<size && j+1<size) { Cells[i+1][j+1].seen = true;}
							if (i+1<size && j-1>=0) { Cells[i+1][j-1].seen = true;}
							if (i-1>=0 && j+1<size) { Cells[i-1][j+1].seen = true;}
							if (i-1>=0 && j-1>=0) { Cells[i-1][j-1].seen = true;}
						}
						// Draw flags on the flagged cells
						if (Cells[i][j].flagged) {
							drawFlag(Cells[i][j],size);
							flagNumber++;
						}
					}
				}
				if (keyPressed('R')) {
					for (int i = 0; i < size;i++) {
						for (int j =0; j< size; j++) {
							Cells[i][j] = new Cell(i,j);
						}
					}
					BoomBomb = null;
					GenerateBombs(bombsNumber, size, Cells);
					AssignNumbers(Cells, size); // assign numbers of adjacent bombs to the cells
					lost = false;
					generated = false;
					numberClicked = 0;

				}

				if ((seenNumber == size*size - bombsNumber)&&(flagNumber == bombsNumber)) {
					win = true;
				}
				if (keyPressed('E')&&!win||backToMenu) {
					menu = true;
					for (int i = 0; i < 10;i++) {
						for (int j =0; j< 10; j++) {
							Cellsi[i][j] = new Cell(i,j);
						}
					}
					BoomBomb = null;
					lost = false;
					generated = false;
					inPlay = false;
					numberClicked = 0;
					backToMenu = false;
					win = false;
				}
				if(win) {
					drawRectangle(50,300,950,600,BLACK);
					drawRectangle(400,330,600,430,WHITE);
					drawString("You WON!", 60 ,450 , WHITE, 190);
					drawString("MENU", 420 ,360 , BLACK, 70);
					if (mousePressed) {
						double Mx1 = mouseX;
						double My1 = mouseY;
						if (Mx1>400&&Mx1<600&&My1<430&&My1>330) {
							backToMenu= true;
						}
					}
				}
				t++;
				drawString("Total bombs:"+Integer.toString(bombsNumber)+". Flags Used: " + Integer.toString(flagNumber)+"/"+Integer.toString(bombsNumber), 10, 10, BLACK, 25);
				if (!win) {
					drawString("Press E to go back to menu", 600,960 , BLACK, 25);
				}

			}
		}
	}
}