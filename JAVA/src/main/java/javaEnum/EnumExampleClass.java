package javaEnum;

public class EnumExampleClass {
    public void nonEnumExample0(){
        /*
            MONDAY = 1
            TUESDAY = 2
            WEDNESDAY = 3
            THURSDAY = 4
            FRIDAY = 5
            SATURDAY = 6
            SUNDAY = 7
        */
        int day = 1;
        switch (day){ // 가독성이 좋지않음
            case 1:
                System.out.println("월요일 입니다.");
                break;
            case 2:
                System.out.println("화요일 입니다.");
                break;
            case 3:
                System.out.println("수요일 입니다.");
                break;
            case 4:
                System.out.println("목요일 입니다.");
                break;
            case 5:
                System.out.println("금요일 입니다.");
                break;
            case 6:
                System.out.println("토요일 입니다.");
                break;
            case 7:
                System.out.println("일요일 입니다.");
                break;
        }
    }
    private final static int MONDAY = 1;
    private final static int TUESDAY = 2;
    private final static int WEDNESDAY = 3;
    private final static int THURSDAY = 4;
    private final static int FRIDAY = 5;
    private final static int SATURDAY = 6;
    private final static int SUNDAY = 7;
    public void nonEnumExample1(){
        int day = MONDAY;
        switch(day){ // 가독성 향상됨. 하지만 더 많은 조건이 붙을 경우(ex: 달) 코드가 길어지고 복잡해짐
            case MONDAY:
                System.out.println("월요일 입니다.");
                break;
            case TUESDAY:
                System.out.println("화요일 입니다.");
                break;
            case WEDNESDAY:
                System.out.println("수요일 입니다.");
                break;
            case THURSDAY:
                System.out.println("목요일 입니다.");
                break;
            case FRIDAY:
                System.out.println("금요일 입니다.");
                break;
            case SATURDAY:
                System.out.println("토요일 입니다.");
                break;
            case SUNDAY:
                System.out.println("일요일 입니다.");
                break;
        }
    }
    public void nonEnumExample2(){
        // 데이터를 인터페이스로 분리 및 관리하여 기존 속성은 가져가고 더 간결하게 코드 작성, 관리 가능

        if(DAY.MONDAY == MONTH.JANUARY){ // 일 과 월은 비교가 되면 안되는게 정상. 하지만 같은 타입이라 비교가 가능함.
            System.out.println("두 상수는 같습니다.");
        }
        int day = DAY.MONDAY; // 만약 여기에 1월이라는 값이 들어가도 "월요일 입니다."가 출력 되므로 일과 달이 호환되는 것을 막아야함.
        switch(day){
            case DAY.MONDAY:
                System.out.println("월요일 입니다.");
                break;
            case DAY.TUESDAY:
                System.out.println("화요일 입니다.");
                break;
            case DAY.WEDNESDAY:
                System.out.println("수요일 입니다.");
                break;
            case DAY.THURSDAY:
                System.out.println("목요일 입니다.");
                break;
            case DAY.FRIDAY:
                System.out.println("금요일 입니다.");
                break;
            case DAY.SATURDAY:
                System.out.println("토요일 입니다.");
                break;
            case DAY.SUNDAY:
                System.out.println("일요일 입니다.");
                break;
        }
    }
    public void nonEnumExample3(){
//        if(DayClass.MONDAY == MonthClass.JANUARY){ // 이젠 이렇게 비교 하면, 컴파일러에서 오류 체크 가능해짐
//            System.out.println("두 상수는 같습니다.");
//        }

        DayClass day = DayClass.MONDAY;
//        switch(day){ // 위에서 에러는 걸러졌지만, 실행을 할 수 없음. 상수로 사용하려 했으나 제한이 생김.
//            case DAY.MONDAY:
//                System.out.println("월요일 입니다.");
//                break;
//            case DAY.TUESDAY:
//                System.out.println("화요일 입니다.");
//                break;
//            case DAY.WEDNESDAY:
//                System.out.println("수요일 입니다.");
//                break;
//            case DAY.THURSDAY:
//                System.out.println("목요일 입니다.");
//                break;
//            case DAY.FRIDAY:
//                System.out.println("금요일 입니다.");
//                break;
//            case DAY.SATURDAY:
//                System.out.println("토요일 입니다.");
//                break;
//            case DAY.SUNDAY:
//                System.out.println("일요일 입니다.");
//                break;
//        }
    }

