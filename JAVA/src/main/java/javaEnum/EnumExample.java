package javaEnum;

public enum EnumExample {
    ONE(1), TWO(2), THREE(3);
    private int num;
    EnumExample() {} // 생성자는 private 속성이 기본
//    public EnumExample() {} // public 속성 부여시, 에러 발생. -> 열거형이기에 동적으로 변경되는것을 허용 안함.(final 특징 띔)
    EnumExample(int num){
        this.num = num;
    }
    public int getNum(){return num;}
}

enum SwitchEnum{ // fianl과 같은 특징으로 싱글톤 디자인 패턴으로 사용 가능함.
    ON("켜짐"), OFF("꺼짐");
    private String name;
    SwitchEnum(){}
    SwitchEnum(String name){
        this.name = name;
    }
    public SwitchEnum changeSwitchStatus(){
        if(this == SwitchEnum.ON){
            return SwitchEnum.OFF;
        }
        else{
            return SwitchEnum.ON;
        }
    }
}

enum Day{
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;
}
enum Month{
    JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER ,NOVEMBER ,DECEMBER;
}
