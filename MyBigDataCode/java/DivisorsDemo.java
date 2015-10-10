import java.util.ArrayList;


public class DivisorsDemo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<Long> al = new ArrayList<>();
		long sum = 0;
		long tmp = 0;
		for(long i=2;i<=Math.pow(10,12);i++){
			if(i==2||i==3){
				al.add(i);
				continue;
			}
			for(long j=2;j<=(tmp=(long)Math.sqrt(i));j++){
				if(tmp%j==0){
					continue;
				}
				al.add(j);
			}
		}
		ArrayList<Long> al1 = new ArrayList<>();
		for(long i=2;i<=Math.pow(10, 12);i++){
			al1.add(i);
		}
		al1.removeAll(al);
		for(long l:al1){
			for(long i=2;i<=(tmp=(long)Math.sqrt(l));i++){
				ArrayList<Long> alt = new ArrayList<>();
				for(long l1:al){
					if(l1<=tmp){
						alt.add(l1);
					}else{
						break;
					}
				}
				for(long l2:alt){
					if(l%l2==0){
						long tmp1 = l/l2;
						if(al.contains(tmp1)){
							sum+=Math.pow(l2,1)+Math.pow(tmp1,1);
						}else{
							
						}
					}
				}
			}
		}
	}
	
}
