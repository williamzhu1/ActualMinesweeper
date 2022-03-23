package model;

public class Minesweeper extends AbstractMineSweeper{

    int height;
    int width;
    int mine;
    int flagcount;

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
            mine = 10;

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

        this.startNewGame(height, width, mine);
        this.viewNotifier.notifyNewGame(height,width);
    }

    @Override
    public void startNewGame(int row, int col, int explosionCount) {
        height = row;
        width = col;
        mine = explosionCount;


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
        if(x < 0 || y < 0 || x >= this.width || y >= this.height){
            return;
        }
        if(tiles[y][x].isFlagged){
            tiles[y][x].unflag();
            flagcount--;
            this.viewNotifier.notifyUnflagged(x,y);
        }else{
            tiles[y][x].flag();
            flagcount++;
            this.viewNotifier.notifyFlagged(x,y);
        }
        this.viewNotifier.notifyFlagCountChanged(flagcount);
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
        if(x < 0 || y < 0 || x >= this.width || y >= this.height){
            return;
        }
        if(!tiles[y][x].isOpened) {
            tiles[y][x].open();
//If the tile is not explosive
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

            }else{
                //when the tile is explosive
                this.viewNotifier.notifyExploded(x,y);
            }


            //void notifyOpened(int x, int y, int explosiveNeighbourCount);
        }
    }

    @Override
    public void flag(int x, int y) {
        if(x < 0 || y < 0 || x >= this.width || y >= this.height || tiles[y][x].isFlagged){
            return;
        }
        tiles[y][x].flag();
        flagcount++;
        this.viewNotifier.notifyFlagCountChanged(flagcount);
        this.viewNotifier.notifyFlagged(x,y);
    }

    @Override
    public void unflag(int x, int y) {
        if(x < 0 || y < 0 || x >= this.width || y >= this.height || !tiles[y][x].isFlagged){
            return;
        }
        tiles[y][x].unflag();
        flagcount--;
        this.viewNotifier.notifyFlagCountChanged(flagcount);
        this.viewNotifier.notifyFlagged(x,y);
    }

    @Override
    public void deactivateFirstTileRule() {

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
