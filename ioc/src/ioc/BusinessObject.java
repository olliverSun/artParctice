package ioc;
@Component(id="businessObject")
public class BusinessObject {
 
    @Autowire(id="dataAccessInterface")
    private DataAccessInterface dai;
    
    public void print() {
        System.out.println(dai.queryFromTableA());
    }
    
    public void setDai(DataAccessInterface dai) {
        this.dai = dai;
    }
}