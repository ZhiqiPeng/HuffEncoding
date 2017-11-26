import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class ReadAndCount {
	private byte[] buffer = new byte[256];
	private ArrayList<FreV> list;

	public ReadAndCount(ArrayList<FreV> list) {
		this.list = list;
	}

	public void readbyte(String inputAddress) throws IOException {
		Path path = Paths.get(inputAddress);
		InputStream input = Files.newInputStream(path, StandardOpenOption.READ);
		int len = input.read(buffer);
		while (len != -1) {
			count(len);
			setZeroToBuff(buffer);
			len = input.read(buffer);
		}
	}

	private void count(int len) {// addFrequency
		for (int i = 0; i < len; i++) {
			seekAndAdd(buffer[i]);
		}
	}

	private void seekAndAdd(byte b) {
		for (FreV v : list) {
			if (v.getV() == b) {
				v.setF(v.getF() + 1);
				return;
			}
		}
		list.add(new FreV(b));
	}

	private void setZeroToBuff(byte[] buff) {
		for (int i = 0; i < buff.length; i++) {
			buff[i] = 0;
		}
	}

}
