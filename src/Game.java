public class Game{
    public static void main(String[] args) {
        System.out.println("Hello World!"); // Display the string.

        Board board = new Board(40,50);
        BoardVisual boardVisual = new BoardVisual(board);
	BoardFrame frame = new BoardFrame(board, "window name", boardVisual);
	board.addBoardListener(boardVisual);

        Player player = new Player(boardVisual, board);
        System.out.println("Bye World!"); // Display the string.
    }



}