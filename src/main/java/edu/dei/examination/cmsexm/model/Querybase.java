package edu.dei.examination.cmsexm.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({

@NamedQuery (name="getParentMenuItems",
	//query="SELECT parent.id as id, parent.name as name, parent.lft as lft , "
	//		+ " parent.rgt as rgt , parent.route  as route FROM Menu AS node, Menu AS parent" +
query="SELECT  parent "
		+ "  FROM Menu AS node, Menu AS parent" + 
		
        " WHERE node.lft BETWEEN parent.lft AND parent.rgt"+
        " AND node.id in (select menu_id FROM Role_Menu  WHERE role_id = :role_id )"+
        " and parent.id > 1 and parent.lft+1 <>  parent.rgt"+
        " group by parent.id order by parent.lft"
		),
		
@NamedQuery (name="getChildrenMenuItems",
	query ="Select  node "+
		" FROM Menu AS node,Menu AS parent"+
	    " WHERE node.lft BETWEEN parent.lft AND parent.rgt"+
		" and node.rgt <parent.rgt"+
		" AND node.id in (select menu_id FROM Role_Menu  WHERE role_id = :role_id )"+
	    " AND parent.id = :MenuId "+
		" ORDER BY node.lft"
	)}

		)
public class Querybase {
	 @Id
     private long id;
}
