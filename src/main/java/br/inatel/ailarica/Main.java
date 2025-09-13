package br.inatel.ailarica;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UsuarioService usuarioService = new UsuarioService();

        System.out.println("1 - Cadastro");
        System.out.println("2 - Login");
        int opcao = scanner.nextInt();
        scanner.nextLine();

        if (opcao == 1) {
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Senha: ");
            String senha = scanner.nextLine();

            boolean sucesso = usuarioService.cadastrar(nome, email, senha);
            if (sucesso) {
                System.out.println("Usuário cadastrado.");
            } else {
                System.out.println("Email já existe.");
            }

        } else if (opcao == 2) {
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Senha: ");
            String senha = scanner.nextLine();

            Usuario usuario = usuarioService.login(email, senha);
            if (usuario != null) {
                System.out.println("Login bem-sucedido. Bem-vindo, " + usuario.getNome() + "!");
            } else {
                System.out.println("Email ou senha incorretos.");
            }
        }

        scanner.close();
    }
}