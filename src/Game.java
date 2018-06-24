public class Game{
    public static void main(String[] args) {
        System.out.println("Hello World!"); // Display the string.
        Board board = new Board(40,50);
        BoardVisualComponent comp = new BoardVisualComponent(board);
	BoardVisualFrame frame = new BoardVisualFrame(board, "window name", comp);
        System.out.println("Bye World!asdasdasdasd"); // Display the string.
        System.out.println(board.getBlockAt(1,16));
        // TODO Fix prefered window size and such
    }



}