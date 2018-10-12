package ioc;
@Component(id="dataAccessInterface")
public class DataAccessInterface {
 
    public String queryFromTableA() {
        return "query result";
    }
}