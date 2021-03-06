package data;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 
 * @author Mattias Stjernstedt, Damir Pervan, Johan Trygg
 *
 */
@Entity
public class Blog implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue
	private int blogId;
	private String name;
	private int userId;
	private int postId;


}