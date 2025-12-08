package RMICalculator;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class CalClient {
    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException {


        try {
            RMICalInterface stub = (RMICalInterface) Naming.lookup("rmi://localhost:1099/calServer");

            Scanner sc = new Scanner(System.in);

            System.out.println("Enter the first number: ");
            int a = sc.nextInt();
            System.out.println("Enter the second number: ");
            int b = sc.nextInt();

            System.out.println("Addition" + stub.add(a, b));
            System.out.println(stub.sub(a, b));
            System.out.println(stub.mul(a, b));
            System.out.println(stub.div(a, b));
            System.out.println(stub.mod(a, b));

            sc.close();

//            System.out.println("Choose operation: 1,2,3,4,5");
//            int op = sc.nextInt();
//            switch (op){
//                case 1:
//                    System.out.println(stub.add(a, b));
//                    break;
//                    case 2:
//                        System.out.println(stub.sub(a, b));
//                        break;
//                        case 3:
//                            System.out.println(stub.mul(a, b));
//                            break;
//                            case 4:
//                                System.out.println(stub.div(a, b));
//                                break;
//                                case 5:
//                                        System.out.println(stub.mod(a, b));
//                                        break;
//                                        default:
//                                            System.out.println("Invalid operation");
//            }



        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
