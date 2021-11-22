package edu.dei.examination.cmsexm.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.dei.examination.cmsexm.model.Login;
import edu.dei.examination.cmsexm.model.Menu;
import edu.dei.examination.cmsexm.model.User;
import edu.dei.examination.cmsexm.repository.UserRepository;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	
	@Autowired
	UserRepository userRepository; 
	
	@Autowired
	EntityManager em;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

		return UserDetailsImpl.build(user);
	}


	
	
	
	
	public JSONArray getNewMenu(Login login){
		
		//List<Login> ParentList=getSqlMapClientTemplate().queryForList("login.getParentMenuItems",login);

		
		int role_id = 1;
		List<Menu> ParentList =(List<Menu>)em.createNamedQuery("getParentMenuItems",Menu.class )
		.setParameter("role_id", role_id)
		.getResultList();
						
		JSONArray mainary = new JSONArray();
		JSONArray zerolvlchildren = new JSONArray();
		
		
		
		JSONObject zerolvlparent = new JSONObject();
		
		
	
		ArrayList ary1 = new ArrayList<String>(); 
		
	
		
	
		int prvleft =0;
		int prvright =0;
				
		for (int i=0 ;i<ParentList.size();i++){
			
			
			
			zerolvlparent = new JSONObject();
			
			zerolvlchildren=new JSONArray();
			
			
			if(ary1.contains(ParentList.get(i).getId())){
				System.out.println("inside 0 level");
				continue;
			}
			
			
			login.setMenuId(ParentList.get(i).getId());
			zerolvlchildren=getzerolvlchildren(login,ary1)  ;          	
            
			

			
//			if(firstlvlchildren.length()>0&&secondlvlchildren.length()<1){
//				zerolvlparent.put("displayname", ParentList.get(i).getMenuName());
//			
//				zerolvlparent.put("children",firstlvlparent);
//				
//				mainary.put(zerolvlparent);
//			}
			//if(zerolvlchildren.length()>0&&firstlvlchildren.length()<1){
				if(zerolvlchildren.length()>0){
				zerolvlparent.put("displayname", ParentList.get(i).getName());

				zerolvlparent.put("children",zerolvlchildren);
				mainary.put(zerolvlparent);
			}
			
			prvleft =ParentList.get(i).getLft();
     		prvright =ParentList.get(i).getRgt();
			
				
		}
		
		return mainary;
	}

private  JSONArray getzerolvlchildren(Login login,ArrayList ary1){

	int [] ary = new int[100];
	
	int aryidx =0;
    JSONArray zerolvlchildren = new JSONArray();
    JSONArray nxtchildren = new JSONArray();
   
    JSONObject	jsonobj = new JSONObject();
	//List<Login> childrenlist=getSqlMapClientTemplate().queryForList("login.getChildrenMenuItems",login);
	
	List<Menu> childrenlist =em.createNamedQuery("getChildrenMenuItems")
	.setParameter("role_id",1)
	.setParameter("MenuId", login.getMenuId()).getResultList();
	
	
	
	

	for(int j=0 ;j<childrenlist.size();j++){
		
						
			if(ary1.contains(childrenlist.get(j).getId())){
				System.out.println("Inside first level ");
				continue;
			}
			//ary[aryidx]=Integer.parseInt(childrenlist.get(j).getId());
			ary[aryidx]=childrenlist.get(j).getId();
			aryidx++;
			ary1.add((childrenlist.get(j).getId()));

		// if it is a first level parent
		if(childrenlist.get(j).getLft()+1!=childrenlist.get(j).getRgt()){
			    
             login.setMenuId(childrenlist.get(j).getId());
             nxtchildren=getfirstlevelchildren(login,ary1);
             jsonobj = new JSONObject();
             jsonobj.put("displayname", childrenlist.get(j).getName());
             jsonobj.put("children", nxtchildren);
             zerolvlchildren.put(jsonobj);
             
             
			  
		}else{
			jsonobj = new JSONObject();
			jsonobj.put("displayname", childrenlist.get(j).getName());
			jsonobj.put("route", childrenlist.get(j).getRoute());
			zerolvlchildren.put(jsonobj);
		
		}
		
		//firstlvlparent.put("display",(childrenlist.get(j).getMenuName()));
	}
			
		//zerolvlchildren.put(firstlvlparent);	
		return zerolvlchildren;
}
	
