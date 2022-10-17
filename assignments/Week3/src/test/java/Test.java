import edu.whu.context.utils.ScannerUtil;

public class Test {

    @org.junit.jupiter.api.Test
    public void test01(){
        ScannerUtil.getAllClass("edu.entity").forEach(t-> System.out.println(t));

    }


}