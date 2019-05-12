package operate;

public class Cnt {
	int cn=0;

	public int getCn() {
		return cn;
	}

	public void setCn(int cn) {
		this.cn = cn;
	}
	
	public void add(int x){
		cn+=x;
	}
}
