package com.BlogSpace;
import org.springframework.stereotype.Service;
import java.util.*; 
import java.util.concurrent.ConcurrentHashMap;
@Service
public class CaptchaService {
  private Map<String,Integer> store=new ConcurrentHashMap<>();
  private Random rand=new Random();
  public Map<String,String> generate(){
    int a=rand.nextInt(10)+1, b=rand.nextInt(10)+1;
    String id=UUID.randomUUID().toString();
    store.put(id,a+b);
    return Map.of("captchaId",id,"question",a+" + "+b+" = ?");
  }
  public boolean validate(String id,String ans){
    if(id==null||ans==null) return false;
    Integer c=store.remove(id);
    try{return c!=null && c==Integer.parseInt(ans);}catch(Exception e){return false;}
  }
}
