package it.polito.tdp.ufo.model;

public class Arco {

	private Stato source;
	private Stato dest;
	
	public Arco( Stato s1, Stato s2) {
		this.source = s1;
		this.dest = s2;
	}

	public Stato getSource() {
		return source;
	}

	public void setSource(Stato source) {
		this.source = source;
	}

	public Stato getDest() {
		return dest;
	}

	public void setDest(Stato dest) {
		this.dest = dest;
	}
	
	
}
