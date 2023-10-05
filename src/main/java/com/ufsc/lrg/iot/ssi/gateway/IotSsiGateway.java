/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.ufsc.lrg.iot.ssi.gateway;



import com.ufsc.lrg.iot.ssi.gateway.broker.SubscribeMqtt;
import com.ufsc.lrg.iot.ssi.gateway.controller.Controller;
import java.io.IOException;
import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IotSsiGateway {

	public static void main(String[] args) throws IOException, Exception {
            SpringApplication.run(IotSsiGateway.class, args);
            SubscribeMqtt subscribeBrokerMqtt = new SubscribeMqtt();
            subscribeBrokerMqtt.processMqttData();
            Controller controller = new Controller();

               
            boolean running = true;
            Scanner scanner = new Scanner(System.in);
            String command;
        
            while (running) {
                printHelp();
                command = scanner.nextLine();
                switch (command) {
                    case "1":
                        boolean submenu1Running = true;
                        while (submenu1Running) {
                            printHelp2();
                            command = scanner.nextLine();
                            switch (command) {
                                case "1":
                                    controller.createinvitation();
                                    break;
                                case "2":
                                    System.out.println("Informe o InvitationID");
                                    String invitationId = scanner.nextLine();
                                    System.out.println("Informe o Routing key");
                                    String routingkey = scanner.nextLine();
                                    controller.receiveInvitation(invitationId, routingkey);
                                    break;
                                case "0":
                                    submenu1Running = false; // Sai do submenu 1
                                    break;
                                default:
                                    System.out.println("Opção inválida");
                                    break;
                            }
                        }
                        break;
                    case "2":
                        controller.getConnections();
                        break;
                    case "3":
                        controller.listAll();
                        break;    
                    case "4":
                        controller.getCredentialsExchange();
                        //baseAgent.getAllCredentials();
                        break;
                    case "5":
                        String cred_def = "";
                        String conn = "";
                        controller.credentialsIssueV1(cred_def, conn);
                        break;
                    case "6":
                        boolean submenu2Running = true;
                        while (submenu2Running) {
                            printHelp3();
                            command = scanner.nextLine();
                            switch (command) {
                                case "1":
                                    controller.getSchemas();
                                    break;
                                case "2":
                                    controller.getCredentialsDefinitions();
                                    break;
                                case "3":
                                    System.out.println("Informe o nome do Schema");
                                    String schemaName = scanner.nextLine();
                                    System.out.println("Informe o nome a versao");
                                    String schemaVersion = scanner.nextLine();
                                    String didSchema = "ZgWxqztrNooG92RXvxSCAV";
                                    controller.createSchema(schemaName, schemaVersion, didSchema);
                                    break;
                                case "4":
                                    System.out.println("Informe o id do Schema");
                                    String schemaId = scanner.nextLine();
                                    controller.CreateCredentialDefinition(schemaId);
                                    break;
                                case "5":
                                    // Lógica para definir uma credencial
                                    break;
                                 case "6":
                                    // controller.createDid();
                                    //controller.registerDIDDevice("00:00:00:11:11:11");
                                    break;
                                     case "7":
                                     controller.getDid();
                                    break;
                                case "0":
                                    submenu2Running = false; // Sai do submenu
                                    break;
                                default:
                                    System.out.println("Opção inválida");
                                    break;
                            }
                        }
                        break;
                    case "0":
                        running = false;
                        break;
                    default:
                        throw new AssertionError();
                }
            }

        }
    


            private static void printHelp() {
                System.out.println("Instrucoes");
                System.out.println("Para se conectar a um agente digite: 1 \n" +
                        "Para listar conexões ativas digite: 2\n" +
                        "Para listar evidencias salvas digite: 3\n" +
                        "Para listar as credenciais emitidas digite: 4\n" +             
                        "Para emitir uma credencial digite: 5\n" +
                        "Outras opções: 6\n" +
                        "Para sair digite: 0");
            }

            private static void printHelp2() {
                System.out.println("Instrucoes");
                System.out.println("Para gerar um convite digite: 1 \n" +
                        "Para receber um convite digite: 2\n" +
                        "Para voltar digite: 0");
            }

            private static void printHelp3() {
                System.out.println("Instrucoes");
                System.out.println("\"Para listar os schemas gravados digite: 1 \n" +
                        "Para listar as definições de credenciais digite: 2\n" +
                        "Para criar um schema digite: 3\n" +
                        "Para criar uma definição de credencial digite: 4\n" +
                        "Para listar credenciais salvas na carteira: 5\n" +
                        "Para criar did: 6\n" +
                        "Para listar did: 7\n" +
                        "Voltar digite: 0");
            }

        }
	

