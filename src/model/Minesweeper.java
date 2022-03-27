package model;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;
import java.time.Instant;

public class Minesweeper extends AbstractMineSweeper{

    int height;
    int width;
    int mine;
    int flagcount;
    int opentiles;
    Instant start;
    Timer t;
    public Duration duration;
    int minecount;

    AbstractTile[][] tiles;

    public Minesweeper(){

    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void startNewGame(Difficulty level) {
        if(level == Difficulty.EASY){
            //8*8 with 10 mine
            height = 8;
            width = 8;
            mine = 3;

        }else if(level == Difficulty.MEDIUM){
            //16*16 with 40 mine
            height = 16;
            width = 16;
            mine = 40;


        }else{
            //16*30 with 99 mine
            height = 16;
            width = 30;
            mine = 99;

        }
        opentiles = 0;
        flagcount = 0;
        minecount = mine;
        this.startNewGame(height, width, mine);
        this.viewNotifier.notifyNewGame(height,width);
        this.viewNotifier.notifyMinesLeft(minecount);
    }

    @Override
    public void startNewGame(int row, int col, int explosionCount) {
        height = row;
        width = col;
        mine = explosionCount;
        opentiles = 0;



        tiles = new AbstractTile[height][width];

        for(int j=0; j<height; j++){
            for(int i=0; i<width; i++){
                tiles[j][i] = generateEmptyTile();
            }
        }

        for(int x=0; x< explosionCount;){
            int j = (int)(Math.random() * height);
            int i = (int)(Math.random() * width);

            if(!tiles[j][i].isExplosive){
                tiles[j][i] = generateExplosiveTile();
                x++;
            }
        }
    }

    @Override
    public void toggleFlag(int x, int y) {
        if(x < 0 || y < 0 || x >= this.width || y >= this.height || tiles[y][x].isOpened){
            return;
        }
        if(tiles[y][x].isFlagged){
            tiles[y][x].unflag();
            flagcount--;
            minecount ++;
            this.viewNotifier.notifyUnflagged(x,y);
        }else{
            tiles[y][x].flag();
            flagcount++;
            minecount --;
            this.viewNotifier.notifyFlagged(x,y);
        }
        this.viewNotifier.notifyFlagCountChanged(flagcount);
        this.viewNotifier.notifyMinesLeft(minecount);
    }

    @Override
    public AbstractTile getTile(int x, int y) {
        if(x < 0 || y < 0 || x >= this.width || y >= this.height){
            return null;
        }
        return tiles[y][x];
    }

    @Override
    public void setWorld(AbstractTile[][] world) {
        height = world.length;
        width = world[0].length;
        tiles = world;
    }

    @Override
    public void open(int x, int y) {
        if(x < 0 || y < 0 || x >= this.width || y >= this.height || tiles[y][x].isFlagged){
            return;
        }

        if(opentiles == 0){
            start = Instant.now();
            t = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    duration = duration.between(start, Instant.now());
                    viewNotifier.notifyTimeElapsedChanged(duration);
                }
            });

            t.start();

            if(tiles[y][x].isExplosive()){
                //removing the explosive tile for the first click
                tiles[y][x] = generateEmptyTile();
                //add explosive tiles somewhere else
                deactivateFirstTileRule();

            }

        }

        if(!tiles[y][x].isOpened) {
            tiles[y][x].open();

            opentiles ++;
          
            if(!tiles[y][x].isExplosive()){
                int explosiveNeibourCount = 0;
                //count the explosiveNeibourCount of a tile
                boolean right = false;

                if(x+1 <= this.width-1){
                    right = tiles[y][x+1].isExplosive();
                }
                boolean topRight = false;
                if(x+1 <= this.width-1 && y-1>=0){
                    topRight = tiles[y-1][x+1].isExplosive();
                }
                boolean bottomRight = false;
                if(x+1 <= this.width-1 && y+1 <= this.height-1){
                    bottomRight = tiles[y+1][x+1].isExplosive();
                }
                boolean left = false;
                if(x-1 >= 0){
                    left = tiles[y][x-1].isExplosive();
                }
                boolean topLeft = false;
                if(x-1 >= 0 && y-1>=0){
                    topLeft = tiles[y-1][x-1].isExplosive();
                }
                boolean bottomLeft = false;
                if(x-1 >= 0 && y+1 <= this.height-1){
                    bottomLeft = tiles[y+1][x-1].isExplosive();
                }
                boolean top = false;
                if(y-1 >= 0){
                    top = tiles[y-1][x].isExplosive();
                }
                boolean bottom = false;
                if(y+1 <= this.height-1){
                    bottom = tiles[y+1][x].isExplosive();
                }

                if(right) {
                    explosiveNeibourCount += 1;
                }
                if(topRight){
                    explosiveNeibourCount+=1;
                }
                if(bottomRight){
                    explosiveNeibourCount+=1;
                }
                if(left){
                    explosiveNeibourCount+=1;
                }
                if(topLeft){
                    explosiveNeibourCount+=1;
                }
                if(bottomLeft){
                    explosiveNeibourCount+=1;
                }
                if(top){
                    explosiveNeibourCount+=1;
                }
                if(bottom){
                    explosiveNeibourCount+=1;
                }


                this.viewNotifier.notifyOpened(x,y,explosiveNeibourCount);
                if(opentiles >= height * width - mine){
                    for(int j=0; j<height; j++){
                        for(int i=0; i<width; i++){
                            if(!tiles[j][i].isOpened && !tiles[j][i].isFlagged){
                                toggleFlag(i,j);
                            }
                        }
                    }
                    t.stop();
                    this.viewNotifier.notifyGameWon();
                }

                //Open neighbours
                if(explosiveNeibourCount == 0){
                    open(x+1,y);
                    open(x+1,y-1);
                    open(x+1,y+1);
                    open(x-1,y);
                    open(x-1,y-1);
                    open(x-1,y+1);
                    open(x,y+1);
                    open(x,y-1);
                }

            }else{
                //when the tile is explosive
                minecount--;
                this.viewNotifier.notifyMinesLeft(minecount);
                this.viewNotifier.notifyExploded(x,y);
                t.stop();
                this.viewNotifier.notifyGameLost();
            }
        }
    }

    @Override
    public void flag(int x, int y) {
        if(x < 0 || y < 0 || x >= this.width || y >= this.height || tiles[y][x].isFlagged || tiles[y][x].isOpened){
            return;
        }
        tiles[y][x].flag();
        flagcount++;
        minecount--;
        this.viewNotifier.notifyFlagCountChanged(flagcount);
        this.viewNotifier.notifyFlagged(x,y);
        this.viewNotifier.notifyMinesLeft(minecount);
    }

    @Override
    public void unflag(int x, int y) {
        if(x < 0 || y < 0 || x >= this.width || y >= this.height || !tiles[y][x].isFlagged || tiles[y][x].isOpened){
            return;
        }
        tiles[y][x].unflag();
        flagcount--;
        minecount++;
        this.viewNotifier.notifyFlagCountChanged(flagcount);
        this.viewNotifier.notifyFlagged(x,y);
        this.viewNotifier.notifyMinesLeft(minecount);
    }

    @Override
    public void deactivateFirstTileRule() {
        int j = (int)(Math.random() * height);
        int i = (int)(Math.random() * width);

        if(!tiles[j][i].isExplosive){
            tiles[j][i] = generateExplosiveTile();
        }
    }

    @Override
    public AbstractTile generateEmptyTile()
    {
        return new EmptyTile();
    }

    @Override
    public AbstractTile generateExplosiveTile() {
        return new ExplosiveTile();
    }
  
}
