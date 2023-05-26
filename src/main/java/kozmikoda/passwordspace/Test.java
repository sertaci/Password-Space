package kozmikoda.passwordspace;

public class Test {

    public static void main(String[] args) {
        try {

            ResetCodeSender.sendViaSMS("deneme mesagi", "5457142292");

        }catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void printUser(MainUserAccount m) {
        System.out.printf("%s %s %s %s \n", m.getUserName(), m.getRealName(), m.getEMail(), m.getPhoneNumber()) ;
    }

}
