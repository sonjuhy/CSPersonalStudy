package tryException;

import java.io.PrintWriter;
import java.io.StringWriter;

public class tryException {
    public void start(){
        printExceptionMessage();
    }

    public void defaultException(){
        try{
            // TODO : running code
            System.out.println("running code");
        }
        catch(Exception e){
            e.getMessage(); // 예외 내용 반환
            e.printStackTrace(); // 예외 발생 위치 및 내용 표시
        }
        finally {
            System.out.println("finally : 무조건 실행");
        }
    }

    public void printExceptionMessage(){
        int[] errorArr = new int[2];
        try{
            errorArr[2] = 1;
        }
        catch(Exception e){
            System.out.println(e);
            System.out.println(e.getMessage());
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();

            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String error = sw.toString();
            System.out.println(error); // printStackTrace to String
        }
    }
}