private  JSONArray getfirstlevelchildren(Login login,ArrayList ary1){

	int [] ary = new int[100];
	
	int aryidx =0;
    JSONArray firstlvlchildren = new JSONArray();
    JSONArray nxtchildren = new JSONArray();
   
    JSONObject	jsonobj = new JSONObject();
	//List<Login> childrenlist=getSqlMapClientTemplate().queryForList("login.getChildrenMenuItems",login);
    List<Menu> childrenlist =em.createNamedQuery("getChildrenMenuItems")
    		.setParameter("role_id",1)
    		.setParameter("MenuId", login.getMenuId()).getResultList(); 

	for(int j=0 ;j<childrenlist.size();j++){
		
						
			if(ary1.contains(childrenlist.get(j).getId())){
				System.out.println("Inside first level ");
				continue;
			}
			//ary[aryidx]=Integer.parseInt(childrenlist.get(j).getId());
			ary[aryidx]=childrenlist.get(j).getId();
			aryidx++;
			ary1.add((childrenlist.get(j).getId()));

		// if it is a Second level parent
		if(childrenlist.get(j).getLft()+1!=childrenlist.get(j).getRgt()){
			    
             login.setMenuId(childrenlist.get(j).getId());
             nxtchildren=getsecondlevelchildren(login,ary1);
             jsonobj = new JSONObject();
             jsonobj.put("displayname", childrenlist.get(j).getName());
             jsonobj.put("children", nxtchildren);
             firstlvlchildren.put(jsonobj);

			  
		}else{
			jsonobj = new JSONObject();
			jsonobj.put("displayname", childrenlist.get(j).getName());
			jsonobj.put("route", childrenlist.get(j).getRoute());
			firstlvlchildren.put(jsonobj);
		
		}
		
		//firstlvlparent.put("display",(childrenlist.get(j).getMenuName()));
	}
			
		//zerolvlchildren.put(firstlvlparent);	
		return firstlvlchildren;
}
	
private  JSONArray getsecondlevelchildren(Login login,ArrayList ary1){

	int [] ary = new int[100];
	
	int aryidx =0;
    JSONArray secondlvlchildren = new JSONArray();
    JSONArray nxtchildren = new JSONArray();
   
    JSONObject	jsonobj = new JSONObject();
	//List<Login> childrenlist=getSqlMapClientTemplate().queryForList("login.getChildrenMenuItems",login);
    List<Menu> childrenlist =em.createNamedQuery("getChildrenMenuItems")
    		.setParameter("role_id",1)
    		.setParameter("MenuId", login.getMenuId()).getResultList();  

	for(int j=0 ;j<childrenlist.size();j++){
		
						
			if(ary1.contains(childrenlist.get(j).getId())){
				System.out.println("Inside first level ");
				continue;
			}
			//ary[aryidx]=Integer.parseInt(childrenlist.get(j).getId());
			ary[aryidx]=childrenlist.get(j).getId();
			aryidx++;
			ary1.add((childrenlist.get(j).getId()));

		// if it is a Third level parent
		if(childrenlist.get(j).getLft()+1!=childrenlist.get(j).getRgt()){
			    
             login.setMenuId(childrenlist.get(j).getId());
             nxtchildren=getthirdlevelchildren(login,ary1);
             jsonobj = new JSONObject();
             jsonobj.put("displayname", childrenlist.get(j).getName());
             jsonobj.put("children", nxtchildren);
             secondlvlchildren.put(jsonobj);
			  
		}else{
			jsonobj = new JSONObject();
			jsonobj.put("displayname", childrenlist.get(j).getName());
			jsonobj.put("route", childrenlist.get(j).getRoute());
			secondlvlchildren.put(jsonobj);
		
		}
		
		//firstlvlparent.put("display",(childrenlist.get(j).getMenuName()));
	}
			
		//zerolvlchildren.put(firstlvlparent);	
		return secondlvlchildren;
}
		
