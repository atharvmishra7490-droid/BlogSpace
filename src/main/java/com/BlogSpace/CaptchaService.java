papackage com.BlogSpace;
import org.springframework.stereotype.Service;
import java.util.*; 
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CaptchaService {
  private Map<String,Integer> store=new ConcurrentHashMap<>();
  private Random rand=new Random();
  
  // Stores with expiry time (5 min)
  private Map<String,Long> expiry = new ConcurrentHashMap<>();

  public Map<String,String> generate(){
    int a = rand.nextInt(20)+1; // 1-20 more pro
    int b = rand.nextInt(20)+1;
    int type = rand.nextInt(3); // 0:+, 1:-, 2:+
    
    String question;
    int answer;
    
    if(type==1 && a < b) { // avoid negative for simple
      int temp=a; a=b; b=temp;
    }
    
    if(type==0) {
      question = a+" + "+b+" = ?";
      answer = a+b;
    } else if(type==1) {
      question = a+" - "+b+" = ?";
      answer = a-b;
    } else {
      a = rand.nextInt(10)+1; // smaller for multiply
      b = rand.nextInt(10)+1;
      question = a+" × "+b+" = ?";
      answer = a*b;
    }
    
    String id = UUID.randomUUID().toString();
    store.put(id, answer);
    expiry.put(id, System.currentTimeMillis() + 300000); // 5 min expiry
    
    // Cleanup old
    cleanup();
    
    return Map.of("captchaId", id, "question", question);
  }
  
  public boolean validate(String id, String ans){
    if(id==null||ans==null) return false;
    
    Long exp = expiry.get(id);
    if(exp!=null && System.currentTimeMillis() > exp) {
      store.remove(id); expiry.remove(id);
      return false;
    }
    
    Integer correct = store.remove(id);
    expiry.remove(id);
    
    try{
      return correct!=null && correct==Integer.parseInt(ans.trim());
    }catch(Exception e){return false;}
  }
  
  private void cleanup() {
    long now = System.currentTimeMillis();
    expiry.entrySet().removeIf(e -> {
      if(now > e.getValue()) {
        store.remove(e.getKey());
        return true;
      }
      return false;
    });
  }
}