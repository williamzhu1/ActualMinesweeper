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
            mine = 0;

            tiles = new AbstractTile[8][8];

        }else if(level == Difficulty.MEDIUM){
            //16*16 with 40 mine
            height = 16;
            width = 16;
            mine = 40;

            tiles = new AbstractTile[16][16];

        }else{
            //16*30 with 99 mine
            height = 16;
            width = 30;
            mine = 99;

            tiles = new AbstractTile[16][30];

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
            this.viewNotifier.notifyFlagged(x,y);
            flagcount--;
            this.viewNotifier.notifyFlagCountChanged(flagcount);
        }else{
            tiles[y][x].flag();
            this.viewNotifier.notifyUnflagged(x,y);
            flagcount++;
            this.viewNotifier.notifyFlagCountChanged(flagcount);
        }
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
            this.viewNotifier.notifyOpened(x,y,1);
        }
    }

    @Override
    public void flag(int x, int y) {
        if(x < 0 || y < 0 || x >= this.width || y >= this.height){
            return;
        }
        tiles[y][x].flag();
        this.viewNotifier.notifyFlagged(x,y);
        flagcount++;
        this.viewNotifier.notifyFlagCountChanged(flagcount);
        System.out.println("xyz");
    }

    @Override
    public void unflag(int x, int y) {
        if(x < 0 || y < 0 || x >= this.width || y >= this.height){
            return;
        }
        tiles[y][x].unflag();
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
