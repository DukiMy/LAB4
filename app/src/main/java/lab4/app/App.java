package lab4.app;

public class App {

  public static void main(String[] args) {
    CarController cc = new CarController();

    cc.volvo.mutatePoint(0, 0);
    cc.saab.mutatePoint(0, 100);
    cc.scania.mutatePoint(0, 200);

    cc.cars.add(cc.volvo);
    cc.cars.add(cc.saab);
    cc.cars.add(cc.scania);

    cc.frame = new CarView("CarSim 1.0", cc);

    try {
      BufferedImage volvoImg = ImageIO.read(
        DrawPanel.class.getResourceAsStream("/pics/Volvo240.jpg")
      );
      BufferedImage saabImg = ImageIO.read(
        DrawPanel.class.getResourceAsStream("/pics/Saab95.jpg")
      );
      BufferedImage scaniaImg = ImageIO.read(
        DrawPanel.class.getResourceAsStream("/pics/Scania.jpg")
      );

      cc.frame.drawPanel.addVehicle(cc.volvo, volvoImg);
      cc.frame.drawPanel.addVehicle(cc.saab, saabImg);
      cc.frame.drawPanel.addVehicle(cc.scania, scaniaImg);

    } catch (IOException ex) {
      ex.printStackTrace();
    }

    cc.timer.start();
  }
}
