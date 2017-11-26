
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
//Decode improve the start index.
public class DecodeProcess3 {
	private String inputAddress;
	private InputStream input;
	private OutputStream output;
	private long fileBitLen;// not actual len, is the compressed total bitlen.
	private ArrayList<FreV> code;
	private byte[] inBuff = new byte[256];
	private byte[] outBuff = new byte[256];
	private int index = 0;
	private int outBytePosition = 0;
	private File original;
	private int minLen;
	private int maxLen;
	private long timer=0;
	private ArrayList<ArrayList<FreV>> groupCode = new ArrayList<>();
    private ArrayList<SearchProcess> sps=new ArrayList<>();
    private ArrayList<Thread> threads=new ArrayList<>();
	public DecodeProcess3(String address) {
		this.inputAddress = address;
	}

	public void setCode(ArrayList<FreV> code) {
		this.code = code;
	}

	private void loadCode() throws IOException {
		code = new ArrayList<FreV>();
		int suffix = inputAddress.length() - 4;
		String codeAddress = inputAddress.substring(0, suffix);
		Path codein = Paths.get(codeAddress + ".cd");
		BufferedReader in = Files.newBufferedReader(codein);
		fileBitLen = Long.parseLong(in.readLine());
		String[] codepair = in.readLine().split(",");
		for (int i = 0; i < codepair.length; i = i + 2) {
			FreV v = new FreV(codepair[i]);
			v.setHuff(codepair[i + 1]);
			code.add(v);
		}
		groupCode();
		File codeFile = codein.toFile();
//		codeFile.delete();
//		printAllCode();//test
	}

	private void groupCode() {// test method
		minLen = code.get(0).getHuff().length();
		maxLen = code.get(0).getHuff().length();
		for (FreV v : code) {
			if (v.getHuff().length() < minLen) {
				minLen = v.getHuff().length();
				continue;
			}
			if (v.getHuff().length() > maxLen) {
				maxLen = v.getHuff().length();
				continue;
			}
		}
		for (int i = 0; i < maxLen - minLen + 1; i++) {
			groupCode.add(new ArrayList<FreV>());// groupCode[i] i is how many
													// digit more than minLen.
		}
		for (FreV v : code) {
			groupCode.get(v.getHuff().length() - minLen).add(v);
		}
	}

	public void start() throws IOException, InterruptedException {
		timer=System.currentTimeMillis();//test
		setUpIO();
		loadCode();
		System.out.println("LoadCode complete.");
		System.out.println("Time cost: "+(System.currentTimeMillis()-timer)+"mili seconds");//test
		timer=System.currentTimeMillis();
		Decode();
		System.out.println("Decode complete.");
		System.out.println("Time cost: "+(System.currentTimeMillis()-timer)+"mili seconds");//test
		timer=System.currentTimeMillis();
		if (outBytePosition != 0) {
			byte[] bff = new byte[outBytePosition];
			for (int i = 0; i < bff.length; i++) {
				bff[i] = outBuff[i];
			}
			output.write(bff);
		}
		output.flush();
//		original.delete();
	}

	private void setUpIO() throws IOException {
		Path in = Paths.get(inputAddress);
		int suffix = inputAddress.length() - 4;
		String outputAddress = inputAddress.substring(0, suffix);
		Path out = Paths.get(outputAddress);
		input = Files.newInputStream(in, StandardOpenOption.READ);
		output = Files.newOutputStream(out, StandardOpenOption.CREATE);
		original = in.toFile();
	}

	private void Decode() throws IOException, InterruptedException {
		int len = input.read(inBuff);
		String huffCode = "";
		while (len != -1) {
			huffCode = oneDecode(len, huffCode);
			setZeroToBuff(inBuff);
			len = input.read(inBuff);
		}
	}

