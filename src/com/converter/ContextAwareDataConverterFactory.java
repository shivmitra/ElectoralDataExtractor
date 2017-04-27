package com.converter;

import java.util.HashMap;
import java.util.HashSet;

public class ContextAwareDataConverterFactory {
	private final HashMap<Context,HashSet<IDataConverter> > m;
	private Context activeContext;
	private final HashSet<Context> context;
	private static ContextAwareDataConverterFactory me;
	
	private ContextAwareDataConverterFactory(){
		m = new HashMap<Context,HashSet<IDataConverter> >();
		activeContext = null;
		context = new HashSet<Context>();
	}
	
	public static ContextAwareDataConverterFactory getInstance(){
		if(me == null)
			me = new ContextAwareDataConverterFactory();
		return me;
	}
	
	public void register(Context ctx, IDataConverter conv){
		if(m.containsKey(ctx) == false){
			m.put(ctx,new HashSet<IDataConverter>());
		}
		m.get(ctx).add(conv);
	}
	
	public IDataConverter getDataConverter(String data){
		
		if(activeContext != null){
			HashSet<IDataConverter> list = m.get(activeContext);
			if(list == null)
				return null;
			
			for(IDataConverter conv : list){
				IDataConverter newconv = conv.getNewInstance(data);
				if(newconv.isConvertable())
					return newconv;
			}
		}
		return null;
	}

	public void clearActiveContext(){
		activeContext = null;
	}
	
	public void clearAllContexts(){
		context.clear();
		clearActiveContext();
	}
	
	public void addContexts(Context[] list){
		for(int i=0;i<list.length;i++)
			context.add(list[i]);
	}
	
	public Context setActiveContext(String data) {
		//If the data contains the context and context is one we are interested in, add it to activeContext
		for(Context ctx : context){
			if(ctx.switchOnContext(data)){
				activeContext = ctx;
				return ctx;
			}
		}
		return null;
	}
	
	public Context setActiveContext(Context ctx) {
		//If the data contains the context and context is one we are interested in, add it to activeContext
		if(context.contains(ctx)){
			activeContext = ctx;
			return ctx;
		}
		return null;
	}
	
	public Context getActiveContext() {
		//If the data contains the context and context is one we are interested in, add it to activeContext
		return activeContext;
	}
}
