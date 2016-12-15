import java.util.ArrayList;
import java.io.*;
import java.util.*;


public class symboltable{
    private static ArrayList<symbolScope> scopeAlist;
    private static int block_count = 0;
    private static boolean isError = false;
    public static boolean is_AddBlock = false;
    public symboltable(){
	scopeAlist = new ArrayList <symbolScope>();
	block_count = 0;
    }

    public static void tinySymbols(){
	//	symbolScope scope = scopeAlist.remove(scopeAlist.size()-1);
	//System.out.println(scope.getscopeName());
	//	scope.tinyPrint();
	int i = 0;
	for(i=0;i<1;i++){
	    scopeAlist.get(i).tinyPrint();
	}
	//	scopeAlist.add(scope);
    }

    public static symbol getSymbol(String name){
	//	int i=0;
	symbol result = null;
	/*	printTable();
	for(i=0;i<scopeAlist.size();i++){
	    // scopeAlist.get(i).printSymbols();
	    result = scopeAlist.get(i).scopeMap.get(name);
	    if(!(result == null)){
		return result;
	    }
	}*/
	if(scopeAlist.get(0).contains(name)){
	    //System.out.println(name+" is in global");
	    result = scopeAlist.get(0).getSymbol(name);
	
	}
	else{
	    //System.out.println(name+" is local var ");
	    result = NodeManager.topFunction().getSymbol(name);
	    // System.out.println("function name: "+NodeManager.topFunction().name);
	    scopeAlist.get(0).printSymbols();
	}
	return result;
    }

    public static boolean isGlobalString(String name){
	symbol sym = null;
	if(scopeAlist.get(0).contains(name)){
	    //System.out.println(name+" is in global");
	    if(scopeAlist.get(0).scopeMap.get(name).gettype().equals("STRING")){
		return true;
	    }
	}
	return false; 
    }


    public static String getType(String Name){
	//System.out.println("naem: "+Name);
	symbol Sym = getSymbol(Name);

	//	System.out.println(Name);


	if(Sym.gettype().equals("FLOAT"))
	    return "F";
	else if (Sym.gettype().equals("INT"))
	    return "I";
	else if (Sym.gettype().equals("STRING"))
	    return "S";

	return "";
	
    }

    public static void addsymbolScope(String newscope){
	if(isError){ return;}
	//System.out.println(">>"+newscope);
	if(newscope.equals("GLOBAL")){
	    scopeAlist = new ArrayList<symbolScope>();
	}

	if(newscope.equals("BLOCK")){
	    block_count++;
	    scopeAlist.add(new symbolScope("BLOCK "+Integer.toString(block_count)));
	}
	else{
	    scopeAlist.add(new symbolScope(newscope));
	}
    }
    
    public static void popsymbolScope(){
	if(isError){ return;}
	//	return;///*delete if needed------------------------------
	symbolScope topscope;
	if(!scopeAlist.isEmpty()){
	    topscope = scopeAlist.remove(scopeAlist.size()-1);
	    IRnodelist.Addnode(new IRnode("SPILL","","",""));
	    //topscope.printSymbols();
	}//
    }

    public static void addsymbol(String id,String type,String value){
      	//System.out.println(" IN addsymbol: "+id);
	if(isError | is_AddBlock){ 
	    //  System.out.println("inside conditional :"+is_AddBlock);
	    return;
	}
	//	System.out.println("outside conditional :"+is_AddBlock);
	//	printTable();
	symbolScope topscope = scopeAlist.remove(scopeAlist.size()-1);
	String s2 = type;
	
	

	if(topscope.scopeMap.get(id) != null){
	    System.out.println("DECLARATION ERROR "+id);
	    isError = true;
	    return;
		}

	if(value.equals("")){
	    if(type.equals("SAME")){
		s2 = symbol.sameType;
	    }else{
		symbol.sameType = s2;
	    }

	    topscope.addSymbol(id,new symbol(id,s2));
	    if(!(NodeManager.FList.size()== 0))
		{	NodeManager.topFunction().scopeMap.put(id,new symbol(id,s2));}
	    
	}else {
	    
	   
	    topscope.addSymbol(id,new symbol(id,type,value));
	    if(!(NodeManager.FList.size()== 0)){
	      	NodeManager.topFunction().scopeMap.put(id,new symbol(id,s2,value));
	    }
	}
	//System.out.println(id+" "+type+" "+topscope.getscopeName());
	scopeAlist.add(topscope);
	//	System.out.println("print second");
         
    }

    public static void printTable(){
	//System.out.println(";IR code");
	if(isError){ return;}
	int i=0;
	for(i=0;i<scopeAlist.size();i++){
	    if(!scopeAlist.get(i).getscopeName().equals("GLOBAL")){
		//System.out.println();
	    }
	    // System.out.println("Symbol table "+scopeAlist.get(i).getscopeName());
	    // scopeAlist.get(i).printSymbols();
	    //System.out.println();
	}
    }

}
