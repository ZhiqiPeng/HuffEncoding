import java.util.ArrayList;

public class SearchProcess implements Runnable{
	private ArrayList<FreV> list;
	private String huffCode;
    private int start;
    private int end;
    private int position=-1;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		for(int i=start;i<end+1;i++){
			if(list.get(i).getHuff().equals(huffCode)){
				position=i;
//				System.out.println("SearchFound.");
				return;
			}
		}
	}
	public SearchProcess(ArrayList<FreV> list,String huffCode,int start,int end){//include start and end
		this.list=list;
		this.huffCode=huffCode;
		this.start=start;
		if(end>list.size()-1){
			this.end=list.size()-1;
		}else{
			if(end<0){
			   this.end=list.size()-1;//Due to partition 0, this could be any number not only zero!
			}else{
				this.end=end;
			}
		}
	}
	public int getPosition(){
		return position;
	}

}