    public void withEnumExample(){
        Day day = Day.MONDAY; // EnumExample.java 파일안에 존재하는 Day, Month enum 코드에서 가져옴
//        if(Day.MONDAY == Month.JANUARY){ // 마찬가지로 두 값은 비교가 안되므로 후에 에러가 날 일이 없음.
//            System.out.println("두 값이 같습니다.");
//        }
        switch(day){ // 가독성 향상됨. 원하는 타입으로 잘 실행이 됨.
            case MONDAY:
                System.out.println("월요일 입니다.");
                break;
            case TUESDAY:
                System.out.println("화요일 입니다.");
                break;
            case WEDNESDAY:
                System.out.println("수요일 입니다.");
                break;
            case THURSDAY:
                System.out.println("목요일 입니다.");
                break;
            case FRIDAY:
                System.out.println("금요일 입니다.");
                break;
            case SATURDAY:
                System.out.println("토요일 입니다.");
                break;
            case SUNDAY:
                System.out.println("일요일 입니다.");
                break;
        }
    }
    public void enumExample(){

        // enum 안에 모든 상수를 가져오기 : values()
        Month[] months = Month.values();
        for(Month month : months){
            System.out.println(month);
        }

        // enum 안에 전달된 문자열과 일치하는 상수를 반환하기 : valueOf
        Month month = Month.valueOf("OCTOBER");
        System.out.println(month);

        // enum 안에 상수가 몇번째 위치에 있는지 확인하기(0부터 시작) : ordinal()
        int monthIdx = Month.OCTOBER.ordinal();
        System.out.println(monthIdx);

        // if문 대신 사용하기
        SwitchEnum switchEnum = SwitchEnum.ON;
        if(switchEnum == SwitchEnum.ON){
            System.out.println("전원을 On 합니다.");
        }
        else{
            System.out.println("전원을 Off 합니다.");
        }
    }
}

interface DAY{
    int MONDAY = 1;
    int TUESDAY = 2;
    int WEDNESDAY = 3;
    int THURSDAY = 4;
    int FRIDAY = 5;
    int SATURDAY = 6;
    int SUNDAY = 7;
}
interface MONTH {
    int JANUARY = 1;
    int FEBRUARY = 2;
    int MARCH = 3;
    int APRIL = 4;
    int MAY = 5;
    int JUNE = 6;
    int JULY = 7;
    int AUGUST = 8;
    int SEPTEMBER = 9;
    int OCTOBER = 10;
    int NOVEMBER = 11;
    int DECEMBER = 12;
}
class DayClass{
    public final static DayClass MONDAY = new DayClass();
    public final static DayClass TUESDAY = new DayClass();
    public final static DayClass WEDNESDAY = new DayClass();
    public final static DayClass THURSDAY = new DayClass();
    public final static DayClass FRIDAY = new DayClass();
    public final static DayClass SATURDAY = new DayClass();
    public final static DayClass SUNDAY = new DayClass();
}
class MonthClass{
    public final static MonthClass JANUARY = new MonthClass();
    public final static MonthClass FEBRUARY = new MonthClass();
    public final static MonthClass MARCH = new MonthClass();
    public final static MonthClass APRIL = new MonthClass();
    public final static MonthClass MAY = new MonthClass();
    public final static MonthClass JUNE = new MonthClass();
    public final static MonthClass JULY = new MonthClass();
    public final static MonthClass AUGUST = new MonthClass();
    public final static MonthClass SEPTEMBER = new MonthClass();
    public final static MonthClass OCTOBER = new MonthClass();
    public final static MonthClass NOVEMBER = new MonthClass();
    public final static MonthClass DECEMBER = new MonthClass();
}