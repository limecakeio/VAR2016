package var.rmi.currency;

@SuppressWarnings("serial")
public class Geld implements java.io.Serializable {

	private Integer geldbetrag;
	private Waehrung waehrung;
	
	/**
	 * Geld
	 *
	 *@author Richard Vladimirskij
	 * 
	 * A Geld-Object has an amount (Geldbetrag) and a currency code (Waehrung).
	 * @param - geldbetrag, expexts the amount of Geld as an Integer object.
	 * @param - waehrung, expects the currency code as an Waehrung enumeration value.
	 * */
	Geld(Integer geldbetrag, Waehrung waehrung){
		this.geldbetrag = geldbetrag;
		this.waehrung = waehrung;
	}
	
	/**
	 * @return the Geld's Geldbetrag as an Integer object.
	 * */
	public int getGeldbetrag() {
		return geldbetrag;
	}
	
	/**
	 * Sets the Geld's Geldbetrag
	 * @param geldbetrag - the Geldbetrag as an Integer object.
	 * */
	public void setGeldbetrag(Integer geldbetrag){
		this.geldbetrag = geldbetrag;
	}
	
	/**
	 * @return the Geld's Waehrung as a Waehrung
	 * */
	public Waehrung getWaehrung() {
		return waehrung;
	}
	
	/**
	 * Sets the Geld's Waehrung
	 * @param waehrung - expects the Waehrung as a type of waehrung
	 * */
	public void setWaehrung(Waehrung waehrung){
		this.waehrung = waehrung;
	}
	
	/**
	 * Outputs the Geld onto the console
	 * @return 
	 * */
	public String toString() {
		return geldbetrag + " " + waehrung;
	}
}
