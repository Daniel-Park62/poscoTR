package poc.poscoTR.model;

import java.sql.Timestamp;
import javax.persistence.*;

import org.eclipse.persistence.annotations.ReadOnly;

/**
 * Entity implementation class for Entity: LasTime
 *
 */
@ReadOnly
@Entity
@NamedQuery(name="LasTime.findAll", query="SELECT l FROM LasTime l")
public class LasTime  {

	private static LasTime instance ;
	public static LasTime getInstance() {
		if ( instance == null )
			instance = new LasTime();
		return instance;
	}
	@Id
	private int id;
	private Timestamp lastm;

	private LasTime() {	}   
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}   
	public Timestamp getLastm() {
		return this.lastm;
	}

	public void setLastm(Timestamp lastm) {
		this.lastm = lastm;
	}
   
}
