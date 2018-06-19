public class Game{
    public static void main(String[] args) {
        System.out.println("Hello World!"); // Display the string.
        Board board = new Board(50,50);
        BoardVisualComponent comp = new BoardVisualComponent(board);
	BoardVisualFrame frame = new BoardVisualFrame(board, "window name", comp);
        // TODO Fix prefered window size and such
    }



}