	private String oneDecode(int len, String huffCode) throws IOException {
		len = howMuchToReadFromByte(len);// switch from byte len to bit len.
		while (index < len) {
			int bitPosition = index % 8;
			int bytePosition = index / 8;// change to this to check efficiency.
			huffCode = huffCode + Byte.toString(getBit(inBuff[bytePosition], 8 - bitPosition));
			if(huffCode.length()<minLen){//improved here
				index++;//improved here
				continue;//improved here
			}//improved here
//			int codeGroupIndex=(huffCode.length() - minLen)>0?(huffCode.length() - minLen):0;//this is the mistake (Index out of bound).
			for (FreV v : groupCode.get(huffCode.length() - minLen)) {
//			for (FreV v : groupCode.get(codeGroupIndex)) {	
				if (v.getHuff().equals(huffCode)) {
					outBuff[outBytePosition] = v.getV();
					huffCode = "";
					outBytePosition++;
					if (outBytePosition > outBuff.length - 1) {
						outBytePosition = 0;
						write();
					}
					break;
				}
			}
			index++;
		}

		index = 0;/// this is the problem that only write a little

		fileBitLen = fileBitLen - len;
//		System.out.println("One round decode Time cost: "+(System.currentTimeMillis()-timer)+"mili seconds");//test
//		timer=System.currentTimeMillis();//
		return huffCode;
	}
    
	private int howMuchToReadFromByte(int len) {
		if (fileBitLen < len * 8) {
			return (int) fileBitLen;
		} else {
			return len * 8;
		}
	}

	private void setZeroToBuff(byte[] buff) {
		for (int i = 0; i < buff.length; i++) {
			buff[i] = 0;
		}
	}

	private byte getBit(byte b, int position) {
		return (byte) ((b >> position - 1) & 1);
	}

	private void write() throws IOException {
		output.write(outBuff);
		output.flush();
		setZeroToBuff(outBuff);
	}
	private String oneDecode2(int len, String huffCode) throws IOException, InterruptedException {//test Method
		len = howMuchToReadFromByte(len);// switch from byte len to bit len.
		while (index < len) {
			int bitPosition = index % 8;
			int bytePosition = index / 8;// change to this to check efficiency.
			huffCode = huffCode + Byte.toString(getBit(inBuff[bytePosition], 8 - bitPosition));
			if(huffCode.length()<minLen){//improved here
				index++;//improved here
				continue;//improved here
			}//improved here
/*
			for (FreV v : groupCode.get(huffCode.length() - minLen)) {
//			for (FreV v : groupCode.get(codeGroupIndex)) {	
				if (v.getHuff().equals(huffCode)) {
					outBuff[outBytePosition] = v.getV();
					huffCode = "";
					outBytePosition++;
					if (outBytePosition > outBuff.length - 1) {
						outBytePosition = 0;
						write();
					}
					break;
				}
			}
			*/
			sps.clear();
			threads.clear();
			int partition=groupCode.get(huffCode.length() - minLen).size()/3;//maximum 4 search thread.
			int tail=groupCode.get(huffCode.length() - minLen).size()%3;
	//		System.out.println("GroupSize: "+groupCode.get(huffCode.length() - minLen).size());//test
			int threadNum;
			if(partition==0){
				threadNum=1;
			}else{
			if(tail==0){
				threadNum=3;
			}else{
				threadNum=4;
			}
			}
//			System.out.println("ThreadNum: "+threadNum);//
			for(int i=0;i<threadNum;i++){
				SearchProcess sp=new SearchProcess(groupCode.get(huffCode.length() - minLen),huffCode,i*partition,(i+1)*partition-1);//caution if partition=0, end will be -1. 
				Thread t=new Thread(sp);
				t.start();
				sps.add(sp);
				threads.add(t);
			}
//			System.out.println(huffCode);//
			for(Thread t:threads){
				t.join();
			}
			for(int i=0;i<threadNum;i++){
				int indexPosition=sps.get(i).getPosition();
				if(indexPosition!=-1){
					outBuff[outBytePosition] = groupCode.get(huffCode.length() - minLen).get(indexPosition).getV();
					huffCode = "";
					outBytePosition++;
					if (outBytePosition > outBuff.length - 1) {
						outBytePosition = 0;
						write();
					}
					break;
				}
			}
			
			
			
			index++;
		}

		index = 0;/// this is the problem that only write a little
        System.out.println("256 byte done.");
		fileBitLen = fileBitLen - len;
		System.out.println("One round decode Time cost: "+(System.currentTimeMillis()-timer)+"mili seconds");//test
		timer=System.currentTimeMillis();
		return huffCode;
	}
	private void printAllCode(){
		for(FreV v:code){
			System.out.println("Char: "+(char)v.getV()+" Huff: "+v.getHuff());
		}
	}
}
