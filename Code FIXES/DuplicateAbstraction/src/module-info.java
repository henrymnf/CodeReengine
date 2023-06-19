public class Main {
    public static void main(String[] args) {
        Vehicle mercedes = new Mercedes();
        Vehicle ducati = new Ducati();

        mercedes.stock();
        mercedes.model();
        mercedes.color();
        System.out.println();
        ducati.stock();
        ducati.model();
        ducati.color();

        if (ducati instanceof Ducati) {
            Ducati ducatiObj = (Ducati) ducati;
            ducatiObj.cylinderCapacity();
        }
    }
}