private  JSONArray getthirdlevelchildren(Login login,ArrayList ary1){

	int [] ary = new int[100];
	
	int aryidx =0;
    JSONArray thirdlvlchildren = new JSONArray();
    JSONArray nxtchildren = new JSONArray();
   
    JSONObject	jsonobj = new JSONObject();
	//List<Login> childrenlist=getSqlMapClientTemplate().queryForList("login.getChildrenMenuItems",login);
    List<Menu> childrenlist =em.createNamedQuery("getChildrenMenuItems")
    		.setParameter("role_id",1)
    		.setParameter("MenuId", login.getMenuId()).getResultList();     

	for(int j=0 ;j<childrenlist.size();j++){
		
						
			if(ary1.contains(childrenlist.get(j).getId())){
				System.out.println("Inside first level ");
				continue;
			}
			//ary[aryidx]=Integer.parseInt(childrenlist.get(j).getMenuId());
			ary[aryidx]=childrenlist.get(j).getId();
			aryidx++;
			ary1.add((childrenlist.get(j).getId()));

		// if it is a fourth level parent
		if(childrenlist.get(j).getLft()+1!=childrenlist.get(j).getRgt()){
			    
             login.setMenuId(childrenlist.get(j).getId());
             nxtchildren=getfourthlevelchildren(login,ary1);
             jsonobj = new JSONObject();
             jsonobj.put("displayname", childrenlist.get(j).getName());
             jsonobj.put("children", nxtchildren);
             thirdlvlchildren.put(jsonobj);
			  
		}else{
			jsonobj = new JSONObject();
			jsonobj.put("displayname", childrenlist.get(j).getName());
			jsonobj.put("route", childrenlist.get(j).getRoute());
			
			thirdlvlchildren.put(jsonobj);
		
		}
		
		//firstlvlparent.put("display",(childrenlist.get(j).getMenuName()));
	}
			
		//zerolvlchildren.put(firstlvlparent);	
		return thirdlvlchildren;
}
	
private  JSONArray getfourthlevelchildren(Login login,ArrayList ary1){

	int [] ary = new int[100];
	
	int aryidx =0;
    JSONArray fourthlvlchildren = new JSONArray();
    JSONArray nxtchildren = new JSONArray();
   
    JSONObject	jsonobj = new JSONObject();
	//List<Login> childrenlist=getSqlMapClientTemplate().queryForList("login.getChildrenMenuItems",login);
    List<Menu> childrenlist =em.createNamedQuery("getChildrenMenuItems")
    		.setParameter("role_id",1)
    		.setParameter("MenuId", login.getMenuId()).getResultList();     

	for(int j=0 ;j<childrenlist.size();j++){
		
						
			if(ary1.contains(childrenlist.get(j).getId())){
				System.out.println("Inside first level ");
				continue;
			}
			//ary[aryidx]=Integer.parseInt(childrenlist.get(j).getMenuId());
			ary[aryidx]=childrenlist.get(j).getId();
			aryidx++;
			ary1.add((childrenlist.get(j).getId()));

		// if it is a fourth level parent
		if(childrenlist.get(j).getLft()+1!=childrenlist.get(j).getRgt()){
			    
             login.setMenuId(childrenlist.get(j).getId());
            // nxtchildren=getfivthlevelchildren(login,ary1);
			  
		}else{
			jsonobj = new JSONObject();
			jsonobj.put("displayname", childrenlist.get(j).getName());
			jsonobj.put("route", childrenlist.get(j).getRoute());
			
			fourthlvlchildren.put(jsonobj);
		
		}
		
		//firstlvlparent.put("display",(childrenlist.get(j).getMenuName()));
	}
			
		//zerolvlchildren.put(firstlvlparent);	
		return fourthlvlchildren;
}
			
	
//  private getdefaultrole() {
//	  
//  }
//	
}
