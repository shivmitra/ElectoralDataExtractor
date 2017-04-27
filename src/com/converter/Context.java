package com.converter;

public class Context {
	public final String ctx;
	public Context(String ctx){
		this.ctx = ctx;
	}
	
	@Override
	public int hashCode(){
		return ctx.hashCode();
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null)                
			return false;

	    Context other = (Context) o;
	    return this.ctx.equals(other.ctx);
	}
	
	public boolean switchOnContext(String data){
		return data.startsWith(ctx);
	}
}
