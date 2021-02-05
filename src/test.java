import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class test {

    /*
    Skapad av Robin Heidari, 2021-02-05
    
     */

    public static void main(String[] args) {
        LocalDate date = LocalDate.now();

        LocalDate date2 = LocalDate.now().plusDays(5);

        System.out.println(ChronoUnit.DAYS.between(date,date2));
    }

}
