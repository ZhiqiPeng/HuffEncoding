import java.io.IOException;
import java.util.ArrayList;

public class henc {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length != 1) {
			System.out.println("The usage should be henc fileAddress");
			System.exit(0);
		}
		ArrayList<FreV> list = new ArrayList<>();
		ReadAndCount rc = new ReadAndCount(list);
		try {
			rc.readbyte(args[0]);
			CalcuPath cp = new CalcuPath(list);
			cp.buildTree();
			WriteToFile w = new WriteToFile(args[0]);
			w.setCodeList(list);
			w.start();
			System.out.println("Encoded");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Invalid address!");
		}

	}

}
