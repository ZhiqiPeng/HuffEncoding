import java.io.IOException;

public class hdec {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		if (args.length != 1) {
			System.out.println("The usage should be henc fileAddress");
			System.exit(0);
		}
		DecodeProcess3 dc = new DecodeProcess3(args[0]);//here is 2, original is DecodeProcess
		try {
			dc.start();
			System.out.println("Decoded.");
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("Invalid address or Missing .cd file.");
		}

	}

}
