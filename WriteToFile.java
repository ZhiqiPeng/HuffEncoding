import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class WriteToFile {
	private String inputAddress;
	private InputStream input;
	private OutputStream output;
	private byte[] inBuff = new byte[256];
	private ArrayList<FreV> codeList;
	private byte[] outBuff = new byte[256];
	private short index = 0;// next write position. start from 0.
	private File original;

	public WriteToFile(String inputAddress) {
		this.inputAddress = inputAddress;
	}

	public void setCodeList(ArrayList<FreV> codeList) {
		this.codeList = codeList;
	}

	public void start() throws IOException {
		setUpIO();
		readAndWrite();
		if (index != 0) {
			int size = index / 8 + ((index % 8 == 0) ? 0 : 1);
			byte[] bff = new byte[size];
			for (int i = 0; i < size; i++) {
				bff[i] = outBuff[i];
			}
			output.write(bff);
		}
		original.delete();
		saveCodeList();
	}

	private void setUpIO() throws IOException {
		Path in = Paths.get(inputAddress);
		Path out = Paths.get(inputAddress + ".huf");
		input = Files.newInputStream(in, StandardOpenOption.READ);
		output = Files.newOutputStream(out, StandardOpenOption.CREATE);
		original = in.toFile();
		// original=in.toFile();
	}

	private void readAndWrite() throws IOException {
		int len = input.read(inBuff);
		while (len != -1) {
			writeBuff(len);
			setZeroToBuff(inBuff);
			len = input.read(inBuff);
		}

	}

	private void writeBuff(int len) throws IOException {
		for (int i = 0; i < len; i++) {

			for (FreV v : codeList) {
				if (v.getV() == inBuff[i]) {
					changeBit(v.getHuff());
					break;
				}
			}

		}
	}

	private void changeBit(String huff) throws IOException {
		for (int i = 0; i < huff.length(); i++) {
			int bitPosition = index % 8;
			int bytePosition = (int) Math.floor(index / 8);

			if (huff.charAt(i) == '1') {
				outBuff[bytePosition] = setBitToOne(outBuff[bytePosition], 8 - bitPosition);
				index++;
			} else {
				outBuff[bytePosition] = setBitToZero(outBuff[bytePosition], 8 - bitPosition);
				index++;
			}
			if (index == (outBuff.length * 8)) {
				output.write(outBuff);
				index = 0;
				setZeroToBuff(outBuff);
			}
		}
	}

	private byte setBitToOne(byte b, int position) {
		b = (byte) (b | (1 << (position - 1)));
		return b;
	}

	private byte setBitToZero(byte b, int position) {
		b = (byte) (b & ~(1 << (position - 1)));
		return b;
	}

	private void setZeroToBuff(byte[] buff) {
		for (int i = 0; i < buff.length; i++) {
			buff[i] = 0;
		}
	}

	private long calcuBitNum() {
		long fileLen = 0;
		for (FreV v : codeList) {
			fileLen = fileLen + v.getHuff().length() * v.getF();
		}
		return fileLen;
	}

	private void saveCodeList() throws IOException {
		String codeAddress = inputAddress + ".cd";
		Path codeOut = Paths.get(codeAddress);
		OutputStream out = Files.newOutputStream(codeOut, StandardOpenOption.CREATE);
		PrintWriter writer = new PrintWriter(out);
		writer.println(calcuBitNum());// first line is bit length.
		for (FreV v : codeList) {
			writer.print(v.getV() + "," + v.getHuff() + ",");
		}
		writer.flush();
		writer.close();
	}
}
